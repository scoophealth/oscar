package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.receive.*;
import ca.uvic.leadlab.obibconnector.impl.receive.mock.ReceiveDocMock;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.*;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.common.dao.DocumentDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CDXImport {

    private IReceiveDoc receiver = new ReceiveDocMock();


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

        DocumentDao docdao = SpringUtils.getBean(DocumentDao.class);
        Document docEntity = new Document();

        docEntity.setDoctype(doc.getTemplateName());
//        docEntity.setDocdesc(doc.getTemplateName());
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

        addProviderRouting(docEntity, doc.getPrimaryRecipient());

        for (IProvider p : doc.getSecondaryRecipients()) {
            addProviderRouting(docEntity, p);
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

    private void addPatient(Document docEntity, IPerson patient) {
        DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
        CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);

        List<Demographic> demos = demoDao.getDemographicsByHealthNum(patient.getID());
        int demoId = -1;

        if (demos.size()==1) { // unique match of HIN
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

        CtlDocument ctlDoc = new CtlDocument();
        ctlDoc.getId().setDocumentNo(docEntity.getDocumentNo());
        ctlDoc.getId().setModule("demographic");
        ctlDoc.getId().setModuleId(demoId);
        ctlDoc.setStatus("A");
        ctlDocDao.persist(ctlDoc);
    }

    private void addProviderRouting(Document docEntity, IProvider prov) {

        ProviderLabRoutingDao plrDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
        ProviderLabRoutingModel plr = new ProviderLabRoutingModel();
        Provider provEntity;

        if (providerBelongsToUs(prov)) {

            plr.setLabNo(docEntity.getDocumentNo());
            plr.setStatus("N"); // Staus:New? (need to confirm semantics)
            plr.setLabType("DOC");

            provEntity = matchProvider(prov);

            if (provEntity == null)  // provider can't be found - use default
                provEntity = getDefaultProvider();

            plr.setProviderNo(provEntity.getProviderNo());

            plrDao.persist(plr);
        }
    }

    private Provider getDefaultProvider() {
        return new Provider(); // implement by looking up from DB
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
        return true;
        // needs to be implemented - looking up clinic ID in the clinic_location table etc.
    }
}
