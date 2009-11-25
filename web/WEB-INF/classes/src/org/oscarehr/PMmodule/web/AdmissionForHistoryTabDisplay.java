package org.oscarehr.PMmodule.web;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.util.MiscUtils;

/**
 * This is a display object for the history tab of a clients admissions.
 */
public class AdmissionForHistoryTabDisplay {
	
	public static final Comparator<AdmissionForHistoryTabDisplay> ADMISSION_DATE_COMPARATOR=new Comparator<AdmissionForHistoryTabDisplay>() {
		public int compare(AdmissionForHistoryTabDisplay arg0, AdmissionForHistoryTabDisplay arg1) {
			return(arg1.admissionDate.compareTo(arg0.admissionDate));
		}
	};
	
	private static final Logger logger = MiscUtils.getLogger();
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

	private Integer admissionId;
	private boolean isFromIntegrator;
	private String programName;
	private String programType;
	private String facilityName;
	private String admissionDate;
	private String facilityAdmission;
	private String dischargeDate;
	private String facilityDischarge;
	private int daysInProgram;
	private String temporaryAdmission;

	public AdmissionForHistoryTabDisplay(Admission admission) {
		admissionId = admission.getId().intValue();
		isFromIntegrator = false;
		programName = admission.getProgramName();
		programType = admission.getProgramType();
		facilityName = "local facility";

		admissionDate = dateFormatter.format(admission.getAdmissionDate());
		facilityAdmission = String.valueOf(!admission.isAdmissionFromTransfer());

		if (admission.getDischargeDate() != null) {
			dischargeDate = dateFormatter.format(admission.getDischargeDate());
			daysInProgram = MiscUtils.calculateDayDifference(admission.getAdmissionDate(), admission.getDischargeDate());
		} else {
			daysInProgram = MiscUtils.calculateDayDifference(admission.getAdmissionDate(), new Date());
		}

		facilityDischarge = String.valueOf(!admission.isDischargeFromTransfer());
		temporaryAdmission = String.valueOf(admission.isTemporaryAdmission());
	}

	public AdmissionForHistoryTabDisplay(CachedAdmission cachedAdmission) {
		isFromIntegrator = true;

		FacilityIdIntegerCompositePk remoteProgramPk = new FacilityIdIntegerCompositePk();
		int remoteFacilityId = cachedAdmission.getFacilityIdIntegerCompositePk().getIntegratorFacilityId();
		remoteProgramPk.setIntegratorFacilityId(remoteFacilityId);
		remoteProgramPk.setCaisiItemId(cachedAdmission.getCaisiProgramId());
		try {
			CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(remoteProgramPk);
			programName = cachedProgram.getName();
			programType = cachedProgram.getType();
		} catch (Exception e) {
			logger.error("Error retriving integrator program.", e);
		}

		try {
			CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(remoteFacilityId);
			facilityName = cachedFacility.getName();
		} catch (Exception e) {
			logger.error("Error retrieving integrator Facility.", e);
		}

		admissionDate = dateFormatter.format(cachedAdmission.getAdmissionDate());
		facilityAdmission = "n/a";

		if (cachedAdmission.getDischargeDate() != null) {
			dischargeDate = dateFormatter.format(cachedAdmission.getDischargeDate());
			daysInProgram = MiscUtils.calculateDayDifference(cachedAdmission.getAdmissionDate(), cachedAdmission.getDischargeDate());
		} else {
			daysInProgram = MiscUtils.calculateDayDifference(cachedAdmission.getAdmissionDate(), new Date());

		}

		facilityDischarge = "n/a";
		temporaryAdmission = "n/a";
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

	public boolean isFromIntegrator() {
		return isFromIntegrator;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public String getFacilityAdmission() {
		return facilityAdmission;
	}

	public String getDischargeDate() {
		return dischargeDate;
	}

	public String getFacilityDischarge() {
		return facilityDischarge;
	}

	public int getDaysInProgram() {
		return daysInProgram;
	}

	public String getTemporaryAdmission() {
		return temporaryAdmission;
	}

}
