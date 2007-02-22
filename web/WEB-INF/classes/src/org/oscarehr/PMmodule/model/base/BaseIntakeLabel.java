package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the intake_label table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="intake_label"
 */

public abstract class BaseIntakeLabel  implements Serializable {

	public static String REF = "IntakeLabel";
	public static String PROP_NODE = "node";
	public static String PROP_LABEL = "label";
	public static String PROP_ID = "id";


	// constructors
	public BaseIntakeLabel () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeLabel (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeLabel (
		java.lang.Integer id,
		org.oscarehr.PMmodule.model.IntakeNode node,
		java.lang.String label) {

		this.setId(id);
		this.setNode(node);
		this.setLabel(label);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String label;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNode node;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="intake_label_id"
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
	 * Return the value associated with the column: lbl
	 */
	public java.lang.String getLabel () {
		return label;
	}

	/**
	 * Set the value related to the column: lbl
	 * @param label the lbl value
	 */
	public void setLabel (java.lang.String label) {
		this.label = label;
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeLabel)) return false;
		else {
			org.oscarehr.PMmodule.model.IntakeLabel intakeLabel = (org.oscarehr.PMmodule.model.IntakeLabel) obj;
			if (null == this.getId() || null == intakeLabel.getId()) return false;
			else return (this.getId().equals(intakeLabel.getId()));
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