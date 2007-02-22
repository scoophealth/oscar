package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the intake_node table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="intake_node"
 */

public abstract class BaseIntakeNode  implements Serializable {

	public static String REF = "IntakeNode";
	public static String PROP_TYPE = "type";
	public static String PROP_PARENT = "parent";
	public static String PROP_ID = "id";


	// constructors
	public BaseIntakeNode () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeNode (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeNode (
		java.lang.Integer id,
		org.oscarehr.PMmodule.model.IntakeNodeType type) {

		this.setId(id);
		this.setType(type);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNodeType type;
	private org.oscarehr.PMmodule.model.IntakeNode parent;

	// collections
	private java.util.List<org.oscarehr.PMmodule.model.IntakeNode> children;
	private java.util.List<org.oscarehr.PMmodule.model.IntakeLabel> labels;
	private java.util.Set<org.oscarehr.PMmodule.model.IntakeAnswer> answers;
	private java.util.Set<org.oscarehr.PMmodule.model.IntakeInstance> instances;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="intake_node_id"
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
	 * Return the value associated with the column: intake_node_type_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNodeType getType () {
		return type;
	}

	/**
	 * Set the value related to the column: intake_node_type_id
	 * @param type the intake_node_type_id value
	 */
	public void setType (org.oscarehr.PMmodule.model.IntakeNodeType type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: parent_intake_node_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNode getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: parent_intake_node_id
	 * @param parent the parent_intake_node_id value
	 */
	public void setParent (org.oscarehr.PMmodule.model.IntakeNode parent) {
		this.parent = parent;
	}



	/**
	 * Return the value associated with the column: children
	 */
	public java.util.List<org.oscarehr.PMmodule.model.IntakeNode> getChildren () {
		return children;
	}

	/**
	 * Set the value related to the column: children
	 * @param children the children value
	 */
	public void setChildren (java.util.List<org.oscarehr.PMmodule.model.IntakeNode> children) {
		this.children = children;
	}

	public void addTochildren (org.oscarehr.PMmodule.model.IntakeNode intakeNode) {
		if (null == getChildren()) setChildren(new java.util.ArrayList<org.oscarehr.PMmodule.model.IntakeNode>());
		getChildren().add(intakeNode);
	}



	/**
	 * Return the value associated with the column: labels
	 */
	public java.util.List<org.oscarehr.PMmodule.model.IntakeLabel> getLabels () {
		return labels;
	}

	/**
	 * Set the value related to the column: labels
	 * @param labels the labels value
	 */
	public void setLabels (java.util.List<org.oscarehr.PMmodule.model.IntakeLabel> labels) {
		this.labels = labels;
	}

	public void addTolabels (org.oscarehr.PMmodule.model.IntakeLabel intakeLabel) {
		if (null == getLabels()) setLabels(new java.util.ArrayList<org.oscarehr.PMmodule.model.IntakeLabel>());
		getLabels().add(intakeLabel);
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



	/**
	 * Return the value associated with the column: instances
	 */
	public java.util.Set<org.oscarehr.PMmodule.model.IntakeInstance> getInstances () {
		return instances;
	}

	/**
	 * Set the value related to the column: instances
	 * @param instances the instances value
	 */
	public void setInstances (java.util.Set<org.oscarehr.PMmodule.model.IntakeInstance> instances) {
		this.instances = instances;
	}

	public void addToinstances (org.oscarehr.PMmodule.model.IntakeInstance intakeInstance) {
		if (null == getInstances()) setInstances(new java.util.TreeSet<org.oscarehr.PMmodule.model.IntakeInstance>());
		getInstances().add(intakeInstance);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeNode)) return false;
		else {
			org.oscarehr.PMmodule.model.IntakeNode intakeNode = (org.oscarehr.PMmodule.model.IntakeNode) obj;
			if (null == this.getId() || null == intakeNode.getId()) return false;
			else return (this.getId().equals(intakeNode.getId()));
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