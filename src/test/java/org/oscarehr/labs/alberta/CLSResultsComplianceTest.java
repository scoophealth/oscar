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
package org.oscarehr.labs.alberta;

import static org.junit.Assert.*;

import org.junit.Test;

import oscar.oscarLab.ca.all.parsers.CLSHandler;
import oscar.util.ConversionUtils;

public class CLSResultsComplianceTest {

	static final String SAMPLE_LAB_WITH_ORC_SEGMENTS  = "MSH|^~\\&|LCS|LCA|LIS|TEST9999|199807311532||ORU^R01|3629|P|2.3\r" + 
			"PID|2|2161348462|20809880170|1614614|20809880170^TESTPAT||19760924|M|||^^^^00000-0000|||||||86427531^^^03|SSN# HERE\r" + 
			"ORC|NW|8642753100012^LIS|20809880170^LCS||||||19980727000000|||HAVILAND\r" + 
			"OBR|1|8642753100012^LIS|20809880170^LCS|008342^UPPER RESPIRATORY CULTURE^L|||19980727175800||||||SS#634748641 CH14885 SRC:THROA SRC:PENI|19980727000000||||||20809880170||19980730041800||BN|F\r" + 
			"OBX|1|ST|008342^UPPER RESPIRATORY CULTURE^L||FINALREPORT|||||N|F||| 19980729160500|BN\r" + 
			"ORC|NW|8642753100012^LIS|20809880170^LCS||||||19980727000000|||HAVILAND\r" + 
			"OBR|2|8642753100012^LIS|20809880170^LCS|997602^.^L|||19980727175800||||G|||19980727000000||||||20809880170||19980730041800|||F|997602|||008342\r" + 
			"OBX|2|CE|997231^RESULT 1^L||M415|||||N|F|||19980729160500|BN\r" + 
			"NTE|1|L|MORAXELLA (BRANHAMELLA) CATARRHALIS\r" + 
			"NTE|2|L| HEAVY GROWTH\r" + 
			"NTE|3|L| BETA LACTAMASE POSITIVE\r" + 
			"OBX|3|CE|997232^RESULT 2^L||MR105|||||N|F|||19980729160500|BN\r" + 
			"NTE|1|L|ROUTINE RESPIRATORY FLORA";
	
	@Test
	public void testResultElementsForLab01() throws Exception {
		CLSHandler handler = new CLSHandler();
		handler.init(TestLabs.LAB01);
		
		assertEquals(1, handler.getOBRCount());
		assertEquals(5, handler.getOBXCount(0));

		assertEquals("URINE MICROSCOPIC", handler.getOBRName(0)); // Order Name
		
		// Test Name
		assertEquals("WBC", handler.getOBXName(0, 0));
		assertEquals("RBC", handler.getOBXName(0, 1));
		assertEquals("EPITHELIAL CELLS", handler.getOBXName(0, 2));
		assertEquals("URINE BACTERIA", handler.getOBXName(0, 3));
		assertEquals("HYALINE CASTS", handler.getOBXName(0, 4));
		
		// Test Result
		assertEquals("6-10", handler.getOBXResult(0, 0));
		assertEquals("6-10", handler.getOBXResult(0, 1));
		assertEquals("Few", handler.getOBXResult(0, 2));
		assertEquals("Few", handler.getOBXResult(0, 3));
		assertEquals("0-4", handler.getOBXResult(0, 4));
		
		// Units of Measure
		assertEquals("/HPF", handler.getOBXUnits(0, 0));
		assertEquals("/HPF", handler.getOBXUnits(0, 1));
		assertEquals("/HPF", handler.getOBXUnits(0, 2));
		assertEquals("/HPF", handler.getOBXUnits(0, 3));
		assertEquals("/LPF", handler.getOBXUnits(0, 4));
		
		// Reference Range
		assertEquals("0-5", handler.getOBXReferenceRange(0, 0));
		assertEquals("0-5", handler.getOBXReferenceRange(0, 1));
		assertEquals("", handler.getOBXReferenceRange(0, 2));
		assertEquals("", handler.getOBXReferenceRange(0, 3));
		assertEquals("", handler.getOBXReferenceRange(0, 4));
		
		// Abnormal Flag
		assertEquals("A", handler.getOBXAbnormalFlag(0, 0));
		assertEquals("A", handler.getOBXAbnormalFlag(0, 1));
		assertEquals("", handler.getOBXAbnormalFlag(0, 2));
		assertEquals("", handler.getOBXAbnormalFlag(0, 3));
		assertEquals("", handler.getOBXAbnormalFlag(0, 4));
		
		// Corrected Flag
		assertEquals("Final", handler.getOBXResultStatus(0, 0));
		assertEquals("Final", handler.getOBXResultStatus(0, 1));
		assertEquals("Final", handler.getOBXResultStatus(0, 2));
		assertEquals("Final", handler.getOBXResultStatus(0, 3));
		assertEquals("Final", handler.getOBXResultStatus(0, 4));
		
		// Result Comments
		assertEquals(0, handler.getOBXCommentCount(0, 0));
		assertEquals(0, handler.getOBXCommentCount(0, 1));
		assertEquals(0, handler.getOBXCommentCount(0, 2));
		assertEquals(0, handler.getOBXCommentCount(0, 3));
		assertEquals(0, handler.getOBXCommentCount(0, 4));
		
		// Order Comments
		assertEquals(0, handler.getOBRCommentCount(0));
		
		// Collected Date and Time
		assertEquals("20101203122200", handler.getOBRDateTime(0));
		assertEquals(ConversionUtils.fromDateString("20101203122200", "yyyyMMddHHmmss"), handler.getOBRDateTimeAsDate(0));
	}
	
