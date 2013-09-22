/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package org.oscarehr.exports.e2e;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test class tests that the E2E XML Schema validator is functioning correctly.
 * 
 * @author Raymond Rusk
 */
public class E2EExportValidatorTest {
	private static String s = null;

	@BeforeClass
	public static void onlyOnce() throws IOException {
		// load string s with valid XML file
		String filename = System.getProperty("basedir")+"/src/test/resources/e2e/validatorTest.xml";
		s = readFile(filename);
	}

	@Test
	public void testE2EExportValidator() {
		assertNotNull(new E2EExportValidator());
	}

	@Test
	public void testIsWellFormedXML() {
		assertFalse(s==null || s.isEmpty());
		// check output is well-formed
		assertTrue("XML document is unexpectedly not well-formed", new E2EExportValidator().isWellFormedXML(s));
	}

	@Test
	public void testIsWellFormedXMLOnNonWellFormedDocument() {
		// logger.warn("There should be one VALIDATION ERROR warning below.");
		// string substitution below should cause error (if not suppressed)
		assertFalse("XML documented expected not well-formed but was found well-formed", new E2EExportValidator().isWellFormedXML(s.replace("</ClinicalDocument>", "</clinicalDocument>"), true));
	}

	@Test
	public void testIsValidXML() {
		// validate against XML schema
		assertTrue("XML document expected valid but was not", new E2EExportValidator().isValidXML(s));
	}

	@Test
	public void testIsValidXMLOnNonValidDocument() {
		// logger.warn("There should be one VALIDATION ERROR warning below.");
		// following statement should cause error (if not suppressed)
		assertFalse("XML document expected not valid but found valid", new E2EExportValidator().isValidXML(s.replace("DOCSECT", "DOXSECT"), true));
	}

	private static String readFile( String file ) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}
		reader.close();
		return stringBuilder.toString();
	}

}