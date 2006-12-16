package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the room_type table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="room_type"
 */

public abstract class BaseRoomType  implements Serializable {

	public static String REF = "RoomType";
	public static String PROP_DEFAULT = "default";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseRoomType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRoomType (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRoomType (
		java.lang.Integer id,
		java.lang.String name,
		boolean m_default) {

		this.setId(id);
		this.setName(name);
		this.setDefault(m_default);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private boolean m_default;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="room_type_id"
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
	 * Return the value associated with the column: dflt
	 */
	public boolean isDefault () {
		return m_default;
	}

	/**
	 * Set the value related to the column: dflt
	 * @param m_default the dflt value
	 */
	public void setDefault (boolean m_default) {
		this.m_default = m_default;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.RoomType)) return false;
		else {
			org.oscarehr.PMmodule.model.RoomType roomType = (org.oscarehr.PMmodule.model.RoomType) obj;
			if (null == this.getId() || null == roomType.getId()) return false;
			else return (this.getId().equals(roomType.getId()));
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