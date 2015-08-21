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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.oscarehr.e2e.constant.BodyConstants.Labs;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.PatientExport.Lab;
import org.oscarehr.e2e.model.export.body.LabsModel;

public class LabsPopulator extends AbstractBodyPopulator<Lab> {
	private List<Lab> labs = null;

	LabsPopulator(PatientExport patientExport) {
		bodyConstants = Labs.getConstants();
		if(patientExport.isLoaded()) {
			labs = patientExport.getLabs();
		}
	}

	@Override
	public void populate() {
		if(labs != null) {
			for(Lab lab : labs) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true));
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entry.setClinicalStatement(populateClinicalStatement(Arrays.asList(lab)));
				entries.add(entry);
			}
		}

		super.populate();
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<Lab> list) {
		LabsModel labsModel = new LabsModel(list.get(0));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		observation.setId(labsModel.getIds());
		observation.setCode(labsModel.getCode());
		observation.setText(labsModel.getText());
		observation.setAuthor(labsModel.getAuthor());
		observation.setEntryRelationship(labsModel.getResultOrganizers());

		return observation;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		LabsModel labsModel = new LabsModel(null);
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		observation.setId(labsModel.getIds());
		observation.setCode(labsModel.getCode());
		observation.setText(labsModel.getText());
		observation.setAuthor(labsModel.getAuthor());
		observation.setEntryRelationship(labsModel.getResultOrganizers());

		return observation;
	}

	@Override
	public List<String> populateText() {
		List<String> list = new ArrayList<String>();
		if(labs != null) {
			for(Lab lab : labs) {
				LabsModel labsModel = new LabsModel(lab);
				list.add(labsModel.getTextSummary());
			}
		}

		return list;
	}
}
