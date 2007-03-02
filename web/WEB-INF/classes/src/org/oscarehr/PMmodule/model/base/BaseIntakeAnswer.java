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
 * This is an object that contains data related to the intake_answer table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_answer"
 */

public abstract class BaseIntakeAnswer implements Serializable {

	public static String REF = "IntakeAnswer";

	public static String PROP_VALUE = "value";

	public static String PROP_NODE = "node";

	public static String PROP_INTAKE = "intake";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeAnswer() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeAnswer(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeAnswer(java.lang.Integer id, org.oscarehr.PMmodule.model.Intake intake, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.String value) {

		this.setId(id);
		this.setIntake(intake);
		this.setNode(node);
		this.setValue(value);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String value;

	// many to one
	private org.oscarehr.PMmodule.model.Intake intake;

	private org.oscarehr.PMmodule.model.IntakeNode node;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_answer_id"
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
	 * Return the value associated with the column: val
	 */
	public java.lang.String getValue() {
		return value;
	}

	/**
	 * Set the value related to the column: val
	 * 
	 * @param value
	 *            the val value
	 */
	public void setValue(java.lang.String value) {
		this.value = value;
	}

	/**
	 * Return the value associated with the column: intake_id
	 */
	public org.oscarehr.PMmodule.model.Intake getIntake() {
		return intake;
	}

	/**
	 * Set the value related to the column: intake_id
	 * 
	 * @param intake
	 *            the intake_id value
	 */
	public void setIntake(org.oscarehr.PMmodule.model.Intake intake) {
		this.intake = intake;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeAnswer))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeAnswer intakeAnswer = (org.oscarehr.PMmodule.model.IntakeAnswer) obj;
			if (null == this.getId() || null == intakeAnswer.getId())
				return false;
			else
				return (this.getId().equals(intakeAnswer.getId()));
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