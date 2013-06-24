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
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class FacilityDaoTest extends DaoTestFixtures {

	protected FacilityDao dao = SpringUtils.getBean(FacilityDao.class);


	@Before
	public void before() throws Exception {
		this.beforeForInnoDB();
		SchemaUtils.restoreTable(true, "Facility");
	}

	@Test
	public void testCreate() throws Exception {
		Facility entity = new Facility();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		boolean isDisabled = true;
		
		String name1 = "alpha";
		String name2 = "bravo";
		String name3 = "charlie";
		String name4 = "delta";
		String name5 = "sigma";
		
		Facility fac1= new Facility();
		EntityDataGenerator.generateTestDataForModelClass(fac1);
		fac1.setDisabled(isDisabled);
		fac1.setName(name2);
		dao.persist(fac1);
		
		Facility fac2= new Facility();
		EntityDataGenerator.generateTestDataForModelClass(fac2);
		fac2.setDisabled(!isDisabled);
		fac2.setName(name4);
		dao.persist(fac2);
		
		Facility fac3= new Facility();
		EntityDataGenerator.generateTestDataForModelClass(fac3);
		fac3.setDisabled(isDisabled);
		fac3.setName(name3);
		dao.persist(fac3);
		
		Facility fac4= new Facility();
		EntityDataGenerator.generateTestDataForModelClass(fac4);
		fac4.setDisabled(isDisabled);
		fac4.setName(name1);
		dao.persist(fac4);
		
		Facility fac5= new Facility();
		EntityDataGenerator.generateTestDataForModelClass(fac5);
		fac5.setDisabled(!isDisabled);
		fac5.setName(name5);
		dao.persist(fac5);
		
		Logger logger = MiscUtils.getLogger();
		
		List<Facility> expectedResult = new ArrayList<Facility>(Arrays.asList(fac4, fac1, fac3));
		List<Facility> result = dao.findAll(!isDisabled);
	
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
