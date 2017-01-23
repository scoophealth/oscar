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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.oscarehr.util.WebUtils;

public final class EctConsultationFormRequestForm extends ActionForm {

	String allergies;

	String appointmentDate;

	String appointmentHour;

	String appointmentMinute;

	String appointmentNotes;

	String appointmentPm;

	String appointmentTime;

	String clinicalInformation;

	String concurrentProblems;

	String currentMedications;

	String demographicNo;

	// Documents attached to this consultation
	String documents;

	// Patient Will Book Field, can be either "1" or "0"
	String patientWillBook;

	String providerNo;

	String reasonForConsultation;

	String referalDate;

	String requestId;

	String sendTo;

	String service;

	String specialist;

	String status;

	String submission;

	String urgency;
	
	//multi-site
	String siteName;

	private String signatureImg;
	private String patientFirstName;
	private String patientLastName;
	private String patientAddress;
	private String patientPhone;
	private String patientWPhone;
        private String patientEmail;
	private String patientDOB;
	private String patientSex;
	private String patientHealthNum;
	private String patientHealthCardVersionCode;
	private String patientHealthCardType;
	private String patientAge;
	private String providerName;
	private String professionalSpecialistName;
	private String professionalSpecialistPhone;
	private String professionalSpecialistAddress;
        private String followUpDate;
	private boolean eReferral = false;
	private Integer hl7TextMessageId;

	private String letterheadName, letterheadAddress, letterheadPhone, letterheadFax;
	
	private Integer fdid;
	private String source;
	
	private String appointmentInstructions;
	private String appointmentInstructionsLabel;
	
	public String getProfessionalSpecialistName() {
		return (StringUtils.trimToEmpty(professionalSpecialistName));
	}

	public void setProfessionalSpecialistName(String professionalSpecialistName) {
		this.professionalSpecialistName = professionalSpecialistName;
	}

	public String getProfessionalSpecialistPhone() {
		return (StringUtils.trimToEmpty(professionalSpecialistPhone));
	}

	public void setProfessionalSpecialistPhone(String professionalSpecialistPhone) {
		this.professionalSpecialistPhone = professionalSpecialistPhone;
	}

	public String getProfessionalSpecialistAddress() {
		return (StringUtils.trimToEmpty(professionalSpecialistAddress));
	}

	public void setProfessionalSpecialistAddress(String professionalSpecialistAddress) {
		this.professionalSpecialistAddress = professionalSpecialistAddress;
	}

	public boolean iseReferral() {
		return eReferral;
	}

	public void seteReferral(boolean eReferral) {
		this.eReferral = eReferral;
	}

