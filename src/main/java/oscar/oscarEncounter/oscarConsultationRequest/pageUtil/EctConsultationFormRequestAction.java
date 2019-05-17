/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.send.ISubmitDoc;
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.RefI12;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.model.*;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.integration.cdx.CDXConfiguration;
import org.oscarehr.integration.cdx.CDXSpecialist;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.*;
import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


public class EctConsultationFormRequestAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "w", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		EctConsultationFormRequestForm frm = (EctConsultationFormRequestForm) form;		

		String appointmentHour = frm.getAppointmentHour();
		String appointmentPm = frm.getAppointmentPm();

		if (appointmentPm.equals("PM") && Integer.parseInt(appointmentHour) < 12 ) {
			appointmentHour = Integer.toString(Integer.parseInt(appointmentHour) + 12);
		}
                else if( appointmentHour.equals("12") && appointmentPm.equals("AM") ) {
                    appointmentHour = "0";
                }		

		String sendTo = frm.getSendTo();
		String submission = frm.getSubmission();
		String providerNo = frm.getProviderNo();
		String demographicNo = frm.getDemographicNo();

		String requestId = "";

		boolean newSignature = request.getParameter("newSignature") != null && request.getParameter("newSignature").equalsIgnoreCase("true");
		String signatureId = null;
		String signatureImg = frm.getSignatureImg();
		if(StringUtils.isBlank(signatureImg)) {
			signatureImg = request.getParameter("newSignatureImg");
			if(signatureImg ==null) 
				signatureImg = "";
		}
	
		
        ConsultationRequestDao consultationRequestDao=(ConsultationRequestDao)SpringUtils.getBean("consultationRequestDao");
        ConsultationRequestExtDao consultationRequestExtDao=(ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
        ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
        
        String[] format = new String[] {"yyyy-MM-dd","yyyy/MM/dd"};

		if (submission.startsWith("Submit")) {

			try {				
								if (newSignature) {
									DigitalSignature signature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(loggedInInfo, signatureImg, Integer.parseInt(demographicNo));
									if (signature != null) { signatureId = "" + signature.getId(); }
								}
				
								
                                ConsultationRequest consult = new ConsultationRequest();
                                Date date = DateUtils.parseDate(frm.getReferalDate(), format);
                                consult.setReferralDate(date);
                                consult.setServiceId(new Integer(frm.getService()));

                                consult.setSignatureImg(signatureId);
                                
                        		consult.setLetterheadName(frm.getLetterheadName());
                        		consult.setLetterheadAddress(frm.getLetterheadAddress());
                        		consult.setLetterheadPhone(frm.getLetterheadPhone());
                        		consult.setLetterheadFax(frm.getLetterheadFax());
                        		
								if (frm.getAppointmentDate() != null && !frm.getAppointmentDate().equals("")) {
									date = DateUtils.parseDate(frm.getAppointmentDate(), format);
									consult.setAppointmentDate(date);
									
									if(!StringUtils.isEmpty(appointmentHour) && !StringUtils.isEmpty(frm.getAppointmentMinute())) {
										try {
											date = DateUtils.setHours(date, new Integer(appointmentHour));
											date = DateUtils.setMinutes(date, new Integer(frm.getAppointmentMinute()));
											consult.setAppointmentTime(date);
										}
										catch(NumberFormatException nfEx) {
							                MiscUtils.getLogger().error("Invalid Time", nfEx);
										}
									}
								}
                                consult.setReasonForReferral(frm.getReasonForConsultation());
                                consult.setClinicalInfo(frm.getClinicalInformation());
                                consult.setCurrentMeds(frm.getCurrentMedications());
                                consult.setAllergies(frm.getAllergies());
                                consult.setProviderNo(frm.getProviderNo());
                                consult.setDemographicId(new Integer(frm.getDemographicNo()));
                                consult.setStatus(frm.getStatus());
                                consult.setStatusText(frm.getAppointmentNotes());
                                consult.setSendTo(frm.getSendTo());
                                consult.setConcurrentProblems(frm.getConcurrentProblems());
                                consult.setUrgency(frm.getUrgency());
                                consult.setAppointmentInstructions( frm.getAppointmentInstructions() );
                                consult.setSiteName(frm.getSiteName());
                                Boolean pWillBook = false;
                                if( frm.getPatientWillBook() != null ) {
                                    pWillBook = frm.getPatientWillBook().equals("1");
                                }
                                consult.setPatientWillBook(pWillBook);

                                if( frm.getFollowUpDate() != null && !frm.getFollowUpDate().equals("") ) {
                                    date = DateUtils.parseDate(frm.getFollowUpDate(), format);
                                    consult.setFollowUpDate(date);
                                }

                                if(frm.getSource()!=null && !"null".equals(frm.getSource())) {
                                	consult.setSource(frm.getSource());
                                } else {
                                	consult.setSource("");
                                }

                                consultationRequestDao.persist(consult);

                                    Integer specId = new Integer(frm.getSpecialist());
                                    ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(specId);
                                    if( professionalSpecialist != null ) {
                                        consult.setProfessionalSpecialist(professionalSpecialist);
                                        consultationRequestDao.merge(consult);
                                    }
                                        MiscUtils.getLogger().debug("saved new consult id "+ consult.getId());
                                        requestId = String.valueOf(consult.getId());
                                        
                                Enumeration e = request.getParameterNames();
                                while(e.hasMoreElements()) {
                                	String name = (String)e.nextElement();
                                	if(name.startsWith("ext_")) {
                                		String value = request.getParameter(name);
                                		consultationRequestExtDao.persist(createExtEntry(requestId,name.substring(name.indexOf("_")+1),value));
                                	}
                                }
                                // now that we have consultation id we can save any attached docs as well
								// format of input is D2|L2 for doc and lab
								String[] docs = frm.getDocuments().split("\\|");
			
								for (int idx = 0; idx < docs.length; ++idx) {
									if (docs[idx].length() > 0) {
										if (docs[idx].charAt(0) == 'D') EDocUtil.attachDocConsult(providerNo, docs[idx].substring(1), requestId);
										else if (docs[idx].charAt(0) == 'L') ConsultationAttachLabs.attachLabConsult(providerNo, docs[idx].substring(1), requestId);
									}
								}
			}
	        catch (ParseException e) {
	                MiscUtils.getLogger().error("Invalid Date", e);
	        }


			request.setAttribute("transType", "2");

		} else

		if (submission.startsWith("Update")) {

			requestId = frm.getRequestId();

			try {				     
				
				if (newSignature) {
					DigitalSignature signature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(loggedInInfo, signatureImg, Integer.parseInt(demographicNo));
					if (signature != null) {
						signatureId = "" + signature.getId();
					} else {
						signatureId = null;
					}
				} else {
					signatureId = signatureImg;
				}
				
                ConsultationRequest consult = consultationRequestDao.find(new Integer(requestId));
                Date date = DateUtils.parseDate(frm.getReferalDate(), format);
                consult.setReferralDate(date);
                consult.setServiceId(new Integer(frm.getService()));

                consult.setSignatureImg(signatureId);
                
                //We shouldn't change the referral provider just because someone updated and printed it! 
                //consult.setProviderNo(frm.getProviderNo());
                
        		consult.setLetterheadName(frm.getLetterheadName());
        		consult.setLetterheadAddress(frm.getLetterheadAddress());
        		consult.setLetterheadPhone(frm.getLetterheadPhone());
        		consult.setLetterheadFax(frm.getLetterheadFax());

                /*
                 * If Consultant: was changed to "blank/No Consultant Saved" we
                 * don't want to try and create an Integer out of the specId as
                 * it will throw a NumberForamtException
                */
                String specIdStr = frm.getSpecialist();
                ProfessionalSpecialist professionalSpecialist=null;

                if (specIdStr != null && !specIdStr.isEmpty())
                {
                    Integer specId = new Integer(frm.getSpecialist());
                    professionalSpecialist=professionalSpecialistDao.find(specId);
                }
                consult.setProfessionalSpecialist(professionalSpecialist);

                if( frm.getAppointmentDate() != null && !frm.getAppointmentDate().equals("") ) {
                	date = DateUtils.parseDate(frm.getAppointmentDate(), format);
                	consult.setAppointmentDate(date);
			try {
	                	date = DateUtils.setHours(date, new Integer(appointmentHour));
        	        	date = DateUtils.setMinutes(date, new Integer(frm.getAppointmentMinute()));
                		consult.setAppointmentTime(date);
			}catch(NumberFormatException nfEx) {
				MiscUtils.getLogger().error("Invalid Time", nfEx);
			} catch(IllegalArgumentException e) {
				MiscUtils.getLogger().error("Invalid Time", e);
			}
                }
                consult.setReasonForReferral(frm.getReasonForConsultation());
                consult.setClinicalInfo(frm.getClinicalInformation());
                consult.setCurrentMeds(frm.getCurrentMedications());
                consult.setAllergies(frm.getAllergies());
                consult.setDemographicId(new Integer(frm.getDemographicNo()));
                consult.setStatus(frm.getStatus());
                consult.setStatusText(frm.getAppointmentNotes());
                consult.setSendTo(frm.getSendTo());
                consult.setConcurrentProblems(frm.getConcurrentProblems());
                consult.setUrgency(frm.getUrgency());
                consult.setAppointmentInstructions( frm.getAppointmentInstructions() );
                consult.setSiteName(frm.getSiteName());
                 Boolean pWillBook = false;
                if( frm.getPatientWillBook() != null ) {
                    pWillBook = frm.getPatientWillBook().equals("1");
                }
                consult.setPatientWillBook(pWillBook);

                if( frm.getFollowUpDate() != null && !frm.getFollowUpDate().equals("") ) {
                    date = DateUtils.parseDate(frm.getFollowUpDate(), format);
                    consult.setFollowUpDate(date);
                }
                
                if(frm.getSource()!=null && !"null".equals(frm.getSource())) {
                	consult.setSource(frm.getSource());
                } else {
                	consult.setSource("");
                }
                
                consultationRequestDao.merge(consult);
                
                consultationRequestExtDao.clear(Integer.parseInt(requestId));
                Enumeration e = request.getParameterNames();
                while(e.hasMoreElements()) {
                	String name = (String)e.nextElement();
                	if(name.startsWith("ext_")) {
                		String value = request.getParameter(name);
                		consultationRequestExtDao.persist(createExtEntry(requestId,name.substring(name.indexOf("_")+1),value));
                	}
                }
			}

			catch (ParseException e) {
				MiscUtils.getLogger().error("Error", e);
			}

			request.setAttribute("transType", "1");

		}
		else if( submission.equalsIgnoreCase("And Print Preview")) {
			requestId = frm.getRequestId();
		}
				

		frm.setRequestId("");

		request.setAttribute("teamVar", sendTo);

		if (submission.endsWith("And Print Preview")) {

			request.setAttribute("reqId", requestId);
			if (OscarProperties.getInstance().isConsultationFaxEnabled()) {
				return mapping.findForward("printIndivica");
			}
			else if (IsPropertiesOn.propertiesOn("CONSULT_PRINT_PDF")) {
				return mapping.findForward("printpdf");
			} else if (IsPropertiesOn.propertiesOn("CONSULT_PRINT_ALT")) {
				return mapping.findForward("printalt");
			} else {
				return mapping.findForward("print");
			}

		} else if (submission.endsWith("And Fax")) {

			request.setAttribute("reqId", requestId);
			if (OscarProperties.getInstance().isConsultationFaxEnabled()) {
				return mapping.findForward("faxIndivica");
			}	
			else {
				return mapping.findForward("fax");
			}

		} 
		else if (submission.endsWith("esend"))
		{
			// upon success continue as normal with success message
			// upon failure, go to consultation update page with message
			try {
	            doHl7Send(loggedInInfo, Integer.parseInt(requestId));
	            WebUtils.addLocalisedInfoMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESent");
            } catch (Exception e) {
            	logger.error("Error contacting remote server.", e);
            	
            	WebUtils.addLocalisedErrorMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESendError");
	    		ParameterActionForward forward = new ParameterActionForward(mapping.findForward("failESend"));
	    		forward.addParameter("de", demographicNo);
	    		forward.addParameter("requestId", requestId);
	    		return forward;
            }
		} else if (submission.endsWith("And Save")) {  //for BC CDX messages
			// upon success continue as normal with success message
			// upon failure, go to consultation update page with message
			ConsultationRequest consultationRequest=consultationRequestDao.find(Integer.parseInt(requestId));
			ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
			if (!professionalSpecialist.getCdxCapable()) {
				WebUtils.addLocalisedErrorMessage(request, "oscarEncounter.oscarConsultationRequest.SelectProfessionalSpecialist.msgSelectionError" );
				ParameterActionForward forward = new ParameterActionForward(mapping.findForward("failESend"));
				forward.addParameter("de", demographicNo);
				forward.addParameter("requestId", requestId);
				return forward;
			}
			try {
				doCdxSend(loggedInInfo, Integer.parseInt(requestId));
				WebUtils.addLocalisedInfoMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCdxCreatedUpdateESent");
			} catch (OBIBException e) {
				logger.error("Error sending CDX consultation request.", e);
				WebUtils.addLocalisedErrorMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCdxCreatedUpdateESendError");
				ParameterActionForward forward = new ParameterActionForward(mapping.findForward("failESend"));
				forward.addParameter("de", demographicNo);
				forward.addParameter("requestId", requestId);
				return forward;
			}
		}

		ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
		forward.addParameter("de", demographicNo);
		return forward;
	}
	
	private ConsultationRequestExt createExtEntry(String requestId, String name,String value) {
		ConsultationRequestExt obj = new ConsultationRequestExt();
		obj.setDateCreated(new Date());
		obj.setKey(name);
		obj.setValue(value);
		obj.setRequestId(Integer.parseInt(requestId));
		return obj;
	}
	
	private void doHl7Send(LoggedInInfo loggedInInfo, Integer consultationRequestId) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception, ServletException {
		
	    ConsultationRequestDao consultationRequestDao=(ConsultationRequestDao)SpringUtils.getBean("consultationRequestDao");
	    ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
	    Hl7TextInfoDao hl7TextInfoDao=(Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	    ClinicDAO clinicDAO=(ClinicDAO)SpringUtils.getBean("clinicDAO");

	    ConsultationRequest consultationRequest=consultationRequestDao.find(consultationRequestId);
	    ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
	    Clinic clinic=clinicDAO.getClinic();
	    
	    // set status now so the remote version shows this status
	    consultationRequest.setStatus("2");

	    REF_I12 refI12=RefI12.makeRefI12(clinic, consultationRequest);
	    SendingUtils.send(loggedInInfo, refI12, professionalSpecialist);
	    
	    // save after the sending just in case the sending fails.
	    consultationRequestDao.merge(consultationRequest);
	    
	    //--- send attachments ---
    	Provider sendingProvider=loggedInInfo.getLoggedInProvider();
    	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
    	Demographic demographic=demographicManager.getDemographic(loggedInInfo, consultationRequest.getDemographicId());

    	//--- process all documents ---
	    ArrayList<EDoc> attachments=EDocUtil.listDocs(loggedInInfo, demographic.getDemographicNo().toString(), consultationRequest.getId().toString(), EDocUtil.ATTACHED);
	    for (EDoc attachment : attachments)
	    {
	        ObservationData observationData=new ObservationData();
	        observationData.subject=attachment.getDescription();
	        observationData.textMessage="Attachment for consultation : "+consultationRequestId;
	        observationData.binaryDataFileName=attachment.getFileName();
	        observationData.binaryData=attachment.getFileBytes();

	        ORU_R01 hl7Message=OruR01.makeOruR01(clinic, demographic, observationData, sendingProvider, professionalSpecialist);        
	        SendingUtils.send(loggedInInfo, hl7Message, professionalSpecialist);	    	
	    }
	    
	    //--- process all labs ---
        CommonLabResultData labData = new CommonLabResultData();
        ArrayList<LabResultData> labs = labData.populateLabResultsData(loggedInInfo, demographic.getDemographicNo().toString(), consultationRequest.getId().toString(), CommonLabResultData.ATTACHED);
	    for (LabResultData attachment : labs)
	    {
	    	try {
	            byte[] dataBytes=LabPDFCreator.getPdfBytes(attachment.getSegmentID(), sendingProvider.getProviderNo());
	            Hl7TextInfo hl7TextInfo=hl7TextInfoDao.findLabId(Integer.parseInt(attachment.getSegmentID()));
	            
	            ObservationData observationData=new ObservationData();
	            observationData.subject=hl7TextInfo.getDiscipline();
	            observationData.textMessage="Attachment for consultation : "+consultationRequestId;
	            observationData.binaryDataFileName=hl7TextInfo.getDiscipline()+".pdf";
	            observationData.binaryData=dataBytes;

	            
	            ORU_R01 hl7Message=OruR01.makeOruR01(clinic, demographic, observationData, sendingProvider, professionalSpecialist);        
	            int statusCode=SendingUtils.send(loggedInInfo, hl7Message, professionalSpecialist);
	            if (HttpServletResponse.SC_OK!=statusCode) throw(new ServletException("Error, received status code:"+statusCode));
            } catch (DocumentException e) {
	            logger.error("Unexpected error.", e);
            }	    	
	    }
    }

	private void doCdxSend(LoggedInInfo loggedInInfo, Integer consultationRequestId) throws OBIBException {

		ConsultationRequestDao consultationRequestDao = (ConsultationRequestDao) SpringUtils.getBean("consultationRequestDao");
		ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
//		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
		ClinicDAO clinicDAO = (ClinicDAO) SpringUtils.getBean("clinicDAO");

		ConsultationRequest consultationRequest = consultationRequestDao.find(consultationRequestId);
		ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.find(consultationRequest.getSpecialistId());
		Clinic clinic = clinicDAO.getClinic();

		// set status now so the remote version shows this status
		consultationRequest.setStatus("2");

		//REF_I12 refI12 = RefI12.makeRefI12(clinic, consultationRequest);
		String message = fillReferralNotes(consultationRequest);

		// save after the sending just in case the sending fails.
		consultationRequestDao.merge(consultationRequest);

		Provider sendingProvider = loggedInInfo.getLoggedInProvider();
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		Demographic demographic = demographicManager.getDemographic(loggedInInfo, consultationRequest.getDemographicId());

		// Create pdf version of Consultation Request which can be attached to request.
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String filename = null;
//		EctConsultationFormRequestPrintPdf pdf = new EctConsultationFormRequestPrintPdf(consultationRequestId.toString(), professionalSpecialist.getAddress(), professionalSpecialist.getPhone(), professionalSpecialist.getFax(), demographic.getDemographicNo().toString());
        byte[] newBytes = null;
		try {
			filename = createPDF2(loggedInInfo, ""+demographic.getDemographicNo(), ""+consultationRequestId); //pdf.printPdf(loggedInInfo);
			if (filename != null) {
				newBytes = pdfFileToByteArray(path + filename);
				MiscUtils.getLogger().info("Consultation Request PDF: " + path + filename);
			}
		} catch (IOException e) {
			MiscUtils.getLogger().info(e.getMessage());
		}

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
		} else {
			MiscUtils.getLogger().error("Sending providers CDX ID not found");
			throw new OBIBException("Sending providers CDX ID not found");
		}

		IDocument response = null;
		CDXConfiguration cdxConfig = new CDXConfiguration();
		SubmitDoc submitDoc = new SubmitDoc(cdxConfig);

//		ArrayList<Object> pdfDocs = new ArrayList<Object>();
//		ArrayList<EDoc> privatedocs = new ArrayList<EDoc>();
//		privatedocs = EDocUtil.listDocs(loggedInInfo, ""+demographic.getDemographicNo(), ""+consultationRequestId, EDocUtil.ATTACHED);
//		EDoc curDoc;
//		for (int idx = 0; idx < privatedocs.size(); ++idx) {
//			curDoc = (EDoc) privatedocs.get(idx);
//			if (curDoc.isPDF()) {
//				pdfDocs.add(curDoc);
//			}
//			MiscUtils.getLogger().info("curDoc.getDocId(): " + curDoc.getDocId() + " curDoc.getDescription: " + curDoc.getDescription() +
//					" curDoc.getContentType: " + curDoc.getContentType());
//		}
//		CommonLabResultData labData = new CommonLabResultData();
//		ArrayList labs = labData.populateLabResultsData(loggedInInfo, ""+demographic.getDemographicNo(), ""+consultationRequestId, CommonLabResultData.ATTACHED);
//		LabResultData resData;
//		for (int idx = 0; idx < labs.size(); ++idx) {
//			resData = (LabResultData) labs.get(idx);
//			MiscUtils.getLogger().info("lab discipline: " + resData.getDiscipline() + " datetime: " + resData.getDateTime());
//		}

//		response = submitDoc.newDoc()
        ISubmitDoc doc = submitDoc.newDoc()
                .documentType(DocumentType.REFERRAL_NOTE)
				.inFulfillmentOf()
					.id(Integer.toString(consultationRequestId))
					.statusCode(OrderStatus.ACTIVE).and()
				.patient()
					.id(patientId)
					.name(NameType.LEGAL, demographic.getFirstName(), demographic.getLastName())
					.address(AddressType.HOME, demographic.getAddress(), demographic.getCity(), demographic.getProvince(), demographic.getPostal(), "CA")
					.phone(TelcoType.HOME, demographic.getPhone())
					.birthday(demographic.getYearOfBirth(),demographic.getMonthOfBirth(),demographic.getDateOfBirth())
					.gender("M".equalsIgnoreCase(demographic.getSex()) ? Gender.MALE : Gender.FEMALE)
				.and().author()
					.id(authorId)
					.time(new Date())
					.name(NameType.LEGAL, sendingProvider.getFirstName(), sendingProvider.getLastName())
					.address(AddressType.HOME, clinic.getAddress(), clinic.getCity(), clinic.getProvince(), clinic.getPostal(), "CA")
					.phone(TelcoType.HOME, clinic.getPhone())
				.and().recipient()
					.primary()
					.id(recipientId)
					.name(NameType.LEGAL, professionalSpecialist.getFirstName(), professionalSpecialist.getLastName())
					.address(AddressType.HOME, professionalSpecialist.getAddress(), professionalSpecialist.getCity(), professionalSpecialist.getProvince(), professionalSpecialist.getPostal(), "CA")
					.phone(TelcoType.HOME, professionalSpecialist.getPhoneNumber())
				.and().inFulfillmentOf()
					.id(Integer.toString(consultationRequestId))
				.and()
					.receiverId(clinicID)
					.content(message);
        if (filename != null && newBytes != null) {
			doc = doc
					.attach(AttachmentType.PDF, filename, newBytes);
		}
				response = doc.submit();

		//logResponse(response);
		MiscUtils.getLogger().info("Attempting to save document using logSentAction");
        CdxProvenanceDao cdxProvenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
		cdxProvenanceDao.logSentAction(response);

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

	private String fillReferralNotes(ConsultationRequest consultationRequest) {

		StringBuilder sb = new StringBuilder();
		String temp = consultationRequest.getReasonForReferral();
		String br ="\r\n";
		if (temp != null && !temp.trim().isEmpty()) {
			sb.append("REASON FOR CONSULTATION: " + temp + br);
		}

		temp=consultationRequest.getClinicalInfo();
		if (temp!=null && !temp.trim().isEmpty()) {
			sb.append("CLINICAL INFORMATION: " + temp  + br);
		}

		temp=consultationRequest.getConcurrentProblems();
		if (temp!=null && !temp.trim().isEmpty()) {
			sb.append("CONCURRENT PROBLEMS: " + temp + br);
		}

		temp=consultationRequest.getCurrentMeds();
		if (temp!=null && !temp.trim().isEmpty()) {
			sb.append("CURRENT MEDICATIONS: " + temp + br);
		}

		temp=consultationRequest.getAllergies();
		if (temp!=null && !temp.trim().isEmpty()) {
			sb.append("ALLERGIES: " + temp);
		}

		return sb.toString();
	}

	private byte[] pdfFileToByteArray(String filename) throws IOException {
		byte[] bytes = null;
		if (filename != null && !filename.isEmpty()) {
			File file = new File(filename);
			bytes = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		}
        return bytes;
	}

	private String combinePDFs(LoggedInInfo loggedInInfo, String demoNo, String reqId) throws IOException{
		//Create new file to save attachments to
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String fileName = path + "ConsultationRequestForm-"+ UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss")+".pdf";
		FileOutputStream out = new FileOutputStream(fileName);

		ArrayList<EDoc> consultdocs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		ArrayList<Object> pdfDocs = new ArrayList<Object>();

		for (int i=0; i < consultdocs.size(); i++){
			EDoc curDoc =  consultdocs.get(i);
			if ( curDoc.isPDF() ) {
				pdfDocs.add(curDoc.getFilePath());
			}
		}
		// TODO:need to do something about the docs that are not PDFs
		// create pdfs from attached labs
		try {
            CommonLabResultData labData = new CommonLabResultData();
            ArrayList labs = labData.populateLabResultsData(loggedInInfo, ""+demoNo, reqId, CommonLabResultData.ATTACHED);
            LabResultData resData;
            for (int idx = 0; idx < labs.size(); ++idx) {
                resData = (LabResultData) labs.get(idx);
                String segmentId = resData.getSegmentID();
				MessageHandler handler = Factory.getHandler(segmentId);
				String pdfFileName = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"//"+handler.getPatientName().replaceAll("\\s", "_")+"_"+handler.getMsgDate()+"_LabReport.pdf";
				OutputStream os = new FileOutputStream(pdfFileName);
				LabPDFCreator pdf = new LabPDFCreator(os, segmentId, loggedInInfo.getLoggedInProviderNo());
				pdf.printPdf();
				pdfDocs.add(pdfFileName);
			}
		}catch(DocumentException de) {
			MiscUtils.getLogger().error("PDF generation error: " + de.getMessage());
		}catch(IOException ioe) {
			MiscUtils.getLogger().error("PDF generation error: " + ioe.getMessage());
		}catch(Exception e){
			MiscUtils.getLogger().error("PDF generation error: " + e.getMessage());
		}
		ConcatPDF.concat(pdfDocs,out);
		return fileName;
	}

	private String createPDF(LoggedInInfo loggedInInfo, String demoNo, String reqId) throws IOException {
		//Create new file to save attachments to
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String fileName = path + "ConsultationRequestForm-"+ UtilDateUtilities.getToday("yyyy-MM-dd_hhmmss")+".pdf";
		FileOutputStream out = new FileOutputStream(fileName);

		ArrayList<EDoc> consultdocs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		ArrayList<Object> pdfDocs = new ArrayList<Object>();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();

		byte[] buffer;
		ByteInputStream bis;
		ByteOutputStream bos;

		for (int i=0; i < consultdocs.size(); i++){
			EDoc curDoc =  consultdocs.get(i);
			if ( curDoc.isPDF() ) {
				pdfDocs.add(curDoc.getFilePath());
			} else if (curDoc.isImage()) {
				boolean success = false;
				bos = new ByteOutputStream();
				String imagePath = path + curDoc.getFileName();
				String imageTitle = curDoc.getDescription();
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
					pdfDocs.add(bis);
				}
			}
		}
		// create pdfs from attached labs
		try {
            CommonLabResultData labData = new CommonLabResultData();
            ArrayList labs = labData.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
            LabResultData resData;
            for (int idx = 0; idx < labs.size(); ++idx) {
                resData = (LabResultData) labs.get(idx);
                String segmentId = resData.getSegmentID();
                MessageHandler handler = Factory.getHandler(segmentId);
                String pdfFileName = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"//"+handler.getPatientName().replaceAll("\\s", "_")+"_"+handler.getMsgDate()+"_LabReport.pdf";
                OutputStream os = new FileOutputStream(pdfFileName);
                LabPDFCreator pdf = new LabPDFCreator(os, segmentId, loggedInInfo.getLoggedInProviderNo());
                pdf.printPdf();
                pdfDocs.add(pdfFileName);
			}
		}catch(DocumentException de) {
			MiscUtils.getLogger().error("PDF generation error: " + de.getMessage());
		}
		ConcatPDF.concat(pdfDocs,out);
		return fileName;
	}

    private String createPDF2(LoggedInInfo loggedInInfo, String demoNo, String reqId) throws IOException {

    ArrayList<EDoc> docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
    String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
    String fileName = null;
    ArrayList<Object> alist = new ArrayList<Object>();
    byte[] buffer;
    ByteInputStream bis;
    ByteOutputStream bos;
    CommonLabResultData consultLabs = new CommonLabResultData();
    ArrayList<InputStream> streams = new ArrayList<InputStream>();

    ArrayList<LabResultData> labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
    String error = "";
    Exception exception = null;
    try {

/*
        bos = new ByteOutputStream();
//        ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request,bos);
//        cpdfc.printPdf(loggedInInfo);

        buffer = bos.getBytes();
        bis = new ByteInputStream(buffer, bos.getCount());
        bos.close();
        streams.add(bis);
        alist.add(bis);
*/

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
                } else {
                    logger.error("EctConsultationFormRequestAction: " + doc.getType() +
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
//            String filename = "combinedPDF-"
//                    + UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
//                    + ".pdf";
//            response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
//        }
        fileName = "ConsultationRequestAttachedPDF-" + UtilDateUtilities.getToday("yyyy-MM-dd_hhmmss") + ".pdf";
        FileOutputStream out = new FileOutputStream(path + fileName);
        out.write(bos.getBytes(), 0, bos.getCount());
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
        logger.error(error + " occured insided ConsultationPrintAction", exception);
//        request.setAttribute("printError", new Boolean(true));
//        return mapping.findForward("error");
    }
    return fileName;

}

		/**
         * Converts attached image in the consultation request to PDF.
         * @param os the output stream where the PDF will be written
         * @throws IOException when an error with the output stream occurs
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
					}
					else {
						if (img.getScaledWidth() > 500 || img.getScaledHeight() > 700) {
							img.scaleToFit(500, 700);
						}
						img.setAbsolutePosition(20, 20);
						document.newPage();
						document.add(new Paragraph(imageTitle +" - page " + (c + 1)));
					}
					cb.addImage(img);

				}
			}
			ra.close();
		}
		else {
			PdfContentByte cb = writer.getDirectContent();
			if (image.getScaledWidth() > 500 || image.getScaledHeight() > 700) {
				image.scaleToFit(500, 700);
			}
			image.setAbsolutePosition(20, 20);
			cb.addImage(image);
		}
		document.close();
	}
}
