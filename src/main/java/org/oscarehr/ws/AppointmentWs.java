package org.oscarehr.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jws.WebService;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.managers.AppointmentManager;
import org.oscarehr.managers.DaySchedule;
import org.oscarehr.ws.transfer_objects.ProviderAppointmentAvailabilityTransfer;
import org.oscarehr.ws.transfer_objects.TimeBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class AppointmentWs extends AbstractWs {
	@Autowired
	private AppointmentManager appointmentManager;

	public ProviderAppointmentAvailabilityTransfer getProviderAppointmentAvailability(String providerNo, Calendar date) {
		DaySchedule daySchedule = appointmentManager.getDaySchedule(providerNo, date);

		ProviderAppointmentAvailabilityTransfer result = new ProviderAppointmentAvailabilityTransfer();
		result.setEndTime(daySchedule.getEndTime());
		result.setProviderNo(providerNo);
		result.setStartTime(daySchedule.getStartTime());
		result.setTimeSlotDurationMin(daySchedule.getTimeSlotDurationMin());

		ArrayList<TimeBlock> timeBlocks = new ArrayList<TimeBlock>();
		for (Appointment appointment : daySchedule.getAppointments()) {
			TimeBlock timeBlock = new TimeBlock();

			//--- sort out start time ---
			Calendar cal = new GregorianCalendar();
			cal.setTime(appointment.getAppointmentDate());

			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(appointment.getStartTime());

			cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));
			cal.getTimeInMillis();

			timeBlock.setStartTime(cal);

			//--- sort out end time ---
			cal = new GregorianCalendar();
			cal.setTime(appointment.getAppointmentDate());

			cal2 = new GregorianCalendar();
			cal2.setTime(appointment.getEndTime());

			cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));
			cal.getTimeInMillis();

			timeBlock.setEndTime(cal);

			//--- add to list ---
			timeBlocks.add(timeBlock);
		}

		result.setUnavailableTimes(timeBlocks.toArray(new TimeBlock[0]));

		return (result);
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
