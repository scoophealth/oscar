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

package org.oscarehr.util;

import java.util.Date;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;

import oscar.util.ConversionUtils;

public class AppointmentUtil {

	private static final String NONE = "(none)";

	private AppointmentUtil() {
	}

	public static String getNextAppointment(String demographicNo) {
		Date nextApptDate = null;
		if (demographicNo != null && !demographicNo.equalsIgnoreCase("") && !demographicNo.equalsIgnoreCase("null")) {
			return NONE;
		}

		OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);
		Appointment appt = dao.findNextAppointment(ConversionUtils.fromIntString(demographicNo));
		if (appt == null) {
			return NONE;
		}

		return ConversionUtils.toDateString(nextApptDate);
	}
	
}
