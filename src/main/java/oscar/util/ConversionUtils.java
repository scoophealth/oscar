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
package oscar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Yet another conversion utility class for bridging JPA entity to legacy schema mismatch. 
 *
 */
public class ConversionUtils {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

	private ConversionUtils() {
	}

	public static List<Integer> toIntList(List<String> list) {
		List<Integer> result = new ArrayList<Integer>();
		for(String str : list)
			result.add(fromIntString(str));
		return result;
	}
	
	public static Date fromTimeString(String timeString) {
		return fromDateString(timeString, DEFAULT_TIME_PATTERN);
	}

	public static Date fromDateString(String dateString) {
		return fromDateString(dateString, DEFAULT_DATE_PATTERN);
	}

	private static Date fromDateString(String dateString, String formatPattern) {
		if (dateString == null || "".equals(dateString.trim())) return null;

		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	private static String toDateString(Date date, String formatPattern) {
		if (date == null) return "";

		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(date);
	}

	public static String toTimeString(Date time) {
		return toDateString(time, DEFAULT_TIME_PATTERN);
	}

	public static String toDateString(Date date) {
		return toDateString(date, DEFAULT_DATE_PATTERN);
	}

	public static Integer fromIntString(String intString) {
		if (intString == null || "".equals(intString.trim())) return 0;
		return Integer.parseInt(intString);
	}

	public static String toIntString(Integer integer) {
		if (integer == null) return "0";
		return integer.toString();
	}
	
	public static String toBoolString(Boolean b) {
		if (b == null || b == Boolean.FALSE)
			return "0";
		return "1";
	}
	
	public static boolean fromBoolString(String str) {
		if (str == null || "".equals(str.trim()) || "0".equals(str))
			return false;
		return true;
	}

}
