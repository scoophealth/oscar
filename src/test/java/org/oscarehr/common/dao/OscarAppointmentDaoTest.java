package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.SpringUtils;


public class OscarAppointmentDaoTest extends DaoTestFixtures {

	OscarAppointmentDao dao=(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


	@Before
	public void before() throws Exception {
		//nothing here yet. should clean up the appointment table to a known state
		SchemaUtils.restoreTable("appointment");
	}


	@Test
	public void test() {
		Calendar cal = Calendar.getInstance();

		Appointment a = new Appointment();
		a.setAppointmentDate(cal.getTime());
		a.setStartTime(cal.getTime());
		cal.add(Calendar.MINUTE,60);
		a.setEndTime(cal.getTime());
		a.setCreateDateTime(new Date());
		a.setCreator("999998");
		a.setDemographicNo(1);
		a.setProviderNo("999998");
		a.setStatus("t");
		a.setUpdateDateTime(new Date());
		dao.persist(a);

		assertNotNull(a.getId());
		assertNotNull(dao.find(a.getId()));

		dao.remove(a.getId());
		assertNull(dao.find(a.getId()));
	}

	@Test
	public void testGetByProviderAndDay() throws Exception {
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appt.setProviderNo("000001");
		appt.setAppointmentDate(new Date());
		dao.persist(appt);

		Integer apptId = appt.getId();
		assertNotNull(apptId);

		List<Appointment> appts = dao.getByProviderAndDay(appt.getAppointmentDate(),appt.getProviderNo());
		assertEquals(appts.size(),1);
		assertEquals(appts.get(0).getId(),apptId);
	}

	@Test
	public void testFindByProviderDayAndStatus() throws Exception {
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appt.setProviderNo("000001");
		appt.setAppointmentDate(new Date());
		appt.setStatus("t");
		dao.persist(appt);

		Integer apptId = appt.getId();
		assertNotNull(apptId);

		List<Appointment> appts = dao.findByProviderDayAndStatus(appt.getProviderNo(),appt.getAppointmentDate(),"t");
		assertEquals(appts.size(),1);
		assertEquals(appts.get(0).getId(),apptId);

	}

	@Test
	public void testFindByDayAndStatus() throws Exception {
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appt.setProviderNo("000001");
		appt.setAppointmentDate(new Date());
		appt.setStatus("t");
		dao.persist(appt);

		Integer apptId = appt.getId();
		assertNotNull(apptId);

		List<Appointment> appts = dao.findByDayAndStatus(appt.getAppointmentDate(),"t");
		assertEquals(appts.size(),1);
		assertEquals(appts.get(0).getId(),apptId);

	}

	@Test
	public void testFind() throws Exception {
		Appointment appt = new Appointment();
		EntityDataGenerator.generateTestDataForModelClass(appt);
		appt.setProviderNo("000001");
		appt.setAppointmentDate(new Date());
		appt.setStatus("t");
		dao.persist(appt);

		Integer apptId = appt.getId();
		assertNotNull(apptId);

		List<Appointment> appts = dao.find(
				appt.getAppointmentDate(),
				appt.getProviderNo(),
				appt.getStartTime(),
				appt.getEndTime(),
				appt.getName(),
				appt.getNotes(),
				appt.getReason(),
				appt.getCreateDateTime(),
				appt.getCreator(),
				appt.getDemographicNo()
				);

		assertEquals(appts.size(),1);
		assertEquals(appts.get(0).getId(),apptId);

	}
}
