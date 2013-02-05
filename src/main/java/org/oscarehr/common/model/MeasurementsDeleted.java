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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "measurementsDeleted")
public class MeasurementsDeleted extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	private Integer demographicNo = null;
	
	private String type = null;
		
	private String providerNo = null;
	
	@Column(nullable = false)
	private String dataField = null;
		
	private String measuringInstruction = null;
	
	private String comments = null;
	
	private Date dateObserved = null;
	
	@Column(nullable = false)
	private Date dateEntered = null;
	
	@Column(nullable = false)
	private Date dateDeleted = new Date();

	@Column(nullable = false)
	private Integer originalId = null;

	public MeasurementsDeleted() {
	}
	
	public MeasurementsDeleted(Measurement m) {
    	setType(m.getType());
    	setDemographicNo(m.getDemographicId());
    	setProviderNo(m.getProviderNo());
    	setDataField(m.getDataField());
    	setMeasuringInstruction(m.getMeasuringInstruction());
    	setComments(m.getComments());
    	setDateObserved(m.getDateObserved());
    	setDateEntered(m.getCreateDate());
    	setOriginalId(m.getId());
	}
	
	@Override
    public Integer getId() {
		return id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	
	public String getMeasuringInstruction() {
		return measuringInstruction;
	}
	public void setMeasuringInstruction(String instruction) {
		this.measuringInstruction = instruction;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Date getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}
	
	public Date getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}
	
	public Date getDateDeleted() {
		return dateDeleted;
	}
	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}
	
	public Integer getOriginalId() {
		return originalId;
	}
	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}
}
