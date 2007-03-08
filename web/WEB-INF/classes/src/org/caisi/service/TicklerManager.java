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


package org.caisi.service;

import java.util.List;

import org.caisi.dao.CustomFilterDAO;
import org.caisi.dao.TicklerDAO;
import org.caisi.model.CustomFilter;
import org.caisi.model.Tickler;

/**
 * Manager Interface for Ticklers
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface TicklerManager {
	
	public void setTicklerDAO(TicklerDAO dao);
	public void setCustomFilterDAO(CustomFilterDAO dao);
	
	/**
	 * Add a new tickler
	 * @param tickler
	 */
	public void addTickler(Tickler tickler);
	
	/**
	 * Get all Ticklers
	 * @return
	 */
	public List getTicklers();
	
	/**
	 * Get a tickler using it's primary Id
	 * @param tickler_no
	 * @return
	 */
	public Tickler getTickler(String tickler_no);
	
	
	/**
	 * update a tickler's assignee
	 * @param tickler_no The primary Id
	 * @param provider The provider reassign the tickler
	 * @param task_assigned_to the provider being assigned to
	 */
	public void reassign(String tickler_no, String provider, String task_assigned_to);	
	
	/**
	 * Add a comment to a tickler
	 * @param tickler_no The primary Id
	 * @param provider The provider adding the comment
	 * @param message The comment itself
	 */
	public void addComment(String tickler_no, String provider, String message);
	
	/**
	 * Set a tickler to 'Active'
	 * @param tickler_no
	 * @param provider
	 */
	public void activateTickler(String tickler_no, String provider);
	
	/**
	 * Set a tickler to 'Complete'
	 * @param tickler_no
	 * @param provider
	 */
	public void completeTickler(String tickler_no, String provider);
	
	/**
	 * Set a tickler to 'Delete'
	 * @param tickler_no
	 * @param provider
	 */
	public void deleteTickler(String tickler_no, String provider);
	
	/**
	 * Get all ticklers which match filter
	 * @param filter
	 * @return
	 */
	public List getTicklers(CustomFilter filter);
	
	/**
	 * Get all custom filters
	 * @return
	 */
	public List getCustomFilters();
	
	/**
	 * Get all custom filters for a provider
	 * @param provider_no the provider
	 * @return the list of filters
	 */
	public List getCustomFilters(String provider_no);
	
	/**
	 * Get a Custom Filter
	 * @param name
	 * @return
	 */
	public CustomFilter getCustomFilter(String name);
	
	public CustomFilter getCustomFilterById(Integer id);
	
	/**
	 * Save a custom filter
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
