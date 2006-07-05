package org.caisi.dao;

import org.caisi.model.EChart;

/**
 * Data Access Object for working with eChart table in OSCAR
 * @author Marc Dumontier marc@mdumontier.com
 *
 */
public interface EChartDAO extends DAO {
	
	/**
	 * Add/Update an encounter
	 * 
	 * Note: should always make a copy of the latest chart, and save it.
	 * @param chart The Chart object to save
	 */
	public void saveEncounter(EChart chart);
	
	/**
	 * Get the latest eChart entry for a patient
	 * @param demographicNo Demographic Id
	 * @return The Chart object
	 */
	public EChart getLatestChart(int demographicNo);
}
