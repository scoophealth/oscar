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

import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedDemographicHistorical;
import org.oscarehr.PMmodule.model.BedDemographicStatus;

public interface BedDemographicManager {

	/**
	 * Does demographic exist for given bedId
	 * 
	 * @param bedId
	 *            given bedId
	 * @return true, if demographic exists
	 */
	public boolean demographicExists(Integer bedId);

	/**
	 * Get BedDemographic relationship object with given bed identifier
	 * 
	 * @param bedId
	 *            bed identifier
	 * @return BedDemographic relationship object
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId);

	/**
	 * Get BedDemographic relationship object with given demographic identifier
	 * 
	 * @param facilityId can be null
	 * @param demographicNo
	 *            demographic identifier
	 * @return BedDemographic relationship object
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo, Integer facilityId);

	/**
	 * Get default BedDemographic status
	 * 
	 * @return BedDemographic
	 */
	public BedDemographicStatus getDefaultBedDemographicStatus();

	/**
	 * Get BedDemographic statuses
	 * 
	 * @return array of BedDemographic statuses
	 */
	public BedDemographicStatus[] getBedDemographicStatuses();

	/**
	 * Get reservations that expired today
	 * 
	 * @return array of expired reservations
	 */
	public BedDemographicHistorical[] getExpiredReservations();

	/**
	 * Save BedDemographic relationship object
	 * 
	 * @param bedDemographic
	 *            BedDemographic relationship object
	 */
	public void saveBedDemographic(BedDemographic bedDemographic);

	/**
	 * Delete bed demographic and create bed demographic historical
	 * 
	 * @param bedDemographic
	 *            BedDemographic relationship object
	 */
	public void deleteBedDemographic(BedDemographic bedDemographic);

}
