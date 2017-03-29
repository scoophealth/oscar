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

package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.AppointmentStatusDao;
import org.oscarehr.common.dao.AppointmentTypeDao;
import org.oscarehr.common.dao.MyGroupDao;
import org.oscarehr.common.dao.MyGroupProgramDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ScheduleDateDao;
import org.oscarehr.common.dao.ScheduleHolidayDao;
import org.oscarehr.common.dao.ScheduleTemplateCodeDao;
import org.oscarehr.common.dao.ScheduleTemplateDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.AppointmentType;
import org.oscarehr.common.model.MyGroupProgram;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.common.model.ScheduleHoliday;
import org.oscarehr.common.model.ScheduleTemplate;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.common.model.ScheduleTemplatePrimaryKey;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class ScheduleManager {

	private static Logger logger = MiscUtils.getLogger();

	@Autowired
	private OscarAppointmentDao oscarAppointmentDao;

	@Autowired
	private AppointmentArchiveDao appointmentArchiveDao;

	@Autowired
	private ScheduleHolidayDao scheduleHolidayDao;

	@Autowired
	private ScheduleDateDao scheduleDateDao;

	@Autowired
	private ScheduleTemplateDao scheduleTemplateDao;

	@Autowired
	private ScheduleTemplateCodeDao scheduleTemplateCodeDao;

	@Autowired
	private AppointmentTypeDao appointmentTypeDao;

	@Autowired
	private AppointmentStatusDao appointmentStatusDao;
	
	@Autowired
	private MyGroupDao myGroupDao;
	
	@Autowired
	private MyGroupProgramDao myGroupProgramDao;
	

	/*Right now the date object passed is converted to a local time.  
	*
	* As in, if the server's timezone is set to EST and the method is called with two data objects set to
	*
	* 2011-11-11 2:01 TZ america/new york
	* 2011-11-10 23:01 TZ america/los angeles
	* 
	* They will both return the DayWorkSchedule for November 11 2011;
	* 
	* The DayWorkSchedule returned will be in the server's local timezone.
	*
	*/
	public DayWorkSchedule getDayWorkSchedule(String providerNo, Calendar date) {
		// algorithm
		//----------
		// select entries from scheduledate for the given day/provider where status = 'A' (for active?)
		// "hour" setting is the template to apply, i.e. template name
		// select entry from scheduletemplate to get the template to apply for the given day
		// timecode is a breakdown of the day into equal slots, where _ means nothing and some letter means a code in scheduletemplatecode
		// The only way to know the duration of the time code is to divide it up, i.e. minutes_per_day/timecode.length, i.e. 1440 minutes per second / 96 length = 15 minutes per slot.
		// For each time slot, then look up the scheduletemplatecode

		DayWorkSchedule dayWorkSchedule = new DayWorkSchedule();

		ScheduleHoliday scheduleHoliday = scheduleHolidayDao.find(date.getTime());
		dayWorkSchedule.setHoliday(scheduleHoliday != null);

		ScheduleDate scheduleDate = scheduleDateDao.findByProviderNoAndDate(providerNo, date.getTime());
		if (scheduleDate == null) {
			logger.debug("No scheduledate for date requested. providerNo=" + providerNo + ", date=" + date.getTime());
			return (null);
		}
		String scheduleTemplateName = scheduleDate.getHour();

		// okay this is a mess, the ScheduleTemplate is messed up because no one links there via a PK, they only link there via the name column
		// and the name column isn't unique... so... we will have to do a search for the right template.
		// first we'll check under the providersId, if not we'll check under the public id.
		ScheduleTemplatePrimaryKey scheduleTemplatePrimaryKey = new ScheduleTemplatePrimaryKey(providerNo, scheduleTemplateName);
		ScheduleTemplate scheduleTemplate = scheduleTemplateDao.find(scheduleTemplatePrimaryKey);
		if (scheduleTemplate == null) {
			scheduleTemplatePrimaryKey = new ScheduleTemplatePrimaryKey(ScheduleTemplatePrimaryKey.DODGY_FAKE_PROVIDER_NO_USED_TO_HOLD_PUBLIC_TEMPLATES, scheduleTemplateName);
			scheduleTemplate = scheduleTemplateDao.find(scheduleTemplatePrimaryKey);
		}

		//  if it's still null, then ignore it as there's no schedule for the day.
		if (scheduleTemplate != null) {
			// time interval
			String timecode = scheduleTemplate.getTimecode();
			int timeSlotDuration = (60 * 24) / timecode.length();
			dayWorkSchedule.setTimeSlotDurationMin(timeSlotDuration);

			// sort out designated timeslots and their purpose
			Calendar timeSlot = (Calendar) date.clone();

			//make sure the appts returned are in local time. 
			timeSlot.setTimeZone(Calendar.getInstance().getTimeZone());
			timeSlot = DateUtils.truncate(timeSlot, Calendar.DAY_OF_MONTH);
			TreeMap<Calendar, Character> allTimeSlots = dayWorkSchedule.getTimeSlots();

			for (int i = 0; i < timecode.length(); i++) {
				// ignore _ because that's a blank place holder identifier... also not my fault, just processing what's been already written.
				if ('_' != timecode.charAt(i)) {
					allTimeSlots.put((Calendar) timeSlot.clone(), timecode.charAt(i));
				}

				timeSlot.add(GregorianCalendar.MINUTE, timeSlotDuration);
			}
		}

		// This method will not log access as the schedule is not private medical data.
		return (dayWorkSchedule);
	}

	public List<Appointment> getDayAppointments(LoggedInInfo loggedInInfo, String providerNo, Date date) {
		List<Appointment> appointments = oscarAppointmentDao.findByProviderAndDayandNotStatus(providerNo, date, AppointmentStatus.APPOINTMENT_STATUS_CANCELLED);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getDayAppointments", "appointments for providerNo=" + providerNo + ", appointments for date=" + date);

		return (appointments);
	}

	public List<Appointment> getDayAppointments(LoggedInInfo loggedInInfo, String providerNo, Calendar date) {
		return getDayAppointments(loggedInInfo, providerNo, date.getTime());
	}

	public List<ScheduleTemplateCode> getScheduleTemplateCodes() {
		List<ScheduleTemplateCode> scheduleTemplateCodes = scheduleTemplateCodeDao.findAll();

		// This method will not log access as the codes are not private medical data.
		return (scheduleTemplateCodes);
	}

	public List<AppointmentType> getAppointmentTypes() {
		List<AppointmentType> appointmentTypes = appointmentTypeDao.listAll();

		// This method will not log access as the appointment types are not private medical data.
		return (appointmentTypes);
	}

	public void addAppointment(LoggedInInfo loggedInInfo, Security security, Appointment appointment) {
		appointment.setCreatorSecurityId(security.getSecurityNo());
		appointment.setCreator(security.getUserName());

		oscarAppointmentDao.persist(appointment);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.addAppointment", appointment.toString());
	}

	public List<Appointment> getAppointmentsForPatient(LoggedInInfo loggedInInfo, Integer demographicId, int startIndex, int itemsToReturn) {
		List<Appointment> results = oscarAppointmentDao.findByDemographicId(demographicId, startIndex, itemsToReturn);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointmentsForPatient", "appointments for demographicId=" + demographicId + ", startIndex=" + startIndex + ", itemsToReturn=" + itemsToReturn);

		return (results);
	}

	public List<Appointment> getAppointmentsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Appointment> results = oscarAppointmentDao.findByProgramProviderDemographicDate(programId, providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointmentsByProgramProviderPatientDate", "appointments for programId="+programId+", providerNo="+providerNo+", demographicId=" + demographicId + ", updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive.getTime() + ", itemsToReturn=" + itemsToReturn);

		return (results);
	}

	public Appointment getAppointment(LoggedInInfo loggedInInfo, Integer appointmentId) {
		Appointment result = oscarAppointmentDao.find(appointmentId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointment", "appointmentId=" + appointmentId);
		}

		return (result);
	}

	public void updateAppointment(LoggedInInfo loggedInInfo, Appointment appointment) {
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.updateAppointment", "appointmentId=" + appointment.getId());

		// generate archive object
		oscarAppointmentDao.archiveAppointment(appointment.getId());

		// save new changes
		oscarAppointmentDao.merge(appointment);
	}

	public List<Appointment> getAppointmentsForDateRangeAndProvider(LoggedInInfo loggedInInfo, Date startTime, Date endTime, String providerNo) {
		List<Appointment> appointments = oscarAppointmentDao.findByDateRangeAndProvider(startTime, endTime, providerNo);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointmentsForDateRangeAndProvider", "appointments for providerNo=" + providerNo + ", appointments for " + startTime + " to: " + endTime);

		return (appointments);
	}

	public List<Appointment> getAppointmentUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Appointment> results = oscarAppointmentDao.findByUpdateDate(updatedAfterThisDateExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "ScheduleManager.getAppointmentUpdatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public List<AppointmentArchive> getAppointmentArchiveUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<AppointmentArchive> results = appointmentArchiveDao.findByUpdateDate(updatedAfterThisDateExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "ScheduleManager.getAppointmentArchiveUpdatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public List<AppointmentStatus> getAppointmentStatuses(LoggedInInfo loggedInInfo) {
		List<AppointmentStatus> results = appointmentStatusDao.findAll(0, 100);

		if (results.size() >= 100) {
			logger.error("We reached a hard coded limit, why >100 statuses?");
		}

		LogAction.addLogSynchronous(loggedInInfo, "ScheduleManager.getAppointmentStatuses", null);

		return (results);
	}
	
	public List<Integer> getAllDemographicIdByProgramProvider(LoggedInInfo loggedInInfo, Integer programId, String providerNo) {
		List<Integer> results = oscarAppointmentDao.findAllDemographicIdByProgramProvider(programId, providerNo);

		LogAction.addLogSynchronous(loggedInInfo, "ScheduleManager.getAllDemographicIdByProgramProvider", "programId=" + programId+", providerNo="+providerNo);

		return (results);
	}
	
	public List<String> getMyGroups() {
		return myGroupDao.getGroups();
	}
	
	public List<String> getMyGroupsByProgramNo(Integer programNo) {
		List<MyGroupProgram> gpList =  myGroupProgramDao.findByProgramNo(programNo);
		List<String> groups = new ArrayList<String>();
		
		for(MyGroupProgram gp:gpList) {
			groups.add(gp.getMyGroupNo());
		}
		
		return groups;
	}
	
	public void replaceMyGroupProgram(Integer programNo, String[] groups) {
		myGroupProgramDao.removeByProgramNo(programNo);
		
		if(groups != null) {
			for(String g:groups) {
				MyGroupProgram e = new MyGroupProgram();
				e.setMyGroupNo(g);
				e.setProgramNo(programNo);
				myGroupProgramDao.persist(e);
			}
		}
	}
	
	public List<String> getMyGroups(LoggedInInfo loggedInInfo, List<Integer> programDomain) {
		
		List<String> groups =  myGroupProgramDao.findByProgramNos(programDomain);
		
		
		return groups;
	}
}
