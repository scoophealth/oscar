package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;

@Entity
public class IntegratorConsent {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private int facilityId;
	private int demographicId;
	private String providerNo = null;
	@Version
	private Date createdDate = null;

	private boolean consentToStatistics = false;
	private boolean consentToBasicPersonalId = false;
	private boolean consentToHealthCardId = false;
	private boolean consentToIssues = false;
	private boolean consentToNotes = false;
	private boolean consentToPreventions = false;
	private boolean consentToPhoto = false;
	private boolean restrictConsentToHic = false;

	private String formVersion;
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

	public boolean isConsentToPreventions() {
		return consentToPreventions;
	}

	public void setConsentToPreventions(boolean consentToPreventions) {
		this.consentToPreventions = consentToPreventions;
	}

	public boolean isConsentToPhoto() {
		return consentToPhoto;
	}

	public void setConsentToPhoto(boolean consentToPhoto) {
		this.consentToPhoto = consentToPhoto;
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
	protected void jpa_preventDelete()
	{
		throw(new IllegalArgumentException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate()
	{
		throw(new IllegalArgumentException("Update is not allowed for this type of item."));
	}
	
	public boolean isConsentToAll()
	{
		return(consentToStatistics&&consentToBasicPersonalId&&consentToHealthCardId&&consentToIssues&&consentToNotes&&consentToPreventions&&consentToPhoto&&!restrictConsentToHic); 
	}

	public boolean isConsentToAllHic()
	{
		return(consentToStatistics&&consentToBasicPersonalId&&consentToHealthCardId&&consentToIssues&&consentToNotes&&consentToPreventions&&consentToPhoto&&restrictConsentToHic); 
	}

	public boolean isConsentToNone()
	{
		return(!consentToStatistics&&!consentToBasicPersonalId&&!consentToHealthCardId&&!consentToIssues&&!consentToPreventions&&!consentToPhoto&&!consentToNotes);
	}
	
	public void setConsentToAll()
	{
		setAllConsents(true);
		restrictConsentToHic=false;
	}
	
	public void setConsentToNone()
	{
		setAllConsents(false);
	}
	
	private void setAllConsents(boolean b)
	{
		consentToStatistics=b;
		consentToBasicPersonalId=b;
		consentToHealthCardId=b;
		consentToIssues=b;
		consentToNotes=b;
		consentToPreventions=b;
		consentToPhoto=b;
	}
}