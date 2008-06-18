package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class IntegratorConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;
    private int facilityId = 0;
    private int demographicId = 0;
    private String provider_no = null;
    @Version
    private Date lastUpdate = null;

	private boolean consentToStatistics = false;
	private boolean consentToBasicPersonalId = false;
	private boolean consentToHealthCardId = false;
	private boolean consentToIssues = false;
	private boolean consentToNotes = false;
	private boolean restrictConsentToHic=false;
	
    public boolean isRestrictConsentToHic()
    {
    	return restrictConsentToHic;
    }

	public void setRestrictConsentToHic(boolean restrictConsentToHic)
    {
    	this.restrictConsentToHic = restrictConsentToHic;
    }

	public int getId() {
        return id;
    }

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

    public String getProvider_no() {
        return provider_no;
    }

    public void setProvider_no(String provider_no) {
        this.provider_no = provider_no;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

	public boolean isConsentToStatistics()
    {
    	return consentToStatistics;
    }

	public void setConsentToStatistics(boolean consentToStatistics)
    {
    	this.consentToStatistics = consentToStatistics;
    }

	public boolean isConsentToBasicPersonalId()
    {
    	return consentToBasicPersonalId;
    }

	public void setConsentToBasicPersonalId(boolean consentToBasicPersonalId)
    {
    	this.consentToBasicPersonalId = consentToBasicPersonalId;
    }

	public boolean isConsentToHealthCardId()
    {
    	return consentToHealthCardId;
    }

	public void setConsentToHealthCardId(boolean consentToHealthCardId)
    {
    	this.consentToHealthCardId = consentToHealthCardId;
    }

	public boolean isConsentToIssues()
    {
    	return consentToIssues;
    }

	public void setConsentToIssues(boolean consentToIssues)
    {
    	this.consentToIssues = consentToIssues;
    }

	public boolean isConsentToNotes()
    {
    	return consentToNotes;
    }

	public void setConsentToNotes(boolean consentToNotes)
    {
    	this.consentToNotes = consentToNotes;
    }

}