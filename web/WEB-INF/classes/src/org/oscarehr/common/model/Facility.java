package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Facility implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private boolean integratorEnabled=false;
    private String integratorUrl=null;
    private String integratorUser=null;
    private String integratorPassword=null;
	@Temporal(TemporalType.TIMESTAMP)
    private Date integratorLastPushTime=null;
    private boolean allowQuickConsent=true;
    private boolean enableIntegratedReferrals=true;
    //private String lastUpdateUser;
    //private Date lastUpdateDate;
    
    public boolean isEnableIntegratedReferrals() {
		return enableIntegratedReferrals;
	}

	public void setEnableIntegratedReferrals(boolean enableIntegratedReferrals) {
		this.enableIntegratedReferrals = enableIntegratedReferrals;
	}

	public boolean isAllowQuickConsent() {
		return allowQuickConsent;
	}

	public void setAllowQuickConsent(boolean allowQuickConsent) {
		this.allowQuickConsent = allowQuickConsent;
	}

	public Facility() {
    }

    public Facility(String name, String description) {
        this.name = name;
        this.description = description;
    }

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

    public Date getIntegratorLastPushTime() {
        return integratorLastPushTime;
    }

    public void setIntegratorLastPushTime(Date integratorLastPushTime) {
        this.integratorLastPushTime = integratorLastPushTime;
    }
/*
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
*/	
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Facility facility = (Facility) o;

        if (id != null ? !id.equals(facility.id) : facility.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

}
