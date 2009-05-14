package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.apache.commons.lang.StringUtils;

/**
 */
@Entity
public class IntegratorConsent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = null;
	/** This is the facility which is creating this record, i.e. the facility of the provider making this change. */
	private Integer facilityId = -1;
	private Integer demographicId = -1;
	private String providerNo = null;
	private Date createdDate = new Date();

	/** This is the facilityId on the integrator - for which these consents apply. */
	private Integer integratorFacilityId = null;

	private boolean consentToShareData = false;
	private boolean excludeMentalHealthData = false;

	private Integer digitalSignatureId = null;

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getIntegratorFacilityId() {
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId) {
		this.integratorFacilityId = integratorFacilityId;
	}

	public Integer getId() {
		return id;
	}

	public boolean isConsentToShareData() {
		return consentToShareData;
	}

	public void setConsentToShareData(boolean consentToShareData) {
		this.consentToShareData = consentToShareData;
	}

	public boolean isExcludeMentalHealthData() {
		return excludeMentalHealthData;
	}

	public void setExcludeMentalHealthData(boolean excludeMentalHealthData) {
		this.excludeMentalHealthData = excludeMentalHealthData;
	}

	public Integer getDigitalSignatureId() {
		return digitalSignatureId;
	}

	public void setDigitalSignatureId(Integer digitalSignatureId) {
		this.digitalSignatureId = digitalSignatureId;
	}

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
}