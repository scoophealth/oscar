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

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component3;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.StructuredBody;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.populator.AbstractPopulator;

public class DocumentBodyPopulator extends AbstractPopulator {
	public DocumentBodyPopulator(PatientExport patientExport) {
		populators.add(new AdvanceDirectivesPopulator());
		populators.add(new AlertsPopulator(patientExport));
		populators.add(new AllergiesPopulator(patientExport));
		populators.add(new ClinicallyMeasuredObservationsPopulator(patientExport));
		populators.add(new EncountersPopulator(patientExport));
		populators.add(new FamilyHistoryPopulator(patientExport));
		populators.add(new ImmunizationsPopulator(patientExport));
		populators.add(new LabsPopulator(patientExport));
		populators.add(new MedicationsPopulator(patientExport));
		populators.add(new OrdersAndRequestsPopulator());
		populators.add(new ProblemsPopulator(patientExport));
		populators.add(new RiskFactorsPopulator(patientExport));
	}

	@Override
	public void populate() {
		Component2 bodyComponent = new Component2();
		bodyComponent.setTypeCode(ActRelationshipHasComponent.HasComponent);
		bodyComponent.setContextConductionInd(true);
		bodyComponent.setBodyChoice(getStructuredBody());

		clinicalDocument.setComponent(bodyComponent);
	}

	public StructuredBody getStructuredBody() {
		StructuredBody body = new StructuredBody();
		ArrayList<Component3> sectionComponents = new ArrayList<Component3>();

		body.setConfidentialityCode(x_BasicConfidentialityKind.Normal);
		body.setLanguageCode(Constants.DocumentHeader.LANGUAGE_ENGLISH_CANADIAN);
		body.setComponent(sectionComponents);
		return body;
	}
}
