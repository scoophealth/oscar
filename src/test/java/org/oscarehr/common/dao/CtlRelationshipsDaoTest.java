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
import org.oscarehr.common.model.CtlRelationships;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CtlRelationshipsDaoTest extends DaoTestFixtures {

	protected CtlRelationshipsDao dao = SpringUtils.getBean(CtlRelationshipsDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "CtlRelationships");
	}

	@Test
	public void testCreate()  {
		CtlRelationships entity = new CtlRelationships();
		entity.setValue("value");
		entity.setLabel("label");
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAllActive() throws Exception {
		
		boolean isActive = true;
		
		CtlRelationships ctlRelation1 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation1);
		ctlRelation1.setActive(isActive);
		dao.persist(ctlRelation1);
		
		CtlRelationships ctlRelation2 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation2);
		ctlRelation2.setActive(!isActive);
		dao.persist(ctlRelation2);
		
		CtlRelationships ctlRelation3 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation3);
		ctlRelation3.setActive(isActive);
		dao.persist(ctlRelation3);
		
		List<CtlRelationships> expectedResult = new ArrayList<CtlRelationships>(Arrays.asList(ctlRelation1, ctlRelation3));
		List<CtlRelationships> result = dao.findAllActive();

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
	public void testFindByValue() throws Exception {
		
		boolean isActive = true;
		
		String value1 = "alpha";
		String value2 = "bravo";
		
		CtlRelationships ctlRelation1 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation1);
		ctlRelation1.setActive(!isActive);
		ctlRelation1.setValue(value1);
		dao.persist(ctlRelation1);
		
		CtlRelationships ctlRelation2 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation2);
		ctlRelation2.setActive(isActive);
		ctlRelation2.setValue(value2);
		dao.persist(ctlRelation2);
		
		CtlRelationships ctlRelation3 = new CtlRelationships();
		EntityDataGenerator.generateTestDataForModelClass(ctlRelation3);
		ctlRelation3.setActive(isActive);
		ctlRelation3.setValue(value1);
		dao.persist(ctlRelation3);
		
		CtlRelationships expectedResult = ctlRelation3;
		CtlRelationships result = dao.findByValue(value1);
		
		assertEquals(expectedResult, result);				
	}
}