/*
 * Created on 2005-5-19
 *
 */
package oscar.login;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerParam;

/**
 * @author yilee18
 */
public class DBHelp {
    private static final Logger _logger = Logger.getLogger(DBHelp.class);

    public synchronized boolean updateDBRecord(String sql) throws SQLException {
        boolean ret = false;
        DBHandler db = null;
        try {
            db = new DBHandler(DBHandler.OSCAR_DATA);
            ret = db.RunSQL(sql);
            ret = true;
        } catch (SQLException e) {
            ret = false;
            System.out.println(e.getMessage());
        } finally {
            db.CloseConn();
        }
        return ret;
    }

//    public synchronized boolean updateDBRecord(String sql, DBPreparedHandlerParam[] param) throws SQLException {
    public synchronized int updateDBRecord(String sql, DBPreparedHandlerParam[] params) throws SQLException {
        int ret = 0;
        DBPreparedHandler db = null;
        try {
            db = new DBPreparedHandler();
            ret = db.queryExecuteUpdate(sql, params);
//            _logger.info("updateDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } catch (SQLException e) {
//            _logger.error("updateDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } finally {
            db.closeConn();
        }
        return ret;
    }
    	
    public synchronized ResultSet searchDBRecord(String sql) throws SQLException {
        ResultSet ret = null;
        DBHandler db = null;
        try {
            db = new DBHandler(DBHandler.OSCAR_DATA);
            ret = db.GetSQL(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            db.CloseConn();
        }
        return ret;
    }

    public synchronized ResultSet searchDBRecord(String sql, DBPreparedHandlerParam[] params) throws SQLException {
        ResultSet ret = null;
        DBPreparedHandler db = null;
        try {
            db = new DBPreparedHandler();
            ret = db.queryResults_paged(sql, params, 0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeConn();
        }
        return ret;
    }
    
    public synchronized boolean updateDBRecord(String sql, String userId) throws SQLException {
        boolean ret = false;
        DBHandler db = null;
        try {
            db = new DBHandler(DBHandler.OSCAR_DATA);
            ret = db.RunSQL(sql);
            ret = true;
            _logger.info("updateDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } catch (SQLException e) {
            ret = false;
            _logger.error("updateDBRecord(sql = " + sql + ", userId = " + userId + ")",e);
        } finally {
            db.CloseConn();
        }
        return ret;
    }

    public synchronized ResultSet searchDBRecord(String sql, String userId) throws SQLException {
        ResultSet ret = null;
        DBHandler db = null;
        try {
            db = new DBHandler(DBHandler.OSCAR_DATA);
            ret = db.GetSQL(sql);
            _logger.info("searchDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } catch (SQLException e) {
            _logger.error("searchDBRecord(sql = " + sql + ", userId = " + userId + ")");
        } finally {
            db.CloseConn();
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
