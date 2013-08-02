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
import org.oscarehr.common.model.MeasurementCSSLocation;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MeasurementCSSLocationDaoTest extends DaoTestFixtures {

	protected MeasurementCSSLocationDao dao = SpringUtils.getBean(MeasurementCSSLocationDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("measurementCSSLocation");
	}

	@Test
	public void testCreate() throws Exception {
		MeasurementCSSLocation entity = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByLocation() throws Exception {
		
		String location1 = "alpha", location2 = "bravo";
		
		MeasurementCSSLocation mCSSL1 = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(mCSSL1);
		mCSSL1.setLocation(location2);
		dao.persist(mCSSL1);
		
		MeasurementCSSLocation mCSSL2 = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(mCSSL2);
		mCSSL2.setLocation(location1);
		dao.persist(mCSSL2);
		
		MeasurementCSSLocation mCSSL3 = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(mCSSL3);
		mCSSL3.setLocation(location1);
		dao.persist(mCSSL3);
		
		MeasurementCSSLocation mCSSL4 = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(mCSSL4);
		mCSSL4.setLocation(location2);
		dao.persist(mCSSL4);
		
		MeasurementCSSLocation mCSSL5 = new MeasurementCSSLocation();
		EntityDataGenerator.generateTestDataForModelClass(mCSSL5);
		mCSSL5.setLocation(location1);
		dao.persist(mCSSL5);
		
		List<MeasurementCSSLocation> expectedResult = new ArrayList<MeasurementCSSLocation>(Arrays.asList(mCSSL2, mCSSL3, mCSSL5));
		List<MeasurementCSSLocation> result = dao.findByLocation(location1);

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