package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.integration.cdx.dao.CdxAttachmentDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.dms.EDocUtil;
import oscar.oscarEncounter.data.EctProgram;

import javax.servlet.http.HttpSession;
import java.util.Date;

import static org.oscarehr.util.SpringUtils.getBean;

public class CDXDocumentStore {

    private final DocumentDao docDao;
    private final CdxProvenanceDao provDao;
    private final ProviderLabRoutingDao plrDao;
    private final CdxAttachmentDao atDao;
    private final CtlDocumentDao ctlDocDao;
    private final PatientLabRoutingDao patientLabRoutingDao;
    private final SecRoleDao secRoleDao;

    // to get the user and program id
    private final HttpSession session;

    public CDXDocumentStore(HttpSession session) {
        this.session = session;
        this.docDao = getBean(DocumentDao.class);
        this.provDao = getBean(CdxProvenanceDao.class);
        this.plrDao = getBean(ProviderLabRoutingDao.class);
        this.atDao = getBean(CdxAttachmentDao.class);
        this.ctlDocDao = getBean(CtlDocumentDao.class);
        this.patientLabRoutingDao = getBean(PatientLabRoutingDao.class);
        this.secRoleDao = getBean(SecRoleDao.class);
    }

    /**
     * Store the cdx document as a document object
     * @param doc cdx document object
     */
    public void storeDocument(IDocument doc, Demographic demographic) {
        Document document = createDocument(doc, demographic);
        // Update provenance
        CdxProvenance provenance = provDao.findByDocumentIdAndAction(doc.getDocumentID(), "SEND");
        if (provenance != null) {
            provenance.setDocumentNo(document.getDocumentNo());
            provDao.merge(provenance);
        }
    }

    /**
     * create document object for cdx document
     * @param doc cdx document object
     * @return created document object
     */
    private Document createDocument(IDocument doc, Demographic demographic) {
        Document docEntity = new Document();
        populateDocument(doc, docEntity);
        addPatient(docEntity, demographic);
        addPatientNote(doc.getAuthor(), demographic);
        return docEntity;
    }

    /**
     * populate document with data from CDX IDocument object
     * @param doc CDX IDocument object
     * @param docEntity new document object
     */
    private void populateDocument(IDocument doc, Document docEntity) {
        try {
            IProvider p;
            docEntity.setDoctype(CDXImport.translateCdxCodeToDocType(doc.getLoincCodeDisplayName()));
            docEntity.setDocdesc(doc.getLoincCodeDisplayName());
            docEntity.setDocfilename("N/A");
            docEntity.setDoccreator(doc.getCustodianName());
            docEntity.setNumberofpages(0);

            p = doc.getOrderingProvider();
            if (p != null) {
                docEntity.setResponsible(p.getFirstName() + " " + p.getLastName());
            } else {
                docEntity.setResponsible("");
            }

            docEntity.setAbnormal(0);

            docEntity.setDocClass(doc.getLoincCodeDisplayName());
            docEntity.setDocxml("stored in CDX provenance table");
            docEntity.setDocfilename("CDX");
            docEntity.setContenttype("CDX");
            docEntity.setRestrictToProgram(false); // need to confirm semantics

            IProvider auth = doc.getAuthor();

            docEntity.setSource(auth != null ? auth.getLastName() : "");
            docEntity.setUpdatedatetime(doc.getAuthoringTime());
            docEntity.setStatus(Document.STATUS_ACTIVE);
            docEntity.setReportStatus(doc.getStatusCode().code);

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
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception while saving Document " + ex.getMessage());
        }
    }

    /**
     * Link the document with the demographic to display the document in the patient e-chart
     * @param docEntity inbox document to be linked to demographic
     * @param demographic demographic linked to the document
     */
    private void addPatient(Document docEntity, Demographic demographic) {
        try {
            CtlDocument ctlDoc = new CtlDocument();
            ctlDoc.getId().setDocumentNo(docEntity.getDocumentNo());
            ctlDoc.getId().setModule("demographic");
            ctlDoc.getId().setModuleId(demographic.getId());
            ctlDoc.setStatus("A");
            ctlDocDao.persist(ctlDoc);

            PatientLabRouting patientLabRouting = new PatientLabRouting();
            patientLabRouting.setLabNo(docEntity.getDocumentNo());
            patientLabRouting.setLabType("DOC");
            patientLabRouting.setDemographicNo(demographic.getDemographicNo());
            patientLabRoutingDao.persist(patientLabRouting);
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception while saving Patient" + ex.getMessage());
        }
    }

    /**
     * Create a note in the patient e-chart
     * @param author
     * @param demographic
     */
    private void addPatientNote(IProvider author, Demographic demographic) {
        try {
            //Code to add Information in NOTE Section in patient chart.
            Date now = EDocUtil.getDmsDateTimeAsDate();

            String docDesc = EDocUtil.getLastDocumentDesc();

            CaseManagementNote cmn = new CaseManagementNote();
            cmn.setUpdate_date(now);

            cmn.setObservation_date(now);
            cmn.setDemographic_no(demographic.getDemographicNo().toString());
            String user_no = (String) session.getAttribute("user");
            String prog_no = new EctProgram(session).getProgram(user_no);
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
            CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
            cmn.setProviderNo("-1");// set the provider no to be -1 so the editor appear as 'System'.

            String strNote = "Document" + " " + docDesc + " " + "created at " + now + " by " + author.getFirstName() + " " + author.getLastName() + ".";

            cmn.setNote(strNote);
            cmn.setSigned(true);
            cmn.setSigning_provider_no("-1");
            cmn.setProgram_no(prog_no);

            SecRole doctorRole = secRoleDao.findByName("doctor");
            cmn.setReporter_caisi_role(doctorRole.getId().toString());

            cmn.setReporter_program_team("0");
            cmn.setPassword("NULL");
            cmn.setLocked(false);
            cmn.setHistory(strNote);
            cmn.setPosition(0);

            Long note_id = cmm.saveNoteSimpleReturnID(cmn);

            // Add a noteLink to casemgmt_note_link
            CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
            cmnl.setTableName(CaseManagementNoteLink.DOCUMENT);
            cmnl.setTableId(Long.parseLong(EDocUtil.getLastDocumentNo()));
            cmnl.setNoteId(note_id);
            EDocUtil.addCaseMgmtNoteLink(cmnl);
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception while saving Note" + ex.getMessage());
        }
    }
}
