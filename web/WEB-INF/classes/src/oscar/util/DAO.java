package oscar.util;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;

import java.sql.*;
import java.util.Properties;


public class DAO {
    private DBHandler dBHandler;
    private DBPreparedHandler dBPreparedHandler;
	private DBPreparedHandlerAdvanced dBPreparedHandlerAdvanced;
    private Properties pvar;

    public DAO(Properties pvar) throws SQLException {
		this.pvar = pvar;
		oscar.oscarDB.DBHandler.init(pvar.getProperty("db_name"),pvar.getProperty("db_driver"),pvar.getProperty("db_uri"),pvar.getProperty("db_username"),pvar.getProperty("db_password")  ) ;
		
    }

    protected void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }

            rs = null;
        }
    }

    protected void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
            }

            pstmt = null;
        }
    }

    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            conn = null;
        }
    }

    protected void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            conn = null;
        }
    }

    /**
     * @return
     */
    public DBHandler getDb() throws SQLException {
		return dBHandler = new DBHandler(DBHandler.OSCAR_DATA);
    }

    /**
     * @param handler
     */
    public void setDb(DBHandler handler) {
        dBHandler = handler;
    }

    /**
     * @return
     */
    public DBPreparedHandler getDBPreparedHandler() throws SQLException {
        return new DBPreparedHandler(pvar.getProperty("db_driver"),pvar.getProperty("db_uri") + pvar.getProperty("db_name"),pvar.getProperty("db_username"),pvar.getProperty("db_password"));
    }

	/**
	 * @return
	 */
	public DBPreparedHandlerAdvanced getDBPreparedHandlerAdvanced() throws SQLException {
		return new DBPreparedHandlerAdvanced(pvar.getProperty("db_driver"),pvar.getProperty("db_uri") + pvar.getProperty("db_name"),pvar.getProperty("db_username"),pvar.getProperty("db_password"));
	}

    /**
     * @param handler
     */
    public void setDBPreparedHandler(DBPreparedHandler handler) {
        dBPreparedHandler = handler;
    }

    protected String getStrIn(String[] ids) {
        String id = "";

        for (int i = 0; i < ids.length; i++) {
            if (i == 0) {
                id = ids[i];
            } else {
                id = id + "," + ids[i];
            }
        }

        return id;
    }
}
