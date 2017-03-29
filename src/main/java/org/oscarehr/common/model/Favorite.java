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

import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="favorites")
public class Favorite extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="favoriteid")
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="favoritename")
	private String name;

	@Column(name="BN")
	private String bn;

	@Column(name="GCN_SEQNO")
	private double gcnSeqno;

	private String customName;

	@Column(name="takemin")
	private float takeMin;

	@Column(name="takemax")
	private float takeMax;

	@Column(name="freqcode")
	private String frequencyCode;

	private String duration;

	@Column(name="durunit")
	private String durationUnit;

	private String quantity;

	private String dispensingUnits;

	@Column(name="`repeat`")
	private int repeat;

	private boolean nosubs;

	private boolean prn;

	private String special;

	@Column(name="GN")
	private String gn;

	@Column(name="ATC")
	private String atc;

	@Column(name="regional_identifier")
	private String regionalIdentifier;

	private String unit;

	private String method;

	private String route;

	@Column(name="drug_form")
	private String drugForm;

	private String dosage;

	@Column(name="custom_instructions")
	private boolean customInstructions;

	private String unitName;
	
	private boolean dispenseInternal = false;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getBn() {
    	return bn;
    }

	public void setBn(String bn) {
    	this.bn = bn;
    }

	public double getGcnSeqno() {
    	return gcnSeqno;
    }

	public void setGcnSeqno(double gcnSeqno) {
    	this.gcnSeqno = gcnSeqno;
    }

	public String getCustomName() {
    	return customName;
    }

	public void setCustomName(String customName) {
    	this.customName = customName;
    }

	public float getTakeMin() {
    	return takeMin;
    }

	public void setTakeMin(float takeMin) {
    	this.takeMin = takeMin;
    }

	public float getTakeMax() {
    	return takeMax;
    }

	public void setTakeMax(float takeMax) {
    	this.takeMax = takeMax;
    }

	public String getFrequencyCode() {
    	return frequencyCode;
    }

	public void setFrequencyCode(String frequencyCode) {
    	this.frequencyCode = frequencyCode;
    }

	public String getDuration() {
    	return duration;
    }

	public void setDuration(String duration) {
    	this.duration = duration;
    }

	public String getDurationUnit() {
    	return durationUnit;
    }

	public void setDurationUnit(String durationUnit) {
    	this.durationUnit = durationUnit;
    }

	public String getQuantity() {
    	return quantity;
    }

	public void setQuantity(String quantity) {
    	this.quantity = quantity;
    }

	public String getDispensingUnits() {
		return dispensingUnits;
	}

	public void setDispensingUnits(String dispensingUnits) {
		this.dispensingUnits = StringUtils.trimToNull(dispensingUnits);
	}

	public int getRepeat() {
    	return repeat;
    }

	public void setRepeat(int repeat) {
    	this.repeat = repeat;
    }

	public boolean isNosubs() {
    	return nosubs;
    }

	public void setNosubs(boolean nosubs) {
    	this.nosubs = nosubs;
    }

	public boolean isPrn() {
    	return prn;
    }

	public void setPrn(boolean prn) {
    	this.prn = prn;
    }

	public String getSpecial() {
    	return special;
    }

	public void setSpecial(String special) {
    	this.special = special;
    }

	public String getGn() {
    	return gn;
    }

	public void setGn(String gn) {
    	this.gn = gn;
    }

	public String getAtc() {
    	return atc;
    }

	public void setAtc(String atc) {
    	this.atc = atc;
    }

	public String getRegionalIdentifier() {
    	return regionalIdentifier;
    }

	public void setRegionalIdentifier(String regionalIdentifier) {
    	this.regionalIdentifier = regionalIdentifier;
    }

	public String getUnit() {
    	return unit;
    }

	public void setUnit(String unit) {
    	this.unit = unit;
    }

	public String getMethod() {
    	return method;
    }

	public void setMethod(String method) {
    	this.method = method;
    }

	public String getRoute() {
    	return route;
    }

	public void setRoute(String route) {
    	this.route = route;
    }

	public String getDrugForm() {
    	return drugForm;
    }

	public void setDrugForm(String drugForm) {
    	this.drugForm = drugForm;
    }

	public String getDosage() {
    	return dosage;
    }

	public void setDosage(String dosage) {
    	this.dosage = dosage;
    }

	public boolean isCustomInstructions() {
    	return customInstructions;
    }

	public void setCustomInstructions(boolean customInstructions) {
    	this.customInstructions = customInstructions;
    }

	public String getUnitName() {
    	return unitName;
    }

	public void setUnitName(String unitName) {
    	this.unitName = unitName;
    }

	public boolean isDispenseInternal() {
		return dispenseInternal;
	}

	public void setDispenseInternal(boolean dispenseInternal) {
		this.dispenseInternal = dispenseInternal;
	}



}
