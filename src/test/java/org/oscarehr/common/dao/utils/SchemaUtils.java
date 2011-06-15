package org.oscarehr.common.dao.utils;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.NotImplementedException;

/**
 * This class is a utility class to create and drop the database schema and all it's bits.
 * This class is highly database specific and alternate versions of this class will have to be 
 * written for other databases.
 */
public class SchemaUtils
{
	private static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		String driver=ConfigUtils.getProperty("db_driver");
		String user=ConfigUtils.getProperty("db_user");
		String password=ConfigUtils.getProperty("db_password");
		String urlPrefix=ConfigUtils.getProperty("db_url_prefix");
		
		Class.forName(driver).newInstance();
		
		return(DriverManager.getConnection(urlPrefix, user, password));
	}
	
	/**
	 * Intent is to support junit tests.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void dropDatabaseIfExists() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			s.executeUpdate("drop database if exists "+ConfigUtils.getProperty("db_schema"));
		}
		finally
		{
			c.close();
		}
	}
	
	/**
	 * Intent is for new installations.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void createDatabaseAndTables() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchAlgorithmException
	{
		String schema=ConfigUtils.getProperty("db_schema");
		
		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			s.executeUpdate("create database "+schema);
			s.executeUpdate("use "+schema);
			
			runCreateTablesScript(c);			
		}
		finally
		{
			c.close();
		}
	}
	
	private static int loadFileIntoMySQL(String filename) throws IOException {
		String dir = new File(filename).getParent();
		Process p = Runtime.getRuntime().exec(new String[] {"mysql","--user="+ConfigUtils.getProperty("db_user"),"--password="+ConfigUtils.getProperty("db_password"),"-e","source "+filename,ConfigUtils.getProperty("db_schema")},new String[]{},new File(dir));
					
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        String s= null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        
        // read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        int exitValue = -1;
        try {
        	exitValue = p.waitFor();
        }catch(InterruptedException e) {
        	throw new IOException("error with process");
        }		
		return exitValue;
	}

	private static void runCreateTablesScript(Connection c) throws IOException, SQLException
	{				
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/oscarinit.sql"),0);

		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/oscarinit_on.sql"),0);
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/oscardata.sql"),0);
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/oscardata_on.sql"),0);
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/icd9.sql"),0);

		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/caisi/initcaisi.sql"),0);
		
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/caisi/initcaisidata.sql"),0);
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/caisi/populate_issue_icd9.sql"),0);
		//		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/icd9_issue_groups.sql"),0);
		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/measurementMapData.sql"),0);
//		assertEquals(loadFileIntoMySQL(System.getProperty("basedir") + "/database/mysql/expire_oscardoc.sql"),0);
		
	}
	
	/**
	 * Intended for existing installations.
	 */
	public static void upgradeDatabase()
	{
		throw(new NotImplementedException("Not implemented yet..."));
	}
	
	/**
	 * Intent is for junit tests.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void dropAndRecreateDatabase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchAlgorithmException
	{
		dropDatabaseIfExists();
		createDatabaseAndTables();
	}
	
	public static void main(String... argv) throws Exception
	{
		dropAndRecreateDatabase();
	}
}