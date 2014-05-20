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
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.util.SpringUtils;

public class AppointmentArchiveDaoTest extends DaoTestFixtures {

	protected AppointmentArchiveDao dao = (AppointmentArchiveDao) SpringUtils.getBean("appointmentArchiveDao");

	public AppointmentArchiveDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("appointmentArchive", "appointment");
	}

	@Test
	public void testCreate() throws Exception {
		AppointmentArchive entity = new AppointmentArchive();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
		
		Calendar cal=new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		List<AppointmentArchive> results=dao.findByUpdateDate(cal.getTime(), 99);
		assertTrue(results.size()>0);

		cal.add(Calendar.DAY_OF_YEAR, 2);
		results=dao.findByUpdateDate(cal.getTime(), 99);
		assertEquals(0, results.size());
	}

	@Test
	public void testArchiveAppointment() throws Exception {
		OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appointmentDao.persist(appt);

		AppointmentArchive archive = dao.archiveAppointment(appt);

		assertNotNull(archive.getId());
	}
}
