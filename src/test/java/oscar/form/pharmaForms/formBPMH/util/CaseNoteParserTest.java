/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.form.pharmaForms.formBPMH.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaseNoteParserTest {

	@Test
	public void testNoteParser() {
		String note = "<unotes>Family Physician : Dr. Joe, p:604-789-3652, f:037-286-3753</unotes>";

		assertEquals("Dr. Joe", CaseNoteParser.getFamilyDr(note));
		assertEquals("604-789-3652",CaseNoteParser.getPhoneNumber(note));
		assertEquals("037-286-3753",CaseNoteParser.getFaxNumber(note));
	}
	@Test
	public void testNoteParserMangeledInput() {
		String note = "<unotes>Family Physician: Dr. Iglesias, p:(250)957-2332</unotes>";

		assertEquals("Dr. Iglesias",CaseNoteParser.getFamilyDr(note));
		assertEquals("(250)957-2332",CaseNoteParser.getPhoneNumber(note));
		assertEquals("",CaseNoteParser.getFaxNumber(note));
	}
	@Test
	public void testNoteParserMangeledInputII() {
		String note = "<unotes>Family Physician: Dr. Iglesias (250)957-2332</unotes>";

		assertEquals("Dr. Iglesias (250)957-2332",CaseNoteParser.getFamilyDr(note));
		assertEquals("",CaseNoteParser.getPhoneNumber(note));
		assertEquals("",CaseNoteParser.getFaxNumber(note));
	}
	@Test
	public void testNoteParserNoXMLTag() {
		String note = "Family Physician : Dr. Joe, p:604-789-3652, f:037-286-3753";

		assertEquals("Dr. Joe", CaseNoteParser.getFamilyDr(note));
		assertEquals("604-789-3652",CaseNoteParser.getPhoneNumber(note));
		assertEquals("037-286-3753",CaseNoteParser.getFaxNumber(note));
	}
	
	@Test
	public void testNoteParserEmpty() {
		String note = "";

		assertEquals("", CaseNoteParser.getFamilyDr(note));
		assertEquals("",CaseNoteParser.getPhoneNumber(note));
		assertEquals("",CaseNoteParser.getFaxNumber(note));
	}
	
	@Test
	public void testNoteParserNull() {
		String note = null;

		assertEquals("", CaseNoteParser.getFamilyDr(note));
		assertEquals("",CaseNoteParser.getPhoneNumber(note));
		assertEquals("",CaseNoteParser.getFaxNumber(note));
	}

}
