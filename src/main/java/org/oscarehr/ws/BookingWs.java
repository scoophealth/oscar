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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;
import org.oscarehr.appointment.search.AppointmentType;
import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.appointment.search.BookingError;
import org.oscarehr.appointment.search.BookingType;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Appointment.BookingSource;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.AppointmentSearchManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.ScheduleManager;

import org.oscarehr.appointment.search.AppointmentOptionTransfer;
import org.oscarehr.appointment.search.AppointmentResults;
import org.oscarehr.appointment.search.AppointmentConfirmationTransfer;
import org.oscarehr.util.MiscUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.dms.EDocUtil;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class BookingWs extends AbstractWs {
	private static final Logger logger=MiscUtils.getLogger();
	
	@Autowired
	private ScheduleManager scheduleManager;
	
	@Autowired
	private AppointmentSearchManager appointmentSearchManager;
	
	@Autowired
	private DemographicManager demographicManager;
	
	
	//public ScheduleTemplateCodeTransfer[] getScheduleTemplateCodes() {
	//	List<ScheduleTemplateCode> scheduleTemplateCodes = scheduleManager.getScheduleTemplateCodes();
	//	return (ScheduleTemplateCodeTransfer.toTransfer(scheduleTemplateCodes));
	//}

	//getAppointmentTypes
	
	public BookingType[] getAppointmentTypesByProvider(String providerNo) {
		SearchConfig config = appointmentSearchManager.getProviderSearchConfig(this.getLoggedInInfo().getLoggedInProviderNo());
		List<AppointmentType> list = appointmentSearchManager.getAppointmentTypes(config, providerNo);
		
		BookingType[] result = new BookingType[list.size()];
		for (int i = 0; i < list.size(); i++) {
			AppointmentType appointmentType = list.get(i);
			result[i] = config.getBookingType(appointmentType, this.getLoggedInInfo().getLoggedInProviderNo());
		}
		return result;
	}
	
	public BookingType[] getExternalAppointmentTypes(Integer demographicNo) {

		SearchConfig config = appointmentSearchManager.getProviderSearchConfig(this.getLoggedInInfo().getLoggedInProviderNo());
		if(config == null) return null;
		
		List<AppointmentType> list = appointmentSearchManager.getAppointmentTypes(config, demographicNo);
		
		BookingType[] result = new BookingType[list.size()];
		for (int i = 0; i < list.size(); i++) {
			AppointmentType appointmentType = list.get(i);
			result[i] = config.getBookingType(appointmentType, this.getLoggedInInfo().getLoggedInProviderNo());
		}
		return result;
	}
	
	//findAppointment
	public AppointmentResults findAppointment(Integer demographicNo,String appointmentTypeStr,Calendar startDate) {
		String providerNo = this.getLoggedInInfo().getLoggedInProviderNo();
		SearchConfig config = appointmentSearchManager.getProviderSearchConfig(providerNo);
		AppointmentResults appointmentResults = null;
		Long appointmentTypeId = config.getAppointmentTypeId(appointmentTypeStr);
		if(appointmentTypeId == null) { // if this is null the appt type was corrupt and shouldn't be trusted
			logger.error("ERROR: Appointment Type was not parsed returning System Error");
			appointmentResults = new AppointmentResults();
			appointmentResults.setBookingError(new BookingError("ERROR", "System Error"));
			return appointmentResults;
		}
		try {
			List<TimeSlot> timeslots = appointmentSearchManager.findAppointment(this.getLoggedInInfo(),config,  demographicNo, appointmentTypeId, startDate);
			List<AppointmentOptionTransfer> apptList = new ArrayList<AppointmentOptionTransfer>();

			int dayYear = -1;
			Calendar lastDateSearched = null;
			for (TimeSlot app : timeslots) {
				app.setDemographicNo(demographicNo);
				Calendar availTime = app.getAvailableApptTime();
				if (dayYear == -1 || dayYear != availTime.get(Calendar.DAY_OF_YEAR)) {
					dayYear = availTime.get(Calendar.DAY_OF_YEAR);
				}
				String timeDisplay = getTimeDisplayHtmlEscaped(getLoggedInInfo().getLocale(), availTime);
				app.setAppointmentType(appointmentTypeId);
				String encString = config.encrypt(app);
				lastDateSearched = app.getAvailableApptTime();

				AppointmentOptionTransfer appointmentOptionTransfer = new AppointmentOptionTransfer(availTime, timeDisplay,  EDocUtil.getProviderName(app.getProviderNo()), encString);
				apptList.add(appointmentOptionTransfer);
			}

			Calendar nextStartDate = null;
			if (lastDateSearched != null && timeslots.size() > config.getNumberOfAppointmentOptionsToReturn()) {
				nextStartDate = (Calendar) lastDateSearched.clone();
				nextStartDate.add(Calendar.DAY_OF_YEAR, 1);
			} else {
				nextStartDate = (Calendar) startDate.clone();
				nextStartDate.add(Calendar.DAY_OF_YEAR, config.getDaysToSearchAheadLimit());
			}
			String nextStartDateEncrypted = config.encrypt(getJustDateFormat(getLoggedInInfo().getLocale(), nextStartDate));

			appointmentResults = new AppointmentResults(nextStartDateEncrypted, apptList.toArray(new AppointmentOptionTransfer[0]));
		}catch(Exception e) {
			appointmentResults = new AppointmentResults();
			appointmentResults.setBookingError(new BookingError("ERROR", "System Error"));
		}
			return appointmentResults;
	}
	//bookAppointment
	
	public AppointmentConfirmationTransfer bookAppointment(String encryptedAppointmentTimeSlot,String appointmentNotes) {
		String providerNo = this.getLoggedInInfo().getLoggedInProviderNo();
		SearchConfig config = appointmentSearchManager.getProviderSearchConfig(providerNo);
		
		try {

			TimeSlot timeslot = config.decryptTimeSlot(encryptedAppointmentTimeSlot);
			
			String reason = config.getAppointmentType(timeslot.getAppointmentType()).getName();
			String appointmentLocation = config.getAppointmentLocation();
			Calendar endTime = calculateEndTime(config, timeslot.getAvailableApptTime(), timeslot.getProviderNo(), timeslot.getAppointmentType(), timeslot.getCode());
			Appointment appointment = new Appointment();
			
			//appointmentTransfer.copyTo(appointment);
			appointment.setAppointmentDate(timeslot.getAvailableApptTime().getTime());
			appointment.setEndTime(endTime.getTime());
			appointment.setStartTime(timeslot.getAvailableApptTime().getTime());
			appointment.setDemographicNo(timeslot.getDemographicNo());
			
			Demographic demographic = demographicManager.getDemographic(getLoggedInInfo(), timeslot.getDemographicNo());
			
			appointment.setName(demographic.getFormattedName());
			appointment.setNotes(appointmentNotes);
			appointment.setReason(reason);
			appointment.setProviderNo(timeslot.getProviderNo());
			appointment.setType(reason);
			appointment.setStatus("t"); // dunno what that means but seems to be what everyone else uses
			appointment.setBookingSource(BookingSource.MYOSCAR_SELF_BOOKING);
			if (appointmentLocation != null) {
				appointment.setLocation(appointmentLocation);
			}
			
			scheduleManager.addAppointment(getLoggedInInfo(),getLoggedInSecurity(), appointment);
			Integer appointmentId = appointment.getId();
			if(scheduleManager.removeIfDoubleBooked(getLoggedInInfo(), timeslot.getAvailableApptTime(), endTime, providerNo, appointment)) {
				String msg = "Booking conflict, some one else may have taken that time slot just now.";
				logger.debug(msg);
				return new AppointmentConfirmationTransfer(new BookingError("DOUBLE_BOOKED", msg));
			}
			
			//There was a check to see if there was more than one appt book for that day but 		
			/*if(appointmentId == -1){
				String msg = "You may not book more appointments on this day.";
				logger.debug(msg);
				return new AppointmentConfirmationTransfer(new BookingError("LIMIT_EXCEED", msg));
			}*/
			if (appointmentId != null) {
				return new AppointmentConfirmationTransfer(timeslot.getAvailableApptTime(), endTime, getLongExtDateTime(getLoggedInInfo().getLocale(), timeslot.getAvailableApptTime()), EDocUtil.getProviderName( timeslot.getProviderNo()), config.getTitle());
			}
		
		} catch (Exception e) {
			logger.error("Unexpected error", e);
		}
		return new AppointmentConfirmationTransfer(new BookingError("ERROR", "Unable to create appointment"));
		
	}
	
	
	//cancelAppointment
	
	private static Calendar calculateEndTime(SearchConfig clinic, Calendar startTime, String providerId, Long appointmentTypeId, Character code) {
		int durationMinutes = clinic.getAppointmentDuration(providerId, appointmentTypeId, code);

		Calendar endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.MINUTE, durationMinutes);
		endTime.getTimeInMillis();
		return endTime;
	}
	
	
	private static String getTimeDisplayHtmlEscaped(Locale locale, Calendar date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", locale);
		simpleDateFormat.setTimeZone(date.getTimeZone());
		return(simpleDateFormat.format(date.getTime()));
	}
	
	private static String getJustDateFormat(Locale locale, Calendar date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date.getTime());
	}
	private static String getLongExtDateTime(Locale locale,Calendar date){
		SimpleDateFormat extendDate = new SimpleDateFormat("EEEE, yyyy-MM-dd, hh:mm a", locale);
		extendDate.setTimeZone(date.getTimeZone());
		return extendDate.format(date.getTime());
	}
	
	
	//
	
}
