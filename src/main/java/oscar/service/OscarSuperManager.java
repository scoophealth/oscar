/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import oscar.dao.OscarSuperDao;

/**
 * Oscar super manager implementation created to extract database access code
 * from JSP files. This class should be injected with every scope named DAO
 * class that extends OscarSuperDao. Use methods of this manager in JSP/Action
 * code to perform database access: <br>
 * <br>
 *
 * @author Eugene Petruhin
 *
 */
public class OscarSuperManager {

	private Map<String, OscarSuperDao> oscarDaoMap = new TreeMap<String, OscarSuperDao>();

	
	private OscarSuperDao providerSuperDao;

	
	public void setProviderSuperDao(OscarSuperDao providerDao) {
		this.providerSuperDao = providerDao;
	}

	/**
	 * Enables every injected dao in this manager to be accessed by its name.<br>
	 * Don't forget to update this function then you add a dao field.
	 */
	public void init() {
		oscarDaoMap.put("providerDao", providerSuperDao);
		
		// making sure all daos have been injected properly
		for (String daoName : oscarDaoMap.keySet()) {
			if (oscarDaoMap.get(daoName) == null) {
				throw new IllegalStateException(
						"Dao with specified name has not been injected into OscarSuperManager: " + daoName);
			}
		}
	}

	/**
	 * Directs a call to a specified dao object that executes a parameterized
	 * select query identified by a query name.<br>
	 * Returned collection item is an automatically populated Map.
	 *
	 * @param daoName
	 *            dao class key
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of Map objects created for each result set row
	 */
	public List<Map<String, Object>> find(String daoName, String queryName, Object[] params) {
		return getDao(daoName).executeSelectQuery(queryName, params);
	}

	/**
	 * Directs a call to a specified dao object that executes a parameterized
	 * select query identified by a query name.<br>
	 * Returned collection item is a value object populated by a row mapper
	 * identified by the same query name.
	 *
	 * @param daoName
	 *            dao class key
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of value objects created for each result set row by a row
	 *         mapper
	 */
	public List<Object> populate(String daoName, String queryName, Object[] params) {
		return getDao(daoName).executeRowMappedSelectQuery(queryName, params);
	}

	/**
	 * Makes sure a requested dao is found or reported missing.
	 *
	 * @param daoName
	 *            dao name
	 * @return dao instance
	 */
	protected OscarSuperDao getDao(String daoName) {
		OscarSuperDao oscarSuperDao = oscarDaoMap.get(daoName);
		if (oscarSuperDao != null) {
			return oscarSuperDao;
		}
		throw new IllegalArgumentException("OscarSuperManager contains no dao with specified name: " + daoName);
	}
}
