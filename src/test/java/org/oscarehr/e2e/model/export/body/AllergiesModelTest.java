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
package org.oscarehr.e2e.model.export.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActClassObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class AllergiesModelTest extends AbstractExportModelTest {
	public static AllergyDao dao;
	public static Allergy allergy;
	public static AllergiesModel allergiesModel;

	public static Allergy nullAllergy;
	public static AllergiesModel nullAllergiesModel;

	@Before
	public void before() {
		dao = SpringUtils.getBean(AllergyDao.class);
		allergy = dao.findAllergies(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		allergiesModel = new AllergiesModel(allergy);

		nullAllergy = new Allergy();
		nullAllergiesModel = new AllergiesModel(nullAllergy);
	}

	@Test
	public void allergiesModelNullTest() {
		assertNotNull(new AllergiesModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = allergiesModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullAllergiesModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void idTest() {
		SET<II> ids = allergiesModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.Allergies.toString()));
		assertTrue(id.getExtension().contains(allergy.getId().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullAllergiesModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = allergiesModel.getCode();
		assertNotNull(code);
		assertEquals("48765-2", code.getCode());
		assertEquals(Constants.CodeSystems.LOINC_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.LOINC_NAME, code.getCodeSystemName());
		assertEquals(Constants.CodeSystems.LOINC_VERSION, code.getCodeSystemVersion());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullAllergiesModel.getCode();
		assertNotNull(code);
		assertEquals("48765-2", code.getCode());
		assertEquals(Constants.CodeSystems.LOINC_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.LOINC_NAME, code.getCodeSystemName());
		assertEquals(Constants.CodeSystems.LOINC_VERSION, code.getCodeSystemVersion());
	}

	@Test
	public void statusCodeActiveTest() {
		ActStatus status = allergiesModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status);
	}

	@Test
	public void statusCodeCompleteTest() {
		allergy.setArchived(true);
		allergiesModel = new AllergiesModel(allergy);

		ActStatus status = allergiesModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);
	}

	@Test
	public void statusCodeNullTest() {
		ActStatus status = nullAllergiesModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status);
	}

	@Test
	public void effectiveTimeTest() {
		IVL<TS> ivl = allergiesModel.getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(allergy.getEntryDate()), ivl.getLow());
	}

	@Test
	public void effectiveTimeNullTest() {
		IVL<TS> ivl = nullAllergiesModel.getEffectiveTime();
		assertNotNull(ivl);
		assertTrue(ivl.isNull());
		assertEquals(NullFlavor.Unknown, ivl.getNullFlavor().getCode());
	}

	@Test
	public void allergyObservationTest() {
		EntryRelationship entryRelationship = allergiesModel.getAllergyObservation();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());
		assertNotNull(observation.getCode());
		assertNotNull(observation.getEffectiveTime());
		assertNotNull(observation.getParticipant());
		assertNotNull(observation.getEntryRelationship());
	}

	@Test
	public void allergyObservationNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getAllergyObservation();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());
		assertNotNull(observation.getCode());
		assertNotNull(observation.getEffectiveTime());
		assertNotNull(observation.getParticipant());
		assertNotNull(observation.getEntryRelationship());
	}

	@Test
	public void adverseEventCodeTest() {
		CD<String> code = allergiesModel.getAdverseEventCode();
		assertNotNull(code);
		assertEquals(Mappings.reactionTypeCode.get(allergy.getTypeCode()), code.getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, code.getCodeSystemName());
	}

	@Test
	public void adverseEventCodeNullTest() {
		CD<String> code = nullAllergiesModel.getAdverseEventCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.Unknown, code.getNullFlavor().getCode());
	}

	@Test
	public void onsetDateTest() {
		IVL<TS> ivl = allergiesModel.getOnsetDate();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(allergy.getStartDate()), ivl.getLow());
	}

	@Test
	public void onsetDateNullTest() {
		IVL<TS> ivl = nullAllergiesModel.getOnsetDate();
		assertNotNull(ivl);
		assertTrue(ivl.isNull());
		assertEquals(NullFlavor.Unknown, ivl.getNullFlavor().getCode());
	}

	@Test
	public void allergenStructureTest() {
		Participant2 provider = nullAllergiesModel.getAllergen().get(0);
		assertNotNull(provider);
		assertEquals(ParticipationType.Consumable, provider.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, provider.getContextControlCode().getCode());

		ParticipantRole participantRole = provider.getParticipantRole();
		assertNotNull(participantRole);
		assertEquals(Constants.RoleClass.MANU.toString(), participantRole.getClassCode().getCode());

		PlayingEntity playingEntity = participantRole.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertEquals(EntityClassRoot.ManufacturedMaterial, playingEntity.getClassCode().getCode());
		assertNotNull(playingEntity.getCode());
		assertNotNull(playingEntity.getName());
	}

	@Test
	public void allergenCodedTest() {
		String test = "test";
		allergy.setRegionalIdentifier(test);
		allergiesModel = new AllergiesModel(allergy);

		Participant2 provider = allergiesModel.getAllergen().get(0);
		assertNotNull(provider);

		PlayingEntity playingEntity = provider.getParticipantRole().getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);

		CE<String> code = playingEntity.getCode();
		assertNotNull(code);
		assertEquals(test, code.getCode());
		assertEquals(Constants.CodeSystems.DIN_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.DIN_NAME, code.getCodeSystemName());
	}

	@Test
	public void allergenUncodedTest() {
		Participant2 provider = allergiesModel.getAllergen().get(0);
		assertNotNull(provider);

		PlayingEntity playingEntity = provider.getParticipantRole().getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);

		CE<String> code = playingEntity.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void allergyGroupTest() {
		EntryRelationship entryRelationship = allergiesModel.getAllergenGroup();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		assertNotNull(observation.getCode());
		assertEquals(Constants.ObservationType.ALRGRP.toString(), observation.getCode().getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, observation.getCode().getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, observation.getCode().getCodeSystemName());

		assertNotNull(observation.getValue());
		assertTrue(observation.getValue().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getValue().getNullFlavor().getCode());
	}

	@Test
	public void allergyGroupNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getAllergenGroup();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		assertNotNull(observation.getCode());
		assertEquals(Constants.ObservationType.ALRGRP.toString(), observation.getCode().getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, observation.getCode().getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, observation.getCode().getCodeSystemName());

		assertNotNull(observation.getValue());
		assertTrue(observation.getValue().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getValue().getNullFlavor().getCode());
	}

	@Test
	public void lifeStageTest() {
		EntryRelationship entryRelationship = allergiesModel.getLifestage();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lifeStageNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getLifestage();
		assertNull(entryRelationship);
	}

	@Test
	public void reactionTest() {
		EntryRelationship entryRelationship = allergiesModel.getReaction();
		assertNotNull(entryRelationship);
	}

	@Test
	public void reactionNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getReaction();
		assertNull(entryRelationship);
	}

	@Test
	public void severityTest() {
		EntryRelationship entryRelationship = allergiesModel.getSeverity();
		assertNotNull(entryRelationship);
	}

	@Test
	public void severityNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getSeverity();
		assertNotNull(entryRelationship);
	}

	@Test
	public void clinicalStatusTest() {
		EntryRelationship entryRelationship = allergiesModel.getClinicalStatus();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.SUBJ, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		assertNotNull(observation.getCode());
		assertEquals(Constants.ObservationType.CLINSTAT.toString(), observation.getCode().getCode());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, observation.getCode().getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, observation.getCode().getCodeSystemName());

		assertNotNull(observation.getText());
		assertTrue(observation.getText().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getText().getNullFlavor().getCode());

		assertNotNull(observation.getValue());
		assertTrue(observation.getValue().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getValue().getNullFlavor().getCode());
	}

	@Test
	public void clinicalStatusNullTest() {
		EntryRelationship entryRelationship = nullAllergiesModel.getClinicalStatus();
		assertNotNull(entryRelationship);
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(x_ActRelationshipEntryRelationship.SUBJ, entryRelationship.getTypeCode().getCode());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		assertNotNull(observation.getCode());
		assertEquals(Constants.ObservationType.CLINSTAT.toString(), observation.getCode().getCode());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, observation.getCode().getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, observation.getCode().getCodeSystemName());

		assertNotNull(observation.getText());
		assertTrue(observation.getText().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getText().getNullFlavor().getCode());

		assertNotNull(observation.getValue());
		assertTrue(observation.getValue().isNull());
		assertEquals(NullFlavor.NoInformation, observation.getValue().getNullFlavor().getCode());
	}
}
