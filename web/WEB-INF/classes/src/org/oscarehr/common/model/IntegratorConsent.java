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
 * Here's my futile attempt at describing the consent requirements. 1) Consents are segmented by groups, these groups are "real world" data groupings of problems, they are not data-centric groupings. i.e. "mental health" is a group, where a "notes" is not,
 * the notes must actually be broken down into groupings like "physical health" and "mental health" 2) Consents are segmented by facilities, i.e. each facility must have it's own bit for each group. In other words consent is not just a 1D matrix of bits,
 * it's a 2D matrix, where one of the dimensions is the above groups, and the other is a list of facilities. 3) Consent is applied across all facilities with in the integrated community, but is set-able by any facility. i.e. any consent changes made on any
 * facility is reflected across all facilities. <br />
 * <br />
 * Each consent entity instance represents only 1D of the 2D consent matrix.
 */
@Entity
public class IntegratorConsent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	/** This is the facility which is creating this record, i.e. the facility of the provider making this change. */
	private int facilityId;
	private int demographicId;
	private String providerNo = null;
	private Date createdDate = new Date();

	/** This is the facilityId on the integrator - for which these consents apply. */
	private Integer integratorFacilityId = null;
	private boolean restrictConsentToHic = false;

	private boolean consentToSearches = false;
	private boolean consentToBasicPersonalData = false;
	private boolean consentToMentalHealthData = false;
	private boolean consentToHealthNumberRegistry = false;

	private String formVersion=null;
	private String printedFormLocation = null;
	private boolean refusedToSign = false;

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public int getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(int demographicId) {
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

	public boolean isConsentToSearches() {
		return consentToSearches;
	}

	public void setConsentToSearches(boolean consentToSearches) {
		this.consentToSearches = consentToSearches;
	}

	public boolean isConsentToBasicPersonalData() {
		return consentToBasicPersonalData;
	}

	public void setConsentToBasicPersonalData(boolean consentToBasicPersonalData) {
		this.consentToBasicPersonalData = consentToBasicPersonalData;
	}

	public boolean isConsentToMentalHealthData() {
		return consentToMentalHealthData;
	}

	public void setConsentToMentalHealthData(boolean consentToMentalHealthData) {
		this.consentToMentalHealthData = consentToMentalHealthData;
	}

	public boolean isConsentToHealthNumberRegistry() {
		return consentToHealthNumberRegistry;
	}

	public void setConsentToHealthNumberRegistry(boolean consentToHealthNumberRegistry) {
		this.consentToHealthNumberRegistry = consentToHealthNumberRegistry;
	}

	public boolean isRestrictConsentToHic() {
		return restrictConsentToHic;
	}

	public void setRestrictConsentToHic(boolean restrictConsentToHic) {
		this.restrictConsentToHic = restrictConsentToHic;
	}

	public String getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(String formVersion) {
		this.formVersion = StringUtils.trimToNull(formVersion);
	}

	public String getPrintedFormLocation() {
		return printedFormLocation;
	}

	public void setPrintedFormLocation(String printedFormLocation) {
		this.printedFormLocation = StringUtils.trimToNull(printedFormLocation);
	}

	public boolean isRefusedToSign() {
		return refusedToSign;
	}

	public void setRefusedToSign(boolean refusedToSign) {
		this.refusedToSign = refusedToSign;
	}

	public Integer getId() {
		return id;
	}

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

	public boolean isConsentToAll() {
		return (consentToBasicPersonalData && consentToHealthNumberRegistry && consentToMentalHealthData && consentToSearches && !restrictConsentToHic);
	}

	public boolean isConsentToAllButRestrictToHic() {
		return (consentToBasicPersonalData && consentToHealthNumberRegistry && consentToMentalHealthData && consentToSearches && restrictConsentToHic);
	}

	public boolean isConsentToNone() {
		return (!consentToBasicPersonalData && !consentToHealthNumberRegistry && !consentToMentalHealthData && !consentToSearches);
	}

	public void setConsentToAll() {
		setAllConsents(true);
		restrictConsentToHic = false;
	}

	public void setConsentToNone() {
		setAllConsents(false);
	}

	private void setAllConsents(boolean b) {
		consentToBasicPersonalData = consentToHealthNumberRegistry = consentToMentalHealthData = consentToSearches = b;
	}
}