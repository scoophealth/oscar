package org.caisi.dao;

import java.util.List;

import org.caisi.model.CustomFilter;
/**
 * DAO interface for working with Custom Filters
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface CustomFilterDAO extends DAO {
	/**
	 * Search for a Custom Filter by name
	 * @param name The name
	 * @return The filter
	 */
	public CustomFilter getCustomFilter(String name);
	
	/**
	 * Get all custom filters
	 * @return The list of custom filters
	 */
	public List getCustomFilters();
	
	/**
	 * Get all custom filters for a user
	 * @param provider_no the provider
	 * @return The list of custom filters
	 */
	public List getCustomFilters(String provider_no);
	
	/**
	 * Add/Update a Custom Filter
	 * @param filter The filter
	 */
	public void saveCustomFilter(CustomFilter filter);
	
	/**
	 * Delete a custom filter
	 * @param name The name
	 */
	public void deleteCustomFilter(String name);
}
