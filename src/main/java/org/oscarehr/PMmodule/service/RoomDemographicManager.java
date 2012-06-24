/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.RoomDemographic;

public interface RoomDemographicManager {

	/**
	 * Does demographic exist for given roomId
	 * 
	 * @param roomId
	 *            given roomId
	 * @return true, if roomDemographicExists exists
	 */
	public boolean roomDemographicExists(Integer roomId);

	/**
	 * Get room occupancy integer with given room identifier
	 * 
	 * @param roomId
	 *            room identifier
	 * @return int
	 */
	public int getRoomOccupanyByRoom(Integer roomId);

	/**
	 * Get RoomDemographic relationship object with given room identifier
	 * 
	 * @param roomId
	 *            room identifier
	 * @return RoomDemographic relationship object
	 */
	public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId);

	/**
	 * Get RoomDemographic relationship object with given demographic identifier
	 *
	 * @param facilityId can be null
	 * @param demographicNo
	 *            demographic identifier
	 * @return RoomDemographic relationship object
	 */
	public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo, Integer facilityId);

	/**
	 * Save RoomDemographic relationship object
	 * 
	 * @param roomDemographic
	 *            RoomDemographic relationship object
	 */
	public void saveRoomDemographic(RoomDemographic roomDemographic);

	/**
	 * Clean up BedDemographic relationship object
	 * 
	 * @param roomDemographic
	 *            RoomDemographic relationship object
	 */
	public void cleanUpBedTables(RoomDemographic roomDemographic);
	
	/**
	 * Delete room demographic and create room demographic historical
	 * 
	 * @param roomDemographic
	 *            RoomDemographic relationship object
	 */
	public void deleteRoomDemographic(RoomDemographic roomDemographic);

}
