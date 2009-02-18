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

@Entity
@Table(name = "drugs")
public class Drug implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "drugid")
	private Integer id = null;
	private String provider_no = null;
	@Column(name = "demographic_no")
	private Integer demographicId = null;
	@Column(name = "rx_date")
	@Temporal(TemporalType.DATE)
	private Date rxDate = new Date();
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate = null;
	@Column(name = "BN")
	private String bn = null;
	@Column(name = "GCN_SEQNO")
	private int gcnSeqNo = 0;
	private String customName=null;
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
	private int repeat = 0;
	@Column(name = "last_refill_date")
	@Temporal(TemporalType.DATE)
	private Date lastRefillDate = null;
	@Column(name = "nosubs")
	private boolean noSubs;
	private boolean prn;
	private String special = null;
	private boolean archived;
	@Column(name = "GN")
	private String gn = null;
	@Column(name = "ATC")
	private String atc = null;
	@Column(name = "script_no")
	private int scriptNo = 0;
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
	private Boolean longTerm;
	@Column(name = "past_med")
	private Boolean pastMed;
	@Column(name = "patient_compliance")
	private Boolean patientCompliance;

	public String getProvider_no() {
		return provider_no;
	}

	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
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
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
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

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
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

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
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

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
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
}
