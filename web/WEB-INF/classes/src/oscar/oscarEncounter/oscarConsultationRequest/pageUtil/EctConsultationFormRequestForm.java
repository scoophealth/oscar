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

public final class EctConsultationFormRequestForm
    extends ActionForm {

  public String getProviderNo() {

    if (providerNo == null) {

      providerNo = new String();

    }
    return providerNo;

  }

  public void setProviderNo(String str) {

    System.out.println("providerNo date has been set");

    providerNo = str;

  }

  public String getDemographicNo() {

    if (demographicNo == null) {

      demographicNo = new String();

    }
    return demographicNo;

  }

  public void setDemographicNo(String str) {

    System.out.println("demographicNo date has been set");

    demographicNo = str;

  }

  public String getRequestId() {

    if (requestId == null) {

      requestId = new String();

    }
    return requestId;

  }

  public void setRequestId(String str) {

    System.out.println("requestId date has been set");

    requestId = str;

  }

  public String getReferalDate() {

    if (referalDate == null) {

      referalDate = new String();

    }
    return referalDate;

  }

  public void setReferalDate(String str) {

    System.out.println("referal date has been set");

    referalDate = str;

  }

  public String getService() {

    if (service == null) {

      service = new String();

    }
    return service;

  }

  public void setService(String str) {

    System.out.println("service has been set");

    service = str;

  }

  public String getSpecialist() {

    if (specialist == null) {

      specialist = new String();

    }
    return specialist;

  }

  public void setSpecialist(String str) {

    System.out.println("specialist setter");

    specialist = str;

  }

  public String getAppointmentYear() {

    if (appointmentYear == null) {

      appointmentYear = new String();

    }
    return appointmentYear;

  }

  public void setAppointmentYear(String str) {

    System.out.println("appointmentYear setter");

    appointmentYear = str;

  }

  public String getAppointmentMonth() {

    if (appointmentMonth == null) {

      appointmentMonth = new String();

    }
    return appointmentMonth;

  }

  public void setAppointmentMonth(String str) {

    System.out.println("appointmentMonth setter");

    appointmentMonth = str;

  }

  public String getAppointmentDay() {

    if (appointmentDay == null) {

      appointmentDay = new String();

    }
    return appointmentDay;

  }

  public void setAppointmentDay(String str) {

    System.out.println("appointmentDay setter");

    appointmentDay = str;

  }

  public String getAppointmentTime() {

    if (appointmentTime == null) {

      appointmentTime = new String();

    }
    return appointmentTime;

  }

  public void setAppointmentTime(String str) {

    System.out.println("appointmentTime setter");

    appointmentTime = str;

  }

  public String getReasonForConsultation() {

    if (reasonForConsultation == null) {

      reasonForConsultation = new String();

    }
    return reasonForConsultation;

  }

  public void setReasonForConsultation(String str) {

    System.out.println("reasonForConsultation setter");

    reasonForConsultation = str;

  }

  public String getClinicalInformation() {

    if (clinicalInformation == null) {

      clinicalInformation = new String();

    }
    return clinicalInformation;

  }

  public void setClinicalInformation(String str) {

    System.out.println("clinicalInformation setter");

    clinicalInformation = str;

  }

  public String getConcurrentProblems() {

    if (concurrentProblems == null) {

      concurrentProblems = new String();

    }
    return concurrentProblems;

  }

  public void setConcurrentProblems(String str) {

    System.out.println("concurrentProblems setter");

    concurrentProblems = str;

  }

  public String getCurrentMedications() {

    if (currentMedications == null) {

      currentMedications = new String();

    }
    return currentMedications;

  }

  public void setCurrentMedications(String str) {

    System.out.println("currentMedications setter");

    currentMedications = str;

  }

  public String getAllergies() {

    if (allergies == null) {

      allergies = new String();

    }
    return allergies;

  }

  public void setAllergies(String str) {

    System.out.println("allergies setter = ".concat(String.valueOf(String.
        valueOf(str))));

    allergies = str;

  }

  public String getSendTo() {

    if (sendTo == null) {

      sendTo = new String();

    }
    return sendTo;

  }

  public void setSendTo(String str) {

    System.out.println("sendTo setter");

    sendTo = str;

  }

  public String getStatus() {

    if (status == null) {

      status = new String();

    }
    return status;

  }

  public void setStatus(String str) {

    status = str;

  }

  public String getSubmission() {

    if (submission == null) {

      submission = new String();

    }
    return submission;

  }

  public void setSubmission(String str) {

    submission = str;

  }

  public String getAppointmentHour() {

    if (appointmentHour == null) {

      appointmentHour = new String();

    }
    return appointmentHour;

  }

  public void setAppointmentHour(String str) {

    appointmentHour = str;

  }

  public String getAppointmentMinute() {

    if (appointmentMinute == null) {

      appointmentMinute = new String();

    }
    return appointmentMinute;

  }

  public void setAppointmentMinute(String str) {

    appointmentMinute = str;

  }

  public String getAppointmentPm() {

    if (appointmentPm == null) {

      appointmentPm = new String();

    }
    return appointmentPm;

  }

  public void setAppointmentPm(String str) {

    appointmentPm = str;

  }

  public String getAppointmentNotes() {

    if (appointmentNotes == null) {

      appointmentNotes = new String();

    }
    return appointmentNotes;

  }

  public void setAppointmentNotes(String str) {

    appointmentNotes = str;

  }

  public String getUrgency() {

    if (urgency == null) {

      urgency = new String();

    }
    return urgency;

  }

  public void setUrgency(String str) {

    urgency = str;

  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {

    System.out.println("\nReseting ConsultationFormRequestForm\n");

  }

  public ActionErrors validate(ActionMapping mapping,
                               HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    if (this.patientWillBook==null||!this.patientWillBook.equals("1")) {
    this.patientWillBook = "0";
   }


    if (service == null || service.length() == 0) {

      errors.add("service", new ActionMessage("Errors.service.null"));

    }
    try {

      int temp = Integer.parseInt(service);

      if (temp < 0) {

        errors.add("service",
                   new ActionMessage("Errors.service.noServiceSelected"));

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

  public String getPatientWillBook() {
    return patientWillBook;
  }

  public void setPatientWillBook(String str) {
    this.patientWillBook = str;

  }
  
  public String getDocuments() {
    return documents;
  }
  
  public void setDocuments(String doc) {
      documents = doc;
  }

  String providerNo;

  String demographicNo;

  String requestId;

  String referalDate;

  String service;

  String specialist;

  String appointmentYear;

  String appointmentMonth;

  String appointmentDay;

  String appointmentTime;

  String reasonForConsultation;

  String clinicalInformation;

  String concurrentProblems;

  String currentMedications;

  String allergies;

  String sendTo;

  String status;

  String submission;


  String appointmentHour;

  String appointmentMinute;

  String appointmentPm;

  String appointmentNotes;

  String urgency;
  //Patient Will Book Field, can be either "1" or "0"
  String patientWillBook;
  
  //Documents attached to this consultation
  String documents;

}
