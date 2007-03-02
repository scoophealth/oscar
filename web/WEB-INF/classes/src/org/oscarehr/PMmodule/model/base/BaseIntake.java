/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the intake table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake"
 */
public abstract class BaseIntake implements Serializable {

	public static String REF = "Intake";

	public static String PROP_NODE = "node";

	public static String PROP_STAFF_ID = "staffId";

	public static String PROP_CREATED_ON = "createdOn";

	public static String PROP_ID = "id";

	public static String PROP_CLIENT_ID = "clientId";

	// constructors
	public BaseIntake() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntake(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntake(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.Integer clientId, java.lang.String staffId, java.util.Calendar createdOn) {

		this.setId(id);
		this.setNode(node);
		this.setClientId(clientId);
		this.setStaffId(staffId);
		this.setCreatedOn(createdOn);
		initialize();
	}

	protected void initialize() {
	}

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
	 * 
	 * @hibernate.id generator-class="native" column="intake_id"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: client_id
	 */
	public java.lang.Integer getClientId() {
		return clientId;
	}

	/**
	 * Set the value related to the column: client_id
	 * 
	 * @param clientId
	 *            the client_id value
	 */
	public void setClientId(java.lang.Integer clientId) {
		this.clientId = clientId;
	}

	/**
	 * Return the value associated with the column: staff_id
	 */
	public java.lang.String getStaffId() {
		return staffId;
	}

	/**
	 * Set the value related to the column: staff_id
	 * 
	 * @param staffId
	 *            the staff_id value
	 */
	public void setStaffId(java.lang.String staffId) {
		this.staffId = staffId;
	}

	/**
	 * Return the value associated with the column: creation_date
	 */
	public java.util.Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * Set the value related to the column: creation_date
	 * 
	 * @param createdOn
	 *            the creation_date value
	 */
	public void setCreatedOn(java.util.Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Return the value associated with the column: intake_node_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNode getNode() {
		return node;
	}

	/**
	 * Set the value related to the column: intake_node_id
	 * 
	 * @param node
	 *            the intake_node_id value
	 */
	public void setNode(org.oscarehr.PMmodule.model.IntakeNode node) {
		this.node = node;
	}

	/**
	 * Return the value associated with the column: answers
	 */
	public java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> getAnswers() {
		return answers;
	}

	/**
	 * Set the value related to the column: answers
	 * 
	 * @param answers
	 *            the answers value
	 */
	public void setAnswers(java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> answers) {
		this.answers = answers;
	}

	public void addToanswers(org.oscarehr.PMmodule.model.IntakeAnswer intakeAnswer) {
		if (null == getAnswers())
			setAnswers(new java.util.TreeSet<org.oscarehr.PMmodule.model.IntakeAnswer>());
		getAnswers().add(intakeAnswer);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Intake))
			return false;
		else {
			org.oscarehr.PMmodule.model.Intake intake = (org.oscarehr.PMmodule.model.Intake) obj;
			if (null == this.getId() || null == intake.getId())
				return false;
			else
				return (this.getId().equals(intake.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
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

}