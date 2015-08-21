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
package org.oscarehr.e2e.populator.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.marc.everest.datatypes.TS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.oscarehr.e2e.populator.AbstractPopulatorTest;

public class AuthorPopulatorTest extends AbstractPopulatorTest {
	@Test
	public void authorTest() {
		ArrayList<Author> authors = clinicalDocument.getAuthor();
		assertNotNull(authors);
		assertEquals(2, authors.size());
	}

	@Test
	public void authorProviderTest() {
		Author author = clinicalDocument.getAuthor().get(0);
		assertNotNull(author);
		assertEquals(ContextControl.OverridingPropagating, author.getContextControlCode().getCode());
		assertFalse(author.getTime().isInvalidDate());

		TS now = TS.now();
		now.setDateValuePrecision(TS.DAY);
		assertTrue(author.getTime().toString().contains(now.toString()));

		AssignedAuthor assignedAuthor = author.getAssignedAuthor();
		assertNotNull(assignedAuthor);
		assertNotNull(assignedAuthor.getAssignedAuthorChoiceIfAssignedPerson());
	}

	@Test
	public void authorSystemTest() {
		Author author = clinicalDocument.getAuthor().get(1);
		assertNotNull(author);
		assertEquals(ContextControl.OverridingPropagating, author.getContextControlCode().getCode());
		assertFalse(author.getTime().isInvalidDate());

		TS now = TS.now();
		now.setDateValuePrecision(TS.DAY);
		assertTrue(author.getTime().toString().contains(now.toString()));

		AssignedAuthor assignedAuthor = author.getAssignedAuthor();
		assertNotNull(assignedAuthor);
		assertNotNull(assignedAuthor.getAssignedAuthorChoiceIfAssignedAuthoringDevice());
	}
}
