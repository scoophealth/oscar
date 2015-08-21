/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.populator.header;

import java.util.ArrayList;
import java.util.Arrays;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Patient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PatientRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.export.header.RecordTargetModel;
import org.oscarehr.e2e.populator.AbstractPopulator;

class RecordTargetPopulator extends AbstractPopulator {
	private final RecordTargetModel recordTargetModel;

	RecordTargetPopulator(PatientExport patientExport) {
		recordTargetModel = new RecordTargetModel(patientExport.getDemographic());
	}

	@Override
	public void populate() {
		RecordTarget recordTarget = new RecordTarget();
		PatientRole patientRole = new PatientRole();
		Patient patient = new Patient();

		recordTarget.setContextControlCode(ContextControl.OverridingPropagating);
		recordTarget.setPatientRole(patientRole);

		patientRole.setId(recordTargetModel.getIds());
		patientRole.setAddr(recordTargetModel.getAddresses());
		patientRole.setTelecom(recordTargetModel.getTelecoms());
		patientRole.setPatient(patient);

		patient.setName(recordTargetModel.getNames());
		patient.setAdministrativeGenderCode(recordTargetModel.getGender());
		patient.setBirthTime(recordTargetModel.getBirthDate());
		patient.setLanguageCommunication(recordTargetModel.getLanguages());

		clinicalDocument.setRecordTarget(new ArrayList<RecordTarget>(Arrays.asList(recordTarget)));
	}
}
