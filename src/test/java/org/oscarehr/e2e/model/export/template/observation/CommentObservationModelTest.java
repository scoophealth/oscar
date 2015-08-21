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
import static org.junit.Assume.assumeNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.template.observation.CommentObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class CommentObservationModelTest {
	private static final String test = "test";
	private static final Date time = new Date();

	@Test
	public void commentObservationCommentOnlyTest() {
		Observation observation = commentObservationStructureTestHelper(test, null, null);
		ED text = observation.getText();
		assertNotNull(text);
		assertEquals(test, new String(text.getData()));

		IVL<TS> ivl = observation.getEffectiveTime();
		assertNull(ivl);

		ArrayList<Author> authors = observation.getAuthor();
		assertNotNull(authors);
		assertTrue(authors.isEmpty());
	}

	@Test
	public void commentObservationCommentDateTest() {
		Observation observation = commentObservationStructureTestHelper(test, time, null);
		ED text = observation.getText();
		assertNotNull(text);
		assertEquals(test, new String(text.getData()));

		IVL<TS> ivl = observation.getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(time), ivl.getLow());

		ArrayList<Author> authors = observation.getAuthor();
		assertNotNull(authors);
		assertTrue(authors.isEmpty());
	}

	@Test
	public void commentObservationCommentAuthorTest() {
		Observation observation = commentObservationStructureTestHelper(test, null, Constants.Runtime.VALID_PROVIDER.toString());
		ED text = observation.getText();
		assertNotNull(text);
		assertEquals(test, new String(text.getData()));

		IVL<TS> ivl = observation.getEffectiveTime();
		assertNull(ivl);

		ArrayList<Author> authors = observation.getAuthor();
		assertNotNull(authors);
	}

	@Test
	public void commentObservationAllTest() {
		Observation observation = commentObservationStructureTestHelper(test, time, Constants.Runtime.VALID_PROVIDER.toString());
		ED text = observation.getText();
		assertNotNull(text);
		assertEquals(test, new String(text.getData()));

		IVL<TS> ivl = observation.getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(time), ivl.getLow());

		ArrayList<Author> authors = observation.getAuthor();
		assertNotNull(authors);
	}

	@Test
	public void commentObservationNullTest() {
		EntryRelationship entryRelationship = new CommentObservationModel().getEntryRelationship(null, null, null);
		assertNull(entryRelationship);

		EntryRelationship entryRelationship2 = new CommentObservationModel().getEntryRelationship(null, new Date(), null);
		assertNull(entryRelationship2);

		EntryRelationship entryRelationship3 = new CommentObservationModel().getEntryRelationship(null, null, new String());
		assertNull(entryRelationship3);

		EntryRelationship entryRelationship4 = new CommentObservationModel().getEntryRelationship(null, new Date(), new String());
		assertNull(entryRelationship4);
	}

	private Observation commentObservationStructureTestHelper(String comment, Date time, String author) {
		assumeNotNull(comment);

		EntryRelationship entryRelationship = new CommentObservationModel().getEntryRelationship(comment, time, author);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.SUBJ, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(Constants.ObservationOids.COMMENT_OBSERVATION_TEMPLATE_ID, entryRelationship.getTemplateId().get(0).getRoot());

		Observation observation = entryRelationship.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		CD<String> code = observation.getCode();
		assertNotNull(code);
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME, code.getCodeSystemName());
		assertEquals(Constants.ObservationType.COMMENT.toString(), code.getCode());

		return observation;
	}
}
