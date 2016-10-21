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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "measurements")
public class Measurement extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "demographicNo")
	private Integer demographicId;
	
	@Column(name = "providerNo")	
	private String providerNo;
	
	@Column(name = "dataField", nullable=false, length=255)
	private String dataField = "";
	
	@Column(name = "measuringInstruction", length=255)
	private String measuringInstruction = "";
	
	@Column(name = "comments", length=255)
	private String comments = "";

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateObserved")
	private Date dateObserved;

	@Column(name = "appointmentNo")
	private Integer appointmentNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateEntered")
	private Date createDate = new Date();

	@PreUpdate
	protected void jpaPreventChange() {
		throw (new UnsupportedOperationException("This action is not allowed for this type of item."));
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getType() {
		return (type);
	}

	public void setType(String type) {
		this.type = StringUtils.trimToEmpty(type);
	}

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

	public String getDataField() {
		return (dataField);
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public String getMeasuringInstruction() {
		return (measuringInstruction);
	}

	public void setMeasuringInstruction(String measuringInstruction) {
		this.measuringInstruction = measuringInstruction;
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getDateObserved() {
		return (dateObserved);
	}

	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}

	public Integer getAppointmentNo() {
		return (appointmentNo);
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public Date getCreateDate() {
		return (createDate);
	}
	
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	   public static final Comparator<Measurement> DateObservedComparator = new Comparator<Measurement>() {
	        public int compare(Measurement o1, Measurement o2) {
	        	if(o1.getId()!=null && o2.getId() != null) {
	        		return o1.getDateObserved().compareTo(o2.getDateObserved());
	        	}
	        	return 0;
	        }
	    };
}
