package org.oscarehr.PMmodule.dao;

import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedType;

public interface BedDAO {

	/**
	 * @param bedId
	 * @return
	 */
	public boolean bedExists(Integer bedId);

	/**
	 * @param bedTypeId
	 * @return
	 */
	public boolean bedTypeExists(Integer bedTypeId);

	/**
	 * @param bedId
	 * @return
	 */
	public Bed getBed(Integer bedId);
	
	/**
	 * @param bedTypeId
	 * @return
	 */
	public BedType getBedType(Integer bedTypeId);

	/**
	 * @param programId
	 * @param active
	 * @return
	 */
	public Bed[] getBeds(Integer roomId, Boolean active);
	
	/**
	 * @return
	 */
	public BedType[] getBedTypes();

	/**
	 * @param bed
	 * @return TODO
	 */
	public void saveBed(Bed bed);

}