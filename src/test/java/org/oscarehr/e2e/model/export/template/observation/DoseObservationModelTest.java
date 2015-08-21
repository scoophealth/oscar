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

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PQ;
import org.marc.everest.datatypes.SetOperator;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.PIVL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.util.SpringUtils;

public class DoseObservationModelTest extends AbstractExportModelTest {
	public static DrugDao dao;
	public static Drug drug;
	public static Drug nullDrug;
	public static DoseObservationModel doseObservationModel;

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(DrugDao.class);
		doseObservationModel = new DoseObservationModel();
	}

	@Before
	public void before() {
		drug = dao.findByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		nullDrug = new Drug();
	}

	@Test
	public void doseObservationStructureTest() {
		EntryRelationship entryRelationship = doseObservationModel.getEntryRelationship(drug);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(Constants.TemplateOids.DOSE_OBSERVATION_TEMPLATE_ID, entryRelationship.getTemplateId().get(0).getRoot());

		SubstanceAdministration substanceAdministration = entryRelationship.getClinicalStatementIfSubstanceAdministration();
		assertNotNull(substanceAdministration);
		assertEquals(x_DocumentSubstanceMood.RQO, substanceAdministration.getMoodCode().getCode());

		Consumable consumable = substanceAdministration.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void doseObservationNullTest() {
		EntryRelationship entryRelationship = doseObservationModel.getEntryRelationship(null);
		assertNotNull(entryRelationship);

		SubstanceAdministration substanceAdministration = entryRelationship.getClinicalStatementIfSubstanceAdministration();
		assertNotNull(substanceAdministration);

		Consumable consumable = substanceAdministration.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void doseInstructionsTest() {
		ED text = substanceAdministrationHelper(drug).getText();
		assertNotNull(text);
	}

	@Test
	public void doseInstructionsNullTest() {
		ED text = substanceAdministrationHelper(nullDrug).getText();
		assertNotNull(text);
	}

	@Test
	public void durationTest() {
		IVL<TS> ivl = (IVL<TS>) substanceAdministrationHelper(drug).getEffectiveTime().get(0);
		assertNotNull(ivl);
		assertTrue(IVL.isValidWidthFlavor(ivl));
		assertEquals(drug.getDuration(), ivl.getWidth().getValue().toString());
		assertEquals(drug.getDurUnit(), ivl.getWidth().getUnit());
	}

	@Test
	public void durationInvalidTest() {
		drug.setDuration("test");

		IVL<TS> ivl = (IVL<TS>) substanceAdministrationHelper(drug).getEffectiveTime().get(0);
		assertNotNull(ivl);
		assertTrue(ivl.isNull());
		assertEquals(NullFlavor.Unknown, ivl.getNullFlavor().getCode());
	}

	@Test
	public void durationNullTest() {
		IVL<TS> ivl = (IVL<TS>) substanceAdministrationHelper(nullDrug).getEffectiveTime().get(0);
		assertNotNull(ivl);
		assertTrue(ivl.isNull());
		assertEquals(NullFlavor.Unknown, ivl.getNullFlavor().getCode());
	}

	@Test
	public void frequencyTest() {
		PIVL<TS> pivl = (PIVL<TS>) substanceAdministrationHelper(drug).getEffectiveTime().get(1);
		assertNotNull(pivl);
		assertEquals(new PQ(BigDecimal.ONE, Constants.TimeUnit.d.toString()), pivl.getPeriod());
		assertEquals(SetOperator.Intersect, pivl.getOperator());
		assertTrue(pivl.getInstitutionSpecified());
	}

	@Test
	public void frequencyInvalidTest() {
		drug.setFreqCode("test");

		PIVL<TS> pivl = (PIVL<TS>) substanceAdministrationHelper(drug).getEffectiveTime().get(1);
		assertNotNull(pivl);
		assertTrue(pivl.isNull());
		assertEquals(NullFlavor.Other, pivl.getNullFlavor().getCode());
	}

	@Test
	public void frequencyNullTest() {
		PIVL<TS> pivl = (PIVL<TS>) substanceAdministrationHelper(nullDrug).getEffectiveTime().get(1);
		assertNotNull(pivl);
		assertTrue(pivl.isNull());
		assertEquals(NullFlavor.Unknown, pivl.getNullFlavor().getCode());
	}

	@Test
	public void routeTest() {
		CE<String> route = substanceAdministrationHelper(drug).getRouteCode();
		assertNotNull(route);
		assertEquals(drug.getRoute().toUpperCase(), route.getCode());
		assertEquals(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_OID, route.getCodeSystem());
		assertEquals(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_NAME, route.getCodeSystemName());
	}

	@Test
	public void routeNullTest() {
		CE<String> route = substanceAdministrationHelper(nullDrug).getRouteCode();
		assertNull(route);
	}

	@Test
	public void doseQuantityTest() {
		IVL<PQ> ivl = substanceAdministrationHelper(drug).getDoseQuantity();
		assertNotNull(ivl);
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMin()), null), ivl.getLow());
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMax()), null), ivl.getHigh());

		drug.setUnitName("te st");

		ivl = substanceAdministrationHelper(drug).getDoseQuantity();
		assertNotNull(ivl);
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMin()), "te_st"), ivl.getLow());
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMax()), "te_st"), ivl.getHigh());
	}

	@Test
	public void doseQuantityNullTest() {
		IVL<PQ> ivl = substanceAdministrationHelper(drug).getDoseQuantity();
		assertNotNull(ivl);
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMin()), null), ivl.getLow());
		assertEquals(new PQ(BigDecimal.valueOf(drug.getTakeMax()), null), ivl.getHigh());
	}

	@Test
	public void formTest() {
		CE<String> form = substanceAdministrationHelper(drug).getAdministrationUnitCode();
		assertNotNull(form);
		assertEquals(Mappings.formCode.get(drug.getDrugForm()), form.getCode());
		assertEquals(Constants.CodeSystems.ADMINISTERABLE_DRUG_FORM_OID, form.getCodeSystem());
		assertEquals(Constants.CodeSystems.ADMINISTERABLE_DRUG_FORM_NAME, form.getCodeSystemName());
		assertEquals(drug.getDrugForm(), form.getDisplayName());
	}

	@Test
	public void formNullTest() {
		CE<String> form = substanceAdministrationHelper(nullDrug).getAdministrationUnitCode();
		assertNull(form);
	}

	public SubstanceAdministration substanceAdministrationHelper(Drug drug) {
		EntryRelationship entryRelationship = doseObservationModel.getEntryRelationship(drug);
		return entryRelationship.getClinicalStatementIfSubstanceAdministration();
	}
}
