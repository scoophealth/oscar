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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.SystemMessage;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
/**
 * @author Shazib
 */

@SuppressWarnings("unused")
public class SystemMessageDaoTest extends DaoTestFixtures {

	protected SystemMessageDao dao = SpringUtils.getBean(SystemMessageDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("SystemMessage");
	}

	@Test
	public void testCreate() throws Exception {
		SystemMessage entity = new SystemMessage();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}


	@Test
	public void testFindAll() throws Exception {
		
		SystemMessage sysMessage1 = new SystemMessage();
		EntityDataGenerator.generateTestDataForModelClass(sysMessage1);
		Date date1 = new Date(dfm.parse("20110701").getTime());
		sysMessage1.setExpiryDate(date1);
		dao.persist(sysMessage1);
		
		SystemMessage sysMessage2 = new SystemMessage();
		EntityDataGenerator.generateTestDataForModelClass(sysMessage2);
		Date date2 = new Date(dfm.parse("20100701").getTime());
		sysMessage2.setExpiryDate(date2);
		dao.persist(sysMessage2);
		
		SystemMessage sysMessage3 = new SystemMessage();
		EntityDataGenerator.generateTestDataForModelClass(sysMessage3);
		Date date3 = new Date(dfm.parse("20120701").getTime());
		sysMessage3.setExpiryDate(date3);
		dao.persist(sysMessage3);
		
		List<SystemMessage> result = dao.findAll();
		List<SystemMessage> expectedResult = new ArrayList<SystemMessage>(Arrays.asList(sysMessage3,sysMessage1,sysMessage2));
			
		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
		
		
		
	}

}
