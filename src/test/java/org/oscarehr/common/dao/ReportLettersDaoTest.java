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

import static org.junit.Assert.assertNotNull;
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
import org.oscarehr.common.model.ReportLetters;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportLettersDaoTest extends DaoTestFixtures {

	protected ReportLettersDao dao = SpringUtils.getBean(ReportLettersDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("report_letters");
	}

	@Test
	public void testCreate() throws Exception {
		ReportLetters entity = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindCurrent() throws Exception {
		
		String archive0 = "0", archive1 = "1";
		String reportName1 = "alpha", reportName2 = "bravo", reportName3 = "charlie", reportName4 = "delta", reportName5 = "epislon";
		
		Date date1 = new Date(dfm.parse("20020101").getTime());
		Date date2 = new Date(dfm.parse("20040101").getTime());
		Date date3 = new Date(dfm.parse("20060101").getTime());
		Date date4 = new Date(dfm.parse("20080101").getTime());
		Date date5 = new Date(dfm.parse("20110101").getTime());
		
		ReportLetters reportLetters1 = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(reportLetters1);
		reportLetters1.setArchive(archive0);
		reportLetters1.setDateTime(date1);
		reportLetters1.setReportName(reportName1);
		dao.persist(reportLetters1);
		
		ReportLetters reportLetters2 = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(reportLetters2);
		reportLetters2.setArchive(archive1);
		reportLetters2.setDateTime(date2);
		reportLetters2.setReportName(reportName2);
		dao.persist(reportLetters2);
		
		ReportLetters reportLetters3 = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(reportLetters3);
		reportLetters3.setArchive(archive1);
		reportLetters3.setDateTime(date3);
		reportLetters3.setReportName(reportName3);
		dao.persist(reportLetters3);
		
		ReportLetters reportLetters4 = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(reportLetters4);
		reportLetters4.setArchive(archive0);
		reportLetters4.setDateTime(date4);
		reportLetters4.setReportName(reportName4);
		dao.persist(reportLetters4);
		
		ReportLetters reportLetters5 = new ReportLetters();
		EntityDataGenerator.generateTestDataForModelClass(reportLetters5);
		reportLetters5.setArchive(archive0);
		reportLetters5.setDateTime(date5);
		reportLetters5.setReportName(reportName5);
		dao.persist(reportLetters5);
		
		List<ReportLetters> expectedResult = new ArrayList<ReportLetters>(Arrays.asList(reportLetters1, reportLetters4, reportLetters5));
		List<ReportLetters> result = dao.findCurrent();

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
}