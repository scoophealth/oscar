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
import org.oscarehr.common.model.CtlBillingType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CtlBillingTypeDaoTest extends DaoTestFixtures {

	protected CtlBillingTypeDao dao = SpringUtils.getBean(CtlBillingTypeDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_billingtype");
	}

	@Test
	public void testCreate() throws Exception {
		CtlBillingType entity = new CtlBillingType();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId("test");
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByServiceType() throws Exception {
		
		String id1 = "alpha", id2 = "bravo";
		
		CtlBillingType cBT1 = new CtlBillingType();
		EntityDataGenerator.generateTestDataForModelClass(cBT1);
		cBT1.setId(id1);
		dao.persist(cBT1);
		
		CtlBillingType cBT2 = new CtlBillingType();
		EntityDataGenerator.generateTestDataForModelClass(cBT2);
		cBT2.setId(id2);
		dao.persist(cBT2);
		
		List<CtlBillingType> expectedResult = new ArrayList<CtlBillingType>(Arrays.asList(cBT1));
		List<CtlBillingType> result = dao.findByServiceType(id1);

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