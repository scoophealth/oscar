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
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.e2e.constant.BodyConstants.Problems;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.export.body.ProblemsModel;

public class ProblemsPopulator extends AbstractBodyPopulator<Dxresearch> {
	private List<Dxresearch> allProblems = null;
	private List<Dxresearch> problems = null;

	ProblemsPopulator(PatientExport patientExport) {
		bodyConstants = Problems.getConstants();
		if(patientExport.isLoaded()) {
			allProblems = patientExport.getProblems();
		}
		problems = new ArrayList<Dxresearch>();

		if(allProblems != null) {
			for(Dxresearch problem : allProblems) {
				if(problem.getStatus() != 'D' && problem.getCodingSystem().equalsIgnoreCase("icd9")) {
					this.problems.add(problem);
				}
			}
		}
	}

	@Override
	public void populate() {
		for(Dxresearch problem : problems) {
			if(problem.getCodingSystem().equals("icd9")) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true));
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entry.setClinicalStatement(populateClinicalStatement(Arrays.asList(problem)));
				entries.add(entry);
			}
		}

		super.populate();
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<Dxresearch> problem) {
		ProblemsModel problemsModel = new ProblemsModel(problem.get(0));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		observation.setId(problemsModel.getIds());
		observation.setCode(problemsModel.getCode());
		observation.setText(problemsModel.getText());
		observation.setStatusCode(problemsModel.getStatusCode());
		observation.setEffectiveTime(problemsModel.getEffectiveTime());
		observation.setValue(problemsModel.getValue());
		observation.setAuthor(problemsModel.getAuthor());

		entryRelationships.add(problemsModel.getSecondaryCodeICD9());
		entryRelationships.add(problemsModel.getDiagnosisDate());

		observation.setEntryRelationship(entryRelationships);

		return observation;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		ProblemsModel problemsModel = new ProblemsModel(null);
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		observation.setId(problemsModel.getIds());
		observation.setCode(problemsModel.getCode());
		observation.setAuthor(problemsModel.getAuthor());

		return observation;
	}

	@Override
	public List<String> populateText() {
		List<String> list = new ArrayList<String>();
		for(Dxresearch problem : problems) {
			if(problem.getCodingSystem().equals("icd9")) {
				ProblemsModel problemsModel = new ProblemsModel(problem);
				list.add(problemsModel.getTextSummary());
			}
		}

		return list;
	}
}
