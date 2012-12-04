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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CdsHospitalisationDays;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsHospitalisationDaysDaoTest extends DaoTestFixtures {

	protected CdsHospitalisationDaysDao dao = (CdsHospitalisationDaysDao)SpringUtils.getBean(CdsHospitalisationDaysDao.class);
	Logger logger = MiscUtils.getLogger(); 
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("CdsHospitalisationDays");
	}

	@Test
	public void testFindByClientId() throws Exception {
		int clientId = 10;
		
		CdsHospitalisationDays hospitalisationDays1 = new CdsHospitalisationDays();
		EntityDataGenerator.generateTestDataForModelClass(hospitalisationDays1);
		hospitalisationDays1.setClientId(clientId);
		
		// Wrong client id; should not be returned
		CdsHospitalisationDays hospitalisationDays2 = new CdsHospitalisationDays();
		EntityDataGenerator.generateTestDataForModelClass(hospitalisationDays2);
		hospitalisationDays2.setClientId(100);
		
		CdsHospitalisationDays hospitalisationDays3 = new CdsHospitalisationDays();
		EntityDataGenerator.generateTestDataForModelClass(hospitalisationDays3);
		hospitalisationDays3.setClientId(clientId);
		
		dao.persist(hospitalisationDays1);
		dao.persist(hospitalisationDays2);
		dao.persist(hospitalisationDays3);
		
		List<CdsHospitalisationDays> result = dao.findByClientId(clientId);
		List<CdsHospitalisationDays> expectedResult = new ArrayList<CdsHospitalisationDays>(Arrays.asList(
				hospitalisationDays1,
				hospitalisationDays3
				));
		
		assertTrue(result.size() == expectedResult.size());
		assertTrue(result.containsAll(expectedResult));
	}

}
