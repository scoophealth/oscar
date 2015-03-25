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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.AppointmentType;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.managers.DayWorkSchedule;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.transfer_objects.AppointmentArchiveTransfer;
import org.oscarehr.ws.transfer_objects.AppointmentTransfer;
import org.oscarehr.ws.transfer_objects.AppointmentTypeTransfer;
import org.oscarehr.ws.transfer_objects.DayWorkScheduleTransfer;
import org.oscarehr.ws.transfer_objects.ScheduleTemplateCodeTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class ScheduleWs extends AbstractWs {
	private static final Logger logger=MiscUtils.getLogger();
	
	@Autowired
	private ScheduleManager scheduleManager;

	public ScheduleTemplateCodeTransfer[] getScheduleTemplateCodes() {
		List<ScheduleTemplateCode> scheduleTemplateCodes = scheduleManager.getScheduleTemplateCodes();
		return (ScheduleTemplateCodeTransfer.toTransfer(scheduleTemplateCodes));
	}

	/**
	 * @deprecated you should use the method with the useGMTTime option
	 */
	public AppointmentTransfer getAppointment(Integer appointmentId) {
		Appointment appointment = scheduleManager.getAppointment(getLoggedInInfo(),appointmentId);
		return (AppointmentTransfer.toTransfer(appointment, false));
	}

	/**
	 * @deprecated you should use the method with the useGMTTime option
	 */
	public AppointmentTransfer[] getAppointmentsForProvider(String providerNo, Calendar date) {
		List<Appointment> appointments = scheduleManager.getDayAppointments(getLoggedInInfo(),providerNo, date);
		return (AppointmentTransfer.toTransfers(appointments, false));
	}

	/**
	 * @deprecated you should use the method with the useGMTTime option
	 */
	public AppointmentTransfer[] getAppointmentsForPatient(Integer demographicId, int startIndex, int itemsToReturn) {
		List<Appointment> appointments = scheduleManager.getAppointmentsForPatient(getLoggedInInfo(),demographicId, startIndex, itemsToReturn);
		return (AppointmentTransfer.toTransfers(appointments, false));
	}

	public AppointmentTransfer getAppointment2(Integer appointmentId, boolean useGMTTime) {
		Appointment appointment = scheduleManager.getAppointment(getLoggedInInfo(),appointmentId);
		return (AppointmentTransfer.toTransfer(appointment, useGMTTime));
	}

	public AppointmentTransfer[] getAppointmentsForProvider2(String providerNo, Calendar date, boolean useGMTTime) {
		List<Appointment> appointments = scheduleManager.getDayAppointments(getLoggedInInfo(),providerNo, date);
		return (AppointmentTransfer.toTransfers(appointments, useGMTTime));
	}

	public AppointmentTransfer[] getAppointmentsForPatient2(Integer demographicId, int startIndex, int itemsToReturn, boolean useGMTTime) {
		List<Appointment> appointments = scheduleManager.getAppointmentsForPatient(getLoggedInInfo(),demographicId, startIndex, itemsToReturn);
		return (AppointmentTransfer.toTransfers(appointments, useGMTTime));
	}
	
	public DayWorkScheduleTransfer getDayWorkSchedule(String providerNo, Calendar date) {
		DayWorkSchedule dayWorkSchedule = scheduleManager.getDayWorkSchedule(providerNo, date);
		if (dayWorkSchedule == null) return (null);
		else return (DayWorkScheduleTransfer.toTransfer(dayWorkSchedule));
	}

	public AppointmentTypeTransfer[] getAppointmentTypes() {
		List<AppointmentType> appointmentTypes = scheduleManager.getAppointmentTypes();
		return (AppointmentTypeTransfer.toTransfer(appointmentTypes));
	}

	/**
	 * @return the ID of the appointment just added
	 */
	public Integer addAppointment(AppointmentTransfer appointmentTransfer) {
		Appointment appointment = new Appointment();
		appointmentTransfer.copyTo(appointment);
		scheduleManager.addAppointment(getLoggedInInfo(),getLoggedInSecurity(), appointment);
		return (appointment.getId());
	}

	public void updateAppointment(AppointmentTransfer appointmentTransfer) {
		Appointment appointment = scheduleManager.getAppointment(getLoggedInInfo(),appointmentTransfer.getId());

		appointmentTransfer.copyTo(appointment);
		scheduleManager.updateAppointment(getLoggedInInfo(),appointment);
	}

	/**
	 * @deprecated you should use the method with the useGMTTime option
	 */
	public AppointmentTransfer[] getAppointmentsForDateRangeAndProvider(Date startTime, Date endTime, String providerNo) {
		List<Appointment> appointments = scheduleManager.getAppointmentsForDateRangeAndProvider(getLoggedInInfo(),startTime, endTime, providerNo);
		return (AppointmentTransfer.toTransfers(appointments, false));
	}

	public AppointmentTransfer[] getAppointmentsForDateRangeAndProvider2(Date startTime, Date endTime, String providerNo, boolean useGMTTime) {
		List<Appointment> appointments = scheduleManager.getAppointmentsForDateRangeAndProvider(getLoggedInInfo(),startTime, endTime, providerNo);
		return (AppointmentTransfer.toTransfers(appointments, useGMTTime));
	}

	public AppointmentTransfer[] getAppointmentsUpdatedAfterDate(Date updatedAfterThisDateExclusive, int itemsToReturn, boolean useGMTTime) {
		List<Appointment> appointments=scheduleManager.getAppointmentUpdatedAfterDate(getLoggedInInfo(),updatedAfterThisDateExclusive, itemsToReturn);
		return(AppointmentTransfer.toTransfers(appointments, useGMTTime));
	}

	public AppointmentArchiveTransfer[] getAppointmentArchivesUpdatedAfterDate(Date updatedAfterThisDateExclusive, int itemsToReturn, boolean useGMTTime) {
		List<AppointmentArchive> appointments=scheduleManager.getAppointmentArchiveUpdatedAfterDate(getLoggedInInfo(),updatedAfterThisDateExclusive, itemsToReturn);
		return(AppointmentArchiveTransfer.toTransfers(appointments, useGMTTime));
	}

	public AppointmentTransfer[] getAppointmentsByProgramProviderDemographicDate(Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn, boolean useGMTTime) {
		List<Appointment> appointments = scheduleManager.getAppointmentsByProgramProviderDemographicDate(getLoggedInInfo(),programId, providerNo, demographicId, updatedAfterThisDateExclusive, itemsToReturn);
		return (AppointmentTransfer.toTransfers(appointments, useGMTTime));
	}
	
	/**
	 * This method is a helper method to help people code and test their clients against time zone differences.
	 * We will not support revisioning for this method, if / when we want to change this, we will.
	 */
	public Calendar testTimeZone_1492_05_12_18_26_32(boolean useGMTTime)
	{
		Calendar cal = new GregorianCalendar(1492, 05, 12, 18, 26, 32);
		cal=AppointmentTransfer.setToGMTIfRequired(cal,useGMTTime);
		
		logger.debug("timeZoneTest sent: "+cal);
		logger.debug("timeZoneTest sent: "+DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(cal));
		
		return(cal);
	}
	
	public Integer[] getAllDemographicIdByProgramProvider(Integer programId, String providerNo) {
		List<Integer> results=scheduleManager.getAllDemographicIdByProgramProvider(getLoggedInInfo(), programId, providerNo);
		return(results.toArray(new Integer[0]));
	}
}
