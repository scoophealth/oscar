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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicSets;
import org.oscarehr.util.SpringUtils;

public class DemographicSetsDaoTest extends DaoTestFixtures {
	protected DemographicSetsDao dao = SpringUtils.getBean(DemographicSetsDao.class);

	public DemographicSetsDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicSets");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindBySetName() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setArchive("0");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setArchive("0");
		dao.persist(entity);

		assertEquals(2,dao.findBySetName("a").size());

	}

	@Test
	public void testFindBySetNames() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setArchive("0");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("b");
		entity.setArchive("0");
		dao.persist(entity);

		List<String> names = new ArrayList<String>();
		names.add("a");
		names.add("b");

		assertEquals(2,dao.findBySetNames(names).size());

	}

	@Test
	public void testFindBySetNameAndEligibility() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setEligibility("0");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setEligibility("1");
		dao.persist(entity);

		assertEquals(1,dao.findBySetNameAndEligibility("a","0").size());
		assertEquals(1,dao.findBySetNameAndEligibility("a","1").size());

	}

	@Test
	public void testFindSetNamesByDemographicNo() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		entity.setDemographicNo(1);
		entity.setArchive("1");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("b");
		entity.setDemographicNo(1);
		entity.setArchive("1");
		dao.persist(entity);

		List<String> names = dao.findSetNamesByDemographicNo(1);
		assertEquals(2,names.size());
		assertTrue(names.contains("a"));
		assertTrue(names.contains("b"));
	}

	@Test
	public void testFindSetNames() throws Exception {
		DemographicSets entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("a");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("b");
		dao.persist(entity);

		entity = new DemographicSets();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("b");
		dao.persist(entity);

		assertEquals(2,dao.findSetNames().size());
	}
}
