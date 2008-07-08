package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;

@Entity
public class IntegratorConsent {

	@EmbeddedId
	private FacilityDemographicPrimaryKey id;
	@Column(name="provider_no")
	private String providerNo = null;
	@Version
	private Date lastUpdate = null;

	private boolean consentToStatistics = false;
	private boolean consentToBasicPersonalId = false;
	private boolean consentToHealthCardId = false;
	private boolean consentToIssues = false;
	private boolean consentToNotes = false;
	private boolean restrictConsentToHic = false;

	public void setId(FacilityDemographicPrimaryKey id) {
		this.id = id;
	}

	public boolean isRestrictConsentToHic() {
		return restrictConsentToHic;
	}

	public void setRestrictConsentToHic(boolean restrictConsentToHic) {
		this.restrictConsentToHic = restrictConsentToHic;
	}

	public FacilityDemographicPrimaryKey getId() {
		return id;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String provider_no) {
		this.providerNo = provider_no;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public boolean isConsentToStatistics() {
		return consentToStatistics;
	}

	public void setConsentToStatistics(boolean consentToStatistics) {
		this.consentToStatistics = consentToStatistics;
	}

	public boolean isConsentToBasicPersonalId() {
		return consentToBasicPersonalId;
	}

	public void setConsentToBasicPersonalId(boolean consentToBasicPersonalId) {
		this.consentToBasicPersonalId = consentToBasicPersonalId;
	}

	public boolean isConsentToHealthCardId() {
		return consentToHealthCardId;
	}

	public void setConsentToHealthCardId(boolean consentToHealthCardId) {
		this.consentToHealthCardId = consentToHealthCardId;
	}

	public boolean isConsentToIssues() {
		return consentToIssues;
	}

	public void setConsentToIssues(boolean consentToIssues) {
		this.consentToIssues = consentToIssues;
	}

	public boolean isConsentToNotes() {
		return consentToNotes;
	}

	public void setConsentToNotes(boolean consentToNotes) {
		this.consentToNotes = consentToNotes;
	}

}