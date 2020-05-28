package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.send.IRecipient;
import ca.uvic.leadlab.obibconnector.facades.send.ISubmitDoc;
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.oscarehr.integration.cdx.dao.CdxMessengerDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxMessenger;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;
import oscar.util.ParameterActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

public class CDXMessengerAction extends DispatchAction {
    private static final Logger logger=MiscUtils.getLogger();
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        CdxMessenger cdxMessenger=saveMessage(request);
        try {
        doCdxSend(loggedInInfo,request,cdxMessenger);
        } catch (OBIBException e) {
            logger.error("Error sending CDX consultation request.", e);
            String additionalText = e.getObibMessage();
            if (additionalText == null || additionalText.isEmpty()) {
                additionalText = e.getMessage();
            }
            logger.error("Additional Message."+ additionalText);
        }

        return mapping.findForward("success");
    }


    private CdxMessenger saveMessage(HttpServletRequest request){

        CdxMessengerDao cdxMessengerDao= SpringUtils.getBean(CdxMessengerDao.class);
        CdxMessenger cdxMessenger = new CdxMessenger();
        String patient = request.getParameter("patientsearch");
        //String primaryrecipient = request.getParameter("precipients");
        String recipients[]=request.getParameterValues("precipients");
        String specialists="";
        if(recipients!=null && recipients.length>0){

            for (String rec:recipients){
                specialists=specialists+rec.split("@")[0]+",";
            }
            specialists= specialists.substring(0, specialists.length() - 1);
        }

        //String secondaryrecipient = request.getParameter("srecipients");
        String messagetype = request.getParameter("messagetype");
        String documenttype = request.getParameter("documenttype");
        String contentmessage = request.getParameter("contentmessage");
        if(patient!=null ||patient.length()!=0 ) {
            cdxMessenger.setPatient(patient);
            cdxMessenger.setRecipients(specialists);
            cdxMessenger.setCategory(messagetype);
            cdxMessenger.setContent(contentmessage);
            cdxMessenger.setDocumentType(documenttype);
            cdxMessenger.setAuthor(request.getSession().getAttribute("userfirstname") + "," + request.getSession().getAttribute("userlastname"));
            cdxMessenger.setTimeStamp(new Timestamp(new Date().getTime()));
            cdxMessenger.setDeliveryStatus("Unknown");
            try {
                cdxMessengerDao.persist(cdxMessenger);
                request.setAttribute("success", true);
            } catch (Exception ex) {
                request.setAttribute("success", false);
                MiscUtils.getLogger().error("Got exception saving messenger Information " + ex.getMessage());
            }

        }
        return cdxMessenger;

    }


    private void doCdxSend(LoggedInInfo loggedInInfo, HttpServletRequest request,CdxMessenger cdxMessenger) throws OBIBException {

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

        // Add pdf attachments (scanned images, lab reports and PDF files)
        String filename = null;
        byte[] newBytes = null;
        ByteOutputStream bos = createPdfForAttachments(loggedInInfo, "" + demographic.getDemographicNo(), "" + consultationRequestId);
        if (bos != null) {
            newBytes = bos.toByteArray();
            filename = "ConsultationRequestAttachedPDF-" + demographic.getLastName() + "," + demographic.getFirstName() + "-" + UtilDateUtilities.getToday("yyyy-MM-dd_HHmmss") + ".pdf";
            MiscUtils.getLogger().debug("File: " + filename + ", Size: " + newBytes.length);
        }

    */

        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        List<Demographic> demoList=null;
        String patient = request.getParameter("patientsearch");
        demoList=demographicDao.searchDemographic(patient);

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
        String specialists[]=cdxMessenger.getRecipients().split(",")[0].split(" ");
        List<ProfessionalSpecialist> professionalSpecialistList = professionalSpecialistDao.findByFullName(specialists[0],specialists[1]);
        ProfessionalSpecialist professionalSpecialist=professionalSpecialistList.get(0);
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



        IRecipient recipient = doc.documentType(DocumentType.PROGRESS_NOTE)
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
                .and().recipient()
                .primary()
                .id(recipientId)
                .name(NameType.LEGAL, professionalSpecialist.getFirstName(), professionalSpecialist.getLastName())
                .address(AddressType.WORK, professionalSpecialist.getAddress(), professionalSpecialist.getCity(), professionalSpecialist.getProvince(), professionalSpecialist.getPostal(), "CA")
                .phone(TelcoType.WORK, professionalSpecialist.getPhoneNumber())
                .recipientOrganization("cdxpostprod-otca","Oscar Test Clinic A");




       /* if (clinicId!=null && !clinicId.equalsIgnoreCase("1")  )
        {
            CdxClinicsDao cdxClinicsDao=SpringUtils.getBean(CdxClinicsDao.class);
            CdxClinics c=cdxClinicsDao.findByClinicId(clinicId);
            recipient.recipientOrganization(clinicId,c.getClinicName());
        }
*/



        recipient.and()
                .receiverId(clinicID)
                .content(cdxMessenger.getContent());

        response = doc.submit();

        boolean debug = false;
        if (debug) logResponse(response);
        MiscUtils.getLogger().debug("Attempting to save document using logSentAction");
        CdxProvenanceDao cdxProvenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
        cdxProvenanceDao.logSentAction(response);

        // Try to update the document distribution status
        CDXDistribution cdxDistribution = new CDXDistribution();
        cdxDistribution.updateDistributionStatus(response.getDocumentID());

        // Set status to pending specialist callback

       /* if (isCancel) {
            consultationRequest.setStatus("1");
        } else if (consultationRequest.getStatus().equals("1")){
            consultationRequest.setStatus("3");
        }
        consultationRequestDao.merge(consultationRequest);

        */
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

}
