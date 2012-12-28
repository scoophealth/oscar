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
import org.oscarehr.common.model.RSchedule;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RScheduleDaoTest extends DaoTestFixtures {

	protected RScheduleDao dao = (RScheduleDao) SpringUtils.getBean("rScheduleDao");
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("rschedule");
	}

        @Test
        public void testCreate() throws Exception {
                RSchedule entity = new RSchedule();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByProviderAvailableAndDate() throws Exception {
		
		String providerNo1 = "100";
		String providerNo2 = "200";
		
		String available1 = "y";
		String available2 = "n";
		
		Date sDate1 = new Date(dfm.parse("20110101").getTime());
		Date sDate2 = new Date(dfm.parse("20100101").getTime());
		
		RSchedule rSchedule1 = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(rSchedule1);
		rSchedule1.setProviderNo(providerNo1);
		rSchedule1.setAvailable(available1);
		rSchedule1.setsDate(sDate1);
		dao.persist(rSchedule1);
		
		RSchedule rSchedule2 = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(rSchedule2);
		rSchedule2.setProviderNo(providerNo2);
		rSchedule2.setAvailable(available2);
		rSchedule2.setsDate(sDate2);
		dao.persist(rSchedule2);
		
		RSchedule rSchedule3 = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(rSchedule3);
		rSchedule3.setProviderNo(providerNo1);
		rSchedule3.setAvailable(available1);
		rSchedule3.setsDate(sDate1);
		dao.persist(rSchedule3);
		
		RSchedule rSchedule4 = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(rSchedule4);
		rSchedule4.setProviderNo(providerNo1);
		rSchedule4.setAvailable(available1);
		rSchedule4.setsDate(sDate2);
		dao.persist(rSchedule4);
		
		List<RSchedule> expectedResult = new ArrayList<RSchedule>(Arrays.asList(rSchedule1, rSchedule3));
		List<RSchedule> result = dao.findByProviderAvailableAndDate(providerNo1, available1, sDate1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.RESULT: " + result.size());
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
    public void testFindByProviderNoAndDates() {
	    assertNotNull(dao.findByProviderNoAndDates("100", new Date()));
    }
}
