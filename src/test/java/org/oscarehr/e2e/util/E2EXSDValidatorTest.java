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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

public class E2EXSDValidatorTest {
	private static String s;

	@BeforeClass
	public static void beforeClass() {
		s = new Scanner(E2EXSDValidatorTest.class.getResourceAsStream("/e2e/validatorTest.xml"), "UTF-8").useDelimiter("\\Z").next();
		assertNotNull(s);
		assertFalse(s.isEmpty());
	}

	@SuppressWarnings("unused")
	@Test(expected=UnsupportedOperationException.class)
	public void instantiationTest() {
		new E2EXSDValidator();
	}

	@Test
	public void isWellFormedXMLTest() {
		assertTrue(E2EXSDValidator.isWellFormedXML(s));
	}

	@Test
	public void isWellFormedXMLOnNonWellFormedDocumentTest() {
		assertFalse(E2EXSDValidator.isWellFormedXML(s.replace("</ClinicalDocument>", "</clinicalDocument>")));
	}

	@Test
	public void testIsValidXML() {
		assertTrue(E2EXSDValidator.isValidXML(s));
	}

	@Test
	public void testIsValidXMLOnNonValidDocument() {
		assertFalse(E2EXSDValidator.isValidXML(s.replace("DOCSECT", "DOXSECT")));
	}
}
