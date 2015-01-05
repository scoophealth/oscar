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
package org.oscarehr.common.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="facility_message")
public class FacilityMessage extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable=false)
	private String message;
	
	@Column(name="creation_date",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Column(name="expiry_date",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	@Column(name="facility_id",nullable=true)
	private Integer facilityId;
	
	@Column(name="facility_name",length=32,nullable=true)
	private String facilityName;
	
	private Integer programId;

	@Transient
	private String programName;
	
	public FacilityMessage() {
		setCreationDate(new Date());
		setExpiryDate(new Date());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	
	/**
	 * Just copying this over from the old hibernate
	 */
	
	/* web specific */
	public String getExpiry_day() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(getExpiryDate());
	}
	
	public void setExpiry_day(String day) throws IllegalArgumentException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt=formatter.parse(day);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(getExpiryDate());		
			cal.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
			setExpiryDate(cal.getTime());
		}catch(Exception e) {
			throw new IllegalArgumentException("date must be in yyyy-MM-dd format");
		}
	}
	
	public String getExpiry_hour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());
		int hour = cal.get(Calendar.HOUR);
		if(cal.get(Calendar.AM_PM) == Calendar.PM) {
			hour += 12;
		}
		return String.valueOf(hour);
	}
	
	public void setExpiry_hour(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());		
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
		setExpiryDate(cal.getTime());
	}
	
	public String getExpiry_minute() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());		
		return String.valueOf(cal.get(Calendar.MINUTE));
	}
	
	public void setExpiry_minute(String minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getExpiryDate());		
		cal.set(Calendar.MINUTE,Integer.valueOf(minute).intValue());
		setExpiryDate(cal.getTime());
	}
	
	public String getFormattedCreationDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getCreationDate());
	}
	
	public String getFormattedExpiryDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return formatter.format(getExpiryDate());
	}
	
	public boolean getActive() {
		Date now = new Date();
		if(now.before(getExpiryDate())) {
			return true;
		}
		return false;
	}
	
	public boolean getExpired() {
		Date now = new Date();
		if(now.after(getExpiryDate())) {
			return true;
		}
		return false;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	@PrePersist
	@PreUpdate
	protected void jpa_prePersistAndUpdate() {
		if(getProgramId() != null && getProgramId().intValue() == 0) {
			setProgramId(null);
		}
	}
}
