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

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class RiskFactorsModelTest extends AbstractExportModelTest {
	public static CaseManagementNoteDAO dao;
	public static CaseManagementNote riskFactor;
	public static RiskFactorsModel riskFactorsModel;

	public static CaseManagementNote nullRiskFactor;
	public static RiskFactorsModel nullRiskFactorsModel;

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(CaseManagementNoteDAO.class);
		riskFactor = dao.getNotesByDemographic(Constants.Runtime.VALID_DEMOGRAPHIC.toString()).get(1);
		riskFactorsModel = new RiskFactorsModel(riskFactor);

		nullRiskFactor = new CaseManagementNote();
		nullRiskFactorsModel = new RiskFactorsModel(nullRiskFactor);
	}

	@Test
	public void riskFactorsModelNullTest() {
		assertNotNull(new RiskFactorsModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = riskFactorsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullRiskFactorsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void idTest() {
		SET<II> ids = riskFactorsModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.RiskFactors.toString()));
		assertTrue(id.getExtension().contains(riskFactor.getId().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullRiskFactorsModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = riskFactorsModel.getCode();
		assertNotNull(code);
		assertEquals("40514009", code.getCode());
		assertEquals(Constants.CodeSystems.SNOMED_CT_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.SNOMED_CT_NAME, code.getCodeSystemName());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullRiskFactorsModel.getCode();
		assertNotNull(code);
		assertEquals("40514009", code.getCode());
		assertEquals(Constants.CodeSystems.SNOMED_CT_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.SNOMED_CT_NAME, code.getCodeSystemName());
	}

	@Test
	public void statusCodeTest() {
		ActStatus status = riskFactorsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);
	}

	@Test
	public void statusCodeNullTest() {
		ActStatus status = nullRiskFactorsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status);
	}

	@Test
	public void authorTest() {
		ArrayList<Author> authors = riskFactorsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void authorNullTest() {
		ArrayList<Author> authors = nullRiskFactorsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void componentObservationTest() {
		ArrayList<Component4> components = riskFactorsModel.getComponentObservation();
		assertNotNull(components);
		assertEquals(1, components.size());

		Component4 component = components.get(0);
		assertNotNull(component);
		assertEquals(ActRelationshipHasComponent.HasComponent, component.getTypeCode().getCode());
		assertTrue(component.getContextConductionInd().toBoolean());

		Observation observation = component.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());
		assertNotNull(observation.getId());
		assertNotNull(observation.getCode());
		assertNotNull(observation.getValue());
	}

	@Test
	public void componentObservationNullTest() {
		ArrayList<Component4> components = nullRiskFactorsModel.getComponentObservation();
		assertNotNull(components);
		assertEquals(1, components.size());

		Component4 component = components.get(0);
		assertNotNull(component);
		assertEquals(ActRelationshipHasComponent.HasComponent, component.getTypeCode().getCode());
		assertTrue(component.getContextConductionInd().toBoolean());

		Observation observation = component.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());
		assertNotNull(observation.getId());
		assertNotNull(observation.getCode());
		assertNotNull(observation.getValue());
	}

	@Test
	public void observationCodeTest() {
		CD<String> code = riskFactorsModel.getObservationCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void observationCodeNullTest() {
		CD<String> code = nullRiskFactorsModel.getObservationCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void observationNameTest() {
		ED text = riskFactorsModel.getObservationName();
		assertNotNull(text);
		assertEquals(riskFactor.getNote(), new String(text.getData()));
	}

	@Test
	public void observationNameNullTest() {
		ED text = nullRiskFactorsModel.getObservationName();
		assertNull(text);
	}

	@Test
	public void observationDateTest() {
		IVL<TS> ivl = riskFactorsModel.getObservationDate();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(riskFactor.getObservation_date()), ivl.getLow());
	}

	@Test
	public void observationDateNullTest() {
		IVL<TS> ivl = nullRiskFactorsModel.getObservationDate();
		assertNull(ivl);
	}

	@Test
	public void observationValueTest() {
		ST value = riskFactorsModel.getObservationValue();
		assertNotNull(value);
		assertEquals(riskFactor.getNote(), value.getValue());
	}

	@Test
	public void observationValueNullTest() {
		ST value = nullRiskFactorsModel.getObservationValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.NoInformation, value.getNullFlavor().getCode());
	}
}
