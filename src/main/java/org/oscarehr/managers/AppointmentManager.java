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
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.AppointmentStatusDao;
import org.oscarehr.common.dao.LookupListDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class AppointmentManager {

	protected Logger logger = MiscUtils.getLogger();

	@Autowired
	private OscarAppointmentDao appointmentDao;
	@Autowired
	private AppointmentStatusDao appointmentStatusDao;
	@Autowired
	private LookupListDao lookupListDao;
	@Autowired
	private SecurityInfoManager securityInfoManager;
	@Autowired
	private AppointmentArchiveDao appointmentArchiveDao;
	

	public List<Appointment> getAppointmentHistoryWithoutDeleted(LoggedInInfo loggedInInfo, Integer demographicNo, Integer offset, Integer limit) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		List<Appointment> result = new ArrayList<Appointment>();
		StringBuilder ids = new StringBuilder();

		List<Appointment> nonDeleted = appointmentDao.getAppointmentHistory(demographicNo, offset, limit);
		for (Appointment tmp : nonDeleted) {
			ids.append(tmp.getId() + ",");
		}
		result.addAll(nonDeleted);

		//--- log action ---
		if (result.size() > 0) {

			LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointmentHistoryWithDeleted", "ids returned=" + ids);
		}
		return result;
	}

	public List<Object> getAppointmentHistoryWithDeleted(LoggedInInfo loggedInInfo, Integer demographicNo, Integer offset, Integer limit) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		List<Object> result = new ArrayList<Object>();
		StringBuilder ids = new StringBuilder();

		List<Appointment> nonDeleted = appointmentDao.getAppointmentHistory(demographicNo, offset, limit);
		for (Appointment tmp : nonDeleted) {
			ids.append(tmp.getId() + ",");
		}
		result.addAll(nonDeleted);

		List<AppointmentArchive> deleted = appointmentDao.getDeletedAppointmentHistory(demographicNo, offset, limit);

		for (AppointmentArchive aa : deleted) {
			if (!hasAppointmentNo(result, aa.getAppointmentNo()) && !aaIsAlreadyInList(result, aa)) {
				result.add(aa);
				ids.append(aa.getId() + ",");
			}
		}

		//--- log action ---
		if (result.size() > 0) {

			LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointmentHistoryWithDeleted", "ids returned=" + ids);
		}
		return result;
	}

	private boolean hasAppointmentNo(List<Object> appts, Integer appointmentNo) {
		for (Object o : appts) {
			if (o instanceof Appointment) {
				Appointment appt = (Appointment) o;
				if (appt.getId().equals(appointmentNo)) return true;
			}
		}
		return false;
	}

	private boolean aaIsAlreadyInList(List<Object> appts, AppointmentArchive aa) {
		for (Object o : appts) {
			if (o instanceof AppointmentArchive) {
				AppointmentArchive appt = (AppointmentArchive) o;
				if (appt.getAppointmentNo().equals(aa.getAppointmentNo())) return true;
			}
		}
		return false;
	}

	/**
	 * Returns appointment for display.
	 * 
	
	 * @param appointment				appointment data
	 * @param loggedInInfo
	 * @return							appointment data
	 * 
	 */
	public void addAppointment(LoggedInInfo loggedInInfo, Appointment appointment) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		appointmentDao.persist(appointment);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.saveAppointment", "id=" + appointment.getId());
	}

	public void updateAppointment(LoggedInInfo loggedInInfo, Appointment appointment) {
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "w", null)) {
			throw new RuntimeException("Access Denied");
		}

		Appointment existing = appointmentDao.find(appointment.getId());
		if(existing != null) {
			appointmentArchiveDao.archiveAppointment(existing);	
		}
		appointmentDao.merge(appointment);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.updateAppointment", "id=" + appointment.getId());

	}

	public void deleteAppointment(LoggedInInfo loggedInInfo, int apptNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "d", null)) {
			throw new RuntimeException("Access Denied");
		}
		Appointment existing = appointmentDao.find(apptNo);
		if(existing != null) {
			appointmentArchiveDao.archiveAppointment(existing);	
		}
		
		appointmentDao.remove(apptNo);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.deleteAppointment", "id=" + apptNo);

	}

	public Appointment getAppointment(LoggedInInfo loggedInInfo, int apptNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		

		Appointment appt = appointmentDao.find(apptNo);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.getAppointment", "id=" + apptNo);

		return appt;
	}

	public Appointment updateAppointmentStatus(LoggedInInfo loggedInInfo, int apptNo, String status) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appointment", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		Appointment appt = appointmentDao.find(apptNo);
		if (appt != null) {
			appointmentArchiveDao.archiveAppointment(appt);	
		
			appt.setStatus(status);
		}
		appointmentDao.merge(appt);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.updateAppointmentStatus", "id=" + appt.getId());

		return appt;

	}

	
	public Appointment updateAppointmentType(LoggedInInfo loggedInInfo, int apptNo, String type) {

		Appointment appt = appointmentDao.find(apptNo);
		if (appt != null) {
			appointmentArchiveDao.archiveAppointment(appt);	
			
			appt.setType(type);
		}
		appointmentDao.merge(appt);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.updateAppointmentType", "id=" + appt.getId());

		return appt;

	}

	public Appointment updateAppointmentUrgency(LoggedInInfo loggedInInfo, int apptNo, String urgency) {

		Appointment appt = appointmentDao.find(apptNo);
		if (appt != null) {
			appointmentArchiveDao.archiveAppointment(appt);	
			
			appt.setUrgency(urgency);
		}
		appointmentDao.merge(appt);

		LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.updateAppointmentUrgency", "id=" + appt.getId());

		return appt;

	}

	
	
	public List<AppointmentStatus> getAppointmentStatuses() {

		List<AppointmentStatus> apptStatus = appointmentStatusDao.findAll();

		return apptStatus;
	}

	public List<LookupListItem> getReasons() {

		List<LookupListItem> itemsList = new ArrayList<LookupListItem>();

		LookupList list = lookupListDao.findByName("reasonCode");
		if(list != null) {
			itemsList = list.getItems();
		}

		return itemsList;
	}

	public List<Appointment> findMonthlyAppointments(LoggedInInfo loggedInInfo, String providerNo, int year, int month) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		
		Date startDate = cal.getTime();
		
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH)+1);
		cal.add(Calendar.MINUTE,-1);
		
		Date endDate = cal.getTime();
		
		logger.info("monthly - checking from " + startDate + " to " + endDate);
		
		
		List<Appointment> results = appointmentDao.findByDateRangeAndProvider(startDate, endDate, providerNo);
		
		if (results.size() > 0) {
			LogAction.addLogSynchronous(loggedInInfo, "AppointmentManager.findMonthlyAppointments", "");
		}
		
		return results;
	}
	
}
