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
import org.oscarehr.common.model.ReportTemplates;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportTemplatesDaoTest extends DaoTestFixtures {

	protected ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("reportTemplates");
	}

	@Test
	public void testCreate() throws Exception {
		ReportTemplates entity = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		ReportTemplates reportTemp1 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp1);
		dao.persist(reportTemp1);
		
		ReportTemplates reportTemp2 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp2);
		dao.persist(reportTemp2);
		
		ReportTemplates reportTemp3 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp3);
		dao.persist(reportTemp3);
		
		ReportTemplates reportTemp4 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp4);
		dao.persist(reportTemp4);
		
		List<ReportTemplates> expectedResult = new ArrayList<ReportTemplates>(Arrays.asList(reportTemp1, reportTemp2, reportTemp3, reportTemp4));
		List<ReportTemplates> result = dao.findAll();

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
	public void testFindActive() throws Exception {
		
		int active1 = 1, active2 = 2;
		
		ReportTemplates reportTemp1 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp1);
		reportTemp1.setActive(active1);
		dao.persist(reportTemp1);
		
		ReportTemplates reportTemp2 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp2);
		reportTemp2.setActive(active2);
		dao.persist(reportTemp2);
		
		ReportTemplates reportTemp3 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp3);
		reportTemp3.setActive(active1);
		dao.persist(reportTemp3);
		
		ReportTemplates reportTemp4 = new ReportTemplates();
		EntityDataGenerator.generateTestDataForModelClass(reportTemp4);
		reportTemp4.setActive(active1);
		dao.persist(reportTemp4);
		
		List<ReportTemplates> expectedResult = new ArrayList<ReportTemplates>(Arrays.asList(reportTemp1, reportTemp3, reportTemp4));
		List<ReportTemplates> result = dao.findActive();

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
