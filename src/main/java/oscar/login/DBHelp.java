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
import oscar.util.SqlUtils;

/**
 * deprecated Use JPA instead, no new code should be written against this class.
 */
public final class DBHelp {
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

    public static ResultSet searchDBRecord(String sql) throws SQLException {
        ResultSet ret = null;
        try {
        	
            ret = DBHandler.GetSQL(sql);
        } catch (SQLException e) {
            logger.error("Error", e);
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
