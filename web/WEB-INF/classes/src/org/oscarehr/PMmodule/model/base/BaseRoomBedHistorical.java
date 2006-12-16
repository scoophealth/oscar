package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the room_bed_historical table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="room_bed_historical"
 */

public abstract class BaseRoomBedHistorical  implements Serializable {

	public static String REF = "RoomBedHistorical";
	public static String PROP_CONTAIN_END = "containEnd";
	public static String PROP_ID = "id";


	// constructors
	public BaseRoomBedHistorical () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRoomBedHistorical (org.oscarehr.PMmodule.model.RoomBedHistoricalPK id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRoomBedHistorical (
		org.oscarehr.PMmodule.model.RoomBedHistoricalPK id,
		java.util.Date containEnd) {

		this.setId(id);
		this.setContainEnd(containEnd);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private org.oscarehr.PMmodule.model.RoomBedHistoricalPK id;

	// fields
	private java.util.Date containEnd;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     */
	public org.oscarehr.PMmodule.model.RoomBedHistoricalPK getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (org.oscarehr.PMmodule.model.RoomBedHistoricalPK id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: contain_end
	 */
	public java.util.Date getContainEnd () {
		return containEnd;
	}

	/**
	 * Set the value related to the column: contain_end
	 * @param containEnd the contain_end value
	 */
	public void setContainEnd (java.util.Date containEnd) {
		this.containEnd = containEnd;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.RoomBedHistorical)) return false;
		else {
			org.oscarehr.PMmodule.model.RoomBedHistorical roomBedHistorical = (org.oscarehr.PMmodule.model.RoomBedHistorical) obj;
			if (null == this.getId() || null == roomBedHistorical.getId()) return false;
			else return (this.getId().equals(roomBedHistorical.getId()));
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