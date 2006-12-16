package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomType;

public interface RoomManager {

	/**
	 * Get room
	 * 
	 * @param roomId
	 *            room identifier
	 * @return room
	 */
	public Room getRoom(Integer roomId);

	/**
	 * Get rooms
	 * 
	 * @return list of rooms
	 */
	public Room[] getRooms();

	/**
	 * Get room types
	 * 
	 * @return list of room types
	 */
	public RoomType[] getRoomTypes();

	/**
	 * Add a new room
	 */
	public void addRoom();

	/**
	 * Save rooms
	 * 
	 * @param rooms
	 *            rooms to create or update
	 */
	public void saveRooms(Room[] rooms);

}