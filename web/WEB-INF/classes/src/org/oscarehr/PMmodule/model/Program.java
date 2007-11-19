/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
package org.oscarehr.PMmodule.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class Program implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer DEFAULT_COMMUNITY_PROGRAM_ID = new Integer(10010);
	
	public static final String BED_TYPE = "Bed";
	public static final String COMMUNITY_TYPE = "community";
	public static final String SERVICE_TYPE = "Service";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Integer id;// fields
    private Integer numOfMembers;
    private Integer queueSize;
    private Integer maxAllowed;
    private String type;
    private String descr;
    private String address;
    private String phone;
    private String fax;
    private String url;
    private String email;
    private String emergencyNumber;
    private String location;
    private String name;
    private Long agencyId;
    private boolean holdingTank;
    private boolean allowBatchAdmission;
    private boolean allowBatchDischarge;
    private boolean hic;
    private String programStatus;
    private Integer intakeProgram;
    private Integer bedProgramLinkId;
    private String manOrWoman;
    private boolean transgender;
    private boolean firstNation;
    private boolean bedProgramAffiliated;
    private boolean alcohol;
    private String abstinenceSupport;
    private boolean physicalHealth;
    private boolean mentalHealth;
    private boolean housing;
    private String exclusiveView;

     // constructors
	public Program () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Program (Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Program (
		Integer id,
		Integer maxAllowed,
		String address,
		String phone,
		String fax,
		String url,
		String email,
		String emergencyNumber,
		String name,
		Long agencyId,
		boolean holdingTank,
		String programStatus) {

		this.setId(id);
		this.setMaxAllowed(maxAllowed);
		this.setAddress(address);
		this.setPhone(phone);
		this.setFax(fax);
		this.setUrl(url);
		this.setEmail(email);
		this.setEmergencyNumber(emergencyNumber);
		this.setName(name);
		this.setAgencyId(agencyId);
		this.setHoldingTank(holdingTank);
		this.setProgramStatus(programStatus);
		initialize();
	}
    public boolean isActive() {
		return getProgramStatus().equalsIgnoreCase("active");
	}
	
	public boolean isFull() {
		return getNumOfMembers() >= getMaxAllowed();
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

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="program_id"
*/
    public Integer getId () {
        return id;
    }

    /**
	 * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: numOfMembers
     */
    public Integer getNumOfMembers () {
        return numOfMembers;
    }

    /**
	 * Set the value related to the column: numOfMembers
     * @param numOfMembers the numOfMembers value
     */
    public void setNumOfMembers (Integer numOfMembers) {
        this.numOfMembers = numOfMembers;
    }

    /**
	 * Return the value associated with the column: queueSize
     */
    public Integer getQueueSize () {
        return queueSize;
    }

    /**
	 * Set the value related to the column: queueSize
     * @param queueSize the queueSize value
     */
    public void setQueueSize (Integer queueSize) {
        this.queueSize = queueSize;
    }

    /**
	 * Return the value associated with the column: max_allowed
     */
    public Integer getMaxAllowed () {
        return maxAllowed;
    }

    /**
	 * Set the value related to the column: max_allowed
     * @param maxAllowed the max_allowed value
     */
    public void setMaxAllowed (Integer maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    /**
	 * Return the value associated with the column: type
     */
    public String getType () {
        return type;
    }

    /**
	 * Set the value related to the column: type
     * @param type the type value
     */
    public void setType (String type) {
        this.type = type;
    }

    /**
	 * Return the value associated with the column: descr
     */
    public String getDescr () {
        return descr;
    }

    /**
	 * Set the value related to the column: descr
     * @param descr the descr value
     */
    public void setDescr (String descr) {
        this.descr = descr;
    }

    /**
	 * Return the value associated with the column: address
     */
    public String getAddress () {
        return address;
    }

    /**
	 * Set the value related to the column: address
     * @param address the address value
     */
    public void setAddress (String address) {
        this.address = address;
    }

    /**
	 * Return the value associated with the column: phone
     */
    public String getPhone () {
        return phone;
    }

    /**
	 * Set the value related to the column: phone
     * @param phone the phone value
     */
    public void setPhone (String phone) {
        this.phone = phone;
    }

    /**
	 * Return the value associated with the column: fax
     */
    public String getFax () {
        return fax;
    }

    /**
	 * Set the value related to the column: fax
     * @param fax the fax value
     */
    public void setFax (String fax) {
        this.fax = fax;
    }

    /**
	 * Return the value associated with the column: url
     */
    public String getUrl () {
        return url;
    }

    /**
	 * Set the value related to the column: url
     * @param url the url value
     */
    public void setUrl (String url) {
        this.url = url;
    }

    /**
	 * Return the value associated with the column: email
     */
    public String getEmail () {
        return email;
    }

    /**
	 * Set the value related to the column: email
     * @param email the email value
     */
    public void setEmail (String email) {
        this.email = email;
    }

    /**
	 * Return the value associated with the column: emergency_number
     */
    public String getEmergencyNumber () {
        return emergencyNumber;
    }

    /**
	 * Set the value related to the column: emergency_number
     * @param emergencyNumber the emergency_number value
     */
    public void setEmergencyNumber (String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    /**
	 * Return the value associated with the column: location
     */
    public String getLocation () {
        return location;
    }

    /**
	 * Set the value related to the column: location
     * @param location the location value
     */
    public void setLocation (String location) {
        this.location = location;
    }

    /**
	 * Return the value associated with the column: name
     */
    public String getName () {
        return name;
    }

    /**
	 * Set the value related to the column: name
     * @param name the name value
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
	 * Return the value associated with the column: agency_id
     */
    public Long getAgencyId () {
        return agencyId;
    }

    /**
	 * Set the value related to the column: agency_id
     * @param agencyId the agency_id value
     */
    public void setAgencyId (Long agencyId) {
        this.agencyId = agencyId;
    }

    /**
	 * Return the value associated with the column: holding_tank
     */
    public boolean isHoldingTank () {
        return holdingTank;
    }

    /**
	 * Set the value related to the column: holding_tank
     * @param holdingTank the holding_tank value
     */
    public void setHoldingTank (boolean holdingTank) {
        this.holdingTank = holdingTank;
    }

    /**
	 * Return the value associated with the column: allow_batch_admission
     */
    public boolean isAllowBatchAdmission () {
        return allowBatchAdmission;
    }

    /**
	 * Set the value related to the column: allow_batch_admission
     * @param allowBatchAdmission the allow_batch_admission value
     */
    public void setAllowBatchAdmission (boolean allowBatchAdmission) {
        this.allowBatchAdmission = allowBatchAdmission;
    }

    /**
	 * Return the value associated with the column: allow_batch_discharge
     */
    public boolean isAllowBatchDischarge () {
        return allowBatchDischarge;
    }

    /**
	 * Set the value related to the column: allow_batch_discharge
     * @param allowBatchDischarge the allow_batch_discharge value
     */
    public void setAllowBatchDischarge (boolean allowBatchDischarge) {
        this.allowBatchDischarge = allowBatchDischarge;
    }

    /**
	 * Return the value associated with the column: hic
     */
    public boolean isHic () {
        return hic;
    }

    /**
	 * Set the value related to the column: hic
     * @param hic the hic value
     */
    public void setHic (boolean hic) {
        this.hic = hic;
    }

    /**
	 * Return the value associated with the column: program_status
     */
    public String getProgramStatus () {
        return programStatus;
    }

    /**
	 * Set the value related to the column: program_status
     * @param programStatus the program_status value
     */
    public void setProgramStatus (String programStatus) {
        this.programStatus = programStatus;
    }

    /**
	 * Return the value associated with the column: intake_program
     */
    public Integer getIntakeProgram () {
        return intakeProgram;
    }

    /**
	 * Set the value related to the column: intake_program
     * @param intakeProgram the intake_program value
     */
    public void setIntakeProgram (Integer intakeProgram) {
        this.intakeProgram = intakeProgram;
    }

    /**
	 * Return the value associated with the column: bed_program_link_id
     */
    public Integer getBedProgramLinkId () {
        return bedProgramLinkId;
    }

    /**
	 * Set the value related to the column: bed_program_link_id
     * @param bedProgramLinkId the bed_program_link_id value
     */
    public void setBedProgramLinkId (Integer bedProgramLinkId) {
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

    public void setExclusiveView(String exclusive_view) {
        this.exclusiveView = exclusive_view;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Program)) return false;
        else {
            Program program = (Program) obj;
            if (null == this.getId() || null == program.getId()) return false;
            else return (this.getId().equals(program.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}