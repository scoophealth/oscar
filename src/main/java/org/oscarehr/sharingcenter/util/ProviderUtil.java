/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */




package org.oscarehr.sharingcenter.util;

import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ProviderUtil {

	private static ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	/**
	 * This method will return the Provider associated with providerNo given.
	 * 
	 * @param providerNo
	 * @return
	 */
	public static Provider createProvider(Demographic demographic) {
		return createProvider(demographic, null);
	}
	
	public static Provider createProvider(String providerNo) {
		return providerDao.getProvider(providerNo);
	}
	
	/**
	 * Creates a provider, if there is no provider this will use the currently logged in doctor.
	 * 
	 * @param demographic The patient demographic.
	 * 
	 * @param providerNo The doctor provider number.
	 * 
	 * @return Returns a Provider object.
	 */
	public static Provider createProvider(Demographic demographic, String providerNo) {
		Provider provider = demographic.getProvider();

		if (provider == null) {
			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			provider = providerDao.getProvider(providerNo);
		}

		return provider;
	}
	
	/**
	 * This method will return the Provider associated with practionerNo given.
	 * 
	 * @param practitionerNo
	 * @return
	 */
	public static Provider getProviderByPractitionerNo(String practitionerNo) {
		return providerDao.getProviderByPractitionerNo(practitionerNo);
	}
	/**
	 * This method will return the List of Providers
	 * 
	 * @return
	 */
	public static List<Provider> getProviders() {
		return providerDao.getProviders();
	}
	/**
	 * This method will return the list of Providers that are either active or inactive.
	 * 
	 * @param active, true: active, false: inactive
	 * @return
	 */
 	public static List<Provider> getProviders(boolean active) {
 		return providerDao.getProviders(active);
 	}
 	/**
 	 * This method will return the list of Providers that have the given name.
 	 * 
 	 * @param name
 	 * @return
 	 */
	public static List<Provider> getProvidersByName(String name){
		return providerDao.search(name);
	}
	/**
	 * This method will return the list of Providers that have the given first name and last name.
	 * 
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	public static List<Provider> getProvidersByName(String firstname, String lastname) {
		return providerDao.getProviderFromFirstLastName(firstname, lastname);
	}
	/**
	 * This method will return the list of Providers of the given type. 
	 * 
	 * @param type
	 * @return
	 */
	public static List<Provider> getProvidersByType(String type) {
		return providerDao.getProvidersByType(type);
	}
 }
