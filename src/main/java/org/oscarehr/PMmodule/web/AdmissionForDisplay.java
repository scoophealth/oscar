/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.util.DateUtils;

/**
 * This is a display object for the history tab of a clients admissions.
 */
public class AdmissionForDisplay {
	
	public static final Comparator<AdmissionForDisplay> ADMISSION_DATE_COMPARATOR=new Comparator<AdmissionForDisplay>() {
		public int compare(AdmissionForDisplay arg0, AdmissionForDisplay arg1) {
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
	private Integer programId;
	private String dischargeNotes;
	
	
	public AdmissionForDisplay(Admission admission) {
		admissionId = admission.getId().intValue();
		isFromIntegrator = false;
		programName = admission.getProgramName();
		programType = admission.getProgramType();
		facilityName = "local";

		admissionDate = dateFormatter.format(admission.getAdmissionDate());
		facilityAdmission = String.valueOf(!admission.isAdmissionFromTransfer());
		programId = admission.getProgramId();
		
		if (admission.getDischargeDate() != null) {
			dischargeDate = dateFormatter.format(admission.getDischargeDate());
			daysInProgram = DateUtils.calculateDayDifference(admission.getAdmissionDate(), admission.getDischargeDate());
		} else {
			daysInProgram = DateUtils.calculateDayDifference(admission.getAdmissionDate(), new Date());
		}

		facilityDischarge = String.valueOf(!admission.isDischargeFromTransfer());
		temporaryAdmission = String.valueOf(admission.isTemporaryAdmission());
		this.dischargeNotes = admission.getDischargeNotes();
	}

	public AdmissionForDisplay(LoggedInInfo loggedInInfo, CachedAdmission cachedAdmission) {
		isFromIntegrator = true;

		FacilityIdIntegerCompositePk remoteProgramPk = new FacilityIdIntegerCompositePk();
		int remoteFacilityId = cachedAdmission.getFacilityIdIntegerCompositePk().getIntegratorFacilityId();
		remoteProgramPk.setIntegratorFacilityId(remoteFacilityId);
		remoteProgramPk.setCaisiItemId(cachedAdmission.getCaisiProgramId());
		try {
			CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteProgramPk);
			programName = cachedProgram.getName();
			programType = cachedProgram.getType();
		} catch (Exception e) {
			logger.error("Error retriving integrator program.", e);
		}

		try {
			CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteFacilityId);
			facilityName = cachedFacility.getName();
		} catch (Exception e) {
			logger.error("Error retrieving integrator Facility.", e);
		}

		admissionDate = dateFormatter.format(cachedAdmission.getAdmissionDate().getTime());
		facilityAdmission = "n/a";

		if (cachedAdmission.getDischargeDate() != null) {
			dischargeDate = dateFormatter.format(cachedAdmission.getDischargeDate());
			daysInProgram = DateUtils.calculateDayDifference(cachedAdmission.getAdmissionDate(), cachedAdmission.getDischargeDate());
		} else {
			daysInProgram = DateUtils.calculateDayDifference(cachedAdmission.getAdmissionDate(), new Date());

		}

		facilityDischarge = "n/a";
		temporaryAdmission = "n/a";
		this.dischargeNotes = cachedAdmission.getDischargeNotes();
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
	public Integer getProgramId() {
		return programId;
	}
	public Integer getClientId() {
		return admissionId;
	}

	public String getDischargeNotes() {
		return dischargeNotes;
	}

	public void setDischargeNotes(String dischargeNotes) {
		this.dischargeNotes = dischargeNotes;
	}
	
}
