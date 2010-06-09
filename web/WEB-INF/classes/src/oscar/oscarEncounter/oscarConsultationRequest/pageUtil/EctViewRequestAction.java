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

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.RefI12;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.util.DateUtils;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.REF_I12;
import ca.uhn.hl7v2.model.v25.segment.PID;

public class EctViewRequestAction extends Action {
	
	private static final Logger logger=MiscUtils.getLogger();
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse  response)	throws ServletException, IOException {

		EctViewRequestForm frm = (EctViewRequestForm) form;

		request.setAttribute("id", frm.getRequestId());

		logger.debug("Id:"+frm.getRequestId());
		logger.debug("SegmentId:"+request.getParameter("segmentId"));
		
		return mapping.findForward("success");
	}

	public static void fillFormValues(EctConsultationFormRequestForm thisForm, EctConsultationFormRequestUtil consultUtil)
	{
        thisForm.setAllergies(consultUtil.allergies);
        thisForm.setReasonForConsultation(consultUtil.reasonForConsultation);
        thisForm.setClinicalInformation(consultUtil.clinicalInformation);
        thisForm.setCurrentMedications(consultUtil.currentMedications);
        thisForm.setReferalDate(consultUtil.referalDate);
        thisForm.setSendTo(consultUtil.sendTo);
        thisForm.setService(consultUtil.service);
        thisForm.setStatus(consultUtil.status);
        thisForm.setAppointmentDay(consultUtil.appointmentDay);
        thisForm.setAppointmentMonth(consultUtil.appointmentMonth);
        thisForm.setAppointmentYear(consultUtil.appointmentYear);
        thisForm.setAppointmentHour(consultUtil.appointmentHour);
        thisForm.setAppointmentMinute(consultUtil.appointmentMinute);
        thisForm.setAppointmentPm(consultUtil.appointmentPm);
        thisForm.setConcurrentProblems(consultUtil.concurrentProblems);
        thisForm.setAppointmentNotes(consultUtil.appointmentNotes);
        thisForm.setUrgency(consultUtil.urgency);
        thisForm.setPatientWillBook(consultUtil.pwb);

        if( !consultUtil.teamVec.contains(consultUtil.sendTo) ) {
            consultUtil.teamVec.add(consultUtil.sendTo);
        }
        
        //---
        
        thisForm.setPatientAddress(consultUtil.patientAddress);
        thisForm.setPatientDOB(consultUtil.patientDOB);
        thisForm.setPatientHealthNum(consultUtil.patientHealthNum);
        thisForm.setPatientHealthCardVersionCode(consultUtil.patientHealthCardVersionCode);
        thisForm.setPatientHealthCardType(consultUtil.patientHealthCardType);
        thisForm.setPatientName(consultUtil.patientName);
        thisForm.setPatientPhone(consultUtil.patientPhone);
        thisForm.setPatientSex(consultUtil.patientSex);
        thisForm.setPatientWPhone(consultUtil.patientWPhone);
	}
	
	public static void fillFormValues(EctConsultationFormRequestForm thisForm, String segmentId) throws HL7Exception
	{
		String hl7Message=Factory.getHL7Body(segmentId);
		REF_I12 refI12=(REF_I12) OscarToOscarUtils.pipeParserParse(hl7Message);
		
        thisForm.setAllergies(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
        thisForm.setReasonForConsultation(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.REASON_FOR_CONSULTATION));
        thisForm.setClinicalInformation(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.CLINICAL_INFORMATION));
        thisForm.setCurrentMedications(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.CURRENT_MEDICATIONS));
        
        GregorianCalendar referralDate=DataTypeUtils.getCalendarFromDTM(refI12.getRF1().getEffectiveDate().getTime());
        thisForm.setReferalDate(DateUtils.getISODateTimeFormatNoT(referralDate));

//        thisForm.setSendTo();
//        thisForm.setService(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setStatus(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentDay(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentMonth(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentYear(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentHour(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentMinute(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setAppointmentPm(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));

        thisForm.setConcurrentProblems(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.CONCURRENT_PROBLEMS));
        thisForm.setAppointmentNotes(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.APPOINTMENT_NOTES));

//        thisForm.setUrgency(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        thisForm.setPatientWillBook(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//
//        if( !consultUtil.teamVec.contains(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES)) ) {
//            consultUtil.teamVec.add(RefI12.getNteValue(refI12, RefI12.REF_NTE_TYPE.ALLERGIES));
//        }
        
        //---
        
        
        PID pid=refI12.getPID();
        Demographic demographic=DataTypeUtils.parsePid(pid);
        
        StringBuilder address=new StringBuilder();
        if (demographic.getAddress()!=null) address.append(demographic.getAddress()).append(", ");
        if (demographic.getCity()!=null) address.append(demographic.getCity()).append(", ");
        if (demographic.getProvince()!=null) address.append(demographic.getProvince());
        thisForm.setPatientAddress(address.toString());
        
        thisForm.setPatientDOB(DateFormatUtils.ISO_DATE_FORMAT.format(demographic.getBirthDay()));
        
        thisForm.setPatientHealthNum(demographic.getHin());
        thisForm.setPatientHealthCardType(demographic.getHcType());
        thisForm.setPatientHealthCardVersionCode(demographic.getVer());
        
        thisForm.setPatientName(demographic.getFirstName()+' '+demographic.getLastName());
        thisForm.setPatientPhone(demographic.getPhone());
        thisForm.setPatientSex(demographic.getSex());
//        thisForm.setPatientWPhone(patientAddress);
	}
	
}
