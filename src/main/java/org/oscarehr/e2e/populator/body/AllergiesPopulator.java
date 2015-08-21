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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryAct;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentActMood;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.e2e.constant.BodyConstants.Allergies;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.export.body.AllergiesModel;

public class AllergiesPopulator extends AbstractBodyPopulator<Allergy> {
	private List<Allergy> allergies = null;

	AllergiesPopulator(PatientExport patientExport) {
		bodyConstants = Allergies.getConstants();
		if(patientExport.isLoaded()) {
			allergies = patientExport.getAllergies();
		}
	}

	@Override
	public void populate() {
		if(allergies != null) {
			for(Allergy allergy : allergies) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true));
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entry.setClinicalStatement(populateClinicalStatement(Arrays.asList(allergy)));
				entries.add(entry);
			}
		}

		super.populate();
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<Allergy> list) {
		AllergiesModel allergiesModel = new AllergiesModel(list.get(0));
		Act act = new Act(x_ActClassDocumentEntryAct.Act, x_DocumentActMood.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		act.setId(allergiesModel.getIds());
		act.setCode(allergiesModel.getCode());
		act.setStatusCode(allergiesModel.getStatusCode());
		act.setEffectiveTime(allergiesModel.getEffectiveTime());

		entryRelationships.add(allergiesModel.getAllergyObservation());

		act.setEntryRelationship(entryRelationships);
		return act;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		AllergiesModel allergiesModel = new AllergiesModel(null);
		Act act = new Act(x_ActClassDocumentEntryAct.Act, x_DocumentActMood.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		act.setId(allergiesModel.getIds());
		act.setCode(allergiesModel.getCode());
		act.setStatusCode(allergiesModel.getStatusCode());
		act.setEffectiveTime(allergiesModel.getEffectiveTime());

		entryRelationships.add(allergiesModel.getAllergyObservation());

		act.setEntryRelationship(entryRelationships);
		return act;
	}

	@Override
	public List<String> populateText() {
		List<String> list = new ArrayList<String>();
		if(allergies != null) {
			for(Allergy allergy : allergies) {
				AllergiesModel allergiesModel = new AllergiesModel(allergy);
				list.add(allergiesModel.getTextSummary());
			}
		}

		return list;
	}
}
