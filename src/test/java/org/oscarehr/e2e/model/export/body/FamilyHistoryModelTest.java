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

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.INT;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RelatedSubject;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Subject;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActClassObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationTargetSubject;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubject;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.PatientExport.FamilyHistoryEntry;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class FamilyHistoryModelTest extends AbstractExportModelTest {
	public static CaseManagementNoteDAO caseManagementNoteDao;
	public static CaseManagementNoteExtDAO caseManagementNoteExtDao;
	public static List<CaseManagementNoteExt> noteExts;
	public static CaseManagementNote familyHistory;
	public static FamilyHistoryEntry familyHistoryEntry;
	public static FamilyHistoryModel familyHistoryModel;
	public static FamilyHistoryModel nullFamilyHistoryModel;

	@BeforeClass
	public static void beforeClass() {
		caseManagementNoteDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
		caseManagementNoteExtDao = SpringUtils.getBean(CaseManagementNoteExtDAO.class);
		familyHistory = caseManagementNoteDao.getNotesByDemographic(Constants.Runtime.VALID_DEMOGRAPHIC.toString()).get(2);
		noteExts = caseManagementNoteExtDao.getExtByNote(Constants.Runtime.VALID_FAMILY_HISTORY);
	}

	@Before
	public void before() {
		familyHistoryEntry = new FamilyHistoryEntry(familyHistory, noteExts);
		familyHistoryModel = new FamilyHistoryModel(familyHistoryEntry);
		nullFamilyHistoryModel = new FamilyHistoryModel(new FamilyHistoryEntry(null, null));
	}

	@Test
	public void familyHistoryModelNullTest() {
		assertNotNull(new FamilyHistoryModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = familyHistoryModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullFamilyHistoryModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void idTest() {
		SET<II> ids = familyHistoryModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.FamilyHistory.toString()));
		assertTrue(id.getExtension().contains(familyHistoryEntry.getFamilyHistory().getId().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullFamilyHistoryModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = familyHistoryModel.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullFamilyHistoryModel.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void textTest() {
		ED text = familyHistoryModel.getText();
		assertNotNull(text);
		assertEquals(familyHistoryEntry.getFamilyHistory().getNote(), new String(text.getData()));
	}

	@Test
	public void textNullTest() {
		ED text = nullFamilyHistoryModel.getText();
		assertNull(text);
	}

	@Test
	public void effectiveTimeTest() {
		IVL<TS> ivl = familyHistoryModel.getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(familyHistoryEntry.getFamilyHistory().getObservation_date()), ivl.getLow());
	}

	@Test
	public void effectiveTimeNullTest() {
		IVL<TS> ivl = nullFamilyHistoryModel.getEffectiveTime();
		assertNotNull(ivl);
		assertTrue(ivl.isNull());
		assertEquals(NullFlavor.NoInformation, ivl.getNullFlavor().getCode());
	}

	@Test
	public void valueTest() {
		CD<String> value = familyHistoryModel.getValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.Unknown, value.getNullFlavor().getCode());
	}

	@Test
	public void valueNullTest() {
		CD<String> value = nullFamilyHistoryModel.getValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.Unknown, value.getNullFlavor().getCode());
	}

	@Test
	public void subjectNormalCodeTest() {
		Subject subject = familyHistoryModel.getSubject();
		assertNotNull(subject);
		assertEquals(ParticipationTargetSubject.SBJ, subject.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, subject.getContextControlCode().getCode());

		RelatedSubject relatedSubject = subject.getRelatedSubject();
		assertNotNull(relatedSubject);
		assertEquals(x_DocumentSubject.PersonalRelationship, relatedSubject.getClassCode().getCode());

		CE<String> code = relatedSubject.getCode();
		assertNotNull(code);
		assertEquals(Mappings.personalRelationshipRole.get(familyHistoryEntry.getExtMap().get(CaseManagementNoteExt.RELATIONSHIP).toLowerCase()), code.getCode());
		assertEquals(Constants.CodeSystems.ROLE_CODE_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ROLE_CODE_NAME, code.getCodeSystemName());
		assertEquals(familyHistoryEntry.getExtMap().get(CaseManagementNoteExt.RELATIONSHIP), code.getDisplayName());
	}

	@Test
	public void subjectOtherCodeTest() {
		String test = "test";
		familyHistoryEntry.getExtMap().remove(CaseManagementNoteExt.RELATIONSHIP);
		familyHistoryEntry.getExtMap().put(CaseManagementNoteExt.RELATIONSHIP, test);
		familyHistoryModel = new FamilyHistoryModel(familyHistoryEntry);

		Subject subject = familyHistoryModel.getSubject();
		assertNotNull(subject);
		assertEquals(ParticipationTargetSubject.SBJ, subject.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, subject.getContextControlCode().getCode());

		RelatedSubject relatedSubject = subject.getRelatedSubject();
		assertNotNull(relatedSubject);
		assertEquals(x_DocumentSubject.PersonalRelationship, relatedSubject.getClassCode().getCode());

		CE<String> code = relatedSubject.getCode();
		assertNotNull(code);
		assertEquals("OTH", code.getCode());
		assertEquals(Constants.CodeSystems.ROLE_CODE_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ROLE_CODE_NAME, code.getCodeSystemName());
		assertEquals(test, code.getDisplayName());
	}

	@Test
	public void subjectNullTest() {
		Subject subject = nullFamilyHistoryModel.getSubject();
		assertNotNull(subject);
		assertEquals(ParticipationTargetSubject.SBJ, subject.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, subject.getContextControlCode().getCode());

		RelatedSubject relatedSubject = subject.getRelatedSubject();
		assertNotNull(relatedSubject);
		assertEquals(x_DocumentSubject.PersonalRelationship, relatedSubject.getClassCode().getCode());

		CE<String> code = relatedSubject.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void treatmentCommentTest() {
		EntryRelationship entryRelationship = familyHistoryModel.getTreatmentComment();
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.SUBJ, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		CD<String> code = observation.getCode();
		assertNotNull(code);
		assertEquals(Constants.ObservationType.TRTNOTE.toString(), code.getCode());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, code.getCodeSystemName());

		ED text = observation.getText();
		assertNotNull(text);
		assertEquals(familyHistoryEntry.getExtMap().get(CaseManagementNoteExt.TREATMENT), new String(text.getData()));

		assertEquals(CD.class, observation.getValue().getDataType());
		ANY value = observation.getValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.NoInformation, value.getNullFlavor().getCode());
	}

	@Test
	public void treatmenteCommentNullTest() {
		EntryRelationship entryRelationship = nullFamilyHistoryModel.getTreatmentComment();
		assertNull(entryRelationship);
	}

	@Test
	public void billingCodeTest() {
		EntryRelationship entryRelationship = familyHistoryModel.getBillingCode();
		assertNotNull(entryRelationship);
	}

	@Test
	public void billingCodeNullTest() {
		EntryRelationship entryRelationship = nullFamilyHistoryModel.getBillingCode();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lifestageObservationTest() {
		EntryRelationship entryRelationship = familyHistoryModel.getLifestageOnset();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lifestageObservationNullTest() {
		EntryRelationship entryRelationship = nullFamilyHistoryModel.getLifestageOnset();
		assertNull(entryRelationship);
	}

	@Test
	public void ageAtOnsetTest() {
		Integer age = Integer.parseInt(familyHistoryEntry.getExtMap().get(CaseManagementNoteExt.AGEATONSET));

		EntryRelationship entryRelationship = familyHistoryModel.getAgeAtOnset();
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		CD<String> code = observation.getCode();
		assertNotNull(code);
		assertEquals("30972-4", code.getCode());
		assertEquals(Constants.CodeSystems.LOINC_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.LOINC_NAME, code.getCodeSystemName());
		assertEquals("Age at onset", code.getDisplayName());

		assertEquals(INT.class, observation.getValue().getDataType());
		INT value = (INT) observation.getValue();
		assertNotNull(value);
		assertEquals(age, value.getValue());
	}

	@Test
	public void ageAtOnsetNullTest() {
		EntryRelationship entryRelationship = nullFamilyHistoryModel.getAgeAtOnset();
		assertNull(entryRelationship);
	}
}
