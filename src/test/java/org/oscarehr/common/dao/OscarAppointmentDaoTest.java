package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.SpringUtils;


public class OscarAppointmentDaoTest extends DaoTestFixtures {

	@Before
	public void before() throws Exception {
		//nothing here yet. should clean up the appointment table to a known state
	}
	
	
	@Test
	public void test() {
		OscarAppointmentDao appointmentDao=(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
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
		appointmentDao.persist(a);
		
		assertNotNull(a.getId());
		assertNotNull(appointmentDao.find(a.getId()));
		
		appointmentDao.remove(a.getId());
		assertNull(appointmentDao.find(a.getId()));
	}
}
