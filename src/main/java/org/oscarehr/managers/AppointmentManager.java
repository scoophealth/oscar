package org.oscarehr.managers;

import java.util.Calendar;
import java.util.List;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.ProviderPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.appt.status.model.AppointmentStatus;
import oscar.log.LogAction;

@Service
public class AppointmentManager {

	@Autowired
	private ProviderPreferenceDao providerPreferenceDao;

	@Autowired
	private OscarAppointmentDao oscarAppointmentDao;

	public DaySchedule getDaySchedule(String providerNo, Calendar date) {

		DaySchedule daySchedule = new DaySchedule();

		ProviderPreference providerPreference = providerPreferenceDao.find(providerNo);
		if (providerPreference != null) {
			Calendar cal = (Calendar) date.clone();
			cal.set(Calendar.HOUR_OF_DAY, providerPreference.getStartHour());
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.getTime();
			daySchedule.setStartTime(cal);

			cal = (Calendar) cal.clone();
			cal.set(Calendar.HOUR_OF_DAY, providerPreference.getEndHour());
			cal.getTime();
			daySchedule.setEndTime(cal);

			daySchedule.setTimeSlotDurationMin(providerPreference.getEveryMin());
		}

		List<Appointment> appointments = oscarAppointmentDao.findByProviderAndDayandNotStatus(providerNo, date.getTime(), AppointmentStatus.APPOINTMENT_STATUS_CANCELLED);
		daySchedule.setAppointments(appointments);

		//--- log action ---
		LogAction.addLogSynchronous("AppointmentManager.getDaySchedule", "providerNo="+providerNo+", date="+date);
		
		return (daySchedule);
	}
}
