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
	 * @param demographicNo
	 *            demographic identifier
	 * @return BedDemographic relationship object
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo);

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