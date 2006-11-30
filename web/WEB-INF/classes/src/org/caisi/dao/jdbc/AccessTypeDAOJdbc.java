package org.caisi.dao.jdbc;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.caisi.dao.AccessTypeDAO;

public class AccessTypeDAOJdbc implements AccessTypeDAO {

	private DataSource ds = null;
	
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}
	public void addAccessType(String name, String type) {
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			String sql = "insert into access_type (name,type) values (\'" + StringEscapeUtils.escapeSql(name) + "\',\'" + type + "\')";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
			stmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
