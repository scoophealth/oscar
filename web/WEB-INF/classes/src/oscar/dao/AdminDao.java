package oscar.dao;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

/**
 * Oscar Admin DAO implementation created to extract database access code
 * from admin related JSP files. This class contains only actual sql
 * queries and row mappers.
 * 
 * @author Eugene Petruhin
 * 
 */
public class AdminDao extends OscarSuperDao {

	private Map<String, RowMapper> rowMappers = new TreeMap<String, RowMapper>();

	public AdminDao() {
	}

	private String [][] dbQueries = new String[][] { 
	    {"security_search_detail", "select * from security where security_no=?"},
	    {"provider_search_providerno", "select provider_no, first_name, last_name from provider order by last_name"},
	};

	/**
	 * Need to provide this method in order to let parent.getDbQueries() access child.dbQueries array.
	 */
	@Override
	protected String[][] getDbQueries() {
		return dbQueries;
	}

	/**
	 * Need to provide this method in order to let parent.getRowMappers() access child.rowMappers map.
	 */
	@Override
	protected Map<String, RowMapper> getRowMappers() {
		return rowMappers;
	}
}
