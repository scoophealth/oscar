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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.MeasurementGroupStyle;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MeasurementGroupStyleDaoTest extends DaoTestFixtures {

	protected MeasurementGroupStyleDao dao = SpringUtils.getBean(MeasurementGroupStyleDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("measurementGroupStyle");
	}

	@Test
	public void testFindAll() throws Exception {

		MeasurementGroupStyle measurementGrpStyle1 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle1);
		dao.persist(measurementGrpStyle1);

		MeasurementGroupStyle measurementGrpStyle2 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle2);
		dao.persist(measurementGrpStyle2);

		MeasurementGroupStyle measurementGrpStyle3 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle3);
		dao.persist(measurementGrpStyle3);

		List<MeasurementGroupStyle> expectedResult = new ArrayList<MeasurementGroupStyle>(Arrays.asList(measurementGrpStyle1, measurementGrpStyle2, measurementGrpStyle3));
		List<MeasurementGroupStyle> result = dao.findAll();

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
	public void testFindByGroupName() throws Exception {
		
		String groupName1 = "alpha", groupName2 = "bravo";
		
		MeasurementGroupStyle measurementGrpStyle1 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle1);
		measurementGrpStyle1.setGroupName(groupName1);
		dao.persist(measurementGrpStyle1);

		MeasurementGroupStyle measurementGrpStyle2 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle2);
		measurementGrpStyle2.setGroupName(groupName1);
		dao.persist(measurementGrpStyle2);

		MeasurementGroupStyle measurementGrpStyle3 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle3);
		measurementGrpStyle3.setGroupName(groupName2);
		dao.persist(measurementGrpStyle3);
		
		MeasurementGroupStyle measurementGrpStyle4 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle4);
		measurementGrpStyle4.setGroupName(groupName1);
		dao.persist(measurementGrpStyle4);

		List<MeasurementGroupStyle> expectedResult = new ArrayList<MeasurementGroupStyle>(Arrays.asList(measurementGrpStyle1, measurementGrpStyle2, measurementGrpStyle4));
		List<MeasurementGroupStyle> result = dao.findByGroupName(groupName1);

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
	public void testFindByCssId() throws Exception {
		
		int cssId1 = 101, cssId2 = 202;
		
		MeasurementGroupStyle measurementGrpStyle1 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle1);
		measurementGrpStyle1.setCssId(cssId1);
		dao.persist(measurementGrpStyle1);

		MeasurementGroupStyle measurementGrpStyle2 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle2);
		measurementGrpStyle2.setCssId(cssId1);
		dao.persist(measurementGrpStyle2);

		MeasurementGroupStyle measurementGrpStyle3 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle3);
		measurementGrpStyle3.setCssId(cssId2);
		dao.persist(measurementGrpStyle3);
		
		MeasurementGroupStyle measurementGrpStyle4 = new MeasurementGroupStyle();
		EntityDataGenerator.generateTestDataForModelClass(measurementGrpStyle4);
		measurementGrpStyle4.setCssId(cssId1);
		dao.persist(measurementGrpStyle4);

		List<MeasurementGroupStyle> expectedResult = new ArrayList<MeasurementGroupStyle>(Arrays.asList(measurementGrpStyle1, measurementGrpStyle2, measurementGrpStyle4));
		List<MeasurementGroupStyle> result = dao.findByCssId(cssId1);

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
}