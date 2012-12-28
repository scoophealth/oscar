/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.appt;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.RScheduleDao;
import org.oscarehr.common.dao.ScheduleDateDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.RSchedule;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class JdbcApptImpl {
	private static final Logger _logger = Logger.getLogger(JdbcApptImpl.class);
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");

	public boolean deleteAppt(String apptNo) {
                Appointment appt = appointmentDao.find(Integer.parseInt(apptNo));
          	  	appointmentArchiveDao.archiveAppointment(appt);
          	  	int retval=0;
          	  	if(appt != null) {
          	  		appointmentDao.remove(appt.getId());
          	  		retval=1;
          	  	}
		if (retval==1) {
			_logger.error("deleteAppt(id=" + apptNo + ")");
		}
		return (retval==1);
	}

	public String getLocationFromSchedule(String apptDate, String provider_no) {
		String retval = getLocationFromSpec(apptDate, provider_no, "c");
		if (!"".equals(retval)) {
			return retval;
		}
		
		retval = getLocationFromSpec(apptDate, provider_no, "b");
		
		if (!"".equals(retval)) {
			return retval;
		}
		
		RScheduleDao dao = SpringUtils.getBean(RScheduleDao.class); 
		for(RSchedule r : dao.findByProviderNoAndDates(provider_no, ConversionUtils.fromDateString(apptDate))) {
			retval = r.getAvailHour();
		}

		// get weekday number
		String[] temp = { "", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
		String strWeekday = UtilDateUtilities.DateToString(UtilDateUtilities.getDateFromString(apptDate, "yyyy-MM-dd"),
				"EEE");
		int n = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equalsIgnoreCase(strWeekday)) {
				n = i;
				break;
			}
		}

		retval = SxmlMisc.getXmlContent(retval, "A" + n);
		retval = retval == null ? "" : retval;
		return retval;
	}

	// priority = c, reason = location
	private String getLocationFromSpec(String apptDate, String provider_no, String priority) {
		String retval = "";
		
		ScheduleDateDao dao = SpringUtils.getBean(ScheduleDateDao.class);
		for(ScheduleDate s : dao.findByProviderStartDateAndPriority(provider_no, ConversionUtils.fromDateString(apptDate), priority)) {
			retval = s.getReason();
		}
	
		retval = retval == null ? "" : retval;
		return retval;
	}

	public String getPrevApptDate(String thisServiceDate) {
		String retval = "";
		
		OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);
		Appointment a = dao.findByDate(ConversionUtils.fromDateString(thisServiceDate));
		
		if (a != null) {
			retval = ConversionUtils.toDateString(a.getAppointmentDate());
		}

		return retval;
	}

}
