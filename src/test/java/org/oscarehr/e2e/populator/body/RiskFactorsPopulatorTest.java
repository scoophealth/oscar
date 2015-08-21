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
package org.oscarehr.e2e.populator.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActMoodEventOccurrence;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryOrganizer;
import org.oscarehr.e2e.constant.BodyConstants.RiskFactors;

public class RiskFactorsPopulatorTest extends AbstractBodyPopulatorTest {
	@BeforeClass
	public static void beforeClass() {
		setupClass(RiskFactors.getConstants());
	}

	@Test
	public void riskFactorsComponentSectionTest() {
		componentSectionTest();
	}

	@Test
	public void riskFactorsEntryCountTest() {
		entryCountTest(1);
	}

	@Test
	public void riskFactorsEntryStructureTest() {
		entryStructureTest();
	}

	@Test
	public void riskFactorsClinicalStatementTest() {
		ClinicalStatement clinicalStatement = component.getSection().getEntry().get(0).getClinicalStatement();
		assertNotNull(clinicalStatement);
		assertTrue(clinicalStatement.isPOCD_MT000040UVOrganizer());

		Organizer organizer = (Organizer) clinicalStatement;
		assertEquals(x_ActClassDocumentEntryOrganizer.CLUSTER, organizer.getClassCode().getCode());
		assertEquals(ActMoodEventOccurrence.Eventoccurrence, organizer.getMoodCode().getCode());
		assertNotNull(organizer.getId());
		assertNotNull(organizer.getCode());
		assertNotNull(organizer.getStatusCode());
		assertNotNull(organizer.getComponent());
	}
}
