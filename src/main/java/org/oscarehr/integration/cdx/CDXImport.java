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
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.integration.cdx.dao.*;
import org.oscarehr.integration.cdx.model.*;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CDXImport {

    private IReceiveDoc receiver = SpringUtils.getBean(IReceiveDoc.class);

    private String clinicId;

    private CDXConfiguration cdxConfig;

    public CDXImport() {
        ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
        clinicId = clinicDao.getClinic().getCdxOid();

        cdxConfig = new CDXConfiguration();
    }


    public void importNewDocs() throws Exception {

        List<String> docIds;

        docIds = receiver.pollNewDocIDs();

        for (String id : docIds) {

            IDocument doc = receiver.retrieveDocument(id);

            storeDocument(doc);

        }

    }

    private void storeDocument(IDocument doc) {
        CdxAttachmentDao dao = SpringUtils.getBean(CdxAttachmentDao.class);

        Document        docEntity = createInboxData(doc);

        for (IAttachment a : doc.getAttachments()) {
            CdxAttachment attachmentEntity = new CdxAttachment();

            attachmentEntity.setDocument(docEntity.getDocumentNo());
            attachmentEntity.setAttachmentType(a.getType().label);
            attachmentEntity.setReference(a.getReference());
            attachmentEntity.setContent(a.getContent());
            dao.persist(attachmentEntity);
        }




    }




    private Document createInboxData(IDocument doc) {
        Boolean         routed = false;
        DocumentDao     docDao = SpringUtils.getBean(DocumentDao.class);
        Document        docEntity = new Document();
        IProvider       p;

        docEntity.setDoctype(doc.getTemplateName());
        docEntity.setDocdesc(doc.getTemplateName());
        docEntity.setDocfilename("N/A");
        docEntity.setDoccreator(doc.getCustodianName());

        p = doc.getOrderingProvider();

        if (p != null)
            docEntity.setResponsible(p.getFirstName() + " " + p.getLastName());
        else docEntity.setResponsible("");

        docEntity.setAbnormal(0);


        docEntity.setDocClass("CDX");
        docEntity.setDocxml(doc.getContents());
        docEntity.setDocfilename(doc.getDocumentID());
        docEntity.setRestrictToProgram(false); // need to confirm semantics

        IProvider auth = doc.getAuthor();

        docEntity.setSource(auth != null ? auth.getLastName() : "");
        docEntity.setUpdatedatetime(doc.getAuthoringTime());
        docEntity.setStatus(Document.STATUS_ACTIVE);
        docEntity.setReportStatus(doc.getStatusCode());
        docEntity.setContenttype("text/plain");

        if (doc.getObservationDate() != null) {
            docEntity.setObservationdate(doc.getObservationDate());
        } else
            docEntity.setObservationdate(doc.getAuthoringTime());

        if (doc.getCustodianName() != null)
        docEntity.setSourceFacility(doc.getCustodianName());

        docEntity.setContentdatetime(doc.getAuthoringTime());
        docDao.persist(docEntity);

        routed = addProviderRouting(docEntity, doc.getPrimaryRecipient());

        for (IProvider q : doc.getSecondaryRecipients()) {
            routed = routed || addProviderRouting(docEntity, q);
        }

        if (!routed) { // even if none of the recipients appears to work at our clinic, we will route to the default provider
            addDefaultProviderRouting(docEntity);
        }

        addPatient(docEntity, doc.getPatient());


        return docEntity;
    }


    private void addPatient(Document docEntity, IPatient patient) {
        DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
        CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);
        int demoId = -1;

        // implement 4 point matching as required by CDX conformance spec


        List<Demographic> demos = demoDao.getDemographicsByHealthNum(patient.getID());

        if (demos.size() == 1) {
            Demographic demo = demos.get(0);
            if (demo.getLastName().equals(patient.getLastName().toUpperCase())) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = sdf.parse(demo.getFormattedDob());

                    Date d2 = patient.getBirthdate();

                    if (sameDates(d, d2) && (patient.getGender() != null)) {

                        if (patient.getGender().label.equals(demo.getSex())) {

                            demoId = demo.getId(); // we found the patient
                        }
                    }
                } catch (ParseException e) {
                    MiscUtils.getLogger().error("Error", e);
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

            plr.setProviderNo(provEntity.getProviderNo());

            plrDao.persist(plr);
            routed = true;
        }
        return routed;
    }

    private void addDefaultProviderRouting(Document docEntity) {

        ProviderLabRoutingDao plrDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
        ProviderLabRoutingModel plr = new ProviderLabRoutingModel();

        plr.setLabNo(docEntity.getDocumentNo());
        plr.setStatus("N"); // Status:New? (need to confirm semantics)
        plr.setLabType("DOC");
        plr.setProviderNo("0");
        plrDao.persist(plr);
    }


    private Provider matchProvider(IProvider prov) {
        ProviderDao provdao = SpringUtils.getBean(ProviderDao.class);
        Provider provEntity;

        provEntity = provdao.getProviderByOhipNo(prov.getID());

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

    public static boolean sameDates(Date a, Date b) {
        if (a.getDate() == b.getDate() &&
                a.getMonth() == b.getMonth() &&
                a.getYear() == b.getYear())
            return true;
        else return false;
    }
}
