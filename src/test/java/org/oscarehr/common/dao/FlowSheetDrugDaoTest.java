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
import org.oscarehr.common.model.FlowSheetDrug;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class FlowSheetDrugDaoTest extends DaoTestFixtures {

	protected FlowSheetDrugDao dao = SpringUtils.getBean(FlowSheetDrugDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("flowsheet_drug");
	}

        @Test
        public void testCreate() throws Exception {
                FlowSheetDrug entity = new FlowSheetDrug();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testGetFlowSheetDrugs() throws Exception {
		
		String flowsheet1 = "alpha";
		String flowsheet2 = "bravo";
		
		boolean isArchived = true;
		
		int demographicNo1 = 111;
		int demographicNo2 = 222;
		
		FlowSheetDrug flowSheetDrug1 = new FlowSheetDrug();
		EntityDataGenerator.generateTestDataForModelClass(flowSheetDrug1);
		flowSheetDrug1.setFlowsheet(flowsheet1);
		flowSheetDrug1.setArchived(!isArchived);
		flowSheetDrug1.setDemographicNo(demographicNo1);
		dao.persist(flowSheetDrug1);
		
		FlowSheetDrug flowSheetDrug2 = new FlowSheetDrug();
		EntityDataGenerator.generateTestDataForModelClass(flowSheetDrug2);
		flowSheetDrug2.setFlowsheet(flowsheet2);
		flowSheetDrug2.setArchived(!isArchived);
		flowSheetDrug2.setDemographicNo(demographicNo2);
		dao.persist(flowSheetDrug2);
		
		FlowSheetDrug flowSheetDrug3 = new FlowSheetDrug();
		EntityDataGenerator.generateTestDataForModelClass(flowSheetDrug3);
		flowSheetDrug3.setFlowsheet(flowsheet1);
		flowSheetDrug3.setArchived(!isArchived);
		flowSheetDrug3.setDemographicNo(demographicNo1);
		dao.persist(flowSheetDrug3);
		
		List<FlowSheetDrug> expectedResult = new ArrayList<FlowSheetDrug>(Arrays.asList(flowSheetDrug1, flowSheetDrug3));
		List<FlowSheetDrug> result = dao.getFlowSheetDrugs(flowsheet1, demographicNo1);
		
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
