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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BedType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BedTypeDaoTest extends DaoTestFixtures {

	protected BedTypeDao dao = SpringUtils.getBean(BedTypeDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "bed_type");
	}

	@Test
	public void testCreate() throws Exception {
		BedType entity = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testBedTypeExists() throws Exception {
				
		BedType bedType1 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType1);
		dao.saveEntity(bedType1);
		
		BedType bedType2 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType2);
		dao.saveEntity(bedType2);
		
		boolean result = dao.bedTypeExists(bedType1.getId());
		boolean expectedResult = true;
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetBedTypes() throws Exception {
		
		BedType bedType1 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType1);
		dao.saveEntity(bedType1);
		
		BedType bedType2 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType2);
		dao.saveEntity(bedType2);
		
		BedType bedType3 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType3);
		dao.saveEntity(bedType3);
		
		BedType bedType4 = new BedType();
		EntityDataGenerator.generateTestDataForModelClass(bedType4);
		dao.saveEntity(bedType4);
		
		BedType expectedResult[] = {bedType1, bedType2, bedType3, bedType4};
		BedType result[] = dao.getBedTypes();
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.length != expectedResult.length) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.length; i++) {
			if (!expectedResult[i].equals(result[i])){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);		
	}
}