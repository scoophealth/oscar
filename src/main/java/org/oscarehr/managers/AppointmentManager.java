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
import java.util.List;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class AppointmentManager {

	@Autowired
	private OscarAppointmentDao appointmentDao;
	
	public List<Object> getAppointmentHistoryWithoutDeleted(Integer demographicNo, Integer offset, Integer limit) {
		List<Object> result = new ArrayList<Object>();
		StringBuilder ids = new StringBuilder();
		
		List<Appointment> nonDeleted = appointmentDao.getAppointmentHistory(demographicNo, offset, limit);
		for(Appointment tmp:nonDeleted) {
			ids.append(tmp.getId() + ",");
		}
		result.addAll(nonDeleted);
		
		
		//--- log action ---
		if (result.size()>0) {
		
			LogAction.addLogSynchronous("AppointmentManager.getAppointmentHistoryWithDeleted", "ids returned=" + ids);
		}
		return result;
	}
	
	public List<Object> getAppointmentHistoryWithDeleted(Integer demographicNo, Integer offset, Integer limit) {
		List<Object> result = new ArrayList<Object>();
		StringBuilder ids = new StringBuilder();
		
		List<Appointment> nonDeleted = appointmentDao.getAppointmentHistory(demographicNo, offset, limit);
		for(Appointment tmp:nonDeleted) {
			ids.append(tmp.getId() + ",");
		}
		result.addAll(nonDeleted);
		
		List<AppointmentArchive> deleted =  appointmentDao.getDeletedAppointmentHistory(demographicNo, offset, limit);
		
		
		for(AppointmentArchive aa:deleted) {
			if(!hasAppointmentNo(result,aa.getAppointmentNo()) && !aaIsAlreadyInList(result,aa)) {
				result.add(aa);
				ids.append(aa.getId() + ",");
			}
		}
		
		//--- log action ---
		if (result.size()>0) {
		
			LogAction.addLogSynchronous("AppointmentManager.getAppointmentHistoryWithDeleted", "ids returned=" + ids);
		}
		return result;
	}
	
	private boolean hasAppointmentNo(List<Object> appts, Integer appointmentNo) {
		for(Object o:appts) {
			if(o instanceof Appointment) {
				Appointment appt = (Appointment)o;
				if(appt.getId().equals(appointmentNo))
					return true;
			}
		}
		return false;
	}
	
	private boolean aaIsAlreadyInList(List<Object> appts, AppointmentArchive aa) {
		for(Object o:appts) {
			if(o instanceof AppointmentArchive) {
				AppointmentArchive appt = (AppointmentArchive)o;
				if(appt.getAppointmentNo().equals(aa.getAppointmentNo()))
					return true;
			}
		}
		return false;
	}
}
