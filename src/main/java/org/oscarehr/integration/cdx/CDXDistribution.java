package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.receive.IDistributionStatus;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.receive.ISearchDoc;
import ca.uvic.leadlab.obibconnector.impl.receive.SearchDoc;
import org.oscarehr.integration.cdx.dao.CdxPendingDocsDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxPendingDoc;
import org.oscarehr.util.MiscUtils;

import java.util.ArrayList;
import java.util.List;

import static org.oscarehr.util.SpringUtils.getBean;

public class CDXDistribution {

    private static final String DELIVERED = "DELIVERED";
    private static final String QUEUED = "QUEUED";
    private static final String UNDELIVERABLE = "UNDELIVERABLE";
    private static final String UNKNOWN = "UNKNOWN";
    private static final String LOST = "LOST";

    private CDXConfiguration cdxConfig;
    private ISearchDoc docSearcher;

    private CdxPendingDocsDao pendDocDao;
    private CdxProvenanceDao provDao;

    public CDXDistribution() {
        cdxConfig = new CDXConfiguration();
        docSearcher = new SearchDoc(cdxConfig);

        pendDocDao = getBean(CdxPendingDocsDao.class);
        provDao = getBean(CdxProvenanceDao.class);
    }

    /**
     * Query the distribution status using OBIBConnector.
     */
    public List<IDocument> getDocumentDistributionStatus(String docId) {

        List<IDocument> documents = new ArrayList<IDocument>();
        try {
            documents.addAll(docSearcher.distributionStatus(docId));
        } catch (Exception e) {
            new NotificationController().insertNotifications("Warning",e.getMessage() +" Document ID: "+docId,"POLLING","Polling of CDX distribution status of the document");
            MiscUtils.getLogger().error("Error retrieving CDX distribution status of document " + docId, e);
        }
        return documents;
    }

    /**
     * Updates the document distribution status (CdxProvenance) and change the 'pending status' (CdxPendingDoc)
     * as following:
     * <p>
     * If the status is equal QUEUED or UNKNOWN, add a 'pending status' for this documentId,
     * otherwise remove the 'pending status'.
     */
    private void updateDocumentDistributionStatus(String documentId, String status) {
        provDao.updateProvDistributionStatus(documentId, status);
        // update pending status
        List<CdxPendingDoc> pendingDocs = pendDocDao.findPendingDocs(documentId);
        if (status.startsWith(QUEUED) || status.startsWith(UNKNOWN) || status.startsWith(LOST) ){ // status is QUEUED or UNKNOWN or LOST, create a 'pending status'
            if (pendingDocs == null || pendingDocs.isEmpty()) {
                pendDocDao.persist(CdxPendingDoc.createPendingDocStatus(documentId));
            }
        } else { // otherwise, remove the 'pending status'
            if (pendingDocs != null && !pendingDocs.isEmpty()) {
                for (CdxPendingDoc pendingDoc : pendingDocs) {
                    pendDocDao.removePendDoc(pendingDoc.getDocId());
                }
            }
        }
    }

    /**
     * Updates the distribution status for a document by querying OBIBConnector/CDX.
     *
     * @see CDXDistribution#getDocumentDistributionStatus(String)
     * @see CDXDistribution#updateDocumentDistributionStatus(String, String).
     */
    public void updateDistributionStatus(String documentId) {
        try {
            String status =LOST;
            DOC_LOOP:
            for (IDocument doc : getDocumentDistributionStatus(documentId)) {
                for (IDistributionStatus distStatus : doc.getDistributionStatus()) {
                    status = distStatus.getStatusName();
                    if (!status.startsWith(DELIVERED)) { // if there is a 'not delivered' status, return it.
                        break DOC_LOOP;
                    }
                }
            }
            updateDocumentDistributionStatus(documentId, status);
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Error updating distribution status for document " + documentId, ex);
        }
    }

    /**
     * Updates the distribution status for all pending documents.
     */
    public void updateDistributeStatus() {
        // list all pending status docs
        List<CdxPendingDoc> pendingDocs = pendDocDao.getPendingStatusDocs();
        for (CdxPendingDoc pendingDoc : pendingDocs) {
            updateDistributionStatus(pendingDoc.getDocId());
        }
    }
}
