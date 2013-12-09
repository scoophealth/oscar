/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.utility;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * @deprecated 2013-12-09 use org.oscarehr.util.DateUtils instead
 */
public final class DateTimeFormatUtils {

	private static final Logger log=MiscUtils.getLogger();

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CANADA);

	// Public methods

	// Timestamp

	public static final Timestamp getPast(int numYears) {
		return getPast(Calendar.getInstance(), numYears);
	}

	public static final Timestamp getPast(Calendar calendar, int numYears) {
		calendar.add(Calendar.YEAR, -numYears);

		return new Timestamp(calendar.getTimeInMillis());
	}

	// Dates

	public static final Date getFuture(Date start, Integer daysInFuture) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysInFuture);
		Date future = calendar.getTime();

		return getDateFromDate(future);
	}

	public static final String getStringFromDate(Date date) {
		return getStringFromDate(date, DATE_FORMAT);
	}

	public static final String getStringFromDate(Date date, DateFormat format) {
		return format(date, format);
	}

	public static final Date getDateFromString(String date) {
		return getDateFromString(date, DATE_FORMAT);
	}

	public static final Date getDateFromString(String date, DateFormat format) {
		return parse(date, DATE_FORMAT);
	}

	public static final Date getDateFromDate(Date date) {
		return parseFormat(date, DATE_FORMAT);
	}

	// Times

	public static final Date getTimeFromLong(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();

		return getTimeFromDate(date);
	}

	public static final String getStringFromTime(Date time) {
		return format(time, TIME_FORMAT);
	}

	public static final Date getTimeFromString(String time) {
		return parse(time, TIME_FORMAT);
	}

	public static final Date getTimeFromDate(Date date) {
		return parse(format(date, TIME_FORMAT), TIME_FORMAT);
	}

	// Private methods

	private static final Date parseFormat(Date date, DateFormat format) {
		return parse(format(date, format), format);
	}

	private static final String format(Date date, DateFormat format) {
		return (date != null) ? format.format(date) : new String();
	}

	private static final Date parse(String s, DateFormat format) {
		try {
			return (s != null && s.length() > 0) ? format.parse(s) : null;
		} catch (ParseException e) {
			log.error("Error", e);
			throw new IllegalStateException(e);
		}
	}

}
