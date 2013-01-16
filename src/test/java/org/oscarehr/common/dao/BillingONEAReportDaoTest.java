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
package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.on.data.BillingProviderData;

public class BillingONEAReportDaoTest extends DaoTestFixtures {

	protected BillingONEAReportDao dao = (BillingONEAReportDao)SpringUtils.getBean(BillingONEAReportDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_eareport");
	}
	
	@Test
	/**
	 *  Ensures GetBillingErrorsList method returns accurate data.
	 *  Ensures GetBillingErrorsList method returns all errors in the list.
	 */
	public void testGetBillingErrorsListData() throws Exception {
		boolean dataIsAccurate = false;
		BillingONEAReport eaRpt = createReport(1);
		eaRpt.setClaimError("error01");
		eaRpt.setCodeError("error02");
		dao.persist(eaRpt);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt.getBillingNo());
		if (eaReportErrors.get(0).equals("error01") 
				&& eaReportErrors.get(1).equals("error02"))
		{
			dataIsAccurate = true;
		}
		
		assertTrue(dataIsAccurate);
	}
	
	
//	@Ignore @Test (expected = IllegalArgumentException.class)
//	/**
//	 *  Ensure invalid billing numbers are not acceptable.
//	 *  To be implemented as validation is implemented on dao method parameters.
//	 */
//	public void testGetBillingErrorsListInvalidBillNo() throws Exception {
//		// Create billing report using a negative billing number.
//		BillingONEAReport eaRpt = createReport(-1);
//		eaRpt.setClaimError("error01");
//		eaRpt.setCodeError("error02");
//		dao.persist(eaRpt);
//		
//		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt.getBillingNo());
//	}
	
	
	
	@Test
	/**
	 *  Ensure that all errors are trimmed before 
	 *  they are added to the errors list.
	 */
	public void testGetBillingErrorsListTrim() throws Exception {
		BillingONEAReport eaRpt = createReport(1);
		eaRpt.setClaimError("   ");
		eaRpt.setCodeError("   error02    ");
		dao.persist(eaRpt);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt.getBillingNo());
		List<String> expectedList = new ArrayList<String>(Arrays.asList("error02"));
		
		// This one comparison validates that errors are trimmed,
		// and that blank errors are not added to the list of errors.
		assertEquals(eaReportErrors, expectedList);
	}
	
	
	@Test
	/**
	 * Ensures errors from reports with the same billing number
	 * are ordered by process date descending.
	 */
	public void testGetBillingErrorsListOrder() throws Exception {
		DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
		
		// Build 3 new error reports with different process dates.
		// Add a claims error and code error to each.
		
		// oldest report; errors should be at the bottom of the list
		BillingONEAReport eaRpt1 = createReport(1);
		Date Date1 = new Date(dfm.parse("20090101").getTime());
		eaRpt1.setProcessDate(Date1);
		eaRpt1.setClaimError("error01");
		eaRpt1.setCodeError("error01");
		
		// central report; errors should be in the middle of the list
		BillingONEAReport eaRpt2 = createReport(1);
		Date Date2 = new Date(dfm.parse("20100101").getTime());
		eaRpt2.setProcessDate(Date2);
		eaRpt2.setClaimError("error02");
		eaRpt2.setCodeError("error02");
		
		// newest report; errors, should be at top of list
		BillingONEAReport eaRpt3 = createReport(1);
		Date Date3 = new Date(dfm.parse("20110101").getTime());
		eaRpt3.setProcessDate(Date3);
		eaRpt3.setClaimError("error03");
		eaRpt3.setCodeError("error03");
		
		dao.persist(eaRpt1);
		dao.persist(eaRpt2);
		dao.persist(eaRpt3);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt1.getBillingNo());
		List<String> expectedResult = new ArrayList<String>(Arrays.asList(
				"error03",
				"error03",
				"error02",
				"error02",
				"error01",
				"error01"));
		
		if (eaReportErrors.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		for (int i =0; i < eaReportErrors.size(); i++) {
			if (!eaReportErrors.get(i).equals(expectedResult.get(i))) {
				fail("Items not ordered by process date descending.");
			}
		}
		
		assertTrue(true);
	}
	
	/**
	 * Helper method that creates a new BillingONEAReport and adds data. 
	 * @param billNumber (Integer)
	 * @return BillingONEAReport
	 * @throws Exception
	 */
	public BillingONEAReport createReport(int billNumber) throws Exception {
		BillingONEAReport report = new BillingONEAReport();
		EntityDataGenerator.generateTestDataForModelClass(report);
		report.setBillingNo(billNumber);
		return report;
	}
	
    @Test
    public void testFindByMagic() throws Exception {
	    assertNotNull(dao.findByMagic("OHIP", "BGNO", "SPEC CODE", new Date(), new Date(), "REPORT"));
	    
	    List<BillingProviderData> data = new ArrayList<BillingProviderData>();
	    BillingProviderData d = new BillingProviderData();
	    EntityDataGenerator.generateTestDataForModelClass(d);
	    data.add(d);
	    d = new BillingProviderData();
	    EntityDataGenerator.generateTestDataForModelClass(d);
	    data.add(d);
	    assertNotNull(dao.findByMagic(data, new Date(), new Date(), "REPORT"));
    }
}
