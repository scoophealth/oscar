/*
* Copyright (c) 2009. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Hibernate;


public class ImageStringToBlob {

	public static void main(String[] args) {
		Connection conn = null;
		
		if( args.length != 1 ) {
			System.out.println("Missing argument: full path to <oscar.properties>");
			return;
		}
		
		try {
			
			FileInputStream fin = new FileInputStream(args[0]);        
	        Properties prop = new Properties();
	        prop.load(fin);
	        
	        String driver = prop.getProperty("db_driver");
			String uri = prop.getProperty("db_uri");
			String db = prop.getProperty("db_name");
			String username = prop.getProperty("db_username");
			String password = prop.getProperty("db_password");
			
			Class.forName(driver);
			conn = DriverManager.getConnection(uri + db, username, password);
			conn.setAutoCommit(true); // no transactions
			
			/*
			 * select all records ids with image_data not null and contents is null
			 * for each id fetch record
			 * migrate data from image_data to contents
			 */
			String sql = "select image_id from client_image where image_data is not null and contents is null";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			List<Long> ids = new ArrayList<Long>();
			
			while(rs.next()) {
				ids.add(rs.getLong("image_id"));
			}
			
			rs.close();
			
			sql = "select image_data from client_image where image_id = ?";
			pst = conn.prepareStatement(sql);
			
			System.out.println("Migrating image data for " + ids.size() + " images...");
			for(Long id : ids) {
				pst.setLong(1, id);
				ResultSet imagesRS = pst.executeQuery();
				while(imagesRS.next()) {
					String dataString = imagesRS.getString("image_data");
					Blob dataBlob = fromStringToBlob(dataString);
					if(writeBlobToDb(conn, id, dataBlob) == 1) {
						System.out.println("Image data migrated for image_id: " + id);
					}
				}
				imagesRS.close();
			}
			System.out.println("Migration completed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static int writeBlobToDb(Connection conn, Long id, Blob dataBlob) throws Exception {
		String sql = "update client_image set contents = ? where image_id = ?";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setBlob(1, dataBlob);
		pst.setLong(2, id);
		
		return pst.executeUpdate();
	}

	private static Blob fromStringToBlob(String dataString) throws Exception {
		byte[] decoded = Base64.decodeBase64(dataString.getBytes());
		return Hibernate.createBlob(Base64.encodeBase64(decoded));
	}
}
