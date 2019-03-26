/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.receive.*;
import ca.uvic.leadlab.obibconnector.impl.receive.mock.ReceiveDocMock;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CDXImport {

    private IReceiveDoc receiver = new ReceiveDocMock();
    ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);

    private String clinicId;

    public CDXImport() {
        clinicId = clinicDao.getClinic().getCdxOid();
    }


    public void importNewDocs() {

        String[] docIds;

        try {

            docIds = receiver.pollNewDocIDs();

            for (String id : docIds) {

                IDocument doc = receiver.retrieveDocument(id);

                storeDocument(doc);

            }
        } catch (Exception e) {
            // log exception and error
        }

    }

    private void storeDocument(IDocument doc) {
        Boolean routed = false;
        DocumentDao docdao = SpringUtils.getBean(DocumentDao.class);
        Document docEntity = new Document();

        docEntity.setDoctype(doc.getTemplateName());
        docEntity.setDocdesc("CDX");
        docEntity.setDocClass(doc.getLoingCodeDisplayName());
        docEntity.setDocxml(doc.getContents());
        docEntity.setDocfilename(doc.getDocumentID());
        docEntity.setRestrictToProgram(false); // need to confirm semantics

        IPerson auth = doc.getAuthor();

        docEntity.setSource(auth != null ? auth.getLastName() : "");
        docEntity.setUpdatedatetime(doc.getAuthoringTime());
        docEntity.setStatus(Document.STATUS_ACTIVE);
        docEntity.setReportStatus(doc.getStatusCode());
        docEntity.setContenttype("text/plain");

        if (doc.getObservationDate() != null) {
            docEntity.setObservationdate(doc.getObservationDate());
        } else
            docEntity.setObservationdate(doc.getAuthoringTime());

        if (doc.getCustodian() != null)
        docEntity.setSourceFacility(doc.getCustodian());

        docEntity.setContentdatetime(doc.getAuthoringTime());
        docdao.persist(docEntity);

        routed = addProviderRouting(docEntity, doc.getPrimaryRecipient());

        for (IProvider p : doc.getSecondaryRecipients()) {
            routed = routed || addProviderRouting(docEntity, p);
        }

        if (!routed) { // even if none of the recipients appears to work at our clinic, we will route to the default provider
            addDefaultProviderRouting(docEntity);
        }

        addPatient(docEntity, doc.getPatient());


        //docEntity.setAppointmentNo();
        // docEntity.setReviewer();
        // docEntity.setReviewdatetime();
        // docEntity.setNumberofpages();
        // docEntity.setDocSubClass();
        // docEntity.setDoccreator(); ** need to confirm intention of this field
        // docEntity.setResponsible();

        //   IProvider recipient = doc.get
        //   Provider p = dao.getProviderByOhipNo(doc.getA)

        // docEntity.setResponsible();


    }


    private String selectIDType(IId[] ids, String type) {

        for (IId i : ids) {
            if (i.getIdType().equals(type))
                return i.getIdCode();
        }

        return null;
    }

    private void addPatient(Document docEntity, IPerson patient) {
        DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
        CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);
        int demoId = -1;

        String hin = selectIDType(patient.getIDs(), "HIN");

        if (hin != null) {
            List<Demographic> demos = demoDao.getDemographicsByHealthNum(hin);

            if (demos.size() == 1) {
                Demographic demo = demos.get(0);
                if (demo.getLastName().equals(patient.getLastName())) {

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = sdf.parse(demo.getFormattedDob());

                        if (d.equals(patient.getBirthdate())) {
                            demoId = demo.getId(); // we found the patient
                        }
                    } catch (ParseException e) {
                        MiscUtils.getLogger().error("Error", e);
                    }
                }
            }
        }

        CtlDocument ctlDoc = new CtlDocument();
        ctlDoc.getId().setDocumentNo(docEntity.getDocumentNo());
        ctlDoc.getId().setModule("demographic");
        ctlDoc.getId().setModuleId(demoId);
        ctlDoc.setStatus("A");
        ctlDocDao.persist(ctlDoc);
    }

    private Boolean addProviderRouting(Document docEntity, IProvider prov) {

        ProviderLabRoutingDao plrDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
        ProviderLabRoutingModel plr = new ProviderLabRoutingModel();
        Provider provEntity;
        Boolean routed = false;

        if (providerBelongsToUs(prov)) {

            plr.setLabNo(docEntity.getDocumentNo());
            plr.setStatus("N"); // Staus:New? (need to confirm semantics)
            plr.setLabType("DOC");

            provEntity = matchProvider(prov);

            if (provEntity == null)  // provider can't be found - use default
                provEntity = getDefaultProvider();

            plr.setProviderNo(provEntity.getProviderNo());

            plrDao.persist(plr);
            routed = true;
        }
        return routed;
    }

    private void addDefaultProviderRouting(Document docEntity) {

        ProviderLabRoutingDao plrDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
        ProviderLabRoutingModel plr = new ProviderLabRoutingModel();
        Provider provEntity;

        plr.setLabNo(docEntity.getDocumentNo());
        plr.setStatus("N"); // Staus:New? (need to confirm semantics)
        plr.setLabType("DOC");

        provEntity = getDefaultProvider();

        plr.setProviderNo(provEntity.getProviderNo());

        plrDao.persist(plr);
    }

    private Provider getDefaultProvider() {
        return new Provider(); // implement by looking up from DB
    }

    private Provider matchProvider(IProvider prov) {
        ProviderDao provdao = SpringUtils.getBean(ProviderDao.class);
        Provider provEntity;

        String id = selectIDType(prov.getIDs(), "MSP");

        provEntity = provdao.getProviderByOhipNo(id);

        if (provEntity == null) {

            List<Provider> ps = provdao.getProviderFromFirstLastName(prov.getFirstName(), prov.getLastName());

            if (ps.size() == 1)
                provEntity = ps.iterator().next();
        }

        return provEntity;
    }

    private boolean providerBelongsToUs(IProvider prov) {
        return clinicId.equals(prov.getClinicID());
    }
}
