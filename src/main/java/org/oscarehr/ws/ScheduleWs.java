package org.oscarehr.ws;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentType;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.managers.DayWorkSchedule;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.ws.transfer_objects.AppointmentTransfer;
import org.oscarehr.ws.transfer_objects.AppointmentTypeTransfer;
import org.oscarehr.ws.transfer_objects.DayWorkScheduleTransfer;
import org.oscarehr.ws.transfer_objects.ScheduleTemplateCodeTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class ScheduleWs extends AbstractWs {
	@Autowired
	private ScheduleManager scheduleManager;
	
	public ScheduleTemplateCodeTransfer[] getScheduleTemplateCodes()
	{
		List<ScheduleTemplateCode> scheduleTemplateCodes=scheduleManager.getScheduleTemplateCodes();
		return(ScheduleTemplateCodeTransfer.toTransfer(scheduleTemplateCodes));
	}

	public AppointmentTransfer[] getAppointments(String providerNo, Calendar date)
	{
		List<Appointment> appointments=scheduleManager.getDayAppointments(providerNo, date);
		return(AppointmentTransfer.toTransfer(appointments));
	}
	
	public DayWorkScheduleTransfer getDayWorkSchedule(String providerNo, Calendar date)
	{
		DayWorkSchedule dayWorkSchedule=scheduleManager.getDayWorkSchedule(providerNo, date);
		if (dayWorkSchedule==null) return(null);
		else return(DayWorkScheduleTransfer.toTransfer(dayWorkSchedule));
	}
	
	public AppointmentTypeTransfer[] getAppointmentTypes()
	{
		List<AppointmentType> appointmentTypes=scheduleManager.getAppointmentTypes();
		return(AppointmentTypeTransfer.toTransfer(appointmentTypes));
	}
}
