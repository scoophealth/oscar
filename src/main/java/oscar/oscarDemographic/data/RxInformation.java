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

import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarRx.data.RxPatientData;

public class RxInformation {
	private String currentMedication;
	private String allergies;

	public String getCurrentMedication(String demographic_no) {
		oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
		oscar.oscarRx.data.RxPrescriptionData.Prescription[] arr = {};
		arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demographic_no));
		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].isCurrent()) {

				stringBuffer.append(arr[i].getFullOutLine().replaceAll(";", " ") + "\n");
				// stringBuffer.append(arr[i].getRxDisplay()+"\n");
			}
		}
		this.currentMedication = stringBuffer.toString();
		return this.currentMedication;
	}

	public String getAllergies(LoggedInInfo loggedInInfo, String demographic_no) {
		oscar.oscarRx.data.RxPatientData.Patient patient = RxPatientData.getPatient(loggedInInfo, Integer.parseInt(demographic_no));
		Allergy[] allergies = {};
		allergies = patient.getActiveAllergies();
		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < allergies.length; i++) {
			Allergy allerg = allergies[i];
			stringBuffer.append(allerg.getDescription() + "  " + Allergy.getTypeDesc(allerg.getTypeCode()) + " \n");
		}
		this.allergies = stringBuffer.toString();

		return this.allergies;
	}
}
