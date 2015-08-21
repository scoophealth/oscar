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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.template.observation.InstructionObservationModel;

public class InstructionObservationModelTest {
	private static String validString = "test";

	@Test
	public void validInstructionObservationTest() {
		EntryRelationship entryRelationship = new InstructionObservationModel().getEntryRelationship(validString);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.SUBJ, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(Constants.ObservationOids.INSTRUCTION_OBSERVATION_TEMPLATE_ID, entryRelationship.getTemplateId().get(0).getRoot());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());
		assertEquals(validString, new String(observation.getText().getData()));

		CD<String> code = observation.getCode();
		assertNotNull(code);
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, code.getCodeSystemName());
		assertEquals(Constants.ObservationType.INSTRUCT.toString(), code.getCode());
	}

	@Test
	public void validParticipantTest() {
		EntryRelationship entryRelationship = new InstructionObservationModel().getEntryRelationship(validString);
		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		ArrayList<Participant2> participants = observation.getParticipant();
		assertNotNull(participants);
		assertEquals(1, participants.size());

		Participant2 participant = participants.get(0);
		assertNotNull(participant);
		assertEquals(ParticipationType.PrimaryInformationRecipient, participant.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, participant.getContextControlCode().getCode());

		ParticipantRole role = participant.getParticipantRole();
		assertNotNull(role);
		assertEquals(Constants.RoleClass.ROL.toString(), role.getClassCode().getCode());

		CE<String> code = role.getCode();
		assertNotNull(code);
		assertEquals(Constants.CodeSystems.ROLE_CLASS_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ROLE_CLASS_NAME, code.getCodeSystemName());
		assertEquals(Constants.RoleClass.PAT.toString(), code.getCode());

		PlayingEntity playingEntity = role.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertEquals(EntityClassRoot.Person, playingEntity.getClassCode().getCode());
	}

	@Test
	public void nullFlavorInstructionObservationTest() {
		EntryRelationship entryRelationship = new InstructionObservationModel().getEntryRelationship(null);
		assertNull(entryRelationship);
	}
}
