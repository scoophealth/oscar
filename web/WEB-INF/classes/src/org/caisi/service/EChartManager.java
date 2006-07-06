package org.caisi.service;

import org.caisi.model.EChart;

/**
 * Manager Interface for eChart
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface EChartManager {
	
	/**
	 * Save a new Encounter
	 * @param chart The Chart
	 */
	public void saveEncounter(EChart chart);
	
	/**
	 * Get the latest chart for a patient
	 * @param demographicNo Demographic Id
	 * @return The Chart
	 */
	public EChart getLatestChart(String demographicNo);
}
