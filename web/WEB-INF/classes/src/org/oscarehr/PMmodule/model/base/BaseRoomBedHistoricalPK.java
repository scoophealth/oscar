package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


public abstract class BaseRoomBedHistoricalPK implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private java.lang.Integer roomId;
	private java.lang.Integer bedId;
	private java.util.Date containStart;


	public BaseRoomBedHistoricalPK () {}
	
	public BaseRoomBedHistoricalPK (
		java.lang.Integer roomId,
		java.lang.Integer bedId,
		java.util.Date containStart) {

		this.setRoomId(roomId);
		this.setBedId(bedId);
		this.setContainStart(containStart);
	}


	/**
	 * Return the value associated with the column: room_id
	 */
	public java.lang.Integer getRoomId () {
		return roomId;
	}

	/**
	 * Set the value related to the column: room_id
	 * @param roomId the room_id value
	 */
	public void setRoomId (java.lang.Integer roomId) {
		this.roomId = roomId;
	}



	/**
	 * Return the value associated with the column: bed_id
	 */
	public java.lang.Integer getBedId () {
		return bedId;
	}

	/**
	 * Set the value related to the column: bed_id
	 * @param bedId the bed_id value
	 */
	public void setBedId (java.lang.Integer bedId) {
		this.bedId = bedId;
	}



	/**
	 * Return the value associated with the column: contain_start
	 */
	public java.util.Date getContainStart () {
		return containStart;
	}

	/**
	 * Set the value related to the column: contain_start
	 * @param containStart the contain_start value
	 */
	public void setContainStart (java.util.Date containStart) {
		this.containStart = containStart;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.RoomBedHistoricalPK)) return false;
		else {
			org.oscarehr.PMmodule.model.RoomBedHistoricalPK mObj = (org.oscarehr.PMmodule.model.RoomBedHistoricalPK) obj;
			if (null != this.getRoomId() && null != mObj.getRoomId()) {
				if (!this.getRoomId().equals(mObj.getRoomId())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getBedId() && null != mObj.getBedId()) {
				if (!this.getBedId().equals(mObj.getBedId())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getContainStart() && null != mObj.getContainStart()) {
				if (!this.getContainStart().equals(mObj.getContainStart())) {
					return false;
				}
			}
			else {
				return false;
			}
			return true;
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuilder sb = new StringBuilder();
			if (null != this.getRoomId()) {
				sb.append(this.getRoomId().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getBedId()) {
				sb.append(this.getBedId().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getContainStart()) {
				sb.append(this.getContainStart().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}


}