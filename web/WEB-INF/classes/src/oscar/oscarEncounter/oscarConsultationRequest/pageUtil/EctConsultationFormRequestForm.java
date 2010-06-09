// -----------------------------------------------------------------------------------------------------------------------

// *

// *

// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *

// * This software is published under the GPL GNU General Public License.

// * This program is free software; you can redistribute it and/or

// * modify it under the terms of the GNU General Public License

// * as published by the Free Software Foundation; either version 2

// * of the License, or (at your option) any later version. *

// * This program is distributed in the hope that it will be useful,

// * but WITHOUT ANY WARRANTY; without even the implied warranty of

// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License

// * along with this program; if not, write to the Free Software

// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *

// *

// * <OSCAR TEAM>

// * This software was written for the

// * Department of Family Medicine

// * McMaster University

// * Hamilton

// * Ontario, Canada

// *

// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class EctConsultationFormRequestForm extends ActionForm {

	String allergies;

	String appointmentDay;

	String appointmentHour;

	String appointmentMinute;

	String appointmentMonth;

	String appointmentNotes;

	String appointmentPm;

	String appointmentTime;

	String appointmentYear;

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

	public String getAllergies() {

		if (allergies == null) {

			allergies = new String();

		}
		return allergies;

	}

	public String getAppointmentDay() {

		if (appointmentDay == null) {

			appointmentDay = new String();

		}
		return appointmentDay;

	}

	public String getAppointmentHour() {

		if (appointmentHour == null) {

			appointmentHour = new String();

		}
		return appointmentHour;

	}

	public String getAppointmentMinute() {

		if (appointmentMinute == null) {

			appointmentMinute = new String();

		}
		return appointmentMinute;

	}

	public String getAppointmentMonth() {

		if (appointmentMonth == null) {

			appointmentMonth = new String();

		}
		return appointmentMonth;

	}

	public String getAppointmentNotes() {

		if (appointmentNotes == null) {

			appointmentNotes = new String();

		}
		return appointmentNotes;

	}

	public String getAppointmentPm() {

		if (appointmentPm == null) {

			appointmentPm = new String();

		}
		return appointmentPm;

	}

	public String getAppointmentTime() {

		if (appointmentTime == null) {

			appointmentTime = new String();

		}
		return appointmentTime;

	}

	public String getAppointmentYear() {

		if (appointmentYear == null) {

			appointmentYear = new String();

		}
		return appointmentYear;

	}

	public String getClinicalInformation() {

		if (clinicalInformation == null) {

			clinicalInformation = new String();

		}
		return clinicalInformation;

	}

	public String getConcurrentProblems() {

		if (concurrentProblems == null) {

			concurrentProblems = new String();

		}
		return concurrentProblems;

	}

	public String getCurrentMedications() {

		if (currentMedications == null) {

			currentMedications = new String();

		}
		return currentMedications;

	}

	public String getDemographicNo() {

		if (demographicNo == null) {

			demographicNo = new String();

		}
		return demographicNo;

	}

	public String getDocuments() {
		return documents;
	}

	public String getPatientWillBook() {
		return patientWillBook;
	}

	public String getProviderNo() {

		if (providerNo == null) {

			providerNo = new String();

		}
		return providerNo;

	}

	public String getReasonForConsultation() {

		if (reasonForConsultation == null) {

			reasonForConsultation = new String();

		}
		return reasonForConsultation;

	}

	public String getReferalDate() {

		if (referalDate == null) {

			referalDate = new String();

		}
		return referalDate;

	}

	public String getRequestId() {

		if (requestId == null) {

			requestId = new String();

		}
		return requestId;

	}

	public String getSendTo() {

		if (sendTo == null) {

			sendTo = new String();

		}
		return sendTo;

	}

	public String getService() {

		if (service == null) {

			service = new String();

		}
		return service;

	}

	public String getSpecialist() {

		if (specialist == null) {

			specialist = new String();

		}
		return specialist;

	}

	public String getStatus() {

		if (status == null) {

			status = new String();

		}
		return status;

	}

	public String getSubmission() {

		if (submission == null) {

			submission = new String();

		}
		return submission;

	}

	public String getUrgency() {

		if (urgency == null) {

			urgency = new String();

		}
		return urgency;

	}

	@Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {

		System.out.println("\nReseting ConsultationFormRequestForm\n");

	}

	public void setAllergies(String str) {

		System.out.println("allergies setter = ".concat(String.valueOf(String.valueOf(str))));

		allergies = str;

	}

	public void setAppointmentDay(String str) {

		System.out.println("appointmentDay setter");

		appointmentDay = str;

	}

	public void setAppointmentHour(String str) {

		appointmentHour = str;

	}

	public void setAppointmentMinute(String str) {

		appointmentMinute = str;

	}

	public void setAppointmentMonth(String str) {

		System.out.println("appointmentMonth setter");

		appointmentMonth = str;

	}

	public void setAppointmentNotes(String str) {

		appointmentNotes = str;

	}

	public void setAppointmentPm(String str) {

		appointmentPm = str;

	}

	public void setAppointmentTime(String str) {

		System.out.println("appointmentTime setter");

		appointmentTime = str;

	}

	public void setAppointmentYear(String str) {

		System.out.println("appointmentYear setter");

		appointmentYear = str;

	}

	public void setClinicalInformation(String str) {

		System.out.println("clinicalInformation setter");

		clinicalInformation = str;

	}

	public void setConcurrentProblems(String str) {

		System.out.println("concurrentProblems setter");

		concurrentProblems = str;

	}

	public void setCurrentMedications(String str) {

		System.out.println("currentMedications setter");

		currentMedications = str;

	}

	public void setDemographicNo(String str) {

		System.out.println("demographicNo date has been set");

		demographicNo = str;

	}

	public void setDocuments(String doc) {
		documents = doc;
	}

	public void setPatientWillBook(String str) {
		this.patientWillBook = str;

	}

	public void setProviderNo(String str) {

		System.out.println("providerNo date has been set");

		providerNo = str;

	}

	public void setReasonForConsultation(String str) {

		System.out.println("reasonForConsultation setter");

		reasonForConsultation = str;

	}

	public void setReferalDate(String str) {

		System.out.println("referal date has been set");

		referalDate = str;

	}

	public void setRequestId(String str) {

		System.out.println("requestId date has been set");

		requestId = str;

	}

	public void setSendTo(String str) {

		System.out.println("sendTo setter");

		sendTo = str;

	}

	public void setService(String str) {

		System.out.println("service has been set");

		service = str;

	}

	public void setSpecialist(String str) {

		System.out.println("specialist setter");

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

}
