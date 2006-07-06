package org.caisi.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SystemMessage extends BaseObject {
	private Long id;
	private String message;
	private Date creation_date;
	private Date expiry_date;
	
	public SystemMessage() {
		creation_date = new Date();
		expiry_date = new Date();
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	/* web specific */
	public String getExpiry_day() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(getExpiry_date());
	}
	
	public void setExpiry_day(String day) throws IllegalArgumentException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setExpiry_date(formatter.parse(day));
		}catch(Exception e) {
			throw new IllegalArgumentException("date must be in yyyy-MM-dd format");
		}
	}
	
	public String getExpiry_hour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiry_date());
		int hour = cal.get(Calendar.HOUR);
		if(cal.get(Calendar.AM_PM) == Calendar.PM) {
			hour += 12;
		}
		return String.valueOf(hour);
	}
	
	public void setExpiry_hour(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiry_date());		
		cal.set(Calendar.HOUR,Integer.valueOf(hour).intValue());
		setExpiry_date(cal.getTime());
	}
	
	public String getExpiry_minute() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiry_date());		
		return String.valueOf(cal.get(Calendar.MINUTE));
	}
	
	public void setExpiry_minute(String minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiry_date());		
		cal.set(Calendar.MINUTE,Integer.valueOf(minute).intValue());
		setExpiry_date(cal.getTime());
	}
	
	public String getFormattedCreationDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getCreation_date());
	}
	
	public String getFormattedExpiryDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getExpiry_date());
	}
	
	public boolean getActive() {
		Date now = new Date();
		if(now.before(getExpiry_date())) {
			return true;
		}
		return false;
	}
	
	public boolean getExpired() {
		Date now = new Date();
		if(now.after(getExpiry_date())) {
			return true;
		}
		return false;
	}
}
