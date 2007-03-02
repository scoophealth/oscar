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
 * This is an object that contains data related to the intake_node_template table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_node_template"
 */

public abstract class BaseIntakeNodeTemplate implements Serializable {

	public static String REF = "IntakeNodeTemplate";

	public static String PROP_TYPE = "type";

	public static String PROP_REMOTE_ID = "remoteId";

	public static String PROP_LABEL = "label";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeNodeTemplate() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeNodeTemplate(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeNodeTemplate(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeType type) {

		this.setId(id);
		this.setType(type);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer remoteId;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNodeType type;

	private org.oscarehr.PMmodule.model.IntakeNodeLabel label;

	// collections
	private java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswerElement> answerElements;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_node_template_id"
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
	 * Return the value associated with the column: remote_intake_node_template_id
	 */
	public java.lang.Integer getRemoteId() {
		return remoteId;
	}

	/**
	 * Set the value related to the column: remote_intake_node_template_id
	 * 
	 * @param remoteId
	 *            the remote_intake_node_template_id value
	 */
	public void setRemoteId(java.lang.Integer remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * Return the value associated with the column: intake_node_type_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNodeType getType() {
		return type;
	}

	/**
	 * Set the value related to the column: intake_node_type_id
	 * 
	 * @param type
	 *            the intake_node_type_id value
	 */
	public void setType(org.oscarehr.PMmodule.model.IntakeNodeType type) {
		this.type = type;
	}

	/**
	 * Return the value associated with the column: intake_node_label_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNodeLabel getLabel() {
		return label;
	}

	/**
	 * Set the value related to the column: intake_node_label_id
	 * 
	 * @param label
	 *            the intake_node_label_id value
	 */
	public void setLabel(org.oscarehr.PMmodule.model.IntakeNodeLabel label) {
		this.label = label;
	}

	/**
	 * Return the value associated with the column: answerElements
	 */
	public java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswerElement> getAnswerElements() {
		return answerElements;
	}

	/**
	 * Set the value related to the column: answerElements
	 * 
	 * @param answerElements
	 *            the answerElements value
	 */
	public void setAnswerElements(java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswerElement> answerElements) {
		this.answerElements = answerElements;
	}

	public void addToanswerElements(org.oscarehr.PMmodule.model.IntakeAnswerElement intakeAnswerElement) {
		if (null == getAnswerElements())
			setAnswerElements(new java.util.TreeSet<org.oscarehr.PMmodule.model.IntakeAnswerElement>());
		getAnswerElements().add(intakeAnswerElement);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeNodeTemplate))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeNodeTemplate intakeNodeTemplate = (org.oscarehr.PMmodule.model.IntakeNodeTemplate) obj;
			if (null == this.getId() || null == intakeNodeTemplate.getId())
				return false;
			else
				return (this.getId().equals(intakeNodeTemplate.getId()));
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