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
import org.oscarehr.integration.cdx.dao.CdxDocumentDao;
import org.oscarehr.integration.cdx.dao.CdxPersonDao;
import org.oscarehr.integration.cdx.dao.CdxPersonIdDao;
import org.oscarehr.integration.cdx.dao.CdxTelcoDao;
import org.oscarehr.integration.cdx.model.CdxDocument;
import org.oscarehr.integration.cdx.model.CdxPerson;
import org.oscarehr.integration.cdx.model.CdxPersonId;
import org.oscarehr.integration.cdx.model.CdxTelco;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CDXImport {

    private IReceiveDoc receiver = new ReceiveDocMock();

    private String clinicId;

    public CDXImport() {
        ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
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
        Document        docEntity = createInboxData(doc);
        CdxDocumentDao  cdxDocDao = SpringUtils.getBean(CdxDocumentDao.class);
        CdxDocument     cdxDocEntity = new CdxDocument();

        cdxDocEntity.setDocumentNo(docEntity.getDocumentNo());

        cdxDocEntity.setTemplateId(doc.getTemplateID());
        cdxDocEntity.setTemplateName(doc.getTemplateName());
        cdxDocEntity.setDocumentOid(doc.getDocumentID());
        cdxDocEntity.setLoincCode(doc.getLoincCode());
        cdxDocEntity.setLoincName(doc.getLoingCodeDisplayName());
        cdxDocEntity.setTitle(doc.getTitle());
        cdxDocEntity.setAuthoringTime(doc.getAuthoringTime());
        cdxDocEntity.setDevice(doc.getAuthorDevice());
        cdxDocEntity.setEffectiveTime(doc.getAuthoringTime());
        cdxDocEntity.setCustodian(doc.getCustodian());
        cdxDocEntity.setOrderId(doc.getOrderID());
        cdxDocEntity.setStatusCode(doc.getStatusCode());
        cdxDocEntity.setObservationDate(doc.getObservationDate());
        cdxDocEntity.setProcedureName(doc.getProcedureName());
        cdxDocEntity.setParentDocId(doc.getParentDocumentID());
        cdxDocEntity.setPatientEncounterId(doc.getPatientEncounterID());
        cdxDocEntity.setAdmissionDate(doc.getAdmissionDate());
        cdxDocEntity.setDischargeDate(doc.getDischargeDate());
        cdxDocEntity.setDisposition(doc.getDischargeDisposition());
        cdxDocEntity.setContents(doc.getContents());
        cdxDocEntity.setAttachmentType(doc.getAttachmentType().label);
        cdxDocEntity.setAttachment(doc.getAttachment());

        cdxDocDao.persist(cdxDocEntity);

        createCdxPerson(cdxDocEntity, CdxPerson.rolePatient, doc.getPatient());
        createCdxPerson(cdxDocEntity, CdxPerson.roleAuthor, doc.getAuthor());


    }

    private void createCdxPerson(CdxDocument cdxDocEntity, String role, IPerson person) {
        CdxPersonDao cdxPersonDao = SpringUtils.getBean(CdxPersonDao.class);
        CdxPerson personEntity = new CdxPerson();

        personEntity.setDocument(cdxDocEntity.getDocumentNo());
        personEntity.setFirstName(person.getFirstName());
        personEntity.setLastName(person.getLastName());
        personEntity.setGender(person.getGender().label);
        personEntity.setBirthdate(person.getBirthdate());
        personEntity.setStreetAddress(person.getStreetAddress());
        personEntity.setCity(person.getCity());
        personEntity.setPostalCode(person.getPostalCode());
        personEntity.setProvince(person.getProvince());
        personEntity.setCountry(person.getCountry());
        personEntity.setPrefix(person.getPrefix());
        personEntity.setClinicId(person.getClinicID());
        personEntity.setClinicName(person.getClinicName());
        personEntity.setRoleInDocument(role);

        cdxPersonDao.persist(personEntity);

        for (ITelco p : person.getPhones()) {
            createCdxTelco(personEntity, CdxTelco.kindPhone, p);
        }

        for (ITelco e : person.getEmails()) {
            createCdxTelco(personEntity, CdxTelco.kindEmail, e);
        }

        for (IId id : person.getIDs()) {
            createCdxPersonId(personEntity, id);
        }



    }

    private void createCdxPersonId(CdxPerson personEntity, IId id) {
        CdxPersonIdDao cdxPersonIdDao = SpringUtils.getBean(CdxPersonIdDao.class);
        CdxPersonId idEntity = new CdxPersonId();

        idEntity.setPerson(personEntity.getId());
        idEntity.setIdType(id.getIdType());
        idEntity.setIdCode(id.getIdCode());

        cdxPersonIdDao.persist(idEntity);
    }

    private void createCdxTelco(CdxPerson p, String kind, ITelco t) {
        CdxTelcoDao cdxTelcoDao = SpringUtils.getBean(CdxTelcoDao.class);
        CdxTelco telcoEntity = new CdxTelco();

        telcoEntity.setPerson(p.getId());
        telcoEntity.setKind(kind);
        telcoEntity.setType(t.getType().label);
        telcoEntity.setAddress(t.getAddress());

        cdxTelcoDao.persist(telcoEntity);
    }

    private Document createInboxData(IDocument doc) {
        Boolean         routed = false;
        DocumentDao     docDao = SpringUtils.getBean(DocumentDao.class);
        Document        docEntity = new Document();

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
        docDao.persist(docEntity);

        routed = addProviderRouting(docEntity, doc.getPrimaryRecipient());

        for (IPerson p : doc.getSecondaryRecipients()) {
            routed = routed || addProviderRouting(docEntity, p);
        }

        if (!routed) { // even if none of the recipients appears to work at our clinic, we will route to the default provider
            addDefaultProviderRouting(docEntity);
        }

        addPatient(docEntity, doc.getPatient());

        return docEntity;
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

    private Boolean addProviderRouting(Document docEntity, IPerson prov) {

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

    private Provider matchProvider(IPerson prov) {
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

    private boolean providerBelongsToUs(IPerson prov) {
        return clinicId.equals(prov.getClinicID());
    }
}
