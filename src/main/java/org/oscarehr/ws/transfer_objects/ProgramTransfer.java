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
import org.oscarehr.PMmodule.model.Program;
import org.springframework.beans.BeanUtils;

public final class ProgramTransfer {
	private Integer id;

	private boolean userDefined;
	private Integer numOfMembers;
	private Integer numOfIntakes;
	private Integer queueSize;
	private Integer maxAllowed;
	private String type;
	private String description;
	private String functionalCentreId;
	private String address;
	private String phone;
	private String fax;
	private String url;
	private String email;
	private String emergencyNumber;
	private String location;
	private String name;
	private boolean holdingTank;
	private boolean allowBatchAdmission;
	private boolean allowBatchDischarge;
	private boolean hic;
	private String programStatus;
	private Integer intakeProgram;
	private Integer bedProgramLinkId;
	private String manOrWoman;
	private String genderDesc;
	private boolean transgender;
	private boolean firstNation;
	private boolean bedProgramAffiliated;
	private boolean alcohol;
	private String abstinenceSupport;
	private boolean physicalHealth;
	private boolean mentalHealth;
	private boolean housing;
	private String exclusiveView;
	private Integer ageMin;
	private Integer ageMax;
	private Integer maximumServiceRestrictionDays;
	private Integer defaultServiceRestrictionDays;
	private Integer shelterId;
	private int facilityId;

	private String facilityDesc;
	private String orgCd;
	private Integer totalUsedRoom;
	private String lastUpdateUser;
	private Date lastUpdateDate;
	private String siteSpecificField;
	private Boolean enableEncounterTime = false;
	private Boolean enableEncounterTransportationTime = false;
	private String emailNotificationAddressesCsv = null;
	private Date lastReferralNotification = null;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isUserDefined() {
		return (userDefined);
	}

	public void setUserDefined(boolean userDefined) {
		this.userDefined = userDefined;
	}

	public Integer getNumOfMembers() {
		return (numOfMembers);
	}

	public void setNumOfMembers(Integer numOfMembers) {
		this.numOfMembers = numOfMembers;
	}

	public Integer getNumOfIntakes() {
		return (numOfIntakes);
	}

	public void setNumOfIntakes(Integer numOfIntakes) {
		this.numOfIntakes = numOfIntakes;
	}

	public Integer getQueueSize() {
		return (queueSize);
	}

	public void setQueueSize(Integer queueSize) {
		this.queueSize = queueSize;
	}

	public Integer getMaxAllowed() {
		return (maxAllowed);
	}

	public void setMaxAllowed(Integer maxAllowed) {
		this.maxAllowed = maxAllowed;
	}