	@Test
	public void testResultElementsForLab02() throws Exception {
		CLSHandler handler = new CLSHandler();
		handler.init(TestLabs.LAB02);
				
		assertEquals(1, handler.getOBRCount());
		assertEquals(1, handler.getOBXCount(0));

		assertEquals("URINALYSIS", handler.getOBRName(0)); // Order Name
		
		// Test Name
		assertEquals("Urinalysis Comment", handler.getOBXName(0, 0));
		
		// Test Result
		assertEquals("See Note", handler.getOBXResult(0, 0));
				
		// Units of Measure
		assertEquals("", handler.getOBXUnits(0, 0));
		
		// Reference Range
		assertEquals("", handler.getOBXReferenceRange(0, 0));
		
		// Abnormal Flag
		assertEquals("", handler.getOBXAbnormalFlag(0, 0));
		
		// Corrected Flag
		assertEquals("Final", handler.getOBXResultStatus(0, 0));
		
		// Result Comments
		assertEquals(2, handler.getOBXCommentCount(0, 0));
		assertEquals("Microscopic examination not performed due to negative Blood, Nitrite,", handler.getOBXComment(0, 0, 0));
		assertEquals("Leukocyte Esterase and Protein.", handler.getOBXComment(0, 0, 1));
		
		// Order Comments
		assertEquals(0, handler.getOBRCommentCount(0));
		
		// Collected Date and Time
		assertEquals("20101203122500", handler.getOBRDateTime(0));
		assertEquals(ConversionUtils.fromDateString("20101203122500", "yyyyMMddHHmmss"), handler.getOBRDateTimeAsDate(0));
	}

	@Test
	public void testFillerOrderIdCompliance() throws Exception {
		CLSHandler handler = new CLSHandler();
		handler.init(SAMPLE_LAB_WITH_ORC_SEGMENTS); 
		assertEquals("20809880170", handler.getFillerOrderNumber());
		
		/*
		 * Requirement:
		 * For all results on the Filler Order Id, only the latest version of the result should print. e.g. 
		 * If a preliminary Micro result is sent, then followed by the final result, only the final result should print.
		 * 
		 * Comment:
		 * Based on conversation with Jay only the latest result is shown by the system
		 */
	}
	

	@Test
	public void testCareSetsCompliance() throws Exception {
		CLSHandler handler = new CLSHandler();
		
		// Occult Blood
		handler.init(TestLabs.LAB29); 
		assertEquals("10-344-300006", handler.getAccessionNum());
		
		handler.init(TestLabs.LAB30); 
		assertEquals("10-344-300006", handler.getAccessionNum());
		
		handler.init(TestLabs.LAB31); 
		assertEquals("10-344-300006", handler.getAccessionNum());

		// Glucose Tolerance
		handler.init(TestLabs.LAB55); 
		assertEquals("11-012-300002", handler.getAccessionNum());
				
		handler.init(TestLabs.LAB57); 
		assertEquals("11-012-300004", handler.getAccessionNum());
		
		/*
		 * Care sets. The sequence of the results on the clinic charts do not need to be the same 
		 * as the results appear on the CLS printed report or as sent in the HL7 messages.
		 * 
		 * Cumulative results are combined. In the past the eLab team has accepted the following variations on the 
		 * cumulative results. Going forward the eLab team will advise the clinic and vendor that these results have
		 * been accepted but are not as expected. It will be up to the clinic to encourage the vendor to make
		 * changes so that the results are presented are equivalent to the CLS charts.
		 * 
		 * Occult Blood: Accession: 10-344-300006 - The Occult Blood results are repeated instead of being combined.
		 * Glucose Tolerance Preg: Accessions: 11-012-300002, 11-012-300004, 11-012-300004 - The Glucose Tolerance Pregnancy 
		 * Results are repeated instead of being combined.
		 * 
		 * Note: Glucose Tolerance Pregnancy - The time collected is not clinically relevant. It is acceptable if the 
		 * Collect Time shown is only for one result. e.g. first or last result. The Accession is not a required element; 
		 * having only one Accession is acceptable.
		 */
		
	}
	
	@Test
	public void testHeadersParsing() throws Exception {
		CLSHandler handler = new CLSHandler();
		handler.init(TestLabs.LAB01);
		assertTrue(handler.getHeaders().contains("URINE MICROSCOPIC"));		
	}
}
