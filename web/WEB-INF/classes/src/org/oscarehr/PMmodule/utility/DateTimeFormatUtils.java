package org.oscarehr.PMmodule.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateTimeFormatUtils {
	
	private static final Log log = LogFactory.getLog(DateTimeFormatUtils.class);
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
	
	// Public methods
	
	public static final Date getFuture(Date start, Integer daysInFuture) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysInFuture);
		Date future = calendar.getTime();
		
		return getDateFromDate(future);
	}
	
	public static final Date getToday() {
		return getDateFromDate(Calendar.getInstance().getTime());
	}

	public static final String getStringFromDate(Date date) {
		return format(date, DATE_FORMAT);
	}
	
	public static final Date getDateFromString(String date) {
		return parse(date, DATE_FORMAT);
	}
	
	public static final Date getDateFromDate(Date date) {
		return parseFormat(date, DATE_FORMAT);
	}

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
        	log.error(e);
	        throw new IllegalStateException(e);
        }
	}
	
}