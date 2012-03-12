package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.util.SpringUtils;

public class AppointmentArchiveDaoTest extends DaoTestFixtures{

	private AppointmentArchiveDao dao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");

	public AppointmentArchiveDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("appointmentArchive","appointment");
	}

	@Test
	public void testCreate() throws Exception {
		 AppointmentArchive entity = new AppointmentArchive();
		 EntityDataGenerator.generateTestDataForModelClass(entity);
		 dao.persist(entity);
		 assertNotNull(entity.getId());
	}

	@Test
	public void testArchiveAppointment() throws Exception {
		OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appointmentDao.persist(appt);

		AppointmentArchive archive = dao.archiveAppointment(appt);

		assertNotNull(archive.getId());
	}
}
