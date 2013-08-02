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
import org.oscarehr.common.model.ReportTableFieldCaption;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ReportTableFieldCaptionDaoTest extends DaoTestFixtures {

	protected ReportTableFieldCaptionDao dao = SpringUtils.getBean(ReportTableFieldCaptionDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("reportTableFieldCaption");
	}

	@Test
	public void testCreate() throws Exception {
		ReportTableFieldCaption entity = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByTableNameAndName() throws Exception {
		
		String tableName1 = "table1", tableName2 = "table2";
		String name1 = "alpha", name2 = "bravo";
		
		ReportTableFieldCaption rTFC1 = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(rTFC1);
		rTFC1.setTableName(tableName1);
		rTFC1.setName(name2);
		dao.persist(rTFC1);
		
		ReportTableFieldCaption rTFC2 = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(rTFC2);
		rTFC2.setTableName(tableName1);
		rTFC2.setName(name1);
		dao.persist(rTFC2);
		
		ReportTableFieldCaption rTFC3 = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(rTFC3);
		rTFC3.setTableName(tableName1);
		rTFC3.setName(name1);
		dao.persist(rTFC3);
		
		ReportTableFieldCaption rTFC4 = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(rTFC4);
		rTFC4.setTableName(tableName2);
		rTFC4.setName(name1);
		dao.persist(rTFC4);
		
		ReportTableFieldCaption rTFC5 = new ReportTableFieldCaption();
		EntityDataGenerator.generateTestDataForModelClass(rTFC5);
		rTFC5.setTableName(tableName1);
		rTFC5.setName(name1);
		dao.persist(rTFC5);
		
		List<ReportTableFieldCaption> expectedResult = new ArrayList<ReportTableFieldCaption>(Arrays.asList(rTFC2, rTFC3, rTFC5));
		List<ReportTableFieldCaption> result = dao.findByTableNameAndName(tableName1, name1);

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