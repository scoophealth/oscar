package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the room table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="room"
 */

public abstract class BaseRoom  implements Serializable {

	public static String REF = "Room";
	public static String PROP_ROOM_TYPE_ID = "roomTypeId";
	public static String PROP_ACTIVE = "active";
	public static String PROP_FLOOR = "floor";
	public static String PROP_PROGRAM_ID = "programId";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseRoom () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRoom (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRoom (
		java.lang.Integer id,
		java.lang.Integer roomTypeId,
		java.lang.String name,
		boolean active) {

		this.setId(id);
		this.setRoomTypeId(roomTypeId);
		this.setName(name);
		this.setActive(active);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer roomTypeId;
	private java.lang.Integer programId;
	private java.lang.String name;
	private java.lang.String floor;
	private boolean active;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="room_id"
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
	 * Return the value associated with the column: room_type_id
	 */
	public java.lang.Integer getRoomTypeId () {
		return roomTypeId;
	}

	/**
	 * Set the value related to the column: room_type_id
	 * @param roomTypeId the room_type_id value
	 */
	public void setRoomTypeId (java.lang.Integer roomTypeId) {
		this.roomTypeId = roomTypeId;
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
	 * Return the value associated with the column: floor
	 */
	public java.lang.String getFloor () {
		return floor;
	}

	/**
	 * Set the value related to the column: floor
	 * @param floor the floor value
	 */
	public void setFloor (java.lang.String floor) {
		this.floor = floor;
	}



	/**
	 * Return the value associated with the column: active
	 */
	public boolean isActive () {
		return active;
	}

	/**
	 * Set the value related to the column: active
	 * @param active the active value
	 */
	public void setActive (boolean active) {
		this.active = active;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Room)) return false;
		else {
			org.oscarehr.PMmodule.model.Room room = (org.oscarehr.PMmodule.model.Room) obj;
			if (null == this.getId() || null == room.getId()) return false;
			else return (this.getId().equals(room.getId()));
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