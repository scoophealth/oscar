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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.quatro.model.LookupCodeValue;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class Program implements Serializable {
	public static final Integer DEFAULT_COMMUNITY_PROGRAM_ID = new Integer(10010);

	public static final String EXTERNAL_TYPE = "external";
	public static final String BED_TYPE = "Bed";
	public static final String COMMUNITY_TYPE = "community";
	public static final String SERVICE_TYPE = "Service";

	public static final String PROGRAM_STATUS_ACTIVE = "active";
	public static final String PROGRAM_STATUS_INACTIVE = "inactive";

	private final Integer DEFAULT_SERVICE_RESTRICTION_DAYS = 30;
	private final Integer MIN_AGE = 1;
	private final Integer MAX_AGE = 200;

	private int hashCode = Integer.MIN_VALUE;// primary key

	private Integer id;

	private boolean userDefined = true;
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
	private String programStatus = "active";
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
	private String exclusiveView = "no";
	private Integer ageMin;
	private Integer ageMax;
	private Integer maximumServiceRestrictionDays;
	private Integer defaultServiceRestrictionDays;
	private Integer shelterId;
	private int facilityId;

	private String facilityDesc;
	private String orgCd;
	private Integer capacity_funding = new Integer(0);
	private Integer capacity_space = new Integer(0);
	private Integer capacity_actual = new Integer(0);
	private Integer totalUsedRoom = new Integer(0);
	private String lastUpdateUser;
	private Date lastUpdateDate = new Date();
	private LookupCodeValue shelter;
	private String siteSpecificField;
	private Boolean enableEncounterTime = false;
	private Boolean enableEncounterTransportationTime = false;
	private String emailNotificationAddressesCsv = null;
	private Date lastReferralNotification = null;
	private boolean enableOCAN;
	
	//these are all transient - these need to be removed, we shouldn't be having fields like this in JPA model objects.
	private Integer noOfVacancy = 0;
	private String vacancyName;
	private String dateCreated;
	private double matches;
	private Integer vacancyId;
	private String vacancyTemplateName;

	/**
	 * Constructor for required fields
	 */
	public Program(Integer id, boolean isUserDefined, Integer maxAllowed, String address, String phone, String fax, String url, String email, String emergencyNumber, String name, boolean holdingTank, String programStatus) {

		setId(id);
		setUserDefined(isUserDefined);
		setMaxAllowed(maxAllowed);
		setAddress(address);
		setPhone(phone);
		setFax(fax);
		setUrl(url);
		setEmail(email);
		setEmergencyNumber(emergencyNumber);
		setName(name);
		setHoldingTank(holdingTank);
		setProgramStatus(programStatus);

	}

	public String getSiteSpecificField() {
		return siteSpecificField;
	}

	public void setSiteSpecificField(String siteSpecificField) {
		this.siteSpecificField = siteSpecificField;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public String getFunctionalCentreId() {
		return functionalCentreId;
	}

	public void setFunctionalCentreId(String functionalCentreId) {
		this.functionalCentreId = functionalCentreId;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Integer getCapacity_actual() {
		return capacity_actual;
	}

	public void setCapacity_actual(Integer capacity_actual) {
		this.capacity_actual = capacity_actual;
	}

	public Integer getCapacity_funding() {
		return capacity_funding;
	}

	public void setCapacity_funding(Integer capacity_funding) {
		this.capacity_funding = capacity_funding;
	}

	public Integer getCapacity_space() {
		return capacity_space;
	}

	public void setCapacity_space(Integer capacity_space) {
		this.capacity_space = capacity_space;
	}

	// constructors
	public Program() {
		// no arg constructor for JPA
	}

	public Integer getShelterId() {
		return shelterId;
	}

	public void setShelterId(Integer shelterId) {
		this.shelterId = shelterId;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getOrgCd() {
		return orgCd;
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	/**
	 * Constructor for primary key
	 */
	public Program(Integer id) {
		this.setId(id);
	}

	public boolean isUserDefined() {
		return userDefined;
	}

	public void setUserDefined(boolean userDefined) {
		this.userDefined = userDefined;
	}

	public boolean isActive() {
		return PROGRAM_STATUS_ACTIVE.equals(programStatus);
	}

	public boolean isFull() {
		return getNumOfMembers().intValue() >= getMaxAllowed().intValue();
	}

	public boolean isExternal() {
		return EXTERNAL_TYPE.equalsIgnoreCase(getType());
	}

	public boolean isBed() {
		return BED_TYPE.equalsIgnoreCase(getType());
	}

	public boolean isCommunity() {
		return COMMUNITY_TYPE.equalsIgnoreCase(getType());
	}

	public boolean isService() {
		return SERVICE_TYPE.equalsIgnoreCase(getType());
	}

	public boolean getHoldingTank() {
		return isHoldingTank();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: numOfMembers
	 */
	public Integer getNumOfMembers() {
		return numOfMembers;
	}

	/**
	 * Set the value related to the column: numOfMembers
	 * 
	 * @param numOfMembers
	 *            the numOfMembers value
	 */
	public void setNumOfMembers(Integer numOfMembers) {
		this.numOfMembers = numOfMembers;
	}

	/**
	 * Return the value associated with the column: queueSize
	 */
	public Integer getQueueSize() {
		return queueSize;
	}

	/**
	 * Set the value related to the column: queueSize
	 * 
	 * @param queueSize
	 *            the queueSize value
	 */
	public void setQueueSize(Integer queueSize) {
		this.queueSize = queueSize;
	}

	public Integer getMaxAllowed() {
		return maxAllowed;
	}

	public void setMaxAllowed(Integer maxAllowed) {
		this.maxAllowed = maxAllowed;
	}

	/**
	 * Return the value associated with the column: type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * 
	 * @param type
	 *            the type value
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Return the value associated with the column: address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set the value related to the column: address
	 * 
	 * @param address
	 *            the address value
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Return the value associated with the column: phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set the value related to the column: phone
	 * 
	 * @param phone
	 *            the phone value
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Return the value associated with the column: fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Set the value related to the column: fax
	 * 
	 * @param fax
	 *            the fax value
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Return the value associated with the column: url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the value related to the column: url
	 * 
	 * @param url
	 *            the url value
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the value associated with the column: email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the value related to the column: email
	 * 
	 * @param email
	 *            the email value
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmergencyNumber() {
		return emergencyNumber;
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	/**
	 * Return the value associated with the column: location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set the value related to the column: location
	 * 
	 * @param location
	 *            the location value
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Return the value associated with the column: name
	 */
	public String getName() {
		return name;
	}

	public String getNameJs() {
		return oscar.Misc.getStringJs(name);
	}

	/**
	 * Set the value related to the column: name
	 * 
	 * @param name
	 *            the name value
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isHoldingTank() {
		return holdingTank;
	}

	public void setHoldingTank(boolean holdingTank) {
		this.holdingTank = holdingTank;
	}

	public boolean isAllowBatchAdmission() {
		return allowBatchAdmission;
	}

	public void setAllowBatchAdmission(boolean allowBatchAdmission) {
		this.allowBatchAdmission = allowBatchAdmission;
	}

	public boolean isAllowBatchDischarge() {
		return allowBatchDischarge;
	}

	public void setAllowBatchDischarge(boolean allowBatchDischarge) {
		this.allowBatchDischarge = allowBatchDischarge;
	}

	/**
	 * Return the value associated with the column: hic
	 */
	public boolean isHic() {
		return hic;
	}

	/**
	 * Set the value related to the column: hic
	 * 
	 * @param hic
	 *            the hic value
	 */
	public void setHic(boolean hic) {
		this.hic = hic;
	}

	public String getProgramStatus() {
		return programStatus;
	}

	/**
	 * Set the value related to the column: program_status
	 * 
	 * @param programStatus
	 *            the program_status value
	 */
	public void setProgramStatus(String programStatus) {
		this.programStatus = programStatus;
	}

	public Integer getIntakeProgram() {
		return intakeProgram;
	}

	public void setIntakeProgram(Integer intakeProgram) {
		this.intakeProgram = intakeProgram;
	}

	public Integer getBedProgramLinkId() {
		return bedProgramLinkId;
	}

	public void setBedProgramLinkId(Integer bedProgramLinkId) {
		this.bedProgramLinkId = bedProgramLinkId;
	}

	public String getAbstinenceSupport() {
		return abstinenceSupport;
	}

	public void setAbstinenceSupport(String abstinenceSupport) {
		this.abstinenceSupport = abstinenceSupport;
	}

	public boolean isAlcohol() {
		return alcohol;
	}

	public void setAlcohol(boolean alcohol) {
		this.alcohol = alcohol;
	}

	public boolean isBedProgramAffiliated() {
		return bedProgramAffiliated;
	}

	public void setBedProgramAffiliated(boolean bedProgramAffiliated) {
		this.bedProgramAffiliated = bedProgramAffiliated;
	}

	public boolean isFirstNation() {
		return firstNation;
	}

	public void setFirstNation(boolean firstNation) {
		this.firstNation = firstNation;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public boolean isHousing() {
		return housing;
	}

	public void setHousing(boolean housing) {
		this.housing = housing;
	}

	public String getManOrWoman() {
		return manOrWoman;
	}

	public void setManOrWoman(String manOrWoman) {
		this.manOrWoman = manOrWoman;
	}

	public boolean isMentalHealth() {
		return mentalHealth;
	}

	public void setMentalHealth(boolean mentalHealth) {
		this.mentalHealth = mentalHealth;
	}

	public boolean isPhysicalHealth() {
		return physicalHealth;
	}

	public void setPhysicalHealth(boolean physicalHealth) {
		this.physicalHealth = physicalHealth;
	}

	public boolean isTransgender() {
		return transgender;
	}

	public void setTransgender(boolean transgender) {
		this.transgender = transgender;
	}

	public String getExclusiveView() {
		return exclusiveView;
	}

	public void setExclusiveView(String exclusiveView) {
		this.exclusiveView = exclusiveView;
	}

	public Integer getAgeMin() {
		if (this.ageMin != null) {
			return ageMin;
		}

		return this.MIN_AGE;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getAgeMax() {
		if (this.ageMax != null) {
			return ageMax;
		}
		return this.MAX_AGE;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public Integer getMaximumServiceRestrictionDays() {
		return maximumServiceRestrictionDays;
	}

	public void setMaximumServiceRestrictionDays(Integer maximumServiceRestrictionDays) {
		this.maximumServiceRestrictionDays = maximumServiceRestrictionDays;
	}

	public Integer getDefaultServiceRestrictionDays() {
		if ((this.defaultServiceRestrictionDays != null) && (this.defaultServiceRestrictionDays > 0)) {
			return defaultServiceRestrictionDays;
		}

		return this.DEFAULT_SERVICE_RESTRICTION_DAYS;
	}

	public void setDefaultServiceRestrictionDays(Integer defaultServiceRestrictionDays) {
		this.defaultServiceRestrictionDays = defaultServiceRestrictionDays;
	}

	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Program)) return false;
		else {
			Program program = (Program) obj;
			if (null == this.getId() || null == program.getId()) return false;
			else return (this.getId().equals(program.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public String getFacilityDesc() {
		return facilityDesc;
	}

	public void setFacilityDesc(String facilityDesc) {
		this.facilityDesc = facilityDesc;
	}

	public Integer getTotalUsedRoom() {
		return totalUsedRoom;
	}

	public void setTotalUsedRoom(Integer totalUsedRoom) {
		this.totalUsedRoom = totalUsedRoom;
	}

	public Integer getNumOfIntakes() {
		return numOfIntakes;
	}

	public void setNumOfIntakes(Integer numOfIntakes) {
		this.numOfIntakes = numOfIntakes;
	}

	public String getGenderDesc() {
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public LookupCodeValue getShelter() {
		return shelter;
	}

	public void setShelter(LookupCodeValue shelter) {
		this.shelter = shelter;
	}

	public Boolean isEnableEncounterTime() {
		return enableEncounterTime;
	}

	public Boolean getEnableEncounterTime() {
		return enableEncounterTime;
	}

	public void setEnableEncounterTime(Boolean enableEncounterTime) {
		this.enableEncounterTime = enableEncounterTime;
	}

	public Boolean isEnableEncounterTransportationTime() {
		return enableEncounterTransportationTime;
	}

	public Boolean getEnableEncounterTransportationTime() {
		return enableEncounterTransportationTime;
	}

	public void setEnableEncounterTransportationTime(Boolean enableEncounterTransportationTime) {
		this.enableEncounterTransportationTime = enableEncounterTransportationTime;
	}

	public String getEmailNotificationAddressesCsv() {
		return emailNotificationAddressesCsv;
	}

	public void setEmailNotificationAddressesCsv(String emailNotificationAddressesCsv) {
		this.emailNotificationAddressesCsv = emailNotificationAddressesCsv;
	}

	public Date getLastReferralNotification() {
		return lastReferralNotification;
	}

	public void setLastReferralNotification(Date lastReferralNotification) {
		this.lastReferralNotification = lastReferralNotification;
	}

	public Integer getNoOfVacancy() {
		return noOfVacancy;
	}

	public void setNoOfVacancy(Integer noOfVacancy) {
		this.noOfVacancy = noOfVacancy;
	}

	public String getVacancyName() {
		return vacancyName;
	}

	public void setVacancyName(String vacancyName) {
		this.vacancyName = vacancyName;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public double getMatches() {
		return matches;
	}

	public void setMatches(double matches) {
		this.matches = matches;
	}

	public Integer getVacancyId() {
		return vacancyId;
	}

	public void setVacancyId(Integer vacancyId) {
		this.vacancyId = vacancyId;
	}

	public String getVacancyTemplateName() {
		return vacancyTemplateName;
	}

	public void setVacancyTemplateName(String vacancyTemplateName) {
		this.vacancyTemplateName = vacancyTemplateName;
	}

	public static String getIdsAsStringList(List<Program> results) {
		StringBuilder sb = new StringBuilder();

		for (Program model : results) {
			sb.append(model.getId().toString());
			sb.append(',');
		}

		return (sb.toString());
	}

	public boolean isEnableOCAN() {
		return enableOCAN;
	}

	public void setEnableOCAN(boolean enableOCAN) {
		this.enableOCAN = enableOCAN;
	}

	
	public static final Comparator<Program> NameComparator = new Comparator<Program>() {
        public int compare(Program p1, Program p2) {
        	return p1.getName().compareTo(p2.getName());
        }
    }; 
}
