/*
 * DBHandler.java
 *
 * Created on August 11, 2003, 10:21 PM
 */

package oscar.oscarMDSLab.dbUtil;
import java.io.*;
import java.sql.*;

public class DBHandler
{

    public static String MDS_DATA = "oscar_mcmaster";
    private String  connDriver = "com.mysql.jdbc.Driver";
    private String  connURL = "jdbc:mysql:///";
    private String  connUser = "root";
    private String  connPwd = "liyi";
    private int     connInitialConnections = 1;
    private int     connMaxConnections = 10;
    private boolean connWaitIfBusy = true;

    private Connection conn;
    private ConnectionPool pool;

    public DBHandler(String dbName)
    {
        try
        {               
            pool = MDSLabPool.getInstance(connDriver, connURL + dbName, connUser,
                    connPwd, connInitialConnections, connMaxConnections, connWaitIfBusy);

            conn = pool.getConnection();

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    synchronized public java.sql.ResultSet GetSQL(String SQLStatement) throws SQLException
    {
        Statement stmt;
        ResultSet rs = null;

        System.out.println(SQLStatement);

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQLStatement);
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            throw e;
        }

        return rs;
    }

    synchronized public boolean RunSQL(String SQLStatement) throws SQLException
    {
        boolean b = false;

        System.out.println(SQLStatement);

        try
        {
            Statement stmt;
            stmt = conn.createStatement();
            b = stmt.execute(SQLStatement);
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            throw e;
        }

        return b;
    }
    
    public void CloseConn() throws SQLException
    {
        if(!conn.isClosed())
        {
                pool.free(conn);

//                System.out.println("Connection freed.");
        }
    }
}


    
    
    