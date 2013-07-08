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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ScheduleTemplateCodeDaoTest extends DaoTestFixtures {

	protected ScheduleTemplateCodeDao dao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);

	public ScheduleTemplateCodeDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "scheduletemplatecode");
	}

	@Test
    public void testCreate() throws Exception {
		ScheduleTemplateCode entity = new ScheduleTemplateCode();
        EntityDataGenerator.generateTestDataForModelClass(entity);
        entity.setCode('A');
        dao.persist(entity);
        assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		ScheduleTemplateCode scheduleTempCode1 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode1);
		dao.persist(scheduleTempCode1);
		
		ScheduleTemplateCode scheduleTempCode2 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode2);
		dao.persist(scheduleTempCode2);
		
		ScheduleTemplateCode scheduleTempCode3 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode3);
		dao.persist(scheduleTempCode3);
		
		List<ScheduleTemplateCode> expectedResult = new ArrayList<ScheduleTemplateCode>(Arrays.asList(scheduleTempCode1, scheduleTempCode2, scheduleTempCode3));
		List<ScheduleTemplateCode> result = dao.findAll();

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
	public void testGetByCode() throws Exception {
		
		char code1 = 's';
		char code2 = 'a';
		char code3 = 'b';
		
		ScheduleTemplateCode scheduleTempCode1 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode1);
		scheduleTempCode1.setCode(code1);
		dao.persist(scheduleTempCode1);
		
		ScheduleTemplateCode scheduleTempCode2 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode2);
		scheduleTempCode2.setCode(code2);
		dao.persist(scheduleTempCode2);
		
		ScheduleTemplateCode scheduleTempCode3 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode3);
		scheduleTempCode3.setCode(code3);
		dao.persist(scheduleTempCode3);
		
		ScheduleTemplateCode expectedResult = scheduleTempCode2;
		ScheduleTemplateCode result = dao.getByCode(code2);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindByCode() throws Exception {
		
		char code1 = 'a', code2 = 'b', code3 = 'c';
		String code = "b";

		ScheduleTemplateCode scheduleTempCode1 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode1);
		scheduleTempCode1.setCode(code1);
		dao.persist(scheduleTempCode1);
		
		ScheduleTemplateCode scheduleTempCode2 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode2);
		scheduleTempCode2.setCode(code2);
		dao.persist(scheduleTempCode2);
		
		ScheduleTemplateCode scheduleTempCode3 = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(scheduleTempCode3);
		scheduleTempCode3.setCode(code3);
		dao.persist(scheduleTempCode3);
		
		ScheduleTemplateCode expectedResult = scheduleTempCode2;
		ScheduleTemplateCode result = dao.findByCode(code);
		
		assertEquals(expectedResult, result);	
	}	
}