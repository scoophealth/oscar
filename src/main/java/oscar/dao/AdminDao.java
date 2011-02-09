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
		{"security_add_record", "insert into security (user_name,password,provider_no,pin,b_ExpireSet,date_ExpireDate,b_LocalLockSet,b_RemoteLockSet) values(?,?,?,?,?,?,?,?)" },
	    {"security_search_detail", "select * from security where security_no=?"},
	    {"security_delete", "delete from security where security_no=? and provider_no!='super'"},
	    {"security_update_record", "update security set user_name=?,password=?,provider_no=?,pin=?,b_ExpireSet=?,date_ExpireDate=?,b_LocalLockSet=?,b_RemoteLockSet=? where security_no=?" },   
	    {"security_update_record2", "update security set user_name=?,password=?,provider_no=?,b_ExpireSet=?,date_ExpireDate=?,b_LocalLockSet=?,b_RemoteLockSet=? where security_no=?" },
	    {"security_update_record3", "update security set user_name=?,provider_no=?,pin=?,b_ExpireSet=?,date_ExpireDate=?,b_LocalLockSet=?,b_RemoteLockSet=? where security_no=?" },
	    {"security_update_record4", "update security set user_name=?,provider_no=?,b_ExpireSet=?,date_ExpireDate=?,b_LocalLockSet=?,b_RemoteLockSet=? where security_no=?" },    

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
