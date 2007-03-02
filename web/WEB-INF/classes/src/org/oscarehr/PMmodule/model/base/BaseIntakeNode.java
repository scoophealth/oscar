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
 * This is an object that contains data related to the intake_node table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_node"
 */

public abstract class BaseIntakeNode implements Serializable {

	public static String REF = "IntakeNode";

	public static String PROP_PARENT = "parent";

	public static String PROP_NODE_TEMPLATE = "nodeTemplate";

	public static String PROP_LABEL = "label";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeNode() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeNode(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeNode(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate) {

		this.setId(id);
		this.setNodeTemplate(nodeTemplate);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate;

	private org.oscarehr.PMmodule.model.IntakeNodeLabel label;

	private org.oscarehr.PMmodule.model.IntakeNode parent;

	// collections
	private java.util.List<org.oscarehr.PMmodule.model.IntakeNode> children;

	private java.util.Set<org.oscarehr.PMmodule.model.Intake> intakes;

	private java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> answers;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_node_id"
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
	 * Return the value associated with the column: intake_node_template_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNodeTemplate getNodeTemplate() {
		return nodeTemplate;
	}

	/**
	 * Set the value related to the column: intake_node_template_id
	 * 
	 * @param nodeTemplate
	 *            the intake_node_template_id value
	 */
	public void setNodeTemplate(org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate) {
		this.nodeTemplate = nodeTemplate;
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
	 * Return the value associated with the column: parent_intake_node_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNode getParent() {
		return parent;
	}

	/**
	 * Set the value related to the column: parent_intake_node_id
	 * 
	 * @param parent
	 *            the parent_intake_node_id value
	 */
	public void setParent(org.oscarehr.PMmodule.model.IntakeNode parent) {
		this.parent = parent;
	}

	/**
	 * Return the value associated with the column: children
	 */
	public java.util.List<org.oscarehr.PMmodule.model.IntakeNode> getChildren() {
		return children;
	}

	/**
	 * Set the value related to the column: children
	 * 
	 * @param children
	 *            the children value
	 */
	public void setChildren(java.util.List<org.oscarehr.PMmodule.model.IntakeNode> children) {
		this.children = children;
	}

	public void addTochildren(org.oscarehr.PMmodule.model.IntakeNode intakeNode) {
		if (null == getChildren())
			setChildren(new java.util.ArrayList<org.oscarehr.PMmodule.model.IntakeNode>());
		getChildren().add(intakeNode);
	}

	/**
	 * Return the value associated with the column: intakes
	 */
	public java.util.Set<org.oscarehr.PMmodule.model.Intake> getIntakes() {
		return intakes;
	}

	/**
	 * Set the value related to the column: intakes
	 * 
	 * @param intakes
	 *            the intakes value
	 */
	public void setIntakes(java.util.Set<org.oscarehr.PMmodule.model.Intake> intakes) {
		this.intakes = intakes;
	}

	public void addTointakes(org.oscarehr.PMmodule.model.Intake intake) {
		if (null == getIntakes())
			setIntakes(new java.util.TreeSet<org.oscarehr.PMmodule.model.Intake>());
		getIntakes().add(intake);
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
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeNode))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeNode intakeNode = (org.oscarehr.PMmodule.model.IntakeNode) obj;
			if (null == this.getId() || null == intakeNode.getId())
				return false;
			else
				return (this.getId().equals(intakeNode.getId()));
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