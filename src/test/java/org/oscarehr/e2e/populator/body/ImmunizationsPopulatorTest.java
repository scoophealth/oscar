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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.oscarehr.e2e.constant.BodyConstants.Immunizations;

public class ImmunizationsPopulatorTest extends AbstractBodyPopulatorTest {
	@BeforeClass
	public static void beforeClass() {
		setupClass(Immunizations.getConstants());
	}

	@Test
	public void immunizationsComponentSectionTest() {
		componentSectionTest();
	}

	@Test
	public void immunizationsEntryCountTest() {
		entryCountTest(3);
	}

	@Test
	public void immunizationsEntryStructureTest() {
		entryStructureTest();
	}

	@Test
	public void immunizationsClinicalStatementTest() {
		ClinicalStatement clinicalStatement = component.getSection().getEntry().get(0).getClinicalStatement();
		assertNotNull(clinicalStatement);
		assertTrue(clinicalStatement.isPOCD_MT000040UVSubstanceAdministration());

		SubstanceAdministration substanceAdministration = (SubstanceAdministration) clinicalStatement;
		assertEquals(x_DocumentSubstanceMood.Eventoccurrence, substanceAdministration.getMoodCode().getCode());
		assertNotNull(substanceAdministration.getId());
		assertNotNull(substanceAdministration.getEffectiveTime());
		assertNotNull(substanceAdministration.getConsumable());
		assertNotNull(substanceAdministration.getAuthor());
		assertNotNull(substanceAdministration.getParticipant());
	}
}
