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
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class JointAdmissionDaoTest extends DaoTestFixtures {

	protected JointAdmissionDao dao = SpringUtils.getBean(JointAdmissionDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("joint_admissions");
	}

	@Test
	public void testCreate() throws Exception {
		JointAdmission entity = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetSpouseAndDependents() throws Exception {
		
		boolean isArchived = true;
		int headClientId1 = 101, headClientId2 = 202;
		
		JointAdmission jointAdmission1 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission1);
		jointAdmission1.setArchived(!isArchived);
		jointAdmission1.setHeadClientId(headClientId2);
		dao.persist(jointAdmission1);
		
		JointAdmission jointAdmission2 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission2);
		jointAdmission2.setArchived(!isArchived);
		jointAdmission2.setHeadClientId(headClientId1);
		dao.persist(jointAdmission2);
		
		JointAdmission jointAdmission3 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission3);
		jointAdmission3.setArchived(isArchived);
		jointAdmission3.setHeadClientId(headClientId1);
		dao.persist(jointAdmission3);
		
		JointAdmission jointAdmission4 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission4);
		jointAdmission4.setArchived(!isArchived);
		jointAdmission4.setHeadClientId(headClientId1);
		dao.persist(jointAdmission4);
		
		JointAdmission jointAdmission5 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission5);
		jointAdmission5.setArchived(!isArchived);
		jointAdmission5.setHeadClientId(headClientId1);
		dao.persist(jointAdmission5);
		
		List<JointAdmission> expectedResult = new ArrayList<JointAdmission>(Arrays.asList(jointAdmission2, jointAdmission4, jointAdmission5));
		List<JointAdmission> result = dao.getSpouseAndDependents(headClientId1);

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
	public void testGetJointAdmission() throws Exception {
		
		boolean isArchived = true;
		int clientId1 = 101, clientId2 = 202;
		
		JointAdmission jointAdmission1 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission1);
		jointAdmission1.setArchived(!isArchived);
		jointAdmission1.setClientId(clientId2);
		dao.persist(jointAdmission1);
		
		JointAdmission jointAdmission2 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission2);
		jointAdmission2.setArchived(!isArchived);
		jointAdmission2.setClientId(clientId1);
		dao.persist(jointAdmission2);
		
		JointAdmission jointAdmission3 = new JointAdmission();
		EntityDataGenerator.generateTestDataForModelClass(jointAdmission3);
		jointAdmission3.setArchived(isArchived);
		jointAdmission3.setClientId(clientId1);
		dao.persist(jointAdmission3);
		
		JointAdmission expectedResult = jointAdmission2;
		JointAdmission result = dao.getJointAdmission(clientId1);
		
		assertEquals(expectedResult, result);
	}
}