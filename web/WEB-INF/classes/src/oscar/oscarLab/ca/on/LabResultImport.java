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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar.oscarLab.ca.on;

import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;

public class LabResultImport {
   
    public boolean Save(String testName, String abn, String minimum, String maximum, String result, String units, String description, String ppId) throws SQLException {
	boolean b = false;            
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
	String sql = "INSERT INTO labTestResults (test_name, abn, minimum, maximum, result, units, description, labPatientPhysicianInfo_id) VALUES ('"
		+ testName + "','" + abn + "','" + minimum + "','" + maximum + "','" + result + "','" + units + "','" + description + "','" + ppId + "')";
	b = db.RunSQL(sql);
	db.CloseConn();            
	return b;            
    }
    
    public String saveLabPPInfo(String collDate) throws SQLException {
	String id = "1";
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	
	String sql = "insert into labPatientPhysicianInfo (collection_date) values ('" + collDate + "')";
	if (db.RunSQL(sql)) {
	    sql = "select max(id) from labPatientPhysicianInfo";
	    ResultSet rs = db.GetSQL(sql);
	    if (rs.next()) id = rs.getString(1);
	    rs.close();
	}
	db.CloseConn();
	
	return id;
    }
}
