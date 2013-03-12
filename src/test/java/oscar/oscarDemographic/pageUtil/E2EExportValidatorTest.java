/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarDemographic.pageUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class E2EExportValidatorTest {

	private static String s = null;
	
	@BeforeClass
	public static void onlyOnce() throws IOException {
		// load string s with valid XML file
		String filename = System.getProperty("basedir")+
		"/src/test/resources/e2e/output.xml";
		s = readFile(filename);
	}
	
	@Test
	public void testIsWellFormedXML() {
		boolean logErrors;
		// check output is well-formed
		logErrors = true;
		assertTrue(E2EExportValidator.isWellFormedXML(s, logErrors));
		logErrors = false; // string substitution below should cause error, don't log
		assertFalse(E2EExportValidator.isWellFormedXML(s.replace("</ClinicalDocument>",
				"</clinicalDocument>"), logErrors));
	}
	
	@Test
	public void testIsValidXML() {
		boolean logErrors;
		// validate against XML schema
		logErrors = true;
		assertTrue(E2EExportValidator.isValidXML(s, logErrors));
		logErrors = false;  // following statement should cause error, don't log
		assertFalse(E2EExportValidator.isValidXML(s.replace("DOCSECT", "DOXSECT"), logErrors));
	}

	private static String readFile( String file ) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}
		reader.close();
		return stringBuilder.toString();
	}

}