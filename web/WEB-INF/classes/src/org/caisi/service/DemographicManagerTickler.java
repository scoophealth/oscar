package org.caisi.service;

import java.util.List;

import org.caisi.dao.DemographicDAO;
import org.caisi.model.Demographic;

/**
 * Manager Interface for Demographics
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface DemographicManagerTickler {
	
	public void setDemographicDAO(DemographicDAO demographicDAO);
	/**
	 * Get a demographic using it's primary Id
	 * @param demographic_no Demographic Id
	 * @return A Demographic object
	 */
	public Demographic getDemographic(String demographic_no);
	
	/**
	 * Get a list of all demographic objects in the database
	 * @return List of Demographic objects
	 */
	public List getDemographics();
}
