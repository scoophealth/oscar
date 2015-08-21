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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Encounter;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentEncounterMood;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.e2e.constant.BodyConstants.Encounters;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.export.body.EncountersModel;

public class EncountersPopulator extends AbstractBodyPopulator<CaseManagementNote> {
	private List<CaseManagementNote> encounters = null;

	EncountersPopulator(PatientExport patientExport) {
		bodyConstants = Encounters.getConstants();
		if(patientExport.isLoaded()) {
			encounters = patientExport.getEncounters();
		}
	}

	@Override
	public void populate() {
		if(encounters != null) {
			for(CaseManagementNote encounter : encounters) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true));
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entry.setClinicalStatement(populateClinicalStatement(Arrays.asList(encounter)));
				entries.add(entry);
			}
		}

		super.populate();
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<CaseManagementNote> list) {
		EncountersModel encountersModel = new EncountersModel(list.get(0));
		Encounter encounter = new Encounter(x_DocumentEncounterMood.Eventoccurrence);
		ArrayList<Participant2> participants = new ArrayList<Participant2>();
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		encounter.setId(encountersModel.getIds());
		encounter.setEffectiveTime(encountersModel.getEffectiveTime());
		participants.add(encountersModel.getEncounterLocation());
		participants.add(encountersModel.getEncounterProvider());
		entryRelationships.add(encountersModel.getEncounterNote());

		encounter.setParticipant(participants);
		encounter.setEntryRelationship(entryRelationships);
		return encounter;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		EncountersModel encountersModel = new EncountersModel(null);
		Encounter encounter = new Encounter(x_DocumentEncounterMood.Eventoccurrence);
		ArrayList<Participant2> participants = new ArrayList<Participant2>();
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		encounter.setId(encountersModel.getIds());
		encounter.setEffectiveTime(encountersModel.getEffectiveTime());
		participants.add(encountersModel.getEncounterLocation());
		participants.add(encountersModel.getEncounterProvider());
		entryRelationships.add(encountersModel.getEncounterNote());

		encounter.setParticipant(participants);
		encounter.setEntryRelationship(entryRelationships);
		return encounter;
	}

	@Override
	public List<String> populateText() {
		List<String> list = new ArrayList<String>();
		if(encounters != null) {
			for(CaseManagementNote encounter : encounters) {
				EncountersModel encountersModel = new EncountersModel(encounter);
				list.add(encountersModel.getTextSummary());
			}
		}

		return list;
	}
}
