/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

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
	
	public CustomFilter getCustomFilter(String name, String providerNo);
		
	public CustomFilter getCustomFilterById(Integer id);
	
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
	
	public void deleteCustomFilterById(Integer id);
}
