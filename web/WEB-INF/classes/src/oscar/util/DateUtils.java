/*
 *

 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *

 * This software is published under the GPL GNU General Public License.

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public License

 * as published by the Free Software Foundation; either version 2

 * of the License, or (at your option) any later version. *

 * This program is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License

 * along with this program; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *

 *

 * <OSCAR TEAM>

 *

 * This software was written for the

 * Department of Family Medicine

 * McMaster University

 * Hamilton

 * Ontario, Canada

 */

package oscar.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class DateUtils {

	private static Logger logger = LogManager.getLogger(DateUtils.class);

	private static SimpleDateFormat sdf;

	private static String formatDate = "dd/MM/yyyy";

	public static String getISODateTimeFormatNoT(Calendar cal)
	{
		return(DateFormatUtils.ISO_DATETIME_FORMAT.format(cal).replace('T', ' '));
	}
	
	public static SimpleDateFormat getDateFormatter() {

		if (sdf == null) {

			sdf = new SimpleDateFormat(formatDate);

		}

		return sdf;

	}

	public static void setDateFormatter(String pattern) {

		sdf = new SimpleDateFormat(pattern);

	}

	public static String getDate() {

		Date date = new Date();

		return DateFormat.getDateInstance().format(date);

	}

	public static String getDate(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat();

		return sdf.format(date);

	}

	public static String getDate(Date date, String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

		return sdf.format(date);
	}

	public static String getDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date);
	}

	public static String getDateTime() {

		Date date = new Date();

		return DateFormat.getDateTimeInstance().format(date);

	}

	public static String formatDate(String date, String format,

	String formatAtual) {

		try {

			setDateFormatter(formatAtual);

			Date data = getDateFormatter().parse(date);

			logger.debug("[DateUtils] - formatDate: data formatada: " +

			getDateFormatter().format(data));

			setDateFormatter(format);

			return getDateFormatter().format(data);

		} catch (ParseException e) {

			logger.error("[DateUtils] - formatDate: ", e);

		}

		return "";

	}

	public static String formatDate(String date, String format) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat();

			Date data = sdf.parse(date);

			logger.debug("[DateUtils] - formatDate: data formatada: " +

			sdf.format(data));

			setDateFormatter(format);

			return getDateFormatter().format(data);

		} catch (ParseException e) {

			logger.error("[DateUtils] - formatDate: ", e);

		}

		return "";

	}

	public static String sumDate(String format, String pSum) {

		int iSum = new Integer(pSum).intValue();

		logger.debug("[DateUtils] - sumDate: iSum = " + iSum);

		Calendar calendar = new GregorianCalendar();

		Date now = new Date();

		calendar.setTime(now);

		calendar.add(Calendar.DATE, iSum);

		Date data = calendar.getTime();

		setDateFormatter(format);

		return getDateFormatter().format(data);

	}

	public String NextDay(int day, int month, int year) {

		boolean leapyear;

		switch (month) {

		// the months with 31 days without december

		case 1:

		case 3:

		case 5:

		case 7:

		case 8:

		case 10:

			if (day < 31) {

				day++;

			} else {

				day = 1;

				month++;

			}

			break;

		case 12:

			if (day < 31) {

				day++;

			} else {

				day = 1;

				month = 1;

				year++;

			}

			break;

		case 2:

			if (day < 28) {

				day++;

			} else {

				if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0)) {

					leapyear = true;

				} else {

					leapyear = false;

					// in a leapyear 29 days

				}
				if (leapyear == true) {

					if (day == 28) {

						day++;

					} else {

						day = 1;

						month++;

					}
				}

				else {

					day = 1;

					month++;

				}

			}

			break;

		// these are the other month 4 6 9 11

		default:

			if (day < 30) {

				day++;

			} else {

				day = 1;

				month++;

			}

		} // switch

		String nextDay = year + "-" + month + "-" + day;

		return nextDay;

	}

	public String NextDay(int day, int month, int year, int numDays) {

		int modValue = 28;

		while (numDays > 0) {

			int curNumDays = numDays % modValue;

			if (curNumDays == 0) {

				curNumDays = modValue;

			}

			switch (month) {

			// the months with 31 days without december

			case 1:

			case 3:

			case 5:

			case 7:

			case 8:

			case 10:

				if (day + curNumDays < 31) {

					day = day + curNumDays;

				} else if (((day + curNumDays) % 31) == 0) {

					day = 31;

				}

				else {

					day = ((day + curNumDays) % 31);

					month++;

				}

				break;

			case 12:

				if (day + curNumDays < 31) {

					day = day + curNumDays;

				} else if (((day + curNumDays) % 31) == 0) {

					day = 31;

				}

				else {

					day = ((day + curNumDays) % 31);

					month = 1;

					year++;

				}

				break;

			case 2:

				if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0)) {

					if (day + curNumDays < 29) {

						day = day + curNumDays;

					} else if (((day + curNumDays) % 29) == 0) {

						day = 29;

					}

					else {

						day = ((day + curNumDays) % 29);

						month++;

					}

				}

				else {

					if (day + curNumDays < 28) {

						day = day + curNumDays;

					} else if (((day + curNumDays) % 28) == 0) {

						day = 28;

					}

					else {

						day = ((day + curNumDays) % 28);

						month++;

					}

				}

				break;

			// these are the other month 4 6 9 11

			default:

				if (day + curNumDays < 30) {

					day = day + curNumDays;

				} else if (((day + curNumDays) % 30) == 0) {

					day = 30;

				}

				else {

					day = ((day + curNumDays) % 30);

					month++;

				}

			} // switch

			numDays = numDays - curNumDays;

		}

		String nextDay = year + "-" + month + "-" + day;

		return nextDay;

	}

	/**
	 *Gets the difference between two dates, in days. Takes two dates represented in milliseconds and returns the difference in days
	 */
	public static long getDifDays(Date greater, Date lesser) {
		long timeInMillis=greater.getTime()-lesser.getTime();
		return(timeInMillis/org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY);
	}

	public static long getNumberOfDaysBetweenTwoDates(Calendar cal1, Calendar cal2)
	{
		long timeInMillis=Math.abs(cal1.getTimeInMillis()-cal2.getTimeInMillis());
		return(timeInMillis/org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY);
	}
	
	public static long getNumberOfDaysBetweenTwoDates(Date date1, Date date2)
	{
		long timeInMillis=Math.abs(date1.getTime()-date2.getTime());
		return(timeInMillis/org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY);
	}

	/**
	 * Converts a String date with the form 'yyyy-MM-dd' to a String date with the form 'yyyyMMdd'
	 * 
	 * @param oldDateString String - The string to be converted
	 * @return String - The formatted date String
	 */
	public static String convertDate8Char(String oldDateString) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = "00000000";
		try {
			if (oldDateString != null) {
				Date tempDate = fmt.parse(oldDateString);
				sdate = new SimpleDateFormat("yyyyMMdd").format(tempDate);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		} finally {
			return sdate;
		}
	}

	/**
	 * null safe method for converting an iso date from string to calendar.
	 */
	public static GregorianCalendar toGregorianCalendarDate(String isoDateString) {
		if (isoDateString == null) return (null);

		try {
			String[] split=isoDateString.split("-");
			int year=Integer.parseInt(split[0]);
			int month=Integer.parseInt(split[1])-1;
			int day=Integer.parseInt(split[2]);
			
			return(new GregorianCalendar(year,month,day));
		} catch (Exception e) {
			throw (new IllegalArgumentException("The passed in string is not a valid ISO date"));
		}
	}
	
	public static Date toDate(String isoDateString)
	{
		GregorianCalendar cal=toGregorianCalendarDate(isoDateString);
		if (cal!=null) return(cal.getTime());
		else return(null);
	}
	
	/**
	 * date2-date1
	 * 
	 * if either are null, it returns null.
	 */
	public static Integer yearDifference(Date date1, Date date2)
	{
		if (date1==null || date2==null) return(null);
		
		Calendar cal1=new GregorianCalendar();
		cal1.setTime(date1);
		
		Calendar cal2=new GregorianCalendar();
		cal2.setTime(date2);
		
		return(yearDifference(cal1, cal2));
	}
		
	/**
	 * date2-date1
	 * 
	 * if either are null, it returns null.
	 */
	public static Integer yearDifference(Calendar date1, Calendar date2)
	{
		if (date1==null || date2==null) return(null);
		
		int yearDiff=date2.get(Calendar.YEAR)-date1.get(Calendar.YEAR);
		
		if (date2.get(Calendar.DAY_OF_YEAR)>date1.get(Calendar.DAY_OF_YEAR)) yearDiff--;
		
		return(yearDiff);
	}
	
	/**
	 * This method will calculate the age of the person on the given date.
	 */
	public static Integer getAge(Calendar dateOfBirth, Calendar onThisDay)
	{
		return(yearDifference(dateOfBirth, onThisDay));
	}
	
	/**
	 * date2-date1
	 * 
	 * if either are null, it returns null.
	 */
	public static Integer calculateDayDifference(Calendar date1, Calendar date2)
	{
		if (date1==null || date2==null) return(null);

		long ms=date2.getTimeInMillis()-date1.getTimeInMillis();
		return((int) (ms/org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY));
	}

	/**
	 * date2-date1
	 * 
	 * if either are null, it returns null.
	 */
	public static Integer calculateDayDifference(Date date1, Date date2)
	{
		if (date1==null || date2==null) return(null);
		
		Calendar cal1=new GregorianCalendar();
		cal1.setTime(date1);
		
		Calendar cal2=new GregorianCalendar();
		cal2.setTime(date2);
		
		return(calculateDayDifference(cal1, cal2));
	}

}
