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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ReportAgeSex;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportAgeSexDaoTest extends DaoTestFixtures {
	
	protected ReportAgeSexDao dao = (ReportAgeSexDao)SpringUtils.getBean(ReportAgeSexDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("reportagesex", "demographic", "lst_gender", "admission", "demographic_merged", "program", 
				"health_safety", "provider", "providersite", "site", "program_team","log", "Facility");
	}

	@Test 
	public void testFindBeforeReportDate() throws Exception {
		
		Date date1 = new Date(dfm.parse("20110101").getTime());
		Date date2 = new Date(dfm.parse("20100101").getTime());
		
		ReportAgeSex reportAgeSex1 = new ReportAgeSex();
		EntityDataGenerator.generateTestDataForModelClass(reportAgeSex1);
		reportAgeSex1.setReportDate(date1);
		dao.persist(reportAgeSex1);
		
		ReportAgeSex reportAgeSex2 = new ReportAgeSex();
		EntityDataGenerator.generateTestDataForModelClass(reportAgeSex2);
		reportAgeSex2.setReportDate(date2);
		dao.persist(reportAgeSex2);
		
		ReportAgeSex reportAgeSex3 = new ReportAgeSex();
		EntityDataGenerator.generateTestDataForModelClass(reportAgeSex3);
		reportAgeSex3.setReportDate(date1);
		dao.persist(reportAgeSex3);
		
		List<ReportAgeSex> expectedResult = new ArrayList<ReportAgeSex>(Arrays.asList(reportAgeSex1, reportAgeSex3));		
		List<ReportAgeSex> result = dao.findBeforeReportDate(date1);
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testDeleteAllByDate() throws Exception {
		Date date1 = new Date(dfm.parse("20110101").getTime());
		
		ReportAgeSex reportAgeSex1 = new ReportAgeSex();
		EntityDataGenerator.generateTestDataForModelClass(reportAgeSex1);
		reportAgeSex1.setReportDate(date1);
		dao.persist(reportAgeSex1);
		
		List<ReportAgeSex> expectedResult = new ArrayList<ReportAgeSex>(Arrays.asList(reportAgeSex1));		
		List<ReportAgeSex> result = dao.findBeforeReportDate(date1);
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match Expected: "+expectedResult.size() + " Result: "+ result.size());
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		
		dao.deleteAllByDate(date1);
		
		List<ReportAgeSex> newExpectedResult = new ArrayList<ReportAgeSex>();		
		List<ReportAgeSex> newResult = dao.findBeforeReportDate(date1);
		
		if (newResult.size() != newExpectedResult.size()) {
			logger.warn("Array sizes do not match. Expected: "+newExpectedResult.size() + " Result: "+ newResult.size());
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < newExpectedResult.size(); i++) {
			if (!newExpectedResult.get(i).equals(newResult.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}
}