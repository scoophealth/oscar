package org.oscarehr.PMmodule.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateTimeFormatUtils {
	
	private static final Log log = LogFactory.getLog(DateTimeFormatUtils.class);
	
	// Public methods
	
	public static final Date getFuture(Date start, Integer daysInFuture, DateFormat dateFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysInFuture);
		Date future = calendar.getTime();
		
		return getDateFromDate(future, dateFormat);
	}
	
	public static final Date getToday(DateFormat dateFormat) {
		return getDateFromDate(Calendar.getInstance().getTime(), dateFormat);
	}

	public static final String getStringFromDate(Date date, DateFormat dateFormat) {
		return format(date, dateFormat);
	}
	
	public static final Date getDateFromString(String date, DateFormat dateFormat) {
		return parse(date, dateFormat);
	}
	
	public static final Date getDateFromDate(Date date, DateFormat dateFormat) {
		return parseFormat(date, dateFormat);
	}

	public static final Date getTimeFromLong(long time, DateFormat timeFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		
		return getTimeFromDate(date, timeFormat);
	}

	public static final String getStringFromTime(Date time, DateFormat timeFormat) {
		return format(time, timeFormat);
	}
	
	public static final Date getTimeFromString(String time, DateFormat timeFormat) {
		return parse(time, timeFormat);
	}
	
	public static final Date getTimeFromDate(Date date, DateFormat timeFormat) {
		return parse(format(date, timeFormat), timeFormat);
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
        	log.error(e);
	        throw new IllegalStateException(e);
        }
	}
	
}