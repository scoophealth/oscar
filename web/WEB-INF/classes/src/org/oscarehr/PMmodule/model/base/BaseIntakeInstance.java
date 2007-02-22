package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the intake_instance table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="intake_instance"
 */

public abstract class BaseIntakeInstance  implements Serializable {

	public static String REF = "IntakeInstance";
	public static String PROP_NODE = "node";
	public static String PROP_STAFF_ID = "staffId";
	public static String PROP_CREATED_ON = "createdOn";
	public static String PROP_ID = "id";
	public static String PROP_CLIENT_ID = "clientId";


	// constructors
	public BaseIntakeInstance () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeInstance (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeInstance (
		java.lang.Integer id,
		org.oscarehr.PMmodule.model.IntakeNode node,
		java.lang.Integer clientId,
		java.lang.String staffId,
		java.util.Calendar createdOn) {

		this.setId(id);
		this.setNode(node);
		this.setClientId(clientId);
		this.setStaffId(staffId);
		this.setCreatedOn(createdOn);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer clientId;
	private java.lang.String staffId;
	private java.util.Calendar createdOn;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNode node;

	// collections
	private java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> answers;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="intake_instance_id"
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
	 * Return the value associated with the column: client_id
	 */
	public java.lang.Integer getClientId () {
		return clientId;
	}

	/**
	 * Set the value related to the column: client_id
	 * @param clientId the client_id value
	 */
	public void setClientId (java.lang.Integer clientId) {
		this.clientId = clientId;
	}



	/**
	 * Return the value associated with the column: staff_id
	 */
	public java.lang.String getStaffId () {
		return staffId;
	}

	/**
	 * Set the value related to the column: staff_id
	 * @param staffId the staff_id value
	 */
	public void setStaffId (java.lang.String staffId) {
		this.staffId = staffId;
	}



	/**
	 * Return the value associated with the column: creation_date
	 */
	public java.util.Calendar getCreatedOn () {
		return createdOn;
	}

	/**
	 * Set the value related to the column: creation_date
	 * @param createdOn the creation_date value
	 */
	public void setCreatedOn (java.util.Calendar createdOn) {
		this.createdOn = createdOn;
	}



	/**
	 * Return the value associated with the column: intake_node_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNode getNode () {
		return node;
	}

	/**
	 * Set the value related to the column: intake_node_id
	 * @param node the intake_node_id value
	 */
	public void setNode (org.oscarehr.PMmodule.model.IntakeNode node) {
		this.node = node;
	}



	/**
	 * Return the value associated with the column: answers
	 */
	public java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> getAnswers () {
		return answers;
	}

	/**
	 * Set the value related to the column: answers
	 * @param answers the answers value
	 */
	public void setAnswers (java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> answers) {
		this.answers = answers;
	}

	public void addToanswers (org.oscarehr.PMmodule.model.IntakeAnswer intakeAnswer) {
		if (null == getAnswers()) setAnswers(new java.util.TreeSet<org.oscarehr.PMmodule.model.IntakeAnswer>());
		getAnswers().add(intakeAnswer);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeInstance)) return false;
		else {
			org.oscarehr.PMmodule.model.IntakeInstance intakeInstance = (org.oscarehr.PMmodule.model.IntakeInstance) obj;
			if (null == this.getId() || null == intakeInstance.getId()) return false;
			else return (this.getId().equals(intakeInstance.getId()));
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