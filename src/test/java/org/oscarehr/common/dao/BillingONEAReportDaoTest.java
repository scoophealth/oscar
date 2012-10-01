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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.util.SpringUtils;

public class BillingONEAReportDaoTest extends DaoTestFixtures {

	private BillingONEAReportDao dao = (BillingONEAReportDao)SpringUtils.getBean(BillingONEAReportDao.class);
	
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
		BillingONEAReport eaRpt = createReport(1);
		eaRpt.setClaimError("11111111111111111111");
		eaRpt.setCodeError("22222222222222222222");
		dao.persist(eaRpt);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt.getBillingNo());
		assertEquals("11111111111111111111", eaReportErrors.get(0));
		assertEquals("22222222222222222222", eaReportErrors.get(1));
	}
	
	
	@Test
	/**
	 *  Tests limit of billing number ( currently set to int(6) )
	 */
	public void testGetBillingErrorsListBillingNumberHigh() throws Exception {
		BillingONEAReport eaRpt = createReport(1000000);
		eaRpt.setClaimError("11111111111111111111");
		eaRpt.setCodeError("22222222222222222222");
		dao.persist(eaRpt);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt.getBillingNo());
		assertEquals("11111111111111111111", eaReportErrors.get(0));
		assertEquals("22222222222222222222", eaReportErrors.get(1));
	}
	
	@Test
	/**
	 * Ensures errors from reports with the same billing number
	 * are ordered by process date descending.
	 */
	public void testGetBillingErrorsListOrder() throws Exception {
		DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
		
		// Build 3 new reports with different process dates.
		// Add a claims error and code error to each.
		BillingONEAReport eaRpt1 = createReport(1);
		Date Date1 = new Date(dfm.parse("20090101").getTime());
		eaRpt1.setProcessDate(Date1);
		eaRpt1.setClaimError("11111111111111111111");
		eaRpt1.setCodeError("11111111111111111111");
		
		BillingONEAReport eaRpt2 = createReport(1);
		Date Date2 = new Date(dfm.parse("20100101").getTime());
		eaRpt2.setProcessDate(Date2);
		eaRpt2.setClaimError("22222222222222222222");
		eaRpt2.setCodeError("22222222222222222222");
		
		BillingONEAReport eaRpt3 = createReport(1);
		Date Date3 = new Date(dfm.parse("20110101").getTime());
		eaRpt3.setProcessDate(Date3);
		eaRpt3.setClaimError("33333333333333333333");
		eaRpt3.setCodeError("33333333333333333333");
		
		dao.persist(eaRpt1);
		dao.persist(eaRpt2);
		dao.persist(eaRpt3);
		
		List<String> eaReportErrors = dao.getBillingErrorList(eaRpt1.getBillingNo());
		
		// Expected results are reverse because query performs "ORDER BY processDate desc"
		assertEquals("33333333333333333333", eaReportErrors.get(0));
		assertEquals("33333333333333333333", eaReportErrors.get(1));
		assertEquals("22222222222222222222", eaReportErrors.get(2));
		assertEquals("22222222222222222222", eaReportErrors.get(3));
		assertEquals("11111111111111111111", eaReportErrors.get(4));
		assertEquals("11111111111111111111", eaReportErrors.get(5));
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
}
