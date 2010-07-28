/*
 * Created on 2005-5-19
 *
 */
package oscar.login;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerParam;
import oscar.util.SqlUtils;

public class DBHelp {
    private static final Logger logger = MiscUtils.getLogger();

    public static boolean updateDBRecord(String sql) throws SQLException {
        try {
        	SqlUtils.update(sql);
            return(true);
        } catch (PersistenceException e) {
            logger.error("Error", e);
            return(false);
        }
    }


    public static int updateDBRecord(String sql, DBPreparedHandlerParam[] params) throws SQLException {
        int ret = 0;
        try {
        	DBPreparedHandler db = new DBPreparedHandler();
            ret = db.queryExecuteUpdate(sql, params);
        } catch (SQLException e) {
        	logger.error("Error", e);
        }
        return ret;
    }
    	
    public static ResultSet searchDBRecord(String sql) throws SQLException {
        ResultSet ret = null;
        try {
        	DBHandler db = new DBHandler();
            ret = db.GetSQL(sql);
        } catch (SQLException e) {
            logger.error("Error", e);
        }
        
        return ret;
    }

    public static ResultSet searchDBRecord(String sql, DBPreparedHandlerParam[] params) throws SQLException {
        ResultSet ret = null;
        try {
        	DBPreparedHandler db = new DBPreparedHandler();
            ret = db.queryResults_paged(sql, params, 0);
        } catch (SQLException e) {
            logger.error("Error", e);
        }
        return ret;
    }
    
    public static boolean updateDBRecord(String sql, String userId) throws SQLException {
        boolean ret = false;
        try {
        	DBHandler db = new DBHandler();
            db.RunSQL(sql);
            ret = true;
        } catch (SQLException e) {
            ret = false;
            logger.error("updateDBRecord(sql = " + sql + ", userId = " + userId + ")",e);
        }
        return ret;
    }

    public static ResultSet searchDBRecord(String sql, String userId) throws SQLException {
        ResultSet ret = null;
        try {
        	DBHandler db = new DBHandler();
            ret = db.GetSQL(sql);
            logger.info("searchDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } catch (SQLException e) {
            logger.error("searchDBRecord(sql = " + sql + ", userId = " + userId + ")");
        }
        return ret;
    }
    
    public static String getString(ResultSet rs,String columnName) throws SQLException
    {
    	return oscar.Misc.getString(rs, columnName);
    }
    
    public static String getString(ResultSet rs,int columnIndex) throws SQLException
    {
    	return oscar.Misc.getString(rs, columnIndex);
    }
}
