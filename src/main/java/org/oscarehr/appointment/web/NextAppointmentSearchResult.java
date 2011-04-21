package org.oscarehr.appointment.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.oscarehr.common.model.Provider;

public class NextAppointmentSearchResult {
	private String providerNo;
	private Date date;
	private int duration;
	private Provider provider;
	
	
	public String getProviderNo() {
    	return providerNo;
    }
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	public Date getDate() {
    	return date;
    }
	public void setDate(Date date) {
    	this.date = date;
    }

	public int getDuration() {
    	return duration;
    }
	public void setDuration(int duration) {
    	this.duration = duration;
    }
	public Provider getProvider() {
    	return provider;
    }
	public void setProvider(Provider provider) {
    	this.provider = provider;
    }
	

	public String getYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(getDate());		
	}
	
	public String getMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format(getDate());		
	}
	
	public String getDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		return formatter.format(getDate());		
	}
	
	public String getStartTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(getDate());		
	}
	
	public String getEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		if(duration > 0) {
			cal.add(Calendar.MINUTE, duration-1);
		}
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(cal.getTime());		
	}
	
}
