package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseRoom;

public class Room extends BaseRoom {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_NAME = "";
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

	private RoomType roomType;
	private Program program;
	
	public void setRoomType(RoomType roomType) {
	    this.roomType = roomType;
    }
	
	public void setProgram(Program program) {
	    this.program = program;
    }
	
	public String getRoomTypeName() {
		return roomType.getName();
	}

	public void setRoomTypeName(String roomTypeName) {
		roomType.setName(roomTypeName);
	}

	public String getProgramName() {
		return program != null ? program.getName() : null;
	}

	public void setProgramName(String programName) {
		if (program != null) {
			program.setName(programName);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}