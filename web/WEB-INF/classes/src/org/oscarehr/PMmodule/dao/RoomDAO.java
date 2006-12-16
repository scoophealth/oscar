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