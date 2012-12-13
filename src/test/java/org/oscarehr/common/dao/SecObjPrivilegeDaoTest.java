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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.common.model.SecObjPrivilegePrimaryKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SecObjPrivilegeDaoTest extends DaoTestFixtures {

	protected SecObjPrivilegeDao dao = (SecObjPrivilegeDao) SpringUtils.getBean(SecObjPrivilegeDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("secObjPrivilege", "secUserRole");
	}

	@Test
	public void testFindByRoleUserGroupAndObjectName() throws Exception {

		String objectName1 = "alphaName1";
		String roleUserGroup1 = "sigmaGroup1";
		String objectName2 = "alphaName2";
		String roleUserGroup2 = "sigmaGroup2";

		SecObjPrivilege secObjPrivilege1 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege1);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey1 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey1.setObjectName(objectName1);
		secObjPrivilegePrimaryKey1.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege1.setId(secObjPrivilegePrimaryKey1);
		dao.persist(secObjPrivilege1);

		SecObjPrivilege secObjPrivilege2 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege2);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey2 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey2.setObjectName(objectName2);
		secObjPrivilegePrimaryKey2.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege2.setId(secObjPrivilegePrimaryKey2);
		dao.persist(secObjPrivilege2);

		SecObjPrivilege secObjPrivilege3 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege3);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey3 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey3.setObjectName(objectName1);
		secObjPrivilegePrimaryKey3.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege3.setId(secObjPrivilegePrimaryKey3);
		dao.persist(secObjPrivilege3);

		SecObjPrivilege secObjPrivilege4 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege4);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey4 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey4.setObjectName(objectName2);
		secObjPrivilegePrimaryKey4.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege4.setId(secObjPrivilegePrimaryKey4);
		dao.persist(secObjPrivilege4);

		List<SecObjPrivilege> expectedResult = new ArrayList<SecObjPrivilege>(Arrays.asList(secObjPrivilege1));
		List<SecObjPrivilege> result = dao.findByRoleUserGroupAndObjectName(roleUserGroup1, objectName1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByObjectNames() throws Exception {

		String objectName1 = "alphaName1";
		String objectName2 = "alphaName2";
		String objectName3 = "alphaName3";

		String roleUserGroup1 = "sigmaGroup1";
		String roleUserGroup2 = "sigmaGroup2";

		SecObjPrivilege secObjPrivilege1 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege1);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey1 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey1.setObjectName(objectName1);
		secObjPrivilegePrimaryKey1.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege1.setId(secObjPrivilegePrimaryKey1);
		dao.persist(secObjPrivilege1);

		SecObjPrivilege secObjPrivilege2 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege2);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey2 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey2.setObjectName(objectName2);
		secObjPrivilegePrimaryKey2.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege2.setId(secObjPrivilegePrimaryKey2);
		dao.persist(secObjPrivilege2);

		SecObjPrivilege secObjPrivilege3 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege3);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey3 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey3.setObjectName(objectName3);
		secObjPrivilegePrimaryKey3.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege3.setId(secObjPrivilegePrimaryKey3);
		dao.persist(secObjPrivilege3);

		Collection<String> objectNames = new ArrayList<String>(Arrays.asList(objectName1, objectName2));

		List<SecObjPrivilege> expectedResult = new ArrayList<SecObjPrivilege>(Arrays.asList(secObjPrivilege1, secObjPrivilege2));
		List<SecObjPrivilege> result = dao.findByObjectNames(objectNames);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByRoleUserGroup() throws Exception {

		String objectName1 = "alphaName1";
		String roleUserGroup1 = "sigmaGroup1";
		String objectName2 = "alphaName2";
		String roleUserGroup2 = "sigmaGroup2";

		SecObjPrivilege secObjPrivilege1 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege1);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey1 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey1.setObjectName(objectName1);
		secObjPrivilegePrimaryKey1.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege1.setId(secObjPrivilegePrimaryKey1);
		dao.persist(secObjPrivilege1);

		SecObjPrivilege secObjPrivilege2 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege2);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey2 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey2.setObjectName(objectName2);
		secObjPrivilegePrimaryKey2.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege2.setId(secObjPrivilegePrimaryKey2);
		dao.persist(secObjPrivilege2);

		SecObjPrivilege secObjPrivilege3 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege3);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey3 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey3.setObjectName(objectName1);
		secObjPrivilegePrimaryKey3.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege3.setId(secObjPrivilegePrimaryKey3);
		dao.persist(secObjPrivilege3);

		SecObjPrivilege secObjPrivilege4 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege4);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey4 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey4.setObjectName(objectName2);
		secObjPrivilegePrimaryKey4.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege4.setId(secObjPrivilegePrimaryKey4);
		dao.persist(secObjPrivilege4);

		List<SecObjPrivilege> expectedResult = new ArrayList<SecObjPrivilege>(Arrays.asList(secObjPrivilege1, secObjPrivilege4));
		List<SecObjPrivilege> result = dao.findByRoleUserGroup(roleUserGroup1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByObjectName() throws Exception {

		String objectName1 = "alphaName1";
		String roleUserGroup1 = "sigmaGroup1";
		String objectName2 = "alphaName2";
		String roleUserGroup2 = "sigmaGroup2";

		SecObjPrivilege secObjPrivilege1 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege1);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey1 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey1.setObjectName(objectName1);
		secObjPrivilegePrimaryKey1.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege1.setId(secObjPrivilegePrimaryKey1);
		dao.persist(secObjPrivilege1);

		SecObjPrivilege secObjPrivilege2 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege2);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey2 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey2.setObjectName(objectName2);
		secObjPrivilegePrimaryKey2.setRoleUserGroup(roleUserGroup1);
		secObjPrivilege2.setId(secObjPrivilegePrimaryKey2);
		dao.persist(secObjPrivilege2);

		SecObjPrivilege secObjPrivilege3 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege3);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey3 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey3.setObjectName(objectName1);
		secObjPrivilegePrimaryKey3.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege3.setId(secObjPrivilegePrimaryKey3);
		dao.persist(secObjPrivilege3);

		SecObjPrivilege secObjPrivilege4 = new SecObjPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(secObjPrivilege4);
		SecObjPrivilegePrimaryKey secObjPrivilegePrimaryKey4 = new SecObjPrivilegePrimaryKey();
		secObjPrivilegePrimaryKey4.setObjectName(objectName2);
		secObjPrivilegePrimaryKey4.setRoleUserGroup(roleUserGroup2);
		secObjPrivilege4.setId(secObjPrivilegePrimaryKey4);
		dao.persist(secObjPrivilege4);

		List<SecObjPrivilege> expectedResult = new ArrayList<SecObjPrivilege>(Arrays.asList(secObjPrivilege1, secObjPrivilege3));
		List<SecObjPrivilege> result = dao.findByObjectName(objectName1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testCountObjectsByName() {
		dao.countObjectsByName("OBJ NAME");
	}

	@Test
	public void testFindByFormNamePrivilegeAndProviderNo() {
		assertNotNull(dao.findByFormNamePrivilegeAndProviderNo("frm", "priv", "prov"));
	}

}
