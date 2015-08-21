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

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.e2e.constant.BodyConstants;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class MedicationsModelTest extends AbstractExportModelTest {
	public static DrugDao dao;
	public static Drug drug;
	public static MedicationsModel medicationsModel;

	public static Drug nullDrug;
	public static MedicationsModel nullMedicationsModel;

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(DrugDao.class);
		drug = dao.findByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		medicationsModel = new MedicationsModel(drug);

		nullDrug = new Drug();
		nullMedicationsModel = new MedicationsModel(nullDrug);
	}

	@Test
	public void medicationsModelNullTest() {
		assertNotNull(new MedicationsModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = medicationsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullMedicationsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void idTest() {
		SET<II> ids = medicationsModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.Medications.toString()));
		assertTrue(id.getExtension().contains(drug.getId().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullMedicationsModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = medicationsModel.getCode();
		assertNotNull(code);

		assertEquals(Constants.SubstanceAdministrationType.DRUG.toString(), code.getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, code.getCodeSystemName());
		assertEquals(BodyConstants.Medications.DRUG_THERAPY_ACT_NAME, code.getDisplayName());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullMedicationsModel.getCode();
		assertNotNull(code);

		assertEquals(Constants.SubstanceAdministrationType.DRUG.toString(), code.getCode());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME, code.getCodeSystemName());
		assertEquals(BodyConstants.Medications.DRUG_THERAPY_ACT_NAME, code.getDisplayName());
	}

	@Test
	public void statusCodeActiveTest() {
		ActStatus status = medicationsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status);

		Drug drug2 = dao.findByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		drug2.setLongTerm(false);
		drug2.setEndDate(new Date());
		MedicationsModel medicationsModel2 = new MedicationsModel(drug2);

		ActStatus status2 = medicationsModel2.getStatusCode();
		assertNotNull(status2);
		assertEquals(ActStatus.Active, status2);
	}

	@Test
	public void statusCodeCompleteTest() {
		Drug drug2 = dao.findByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		drug2.setLongTerm(false);
		MedicationsModel medicationsModel2 = new MedicationsModel(drug2);

		ActStatus status = medicationsModel2.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);

		Drug drug3 = dao.findByDemographicId(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		drug3.setArchived(true);
		MedicationsModel medicationsModel3 = new MedicationsModel(drug3);

		ActStatus status2 = medicationsModel3.getStatusCode();
		assertNotNull(status2);
		assertEquals(ActStatus.Completed, status2);
	}

	@Test
	public void statusCodeNullTest() {
		ActStatus status = nullMedicationsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);
	}

	@Test
	public void consumableTest() {
		Consumable consumable = medicationsModel.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void consumableNullTest() {
		Consumable consumable = nullMedicationsModel.getConsumable();
		assertNotNull(consumable);
	}

	@Test
	public void recordTypeTest() {
		EntryRelationship entryRelationship = medicationsModel.getRecordType();
		assertNotNull(entryRelationship);
	}

	@Test
	public void recordTypeNullTest() {
		EntryRelationship entryRelationship = nullMedicationsModel.getRecordType();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lastReviewDateTest() {
		EntryRelationship entryRelationship = medicationsModel.getLastReviewDate();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lastReviewDateNullTest() {
		EntryRelationship entryRelationship = nullMedicationsModel.getLastReviewDate();
		assertNull(entryRelationship);
	}

	@Test
	public void prescriptionInformationTest() {
		EntryRelationship entryRelationship = medicationsModel.getPrescriptionInformation();
		assertNotNull(entryRelationship);
	}

	@Test
	public void prescriptionInformationNullTest() {
		EntryRelationship entryRelationship = nullMedicationsModel.getPrescriptionInformation();
		assertNotNull(entryRelationship);
	}
}
