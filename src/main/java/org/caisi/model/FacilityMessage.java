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

package org.caisi.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FacilityMessage extends BaseObject {
	private Long id;
	private String message;
	private Date creation_date;
	private Date expiry_date;
	private Integer facilityId;
	private String facilityName;
	
	public FacilityMessage() {
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
	public Integer getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Integer facility_id) {
		this.facilityId = facility_id;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facility_name) {
		this.facilityName = facility_name;
	}
	
	/* web specific */
	public String getExpiry_day() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(getExpiry_date());
	}
	
	public void setExpiry_day(String day) throws IllegalArgumentException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt=formatter.parse(day);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(getExpiry_date());		
			cal.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
			setExpiry_date(cal.getTime());
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
		int hr = Integer.valueOf(hour).intValue();
		if (hr >= 12 ) {
			cal.set(Calendar.AM_PM,Calendar.PM);
			cal.set(Calendar.HOUR,hr-12);
		}
		else
		{
			cal.set(Calendar.AM_PM,Calendar.AM);
			cal.set(Calendar.HOUR,hr);
		}
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
