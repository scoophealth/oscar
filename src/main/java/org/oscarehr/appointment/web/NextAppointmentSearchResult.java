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
