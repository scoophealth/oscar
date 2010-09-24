package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

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
	@Column(name = "past_med")
	private Boolean pastMed;
	@Column(name = "patient_compliance")
	private Boolean patientCompliance;
	@Column(name = "outside_provider_name")
	private String outsideProviderName = null;
	@Column(name = "outside_provider_ohip")
	private String outsideProviderOhip = null;
	@Column(name = "hide_from_drug_profile")
	private boolean hideFromDrugProfile;

	// ///
	@Transient
	private String remoteFacilityName = null;

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
	public static final String NEW_SCIENTIFIC_EVIDENCE = "newScientificEvidence";
	public static final String INCREASED_RISK_BENEFIT_RATIO = "increasedRiskBenefitRatio";
	public static final String DISCONTINUED_BY_ANOTHER_PHYSICIAN = "discontinuedByAnotherPhysician";
	public static final String COST = "cost";
	public static final String DRUG_INTERACTION = "drugInteraction";
	public static final String OTHER = "other";

	public boolean isDeleted() {
		if (isArchived() && DELETED.equals(getArchivedReason())) {
			return true;
		}
		return false;
	}

	public boolean isDiscontinued() {
            String ar=getArchivedReason();
		if (isArchived()
		        && (UNKNOWN.equals(ar)|| NOT_SELECTED.equals(ar) || DOSE_CHANGE.equals(ar) || ADVERSE_REACTION.equals(ar) || ALLERGY.equals(ar) || INEFFECTIVE_TREATMENT.equals(ar) || PRESCRIBING_ERROR.equals(ar) || NO_LONGER_NECESSARY.equals(ar) || SIMPLIFYING_TREATMENT.equals(ar) || PATIENT_REQUEST.equals(ar) || NEW_SCIENTIFIC_EVIDENCE.equals(ar)
		                || INCREASED_RISK_BENEFIT_RATIO.equals(ar) || DISCONTINUED_BY_ANOTHER_PHYSICIAN.equals(ar) || COST.equals(ar) || DRUG_INTERACTION.equals(ar) || OTHER.equals(ar))) {
			return true;
		}
		return false;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getRxDate() {
		return rxDate;
	}

	public void setRxDate(Date rxDate) {
		this.rxDate = rxDate;
	}

	public Date getEndDate() {
		if (this.isDiscontinued()) return archivedDate;
		else return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public String getDurUnit() {
		return durUnit;
	}

	public void setDurUnit(String durUnit) {
		this.durUnit = durUnit;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
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
		this.special = special;
	}

	public String getSpecialInstruction() {
		return special_instruction;
	}

	public void setSpecialInstruction(String si) {
		special_instruction = si;
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
		this.genericName = genericName;
	}

	public String getAtc() {
		return atc;
	}

	public void setAtc(String atc) {
		this.atc = atc;
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

	public Boolean getPastMed() {
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
		this.outsideProviderName = outsideProviderName;
	}

	public String getOutsideProviderOhip() {
		return outsideProviderOhip;
	}

	public void setOutsideProviderOhip(String outsideProviderOhip) {
		this.outsideProviderOhip = outsideProviderOhip;
	}

	public boolean isExpired() {
		if (endDate == null) return (false);

		return ((new Date()).after(endDate));
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
		this.archivedReason = archivedReason;
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

	public long daysToExpire() {
		long days = UtilDateUtilities.getNumDays(new Date(), getEndDate());
		if (days < 0) {
			days = 0;
		}
		return days;
	}

}
