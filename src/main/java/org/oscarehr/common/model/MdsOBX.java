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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsOBX")
public class MdsOBX extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="obxID")
	private int obxId;

	private String valueType;

	@Column(name="observationIden")
	private String observationIdentifier;

	@Column(name="observationSubID")
	private String observationSubId;

	private String observationValue;

	private String abnormalFlags;

	private String observationResultStatus;

	@Column(name="producersID")
	private String producersId;

	private int associatedOBR;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getObxId() {
    	return obxId;
    }

	public void setObxId(int obxId) {
    	this.obxId = obxId;
    }

	public String getValueType() {
    	return valueType;
    }

	public void setValueType(String valueType) {
    	this.valueType = valueType;
    }

	public String getObservationIdentifier() {
    	return observationIdentifier;
    }

	public void setObservationIdentifier(String observationIdentifier) {
    	this.observationIdentifier = observationIdentifier;
    }

	public String getObservationSubId() {
    	return observationSubId;
    }

	public void setObservationSubId(String observationSubId) {
    	this.observationSubId = observationSubId;
    }

	public String getObservationValue() {
    	return observationValue;
    }

	public void setObservationValue(String observationValue) {
    	this.observationValue = observationValue;
    }

	public String getAbnormalFlags() {
    	return abnormalFlags;
    }

	public void setAbnormalFlags(String abnormalFlags) {
    	this.abnormalFlags = abnormalFlags;
    }

	public String getObservationResultStatus() {
    	return observationResultStatus;
    }

	public void setObservationResultStatus(String observationResultStatus) {
    	this.observationResultStatus = observationResultStatus;
    }

	public String getProducersId() {
    	return producersId;
    }

	public void setProducersId(String producersId) {
    	this.producersId = producersId;
    }

	public int getAssociatedOBR() {
    	return associatedOBR;
    }

	public void setAssociatedOBR(int associatedOBR) {
    	this.associatedOBR = associatedOBR;
    }


}
