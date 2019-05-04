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

import ca.uvic.leadlab.obibconnector.Support;
import ca.uvic.leadlab.obibconnector.facades.receive.*;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.impl.receive.ReceiveDoc;
import ca.uvic.leadlab.obibconnector.impl.receive.SearchDoc;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.*;
import org.oscarehr.integration.cdx.dao.CdxAttachmentDao;
import org.oscarehr.integration.cdx.dao.CdxPendingDocsDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxAttachment;
import org.oscarehr.integration.cdx.model.CdxPendingDoc;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.oscarehr.util.MiscUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.oscarehr.util.SpringUtils.getBean;


public class CDXImport {

    private IReceiveDoc receiver;
    private ISearchDoc docSearcher;
    private CDXConfiguration cdxConfig;
    private Support support;
    private CdxPendingDocsDao pendDocDao;
    private DocumentDao     docDao;
    private ProviderDao providerDao;
    private CdxProvenanceDao provDao;
    private ProviderLabRoutingDao plrDao;

    public CDXImport() {

        cdxConfig = new CDXConfiguration();

        receiver = new ReceiveDoc(cdxConfig);
        docSearcher = new SearchDoc(cdxConfig);

        support = new Support(cdxConfig);
        pendDocDao = getBean(CdxPendingDocsDao.class);
        docDao = getBean(DocumentDao.class);
        providerDao = getBean(ProviderDao.class);
        provDao = getBean(CdxProvenanceDao.class);
        plrDao = getBean(ProviderLabRoutingDao.class);
    }


    public void importNewDocs() throws Exception {

        List<String> docIds;

        docIds = receiver.pollNewDocIDs();

        MiscUtils.getLogger().info("CDX Import: " + docIds.size() + " new documents to import" );

        importDocuments(docIds);

    }

    public void importAllDocs() throws Exception {

        List<String> docIds = new ArrayList<String>();
        List<IDocument> docs;

        docs = docSearcher.searchDocumentsByClinic(cdxConfig.getClinicId());

        for (IDocument doc : docs) {
            docIds.add(doc.getDocumentID());
        }


        MiscUtils.getLogger().info("CDX Import: " + docIds.size() + " OLD documents to import" );

        importDocuments(docIds);

    }


    public void importPendingDocs() throws Exception {

        List<String> docIds;

        docIds = pendDocDao.getPendingErrorDocs();

        MiscUtils.getLogger().info("CDX Import: " + docIds.size() + " old pending documents to import" );

        importDocuments(docIds);

    }

    private void importDocuments(List<String> docIds) {
        for (String id : docIds)
            try {

                MiscUtils.getLogger().info("CDX Import: importing document " + id );

                IDocument doc = receiver.retrieveDocument(id);

                storeDocument(doc);

            } catch (Exception e) {
                CdxPendingDoc pd = new CdxPendingDoc();
                pd.setDocId(id);
                pd.setReasonCode(CdxPendingDoc.error);
                pd.setExplanation(e.toString());
                pendDocDao.persist(pd);

                MiscUtils.getLogger().error("Error importing CDX Document " + id, e);

                try {
                    support.notifyError("Error importing CDX document", e.toString());
                } catch (Exception e2) {
                    MiscUtils.getLogger().error("Could not communicate CDX Error to OBIB support channel", e2);
                }
            }
    }

    @Transactional
    public void storeDocument(IDocument doc) {
        CdxProvenance prov = new CdxProvenance();
        Document    inboxDoc = null;

        List<CdxProvenance> versions = provDao.findVersionsOrderDesc(doc.getDocumentID());

        if (versions.isEmpty()) // brand new document
            inboxDoc = createInboxData(doc);
        else  // new version of existing document
            inboxDoc = reviseInboxData(doc, versions.get(0).getDocumentNo());

        prov.populate(doc);
        prov.setDocumentNo(inboxDoc.getDocumentNo());
        prov.setAction("import");
        provDao.persist(prov);
        saveAttachments(doc, prov);

    }

    private Document reviseInboxData(IDocument doc, int inboxDocId) {

        Document        existingDocEntity = docDao.getDocument(Integer.toString(inboxDocId));
        Document        newDocEntity = new Document();

        populateInboxDocument(doc, newDocEntity);
        copyPreviousRoutingAndResetStati(newDocEntity, existingDocEntity);
        addPatient(newDocEntity, doc.getPatient());
        return newDocEntity;
    }

