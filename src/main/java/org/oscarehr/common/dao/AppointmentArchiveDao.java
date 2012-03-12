package org.oscarehr.common.dao;

import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentArchiveDao extends AbstractDao<AppointmentArchive> {

	public AppointmentArchiveDao() {
		super(AppointmentArchive.class);
	}

	public AppointmentArchive archiveAppointment(Appointment appointment) {
		AppointmentArchive aa = new AppointmentArchive();
		BeanUtils.copyProperties(appointment, aa, new String[]{"id"});
		aa.setAppointmentNo(appointment.getId());
		persist(aa);
		return aa;
	}
}
