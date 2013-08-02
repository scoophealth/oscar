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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ReportConfig;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportConfigDaoTest extends DaoTestFixtures {

	protected ReportConfigDao dao = SpringUtils.getBean(ReportConfigDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "reportConfig");
	}

	@Test
	public void testCreate() throws Exception {
		ReportConfig entity = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByReportIdAndNameAndCaptionAndTableNameAndSave() throws Exception {
		
		int reportId1 = 101, reportId2 = 202;
		String name1 = "alpha", name2 = "bravo";
		String caption1 = "charlie", caption2 = "delta";
		String tableName1 = "table1", tableName2 = "table2";
		String save1 = "sigma", save2 = "omega";
		
		ReportConfig reportConfig1 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig1);
		reportConfig1.setReportId(reportId1);
		reportConfig1.setName(name2);
		reportConfig1.setCaption(caption1);
		reportConfig1.setTableName(tableName1);
		reportConfig1.setSave(save1);
		dao.persist(reportConfig1);
		
		ReportConfig reportConfig2 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig2);
		reportConfig2.setReportId(reportId1);
		reportConfig2.setName(name1);
		reportConfig2.setCaption(caption2);
		reportConfig2.setTableName(tableName1);
		reportConfig2.setSave(save1);
		dao.persist(reportConfig2);
		
		ReportConfig reportConfig3 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig3);
		reportConfig3.setReportId(reportId1);
		reportConfig3.setName(name1);
		reportConfig3.setCaption(caption1);
		reportConfig3.setTableName(tableName2);
		reportConfig3.setSave(save1);
		dao.persist(reportConfig3);
		
		ReportConfig reportConfig4 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig4);
		reportConfig4.setReportId(reportId1);
		reportConfig4.setName(name1);
		reportConfig4.setCaption(caption1);
		reportConfig4.setTableName(tableName1);
		reportConfig4.setSave(save1);
		dao.persist(reportConfig4);
		
		ReportConfig reportConfig5 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig5);
		reportConfig5.setReportId(reportId1);
		reportConfig5.setName(name1);
		reportConfig5.setCaption(caption1);
		reportConfig5.setTableName(tableName1);
		reportConfig5.setSave(save2);
		dao.persist(reportConfig5);
		
		ReportConfig reportConfig6 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig6);
		reportConfig6.setReportId(reportId1);
		reportConfig6.setName(name1);
		reportConfig6.setCaption(caption1);
		reportConfig6.setTableName(tableName1);
		reportConfig6.setSave(save1);
		dao.persist(reportConfig6);
		
		ReportConfig reportConfig7 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig7);
		reportConfig7.setReportId(reportId2);
		reportConfig7.setName(name1);
		reportConfig7.setCaption(caption1);
		reportConfig7.setTableName(tableName1);
		reportConfig7.setSave(save1);
		dao.persist(reportConfig7);
		
		ReportConfig reportConfig8 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig8);
		reportConfig8.setReportId(reportId1);
		reportConfig8.setName(name1);
		reportConfig8.setCaption(caption1);
		reportConfig8.setTableName(tableName1);
		reportConfig8.setSave(save1);
		dao.persist(reportConfig8);
		
		List<ReportConfig> expectedResult = new ArrayList<ReportConfig>(Arrays.asList(reportConfig4, reportConfig6, reportConfig8));
		List<ReportConfig> result = dao.findByReportIdAndNameAndCaptionAndTableNameAndSave(reportId1, name1, caption1, tableName1, save1);

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
	public void testFindByReportIdAndSaveAndGtOrderNo() throws Exception {
		
		int reportId1 = 101, reportId2 = 202;
		int orderNo1 = 111, orderNo2 = 222;
		String save1 = "alpha", save2 = "bravo";
		
		ReportConfig reportConfig1 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig1);
		reportConfig1.setReportId(reportId2);
		reportConfig1.setSave(save1);
		reportConfig1.setOrderNo(orderNo1);
		dao.persist(reportConfig1);
		
		ReportConfig reportConfig2 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig2);
		reportConfig2.setReportId(reportId1);
		reportConfig2.setSave(save1);
		reportConfig2.setOrderNo(orderNo1);
		dao.persist(reportConfig2);
		
		ReportConfig reportConfig3 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig3);
		reportConfig3.setReportId(reportId1);
		reportConfig3.setSave(save2);
		reportConfig3.setOrderNo(orderNo1);
		dao.persist(reportConfig3);
		
		ReportConfig reportConfig4 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig4);
		reportConfig4.setReportId(reportId1);
		reportConfig4.setSave(save1);
		reportConfig4.setOrderNo(orderNo2);
		dao.persist(reportConfig4);
		
		ReportConfig reportConfig5 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig5);
		reportConfig5.setReportId(reportId1);
		reportConfig5.setSave(save1);
		reportConfig5.setOrderNo(orderNo1);
		dao.persist(reportConfig5);
		
		ReportConfig reportConfig6 = new ReportConfig();
		EntityDataGenerator.generateTestDataForModelClass(reportConfig6);
		reportConfig6.setReportId(reportId1);
		reportConfig6.setSave(save1);
		reportConfig6.setOrderNo(orderNo1);
		dao.persist(reportConfig6);
		
		List<ReportConfig> expectedResult = new ArrayList<ReportConfig>(Arrays.asList(reportConfig4, reportConfig2, reportConfig5, reportConfig6));
		List<ReportConfig> result = dao.findByReportIdAndSaveAndGtOrderNo(reportId1, save1, orderNo1);

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