package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseRoom;

public class Room extends BaseRoom {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_NAME = "new room";

	private static final boolean DEFAULT_ACTIVE = true;

	public static final Room create(RoomType roomType) {
		Room room = new Room();
		room.setRoomTypeId(roomType.getId());
		room.setName(DEFAULT_NAME);
		room.setActive(DEFAULT_ACTIVE);

		return room;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public Room() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Room(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Room(java.lang.Integer id, java.lang.Integer roomTypeId, java.lang.String name, boolean active) {
		super(id, roomTypeId, name, active);
	}

	/* [CONSTRUCTOR MARKER END] */

	private String roomTypeName;

	private String programName;

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}