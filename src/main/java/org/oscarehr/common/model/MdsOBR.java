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
@Table(name="mdsOBR")
public class MdsOBR extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="ObrID")
	private int ObrId;

	private String placerOrderNo;

	@Column(name="universalServiceID")
	private String universalServiceId;

	private String observationDateTime;

	private String specimenRecDateTime;

	private String fillerFieldOne;

	private String quantityTiming;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getObrId() {
    	return ObrId;
    }

	public void setObrId(int obrId) {
    	ObrId = obrId;
    }

	public String getPlacerOrderNo() {
    	return placerOrderNo;
    }

	public void setPlacerOrderNo(String placerOrderNo) {
    	this.placerOrderNo = placerOrderNo;
    }

	public String getUniversalServiceId() {
    	return universalServiceId;
    }

	public void setUniversalServiceId(String universalServiceId) {
    	this.universalServiceId = universalServiceId;
    }

	public String getObservationDateTime() {
    	return observationDateTime;
    }

	public void setObservationDateTime(String observationDateTime) {
    	this.observationDateTime = observationDateTime;
    }

	public String getSpecimenRecDateTime() {
    	return specimenRecDateTime;
    }

	public void setSpecimenRecDateTime(String specimenRecDateTime) {
    	this.specimenRecDateTime = specimenRecDateTime;
    }

	public String getFillerFieldOne() {
    	return fillerFieldOne;
    }

	public void setFillerFieldOne(String fillerFieldOne) {
    	this.fillerFieldOne = fillerFieldOne;
    }

	public String getQuantityTiming() {
    	return quantityTiming;
    }

	public void setQuantityTiming(String quantityTiming) {
    	this.quantityTiming = quantityTiming;
    }


}