    private void copyPreviousRoutingAndResetStati(Document newDocEntity, Document existingDocEntity) {
        for (ProviderLabRoutingModel plr : plrDao.getProviderLabRoutingForLabAndType(existingDocEntity.getDocumentNo(), "DOC")) {
            ProviderLabRoutingModel plrNew = new ProviderLabRoutingModel();
            plrNew.setProviderNo(plr.getProviderNo());
            plrNew.setStatus("N");
            plrNew.setLabType(plr.getLabType());
            plrNew.setLabNo(newDocEntity.getDocumentNo());
            plrDao.persist(plrNew);
        }
    }


    private void saveAttachments(IDocument doc, CdxProvenance prov) {
        CdxAttachmentDao atDao = getBean(CdxAttachmentDao.class);

        for (IAttachment a : doc.getAttachments()) {
            CdxAttachment attachmentEntity = new CdxAttachment();

            attachmentEntity.setDocument(prov.getId());
            attachmentEntity.setAttachmentType(a.getType().mediaType);
            attachmentEntity.setReference(a.getReference());
            attachmentEntity.setContent(a.getContent());
            atDao.persist(attachmentEntity);
        }
    }


    private Document createInboxData(IDocument doc) {
        Document        docEntity = new Document();
        populateInboxDocument(doc, docEntity);

        addPatient(docEntity, doc.getPatient());

        return docEntity;
    }

    private void populateInboxDocument(IDocument doc, Document docEntity) {
        IProvider p;
        docEntity.setDoctype(doc.getLoincCodeDisplayName());
        docEntity.setDocdesc(doc.getLoincCodeDisplayName());
        docEntity.setDocfilename("N/A");
        docEntity.setDoccreator(doc.getCustodianName());

        p = doc.getOrderingProvider();

        if (p != null)
            docEntity.setResponsible(p.getFirstName() + " " + p.getLastName());
        else docEntity.setResponsible("");

        docEntity.setAbnormal(0);


        docEntity.setDocClass("CDX");
        docEntity.setDocxml("stored in CDX provenance table");
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
        } else if (doc.getAuthoringTime() != null) {
            docEntity.setObservationdate(doc.getAuthoringTime());
        } else if (doc.getEffectiveTime() != null) {
            docEntity.setObservationdate(doc.getEffectiveTime());
        } else {
            docEntity.setObservationdate(new Date());

        }

        if (doc.getCustodianName() != null)
            docEntity.setSourceFacility(doc.getCustodianName());

        docEntity.setContentdatetime(doc.getAuthoringTime());
        docDao.persist(docEntity);

        addProviderRouting(docEntity, doc.getPrimaryRecipient());

        for (IProvider q : doc.getSecondaryRecipients()) {
            addProviderRouting(docEntity, q);
        }

        if (!routed(docEntity)) { // even if none of the recipients appears to work at our clinic, we will route to the default provider
            addDefaultProviderRouting(docEntity);
        }
    }

    private boolean routed(Document docEntity) {
        List<ProviderLabRoutingModel> plrs = plrDao.getProviderLabRoutingForLabAndType(docEntity.getDocumentNo(), "DOC");
        return !plrs.isEmpty();
    }


    private void addPatient(Document docEntity, IPatient patient) {
        DemographicDao demoDao = getBean(DemographicDao.class);
        CtlDocumentDao ctlDocDao = getBean(CtlDocumentDao.class);
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

    private void addProviderRouting(Document docEntity, IProvider prov) {

        Provider provEntity = null;

        try {
            providerDao.getProviderByOhipNo(prov.getID());
        } catch (Exception e) {
            MiscUtils.getLogger().info("Provider in CDX document does not have valid ID");
        }

        if (provEntity != null) {
            ProviderLabRoutingModel plr = new ProviderLabRoutingModel();
            plr.setLabNo(docEntity.getDocumentNo());
            plr.setStatus("N");
            plr.setLabType("DOC");
            plr.setProviderNo(provEntity.getProviderNo());
            plrDao.persist(plr);
        }
    }

    private void addDefaultProviderRouting(Document docEntity) {

        ProviderLabRoutingDao plrDao = getBean(ProviderLabRoutingDao.class);
        ProviderLabRoutingModel plr = new ProviderLabRoutingModel();

        plr.setLabNo(docEntity.getDocumentNo());
        plr.setStatus("N"); // Status:New? (need to confirm semantics)
        plr.setLabType("DOC");
        plr.setProviderNo("0");
        plrDao.persist(plr);
    }




    public static boolean sameDates(Date a, Date b) {
        if (a.getDate() == b.getDate() &&
                a.getMonth() == b.getMonth() &&
                a.getYear() == b.getYear())
            return true;
        else return false;
    }
}
