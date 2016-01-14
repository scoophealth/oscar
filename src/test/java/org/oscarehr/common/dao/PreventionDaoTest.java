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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PreventionDaoTest extends DaoTestFixtures {

	protected PreventionDao dao = (PreventionDao) SpringUtils.getBean(PreventionDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographic", "preventions", "demographic_merged");
	}

	@Test
	//pass
	public void testFindByDemographicId() throws Exception {

		int demographicId1 = 111;
		int demographicId2 = 112;

		Prevention prevention1 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention1);
		prevention1.setDemographicId(demographicId1);
		dao.persist(prevention1);

		Prevention prevention2 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention2);
		prevention2.setDemographicId(demographicId2);
		dao.persist(prevention2);

		Prevention prevention3 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention3);
		prevention3.setDemographicId(demographicId1);
		dao.persist(prevention3);

		List<Prevention> expectedResult = new ArrayList<Prevention>(Arrays.asList(prevention1, prevention3));
		List<Prevention> result = dao.findByDemographicId(demographicId1);

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
		
		Calendar cal=new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		List<Prevention> results=dao.findByUpdateDate(cal.getTime(), 99);
		assertTrue(results.size()>0);

		cal.add(Calendar.DAY_OF_YEAR, 2);
		results=dao.findByUpdateDate(cal.getTime(), 99);
		assertEquals(0, results.size());

	}


	@Test
	public void testFindNotDeletedByDemographicId() throws Exception {

		int demographicId1 = 111;
		int demographicId2 = 112;

		boolean isDeleted = true;

		Prevention prevention1 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention1);
		prevention1.setDemographicId(demographicId1);
		prevention1.setDeleted(!isDeleted);
		dao.persist(prevention1);

		Prevention prevention2 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention2);
		prevention2.setDemographicId(demographicId2);
		prevention2.setDeleted(isDeleted);
		dao.persist(prevention2);

		Prevention prevention3 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention3);
		prevention3.setDemographicId(demographicId1);
		prevention3.setDeleted(!isDeleted);
		dao.persist(prevention3);

		Prevention prevention4 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention4);
		prevention4.setDemographicId(demographicId2);
		prevention4.setDeleted(!isDeleted);
		dao.persist(prevention4);

		List<Prevention> expectedResult = new ArrayList<Prevention>(Arrays.asList(prevention1, prevention3));
		List<Prevention> result = dao.findNotDeletedByDemographicId(demographicId1);

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
	public void testFindByTypeAndDate() throws Exception {

		String preventionType1 = "delta";
		String preventionType2 = "omega";

		boolean isDeleted = true;
		boolean isRefused = true;

		Date preventionStartDate = new Date(dfm.parse("20081012").getTime());
		Date preventionEndDate = new Date(dfm.parse("20121001").getTime());

		Date preventionDate1 = new Date(dfm.parse("20130101").getTime());//not in
		Date preventionDate2 = new Date(dfm.parse("20091025").getTime());//in
		Date preventionDate3 = new Date(dfm.parse("20100915").getTime());//in
		Date preventionDate4 = new Date(dfm.parse("20071012").getTime());//not in
		Date preventionDate5 = new Date(dfm.parse("20081212").getTime());//in

		Prevention prevention1 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention1);
		prevention1.setPreventionType(preventionType1);//in
		prevention1.setPreventionDate(preventionDate1);//not in
		prevention1.setDeleted(!isDeleted);//in
		prevention1.setRefused(isRefused);//not in
		dao.persist(prevention1);

		Prevention prevention2 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention2);
		prevention2.setPreventionType(preventionType2);//not in
		prevention2.setPreventionDate(preventionDate2);//in
		prevention2.setDeleted(isDeleted);//not in
		prevention2.setRefused(!isRefused);//in
		dao.persist(prevention2);

		Prevention prevention3 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention3);
		prevention3.setPreventionType(preventionType1);//in
		prevention3.setPreventionDate(preventionDate3);//in
		prevention3.setDeleted(!isDeleted);//in
		prevention3.setRefused(!isRefused);//in
		dao.persist(prevention3);

		Prevention prevention4 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention4);
		prevention4.setPreventionType(preventionType1);//in
		prevention4.setPreventionDate(preventionDate4);//not in
		prevention4.setDeleted(isDeleted);//not in
		prevention4.setRefused(!isRefused);//in
		dao.persist(prevention4);

		Prevention prevention5 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention5);
		prevention5.setPreventionType(preventionType1);//in
		prevention5.setPreventionDate(preventionDate5);//in
		prevention5.setDeleted(!isDeleted);//in
		prevention5.setRefused(!isRefused);//in
		dao.persist(prevention5);

		List<Prevention> expectedResult = new ArrayList<Prevention>(Arrays.asList(prevention5, prevention3));
		List<Prevention> result = dao.findByTypeAndDate(preventionType1, preventionStartDate, preventionEndDate);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.RESULT: " + result.size());
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
	public void testFindByTypeAndDemoNo() throws Exception {

		String preventionType1 = "delta";
		String preventionType2 = "omega";

		int demographicId1 = 111;
		int demographicId2 = 112;

		boolean isDeleted = true;

		Date preventionDate1 = new Date(dfm.parse("20130701").getTime());
		Date preventionDate2 = new Date(dfm.parse("20091012").getTime());
		Date preventionDate3 = new Date(dfm.parse("20121122").getTime());

		Prevention prevention1 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention1);
		prevention1.setPreventionType(preventionType1);
		prevention1.setDemographicId(demographicId1);
		prevention1.setDeleted(!isDeleted);
		prevention1.setPreventionDate(preventionDate1);
		dao.persist(prevention1);

		Prevention prevention2 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention2);
		prevention2.setPreventionType(preventionType2);
		prevention2.setDemographicId(demographicId2);
		prevention2.setDeleted(isDeleted);
		prevention2.setPreventionDate(preventionDate2);
		dao.persist(prevention2);

		Prevention prevention3 = new Prevention();
		EntityDataGenerator.generateTestDataForModelClass(prevention3);
		prevention3.setPreventionType(preventionType1);
		prevention3.setDemographicId(demographicId1);
		prevention3.setDeleted(!isDeleted);
		prevention3.setPreventionDate(preventionDate3);
		dao.persist(prevention3);

		List<Prevention> expectedResult = new ArrayList<Prevention>(Arrays.asList(prevention3, prevention1));
		List<Prevention> result = dao.findByTypeAndDemoNo(preventionType1, demographicId1);

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
	public void testFindActiveByDemoId() {
		assertNotNull(dao.findActiveByDemoId(199));
	}

}
