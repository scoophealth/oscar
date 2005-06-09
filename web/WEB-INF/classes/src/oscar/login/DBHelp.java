/*
 * Created on 2005-5-19
 *
 */
package oscar.login;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

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
            _logger.error("updateDBRecord(sql = " + sql + ", userId = " + userId + ")");
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

}
