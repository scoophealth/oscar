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
package org.oscarehr.e2e.model.export.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryOrganizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.LabOrganizer;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;

public class ResultOrganizerModelTest extends AbstractExportModelTest {
	public static ResultOrganizerModel resultOrganizerModel;

	@BeforeClass
	public static void beforeClass() {
		resultOrganizerModel = new ResultOrganizerModel();
	}

	@Test
	public void resultOrganizerActiveTest() {
		LabOrganizer labOrganizer = new LabOrganizer(Constants.Runtime.VALID_LAB_NO, "P");
		Organizer organizer = resultOrganizerStructureTestHelper(labOrganizer);

		CS<ActStatus> statusCode = organizer.getStatusCode();
		assertEquals(ActStatus.Active, statusCode.getCode());
	}

	@Test
	public void resultOrganizerCompleteTest() {
		LabOrganizer labOrganizer = new LabOrganizer(Constants.Runtime.VALID_LAB_NO, "F");
		Organizer organizer = resultOrganizerStructureTestHelper(labOrganizer);

		CS<ActStatus> statusCode = organizer.getStatusCode();
		assertEquals(ActStatus.Completed, statusCode.getCode());
	}

	@Test
	public void resultOrganizerNullTest() {
		Organizer organizer = resultOrganizerStructureTestHelper(null);
		CS<ActStatus> statusCode = organizer.getStatusCode();

		assertTrue(statusCode.isNull());
		assertEquals(NullFlavor.Unknown, statusCode.getNullFlavor().getCode());
	}

	public Organizer resultOrganizerStructureTestHelper(LabOrganizer labOrganizer) {
		EntryRelationship entryRelationship = resultOrganizerModel.getEntryRelationship(labOrganizer);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());
		assertEquals(Constants.TemplateOids.RESULT_ORGANIZER_TEMPLATE_ID, entryRelationship.getTemplateId().get(0).getRoot());

		Organizer organizer = entryRelationship.getClinicalStatementIfOrganizer();
		assertNotNull(organizer);
		assertEquals(x_ActClassDocumentEntryOrganizer.BATTERY, organizer.getClassCode().getCode());

		if(labOrganizer != null) {
			II id = organizer.getId().get(0);
			assertNotNull(id);
			assertTrue(id.getExtension().contains(Constants.IdPrefixes.LabOBR.toString()));
			assertTrue(id.getExtension().contains(labOrganizer.getGroupId().toString()));
		}

		CD<String> code = organizer.getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.Unknown, code.getNullFlavor().getCode());

		CS<ActStatus> statusCode = organizer.getStatusCode();
		assertNotNull(statusCode);

		ArrayList<Component4> labComponents = organizer.getComponent();
		assertNotNull(labComponents);
		assertEquals(1, labComponents.size());

		return organizer;
	}
}
