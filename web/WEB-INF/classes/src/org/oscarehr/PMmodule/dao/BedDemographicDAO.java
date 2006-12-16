package org.oscarehr.PMmodule.dao;

import org.oscarehr.PMmodule.model.BedDemographic;
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
	 * Get BedDemographic statuses
	 * 
	 * @return array of statutes
	 */
	public BedDemographicStatus[] getBedDemographicStatuses();

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
