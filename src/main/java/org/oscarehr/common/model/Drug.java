/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;
import oscar.util.UtilDateUtilities;

@Entity
@Table(name = "drugs")
public class Drug extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "drugid")
	private Integer id = null;
	@Column(name = "provider_no")
	private String providerNo = null;
	@Column(name = "demographic_no")
	private Integer demographicId = null;
	@Column(name = "rx_date")
	@Temporal(TemporalType.DATE)
	private Date rxDate = new Date();
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate = null;
	@Column(name = "written_date")
	@Temporal(TemporalType.DATE)
	private Date writtenDate = null;
	@Column(name = "BN")
	private String brandName = null;
	@Column(name = "GCN_SEQNO")
	private int gcnSeqNo = 0;
	private String customName = null;
	@Column(name = "takemin")
	private float takeMin = 0;
	@Column(name = "takemax")
	private float takeMax = 0;
	@Column(name = "freqcode")
	private String freqCode = null;
	private String duration = null;
	@Column(name = "durunit")
	private String durUnit = null;
	private String quantity = null;
	private String dispensingUnits = null;
	@Column(name = "`repeat`")
	private Integer repeat = 0;
	@Column(name = "last_refill_date")
	@Temporal(TemporalType.DATE)
	private Date lastRefillDate = null;
	@Column(name = "nosubs")
	private boolean noSubs;
	private boolean prn;
	private String special = null;
	private String special_instruction = null;
	private boolean archived;
	@Column(name = "archived_reason")
	private String archivedReason;
	@Column(name = "archived_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date archivedDate;
	@Column(name = "GN")
	private String genericName = null;
	@Column(name = "ATC")
	private String atc = null;
	@Column(name = "script_no")
	private Integer scriptNo = 0;
	@Column(name = "regional_identifier")
	private String regionalIdentifier = null;
	private String unit = null;
	private String method = null;
	private String route = null;
	@Column(name = "drug_form")
	private String drugForm = null;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	private String dosage = null;
	@Column(name = "custom_instructions")
	private boolean customInstructions;
	private String unitName = null;
	@Column(name = "long_term")
	private Boolean longTerm = false;
	@Column(name = "short_term")
	private Boolean shortTerm = false;
	@Column(name = "past_med")
	private boolean pastMed;
	@Column(name = "patient_compliance")
	private Boolean patientCompliance = null;
	@Column(name = "outside_provider_name")
	private String outsideProviderName = null;
	@Column(name = "outside_provider_ohip")
	private String outsideProviderOhip = null;
	@Column(name = "hide_from_drug_profile")
	private Boolean hideFromDrugProfile;
	@Column(name = "custom_note")
	private Boolean customNote = false;
	@Column(name = "non_authoritative")
	private Boolean nonAuthoritative = false;
	@Column(name = "pickup_datetime")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date pickupDateTime;
	private String eTreatmentType = null;
	private String rxStatus = null;
	@Column(name = "hide_cpp")
	private boolean hideFromCpp;
	@Column(name = "refill_duration")
	private Integer refillDuration;
	@Column(name = "refill_quantity")
	private Integer refillQuantity;
	@Column(name = "dispense_interval")
	private Integer dispenseInterval;
	@Column(name = "position")
	private Integer position;
	@Column(name = "start_date_unknown")
	private boolean startDateUnknown;
	private String comment;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	
	private Boolean dispenseInternal = false;

	// ///
	@Transient
	private String remoteFacilityName = null;
	@Transient
	private Integer remoteFacilityId = null;

	public static final String DELETED = "deleted";
	public static final String DOSE_CHANGE = "doseChange";
	public static final String NOT_SELECTED = "notSelected";
	public static final String UNKNOWN = "unknown";
	public static final String ADVERSE_REACTION = "adverseReaction";
	public static final String ALLERGY = "allergy";
	public static final String INEFFECTIVE_TREATMENT = "ineffectiveTreatment";
	public static final String PRESCRIBING_ERROR = "prescribingError";
	public static final String NO_LONGER_NECESSARY = "noLongerNecessary";
	public static final String SIMPLIFYING_TREATMENT = "simplifyingTreatment";
	public static final String PATIENT_REQUEST = "patientRequest";
    public static final String REPRESCRIBED = "represcribed";
	public static final String NEW_SCIENTIFIC_EVIDENCE = "newScientificEvidence";
	public static final String INCREASED_RISK_BENEFIT_RATIO = "increasedRiskBenefitRatio";
	public static final String DISCONTINUED_BY_ANOTHER_PHYSICIAN = "discontinuedByAnotherPhysician";
	public static final String COST = "cost";
	public static final String DRUG_INTERACTION = "drugInteraction";
	public static final String OTHER = "other";

	public Drug() {
	}

	public Drug(RxPrescriptionData.Prescription drug) {
		id = drug.getDrugId();
		this.providerNo = drug.getProviderNo();
		this.demographicId = drug.getDemographicNo();
		this.rxDate = drug.getRxDate();
		this.endDate = drug.getEndDate();
		this.writtenDate = drug.getWrittenDate();
		this.brandName = drug.getBrandName();
		this.gcnSeqNo = drug.getGCN_SEQNO();
		this.customName = drug.getCustomName();
		this.takeMin = drug.getTakeMin();
		this.takeMax = drug.getTakeMax();
		this.freqCode = drug.getFrequencyCode();
		this.duration = drug.getDuration();
		this.durUnit = drug.getDurationUnit();
		this.dispensingUnits = drug.getDispensingUnits();
		this.quantity = drug.getQuantity();
		this.repeat = drug.getRepeat();
		this.lastRefillDate = drug.getLastRefillDate();
		this.noSubs = drug.getNosubs();
		this.prn = drug.getPrn();
		this.special = drug.getSpecial();
		this.special_instruction = drug.getSpecialInstruction();
		this.archived = drug.isArchived();
		this.archivedReason = drug.getLastArchReason();
		this.archivedDate = drug.getArchivedDate();
		this.genericName = drug.getGenericName();
		this.atc = drug.getAtcCode();
		if (drug.getScript_no() == null || drug.getScript_no().trim().length() == 0) this.scriptNo = null;
		else this.scriptNo = Integer.parseInt(drug.getScript_no());
		this.regionalIdentifier = drug.getRegionalIdentifier();
		this.unit = drug.getUnit();
		this.method = drug.getMethod();
		this.route = drug.getRoute();
		this.drugForm = drug.getDrugForm();
		this.createDate = drug.getRxCreatedDate();
		this.dosage = drug.getDosage();
		this.customInstructions = drug.getCustomInstr();
		this.unitName = drug.getUnitName();
		this.longTerm = drug.isLongTerm();
		this.shortTerm = drug.getShortTerm();
		this.pastMed = drug.isPastMed();
		this.patientCompliance = drug.getPatientCompliance();
		this.outsideProviderName = drug.getOutsideProviderName();
		this.outsideProviderOhip = drug.getOutsideProviderOhip();
		this.hideFromDrugProfile = false;
		this.customNote = drug.isCustomNote();
		this.eTreatmentType = drug.getETreatmentType();
		this.rxStatus = drug.getRxStatus();
		this.refillDuration = drug.getRefillDuration();
		this.refillQuantity = drug.getRefillQuantity();
		this.dispenseInterval = drug.getDispenseInterval();
		this.dispenseInternal = drug.getDispenseInternal();
	}

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}

	public void setId(Integer i) {
		this.id = i;
	}

	public boolean isCustom() {
		boolean b = false;

		if (this.customName != null && !this.customName.equalsIgnoreCase("null")) {
			b = true;
		} else if (this.gcnSeqNo == 0) {
			b = true;
		}
		return b;
	}

	public String getDrugName() {
		String ret;
		if (this.isCustom()) {

			if (this.customName != null) {
				ret = this.customName + " ";
			} else {
				ret = "Unknown ";
			}
		} else {
			ret = this.getBrandName() + " ";
		}
		return ret;
	}

	public Boolean isHideFromDrugProfile() {
		if (hideFromDrugProfile == null) hideFromDrugProfile = false;
		return hideFromDrugProfile;
	}

	public void setHideFromDrugProfile(Boolean c) {
		this.hideFromDrugProfile = c;
	}

	public String getDosageDisplay() {
		String ret = "";
		if(!(Math.abs(this.getTakeMin() - this.getTakeMax()) <  0.00000001 )) {
			ret += RxUtil.FloatToString(this.getTakeMin()) + "-" + RxUtil.FloatToString(this.getTakeMax());
		} else {
			ret += RxUtil.FloatToString(this.getTakeMin());
		}
		return ret;
	}

	public boolean isDeleted() {
		if (isArchived() && DELETED.equals(getArchivedReason())) {
			return true;
		}
		return false;
	}

	public boolean isDiscontinued() {
		String ar = getArchivedReason();
		if (isArchived()
		        && (UNKNOWN.equals(ar) || NOT_SELECTED.equals(ar) || DOSE_CHANGE.equals(ar) || ADVERSE_REACTION.equals(ar) || ALLERGY.equals(ar) || INEFFECTIVE_TREATMENT.equals(ar) || PRESCRIBING_ERROR.equals(ar) || NO_LONGER_NECESSARY.equals(ar) || SIMPLIFYING_TREATMENT.equals(ar) || PATIENT_REQUEST.equals(ar) || NEW_SCIENTIFIC_EVIDENCE.equals(ar) || INCREASED_RISK_BENEFIT_RATIO.equals(ar) || DISCONTINUED_BY_ANOTHER_PHYSICIAN.equals(ar) || COST.equals(ar) || DRUG_INTERACTION.equals(ar) || REPRESCRIBED.equals(ar)  || OTHER.equals(ar))) {
			return true;
		}
		return false;
	}

	public Boolean isNonAuthoritative() {
		if (nonAuthoritative == null) return false;
		return nonAuthoritative;
	}

	public void setNonAuthoritative(Boolean nonAuthoritative) {
		this.nonAuthoritative = nonAuthoritative;
	}

	public Integer getRefillDuration() {
		return refillDuration;
	}

	public void setRefillDuration(int refillDuration) {
		this.refillDuration = refillDuration;
	}

	public Integer getRefillQuantity() {
		return refillQuantity;
	}

	public void setRefillQuantity(int refillQuantity) {
		this.refillQuantity = refillQuantity;
	}

	public Integer getDispenseInterval() {
		return dispenseInterval;
	}

	public void setDispenseInterval(int dispenseInterval) {
		this.dispenseInterval = dispenseInterval;
	}

	public Date getPickUpDateTime() {
		return pickupDateTime;
	}

	public void setPickUpDateTime(Date pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public void setETreatmentType(String eTreatmentType) {
		this.eTreatmentType = StringUtils.trimToNull(eTreatmentType);
	}

	public String getETreatmentType() {
		return eTreatmentType;
	}

	public void setRxStatus(String status) {
		this.rxStatus = StringUtils.trimToNull(status);
	}

	public String getRxStatus() {
		return rxStatus;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public boolean getHideFromCpp() {
		return hideFromCpp;
	}

	public void setHideFromCpp(boolean hideFromCpp) {
		this.hideFromCpp = hideFromCpp;
	}

	public Date getRxDate() {
		return rxDate;
	}

	public void setRxDate(Date rxDate) {
		this.rxDate = rxDate;
	}

	public Date getEndDate() {
		//if (this.isDiscontinued()) return archivedDate;
		//else return endDate;
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = StringUtils.trimToNull(brandName);
	}

	public int getGcnSeqNo() {
		return gcnSeqNo;
	}

	public void setGcnSeqNo(int gcnSeqNo) {
		this.gcnSeqNo = gcnSeqNo;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = StringUtils.trimToNull(customName);
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

	public String getFreqCode() {
		return freqCode;
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = StringUtils.trimToNull(freqCode);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = StringUtils.trimToNull(duration);
	}

	public String getDurUnit() {
		return durUnit;
	}

	public void setDurUnit(String durUnit) {
		this.durUnit = StringUtils.trimToNull(durUnit);
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = StringUtils.trimToNull(quantity);
	}

	public String getDispensingUnits() {
		return dispensingUnits;
	}

	public void setDispensingUnits(String dispensingUnits) {
		this.dispensingUnits = StringUtils.trimToNull(dispensingUnits);
	}

	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Date getLastRefillDate() {
		return lastRefillDate;
	}

	public void setLastRefillDate(Date lastRefillDate) {
		this.lastRefillDate = lastRefillDate;
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
		this.special = StringUtils.trimToNull(special);
	}

	public String getSpecialInstruction() {
		return special_instruction;
	}

	public void setSpecialInstruction(String si) {
		special_instruction = StringUtils.trimToNull(si);
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = StringUtils.trimToNull(genericName);
	}

	public String getAtc() {
		return atc;
	}

	public void setAtc(String atc) {
		this.atc = StringUtils.trimToNull(atc);
	}

	public Integer getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(Integer scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getRegionalIdentifier() {
		return regionalIdentifier;
	}

	public void setRegionalIdentifier(String regionalIdentifier) {
		this.regionalIdentifier = StringUtils.trimToNull(regionalIdentifier);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = StringUtils.trimToNull(unit);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = StringUtils.trimToNull(method);
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = StringUtils.trimToNull(route);
	}

	public String getDrugForm() {
		return drugForm;
	}

	public void setDrugForm(String drugForm) {
		this.drugForm = StringUtils.trimToNull(drugForm);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = StringUtils.trimToNull(dosage);
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
		this.unitName = StringUtils.trimToNull(unitName);
	}

	public Boolean getLongTerm() {
		if (longTerm == null) longTerm = false;
		return longTerm;
	}

	public Boolean isLongTerm() {
		if (longTerm == null) longTerm = false;
		return longTerm;
	}

	public void setLongTerm(Boolean longTerm) {
		this.longTerm = longTerm;
	}
	
	public Boolean getShortTerm() {
		if (shortTerm == null) shortTerm = false;
		return shortTerm;
	}

	public void setShortTerm(Boolean shortTerm) {
		this.shortTerm = shortTerm;
	}

	public Boolean isCustomNote() {
		if (customNote == null) customNote = false;
		return customNote;
	}

	public void setCustomNote(Boolean c) {
		this.customNote = c;
	}

	public boolean getPastMed() {		
		return pastMed;
	}

	public void setPastMed(Boolean pastMed) {
		this.pastMed = pastMed;
	}

	public Boolean getPatientCompliance() {
		return patientCompliance;
	}

	public void setPatientCompliance(Boolean patientCompliance) {
		this.patientCompliance = patientCompliance;
	}

	public Integer getId() {
		return id;
	}

	public Date getWrittenDate() {
		return writtenDate;
	}

	public void setWrittenDate(Date writtenDate) {
		this.writtenDate = writtenDate;
	}

	public boolean isExternal() {// test if prescription is external
		if (outsideProviderName == null || outsideProviderName.trim().length() == 0 || outsideProviderOhip == null || outsideProviderOhip.trim().length() == 0) return false;
		else return true;
	}

	public String getOutsideProviderName() {
		return outsideProviderName;
	}

	public void setOutsideProviderName(String outsideProviderName) {
		this.outsideProviderName = StringUtils.trimToNull(outsideProviderName);
	}

	public String getOutsideProviderOhip() {
		return outsideProviderOhip;
	}

	public void setOutsideProviderOhip(String outsideProviderOhip) {
		this.outsideProviderOhip = StringUtils.trimToNull(outsideProviderOhip);
	}

    public boolean isCurrent() {
        boolean b = false;

        try {
            GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
            cal.add(GregorianCalendar.DATE, -1);

            if (this.getEndDate().after(cal.getTime())) {
                b = true;
            }
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

    public boolean isExpired() {
		if (endDate == null) return (false);

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		return ((new Date()).after(cal.getTime()));
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Drug drug = (Drug) o;

		if (id != null ? !id.equals(drug.id) : drug.id != null) return false;

		return true;
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return the archivedReason
	 */
	public String getArchivedReason() {
		return archivedReason;
	}

	/**
	 * @param archivedReason the archivedReason to set
	 */
	public void setArchivedReason(String archivedReason) {
		this.archivedReason = StringUtils.trimToNull(archivedReason);
	}

	/**
	 * @return the archivedDate
	 */
	public Date getArchivedDate() {
		return archivedDate;
	}

	/**
	 * @param archivedDate the archivedDate to set
	 */
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	// EXTRA METHODS

	public String getAuditString() {
		return getFullOutLine();
	}

	public String getFullOutLine() {
		return (getFullOutLine(getSpecial()));
	}

	public static String getFullOutLine(String special) {
		String ret = "";
		if (special != null) {
			if (special.length() > 0) {
				int i;
				String[] arr = special.split("\n");
				for (i = 0; i < arr.length; i++) {
					ret += arr[i].trim();
					if (i < arr.length - 1) {
						ret += "; ";
					}
				}
			}
		}
		// else {
		// logger.error("Drugs special field was null, this means nothing will print.", new IllegalStateException("Drugs special field was null."));
		// }

		return ret;
	}

	/**
	 * @return the remoteFacilityName
	 */
	public String getRemoteFacilityName() {
		return remoteFacilityName;
	}

	/**
	 * @param remoteFacilityName the remoteFacilityName to set
	 */
	public void setRemoteFacilityName(String remoteFacilityName) {
		this.remoteFacilityName = remoteFacilityName;
	}

	public Integer getRemoteFacilityId() {
		return (remoteFacilityId);
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}

	public long daysToExpire() {
		long days = UtilDateUtilities.getNumDays(new Date(), getEndDate());
		if (days < 0) {
			days = 0;
		}
		return days;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public boolean getStartDateUnknown() {
		return startDateUnknown;
	}

	public void setStartDateUnknown(boolean startDateUnknown) {
		this.startDateUnknown = startDateUnknown;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = StringUtils.trimToNull(comment);
	}

	public Date getLastUpdateDate() {
    	return (lastUpdateDate);
    }

	public void setLastUpdateDate(Date lastUpdateDate) {
    	this.lastUpdateDate = lastUpdateDate;
    }

	public Boolean getDispenseInternal() {
		return dispenseInternal;
	}

	public void setDispenseInternal(Boolean dispenseInternal) {
		this.dispenseInternal = dispenseInternal;
	}



	//Sorts Ids in descending order
	public static class ComparatorIdDesc implements Comparator<Drug> {
		public int compare(Drug d1, Drug d2) {
			if( d1 == null && d2 == null )
				return 0;
			
			if( d1 == null )
				return 1;
			
			if( d2 == null ) 
				return -1;
			
			return d2.getId().compareTo(d1.getId());				
			
		}
	}

}
