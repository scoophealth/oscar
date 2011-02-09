package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Facility extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private String contactName;
	private String contactEmail;
	private String contactPhone;
	private boolean hic;
	private boolean disabled;
	private Integer orgId;
	private Integer sectorId;
	private boolean integratorEnabled = false;
	private String integratorUrl = null;
	private String integratorUser = null;
	private String integratorPassword = null;
	private boolean enableIntegratedReferrals = true;
	private boolean enableHealthNumberRegistry = true;
	private boolean allowSims = true;
	private boolean enableDigitalSignatures = false;
	private boolean enableOcanForms = false;
	private boolean enableAnonymous = false;
	private String ocanServiceOrgNumber;
	private boolean enableGroupNotes = false;


	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated=new Date();
	
	
	public boolean isEnableIntegratedReferrals() {
		return enableIntegratedReferrals;
	}

	public boolean isEnableHealthNumberRegistry() {
		return enableHealthNumberRegistry;
	}

	public void setEnableHealthNumberRegistry(boolean enableHealthNumberRegistry) {
		this.enableHealthNumberRegistry = enableHealthNumberRegistry;
	}

	public void setEnableIntegratedReferrals(boolean enableIntegratedReferrals) {
		this.enableIntegratedReferrals = enableIntegratedReferrals;
	}

	public Facility() {
	}

	public Facility(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getNameJs() {
		return oscar.Misc.getStringJs(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public boolean isHic() {
		return hic;
	}

	public void setHic(boolean hic) {
		this.hic = hic;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getSectorId() {
		return sectorId;
	}

	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}

	public boolean isIntegratorEnabled() {
		return integratorEnabled;
	}

	public void setIntegratorEnabled(boolean integratorEnabled) {
		this.integratorEnabled = integratorEnabled;
	}

	public String getIntegratorUrl() {
		return integratorUrl;
	}

	public void setIntegratorUrl(String integratorUrl) {
		this.integratorUrl = integratorUrl;
	}

	public String getIntegratorUser() {
		return integratorUser;
	}

	public void setIntegratorUser(String integratorUser) {
		this.integratorUser = integratorUser;
	}

	public String getIntegratorPassword() {
		return integratorPassword;
	}

	public void setIntegratorPassword(String integratorPassword) {
		this.integratorPassword = integratorPassword;
	}

	public boolean isAllowSims() {
		return allowSims;
	}

	public void setAllowSims(boolean allowSims) {
		this.allowSims = allowSims;
	}

	public boolean isEnableDigitalSignatures() {
		return enableDigitalSignatures;
	}

	public void setEnableDigitalSignatures(boolean enableDigitalSignatures) {
		this.enableDigitalSignatures = enableDigitalSignatures;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public boolean isEnableOcanForms() {
    	return enableOcanForms;
    }

	public void setEnableOcanForms(boolean enableOcanForms) {
    	this.enableOcanForms = enableOcanForms;
    }
	
	
	public String getOcanServiceOrgNumber() {
		return ocanServiceOrgNumber;
	}

	public void setOcanServiceOrgNumber(String ocanServiceOrgNumber) {
		this.ocanServiceOrgNumber = ocanServiceOrgNumber;
	}

	@PreUpdate
	protected void jpaUpdateLastUpdateTime() {
		lastUpdated = new Date();
	}

	public boolean isEnableAnonymous() {
		return enableAnonymous;
	}

	public void setEnableAnonymous(boolean enableAnonymous) {
		this.enableAnonymous = enableAnonymous;
	}

	public boolean isEnableGroupNotes() {
		return enableGroupNotes;
	}

	public void setEnableGroupNotes(boolean enableGroupNotes) {
		this.enableGroupNotes = enableGroupNotes;
	}

}
