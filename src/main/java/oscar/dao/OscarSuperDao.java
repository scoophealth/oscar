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

package oscar.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Oscar super DAO implementation created to extract database access code from JSP files. This class should be extended by a scope named DAO class, i.e. AppointmentDao. Do not access methods of this class directly - use OscarSuperManager methods instead.
 *
 * @author Eugene Petruhin
 */
public abstract class OscarSuperDao extends JdbcDaoSupport {

	protected static final Logger logger = MiscUtils.getLogger();

	protected abstract String[][] getDbQueries();

	protected abstract Map<String, RowMapper> getRowMappers();

	/**
	 * Executes a parameterized select query identified by a key.<br>
	 * Returned collection item is an automatically populated Map.
	 *
	 * @param queryName sql query key
	 * @param params sql query parameters
	 * @return List of Map objects created for each result set row
	 */
	public List<Map<String, Object>> executeSelectQuery(String queryName, Object[] params) {
		return getJdbcTemplate().queryForList(getSqlQueryByKey(queryName), params);
	}

	/**
	 * Executes a parameterized select query identified by a key.<br>
	 * Returned collection item is a value object populated by a row mapper identified by the same key.
	 *
	 * @param queryName sql query key
	 * @param params sql query parameters
	 * @return List of value objects created for each result set row by a row mapper
	 */
	@SuppressWarnings("unchecked")
	public List<Object> executeRowMappedSelectQuery(String queryName, Object[] params) {
		return getJdbcTemplate().query(getSqlQueryByKey(queryName), params, getRowMapperByKey(queryName));
	}

	/**
	 * Retrieves a sql query associated with a query name or reports an error.
	 *
	 * @param key query name
	 * @return sql query
	 */
	private String getSqlQueryByKey(String key) {
		logger.debug("Calling query " + key);
		for (String[] query : getDbQueries()) {
			if (query[0].equals(key)) {
				return query[1];
			}
		}
		throw new IllegalArgumentException("dbQueries array contains no query with specified name: " + key);
	}

	/**
	 * Retrieves a row mapper associated with a query name or reports an error.
	 *
	 * @param key query name
	 * @return row mapper
	 */
	private RowMapper getRowMapperByKey(String key) {
		RowMapper rowMapper = getRowMappers().get(key);
		if (rowMapper != null) {
			return rowMapper;
		}
		throw new IllegalArgumentException("rowMappers map contains no row mapper with specified name: " + key);
	}
}
