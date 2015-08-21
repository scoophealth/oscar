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
package org.oscarehr.e2e.populator.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.oscarehr.e2e.constant.BodyConstants.FamilyHistory;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.PatientExport.FamilyHistoryEntry;
import org.oscarehr.e2e.model.export.body.FamilyHistoryModel;

public class FamilyHistoryPopulator extends AbstractBodyPopulator<FamilyHistoryEntry> {
	private List<FamilyHistoryEntry> familyHistory = null;

	FamilyHistoryPopulator(PatientExport patientExport) {
		bodyConstants = FamilyHistory.getConstants();
		if(patientExport.isLoaded()) {
			familyHistory = patientExport.getFamilyHistory();
		}
	}

	@Override
	public void populate() {
		if(familyHistory != null) {
			for(FamilyHistoryEntry familyEntry : familyHistory) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true));
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entry.setClinicalStatement(populateClinicalStatement(Arrays.asList(familyEntry)));
				entries.add(entry);
			}
		}

		super.populate();
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<FamilyHistoryEntry> list) {
		FamilyHistoryModel familyHistoryModel = new FamilyHistoryModel(list.get(0));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		observation.setId(familyHistoryModel.getIds());
		observation.setCode(familyHistoryModel.getCode());
		observation.setText(familyHistoryModel.getText());
		observation.setEffectiveTime(familyHistoryModel.getEffectiveTime());
		observation.setValue(familyHistoryModel.getValue());
		observation.setSubject(familyHistoryModel.getSubject());

		entryRelationships.add(familyHistoryModel.getTreatmentComment());
		entryRelationships.add(familyHistoryModel.getBillingCode());
		entryRelationships.add(familyHistoryModel.getLifestageOnset());
		entryRelationships.add(familyHistoryModel.getAgeAtOnset());

		observation.setEntryRelationship(entryRelationships);
		return observation;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		FamilyHistoryModel familyHistoryModel = new FamilyHistoryModel(null);
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		observation.setId(familyHistoryModel.getIds());
		observation.setCode(familyHistoryModel.getCode());
		observation.setText(familyHistoryModel.getText());
		observation.setEffectiveTime(familyHistoryModel.getEffectiveTime());
		observation.setValue(familyHistoryModel.getValue());
		observation.setSubject(familyHistoryModel.getSubject());

		entryRelationships.add(familyHistoryModel.getTreatmentComment());
		entryRelationships.add(familyHistoryModel.getBillingCode());
		entryRelationships.add(familyHistoryModel.getLifestageOnset());
		entryRelationships.add(familyHistoryModel.getAgeAtOnset());

		observation.setEntryRelationship(entryRelationships);
		return observation;
	}

	@Override
	public List<String> populateText() {
		List<String> list = new ArrayList<String>();
		if(familyHistory != null) {
			for(FamilyHistoryEntry familyEntry : familyHistory) {
				FamilyHistoryModel familyHistoryModel = new FamilyHistoryModel(familyEntry);
				list.add(familyHistoryModel.getTextSummary());
			}
		}

		return list;
	}
}
