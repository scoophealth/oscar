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

import java.util.Date;

import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedDemographicHistorical;
import org.oscarehr.PMmodule.model.BedDemographicStatus;

/**
 * Interface for BedDemographicDAO
 */
public interface BedDemographicDAO {

	/**
	 * Does status exist
	 * 
	 * @param bedDemographicStatusId
	 *            status id
	 * @return true if status exists
	 */
	public boolean bedDemographicStatusExists(Integer bedDemographicStatusId);

	/**
	 * Does demographic to bed relationship exist
	 * 
	 * @param bedId
	 *            bed identifier
	 * @return true if relationship exits
	 */
	public boolean demographicExists(Integer bedId);

	/**
	 * Does bed to demographic relationship exist
	 * 
	 * @param demographicNo
	 *            demographic identifier
	 * @return true if relationship exits
	 */
	public boolean bedExists(Integer demographicNo);

	/**
	 * Get BedDemographic relationship object for given bed
	 * 
	 * @param bedId
	 *            bed identifier
	 * @return BedDemographic
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId);

	/**
	 * Get BedDemographic relationship object for given demographic
	 * 
	 * @param demographicNo
	 *            demographic number
	 * @return BedDemographic
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo);

	/**
	 * Get BedDemographicStatus for given id
	 * 
	 * @param bedDemographicStatusId
	 *            status identifier
	 * @return BedDemographicStatus
	 */
	public BedDemographicStatus getBedDemographicStatus(Integer bedDemographicStatusId);

	/**
	 * Get BedDemographicStatuses
	 * 
	 * @return array of statutes
	 */
	public BedDemographicStatus[] getBedDemographicStatuses();

	/**
	 * Get BedDemographicHistoricals since given date
	 * 
	 * @param since
	 *            given date
	 * @return array of historicals
	 */
	public BedDemographicHistorical[] getBedDemographicHistoricals(Date since);

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
