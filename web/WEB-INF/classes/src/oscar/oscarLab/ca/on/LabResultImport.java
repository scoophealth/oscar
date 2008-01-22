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
   
    public void SaveLabTR(String testName, String abn, String minimum, String maximum, String result, String units, String description, String ppId) throws SQLException {
	boolean b = false;            
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
	String sql = "INSERT INTO labTestResults (test_name, abn, minimum, maximum, result, units, description, labPatientPhysicianInfo_id) VALUES ('"
		+ testName + "','" + abn + "','" + minimum + "','" + maximum + "','" + result + "','" + units + "','" + description + "','" + ppId + "')";
	db.RunSQL(sql);
	db.CloseConn();
    }
    
    public String saveLabPPInfo(String labReportInfo_id, String accession_num, String firstname, String lastname, String sex, String hin, String birthdate, String collDate) throws SQLException {
	int id = 1;
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	
	String sql = "insert into labPatientPhysicianInfo (labReportInfo_id, accession_num, patient_first_name, patient_last_name, patient_sex, patient_health_num, patient_dob, collection_date) values ('"
		+labReportInfo_id+"','"+accession_num+"','"+firstname+"','"+lastname+"','"+sex+"','"+hin+"','"+birthdate+"','"+collDate+"')";
	db.RunSQL(sql);
	sql = "select max(id) from labPatientPhysicianInfo";
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) id = rs.getInt(1);
	
	rs.close();
	db.CloseConn();
	
	return String.valueOf(id);
    }
    
    public void savePatientLR(String demo_no, String lab_no) throws SQLException {
	boolean b = false;
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "insert into patientLabRouting (demographic_no, lab_no, lab_type) values ('"+demo_no+"', '"+lab_no+"', 'CML')";
	b = db.RunSQL(sql);
	db.CloseConn();
    }
    
    public String saveLabRI(String location_id, String print_date, String print_time) throws SQLException {
	int id = 1;
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "INSERT INTO labReportInformation (location_id, print_date, print_time) VALUES ('"+location_id+"', '"+print_date+"', '"+print_time+"')";
	db.RunSQL(sql);
	sql = "SELECT MAX(id) FROM labReportInformation";
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) id = rs.getInt(1);
	
	rs.close();
	db.CloseConn();
	
	return String.valueOf(id);
    }
}
