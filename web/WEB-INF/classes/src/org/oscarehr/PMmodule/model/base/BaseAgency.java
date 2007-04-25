package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the agency table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="agency"
 */

public abstract class BaseAgency  implements Serializable {

	public static String REF = "Agency";
	public static String PROP_INTEGRATOR_USERNAME = "integratorUsername";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_LOCAL = "local";
	public static String PROP_CONTACT_NAME = "contactName";
	public static String PROP_INTAKE_QUICK_STATE = "intakeQuickState";
	public static String PROP_CONTACT_EMAIL = "contactEmail";
	public static String PROP_INTEGRATOR_JMS = "integratorJms";
	public static String PROP_CONTACT_PHONE = "contactPhone";
	public static String PROP_INTEGRATOR_ENABLED = "integratorEnabled";
	public static String PROP_INTEGRATOR_URL = "integratorUrl";
	public static String PROP_INTEGRATOR_PASSWORD = "integratorPassword";
	public static String PROP_NAME = "name";
	public static String PROP_HIC = "hic";
	public static String PROP_INTAKE_INDEPTH = "intakeIndepth";
	public static String PROP_INTAKE_QUICK = "intakeQuick";
	public static String PROP_ID = "id";


	// constructors
	public BaseAgency () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAgency (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseAgency (
		java.lang.Long id,
		java.lang.Integer intakeQuick,
		java.lang.String intakeQuickState,
		java.lang.String name,
		boolean local,
		boolean integratorEnabled) {

		this.setId(id);
		this.setIntakeQuick(intakeQuick);
		this.setIntakeQuickState(intakeQuickState);
		this.setName(name);
		this.setLocal(local);
		this.setIntegratorEnabled(integratorEnabled);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer intakeQuick;
	private java.lang.String intakeQuickState;
	private java.lang.Integer intakeIndepth;
	private java.lang.String name;
	private java.lang.String description;
	private java.lang.String contactName;
	private java.lang.String contactEmail;
	private java.lang.String contactPhone;
	private boolean local;
	private boolean integratorEnabled;
	private java.lang.String integratorUrl;
	private java.lang.String integratorJms;
	private java.lang.String integratorUsername;
	private java.lang.String integratorPassword;
	private boolean hic;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: intake_quick
	 */
	public java.lang.Integer getIntakeQuick () {
		return intakeQuick;
	}

	/**
	 * Set the value related to the column: intake_quick
	 * @param intakeQuick the intake_quick value
	 */
	public void setIntakeQuick (java.lang.Integer intakeQuick) {
		this.intakeQuick = intakeQuick;
	}



	/**
	 * Return the value associated with the column: intake_quick_state
	 */
	public java.lang.String getIntakeQuickState () {
		return intakeQuickState;
	}

	/**
	 * Set the value related to the column: intake_quick_state
	 * @param intakeQuickState the intake_quick_state value
	 */
	public void setIntakeQuickState (java.lang.String intakeQuickState) {
		this.intakeQuickState = intakeQuickState;
	}



	/**
	 * Return the value associated with the column: intake_indepth
	 */
	public java.lang.Integer getIntakeIndepth () {
		return intakeIndepth;
	}

	/**
	 * Set the value related to the column: intake_indepth
	 * @param intakeIndepth the intake_indepth value
	 */
	public void setIntakeIndepth (java.lang.Integer intakeIndepth) {
		this.intakeIndepth = intakeIndepth;
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
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}



	/**
	 * Return the value associated with the column: contact_name
	 */
	public java.lang.String getContactName () {
		return contactName;
	}

	/**
	 * Set the value related to the column: contact_name
	 * @param contactName the contact_name value
	 */
	public void setContactName (java.lang.String contactName) {
		this.contactName = contactName;
	}



	/**
	 * Return the value associated with the column: contact_email
	 */
	public java.lang.String getContactEmail () {
		return contactEmail;
	}

	/**
	 * Set the value related to the column: contact_email
	 * @param contactEmail the contact_email value
	 */
	public void setContactEmail (java.lang.String contactEmail) {
		this.contactEmail = contactEmail;
	}



	/**
	 * Return the value associated with the column: contact_phone
	 */
	public java.lang.String getContactPhone () {
		return contactPhone;
	}

	/**
	 * Set the value related to the column: contact_phone
	 * @param contactPhone the contact_phone value
	 */
	public void setContactPhone (java.lang.String contactPhone) {
		this.contactPhone = contactPhone;
	}



	/**
	 * Return the value associated with the column: local
	 */
	public boolean isLocal () {
		return local;
	}

	/**
	 * Set the value related to the column: local
	 * @param local the local value
	 */
	public void setLocal (boolean local) {
		this.local = local;
	}



	/**
	 * Return the value associated with the column: integrator_enabled
	 */
	public boolean isIntegratorEnabled () {
		return integratorEnabled;
	}

	/**
	 * Set the value related to the column: integrator_enabled
	 * @param integratorEnabled the integrator_enabled value
	 */
	public void setIntegratorEnabled (boolean integratorEnabled) {
		this.integratorEnabled = integratorEnabled;
	}



	/**
	 * Return the value associated with the column: integrator_url
	 */
	public java.lang.String getIntegratorUrl () {
		return integratorUrl;
	}

	/**
	 * Set the value related to the column: integrator_url
	 * @param integratorUrl the integrator_url value
	 */
	public void setIntegratorUrl (java.lang.String integratorUrl) {
		this.integratorUrl = integratorUrl;
	}



	/**
	 * Return the value associated with the column: integrator_jms
	 */
	public java.lang.String getIntegratorJms () {
		return integratorJms;
	}

	/**
	 * Set the value related to the column: integrator_jms
	 * @param integratorJms the integrator_jms value
	 */
	public void setIntegratorJms (java.lang.String integratorJms) {
		this.integratorJms = integratorJms;
	}



	/**
	 * Return the value associated with the column: integrator_username
	 */
	public java.lang.String getIntegratorUsername () {
		return integratorUsername;
	}

	/**
	 * Set the value related to the column: integrator_username
	 * @param integratorUsername the integrator_username value
	 */
	public void setIntegratorUsername (java.lang.String integratorUsername) {
		this.integratorUsername = integratorUsername;
	}



	/**
	 * Return the value associated with the column: integrator_password
	 */
	public java.lang.String getIntegratorPassword () {
		return integratorPassword;
	}

	/**
	 * Set the value related to the column: integrator_password
	 * @param integratorPassword the integrator_password value
	 */
	public void setIntegratorPassword (java.lang.String integratorPassword) {
		this.integratorPassword = integratorPassword;
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Agency)) return false;
		else {
			org.oscarehr.PMmodule.model.Agency agency = (org.oscarehr.PMmodule.model.Agency) obj;
			if (null == this.getId() || null == agency.getId()) return false;
			else return (this.getId().equals(agency.getId()));
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