/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.RefI12;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.StreetAddressDataHolder;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.util.ParameterActionForward;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.REF_I12;

public class EctConsultationFormRequestAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		EctConsultationFormRequestForm frm = (EctConsultationFormRequestForm) form;

		String referalDate = frm.getReferalDate();
		String serviceId = frm.getService();
		String specId = frm.getSpecialist();

		String appointmentDate = frm.getAppointmentYear() + "/" + frm.getAppointmentMonth() + "/" + frm.getAppointmentDay();

		String appointmentHour = frm.getAppointmentHour();
		String appointmentPm = frm.getAppointmentPm();

		if (appointmentPm.equals("PM")) {
			appointmentHour = Integer.toString(Integer.parseInt(appointmentHour) + 12);
		}

		String appointmentTime = appointmentHour + ":" + frm.getAppointmentMinute();

		String reason = frm.getReasonForConsultation();
		String clinicalInfo = frm.getClinicalInformation();
		String currentMeds = frm.getCurrentMedications();
		String allergies = frm.getAllergies();
		String concurrentProblems = frm.getConcurrentProblems();
		String sendTo = frm.getSendTo();
		String submission = frm.getSubmission();
		String providerNo = frm.getProviderNo();
		String demographicNo = frm.getDemographicNo();
		String status = frm.getStatus();
		String statusText = frm.getAppointmentNotes();

		String pwb = frm.patientWillBook;

		String urg = frm.getUrgency();

		String requestId = "";

		MsgStringQuote str = new MsgStringQuote();

		if (submission.startsWith("Submit")) {

			try {
				DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

				String sql = "insert into consultationRequests (referalDate,serviceId,specId,appointmentDate,appointmentTime,reason,clinicalInfo,currentMeds,allergies,providerNo,demographicNo,status,statusText,sendTo,concurrentProblems,urgency,patientWillBook) "
				        + " values ('"
				        + str.q(referalDate)
				        + "','"
				        + str.q(serviceId)
				        + "','"
				        + str.q(specId)
				        + "','"
				        + str.q(appointmentDate)
				        + "','"
				        + str.q(appointmentTime)
				        + "','"
				        + str.q(reason)
				        + "','"
				        + str.q(clinicalInfo)
				        + "','"
				        + str.q(currentMeds)
				        + "','"
				        + str.q(allergies) + "','" + str.q(providerNo) + "','" + str.q(demographicNo) + "','" + str.q(status) + "','" + str.q(statusText) + "','" + str.q(sendTo) + "','" + str.q(concurrentProblems) + "','" + urg + "'," + pwb + ")";

				db.RunSQL(sql);

				/* select the correct db specific command */

				String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
				String dbSpecificCommand;

				if (db_type.equalsIgnoreCase("mysql")) {
					dbSpecificCommand = "SELECT LAST_INSERT_ID()";
				} else if (db_type.equalsIgnoreCase("postgresql")) {
					dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
				} else {
					throw new SQLException("ERROR: Database " + db_type + " unrecognized.");
				}

				ResultSet rs = db.GetSQL(dbSpecificCommand);

				if (rs.next()) {

					requestId = rs.getString(1);

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

			}

			catch (SQLException e) {
				e.printStackTrace();
			}

			request.setAttribute("transType", "2");

		} else

		if (submission.startsWith("Update")) {

			requestId = frm.getRequestId();

			try {
				DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

				String sql = "update consultationRequests set  serviceId = '" + str.q(serviceId) + "',  specId = '" + str.q(specId) + "',  appointmentDate = '" + str.q(appointmentDate) + "',  appointmentTime = '" + str.q(appointmentTime)
				        + "',  reason = '" + str.q(reason) + "',  clinicalInfo = '" + str.q(clinicalInfo) + "',  currentMeds = '" + str.q(currentMeds) + "',  allergies = '" + str.q(allergies) + "',  status = '" + str.q(status) + "',  statusText = '"
				        + str.q(statusText) + "',  sendTo = '" + str.q(sendTo) + "',  urgency = '" + urg + "',  concurrentProblems = '" + str.q(concurrentProblems) + "',  patientWillBook = " + str.q(pwb) + "  where requestId = '" + str.q(requestId) + "'";
				db.RunSQL(sql);

			}

			catch (SQLException e) {
				e.printStackTrace();
			}

			request.setAttribute("transType", "1");

		}

		frm.setRequestId("");

		request.setAttribute("teamVar", sendTo);

		if (submission.endsWith("And Print Preview")) {

			request.setAttribute("reqId", requestId);
			if (IsPropertiesOn.propertiesOn("CONSULT_PRINT_PDF")) {
				return mapping.findForward("printpdf");
			} else if (IsPropertiesOn.propertiesOn("CONSULT_PRINT_ALT")) {
				return mapping.findForward("printalt");
			} else {
				return mapping.findForward("print");
			}

		} else if (submission.endsWith("And Fax")) {

			request.setAttribute("reqId", requestId);

			return mapping.findForward("fax");

		} 
		else if (submission.endsWith("esend"))
		{
			// upon success continue as normal with success message
			// upon failure, go to consultation update page with message
			try {
	            doHl7Send(Integer.parseInt(requestId));
	            WebUtils.addResourceBundleInfoMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESent");
            } catch (Exception e) {
            	logger.error("Error contacting remote server.", e);
            	
	            WebUtils.addResourceBundleErrorMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESendError");
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

	private void doHl7Send(Integer consultationRequestId) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception {
		
	    ConsultationRequestDao consultationRequestDao=(ConsultationRequestDao)SpringUtils.getBean("consultationRequestDao");
	    ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");

	    ConsultationRequest consultationRequest=consultationRequestDao.find(consultationRequestId);
	    ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
	    
        LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	    
	    // set status now so the remote version shows this status
	    consultationRequest.setStatus("2");

	    REF_I12 refI12=RefI12.makeRefI12(loggedInInfo.currentFacility.getName(), consultationRequest, new StreetAddressDataHolder());
	    SendingUtils.send(refI12, professionalSpecialist);
	    
	    // save after the sending just in case the sending fails.
	    consultationRequestDao.merge(consultationRequest);
	    
	    //--- send attachments ---
    	Provider sendingProvider=loggedInInfo.loggedInProvider;
    	DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
    	Demographic demographic=demographicDao.getDemographicById(consultationRequest.getDemographicId());
    	Provider receivingProvider=DataTypeUtils.getReceivingProvider(professionalSpecialist);
    	
	    ArrayList<EDoc> attachments=EDocUtil.listDocs(demographic.getDemographicNo().toString(), consultationRequest.getId().toString(), true);
	    for (EDoc attachment : attachments)
	    {
	        ObservationData observationData=new ObservationData();
	        observationData.dataName=attachment.getDescription();
	        observationData.textData="Attachment for consultation : "+consultationRequestId;
	        observationData.binaryDataFileName=attachment.getFileName();
	        observationData.binaryData=attachment.getFileBytes();

	        
	        ORU_R01 hl7Message=OruR01.makeOruR01(loggedInInfo.currentFacility.getName(), demographic, observationData, sendingProvider, receivingProvider);        
	        SendingUtils.send(hl7Message, professionalSpecialist);	    	
	    }
    }

}
