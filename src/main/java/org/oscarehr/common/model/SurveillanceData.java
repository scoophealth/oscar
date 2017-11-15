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

import javax.persistence.*;

@Entity
@Table(name = "SurveillanceData")
public class SurveillanceData extends AbstractModel<Integer> {

	/*create table SurveillanceData (
			id int(10)  NOT NULL auto_increment primary key,
			surveyId varchar(50),
			data mediumblob,
			createDate datetime,
			lastUpdateDate datetime,
			transmissionDate datetime,
			sent boolean
			);
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String surveyId;

	@Column(columnDefinition = "mediumblob")
	private byte[] data;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date transmissionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	
	private boolean sent = false;

	
	@Override
	public Integer getId() {
		return id;
	}


	public byte[] getData() {
		return data;
	}


	public void setData(byte[] data) {
		this.data = data;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getTransmissionDate() {
		return transmissionDate;
	}


	public void setTransmissionDate(Date transmissionDate) {
		this.transmissionDate = transmissionDate;
	}


	
	
	@PrePersist
	@PreUpdate
	protected void jpa_updateCreated() {
		lastUpdateDate = new Date();
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public String getSurveyId() {
		return surveyId;
	}


	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}


	public boolean isSent() {
		return sent;
	}


	public void setSent(boolean sent) {
		this.sent = sent;
	}

}
