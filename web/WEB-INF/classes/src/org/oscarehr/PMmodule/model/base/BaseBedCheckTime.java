package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bed_check_time table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bed_check_time"
 */

public abstract class BaseBedCheckTime  implements Serializable {

	public static String REF = "BedCheckTime";
	public static String PROP_TIME = "time";
	public static String PROP_PROGRAM_ID = "programId";
	public static String PROP_ID = "id";


	// constructors
	public BaseBedCheckTime () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBedCheckTime (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBedCheckTime (
		java.lang.Integer id,
		java.lang.Integer programId,
		java.util.Date time) {

		this.setId(id);
		this.setProgramId(programId);
		this.setTime(time);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer programId;
	private java.util.Date time;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="bed_check_time_id"
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



	/**
	 * Return the value associated with the column: time
	 */
	public java.util.Date getTime () {
		return time;
	}

	/**
	 * Set the value related to the column: time
	 * @param time the time value
	 */
	public void setTime (java.util.Date time) {
		this.time = time;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.BedCheckTime)) return false;
		else {
			org.oscarehr.PMmodule.model.BedCheckTime bedCheckTime = (org.oscarehr.PMmodule.model.BedCheckTime) obj;
			if (null == this.getId() || null == bedCheckTime.getId()) return false;
			else return (this.getId().equals(bedCheckTime.getId()));
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