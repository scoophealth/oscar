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
import org.oscarehr.common.model.ReportTemp;
import org.oscarehr.common.model.ReportTempPK;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportTempDaoTest extends DaoTestFixtures {
	protected ReportTempDao dao = (ReportTempDao)SpringUtils.getBean(ReportTempDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("reporttemp");
	}

	@Test
	public void testFindAll() throws Exception {
		
		int demographicNo1 = 101;
		int demographicNo2 = 202;
		int demographicNo3 = 303;
		
		Date edb1 = new Date(dfm.parse("20110101").getTime());
		Date edb2 = new Date(dfm.parse("20080501").getTime());
		Date edb3 = new Date(dfm.parse("20090101").getTime());
		
		ReportTempPK reportTempPK1 = new ReportTempPK();
		reportTempPK1.setDemographicNo(demographicNo1);
		reportTempPK1.setEdb(edb1);
		
		ReportTempPK reportTempPK2 = new ReportTempPK();
		reportTempPK2.setDemographicNo(demographicNo2);
		reportTempPK2.setEdb(edb2);
		
		ReportTempPK reportTempPK3 = new ReportTempPK();
		reportTempPK3.setDemographicNo(demographicNo3);
		reportTempPK3.setEdb(edb3);
		
		ReportTemp reportTemp1 = new ReportTemp();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp1);
		reportTemp1.setId(reportTempPK1);
		dao.persist(reportTemp1);
		
		ReportTemp reportTemp2 = new ReportTemp();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp2);
		reportTemp2.setId(reportTempPK2);
		dao.persist(reportTemp2);
		
		ReportTemp reportTemp3 = new ReportTemp();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp3);
		reportTemp3.setId(reportTempPK3);
		dao.persist(reportTemp3);	
		
		List<ReportTemp> expectedResult = new ArrayList<ReportTemp>(Arrays.asList(reportTemp1, reportTemp2, reportTemp3));
		List<ReportTemp> result = dao.findAll();

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
