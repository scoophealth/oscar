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
@Table(name="favorites")
public class Favorites extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="favoriteid")
	private Integer id;
	
	@Column(name="provider_no")
	private String providerNo;
	
	@Column(name="favoritename")
	private String favoriteName;
	
	@Column(name="BN")
	private String bn;
	
	@Column(name="GCN_SEQNO")
	private double gcnSeqNo;
	
	private String customName;
	
	@Column(name="takemin")
	private double takeMin;
	
	@Column(name="takemax")
	private double takeMax;
	
	@Column(name="freqcode")
	private String freqCode;
	
	private String duration;
	
	@Column(name="durunit")
	private String durationUnit;
	
	private String quantity;
	
	@Column(name = "`repeat`")
	private int repeat;
	
	@Column(name="nosubs")
	private boolean noSubs;
	
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

	public String getFavoriteName() {
		return favoriteName;
	}

	public void setFavoriteName(String favoriteName) {
		this.favoriteName = favoriteName;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public double getGcnSeqNo() {
		return gcnSeqNo;
	}

	public void setGcnSeqNo(double gcnSeqNo) {
		this.gcnSeqNo = gcnSeqNo;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public double getTakeMin() {
		return takeMin;
	}

	public void setTakeMin(double takeMin) {
		this.takeMin = takeMin;
	}

	public double getTakeMax() {
		return takeMax;
	}

	public void setTakeMax(double takeMax) {
		this.takeMax = takeMax;
	}

	public String getFreqCode() {
		return freqCode;
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
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

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public boolean isNoSubs() {
		return noSubs;
	}

	public void setNoSubs(boolean noSubs) {
		this.noSubs = noSubs;
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
	
	
}
