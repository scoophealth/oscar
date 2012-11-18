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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="survey")
public class Survey extends AbstractModel<Integer> {

        public static final short STATUS_IN_REVIEW      = 0;
	public static final short STATUS_TEST           = 1;
	public static final short STATUS_LAUNCHED       = 2;
	public static final short STATUS_CLOSED         = 3;

    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="surveyid")
	private Integer id;
	
	private String description;
	
	private String surveyData;
	
	private int version;
	
	private int status;
	
	private int userId;
	
	private int facilityId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLaunched;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateClosed;
	
	@Column(name="launched_instance_id")
	private int launchedInstanceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSurveyData() {
		return surveyData;
	}

	public void setSurveyData(String surveyData) {
		this.surveyData = surveyData;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLaunched() {
		return dateLaunched;
	}

	public void setDateLaunched(Date dateLaunched) {
		this.dateLaunched = dateLaunched;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public int getLaunchedInstanceId() {
		return launchedInstanceId;
	}

	public void setLaunchedInstanceId(int launchedInstanceId) {
		this.launchedInstanceId = launchedInstanceId;
	}
	
	
}
