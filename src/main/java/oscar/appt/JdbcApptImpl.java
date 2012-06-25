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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.oscarBilling.ca.on.data.BillingONDataHelp;
import oscar.service.OscarSuperManager;
import oscar.util.UtilDateUtilities;

public class JdbcApptImpl {
	private static final Logger _logger = Logger.getLogger(JdbcApptImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


	public boolean deleteAppt(String apptNo) {
                OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");
                Appointment appt = appointmentDao.find(Integer.parseInt(apptNo));
          	  	appointmentArchiveDao.archiveAppointment(appt);
                int retval = oscarSuperManager.update("appointmentDao", "delete", new String[]{apptNo});
		if (retval==1) {
			_logger.error("deleteAppt(id=" + apptNo + ")");
		}
		return (retval==1);
	}

	public String getLocationFromSchedule(String apptDate, String provider_no) {
		String retval = getLocationFromSpec(apptDate, provider_no, "c");
		if (!"".equals(retval))
			return retval;
		retval = getLocationFromSpec(apptDate, provider_no, "b");
		if (!"".equals(retval))
			return retval;

		String sql = "select avail_hour from rschedule where provider_no='" + provider_no + "' ";
		sql += " and sdate<='" + apptDate + "' and edate>='" + apptDate + "'";
		// _logger.info("getLocationFromSchedule(sql = " + sql + ")");

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"avail_hour");
			}
		} catch (SQLException e) {
			_logger.error("getLocationFromSchedule(sql = " + sql + ")");
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
		String sql = "select reason from scheduledate where sdate='" + apptDate + "' and provider_no='" + provider_no
				+ "' and priority='" + priority + "'";
		// _logger.info("getLocationFromSpec(sql = " + sql + ")");

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"reason");
			}
		} catch (SQLException e) {
			_logger.error("getLocationFromSpec(sql = " + sql + ")");
		}

		retval = retval == null ? "" : retval;
		return retval;
	}

	public String getPrevApptDate(String thisServiceDate) {
		String retval = "";
		String sql = "select appointment_date from appointment where appointment_date<'" + thisServiceDate + "' ";
		sql += " order by appointment_date desc limit 1";

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"appointment_date");
			}
		} catch (SQLException e) {
			_logger.error("getPrevApptDate(sql = " + sql + ")");
		}

		return retval;
	}

}
