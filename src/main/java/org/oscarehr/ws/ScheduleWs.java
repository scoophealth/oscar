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


package org.oscarehr.ws;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
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
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class ScheduleWs extends AbstractWs {
	@Autowired
	private ScheduleManager scheduleManager;
	
	public ScheduleTemplateCodeTransfer[] getScheduleTemplateCodes()
	{
		List<ScheduleTemplateCode> scheduleTemplateCodes=scheduleManager.getScheduleTemplateCodes();
		return(ScheduleTemplateCodeTransfer.toTransfer(scheduleTemplateCodes));
	}

	public AppointmentTransfer getAppointment(Integer appointmentId)
	{
		Appointment appointment=scheduleManager.getAppointment(appointmentId);
		return(AppointmentTransfer.toTransfer(appointment));
	}

	public AppointmentTransfer[] getAppointmentsForProvider(String providerNo, Calendar date)
	{
		List<Appointment> appointments=scheduleManager.getDayAppointments(providerNo, date);
		return(AppointmentTransfer.toTransfer(appointments));
	}
	
	public AppointmentTransfer[] getAppointmentsForPatient(Integer demographicId, int startIndex, int itemsToReturn)
	{
		List<Appointment> appointments=scheduleManager.getAppointmentsForPatient(demographicId, startIndex, itemsToReturn);
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
	
	/**
	 * @return the ID of the appointment just added
	 */
	public Integer addAppointment(AppointmentTransfer appointmentTransfer)
	{
		Appointment appointment=new Appointment();
		appointmentTransfer.copyTo(appointment);
		scheduleManager.addAppointment(appointment);
		return(appointment.getId());
	}
	
	
	public void updateAppointment(AppointmentTransfer appointmentTransfer)
	{
		Appointment appointment=scheduleManager.getAppointment(appointmentTransfer.getId());
		
		appointmentTransfer.copyTo(appointment);
		scheduleManager.updateAppointment(appointment);
	}
}
