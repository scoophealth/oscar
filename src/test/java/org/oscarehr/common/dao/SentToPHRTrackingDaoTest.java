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

import static org.junit.Assert.assertNotNull;
/**
 * @author Shazib
 */
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.util.SpringUtils;

public class SentToPHRTrackingDaoTest extends DaoTestFixtures {

	protected SentToPHRTrackingDao dao = SpringUtils.getBean(SentToPHRTrackingDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("SentToPHRTracking");
	}

	@Test
	public void testCreate() throws Exception {
		SentToPHRTracking entity = new SentToPHRTracking();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindByDemographicObjectServer() throws Exception {
		
		int demoNo1 = 100;
		String objName1 = "alpha";
		String server1 = "server1";
		
		int demoNo2 = 200;
		String objName2 = "bravo";
		String server2 = "server2";
		
		SentToPHRTracking PHRTracking1 = new SentToPHRTracking();
		EntityDataGenerator.generateTestDataForModelClass(PHRTracking1);
		PHRTracking1.setDemographicNo(demoNo1);
		PHRTracking1.setObjectName(objName1);
		PHRTracking1.setSentToServer(server1);
		dao.persist(PHRTracking1);
		
		SentToPHRTracking PHRTracking2 = new SentToPHRTracking();
		EntityDataGenerator.generateTestDataForModelClass(PHRTracking2);
		PHRTracking2.setDemographicNo(demoNo2);
		PHRTracking2.setObjectName(objName2);
		PHRTracking2.setSentToServer(server2);
		dao.persist(PHRTracking2);
		
		SentToPHRTracking expcetedResult = PHRTracking1;
		SentToPHRTracking result = dao.findByDemographicObjectServer(demoNo1, objName1, server1);
		
		assertEquals(expcetedResult, result);		
	}

}
