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
package org.oscarehr.e2e.model.export.template.observation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.template.observation.SecondaryCodeICD9ObservationModel;

public class SecondaryCodeICD9ObservationModelTest {
	@Test
	public void validSecondaryCodeICD9ObservationTest() {
		String validString = "250";
		CD<String> value = commonSecondaryCodeICD9ObservationHelper(validString);
		assertNotNull(value.getDisplayName());
		assertEquals(Constants.CodeSystems.ICD9_OID, value.getCodeSystem());
		assertEquals(Constants.CodeSystems.ICD9_NAME, value.getCodeSystemName());
		assertEquals(validString, value.getCode());
	}

	@Test
	public void nullFlavorSecondaryCodeICD9ObservationTest() {
		CD<String> value = commonSecondaryCodeICD9ObservationHelper(null);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.Unknown, value.getNullFlavor().getCode());
	}

	@SuppressWarnings("unchecked")
	private CD<String> commonSecondaryCodeICD9ObservationHelper(String icd9Value) {
		EntryRelationship entryRelationship = new SecondaryCodeICD9ObservationModel().getEntryRelationship(icd9Value);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(Constants.ObservationOids.SECONDARY_CODE_ICD9_OBSERVATION_TEMPLATE_ID, entryRelationship.getTemplateId().get(0).getRoot());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		CD<String> code = observation.getCode();
		assertNotNull(code);
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, code.getCodeSystemName());
		assertEquals(Constants.ObservationType.ICD9CODE.toString(), code.getCode());

		assertEquals(CD.class, observation.getValue().getDataType());
		CD<String> value = (CD<String>) observation.getValue();
		assertNotNull(value);
		return value;
	}
}
