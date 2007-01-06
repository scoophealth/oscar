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

import org.oscarehr.PMmodule.model.Provider;

/**
 * DAO interface for working with Provider table in OSCAR
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ProviderDAO extends DAO {
	
	/**
	 * Get all the providers
	 * @return The list of providers
	 */
	public List getProviders();
	
	/**
	 * Get provider using it's primary id.
	 * @param provider_no The Id
	 * @return The Provider object
	 */
	public Provider getProvider(String provider_no);
	
	/**
	 * Search for a provider using first and last names
	 * @param lastName Last Name
	 * @param firstName First Name
	 * @return A Provider object if found
	 */
	public Provider getProviderByName(String lastName,String firstName);
	
}
