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

package org.oscarehr.common.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SystemMessage extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = 0;
	private String message = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate = new Date();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
    public Integer getId() {
		return id;
	}

	/**
	 * you should never set id's in an existing jpa object, this leads to known problems and is considered bad practice.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @deprecated
	 */
	public String getExpiry_day() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(getExpiryDate());
	}

	/**
	 * @deprecated
	 */
	public void setExpiry_day(String day) throws IllegalArgumentException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt = formatter.parse(day);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(getExpiryDate());
			cal.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
			setExpiryDate(cal.getTime());
		}
		catch (Exception e) {
			throw new IllegalArgumentException("date must be in yyyy-MM-dd format");
		}
	}

	/**
	 * @deprecated
	 */
	public String getExpiry_hour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());
		int hour = cal.get(Calendar.HOUR);
		if (cal.get(Calendar.AM_PM) == Calendar.PM) {
			hour += 12;
		}
		return String.valueOf(hour);
	}

	/**
	 * @deprecated
	 */
	public void setExpiry_hour(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());
		int hr = Integer.valueOf(hour).intValue();
		if (hr >= 12) {
			cal.set(Calendar.AM_PM, Calendar.PM);
			cal.set(Calendar.HOUR, hr - 12);
		}
		else {
			cal.set(Calendar.AM_PM, Calendar.AM);
			cal.set(Calendar.HOUR, hr);
		}
		setExpiryDate(cal.getTime());
	}

	/**
	 * @deprecated
	 */
	public String getExpiry_minute() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());
		return String.valueOf(cal.get(Calendar.MINUTE));
	}

	/**
	 * @deprecated
	 */
	public void setExpiry_minute(String minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());
		cal.set(Calendar.MINUTE, Integer.valueOf(minute).intValue());
		setExpiryDate(cal.getTime());
	}

	/**
	 * @deprecated
	 */
	public String getFormattedCreationDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getCreationDate());
	}

	/**
	 * @deprecated
	 */
	public String getFormattedExpiryDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getExpiryDate());
	}

	public boolean getActive() {
		Date now = new Date();
		if (now.before(getExpiryDate())) {
			return true;
		}
		return false;
	}

	public boolean getExpired() {
		Date now = new Date();
		if (now.after(getExpiryDate())) {
			return true;
		}
		return false;
	}
}
