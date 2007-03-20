package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the program_clientstatus table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="program_clientstatus"
 */

public abstract class BaseProgramClientStatus  implements Serializable {

	public static String REF = "ProgramClientStatus";
	public static String PROP_PROGRAM_ID = "programId";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseProgramClientStatus () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseProgramClientStatus (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.Integer programId;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="clientstatus_id"
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
	 * Return the value associated with the column: program_id
	 */
	public java.lang.Integer getProgramId () {
		return programId;
	}

	/**
	 * Set the value related to the column: program_id
	 * @param programId the program_id value
	 */
	public void setProgramId (java.lang.Integer programId) {
		this.programId = programId;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.ProgramClientStatus)) return false;
		else {
			org.oscarehr.PMmodule.model.ProgramClientStatus programClientStatus = (org.oscarehr.PMmodule.model.ProgramClientStatus) obj;
			if (null == this.getId() || null == programClientStatus.getId()) return false;
			else return (this.getId().equals(programClientStatus.getId()));
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