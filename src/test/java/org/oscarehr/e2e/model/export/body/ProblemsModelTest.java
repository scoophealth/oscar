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
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class ProblemsModelTest extends AbstractExportModelTest {
	public static DxresearchDAO dao;
	public static Dxresearch problem;
	public static ProblemsModel problemsModel;

	public static Dxresearch nullProblem;
	public static ProblemsModel nullProblemsModel;

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(DxresearchDAO.class);
		problem = dao.getDxResearchItemsByPatient(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		problemsModel = new ProblemsModel(problem);

		nullProblem = new Dxresearch();
		nullProblemsModel = new ProblemsModel(nullProblem);
	}

	@Test
	public void problemsModelNullTest() {
		assertNotNull(new ProblemsModel(null));
	}

	@Test
	public void textSummaryTest() {
		String text = problemsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void textSummaryNullTest() {
		String text = nullProblemsModel.getTextSummary();
		assertNotNull(text);
	}

	@Test
	public void idTest() {
		SET<II> ids = problemsModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertTrue(id.getExtension().contains(Constants.IdPrefixes.ProblemList.toString()));
		assertTrue(id.getExtension().contains(problem.getDxresearchNo().toString()));
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullProblemsModel.getIds();
		assertNotNull(ids);
	}

	@Test
	public void codeTest() {
		CD<String> code = problemsModel.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = nullProblemsModel.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void textTest() {
		ED text = problemsModel.getText();
		assertNotNull(text);
	}

	@Test
	public void textNullTest() {
		ED text = nullProblemsModel.getText();
		assertNull(text);
	}

	@Test
	public void statusCodeActiveTest() {
		ActStatus status = problemsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status);
	}

	@Test
	public void statusCodeCompleteTest() {
		Dxresearch problem2 = dao.getDxResearchItemsByPatient(Constants.Runtime.VALID_DEMOGRAPHIC).get(0);
		problem2.setStatus('C');
		ProblemsModel problemsModel2 = new ProblemsModel(problem2);

		ActStatus status = problemsModel2.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);
	}

	@Test
	public void statusCodeNullTest() {
		ActStatus status = nullProblemsModel.getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status);
	}

	@Test
	public void effectiveTimeTest() {
		IVL<TS> ivl = problemsModel.getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(problem.getStartDate()), ivl.getLow());
	}

	@Test
	public void effectiveTimeNullTest() {
		IVL<TS> ivl = nullProblemsModel.getEffectiveTime();
		assertNull(ivl);
	}

	@Test
	public void valueTest() {
		CD<String> value = problemsModel.getValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.Unknown, value.getNullFlavor().getCode());
	}

	@Test
	public void valueNullTest() {
		CD<String> value = nullProblemsModel.getValue();
		assertNotNull(value);
		assertTrue(value.isNull());
		assertEquals(NullFlavor.Unknown, value.getNullFlavor().getCode());
	}

	@Test
	public void authorTest() {
		ArrayList<Author> authors = problemsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void authorNullTest() {
		ArrayList<Author> authors = nullProblemsModel.getAuthor();
		assertNotNull(authors);
		assertEquals(1, authors.size());
	}

	@Test
	public void lastReviewDateTest() {
		EntryRelationship entryRelationship = problemsModel.getDiagnosisDate();
		assertNotNull(entryRelationship);
	}

	@Test
	public void lastReviewDateNullTest() {
		EntryRelationship entryRelationship = nullProblemsModel.getDiagnosisDate();
		assertNull(entryRelationship);
	}
}
