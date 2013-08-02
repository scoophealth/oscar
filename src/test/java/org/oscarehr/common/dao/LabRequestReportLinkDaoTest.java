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
import org.oscarehr.common.model.LabRequestReportLink;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class LabRequestReportLinkDaoTest extends DaoTestFixtures {

	protected LabRequestReportLinkDao dao = SpringUtils.getBean(LabRequestReportLinkDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("labRequestReportLink");
	}

	@Test
	public void testCreate() throws Exception {
		LabRequestReportLink entity = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByReportTableAndReportId() throws Exception {
		
		String reportTable1 = "alpha", reportTable2 = "bravo";
		int reportId1 = 101, reportId2 = 202;
		
		LabRequestReportLink lRRL1 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL1);
		lRRL1.setReportTable(reportTable2);
		lRRL1.setReportId(reportId1);
		dao.persist(lRRL1);
		
		LabRequestReportLink lRRL2 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL2);
		lRRL2.setReportTable(reportTable1);
		lRRL2.setReportId(reportId1);
		dao.persist(lRRL2);
		
		LabRequestReportLink lRRL3 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL3);
		lRRL3.setReportTable(reportTable1);
		lRRL3.setReportId(reportId2);
		dao.persist(lRRL3);
		
		LabRequestReportLink lRRL4 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL4);
		lRRL4.setReportTable(reportTable1);
		lRRL4.setReportId(reportId1);
		dao.persist(lRRL4);
		
		LabRequestReportLink lRRL5 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL5);
		lRRL5.setReportTable(reportTable2);
		lRRL5.setReportId(reportId2);
		dao.persist(lRRL5);
		
		LabRequestReportLink lRRL6 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL6);
		lRRL6.setReportTable(reportTable1);
		lRRL6.setReportId(reportId1);
		dao.persist(lRRL6);
		
		List<LabRequestReportLink> expectedResult = new ArrayList<LabRequestReportLink>(Arrays.asList(lRRL2, lRRL4, lRRL6));
		List<LabRequestReportLink> result = dao.findByReportTableAndReportId(reportTable1, reportId1);

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
	public void testFindByRequestTableAndRequestId() throws Exception {
		
		String requestTable1 = "alpha", requestTable2 = "bravo";
		int requestId1 = 101, requestId2 = 202;
		
		LabRequestReportLink lRRL1 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL1);
		lRRL1.setRequestTable(requestTable2);
		lRRL1.setRequestId(requestId1);
		dao.persist(lRRL1);
		
		LabRequestReportLink lRRL2 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL2);
		lRRL2.setRequestTable(requestTable1);
		lRRL2.setRequestId(requestId1);
		dao.persist(lRRL2);
		
		LabRequestReportLink lRRL3 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL3);
		lRRL3.setRequestTable(requestTable1);
		lRRL3.setRequestId(requestId2);
		dao.persist(lRRL3);
		
		LabRequestReportLink lRRL4 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL4);
		lRRL4.setRequestTable(requestTable1);
		lRRL4.setRequestId(requestId1);
		dao.persist(lRRL4);
		
		LabRequestReportLink lRRL5 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL5);
		lRRL5.setRequestTable(requestTable2);
		lRRL5.setRequestId(requestId2);
		dao.persist(lRRL5);
		
		LabRequestReportLink lRRL6 = new LabRequestReportLink();
		EntityDataGenerator.generateTestDataForModelClass(lRRL6);
		lRRL6.setRequestTable(requestTable1);
		lRRL6.setRequestId(requestId1);
		dao.persist(lRRL6);
		
		List<LabRequestReportLink> expectedResult = new ArrayList<LabRequestReportLink>(Arrays.asList(lRRL2, lRRL4, lRRL6));
		List<LabRequestReportLink> result = dao.findByRequestTableAndRequestId(requestTable1, requestId1);

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