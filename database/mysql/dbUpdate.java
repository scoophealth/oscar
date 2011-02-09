/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

import java.sql.*;
import java.util.UUID;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class dbUpdate {
	public static void main( String[] args ) {

		if( args.length != 1 ) {
			System.out.println("Usage: java dbUpdate 'path/to/oscar.properties'");
			return;
		}

		try {
                        FileInputStream fin = new FileInputStream(args[0]);        
                        Properties prop = new Properties();
                        prop.load(fin);                                                
                        
                        String driver = prop.getProperty("db_driver");
                        System.out.println("Driver " + driver);
                        
                        String uri = prop.getProperty("db_uri");
                        System.out.println("URI " + uri);
                        
                        String dbName = prop.getProperty("db_name");
                        System.out.println("DB NAME " + dbName);
                        
                        String user = prop.getProperty("db_username");
                        System.out.println("DB USER " + user);
                        
                        String passwd = prop.getProperty("db_password");
                        System.out.println("DB PASSWD " + passwd);
                        
			Statement stmt, stmtUpdate;
			ResultSet rs, rsUpdate;

			Class.forName(driver);
			
                        uri += dbName;
			Connection con = DriverManager.getConnection(uri, user, passwd);

			stmt = con.createStatement();			

			rs = stmt.executeQuery("SELECT note_id FROM casemgmt_note where uuid is null");

			stmtUpdate = con.createStatement();
			UUID uuid;
			String updateSQL;
			while(rs.next()) {
				uuid = UUID.randomUUID();
				String id = rs.getString("note_id");
				updateSQL = "UPDATE casemgmt_note set uuid = '" + uuid + "' WHERE note_id = " + id;
				if( stmtUpdate.executeUpdate(updateSQL) != 1 ) 
					throw new Exception(updateSQL);				                                 
                                System.out.println(updateSQL);
			}
                        
                        rs.close();
                        rs = stmt.executeQuery("SELECT update_date, note_id FROM casemgmt_note where observation_date = '0000-00-00 00:00:00'");
                        while(rs.next()) {
                            updateSQL = "UPDATE casemgmt_note set observation_date = '" + rs.getString("update_date") + "' WHERE note_id = " + rs.getString("note_id");
                            if( stmtUpdate.executeUpdate(updateSQL) != 1 ) 
					throw new Exception(updateSQL);				                                 
                            
                            System.out.println(updateSQL);
                            
                        }
                        
                        rs.close();
			con.close();
                         
		}
                catch( FileNotFoundException e ) {
                    System.out.println("Could not open properties file:\n" + e.getMessage());
                }
                catch( IOException e ) {
                    System.out.println("Error reading properties file\n" + e.getMessage());
                }                
		catch( Exception e ) {
			System.out.println("DB ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
