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


package oscar.oscarDemographic.data;

import java.util.Date;

import org.oscarehr.util.LoggedInInfo;

public class EctInformation {

	private oscar.oscarEncounter.data.EctPatientData.Patient patient;
	private oscar.oscarEncounter.data.EctPatientData.Patient.eChart eChart;

	public EctInformation(LoggedInInfo loggedInInfo, String demographic_no) {
		init(loggedInInfo, demographic_no);
	}

	private void init(LoggedInInfo loggedInInfo, String demographic_no) {
		oscar.oscarEncounter.data.EctPatientData patientData = new oscar.oscarEncounter.data.EctPatientData();
		this.patient = patientData.getPatient(loggedInInfo, demographic_no);
		this.eChart = patient.getEChart();
	}

	public Date getEChartTimeStamp() {
		return eChart.getEChartTimeStamp();
	}

	public String getSocialHistory() {
		return eChart.getSocialHistory();
	}

	public String getFamilyHistory() {
		return eChart.getFamilyHistory();
	}

	public String getMedicalHistory() {
		return eChart.getMedicalHistory();
	}

	public String getOngoingConcerns() {
		return eChart.getOngoingConcerns();
	}

	public String getReminders() {
		return eChart.getReminders();
	}

	public String getEncounter() {
		return eChart.getEncounter();
	}

	public String getSubject() {
		return eChart.getSubject();
	}
}
