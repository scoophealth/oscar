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
package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.Drug;
import org.springframework.beans.BeanUtils;

public class DrugTransfer {

	private Integer id;
	private String providerNo;
	private Integer demographicId;
	private Date rxDate;
	private Date endDate;
	private Date writtenDate;
	private String brandName;
	private int gcnSeqNo;
	private String customName;
	private float takeMin;
	private float takeMax;
	private String freqCode;
	private String duration;
	private String durUnit;
	private String quantity;
	private Integer repeat;
	private Date lastRefillDate;
	private boolean noSubs;
	private boolean prn;
	private String special;
	private String special_instruction;
	private boolean archived;
	private String archivedReason;
	private Date archivedDate;
	private String genericName;
	private String atc;
	private Integer scriptNo;
	private String regionalIdentifier;
	private String unit;
	private String method;
	private String route;
	private String drugForm;
	private Date createDate;
	private String dosage;
	private boolean customInstructions;
	private String unitName;
	private Boolean longTerm;
	private boolean pastMed;
	private Boolean patientCompliance;
	private String outsideProviderName;
	private String outsideProviderOhip;
	private Boolean hideFromDrugProfile;
	private Boolean customNote;
	private Boolean nonAuthoritative;
	private Date pickupDateTime;
	private String eTreatmentType;
	private String rxStatus;
	private boolean hideFromCpp;
	private Integer refillDuration;
	private Integer refillQuantity;
	private Integer dispenseInterval;
	private Integer position;
	private boolean startDateUnknown;
	private String comment;
	private Date lastUpdateDate;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getRxDate() {
		return (rxDate);
	}

	public void setRxDate(Date rxDate) {
		this.rxDate = rxDate;
	}

