package org.oscarehr.PMmodule.service;

import java.util.Date;

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
	 * @param time
	 *            cutoff time
	 * @return array of beds
	 */
	public Bed[] getBedsByProgram(Integer programId, Boolean reserved, Date time);

	/**
	 * @return
	 */
	public Bed[] getBeds();

	/**
	 * @return
	 */
	public BedType[] getBedTypes();

	/**
	 * Add a new bed
	 */
	public void addBed();

	/**
	 * @param beds
	 */
	public void saveBeds(Bed[] beds);

}
