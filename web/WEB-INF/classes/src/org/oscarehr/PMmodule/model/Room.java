/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

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