package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.registry.IClinic;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.send.IRecipient;
import ca.uvic.leadlab.obibconnector.facades.send.ISubmitDoc;
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.lowagie.text.rtf.parser.RtfParser;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.integration.cdx.dao.CdxAttachmentDao;
import org.oscarehr.integration.cdx.dao.CdxMessengerDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxAttachment;
import org.oscarehr.integration.cdx.model.CdxMessenger;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;
import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CDXMessengerAction extends DispatchAction {
    private static final Logger logger = MiscUtils.getLogger();
    private String contentRoute;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        contentRoute = request.getSession().getServletContext().getRealPath("/");
        HashMap<String, String[]> specialistAndClinicsPrimary = new HashMap<>();
        HashMap<String, String[]> specialistAndClinicsSecondary = new HashMap<>();
        String pRecipients[] = request.getParameterValues("precipients");
        String sRecipients[] = request.getParameterValues("srecipients");
        String specialistsToStore = "";
        // String clinics[] = null;
        // String specialists[]=null;
        // int specialistsAndClinicsIndex=0;
        if (pRecipients != null && pRecipients.length > 0) {

            for (String rec : pRecipients) {
                String splittedSpecialistsAndClinics[] = rec.split("@");
                specialistsToStore = specialistsToStore + splittedSpecialistsAndClinics[0] + ",";
                specialistAndClinicsPrimary.put(splittedSpecialistsAndClinics[0], splittedSpecialistsAndClinics[1].split(","));

            }
            specialistsToStore = specialistsToStore.substring(0, specialistsToStore.length() - 1);
        }

        if (sRecipients != null && sRecipients.length > 0) {

            for (String rec : sRecipients) {
                String splittedSpecialistsAndClinics[] = rec.split("@");
                specialistsToStore = specialistsToStore + "," + splittedSpecialistsAndClinics[0];
                specialistAndClinicsSecondary.put(splittedSpecialistsAndClinics[0], splittedSpecialistsAndClinics[1].split(","));

            }

        }


        CdxMessenger cdxMessenger = setCdxMessage(request, specialistsToStore);


        try {
            doCdxSend(loggedInInfo, request, cdxMessenger, specialistAndClinicsPrimary, specialistAndClinicsSecondary);
            request.setAttribute("success", true);
        } catch (OBIBException e) {
            request.setAttribute("success", false);
            request.setAttribute("error", e);
            logger.error("Error sending CDX document", e);
            String additionalText = e.getObibMessage();
            if (additionalText == null || additionalText.isEmpty()) {
                additionalText = e.getMessage();
            }
            logger.error("Additional Message." + additionalText);
        }

        return mapping.findForward("success");
    }


    private CdxMessenger setCdxMessage(HttpServletRequest request, String specialists) {


        CdxMessenger cdxMessenger = new CdxMessenger();
        String patient = request.getParameter("patientsearch");
        //String primaryrecipient = request.getParameter("precipients");


        //String secondaryrecipient = request.getParameter("srecipients");
        String messagetype = request.getParameter("messagetype");
        String documenttype = request.getParameter("documenttype");
        String contentmessage = request.getParameter("contentmessage");
        if (patient != null || patient.length() != 0) {
            cdxMessenger.setPatient(patient);
            cdxMessenger.setRecipients(specialists);
            cdxMessenger.setCategory(messagetype);
            cdxMessenger.setContent(contentmessage);
            cdxMessenger.setDocumentType(documenttype);
            cdxMessenger.setAuthor(request.getSession().getAttribute("userfirstname") + "," + request.getSession().getAttribute("userlastname"));
            cdxMessenger.setTimeStamp(new Timestamp(new Date().getTime()));
            cdxMessenger.setDeliveryStatus("Unknown");


        }
        return cdxMessenger;

    }


    private void doCdxSend(LoggedInInfo loggedInInfo, HttpServletRequest request, CdxMessenger cdxMessenger, HashMap<String, String[]> specialistAndClinicsPrimary, HashMap<String, String[]> specialistAndClinicsSecondary) throws OBIBException {

   /*     ConsultationRequestDao consultationRequestDao = (ConsultationRequestDao) SpringUtils.getBean("consultationRequestDao");
        ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
        ClinicDAO clinicDAO = (ClinicDAO) SpringUtils.getBean("clinicDAO");

        ConsultationRequest consultationRequest = consultationRequestDao.find(consultationRequestId);
        ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.find(consultationRequest.getSpecialistId());
        Clinic clinic = clinicDAO.getClinic();

        String message = fillReferralNotes(consultationRequest);

        // save just in case the sending fails.
        consultationRequestDao.merge(consultationRequest);

        Provider sendingProvider = loggedInInfo.getLoggedInProvider();
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        Demographic demographic = demographicManager.getDemographic(loggedInInfo, consultationRequest.getDemographicId());

        String patientId = demographic.getHin();
        if (patientId == null || patientId.isEmpty()) {
            patientId = demographic.getDemographicNo().toString();
        }
        String authorId = sendingProvider.getOhipNo();
        if (authorId == null || authorId.isEmpty()) {
            authorId = sendingProvider.getProviderNo();
        }
        String recipientId = professionalSpecialist.getCdxId();
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
        List<IProvider> providers = cdxSpecialist.findCdxSpecialistById(recipientId);
        String clinicID = null;
        if (providers != null && !providers.isEmpty()) {
            IProvider cdxProvider = providers.get(0);
            clinicID = cdxProvider.getClinicID();
            if (!professionalSpecialist.getLastName().equalsIgnoreCase(cdxProvider.getLastName())) {
                throw new OBIBException("Last name reported by CDX does not match last name of selected specialist.");
            }
        } else {
            MiscUtils.getLogger().error("Selected specialist's CDX ID not found");
            throw new OBIBException("Selected specialist's CDX ID not found");
        }

        // create PDF for consultation request form

        ByteOutputStream os = new ByteOutputStream();
        ConsultationPDFCreator consultationPDFCreator = new ConsultationPDFCreator(request, requestId, os);

        try {
            consultationPDFCreator.printPdf(loggedInInfo);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Could not generate PDF attachment for consultation request");
            throw new OBIBException("Could not generate PDF attachment for consultation request");
        }

        os.close();
        */




        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        List<Demographic> demoList = null;
        String patient = request.getParameter("patientsearch");
        demoList = demographicDao.searchDemographic(patient);

        Provider sendingProvider = loggedInInfo.getLoggedInProvider();
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        Demographic demographic = demographicManager.getDemographic(loggedInInfo, demoList.get(0).getDemographicNo());
        String patientId = demographic.getHin();
        if (patientId == null || patientId.isEmpty()) {
            patientId = demographic.getDemographicNo().toString();
        }
        String authorId = sendingProvider.getOhipNo();
        if (authorId == null || authorId.isEmpty()) {
            authorId = sendingProvider.getProviderNo();
        }

        ClinicDAO clinicDAO = (ClinicDAO) SpringUtils.getBean("clinicDAO");
        Clinic clinic = clinicDAO.getClinic();

        ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");


        // Add pdf attachments (scanned images, lab reports and PDF files)
        String filename = null;
        byte[] newBytes = null;
        ByteOutputStream bos = createPdfForAttachments(loggedInInfo, "" + demographic.getDemographicNo(), "" + null);
        if (bos != null) {
            newBytes = bos.toByteArray();
            filename = "ConsultationRequestAttachedPDF-" + demographic.getLastName() + "," + demographic.getFirstName() + "-" + UtilDateUtilities.getToday("yyyy-MM-dd_HHmmss") + ".pdf";
            MiscUtils.getLogger().debug("File: " + filename + ", Size: " + newBytes.length);
        }




      /*  //Todo for multiple specialists
       // String specialists[]=cdxMessenger.getRecipients().split(",")[0].split(" ");
        String specialists[]=cdxMessenger.getRecipients().split(",");
        List<ProfessionalSpecialist> professionalSpecialistList = null;
        List<IProvider> providers = null;
        CDXSpecialist cdxSpecialist = new CDXSpecialist();

        for (String s:specialists){
            String lastAndFirstName[]=s.split(" ",2);
            providers = cdxSpecialist.findCdxSpecialistByName(lastAndFirstName[0].trim());

            for (IProvider provider : providers) {
                //   String cdxSpecId = provider.getID();
                // MiscUtils.getLogger().debug("cdxSpecId: " + cdxSpecId);
                //String fName = provider.getFirstName();
                //String lName = provider.getLastName();

                if (provider.getFirstName() != null && !provider.getFirstName().isEmpty()) {
                    if (provider.getLastName().equalsIgnoreCase(lastAndFirstName[0].trim()) && provider.getFirstName().equalsIgnoreCase(lastAndFirstName[1].trim())) {


                    }
                }
            }



           // professionalSpecialistList.add(professionalSpecialistDao.findByFullName(lastAndFirstName[0],lastAndFirstName[1]).get(0));
        }

        List<ProfessionalSpecialist> professionalSpecialistList = professionalSpecialistDao.findByFullName(specialists[0],specialists[1]);

        ProfessionalSpecialist professionalSpecialist=professionalSpecialistList.get(0);
        String recipientId = professionalSpecialist.getCdxId();


        //CDXSpecialist cdxSpecialist = new CDXSpecialist();
        //List<IProvider> providers = cdxSpecialist.findCdxSpecialistById(recipientId);
        String clinicID = null;
        if (providers != null && !providers.isEmpty()) {
            IProvider cdxProvider = providers.get(0);
            clinicID = cdxProvider.getClinicID();
            if (!professionalSpecialist.getLastName().equalsIgnoreCase(cdxProvider.getLastName())) {
                throw new OBIBException("Last name reported by CDX does not match last name of selected specialist.");
            }
        } else {
            MiscUtils.getLogger().error("Selected specialist's CDX ID not found");
            throw new OBIBException("Selected specialist's CDX ID not found");
        }

*/


        IDocument response;
        CDXConfiguration cdxConfig = new CDXConfiguration();
        SubmitDoc submitDoc = new SubmitDoc(cdxConfig);

        ISubmitDoc doc;

       /* if (isUpdate || isCancel ) {
            List<CdxProvenance> sentDocs;
            CdxProvenanceDao cdxProvenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
            sentDocs = cdxProvenanceDao.findByKindAndInFulFillment(DocumentType.REFERRAL_NOTE, requestId);
            String originalDocId = sentDocs.get(sentDocs.size() - 1).getDocumentId();
            Integer latestDocVersion = sentDocs.get(0).getVersion();

            if (isUpdate) {
                doc = submitDoc.updateDoc(originalDocId, latestDocVersion);
            } else {
                doc = submitDoc.cancelDoc(originalDocId, latestDocVersion);
            }
        }

        else {
            doc = submitDoc.newDoc();
        }
        */

        doc = submitDoc.newDoc();
        if (cdxMessenger.getDocumentType().equalsIgnoreCase("Information Request")) {
            doc.documentType(DocumentType.INFO_REQUEST);
        } else if (cdxMessenger.getDocumentType().equalsIgnoreCase("Patient Summary")) {
            doc.documentType(DocumentType.PATIENT_SUMMARY);
        } else if (cdxMessenger.getDocumentType().equalsIgnoreCase("Progress Note")) {
            doc.documentType(DocumentType.PROGRESS_NOTE);
        }


        ISubmitDoc iDoc = doc //.content(cdxMessenger.getContent())
                //.inFulfillmentOf()
                // .id(Integer.toString(cdxMessenger.getId()))
                // .statusCode(OrderStatus.ACTIVE).and()
                .patient()
                .id(patientId)
                .name(NameType.LEGAL, demographic.getFirstName(), demographic.getLastName())
                .address(AddressType.HOME, demographic.getAddress(), demographic.getCity(), demographic.getProvince(), demographic.getPostal(), "CA")
                .phone(TelcoType.HOME, demographic.getPhone())
                .birthday(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth())
                .gender("M".equalsIgnoreCase(demographic.getSex()) ? Gender.MALE : Gender.FEMALE)
                .and().author()
                .id(authorId)
                .time(new Date())
                .name(NameType.LEGAL, sendingProvider.getFirstName(), sendingProvider.getLastName())
                .address(AddressType.WORK, clinic.getAddress(), clinic.getCity(), clinic.getProvince(), clinic.getPostal(), "CA")
                .phone(TelcoType.WORK, clinic.getPhone())
                .and();

        HashSet<String> allUniqueClinics = new HashSet<String>();
        ISubmitDoc iDocReturnedPrimary = addRecipient(iDoc, specialistAndClinicsPrimary, allUniqueClinics, "primary");

        if (specialistAndClinicsSecondary != null) {
            addRecipient(iDocReturnedPrimary, specialistAndClinicsSecondary, allUniqueClinics, "secondary");
        }

        for (String cl : allUniqueClinics) {
            //adding all clinics to the document as a receiver.
            iDoc.receiverId(cl);
        }
        if (newBytes != null) {
            doc = doc.attach(AttachmentType.PDF, filename, newBytes);
        }

        response = doc.submit();

        boolean debug = false;
        if (debug) logResponse(response);
        MiscUtils.getLogger().debug("Attempting to save document using logSentAction");
        CdxProvenanceDao cdxProvenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
        cdxProvenanceDao.logSentAction(response);
        CdxMessengerDao cdxMessengerDao = SpringUtils.getBean(CdxMessengerDao.class);

        try {
            cdxMessengerDao.persist(cdxMessenger);

        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception saving messenger Information " + ex.getMessage());
        }

        //Now we have request id for the cdx messenger, we update the request id for the attachments.
        CommonLabResultData consultLabs = new CommonLabResultData();
        ArrayList<EDoc> attachmentlists = EDocUtil.listDocsForCdxMessenger(loggedInInfo, "" + demographic.getDemographicNo(), "" + null,EDocUtil.ATTACHED);
        ArrayList<LabResultData> labs = consultLabs.populateLabResultsDataCdxMessenger(loggedInInfo, ""+demographic.getDemographicNo(), null, CommonLabResultData.ATTACHED);
        for (int i = 0; i < attachmentlists.size(); ++i) {
            EDocUtil.updateAttachCdxDoc((attachmentlists.get(i)).getDocId(), ""+cdxMessenger.getId(),""+demographic.getDemographicNo());
            EDocUtil.detachCdxDoc((attachmentlists.get(i)).getDocId(), ""+cdxMessenger.getId(),""+demographic.getDemographicNo());
        }

        for (int i = 0; i < labs.size(); ++i) {
            EDocUtil.updateAttachCdxDoc((labs.get(i)).labPatientId, ""+cdxMessenger.getId(),""+demographic.getDemographicNo());
            EDocUtil.detachCdxDoc((labs.get(i)).labPatientId, ""+cdxMessenger.getId(),""+demographic.getDemographicNo());
        }

        // Try to update the document distribution status
        CDXDistribution cdxDistribution = new CDXDistribution();
        cdxDistribution.updateDistributionStatus(response.getDocumentID());

    }

    private boolean logResponse(IDocument doc) {
        boolean result = false;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String docStr = mapper.writeValueAsString(doc);
            MiscUtils.getLogger().info(docStr);
            result = true;
        } catch (JsonProcessingException e) {
            MiscUtils.getLogger().error(e.getMessage());
        }
        return result;
    }

    public ISubmitDoc addRecipient(ISubmitDoc iDoc, HashMap<String, String[]> specialistAndClinics, HashSet<String> allUniqueClinics, String typeofRecipient) {
        List<IProvider> providers = null;
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
        //HashMap<String,String> clinicInfo=(HashMap<String,String>)request.getSession().getAttribute("clinicInfo");

        //Getting all the selected primary recipients.
        for (String s : specialistAndClinics.keySet()) {
            String lastAndFirstName[] = s.split(" ", 2);
            providers = cdxSpecialist.findCdxSpecialistByName(lastAndFirstName[0].trim());

            for (IProvider provider : providers) {
                if (provider.getID() != null && !provider.getID().isEmpty() && provider.getFirstName() != null && !provider.getFirstName().isEmpty()) {
                    if (provider.getLastName().equalsIgnoreCase(lastAndFirstName[0].trim()) && provider.getFirstName().equalsIgnoreCase(lastAndFirstName[1].trim())) {

                        //Adding Multiple Recipients to the document.
                        if (typeofRecipient.equalsIgnoreCase("primary")) {
                            iDoc = iDoc.recipient().primary()
                                    .id(provider.getID())
                                    .name(NameType.LEGAL, provider.getFirstName(), provider.getLastName())
                                    .address(AddressType.WORK, provider.getStreetAddress(), provider.getCity(), provider.getProvince(), provider.getPostalCode(), "CA")
                                    .phone(TelcoType.WORK, String.valueOf(provider.getPhones()))
                                    .and();
                        } else if (typeofRecipient.equalsIgnoreCase("secondary")) {
                            iDoc = iDoc.recipient()
                                    .id(provider.getID())
                                    .name(NameType.LEGAL, provider.getFirstName(), provider.getLastName())
                                    .address(AddressType.WORK, provider.getStreetAddress(), provider.getCity(), provider.getProvince(), provider.getPostalCode(), "CA")
                                    .phone(TelcoType.WORK, String.valueOf(provider.getPhones()))
                                    .and();
                        }


                        String clinics[] = specialistAndClinics.get(s);

                        for (String c : clinics) {
                            if (c != null && !c.isEmpty()) {
                                //Adding all the clinics associated with all the primary recipients.
                                allUniqueClinics.add(c.trim());
                            }

                        }

                    }
                }
            }
        }


        return iDoc;

    }


    private ByteOutputStream createPdfForAttachments(LoggedInInfo loggedInInfo, String demoNo, String reqId) throws OBIBException {
        ArrayList<EDoc> docs = EDocUtil.listDocsForCdxMessenger(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
        String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        ArrayList<Object> alist = new ArrayList<Object>();
        byte[] buffer;
        ByteInputStream bis;
        ByteOutputStream bos = null;
        CommonLabResultData consultLabs = new CommonLabResultData();
        ArrayList<InputStream> streams = new ArrayList<InputStream>();

        ArrayList<LabResultData> labs = consultLabs.populateLabResultsDataCdxMessenger(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
        String error = "";
        Exception exception = null;
        try {
            boolean success = false;
            for (int i = 0; i < docs.size(); i++) {
                EDoc doc = docs.get(i);
                if (doc.isPrintable()) {
                    if (doc.isImage()) {
                        success = false;
                        bos = new ByteOutputStream();
                        String imagePath = path + doc.getFileName();
                        String imageTitle = doc.getDescription();
                        try {
                            imageToPdf(imagePath, imageTitle, bos);
                            success = true;
                        } catch (DocumentException e) {
                            MiscUtils.getLogger().error(e.getMessage());
                        }
                        if (success) {
                            buffer = bos.getBytes();
                            bis = new ByteInputStream(buffer, bos.getCount());
                            bos.close();
                            streams.add(bis);
                            alist.add(bis);
                        }
                    } else if (doc.isPDF()) {
                        alist.add(path + doc.getFileName());
                    }  else if (doc.isCDX()) {
                        success = false;
                        bos = new ByteOutputStream();
                        try {
                            cdxToPdf(doc,bos);
                            success = true;
                        } catch (OBIBException e) {
                            MiscUtils.getLogger().error(e.getMessage());
                            throw e;

                        }if (success) {
                            buffer = bos.getBytes();
                            bis = new ByteInputStream(buffer, bos.getCount());
                            bos.close();
                            streams.add(bis);
                            alist.add(bis);
                        }
                    }
                    else {
                        logger.error("CDXMessengerAction " + doc.getType() +
                                " is marked as printable but no means have been established to print it.");
                    }
                }
            }

            // Iterating over requested labs.
            for (int i = 0; labs != null && i < labs.size(); i++) {
                // Storing the lab in PDF format inside a byte stream.
                bos = new ByteOutputStream();
                LabPDFCreator lpdfc = new LabPDFCreator(bos, labs.get(i).segmentID, loggedInInfo.getLoggedInProviderNo());
                lpdfc.printPdf();

                // Transferring PDF to an input stream to be concatenated with
                // the rest of the documents.
                buffer = bos.getBytes();
                bis = new ByteInputStream(buffer, bos.getCount());
                bos.close();
                streams.add(bis);
                alist.add(bis);
            }
            if (alist.size() > 0) {
                bos = new ByteOutputStream();
                ConcatPDF.concat(alist, bos);
            }
        } catch (DocumentException de) {
            error = "DocumentException";
            exception = de;
        } catch (IOException ioe) {
            error = "IOException";
            exception = ioe;
        } finally {
            // Cleaning up InputStreams created for concatenation.
            for (InputStream is : streams) {
                try {
                    is.close();
                } catch (IOException e) {
                    error = "IOException";
                }
            }
        }
        if (!error.equals("")) {
            logger.error(error + " occured insided createPDF", exception);
        }
        return bos;
    }

    /**
     * Converts attached CDX document in the consultation request to PDF.
     *
     * @param os the output stream where the PDF will be written
     * @throws IOException       when an error with the output stream occurs
     * @throws DocumentException when an error in document construction occurs
     */

    private void cdxToPdf(EDoc doc, ByteOutputStream os) throws OBIBException {

        CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
        CdxProvenance provDoc = provenanceDao.findByDocumentNo(Integer.parseInt(doc.getDocId()));
        CdxAttachmentDao attachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);

        ArrayList<Object> streamList = new ArrayList<>();


        // transform main document from XML to HTML
        try {
            StringReader cdaReader = new StringReader(provDoc.getPayload());
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(contentRoute + "/share/xslt/CDA_to_HTML.xsl"));

            ByteOutputStream bos_html = new ByteOutputStream();
            transformer.transform(new StreamSource(cdaReader), new StreamResult(bos_html));

            String html = new String(bos_html.getBytes(), UTF_8);

            // transform main document from HTML to PDF

            HtmlToPdf htmlToPdf = HtmlToPdf.create()
                    .object(HtmlToPdfObject.forHtml(html));

            InputStream mainDoc = htmlToPdf.convert();
            streamList.add(mainDoc);
            mainDoc.close();

            // transform attachments to CDX document

            for (CdxAttachment att : attachmentDao.findByDocNo(provDoc.getId())) {
                InputStream attDoc = null;
                if (att.getAttachmentType().equals(AttachmentType.PDF.mediaType)) {
                    attDoc = new ByteArrayInputStream(att.getContent());
                } else if (att.getAttachmentType().equals(AttachmentType.JPEG.mediaType)
                        || att.getAttachmentType().equals(AttachmentType.PNG.mediaType)
                        || att.getAttachmentType().equals(AttachmentType.TIFF.mediaType)) {
                    ByteOutputStream aos = new ByteOutputStream();
                    imageToPdf(att.getContent(), aos);
                    aos.close();
                    byte[] buffer = aos.getBytes();
                    attDoc = new ByteArrayInputStream(buffer);
                } else if (att.getAttachmentType().equals(AttachmentType.RTF.mediaType)) {
                    Document document = new Document();
                    ByteOutputStream aos = new ByteOutputStream();
                    PdfWriter writer = PdfWriter.getInstance(document, aos);
                    document.open();
                    RtfParser parser = new RtfParser(null);
                    parser.convertRtfDocument(new ByteArrayInputStream(att.getContent()), document);
                    document.close();
                    aos.close();
                    byte[] buffer = aos.getBytes();
                    attDoc = new ByteArrayInputStream(buffer);
                } else throw new OBIBException("Unknown attachment type of CDX document ("
                        + att.getAttachmentType() + ")");
                streamList.add(attDoc);
            }
            ConcatPDF.concat(streamList, os);
        } catch (Exception e) {
            throw new OBIBException("Attachment document to PDF automatically. The document has *not* been sent.");
        }
    }

    /**
     * Converts attached image in the consultation request to PDF.
     *
     * @param os the output stream where the PDF will be written
     * @throws IOException       when an error with the output stream occurs
     * @throws DocumentException when an error in document construction occurs
     */
    private void imageToPdf(String imagePath, String imageTitle, OutputStream os) throws IOException, DocumentException {

        Image image;
        try {
            image = Image.getInstance(imagePath);
        } catch (Exception e) {
            logger.error("Unexpected error:", e);
            throw new DocumentException(e);
        }

        // Create the document we are going to write to
        Document document = new Document();
        // PdfWriter writer = PdfWriter.getInstance(document, os);
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_6PT);


        document.setPageSize(PageSize.LETTER);
        document.addCreator("OSCAR");
        document.open();

        int type = image.getOriginalType();
        if (type == Image.ORIGINAL_TIFF) {
            // The following is composed of code from com.lowagie.tools.plugins.Tiff2Pdf modified to create the
            // PDF in memory instead of on disk
            RandomAccessFileOrArray ra = new RandomAccessFileOrArray(imagePath);
            int comps = TiffImage.getNumberOfPages(ra);
            boolean adjustSize = false;
            PdfContentByte cb = writer.getDirectContent();
            for (int c = 0; c < comps; ++c) {
                Image img = TiffImage.getTiffImage(ra, c + 1);
                if (img != null) {
                    if (adjustSize) {
                        document.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
                        document.newPage();
                        img.setAbsolutePosition(0, 0);
                    } else {
                        if (img.getScaledWidth() > 500 || img.getScaledHeight() > 700) {
                            img.scaleToFit(500, 700);
                        }
                        img.setAbsolutePosition(20, 20);
                        document.newPage();
                        document.add(new Paragraph(imageTitle + " - page " + (c + 1)));
                    }
                    cb.addImage(img);

                }
            }
            ra.close();
        } else {
            PdfContentByte cb = writer.getDirectContent();
            if (image.getScaledWidth() > 500 || image.getScaledHeight() > 700) {
                image.scaleToFit(500, 700);
            }
            image.setAbsolutePosition(20, 20);
            cb.addImage(image);
        }
        document.close();
    }

    /**
     * Converts attached image in the consultation request to PDF.
     *
     * @param os the output stream where the PDF will be written
     * @throws IOException       when an error with the output stream occurs
     * @throws DocumentException when an error in document construction occurs
     */
    private void imageToPdf(byte[] content, OutputStream os) throws IOException, DocumentException {
        Image image = Image.getInstance(content);

        // Create the document we are going to write to
        Document document = new Document();
        // PdfWriter writer = PdfWriter.getInstance(document, os);
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_6PT);


        document.setPageSize(PageSize.LETTER);
        document.addCreator("OSCAR");
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        if (image.getScaledWidth() > 500 || image.getScaledHeight() > 700) {
            image.scaleToFit(500, 700);
        }
        image.setAbsolutePosition(20, 20);
        cb.addImage(image);
        document.close();
    }
}


