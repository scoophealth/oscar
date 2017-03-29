/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------

package org.oscarehr.common.dao.utils;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * This class is a utility class to create and drop the database schema and all it's bits.
 * This class is highly database specific and alternate versions of this class will have to be
 * written for other databases.
 */
public class SchemaUtils
{
	private static Logger logger=MiscUtils.getLogger();
	
	public static boolean inited=false;
	public static Map<String,String> createTableStatements = new HashMap<String,String>();

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
	public static void createDatabaseAndTables() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		boolean skipDbInit = false;
		if(System.getProperty("oscar.dbinit.skip") != null && System.getProperty("oscar.dbinit.skip").equalsIgnoreCase("true")) 
			skipDbInit=true;
		
		String schema=ConfigUtils.getProperty("db_schema");
		logger.info("using schema : "+schema);
		
		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			
			if(!skipDbInit) {
				s.executeUpdate("create database "+schema);				
			}
			s.executeUpdate("use "+schema);

			runCreateTablesScript(c);
		}
		finally
		{
			c.close();
		}
	}


	public static void restoreTable(String... tableNames) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		restoreTable(true,tableNames);
	}

	public static void dropTable(String... tableNames) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		String schema=ConfigUtils.getProperty("db_schema");

		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			s.executeUpdate("use "+schema);
			for (int i = 0; i < tableNames.length; i++) {
				s.executeUpdate("drop table if exists " + tableNames[i]);
            }
			s.close();

		}
		finally
		{
			c.close();
		}
	}
	
	private static boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows");
	}

    public static String removeFKs(String s)
    {
        String r = s.replaceAll(",\\s+CONSTRAINT `\\w+` FOREIGN KEY .`\\w+`. REFERENCES `\\w+` .`\\w+`.","");
        r = r.replaceAll("ON DELETE CASCADE","");
        r = r.replaceAll("ON UPDATE CASCADE","");
        r = r.replaceAll("ON UPDATE NO ACTION","");
        r = r.replaceAll("ON DELETE NO ACTION","");
        return r;

    }



	public static void restoreTable(boolean includeInitData, String... tableNames) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		long start = System.currentTimeMillis();
		String schema=ConfigUtils.getProperty("db_schema");

		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			s.executeUpdate("use "+schema);
			s.executeUpdate("set foreign_key_checks = 0");
			for (int i = 0; i < tableNames.length; i++) {
				String tableName = tableNames[i];
				if (isWindows()) {
					tableName = tableName.toLowerCase(); // make it case insensitive by default
				}
				s.executeUpdate("drop table if exists " + tableName);
				
				String createTableStatement = createTableStatements.get(tableName);
				if (createTableStatement == null) {
					throw new IllegalStateException("Unable to locate create table statement for " + tableName + ". Please make sure that the table exists in the schema.");
				}
                // remove FK constraints as they cause errors during test with random data
                String sql= removeFKs(createTableStatement);
				s.executeUpdate(sql.replaceAll("_maventest", ""));
				
				if(includeInitData)
					s.executeUpdate("insert into " + tableName + " select * from " + tableName + "_maventest");
            }
			s.executeUpdate("set foreign_key_checks = 1");
			s.close();

		}
		finally
		{
			c.close();
		}
		long end = System.currentTimeMillis();
		long secsTaken = (end-start)/1000;
		if(secsTaken > 30) {
			MiscUtils.getLogger().info("Restoring db took " + secsTaken + " seconds.");
		}
	}

	
	public static void restoreAllTables() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		long start = System.currentTimeMillis();
		String schema=ConfigUtils.getProperty("db_schema");

		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			s.executeUpdate("use "+schema);
			for (String tableName:createTableStatements.keySet()) {
				s.executeUpdate("drop table if exists " + tableName);
                String sql= removeFKs(createTableStatements.get(tableName));
                s.executeUpdate(sql);
				s.executeUpdate("insert into " + tableName + " select * from " + tableName + "_maventest");
            }
			s.close();

		}
		finally
		{
			c.close();
		}
		long end = System.currentTimeMillis();
		long secsTaken = (end-start)/1000;
		if(secsTaken > 30) {
			MiscUtils.getLogger().info("Restoring db took " + secsTaken + " seconds.");
		}
	}

	public static int loadFileIntoMySQL(String filename) throws IOException {
		String dir = new File(filename).getParent();
                String env[] = null;
                String envStr = null;
                if (System.getProperty("os.name").startsWith("Windows")){
                    envStr = "SYSTEMROOT="+System.getenv("SYSTEMROOT");
                }
                if (envStr != null) {
                    env = new String[]{envStr};
                } else {
                    env = new String[]{};
                }

        String[] commandString={"mysql","--user="+ConfigUtils.getProperty("db_user"),"--password="+ConfigUtils.getProperty("db_password"),"--host="+ConfigUtils.getProperty("db_host"),"-e","source "+filename,ConfigUtils.getProperty("db_schema")};
        logger.info("Runtime exec command string : "+Arrays.toString(commandString));
		Process p = Runtime.getRuntime().exec(commandString,env, new File(dir));
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        String s= null;
        while ((s = stdInput.readLine()) != null) {
            MiscUtils.getLogger().info(s);
        }

        // read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
        	 MiscUtils.getLogger().info(s);
        }

        int exitValue = -1;
        try {
        	exitValue = p.waitFor();
        }catch(InterruptedException e) {
        	throw new IOException("error with process");
        }

        stdInput.close();
        stdError.close();

		return exitValue;
	}

	private static void runCreateTablesScript(Connection c) throws IOException
	{
		boolean skipDbInit = false;
		if(System.getProperty("oscar.dbinit.skip") != null && System.getProperty("oscar.dbinit.skip").equalsIgnoreCase("true")) 
			skipDbInit=true;
		
		if(!skipDbInit) {
			String baseDir=System.getProperty("basedir");
			logger.info("using baseDir : "+baseDir);
					
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscarinit.sql"),0);
	
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscarinit_on.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscardata.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscardata_on.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/icd9.sql"),0);
	
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/caisi/initcaisi.sql"),0);
	
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/caisi/initcaisidata.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/caisi/populate_issue_icd9.sql"),0);
			//		assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/icd9_issue_groups.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/measurementMapData.sql"),0);
	//		assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/expire_oscardoc.sql"),0);
	
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscarinit_bc.sql"),0);
			assertEquals(loadFileIntoMySQL(baseDir + "/database/mysql/oscardata_bc.sql"),0);


		 
			createTableStatements.clear();
			try {
				ResultSet rs = c.getMetaData().getTables(ConfigUtils.getProperty("db_schema"), null, "%", null);
				while(rs.next()) {
					String tableName = rs.getString("TABLE_NAME");	
					
					Statement stmt2 = c.createStatement();
					ResultSet rs2 = stmt2.executeQuery("show create table " + tableName + ";");
					if(rs2.next()) {
						String sql = rs2.getString(2);	
						createTableStatements.put(tableName, sql);
	
						Statement stmt = c.createStatement();
						stmt.executeUpdate("alter table "+ tableName + " rename to " + tableName + "_maventest");
						stmt.close();
					}
					rs2.close();
					stmt2.close();
				}
				rs.close();
	
	
			}catch(SQLException e) {
				MiscUtils.getLogger().error("Error:",e);
			}
		} else {
			//we're assuming a db ready to go..we just need to build the createTableStatementsMap
			createTableStatements.clear();
			try {
				ResultSet rs = c.getMetaData().getTables(ConfigUtils.getProperty("db_schema"), null, "%", null);
				while(rs.next()) {
					String tableName = rs.getString("TABLE_NAME");		
					if(!tableName.endsWith("_maventest"))
						continue;
					Statement stmt2 = c.createStatement();
					ResultSet rs2 = stmt2.executeQuery("show create table " + tableName + ";");
					if(rs2.next()) {
						String sql = rs2.getString(2).replaceAll(tableName, tableName.substring(0,tableName.length()-10));	
						createTableStatements.put(tableName.substring(0,tableName.length()-10), sql);							
					}
					rs2.close();
					stmt2.close();
				}
				rs.close();
	
	
			}catch(SQLException e) {
				MiscUtils.getLogger().error("Error:",e);
			}
		}
				
		try {
			restoreTable(false,"ResourceStorage");
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error:",e);
		}
		inited=true;
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
	public static void dropAndRecreateDatabase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		boolean skipDbInit = false;
		if(System.getProperty("oscar.dbinit.skip") != null && System.getProperty("oscar.dbinit.skip").equalsIgnoreCase("true")) 
			skipDbInit=true;
		if(!skipDbInit)
			dropDatabaseIfExists();
		createDatabaseAndTables();
	}

	public static void main(String... argv) throws Exception
	{
		dropAndRecreateDatabase();
	}
}
