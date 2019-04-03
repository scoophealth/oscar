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
import ca.uvic.leadlab.obibconnector.impl.receive.mock.ReceiveDocMock;
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

    private IReceiveDoc receiver = new ReceiveDocMock();

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
        Document        docEntity = createInboxData(doc);
        CdxDocumentDao  cdxDocDao = SpringUtils.getBean(CdxDocumentDao.class);
        CdxDocument     cdxDocEntity = new CdxDocument();

        cdxDocEntity.setDocumentNo(docEntity.getDocumentNo());

        cdxDocEntity.setTemplateId(doc.getTemplateID());
        cdxDocEntity.setTemplateName(doc.getTemplateName());
        cdxDocEntity.setDocumentOid(doc.getDocumentID());
        cdxDocEntity.setLoincCode(doc.getLoincCode());
        cdxDocEntity.setLoincName(doc.getLoincCodeDisplayName());
        cdxDocEntity.setTitle(doc.getTitle());
        cdxDocEntity.setAuthoringTime(doc.getAuthoringTime());
        cdxDocEntity.setDevice(doc.getAuthorDevice());
        cdxDocEntity.setEffectiveTime(doc.getAuthoringTime());
        cdxDocEntity.setCustodian(doc.getCustodianName());
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

        cdxDocDao.persist(cdxDocEntity);

        createCdxPatient(cdxDocEntity, doc.getPatient());
        createCdxProvider(cdxDocEntity, CdxPerson.roleAuthor, doc.getAuthor());
        createCdxProvider(cdxDocEntity, CdxPerson.roleOrderingProvider, doc.getOrderingProvider());
        createCdxProvider(cdxDocEntity, CdxPerson.roleFamilyProvider, doc.getFamilyProvider());
        createCdxProvider(cdxDocEntity, CdxPerson.roleProcedurePerformer, doc.getProcedurePerformer());
        createCdxProvider(cdxDocEntity, CdxPerson.rolePrimaryRecipient, doc.getPrimaryRecipient());


        for (IProvider p : doc.getSecondaryRecipients()) {
            createCdxProvider(cdxDocEntity, CdxPerson.roleSecondaryRecipient, p);
        }

        for (IProvider p : doc.getParticipatingProviders()) {
            createCdxProvider(cdxDocEntity, CdxPerson.roleParticipatingProvider, p);
        }

        for (IAttachment a : doc.getAttachments()) {
            createCdxAttachment(cdxDocEntity, a);
        }

    }

    private void createCdxAttachment(CdxDocument cdxDocEntity, IAttachment a) {
        CdxAttachmentDao dao = SpringUtils.getBean(CdxAttachmentDao.class);
        CdxAttachment attachmentEntity = new CdxAttachment();

        attachmentEntity.setDocument(cdxDocEntity.getDocumentNo());
        attachmentEntity.setAttachmentType(a.getType().label);
        attachmentEntity.setReference(a.getReference());
        attachmentEntity.setContent(a.getContent());

        dao.persist(attachmentEntity);
    }


    private void createCdxProvider(CdxDocument cdxDocEntity, String role, IProvider person) {
        if (person != null) {
            CdxPersonDao cdxPersonDao = SpringUtils.getBean(CdxPersonDao.class);
            CdxPerson personEntity = new CdxPerson();

            personEntity.setDocument(cdxDocEntity.getDocumentNo());
            personEntity.setFirstName(person.getFirstName());
            personEntity.setLastName(person.getLastName());
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
    }

    private void createCdxPatient(CdxDocument cdxDocEntity, IPatient person) {
        if (person != null) {
            CdxPersonDao cdxPersonDao = SpringUtils.getBean(CdxPersonDao.class);
            CdxPerson personEntity = new CdxPerson();

            personEntity.setDocument(cdxDocEntity.getDocumentNo());
            personEntity.setFirstName(person.getFirstName());
            personEntity.setLastName(person.getLastName());
            if (person.getGender()!=null)
                personEntity.setGender(person.getGender().label);
            personEntity.setBirthdate(person.getBirthdate());
            personEntity.setStreetAddress(person.getStreetAddress());
            personEntity.setCity(person.getCity());
            personEntity.setPostalCode(person.getPostalCode());
            personEntity.setProvince(person.getProvince());
            personEntity.setCountry(person.getCountry());
            personEntity.setPrefix(person.getPrefix());
            personEntity.setRoleInDocument(CdxPerson.rolePatient);

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
        telcoEntity.setType(t.getTelcoType().label);
        telcoEntity.setAddress(t.getAddress());

        cdxTelcoDao.persist(telcoEntity);
    }

    private Document createInboxData(IDocument doc) {
        Boolean         routed = false;
        DocumentDao     docDao = SpringUtils.getBean(DocumentDao.class);
        Document        docEntity = new Document();

        docEntity.setDoctype(doc.getTemplateName());
        docEntity.setDocdesc("CDX");
        docEntity.setDocfilename("N/A");
        docEntity.setDoccreator("CDX");
        docEntity.setResponsible("CDX");
        docEntity.setAbnormal(0);


        docEntity.setDocClass(doc.getLoincCodeDisplayName());
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

        for (IProvider p : doc.getSecondaryRecipients()) {
            routed = routed || addProviderRouting(docEntity, p);
        }

        if (!routed) { // even if none of the recipients appears to work at our clinic, we will route to the default provider
            addDefaultProviderRouting(docEntity);
        }

        addPatient(docEntity, doc.getPatient());

        return docEntity;
    }


    private String selectIDType(List<IId> ids, String type) {

        for (IId i : ids) {
            if (i.getIdType().equals(type))
                return i.getIdCode();
        }

        return null;
    }

    private void addPatient(Document docEntity, IPatient patient) {
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
