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
/**
 * @author Shazib
 */

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
import org.oscarehr.common.model.OcanConnexOption;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.MiscUtils;

public class OcanConnexOptionDaoTest extends DaoTestFixtures {

	protected OcanConnexOptionDao dao = SpringUtils.getBean(OcanConnexOptionDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("OcanConnexOption");
	}

	@Test
	public void testCreate() throws Exception {
		OcanConnexOption entity = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	public void testFindByLHINCode() throws Exception {
		
		String orgLHINCode1 = "100";
		String orgLHINCode2 = "200";
		
		String orgName1 = "alpha";
		String orgName2 = "bravo";
		String orgName3 = "charlie";
		
		OcanConnexOption ocanConnexOption1 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption1);
		ocanConnexOption1.setLHINCode(orgLHINCode1);
		ocanConnexOption1.setOrgName(orgName1);
		dao.persist(ocanConnexOption1);
		
		OcanConnexOption ocanConnexOption2 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption2);
		ocanConnexOption2.setLHINCode(orgLHINCode2);
		ocanConnexOption2.setOrgName(orgName2);
		dao.persist(ocanConnexOption2);
		
		OcanConnexOption ocanConnexOption3 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption3);
		ocanConnexOption3.setLHINCode(orgLHINCode1);
		ocanConnexOption3.setOrgName(orgName3);
		dao.persist(ocanConnexOption3);
		
		List<OcanConnexOption> expectedResult = new ArrayList<OcanConnexOption>(Arrays.asList(ocanConnexOption1, ocanConnexOption3));
		List<OcanConnexOption> result = dao.findByLHINCode(orgLHINCode1);

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
	public void testFindByLHINCodeOrgName() throws Exception {
		
		String orgLHINCode1 = "100";
		String orgLHINCode2 = "200";
		
		String orgName1 = "alpha";
		String orgName2 = "bravo";
		
		String programName1 = "Program1";
		String programName2 = "Program2";
		String programName3 = "Program3";
		
		OcanConnexOption ocanConnexOption1 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption1);
		ocanConnexOption1.setLHINCode(orgLHINCode1);
		ocanConnexOption1.setOrgName(orgName1);
		ocanConnexOption1.setProgramName(programName3);
		dao.persist(ocanConnexOption1);
		
		OcanConnexOption ocanConnexOption2 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption2);
		ocanConnexOption2.setLHINCode(orgLHINCode2);
		ocanConnexOption2.setOrgName(orgName2);
		ocanConnexOption2.setProgramName(programName2);
		dao.persist(ocanConnexOption2);
		
		OcanConnexOption ocanConnexOption3 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption3);
		ocanConnexOption3.setLHINCode(orgLHINCode1);
		ocanConnexOption3.setOrgName(orgName1);
		ocanConnexOption3.setProgramName(programName1);
		dao.persist(ocanConnexOption3);
		
		List<OcanConnexOption> expectedResult = new ArrayList<OcanConnexOption>(Arrays.asList(ocanConnexOption3, ocanConnexOption1));
		List<OcanConnexOption> result = dao.findByLHINCodeOrgName(orgLHINCode1, orgName1);

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
	public void testFindByLHINCodeOrgNameProgramName() throws Exception {
		
		String orgLHINCode1 = "100";
		String orgLHINCode2 = "200";
		
		String orgName1 = "alpha";
		String orgName2 = "bravo";
		
		String programName1 = "Program1";
		String programName2 = "Program2";
				
		OcanConnexOption ocanConnexOption1 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption1);
		ocanConnexOption1.setLHINCode(orgLHINCode1);
		ocanConnexOption1.setOrgName(orgName1);
		ocanConnexOption1.setProgramName(programName1);
		dao.persist(ocanConnexOption1);
		
		OcanConnexOption ocanConnexOption2 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption2);
		ocanConnexOption2.setLHINCode(orgLHINCode2);
		ocanConnexOption2.setOrgName(orgName2);
		ocanConnexOption2.setProgramName(programName2);
		dao.persist(ocanConnexOption2);
		
		OcanConnexOption ocanConnexOption3 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption3);
		ocanConnexOption3.setLHINCode(orgLHINCode1);
		ocanConnexOption3.setOrgName(orgName1);
		ocanConnexOption3.setProgramName(programName1);
		dao.persist(ocanConnexOption3);
		
		List<OcanConnexOption> expectedResult = new ArrayList<OcanConnexOption>(Arrays.asList(ocanConnexOption1, ocanConnexOption3));
		List<OcanConnexOption> result = dao.findByLHINCodeOrgNameProgramName(orgLHINCode1, orgName1, programName1);

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
	public void testFindByID() throws Exception {
		
		int id = 3;
		OcanConnexOption ocanConnexOption1 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption1);
		dao.persist(ocanConnexOption1);
		
		OcanConnexOption ocanConnexOption2 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption2);
		dao.persist(ocanConnexOption2);
		
		OcanConnexOption ocanConnexOption3 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption3);
		dao.persist(ocanConnexOption3);
		
		OcanConnexOption ocanConnexOption4 = new OcanConnexOption();
		EntityDataGenerator.generateTestDataForModelClass(ocanConnexOption4);
		dao.persist(ocanConnexOption4);
		
		OcanConnexOption expectedResult = ocanConnexOption3;
		OcanConnexOption result = dao.findByID(id);
		
		assertEquals(expectedResult, result);
	}
}
