package org.caisi.dao;

import java.util.Date;
import java.util.List;

import org.caisi.model.Demographic;

/**
 * DAO interface for working with Demographic table in OSCAR
 * 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 */
public interface DemographicDAO extends DAO {

	/**
	 * Find a demographic by primary identifier
	 * 
	 * @param demographic_no
	 *            The demographic identifier
	 * @return The demographic
	 */
	public Demographic getDemographic(String demographic_no);

	/**
	 * Get a list of all demographics
	 * 
	 * @return The list
	 */
	public List getDemographics();

	public Demographic getDemographicById(Integer demographic_id);

	public List getDemographicByProgram(int programId, Date dt, Date defdt);

	public List getActiveDemographicByProgram(int programId, Date dt, Date defdt);

	public List getProgramIdByDemoNo(String demoNo);

	public List getDemoProgram(String demoNo);

	public void clear();
}