	public String getType() {
		return (type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFunctionalCentreId() {
		return (functionalCentreId);
	}

	public void setFunctionalCentreId(String functionalCentreId) {
		this.functionalCentreId = functionalCentreId;
	}

	public String getAddress() {
		return (address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return (phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return (fax);
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getUrl() {
		return (url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEmail() {
		return (email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmergencyNumber() {
		return (emergencyNumber);
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	public String getLocation() {
		return (location);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHoldingTank() {
		return (holdingTank);
	}

	public void setHoldingTank(boolean holdingTank) {
		this.holdingTank = holdingTank;
	}

	public boolean isAllowBatchAdmission() {
		return (allowBatchAdmission);
	}

	public void setAllowBatchAdmission(boolean allowBatchAdmission) {
		this.allowBatchAdmission = allowBatchAdmission;
	}

	public boolean isAllowBatchDischarge() {
		return (allowBatchDischarge);
	}

	public void setAllowBatchDischarge(boolean allowBatchDischarge) {
		this.allowBatchDischarge = allowBatchDischarge;
	}

	public boolean isHic() {
		return (hic);
	}

	public void setHic(boolean hic) {
		this.hic = hic;
	}

	public String getProgramStatus() {
		return (programStatus);
	}

	public void setProgramStatus(String programStatus) {
		this.programStatus = programStatus;
	}

	public Integer getIntakeProgram() {
		return (intakeProgram);
	}

	public void setIntakeProgram(Integer intakeProgram) {
		this.intakeProgram = intakeProgram;
	}

	public Integer getBedProgramLinkId() {
		return (bedProgramLinkId);
	}

	public void setBedProgramLinkId(Integer bedProgramLinkId) {
		this.bedProgramLinkId = bedProgramLinkId;
	}

	public String getManOrWoman() {
		return (manOrWoman);
	}

	public void setManOrWoman(String manOrWoman) {
		this.manOrWoman = manOrWoman;
	}

	public String getGenderDesc() {
		return (genderDesc);
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public boolean isTransgender() {
		return (transgender);
	}

	public void setTransgender(boolean transgender) {
		this.transgender = transgender;
	}

	public boolean isFirstNation() {
		return (firstNation);
	}

	public void setFirstNation(boolean firstNation) {
		this.firstNation = firstNation;
	}

	public boolean isBedProgramAffiliated() {
		return (bedProgramAffiliated);
	}

	public void setBedProgramAffiliated(boolean bedProgramAffiliated) {
		this.bedProgramAffiliated = bedProgramAffiliated;
	}

	public boolean isAlcohol() {
		return (alcohol);
	}

	public void setAlcohol(boolean alcohol) {
		this.alcohol = alcohol;
	}

	public String getAbstinenceSupport() {
		return (abstinenceSupport);
	}

	public void setAbstinenceSupport(String abstinenceSupport) {
		this.abstinenceSupport = abstinenceSupport;
	}

	public boolean isPhysicalHealth() {
		return (physicalHealth);
	}

	public void setPhysicalHealth(boolean physicalHealth) {
		this.physicalHealth = physicalHealth;
	}

	public boolean isMentalHealth() {
		return (mentalHealth);
	}

	public void setMentalHealth(boolean mentalHealth) {
		this.mentalHealth = mentalHealth;
	}

	public boolean isHousing() {
		return (housing);
	}

	public void setHousing(boolean housing) {
		this.housing = housing;
	}

	public String getExclusiveView() {
		return (exclusiveView);
	}

	public void setExclusiveView(String exclusiveView) {
		this.exclusiveView = exclusiveView;
	}

	public Integer getAgeMin() {
		return (ageMin);
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getAgeMax() {
		return (ageMax);
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public Integer getMaximumServiceRestrictionDays() {
		return (maximumServiceRestrictionDays);
	}

	public void setMaximumServiceRestrictionDays(Integer maximumServiceRestrictionDays) {
		this.maximumServiceRestrictionDays = maximumServiceRestrictionDays;
	}

	public Integer getDefaultServiceRestrictionDays() {
		return (defaultServiceRestrictionDays);
	}

	public void setDefaultServiceRestrictionDays(Integer defaultServiceRestrictionDays) {
		this.defaultServiceRestrictionDays = defaultServiceRestrictionDays;
	}

	public Integer getShelterId() {
		return (shelterId);
	}

	public void setShelterId(Integer shelterId) {
		this.shelterId = shelterId;
	}

	public int getFacilityId() {
		return (facilityId);
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityDesc() {
		return (facilityDesc);
	}

	public void setFacilityDesc(String facilityDesc) {
		this.facilityDesc = facilityDesc;
	}

	public String getOrgCd() {
		return (orgCd);
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	public Integer getTotalUsedRoom() {
		return (totalUsedRoom);
	}

	public void setTotalUsedRoom(Integer totalUsedRoom) {
		this.totalUsedRoom = totalUsedRoom;
	}

	public String getLastUpdateUser() {
		return (lastUpdateUser);
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateDate() {
		return (lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getSiteSpecificField() {
		return (siteSpecificField);
	}

	public void setSiteSpecificField(String siteSpecificField) {
		this.siteSpecificField = siteSpecificField;
	}

	public Boolean getEnableEncounterTime() {
		return (enableEncounterTime);
	}

	public void setEnableEncounterTime(Boolean enableEncounterTime) {
		this.enableEncounterTime = enableEncounterTime;
	}

	public Boolean getEnableEncounterTransportationTime() {
		return (enableEncounterTransportationTime);
	}

	public void setEnableEncounterTransportationTime(Boolean enableEncounterTransportationTime) {
		this.enableEncounterTransportationTime = enableEncounterTransportationTime;
	}

	public String getEmailNotificationAddressesCsv() {
		return (emailNotificationAddressesCsv);
	}

	public void setEmailNotificationAddressesCsv(String emailNotificationAddressesCsv) {
		this.emailNotificationAddressesCsv = emailNotificationAddressesCsv;
	}

	public Date getLastReferralNotification() {
		return (lastReferralNotification);
	}

	public void setLastReferralNotification(Date lastReferralNotification) {
		this.lastReferralNotification = lastReferralNotification;
	}

	public static ProgramTransfer toTransfer(Program program) {
		if (program == null) return (null);

		ProgramTransfer programTransfer = new ProgramTransfer();

		BeanUtils.copyProperties(program, programTransfer);

		return (programTransfer);
	}

	public static ProgramTransfer[] toTransfers(List<Program> programs) {
		ArrayList<ProgramTransfer> results = new ArrayList<ProgramTransfer>();

		for (Program program : programs) {
			results.add(toTransfer(program));
		}

		return (results.toArray(new ProgramTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
