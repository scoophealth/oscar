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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.datatypes.interfaces.ISetComponent;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.Immunization;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class ImmunizationsModelTest extends AbstractExportModelTest {
	public static Prevention prevention;
	public static List<PreventionExt> preventionExt;
	public static Immunization immunization;
	public static ImmunizationsModel immunizationsModel;
	public static ImmunizationsModel nullImmunizationsModel;

	@BeforeClass
	public static void beforeClass() {
		PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
		PreventionExtDao preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);

		prevention = preventionDao.findNotDeletedByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		preventionExt = preventionExtDao.findByPreventionId(Constants.Runtime.VALID_PREVENTION);
	}

	@Before
	public void before() {
		immunization = new Immunization(prevention, preventionExt);
		immunizationsModel = new ImmunizationsModel(immunization);
		nullImmunizationsModel = new ImmunizationsModel(new Immunization(null, null));
	}

	@Test
	public void immunizationsModelNullTest() {
		assertNotNull(new ImmunizationsModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = immunizationsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullImmunizationsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void negationIndTest() {
		BL negationInd = immunizationsModel.getNegationInd();
		assertNotNull(negationInd);
		assertFalse(negationInd.toBoolean());
	}

	@Test
	public void negationIndNullTest() {
		BL negationInd = nullImmunizationsModel.getNegationInd();
		assertNotNull(negationInd);
		assertFalse(negationInd.toBoolean());
	}

	@Test
	public void idTest() {
		SET<II> ids = immunizationsModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.Immunizations.toString()));
		assertTrue(id.getExtension().contains(prevention.getId().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullImmunizationsModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = immunizationsModel.getCode();
		assertNotNull(code);

		assertEquals(Constants.SubstanceAdministrationType.IMMUNIZ.toString(), code.getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, code.getCodeSystemName());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullImmunizationsModel.getCode();
		assertNotNull(code);

		assertEquals(Constants.SubstanceAdministrationType.IMMUNIZ.toString(), code.getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, code.getCodeSystemName());
	}

	@Test
	public void effectiveTimeTest() {
		ArrayList<ISetComponent<TS>> effectiveTime = immunizationsModel.getEffectiveTime();
		assertNotNull(effectiveTime);
		assertEquals(1, effectiveTime.size());

		IVL<TS> ivl = (IVL<TS>) effectiveTime.get(0);
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(immunization.getPrevention().getPreventionDate()), ivl.getLow());
		assertNull(ivl.getHigh());
	}

	@Test
	public void effectiveTimeNullTest() {
		ArrayList<ISetComponent<TS>> effectiveTime = nullImmunizationsModel.getEffectiveTime();
		assertNotNull(effectiveTime);
		assertEquals(1, effectiveTime.size());

		IVL<TS> ivl = (IVL<TS>) effectiveTime.get(0);
		assertNotNull(ivl);
		assertTrue(ivl.getLow().isNull());
		assertEquals(NullFlavor.Unknown, ivl.getLow().getNullFlavor().getCode());
		assertNull(ivl.getHigh());
	}

	@Test
	public void routeTest() {
		CE<String> route = immunizationsModel.getRoute();
		assertNotNull(route);
		assertTrue(route.isNull());
		assertEquals(NullFlavor.Other, route.getNullFlavor().getCode());
		assertEquals(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_OID, route.getCodeSystem());
		assertEquals(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_NAME, route.getCodeSystemName());
		assertEquals(immunization.getPreventionMap().get(Constants.PreventionExtKeys.route.toString()), new String(route.getOriginalText().getData()));
	}

	@Test
	public void routeNullTest() {
		CE<String> route = nullImmunizationsModel.getRoute();
		assertNull(route);
	}

	@Test
	public void consumableTest() {
		Consumable consumable = immunizationsModel.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void consumableNullTest() {
		Consumable consumable = nullImmunizationsModel.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void authorTest() {
		ArrayList<Author> authors = immunizationsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void authorNullTest() {
		ArrayList<Author> authors = nullImmunizationsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void participantTest() {
		ArrayList<Participant2> participants = immunizationsModel.getParticipant();
		assertNotNull(participants);
		assertEquals(1, participants.size());

		Participant2 participant = participants.get(0);
		assertNotNull(participant);
		assertEquals(ParticipationType.LOC, participant.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, participant.getContextControlCode().getCode());

		ParticipantRole participantRole = participant.getParticipantRole();
		assertNotNull(participantRole);
		assertEquals(Constants.RoleClass.SDLOC.toString(), participantRole.getClassCode().getCode());

		PlayingEntity playingEntity = participantRole.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertEquals(EntityClassRoot.Organization, playingEntity.getClassCode().getCode());
		assertNotNull(playingEntity.getName());
		assertNotNull(playingEntity.getName().get(0));

		ENXP enxp = playingEntity.getName().get(0).getPart(0);
		assertNotNull(enxp);
		assertEquals(immunization.getPreventionMap().get(Constants.PreventionExtKeys.location.toString()), enxp.getValue());
	}

	@Test
	public void participantNullTest() {
		ArrayList<Participant2> participants = nullImmunizationsModel.getParticipant();
		assertNotNull(participants);
		assertEquals(1, participants.size());

		Participant2 participant = participants.get(0);
		assertNotNull(participant);
		assertEquals(ParticipationType.LOC, participant.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, participant.getContextControlCode().getCode());

		ParticipantRole participantRole = participant.getParticipantRole();
		assertNotNull(participantRole);
		assertEquals(Constants.RoleClass.SDLOC.toString(), participantRole.getClassCode().getCode());

		PlayingEntity playingEntity = participantRole.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertEquals(EntityClassRoot.Organization, playingEntity.getClassCode().getCode());
		assertNull(playingEntity.getName());
	}

	@Test
	public void antigenTypeTest() {
		EntryRelationship entryRelationship = immunizationsModel.getAntigenType();
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());

		SubstanceAdministration substanceAdministration = entryRelationship.getClinicalStatementIfSubstanceAdministration();
		assertNotNull(substanceAdministration);

		Consumable consumable = substanceAdministration.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void antigenTypeNullTest() {
		EntryRelationship entryRelationship = nullImmunizationsModel.getAntigenType();
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());

		SubstanceAdministration substanceAdministration = entryRelationship.getClinicalStatementIfSubstanceAdministration();
		assertNotNull(substanceAdministration);

		Consumable consumable = substanceAdministration.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void reasonTest() {
		EntryRelationship entryRelationship = immunizationsModel.getRefusalReason();
		assertNotNull(entryRelationship);
	}

	@Test
	public void reasonNullTest() {
		EntryRelationship entryRelationship = nullImmunizationsModel.getRefusalReason();
		assertNull(entryRelationship);
	}

	@Test
	public void nextDateTest() {
		EntryRelationship entryRelationship = immunizationsModel.getNextDate();
		assertNotNull(entryRelationship);
	}

	@Test
	public void nextDateNullTest() {
		EntryRelationship entryRelationship = nullImmunizationsModel.getNextDate();
		assertNull(entryRelationship);
	}

	@Test
	public void commentTest() {
		EntryRelationship entryRelationship = immunizationsModel.getComment();
		assertNotNull(entryRelationship);
	}

	@Test
	public void commentNullTest() {
		EntryRelationship entryRelationship = nullImmunizationsModel.getComment();
		assertNull(entryRelationship);
	}
}
