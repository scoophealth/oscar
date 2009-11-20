package org.oscarehr.PMmodule.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.util.MiscUtils;

/**
 * This is a display object for the history tab of a clients admissions.
 */
public class AdmissionForHistoryTabDisplay {

	private static final String DATE_FORMAT="yyyy-MM-dd HH:mm";
	
	private SimpleDateFormat dateFormatter=new SimpleDateFormat(DATE_FORMAT);
	
	private Integer admissionId;

	private String programName;
	private String programType;

	/** if it's null it means it's a local admission, i.e. local facility */
	private String remoteFacilityName;

	private String admissionDate;
	private boolean facilityAdmission;
	private String dischargeDate;
	private boolean facilityDischarge;
	private int daysInProgram;
	private boolean tempraryAdmission;

	public AdmissionForHistoryTabDisplay(Admission admission)
	{
		admissionId=admission.getId().intValue();
		programName=admission.getProgramName();
		programType=admission.getProgramType();
		
		admissionDate=dateFormatter.format(admission.getAdmissionDate());
		facilityAdmission=!admission.isAdmissionFromTransfer();
		
		if (admission.getDischargeDate()!=null) dischargeDate=dateFormatter.format(admission.getDischargeDate());
		facilityDischarge=!admission.isDischargeFromTransfer();

		Date endDate=null;
		if (admission.getDischargeDate()!=null) endDate=admission.getDischargeDate();
		else endDate=new Date();
		daysInProgram=MiscUtils.calculateDayDifference(admission.getAdmissionDate(), endDate);
		
		tempraryAdmission=admission.isTemporaryAdmission();
	}

	public Integer getAdmissionId() {
    	return admissionId;
    }

	public String getProgramName() {
    	return programName;
    }

	public String getProgramType() {
    	return programType;
    }

	public String getRemoteFacilityName() {
    	return remoteFacilityName;
    }

	public String getAdmissionDate() {
    	return admissionDate;
    }

	public boolean isFacilityAdmission() {
    	return facilityAdmission;
    }

	public String getDischargeDate() {
    	return dischargeDate;
    }

	public boolean isFacilityDischarge() {
    	return facilityDischarge;
    }

	public int getDaysInProgram() {
    	return daysInProgram;
    }

	public boolean isTempraryAdmission() {
    	return tempraryAdmission;
    }

	
}
