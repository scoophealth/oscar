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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicPharmacy;
import org.oscarehr.util.SpringUtils;

public class DemographicPharmacyDaoTest extends DaoTestFixtures {

	protected DemographicPharmacyDao dao = SpringUtils.getBean(DemographicPharmacyDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	public DemographicPharmacyDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicPharmacy");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicPharmacy entity = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testDataMethods() {		
		dao.addPharmacyToDemographic(1, 100, 1);
		java.util.List<DemographicPharmacy> dps = dao.findByDemographicId(100);
		assertFalse(dps.isEmpty());
		assertEquals(1, dps.get(0).getPharmacyId());
	}

	@Test
	@Ignore
	public void testFindByDemographicId() throws Exception {

		Date addDate1 = new Date(dfm.parse("20080101").getTime());
		Date addDate2 = new Date(dfm.parse("20100101").getTime());
		Date addDate3 = new Date(dfm.parse("20110101").getTime());

		int demographicNo1 = 101;
		int demographicNo2 = 202;

		String status1 = "1";
		String status2 = "2";

		DemographicPharmacy demoPhramacy1 = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(demoPhramacy1);
		demoPhramacy1.setStatus(status1);
		demoPhramacy1.setDemographicNo(demographicNo1);
		demoPhramacy1.setAddDate(addDate1);
		dao.persist(demoPhramacy1);

		DemographicPharmacy demoPhramacy2 = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(demoPhramacy2);
		demoPhramacy2.setStatus(status2);
		demoPhramacy2.setDemographicNo(demographicNo1);
		demoPhramacy2.setAddDate(addDate2);
		dao.persist(demoPhramacy2);

		DemographicPharmacy demoPhramacy3 = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(demoPhramacy3);
		demoPhramacy3.setStatus(status1);
		demoPhramacy3.setDemographicNo(demographicNo1);
		demoPhramacy3.setAddDate(addDate3);
		dao.persist(demoPhramacy3);

		DemographicPharmacy demoPhramacy4 = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(demoPhramacy4);
		demoPhramacy4.setStatus(status1);
		demoPhramacy4.setDemographicNo(demographicNo2);
		demoPhramacy4.setAddDate(addDate3);
		dao.persist(demoPhramacy4);

		DemographicPharmacy expectedResult = demoPhramacy1;
		java.util.List<DemographicPharmacy> dps = dao.findByDemographicId(demographicNo1);
		
		assertFalse(dps.isEmpty());
		assertEquals(expectedResult, dps.get(0));
	}
}