	public String getProviderName() {
		return (StringUtils.trimToEmpty(providerName));
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getPatientAge() {
		return (StringUtils.trimToEmpty(patientAge));
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public String getAllergies() {
		return (StringUtils.trimToEmpty(allergies));
	}

	public String getAppointmentDate() {
		return (StringUtils.trimToEmpty(appointmentDate));
	}

	public String getAppointmentHour() {
		return (StringUtils.trimToEmpty(appointmentHour));
	}

	public String getAppointmentMinute() {
		return (StringUtils.trimToEmpty(appointmentMinute));
	}

	public String getAppointmentNotes() {
		return (StringUtils.trimToEmpty(appointmentNotes));
	}

	public String getAppointmentPm() {
		return (StringUtils.trimToEmpty(appointmentPm));
	}

	public String getAppointmentTime() {
		return (StringUtils.trimToEmpty(appointmentTime));
	}

	public String getClinicalInformation() {
		return (StringUtils.trimToEmpty(clinicalInformation));
	}

	public String getConcurrentProblems() {
		return (StringUtils.trimToEmpty(concurrentProblems));
	}

	public String getCurrentMedications() {
		return (StringUtils.trimToEmpty(currentMedications));
	}

	public String getDemographicNo() {
		return (StringUtils.trimToEmpty(demographicNo));
	}

	public String getDocuments() {
		return documents;
	}

	public String getPatientWillBook() {
		return patientWillBook;
	}

	public String getProviderNo() {
		return (StringUtils.trimToEmpty(providerNo));
	}

	public String getReasonForConsultation() {
		return (StringUtils.trimToEmpty(reasonForConsultation));
	}

	public String getReferalDate() {
		return (StringUtils.trimToEmpty(referalDate));
	}

	public String getRequestId() {
		return (StringUtils.trimToEmpty(requestId));
	}

	public String getSendTo() {
		return (StringUtils.trimToEmpty(sendTo));
	}

	public String getService() {
		return (StringUtils.trimToEmpty(service));
	}

	public String getSpecialist() {
		return (StringUtils.trimToEmpty(specialist));
	}

	public String getStatus() {
		return (StringUtils.trimToEmpty(status));
	}

	public String getSubmission() {
		return (StringUtils.trimToEmpty(submission));
	}

	public String getUrgency() {
		return (StringUtils.trimToEmpty(urgency));
	}


	public void setAllergies(String str) {
		allergies = str;
	}

	public void setAppointmentDate(String str) {
		appointmentDate = str;
	}

	public void setAppointmentHour(String str) {
		appointmentHour = str;
	}

	public void setAppointmentMinute(String str) {
		appointmentMinute = str;
	}

	public void setAppointmentNotes(String str) {
		appointmentNotes = str;
	}

	public void setAppointmentPm(String str) {
		appointmentPm = str;
	}

	public void setAppointmentTime(String str) {
		appointmentTime = str;
	}

	public void setClinicalInformation(String str) {
		clinicalInformation = str;
	}

	public void setConcurrentProblems(String str) {
		concurrentProblems = str;
	}

	public void setCurrentMedications(String str) {
		currentMedications = str;
	}

	public void setDemographicNo(String str) {
		demographicNo = str;
	}

	public void setDocuments(String doc) {
		documents = doc;
	}

	public void setPatientWillBook(String str) {
		this.patientWillBook = str;
	}

	public void setProviderNo(String str) {
		providerNo = str;
	}

	public void setReasonForConsultation(String str) {
		reasonForConsultation = str;
	}

	public void setReferalDate(String str) {
		referalDate = str;
	}

	public void setRequestId(String str) {
		requestId = str;
	}

	public void setSendTo(String str) {
		sendTo = str;
	}

	public void setService(String str) {
		service = str;
	}

	public void setSpecialist(String str) {
		specialist = str;
	}

	public void setStatus(String str) {
		status = str;
	}

	public void setSubmission(String str) {
		submission = str;
	}

	public void setUrgency(String str) {
		urgency = str;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (this.patientWillBook == null || !this.patientWillBook.equals("1")) {
			this.patientWillBook = "0";
		}

		if (service == null || service.length() == 0) {

			errors.add("service", new ActionMessage("Errors.service.null"));

		}
		try {

			int temp = Integer.parseInt(service);

			if (temp < 0) {

				errors.add("service", new ActionMessage("Errors.service.noServiceSelected"));

			}
		}

		catch (Exception e) {

			errors.add("fName", new ActionMessage("Errors.service.notNum"));

		}

		if (!errors.isEmpty()) {

			request.setAttribute("validateError", "blah");

		}
		return errors;

	}

	public String getPatientName() {
		return (StringUtils.trimToEmpty(patientLastName + ", " + patientFirstName));
	}

	public String getPatientAddress() {
		return (StringUtils.trimToEmpty(patientAddress));
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getPatientPhone() {
		return (StringUtils.trimToEmpty(patientPhone));
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public String getPatientWPhone() {
		return (StringUtils.trimToEmpty(patientWPhone));
	}

	public void setPatientWPhone(String patientWPhone) {
		this.patientWPhone = patientWPhone;
	}

        public void setPatientEmail(String patientEmail) {
            this.patientEmail = patientEmail;
        }
        
        public String getPatientEmail() {
            return (StringUtils.trimToEmpty(patientEmail));
        }
        
	public String getPatientDOB() {
		return (StringUtils.trimToEmpty(patientDOB));
	}

	public void setPatientDOB(String patientDOB) {
		this.patientDOB = patientDOB;
	}

	public String getPatientSex() {
		return (StringUtils.trimToEmpty(patientSex));
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientHealthNum() {
		return (StringUtils.trimToEmpty(patientHealthNum));
	}

	public void setPatientHealthNum(String patientHealthNum) {
		this.patientHealthNum = patientHealthNum;
	}

	public String getPatientHealthCardVersionCode() {
		return (StringUtils.trimToEmpty(patientHealthCardVersionCode));
	}

	public void setPatientHealthCardVersionCode(String patientHealthCardVersionCode) {
		this.patientHealthCardVersionCode = patientHealthCardVersionCode;
	}

	public String getPatientHealthCardType() {
		return (StringUtils.trimToEmpty(patientHealthCardType));
	}

	public void setPatientHealthCardType(String patientHealthCardType) {
		this.patientHealthCardType = patientHealthCardType;
	}

	public Integer getHl7TextMessageId() {
		return hl7TextMessageId;
	}

	public void setHl7TextMessageId(Integer hl7TextMessageId) {
		this.hl7TextMessageId = hl7TextMessageId;
	}

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	/**
	 * This url will include the context path.
	 */
	public String getOruR01UrlString(HttpServletRequest request) {
		// /lab/CA/ALL/sendOruR01.jsp

		StringBuilder sb = new StringBuilder();

		sb.append(request.getContextPath());
		sb.append("/lab/CA/ALL/sendOruR01.jsp");

		HashMap<String, Object> queryParameters = new HashMap<String, Object>();

		// buildQueryString will take null into account
		queryParameters.put("hl7TextMessageId", hl7TextMessageId);
		queryParameters.put("clientFirstName", patientFirstName);
		queryParameters.put("clientLastName", patientLastName);
		queryParameters.put("clientHin", patientHealthNum);
		queryParameters.put("clientBirthDate", patientDOB);
		queryParameters.put("clientGender", patientSex);

		sb.append(WebUtils.buildQueryString(queryParameters));

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}

    /**
     * @return the followUpDate
     */
    public String getFollowUpDate() {
        return followUpDate;
    }

    /**
     * @param followUpDate the followUpDate to set
     */
    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }
	public String getSiteName() {
    	if (siteName == null) {
	    	siteName = new String();
		}
      	return siteName;
  	}
  
  	public void setSiteName(String str) {
	  	this.siteName = str;
  	}

	public String getSignatureImg() {
	    return signatureImg;
    }

	public void setSignatureImg(String signatureImg) {
	    this.signatureImg = signatureImg;
    }

	public String getLetterheadName() {
	    return letterheadName;
    }

	public void setLetterheadName(String letterheadName) {
	    this.letterheadName = letterheadName;
    }

	public String getLetterheadAddress() {
	    return letterheadAddress;
    }

	public void setLetterheadAddress(String letterheadAddress) {
	    this.letterheadAddress = letterheadAddress;
    }

	public String getLetterheadPhone() {
	    return letterheadPhone;
    }

	public void setLetterheadPhone(String letterheadPhone) {
	    this.letterheadPhone = letterheadPhone;
    }

	public String getLetterheadFax() {
	    return letterheadFax;
    }

	public void setLetterheadFax(String letterheadFax) {
	    this.letterheadFax = letterheadFax;
    }

	public Integer getFdid() {
		return fdid;
	}

	public void setFdid(Integer fdid) {
		this.fdid = fdid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getAppointmentInstructions() {
		return appointmentInstructions;
	}

	public void setAppointmentInstructions(String appointmentInstructions) {
		this.appointmentInstructions = appointmentInstructions;
	}

	public String getAppointmentInstructionsLabel() {
		return appointmentInstructionsLabel;
	}

	public void setAppointmentInstructionsLabel(String appointmentInstructionsLabel) {
		this.appointmentInstructionsLabel = appointmentInstructionsLabel;
	}


}
