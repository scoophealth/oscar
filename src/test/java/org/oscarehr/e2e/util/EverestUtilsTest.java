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
package org.oscarehr.e2e.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.e2e.constant.Constants;

public class EverestUtilsTest extends DaoTestFixtures {
	@BeforeClass
	public static void beforeClass() throws Exception {
		SchemaUtils.restoreTable(Constants.Runtime.TABLES);
		assertEquals(0, SchemaUtils.loadFileIntoMySQL(Constants.Runtime.E2E_SETUP));
	}

	@SuppressWarnings("unused")
	@Test(expected=UnsupportedOperationException.class)
	public void instantiationTest() {
		new EverestUtils();
	}

	@Test
	public void isNullorEmptyorWhitespaceTest() {
		assertTrue(EverestUtils.isNullorEmptyorWhitespace(null));
		assertTrue(EverestUtils.isNullorEmptyorWhitespace(""));
		assertTrue(EverestUtils.isNullorEmptyorWhitespace(" "));
		assertFalse(EverestUtils.isNullorEmptyorWhitespace("test"));
	}

	@Test
	public void generateDocumentToStringTest() {
		ClinicalDocument clinicalDocument = new ClinicalDocument();
		assertNotNull(EverestUtils.generateDocumentToString(clinicalDocument, true));
		assertNull(EverestUtils.generateDocumentToString(null, true));
	}

	@Test
	public void prettyFormatXMLTest() {
		assertNotNull(EverestUtils.prettyFormatXML("<test/>", Constants.XML.INDENT));
		assertNull(EverestUtils.prettyFormatXML(null, Constants.XML.INDENT));
	}

	@Test
	public void getPreventionTypeTest() {
		assertNull(EverestUtils.getPreventionType(null));
		assertNotNull(EverestUtils.preventionTypeCodes);
		assertEquals(40, EverestUtils.preventionTypeCodes.size());
		assertEquals("J07CA02", EverestUtils.getPreventionType("DTaP-HBV-IPV-Hib"));
	}

	@Test
	public void getDemographicProviderNoTest() {
		assertNull(EverestUtils.getDemographicProviderNo(Constants.Runtime.INVALID_VALUE.toString()));
		assertNotNull(EverestUtils.getDemographicProviderNo(Constants.Runtime.VALID_DEMOGRAPHIC.toString()));
		// Test Caching
		assertNotNull(EverestUtils.getDemographicProviderNo(Constants.Runtime.VALID_DEMOGRAPHIC.toString()));
	}

	@Test
	public void getICD9DescriptionTest() {
		assertNull(EverestUtils.getICD9Description(null));
		assertNotNull(EverestUtils.getICD9Description("428"));
		// Test Caching
		assertNotNull(EverestUtils.getICD9Description("428"));
	}
}
