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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

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
import org.oscarehr.common.model.SecPrivilege;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SecPrivilegeDaoTest extends DaoTestFixtures {
	
	protected SecPrivilegeDao dao = (SecPrivilegeDao)SpringUtils.getBean(SecPrivilegeDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "secPrivilege");
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		SecPrivilege secPrivilege1 = new SecPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secPrivilege1);
		secPrivilege1.setPrivilege("alpha");
		dao.persist(secPrivilege1);
				
		SecPrivilege secPrivilege2 = new SecPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secPrivilege2);
		secPrivilege2.setPrivilege("bravo");
		dao.persist(secPrivilege2);
		
		SecPrivilege secPrivilege3 = new SecPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secPrivilege3);
		secPrivilege3.setPrivilege("charlie");
		dao.persist(secPrivilege3);
		
		List<SecPrivilege> expectedResult = new ArrayList<SecPrivilege>(Arrays.asList(secPrivilege1, secPrivilege2, secPrivilege3));
		List<SecPrivilege> result = dao.findAll();

		Logger logger = MiscUtils.getLogger();
		for (SecPrivilege item : result) {
			logger.info("id:" + item.getId());
		}
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result:");
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