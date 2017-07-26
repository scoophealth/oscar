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
import org.oscarehr.common.model.MyGroup;
import org.oscarehr.common.model.MyGroupPrimaryKey;
import org.oscarehr.common.model.WaitingListName;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class WaitingListNameDaoTest extends DaoTestFixtures {

	protected WaitingListNameDao dao = SpringUtils.getBean(WaitingListNameDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "mygroup", "waitingListName");
	}
	
	@Test
	public void testCountActiveWatingListNames() throws Exception {
		
		String isHistory = "Y";
		String isNotHistory = "N";
		
		WaitingListName waitingListName1 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName1);
		waitingListName1.setIsHistory(isNotHistory);
		dao.persist(waitingListName1);
		
		WaitingListName waitingListName2 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName2);
		waitingListName2.setIsHistory(isHistory);
		dao.persist(waitingListName2);
		
		WaitingListName waitingListName3 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName3);
		waitingListName3.setIsHistory(isNotHistory);
		dao.persist(waitingListName3);
		
		WaitingListName waitingListName4 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName4);
		waitingListName4.setIsHistory(isNotHistory);
		dao.persist(waitingListName4);
		
		long expectedResult = 3;
		long result = dao.countActiveWatingListNames();
		
		assertEquals(expectedResult, result);	
	}
	
	@Test
	public void testFindCurrentByNameAndGroup() throws Exception {
		
		String isHistory = "Y";
		String isNotHistory = "N";
		
		String name1 = "alpha";
		String name2 = "bravo";
		
		String groupNo1 = "101";
		String groupNo2 = "202";
		
		WaitingListName waitingListName1 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName1);
		waitingListName1.setName(name1);
		waitingListName1.setGroupNo(groupNo1);
		waitingListName1.setIsHistory(isNotHistory);
		dao.persist(waitingListName1);
		
		WaitingListName waitingListName2 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName2);
		waitingListName2.setName(name2);
		waitingListName2.setGroupNo(groupNo1);
		waitingListName2.setIsHistory(isHistory);
		dao.persist(waitingListName2);
		
		WaitingListName waitingListName3 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName3);
		waitingListName3.setName(name1);
		waitingListName3.setGroupNo(groupNo2);
		waitingListName3.setIsHistory(isNotHistory);
		dao.persist(waitingListName3);
		
		WaitingListName waitingListName4 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName4);
		waitingListName4.setName(name1);
		waitingListName4.setGroupNo(groupNo1);
		waitingListName4.setIsHistory(isNotHistory);
		dao.persist(waitingListName4);
		
		WaitingListName waitingListName5 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName5);
		waitingListName5.setName(name1);
		waitingListName5.setGroupNo(groupNo1);
		waitingListName5.setIsHistory(isHistory);
		dao.persist(waitingListName5);
		
		List<WaitingListName> expectedResult = new ArrayList<WaitingListName>(Arrays.asList(waitingListName1, waitingListName4));
		List<WaitingListName> result = dao.findCurrentByNameAndGroup(name1, groupNo1);

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
	public void testFindByMyGroups() throws Exception {

		String isHistory = "Y";
		String isNotHistory = "N";
		
		String name1 = "charlie";
		String name2 = "bravo";
		String name3 = "delta";
		String name4 = "alpha";
		
		String myGroupNo1 = "101";
		String myGroupNo2 = "202";
		String myGroupNo3 = "303";
		String myGroupNo4 = "404";
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		String providerNo3 = "333";
		String providerNo4 = "444";
		
		MyGroupDao daoMyGroup = (MyGroupDao)SpringUtils.getBean(MyGroupDao.class);
		
		MyGroupPrimaryKey myGroupPrimaryKey1 = new MyGroupPrimaryKey();
		myGroupPrimaryKey1.setMyGroupNo(myGroupNo1);
		myGroupPrimaryKey1.setProviderNo(providerNo1);
		
		MyGroupPrimaryKey myGroupPrimaryKey2 = new MyGroupPrimaryKey();
		myGroupPrimaryKey2.setMyGroupNo(myGroupNo2);
		myGroupPrimaryKey2.setProviderNo(providerNo2);
		
		MyGroupPrimaryKey myGroupPrimaryKey3 = new MyGroupPrimaryKey();
		myGroupPrimaryKey3.setMyGroupNo(myGroupNo3);
		myGroupPrimaryKey3.setProviderNo(providerNo3);
		
		MyGroupPrimaryKey myGroupPrimaryKey4 = new MyGroupPrimaryKey();
		myGroupPrimaryKey4.setMyGroupNo(myGroupNo4);
		myGroupPrimaryKey4.setProviderNo(providerNo4);
		
		MyGroup myGroup1 = new MyGroup();
		EntityDataGenerator.generateTestDataForModelClass(myGroup1);
		myGroup1.setId(myGroupPrimaryKey1);
		daoMyGroup.merge(myGroup1);
		
		MyGroup myGroup2 = new MyGroup();
		EntityDataGenerator.generateTestDataForModelClass(myGroup2);
		myGroup2.setId(myGroupPrimaryKey2);
		daoMyGroup.merge(myGroup2);
		
		MyGroup myGroup3 = new MyGroup();
		EntityDataGenerator.generateTestDataForModelClass(myGroup3);
		myGroup3.setId(myGroupPrimaryKey3);
		daoMyGroup.merge(myGroup3);
		
		MyGroup myGroup4 = new MyGroup();
		EntityDataGenerator.generateTestDataForModelClass(myGroup4);
		myGroup4.setId(myGroupPrimaryKey4);
		daoMyGroup.merge(myGroup4);
		
		WaitingListName waitingListName1 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName1);
		waitingListName1.setGroupNo("101");
		waitingListName1.setIsHistory(isNotHistory);
		waitingListName1.setName(name1);
		dao.persist(waitingListName1);
		
		WaitingListName waitingListName2 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName2);
		waitingListName2.setGroupNo("202");
		waitingListName2.setIsHistory(isHistory);
		waitingListName2.setName(name2);
		dao.persist(waitingListName2);
		
		WaitingListName waitingListName3 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName3);
		waitingListName3.setGroupNo("303");
		waitingListName3.setIsHistory(isHistory);
		waitingListName3.setName(name3);
		dao.persist(waitingListName3);
		
		WaitingListName waitingListName4 = new WaitingListName();
		EntityDataGenerator.generateTestDataForModelClass(waitingListName4);
		waitingListName4.setGroupNo("404");
		waitingListName4.setIsHistory(isNotHistory);
		waitingListName4.setName(name4);
		dao.persist(waitingListName4);
		
		List<MyGroup> myGroups = new ArrayList<MyGroup>(Arrays.asList(myGroup1, myGroup2, myGroup3, myGroup4));
		
		List<WaitingListName> expectedResult = new ArrayList<WaitingListName>(Arrays.asList(waitingListName4, waitingListName1));
		List<WaitingListName> result = dao.findByMyGroups(providerNo1, myGroups);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.Result: " + result.size());
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
