/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Facility;
import org.springframework.beans.BeanUtils;

public final class FacilityTransfer {
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
	private boolean integratorEnabled;
	private String integratorUrl;
	private String integratorUser;
	private String integratorPassword;
	private boolean enableIntegratedReferrals;
	private boolean enableHealthNumberRegistry;
	private boolean allowSims;
	private boolean enableDigitalSignatures;
	private boolean enableOcanForms;
	private boolean enableAnonymous;
	private String ocanServiceOrgNumber;
	private boolean enableGroupNotes;
	private boolean enableEncounterTime;
	private boolean enableEncounterTransportationTime;
	private int rxInteractionWarningLevel;
	private Date lastUpdated;

	public Integer getId() {
    	return (id);
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getName() {
    	return (name);
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getDescription() {
    	return (description);
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getContactName() {
    	return (contactName);
    }

	public void setContactName(String contactName) {
    	this.contactName = contactName;
    }

	public String getContactEmail() {
    	return (contactEmail);
    }

	public void setContactEmail(String contactEmail) {
    	this.contactEmail = contactEmail;
    }

	public String getContactPhone() {
    	return (contactPhone);
    }

	public void setContactPhone(String contactPhone) {
    	this.contactPhone = contactPhone;
    }

	public boolean isHic() {
    	return (hic);
    }

	public void setHic(boolean hic) {
    	this.hic = hic;
    }

	public boolean isDisabled() {
    	return (disabled);
    }

	public void setDisabled(boolean disabled) {
    	this.disabled = disabled;
    }

	public Integer getOrgId() {
    	return (orgId);
    }

	public void setOrgId(Integer orgId) {
    	this.orgId = orgId;
    }

	public Integer getSectorId() {
    	return (sectorId);
    }

	public void setSectorId(Integer sectorId) {
    	this.sectorId = sectorId;
    }

	public boolean isIntegratorEnabled() {
    	return (integratorEnabled);
    }

	public void setIntegratorEnabled(boolean integratorEnabled) {
    	this.integratorEnabled = integratorEnabled;
    }

	public String getIntegratorUrl() {
    	return (integratorUrl);
    }

	public void setIntegratorUrl(String integratorUrl) {
    	this.integratorUrl = integratorUrl;
    }

	public String getIntegratorUser() {
    	return (integratorUser);
    }

	public void setIntegratorUser(String integratorUser) {
    	this.integratorUser = integratorUser;
    }

	public String getIntegratorPassword() {
    	return (integratorPassword);
    }

	public void setIntegratorPassword(String integratorPassword) {
    	this.integratorPassword = integratorPassword;
    }

	public boolean isEnableIntegratedReferrals() {
    	return (enableIntegratedReferrals);
    }

	public void setEnableIntegratedReferrals(boolean enableIntegratedReferrals) {
    	this.enableIntegratedReferrals = enableIntegratedReferrals;
    }

	public boolean isEnableHealthNumberRegistry() {
    	return (enableHealthNumberRegistry);
    }

	public void setEnableHealthNumberRegistry(boolean enableHealthNumberRegistry) {
    	this.enableHealthNumberRegistry = enableHealthNumberRegistry;
    }

	public boolean isAllowSims() {
    	return (allowSims);
    }

	public void setAllowSims(boolean allowSims) {
    	this.allowSims = allowSims;
    }

	public boolean isEnableDigitalSignatures() {
    	return (enableDigitalSignatures);
    }

	public void setEnableDigitalSignatures(boolean enableDigitalSignatures) {
    	this.enableDigitalSignatures = enableDigitalSignatures;
    }

	public boolean isEnableOcanForms() {
    	return (enableOcanForms);
    }

	public void setEnableOcanForms(boolean enableOcanForms) {
    	this.enableOcanForms = enableOcanForms;
    }

	public boolean isEnableAnonymous() {
    	return (enableAnonymous);
    }

	public void setEnableAnonymous(boolean enableAnonymous) {
    	this.enableAnonymous = enableAnonymous;
    }

	public String getOcanServiceOrgNumber() {
    	return (ocanServiceOrgNumber);
    }

	public void setOcanServiceOrgNumber(String ocanServiceOrgNumber) {
    	this.ocanServiceOrgNumber = ocanServiceOrgNumber;
    }

	public boolean isEnableGroupNotes() {
    	return (enableGroupNotes);
    }

	public void setEnableGroupNotes(boolean enableGroupNotes) {
    	this.enableGroupNotes = enableGroupNotes;
    }

	public boolean isEnableEncounterTime() {
    	return (enableEncounterTime);
    }

	public void setEnableEncounterTime(boolean enableEncounterTime) {
    	this.enableEncounterTime = enableEncounterTime;
    }

	public boolean isEnableEncounterTransportationTime() {
    	return (enableEncounterTransportationTime);
    }

	public void setEnableEncounterTransportationTime(boolean enableEncounterTransportationTime) {
    	this.enableEncounterTransportationTime = enableEncounterTransportationTime;
    }

	public int getRxInteractionWarningLevel() {
    	return (rxInteractionWarningLevel);
    }

	public void setRxInteractionWarningLevel(int rxInteractionWarningLevel) {
    	this.rxInteractionWarningLevel = rxInteractionWarningLevel;
    }

	public Date getLastUpdated() {
    	return (lastUpdated);
    }

	public void setLastUpdated(Date lastUpdated) {
    	this.lastUpdated = lastUpdated;
    }

	public static FacilityTransfer toTransfer(Facility facility) {
		if (facility==null) return(null);
		
		FacilityTransfer facilityTransfer = new FacilityTransfer();

		String[] ignores={"lastUpdated"};
		BeanUtils.copyProperties(facility, facilityTransfer, ignores);

		return (facilityTransfer);
	}

	public static FacilityTransfer[] toTransfers(List<Facility> facilities) {
		ArrayList<FacilityTransfer> results = new ArrayList<FacilityTransfer>();

		for (Facility facility : facilities) {
			results.add(toTransfer(facility));
		}

		return (results.toArray(new FacilityTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
