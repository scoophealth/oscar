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
	private boolean enableCbiForm = false;
	private boolean enableAnonymous = false;
	private String ocanServiceOrgNumber;
	private boolean enableGroupNotes = false;
	private boolean enableEncounterTime = false;
	private boolean enableEncounterTransportationTime = false;
	private int rxInteractionWarningLevel = 0;
	private String vacancyWithdrawnTicklerProvider = null;
	private Integer vacancyWithdrawnTicklerDemographic = null;
	private Integer registrationIntake;
	private int displayAllVacancies=1;
	private String assignNewVacancyTicklerProvider = null;
	private Integer assignNewVacancyTicklerDemographic = null;
    private String assignRejectedVacancyApplicant = null;
  
    public int getDisplayAllVacancies() {
    	return displayAllVacancies;
    }

    

	public void setDisplayAllVacancies(int displayAllVacancies) {
    	this.displayAllVacancies = displayAllVacancies;
    }

	public Integer getRegistrationIntake() {
	    return registrationIntake;
	}

	public void setRegistrationIntake(Integer registrationIntake) {
        this.registrationIntake = registrationIntake;
    }

    public boolean isEnablePhoneEncounter() {
        return enablePhoneEncounter;
    }

    public void setEnablePhoneEncounter(boolean enablePhoneEncounter) {
        this.enablePhoneEncounter = enablePhoneEncounter;
    }

    private boolean enablePhoneEncounter = false;
	
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
	
	
	public boolean isEnableCbiForm() {
    	return enableCbiForm;
    }

	public void setEnableCbiForm(boolean enableCbiForm) {
    	this.enableCbiForm = enableCbiForm;
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

	public boolean isEnableEncounterTime() {
    	return enableEncounterTime;
    }

	public void setEnableEncounterTime(boolean enableEncounterTime) {
    	this.enableEncounterTime = enableEncounterTime;
    }

	public boolean isEnableEncounterTransportationTime() {
    	return enableEncounterTransportationTime;
    }

	public void setEnableEncounterTransportationTime(boolean enableEncounterTransportationTime) {
    	this.enableEncounterTransportationTime = enableEncounterTransportationTime;
    }

	public int getRxInteractionWarningLevel() {
    	return rxInteractionWarningLevel;
    }

	public void setRxInteractionWarningLevel(int rxInteractionWarningLevel) {
    	this.rxInteractionWarningLevel = rxInteractionWarningLevel;
    }

	public String getVacancyWithdrawnTicklerProvider() {
		return vacancyWithdrawnTicklerProvider;
	}

	public void setVacancyWithdrawnTicklerProvider(String vacancyWithdrawnTicklerProvider) {
		this.vacancyWithdrawnTicklerProvider = vacancyWithdrawnTicklerProvider;
	}

	public Integer getVacancyWithdrawnTicklerDemographic() {
		return vacancyWithdrawnTicklerDemographic;
	}

	public void setVacancyWithdrawnTicklerDemographic(Integer vacancyWithdrawnTicklerDemographic) {
		this.vacancyWithdrawnTicklerDemographic = vacancyWithdrawnTicklerDemographic;
	}

	public String getAssignNewVacancyTicklerProvider() {
		return assignNewVacancyTicklerProvider;
	}

	public void setAssignNewVacancyTicklerProvider(String assignNewVacancyTicklerProvider) {
		this.assignNewVacancyTicklerProvider = assignNewVacancyTicklerProvider;
	}

	public Integer getAssignNewVacancyTicklerDemographic() {
		return assignNewVacancyTicklerDemographic;
	}

	public void setAssignNewVacancyTicklerDemographic(Integer assignNewVacancyTicklerDemographic) {
		this.assignNewVacancyTicklerDemographic = assignNewVacancyTicklerDemographic;
	}



	public String getAssignRejectedVacancyApplicant() {
		return assignRejectedVacancyApplicant;
	}



	public void setAssignRejectedVacancyApplicant(String assignRejectedVacancyApplicant) {
		this.assignRejectedVacancyApplicant = assignRejectedVacancyApplicant;
	}

	
	
}
