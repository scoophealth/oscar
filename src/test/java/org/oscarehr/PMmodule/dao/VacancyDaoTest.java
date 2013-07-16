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
package org.oscarehr.PMmodule.dao;

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
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class VacancyDaoTest extends DaoTestFixtures {

	public VacancyDao dao = SpringUtils.getBean(VacancyDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("vacancy");
	}

	@Test
	public void testCreate() throws Exception {
		Vacancy entity = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetVacanciesByWlProgramId() throws Exception {
		
		int wlProgramId1 = 101, wlProgramId2 = 202;
		String name = "alpha";
		
		Vacancy vacancy1 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy1);
		vacancy1.setName(name);
		vacancy1.setWlProgramId(wlProgramId1);
		dao.persist(vacancy1);
		
		Vacancy vacancy2 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy2);
		vacancy2.setWlProgramId(wlProgramId2);
		vacancy2.setName(name);
		dao.persist(vacancy2);
		
		Vacancy vacancy3 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy3);
		vacancy3.setWlProgramId(wlProgramId1);
		vacancy3.setName(name);
		dao.persist(vacancy3);
		
		Vacancy vacancy4 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy4);
		vacancy4.setWlProgramId(wlProgramId2);
		vacancy4.setName(name);
		dao.persist(vacancy4);
		
		Vacancy vacancy5 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy5);
		vacancy5.setWlProgramId(wlProgramId1);
		vacancy5.setName(name);
		dao.persist(vacancy5);
		
		List<Vacancy> expectedResult = new ArrayList<Vacancy>(Arrays.asList(vacancy1, vacancy3, vacancy5));
		List<Vacancy> result = dao.getVacanciesByWlProgramId(wlProgramId1);
		
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
	public void testGetVacanciesByName() throws Exception {
		
		String name1 = "alpha", name2 = "charlie";
		
		Vacancy vacancy1 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy1);
		vacancy1.setName(name1);
		dao.persist(vacancy1);
		
		Vacancy vacancy2 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy2);
		vacancy2.setName(name2);
		dao.persist(vacancy2);
		
		Vacancy vacancy3 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy3);
		vacancy3.setName(name1);
		dao.persist(vacancy3);
		
		Vacancy vacancy4 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy4);
		vacancy4.setName(name2);
		dao.persist(vacancy4);
		
		Vacancy vacancy5 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy5);
		vacancy5.setName(name1);
		dao.persist(vacancy5);
		
		List<Vacancy> expectedResult = new ArrayList<Vacancy>(Arrays.asList(vacancy1, vacancy3, vacancy5));
		List<Vacancy> result = dao.getVacanciesByName(name1);
		
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
	
	public void testGetVacanciesByWlProgramIdAndStatus() throws Exception {
		
		int wlProgramId1 = 101, wlProgramId2 = 202;
		String status1 = "delta", status2 = "omega";
		
		Vacancy vacancy1 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy1);
		vacancy1.setWlProgramId(wlProgramId1);
		vacancy1.setStatus(status1);
		dao.persist(vacancy1);
		
		Vacancy vacancy2 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy2);
		vacancy2.setWlProgramId(wlProgramId2);
		vacancy2.setStatus(status2);
		dao.persist(vacancy2);
		
		Vacancy vacancy3 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy3);
		vacancy3.setWlProgramId(wlProgramId1);
		vacancy3.setStatus(status1);
		dao.persist(vacancy3);
		
		Vacancy vacancy4 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy4);
		vacancy4.setWlProgramId(wlProgramId2);
		vacancy4.setStatus(status1);
		dao.persist(vacancy4);
		
		Vacancy vacancy5 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy5);
		vacancy5.setWlProgramId(wlProgramId1);
		vacancy5.setStatus(status1);
		dao.persist(vacancy5);
		
		List<Vacancy> expectedResult = new ArrayList<Vacancy>(Arrays.asList(vacancy1, vacancy3, vacancy5));
		List<Vacancy> result = dao.getVacanciesByWlProgramId(wlProgramId1);
		
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
	public void testGetVacancyById() throws Exception {
		
		Vacancy vacancy1 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy1);
		dao.saveEntity(vacancy1);
		
		Vacancy vacancy2 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy2);
		dao.saveEntity(vacancy2);
		
		Vacancy vacancy3 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy3);
		dao.saveEntity(vacancy3);
		
		Vacancy expcetedResult = vacancy2;
		Vacancy result = dao.getVacancyById(vacancy2.getId());
		
		assertEquals(expcetedResult, result);
	}
	
	@Test
	public void testFindCurrent() throws Exception {
		
		String status1 = "ACTIVE", status2 = "NOTACTIVE";
		
		Vacancy vacancy1 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy1);
		vacancy1.setStatus(status1);
		dao.persist(vacancy1);
		
		Vacancy vacancy2 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy2);
		vacancy2.setStatus(status2);
		dao.persist(vacancy2);
		
		Vacancy vacancy3 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy3);
		vacancy3.setStatus(status1);
		dao.persist(vacancy3);
		
		Vacancy vacancy4 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy4);
		vacancy2.setStatus(status2);
		dao.persist(vacancy4);
		
		Vacancy vacancy5 = new Vacancy();
		EntityDataGenerator.generateTestDataForModelClass(vacancy5);
		vacancy5.setStatus(status1);
		dao.persist(vacancy5);
		
		List<Vacancy> expectedResult = new ArrayList<Vacancy>(Arrays.asList(vacancy1, vacancy3, vacancy5));
		List<Vacancy> result = dao.findCurrent();
		
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