	public Date getEndDate() {
		return (endDate);
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getWrittenDate() {
		return (writtenDate);
	}

	public void setWrittenDate(Date writtenDate) {
		this.writtenDate = writtenDate;
	}

	public String getBrandName() {
		return (brandName);
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getGcnSeqNo() {
		return (gcnSeqNo);
	}

	public void setGcnSeqNo(int gcnSeqNo) {
		this.gcnSeqNo = gcnSeqNo;
	}

	public String getCustomName() {
		return (customName);
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public float getTakeMin() {
		return (takeMin);
	}

	public void setTakeMin(float takeMin) {
		this.takeMin = takeMin;
	}

	public float getTakeMax() {
		return (takeMax);
	}

	public void setTakeMax(float takeMax) {
		this.takeMax = takeMax;
	}

	public String getFreqCode() {
		return (freqCode);
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}

	public String getDuration() {
		return (duration);
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDurUnit() {
		return (durUnit);
	}

	public void setDurUnit(String durUnit) {
		this.durUnit = durUnit;
	}

	public String getQuantity() {
		return (quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Integer getRepeat() {
		return (repeat);
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Date getLastRefillDate() {
		return (lastRefillDate);
	}

	public void setLastRefillDate(Date lastRefillDate) {
		this.lastRefillDate = lastRefillDate;
	}

	public boolean isNoSubs() {
		return (noSubs);
	}

	public void setNoSubs(boolean noSubs) {
		this.noSubs = noSubs;
	}

	public boolean isPrn() {
		return (prn);
	}

	public void setPrn(boolean prn) {
		this.prn = prn;
	}

	public String getSpecial() {
		return (special);
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getSpecial_instruction() {
		return (special_instruction);
	}

	public void setSpecial_instruction(String special_instruction) {
		this.special_instruction = special_instruction;
	}

	public boolean isArchived() {
		return (archived);
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getArchivedReason() {
		return (archivedReason);
	}

	public void setArchivedReason(String archivedReason) {
		this.archivedReason = archivedReason;
	}

	public Date getArchivedDate() {
		return (archivedDate);
	}

	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	public String getGenericName() {
		return (genericName);
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getAtc() {
		return (atc);
	}

	public void setAtc(String atc) {
		this.atc = atc;
	}

	public Integer getScriptNo() {
		return (scriptNo);
	}

	public void setScriptNo(Integer scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getRegionalIdentifier() {
		return (regionalIdentifier);
	}

	public void setRegionalIdentifier(String regionalIdentifier) {
		this.regionalIdentifier = regionalIdentifier;
	}

	public String getUnit() {
		return (unit);
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMethod() {
		return (method);
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRoute() {
		return (route);
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getDrugForm() {
		return (drugForm);
	}

	public void setDrugForm(String drugForm) {
		this.drugForm = drugForm;
	}

	public Date getCreateDate() {
		return (createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDosage() {
		return (dosage);
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public boolean isCustomInstructions() {
		return (customInstructions);
	}

	public void setCustomInstructions(boolean customInstructions) {
		this.customInstructions = customInstructions;
	}

	public String getUnitName() {
		return (unitName);
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Boolean getLongTerm() {
		return (longTerm);
	}

	public void setLongTerm(Boolean longTerm) {
		this.longTerm = longTerm;
	}

	public boolean isPastMed() {
		return (pastMed);
	}

	public void setPastMed(boolean pastMed) {
		this.pastMed = pastMed;
	}

	public Boolean getPatientCompliance() {
		return (patientCompliance);
	}

	public void setPatientCompliance(Boolean patientCompliance) {
		this.patientCompliance = patientCompliance;
	}

	public String getOutsideProviderName() {
		return (outsideProviderName);
	}

	public void setOutsideProviderName(String outsideProviderName) {
		this.outsideProviderName = outsideProviderName;
	}

	public String getOutsideProviderOhip() {
		return (outsideProviderOhip);
	}

	public void setOutsideProviderOhip(String outsideProviderOhip) {
		this.outsideProviderOhip = outsideProviderOhip;
	}

	public Boolean getHideFromDrugProfile() {
		return (hideFromDrugProfile);
	}

	public void setHideFromDrugProfile(Boolean hideFromDrugProfile) {
		this.hideFromDrugProfile = hideFromDrugProfile;
	}

	public Boolean getCustomNote() {
		return (customNote);
	}

	public void setCustomNote(Boolean customNote) {
		this.customNote = customNote;
	}

	public Boolean getNonAuthoritative() {
		return (nonAuthoritative);
	}

	public void setNonAuthoritative(Boolean nonAuthoritative) {
		this.nonAuthoritative = nonAuthoritative;
	}

	public Date getPickupDateTime() {
		return (pickupDateTime);
	}

	public void setPickupDateTime(Date pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public String geteTreatmentType() {
		return (eTreatmentType);
	}

	public void seteTreatmentType(String eTreatmentType) {
		this.eTreatmentType = eTreatmentType;
	}

	public String getRxStatus() {
		return (rxStatus);
	}

	public void setRxStatus(String rxStatus) {
		this.rxStatus = rxStatus;
	}

	public boolean isHideFromCpp() {
		return (hideFromCpp);
	}

	public void setHideFromCpp(boolean hideFromCpp) {
		this.hideFromCpp = hideFromCpp;
	}

	public Integer getRefillDuration() {
		return (refillDuration);
	}

	public void setRefillDuration(Integer refillDuration) {
		this.refillDuration = refillDuration;
	}

	public Integer getRefillQuantity() {
		return (refillQuantity);
	}

	public void setRefillQuantity(Integer refillQuantity) {
		this.refillQuantity = refillQuantity;
	}

	public Integer getDispenseInterval() {
		return (dispenseInterval);
	}

	public void setDispenseInterval(Integer dispenseInterval) {
		this.dispenseInterval = dispenseInterval;
	}

	public Integer getPosition() {
		return (position);
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public boolean isStartDateUnknown() {
		return (startDateUnknown);
	}

	public void setStartDateUnknown(boolean startDateUnknown) {
		this.startDateUnknown = startDateUnknown;
	}

	public String getComment() {
		return (comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getLastUpdateDate() {
		return (lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public static DrugTransfer toTransfer(Drug drug) {
		if (drug == null) return (null);

		DrugTransfer transfer = new DrugTransfer();

		BeanUtils.copyProperties(drug, transfer);

		return (transfer);
	}

	public static DrugTransfer[] toTransfers(List<Drug> drugs) {
		ArrayList<DrugTransfer> results = new ArrayList<DrugTransfer>();

		for (Drug drug : drugs) {
			results.add(toTransfer(drug));
		}

		return (results.toArray(new DrugTransfer[0]));
	}
}
