package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the program table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="program"
 */

public abstract class BaseProgram  implements Serializable {

	public static String REF = "Program";
	public static String PROP_TYPE = "type";
	public static String PROP_DESCR = "descr";
	public static String PROP_AGENCY_ID = "agencyId";
	public static String PROP_QUEUE_SIZE = "queueSize";
	public static String PROP_MAX_ALLOWED = "maxAllowed";
	public static String PROP_URL = "url";
	public static String PROP_ALLOW_BATCH_ADMISSION = "allowBatchAdmission";
	public static String PROP_ALLOW_BATCH_DISCHARGE = "allowBatchDischarge";
	public static String PROP_PHONE = "phone";
	public static String PROP_EMERGENCY_NUMBER = "emergencyNumber";
	public static String PROP_BED_PROGRAM_LINK_ID = "bedProgramLinkId";
	public static String PROP_INTAKE_PROGRAM = "intakeProgram";
	public static String PROP_EMAIL = "email";
	public static String PROP_NUM_OF_MEMBERS = "numOfMembers";
	public static String PROP_PROGRAM_STATUS = "programStatus";
	public static String PROP_FAX = "fax";
	public static String PROP_ADDRESS = "address";
	public static String PROP_HOLDING_TANK = "holdingTank";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_HIC = "hic";
	public static String PROP_LOCATION = "location";


	// constructors
	public BaseProgram () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseProgram (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseProgram (
		java.lang.Integer id,
		java.lang.Integer maxAllowed,
		java.lang.String address,
		java.lang.String phone,
		java.lang.String fax,
		java.lang.String url,
		java.lang.String email,
		java.lang.String emergencyNumber,
		java.lang.String name,
		java.lang.Long agencyId,
		boolean holdingTank,
		java.lang.String programStatus) {

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

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer numOfMembers;
	private java.lang.Integer queueSize;
	private java.lang.Integer maxAllowed;
	private java.lang.String type;
	private java.lang.String descr;
	private java.lang.String address;
	private java.lang.String phone;
	private java.lang.String fax;
	private java.lang.String url;
	private java.lang.String email;
	private java.lang.String emergencyNumber;
	private java.lang.String location;
	private java.lang.String name;
	private java.lang.Long agencyId;
	private boolean holdingTank;
	private boolean allowBatchAdmission;
	private boolean allowBatchDischarge;
	private boolean hic;
	private java.lang.String programStatus;
	private java.lang.Integer intakeProgram;
	private java.lang.Integer bedProgramLinkId;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="program_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: numOfMembers
	 */
	public java.lang.Integer getNumOfMembers () {
		return numOfMembers;
	}

	/**
	 * Set the value related to the column: numOfMembers
	 * @param numOfMembers the numOfMembers value
	 */
	public void setNumOfMembers (java.lang.Integer numOfMembers) {
		this.numOfMembers = numOfMembers;
	}



	/**
	 * Return the value associated with the column: queueSize
	 */
	public java.lang.Integer getQueueSize () {
		return queueSize;
	}

	/**
	 * Set the value related to the column: queueSize
	 * @param queueSize the queueSize value
	 */
	public void setQueueSize (java.lang.Integer queueSize) {
		this.queueSize = queueSize;
	}



	/**
	 * Return the value associated with the column: max_allowed
	 */
	public java.lang.Integer getMaxAllowed () {
		return maxAllowed;
	}

	/**
	 * Set the value related to the column: max_allowed
	 * @param maxAllowed the max_allowed value
	 */
	public void setMaxAllowed (java.lang.Integer maxAllowed) {
		this.maxAllowed = maxAllowed;
	}



	/**
	 * Return the value associated with the column: type
	 */
	public java.lang.String getType () {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * @param type the type value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: descr
	 */
	public java.lang.String getDescr () {
		return descr;
	}

	/**
	 * Set the value related to the column: descr
	 * @param descr the descr value
	 */
	public void setDescr (java.lang.String descr) {
		this.descr = descr;
	}



	/**
	 * Return the value associated with the column: address
	 */
	public java.lang.String getAddress () {
		return address;
	}

	/**
	 * Set the value related to the column: address
	 * @param address the address value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}



	/**
	 * Return the value associated with the column: phone
	 */
	public java.lang.String getPhone () {
		return phone;
	}

	/**
	 * Set the value related to the column: phone
	 * @param phone the phone value
	 */
	public void setPhone (java.lang.String phone) {
		this.phone = phone;
	}



	/**
	 * Return the value associated with the column: fax
	 */
	public java.lang.String getFax () {
		return fax;
	}

	/**
	 * Set the value related to the column: fax
	 * @param fax the fax value
	 */
	public void setFax (java.lang.String fax) {
		this.fax = fax;
	}



	/**
	 * Return the value associated with the column: url
	 */
	public java.lang.String getUrl () {
		return url;
	}

	/**
	 * Set the value related to the column: url
	 * @param url the url value
	 */
	public void setUrl (java.lang.String url) {
		this.url = url;
	}



	/**
	 * Return the value associated with the column: email
	 */
	public java.lang.String getEmail () {
		return email;
	}

	/**
	 * Set the value related to the column: email
	 * @param email the email value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}



	/**
	 * Return the value associated with the column: emergency_number
	 */
	public java.lang.String getEmergencyNumber () {
		return emergencyNumber;
	}

	/**
	 * Set the value related to the column: emergency_number
	 * @param emergencyNumber the emergency_number value
	 */
	public void setEmergencyNumber (java.lang.String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}



	/**
	 * Return the value associated with the column: location
	 */
	public java.lang.String getLocation () {
		return location;
	}

	/**
	 * Set the value related to the column: location
	 * @param location the location value
	 */
	public void setLocation (java.lang.String location) {
		this.location = location;
	}



	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: agency_id
	 */
	public java.lang.Long getAgencyId () {
		return agencyId;
	}

	/**
	 * Set the value related to the column: agency_id
	 * @param agencyId the agency_id value
	 */
	public void setAgencyId (java.lang.Long agencyId) {
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
	public java.lang.String getProgramStatus () {
		return programStatus;
	}

	/**
	 * Set the value related to the column: program_status
	 * @param programStatus the program_status value
	 */
	public void setProgramStatus (java.lang.String programStatus) {
		this.programStatus = programStatus;
	}



	/**
	 * Return the value associated with the column: intake_program
	 */
	public java.lang.Integer getIntakeProgram () {
		return intakeProgram;
	}

	/**
	 * Set the value related to the column: intake_program
	 * @param intakeProgram the intake_program value
	 */
	public void setIntakeProgram (java.lang.Integer intakeProgram) {
		this.intakeProgram = intakeProgram;
	}



	/**
	 * Return the value associated with the column: bed_program_link_id
	 */
	public java.lang.Integer getBedProgramLinkId () {
		return bedProgramLinkId;
	}

	/**
	 * Set the value related to the column: bed_program_link_id
	 * @param bedProgramLinkId the bed_program_link_id value
	 */
	public void setBedProgramLinkId (java.lang.Integer bedProgramLinkId) {
		this.bedProgramLinkId = bedProgramLinkId;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Program)) return false;
		else {
			org.oscarehr.PMmodule.model.Program program = (org.oscarehr.PMmodule.model.Program) obj;
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