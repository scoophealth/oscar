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

package org.oscarehr.PMmodule.dao;

import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomType;

public interface RoomDAO {

	/**
	 * Does room with id exist
	 * 
	 * @param roomId
	 *            id
	 * @return true if room exists
	 */
	public boolean roomExists(Integer roomId);

	/**
	 * Does room type with id exist
	 * 
	 * @param roomTypeId
	 *            id
	 * @return true if room type exists
	 */
	public boolean roomTypeExists(Integer roomTypeId);

	/**
	 * Get room by id
	 * 
	 * @param roomId
	 *            id
	 * @return room
	 */
	public Room getRoom(Integer roomId);

	/**
	 * Get room type by id
	 * 
	 * @param roomTypeId
	 *            id
	 * @return room type
	 */
	public RoomType getRoomType(Integer roomTypeId);

	/**
	 * Get rooms
	 * 
	 * @param active
	 *            filter
	 * @return list of rooms
	 */
	public Room[] getRooms(Integer programId, Boolean active);

	/**
	 * Get room types
	 * 
	 * @return
	 */
	public RoomType[] getRoomTypes();

	/**
	 * Save room
	 * 
	 * @param room
	 *            room to save
	 */
	public void saveRoom(Room room);

}