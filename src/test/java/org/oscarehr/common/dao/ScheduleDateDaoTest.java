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
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.util.SpringUtils;

public class ScheduleDateDaoTest extends DaoTestFixtures {

	private ScheduleDateDao dao = SpringUtils.getBean(ScheduleDateDao.class);

	public ScheduleDateDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduledate");
	}

	@Test
	public void testCreate() throws Exception {
		ScheduleDate entity = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByProviderNoAndDate()  {
		ScheduleDate entity = new ScheduleDate();
		entity.setAvailable('1');
		entity.setCreator("000001");
		entity.setDate(new Date());
		entity.setHour("12");
		entity.setPriority('P');
		entity.setProviderNo("000001");
		entity.setReason("reason");
		entity.setStatus('A');
		dao.persist(entity);

		assertNotNull(dao.findByProviderNoAndDate("000001", new Date()));
		assertEquals(entity.getId(),dao.findByProviderNoAndDate("000001", new Date()).getId());

	}
}
