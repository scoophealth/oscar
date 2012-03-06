package org.oscarehr.ws;

import java.util.Calendar;

import javax.jws.WebService;

import org.oscarehr.managers.AppointmentManager;
import org.oscarehr.managers.DaySchedule;
import org.oscarehr.ws.transfer_objects.DayScheduleTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class AppointmentWs extends AbstractWs {
	@Autowired
	private AppointmentManager appointmentManager;
	
	public DayScheduleTransfer getDaySchedule(String providerNo, Calendar date)
	{
		DaySchedule daySchedule = appointmentManager.getDaySchedule(providerNo, date);
		DayScheduleTransfer dayScheduleTransfer=DayScheduleTransfer.toTransfer(daySchedule);
		return(dayScheduleTransfer);
	}
}
