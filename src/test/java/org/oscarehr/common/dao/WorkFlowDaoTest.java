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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.WorkFlow;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class WorkFlowDaoTest extends DaoTestFixtures {

	protected WorkFlowDao dao = SpringUtils.getBean(WorkFlowDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("workflow");
	}

        @Test
        public void testCreate() throws Exception {
                WorkFlow entity = new WorkFlow();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByWorkflowType() throws Exception {
		
		String workflowType1 = "alpha";
		String workflowType2 = "bravo";
		
		WorkFlow workFlow1 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow1);
		workFlow1.setWorkflowType(workflowType1);
		dao.persist(workFlow1);
		
		WorkFlow workFlow2 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow2);
		workFlow2.setWorkflowType(workflowType2);
		dao.persist(workFlow2);
		
		WorkFlow workFlow3 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow3);
		workFlow3.setWorkflowType(workflowType1);
		dao.persist(workFlow3);
		
		List<WorkFlow> expectedResult = new ArrayList<WorkFlow>(Arrays.asList(workFlow1, workFlow3));
		List<WorkFlow> result = dao.findByWorkflowType(workflowType1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. RESULT:" + result.size());
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
	public void testFindActiveByWorkflowType() throws Exception {
		
		String workflowType1 = "alpha";
		String workflowType2 = "bravo";
		
		String currentState1 = "C";
		String currentState2 = "B";
		
		WorkFlow workFlow1 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow1);
		workFlow1.setWorkflowType(workflowType1);
		workFlow1.setCurrentState(currentState2);
		dao.persist(workFlow1);
		
		WorkFlow workFlow2 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow2);
		workFlow2.setWorkflowType(workflowType2);
		workFlow2.setCurrentState(currentState1);
		dao.persist(workFlow2);
		
		WorkFlow workFlow3 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow3);
		workFlow3.setWorkflowType(workflowType1);
		workFlow3.setCurrentState(currentState2);
		dao.persist(workFlow3);
		
		WorkFlow workFlow4 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow4);
		workFlow4.setWorkflowType(workflowType1);
		workFlow4.setCurrentState(currentState1);
		dao.persist(workFlow4);
		
		List<WorkFlow> expectedResult = new ArrayList<WorkFlow>(Arrays.asList(workFlow1, workFlow3));
		List<WorkFlow> result = dao.findActiveByWorkflowType(workflowType1);
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. RESULT:" + result.size());
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
	public void testFindActiveByWorkflowTypeAndDemographicNo() throws Exception {

		String workflowType1 = "alpha";
		String workflowType2 = "bravo";
		
		String currentState1 = "C";
		String currentState2 = "B";
		
		String demographicNo1 = "100";
		String demographicNo2 = "200";
		
		WorkFlow workFlow1 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow1);
		workFlow1.setWorkflowType(workflowType1);
		workFlow1.setCurrentState(currentState2);
		workFlow1.setDemographicNo(demographicNo1);
		dao.persist(workFlow1);
		
		WorkFlow workFlow2 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow2);
		workFlow2.setWorkflowType(workflowType2);
		workFlow2.setCurrentState(currentState1);
		workFlow2.setDemographicNo(demographicNo2);
		dao.persist(workFlow2);
		
		WorkFlow workFlow3 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow3);
		workFlow3.setWorkflowType(workflowType1);
		workFlow3.setCurrentState(currentState2);
		workFlow3.setDemographicNo(demographicNo1);
		dao.persist(workFlow3);
		
		WorkFlow workFlow4 = new WorkFlow();
		EntityDataGenerator.generateTestDataForModelClass(workFlow4);
		workFlow4.setWorkflowType(workflowType1);
		workFlow4.setCurrentState(currentState1);
		workFlow4.setDemographicNo(demographicNo1);
		dao.persist(workFlow4);
		
		List<WorkFlow> expectedResult = new ArrayList<WorkFlow>(Arrays.asList(workFlow1, workFlow3));
		List<WorkFlow> result = dao.findActiveByWorkflowTypeAndDemographicNo(workflowType1, demographicNo1);
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. RESULT:" + result.size());
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
