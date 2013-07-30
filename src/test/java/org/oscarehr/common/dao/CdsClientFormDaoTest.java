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

import static org.junit.Assert.assertEquals;
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
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsClientFormDaoTest extends DaoTestFixtures {

	protected CdsClientFormDao dao = SpringUtils.getBean(CdsClientFormDao.class);
	protected DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	Logger logger = MiscUtils.getLogger();
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("CdsClientForm");
	}


	@Test
	/**
	 * Ensures that the latest client form is returned.
	 * @throws Exception
	 */
	public void testFindLatestByFacilityClient() throws Exception {
		int facilityId = 101;
		int clientId = 109;

		CdsClientForm clientForm1 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm1);
		clientForm1.setClientId(clientId);
		clientForm1.setFacilityId(facilityId);

		CdsClientForm clientForm2 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm2);
		clientForm2.setClientId(clientId);
		clientForm2.setFacilityId(facilityId);

		dao.persist(clientForm1);
		dao.persist(clientForm2);

		CdsClientForm result = dao.findLatestByFacilityClient(facilityId, clientId);
		CdsClientForm expectedResult = clientForm1;
		
		assertEquals(expectedResult, result);
	}

	
	@Test
	public void testFindByFacilityClient() throws Exception {
		int facilityId = 101;
		int clientId = 109;

		CdsClientForm clientForm1 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm1);
		clientForm1.setClientId(clientId);
		clientForm1.setFacilityId(facilityId);

		CdsClientForm clientForm2 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm2);
		clientForm2.setClientId(clientId);
		clientForm2.setFacilityId(facilityId);

		dao.persist(clientForm1);
		dao.persist(clientForm2);

		List<CdsClientForm> result = dao.findByFacilityClient(facilityId, clientId);
		List<CdsClientForm> expectedResult = new ArrayList<CdsClientForm>(Arrays.asList(
				clientForm1,
				clientForm2
				));
		
		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		// fail if items are not equal and in order
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by date 'created'.");
				fail("Items not sorted by date 'created'.");
			}
		}
		assertTrue(true);
	}

	
	@Test
	public void testFindLatestSignedCdsForms() throws Exception {
		int facilityId = 101;
		String formVersion = "1.1.0";
		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("21001231").getTime());
		
		CdsClientForm clientForm1 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm1);
		clientForm1.setFacilityId(facilityId);
		clientForm1.setCdsFormVersion(formVersion);
		clientForm1.setSigned(true);
		
		CdsClientForm clientForm2 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm2);
		clientForm2.setFacilityId(facilityId);
		clientForm2.setCdsFormVersion(formVersion);
		clientForm2.setSigned(false);

		CdsClientForm clientForm3 = new CdsClientForm();
		EntityDataGenerator.generateTestDataForModelClass(clientForm3);
		clientForm3.setFacilityId(facilityId);
		clientForm3.setCdsFormVersion(formVersion);
		clientForm3.setSigned(true);
		
		dao.persist(clientForm1);
		dao.persist(clientForm2);
		dao.persist(clientForm3);
		
		List<CdsClientForm> result = dao.findSignedCdsForms(facilityId, formVersion, startDate, endDate);
		List<CdsClientForm> expectedResult = new ArrayList<CdsClientForm>(Arrays.asList(
				clientForm1,
				clientForm3
				));

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		// fail if items are not equal and in order
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by date 'created'.");
				fail("Items not sorted by date 'created'.");
			}
		}
		assertTrue(true);
	}

}
