package oscar.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Oscar super DAO implementation created to extract database access code from
 * JSP files. This class should be extended by a scope named DAO class, i.e.
 * AppointmentDao. Do not access methods of this class directly - use
 * OscarSuperManager methods instead.
 * 
 * @author Eugene Petruhin
 * 
 */
public abstract class OscarSuperDao extends JdbcDaoSupport {

	protected abstract String[][] getDbQueries();

	protected abstract Map<String, RowMapper> getRowMappers();

	/**
	 * Executes a parameterized select query identified by a key.<br>
	 * Returned collection item is an automatically populated Map.
	 * 
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of Map objects created for each result set row
	 */
	@SuppressWarnings("unchecked")
	public List<Map> executeSelectQuery(String queryName, Object[] params) {
		return getJdbcTemplate().queryForList(getSqlQueryByKey(queryName), params);
	}

	/**
	 * Executes a parameterized select query identified by a key.<br>
	 * Returned collection item is a value object populated by a row mapper
	 * identified by the same key.
	 * 
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of value objects created for each result set row by a row
	 *         mapper
	 */
	@SuppressWarnings("unchecked")
	public List executeRowMappedSelectQuery(String queryName, Object[] params) {
		return getJdbcTemplate().query(getSqlQueryByKey(queryName), params, getRowMapperByKey(queryName));
	}

	/**
	 * Executes a parameterized insert/update/delete query identified by a key.<br>
	 * 
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return number of affected rows
	 */
	public int executeUpdateQuery(String queryName, Object[] params) {
		return getJdbcTemplate().update(getSqlQueryByKey(queryName), params);
	}

	/**
	 * Retrieves a sql query associated with a query name or reports an error.
	 * 
	 * @param key
	 *            query name
	 * @return sql query
	 */
	private String getSqlQueryByKey(String key) {
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
	 * @param key
	 *            query name
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
