/*
 * Copyright (c) 2001-2002.
 * 
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada
 */
package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedType;

public interface BedManager {

	/**
	 * Get bed
	 * 
	 * @param bedId
	 *            bed identifier
	 * @return bed
	 */
	public Bed getBed(Integer bedId);

	/**
	 * Get beds by program
	 * 
	 * @param programId
	 *            program identifier
	 * @param reserved
	 *            reserved flag
	 * @return array of beds
	 */
	public Bed[] getBedsByProgram(Integer programId, boolean reserved);

	/**
	 * Get beds
	 * 
	 * @return array of beds
	 */
	public Bed[] getBeds();

	/**
	 * @return
	 */
	public BedType[] getBedTypes();

	/**
	 * Add new beds
	 * 
	 * @param numBeds
	 *            number of beds
	 * @throws BedReservedException
	 *             bed is inactive and reserved
	 */
	public void addBeds(int numBeds) throws BedReservedException;

	/**
	 * Save bed
	 * 
	 * @param bed
	 *            bed to save
	 * @throws BedReservedException
	 *             bed is inactive and reserved
	 */
	public void saveBed(Bed bed) throws BedReservedException;

	/**
	 * Save beds
	 * 
	 * @param beds
	 *            beds to save
	 * @throws BedReservedException
	 *             bed is inactive and reserved
	 */
	public void saveBeds(Bed[] beds) throws BedReservedException;

}
