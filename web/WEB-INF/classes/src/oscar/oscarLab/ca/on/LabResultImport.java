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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class LabResultImport {
   
    public Long SaveLabTR(String testName, String abn, String minimum, String maximum, String result, String units, String description, String location, String ppId) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
	String sql = "INSERT INTO labTestResults (test_name, abn, minimum, maximum, result, units, description, location_id, labPatientPhysicianInfo_id, line_type) VALUES ('"
		+ testName + "','" + abn + "','" + minimum + "','" + maximum + "','" + result + "','" + units + "','" + description + "','" + location + "','" + ppId + "','C')";
	if (db.RunSQL(sql)) {
	    sql = "SELECT MAX(id) FROM labTestResults WHERE " +
		    "test_name='"		   + testName	 + "' AND " +
		    "abn='"			   + abn	 + "' AND " +
		    "minimum='"			   + minimum	 + "' AND " +
		    "maximum='"			   + maximum	 + "' AND " +
		    "result='"			   + result	 + "' AND " +
		    "units='"			   + units	 + "' AND " +
		    "description='"		   + description + "' AND " +
		    "location_id='"		   + location	 + "' AND " +
		    "labPatientPhysicianInfo_id='" + ppId	 + "' AND " +
		    "line_type='C'";
	    ResultSet rs = db.GetSQL(sql);
	    db.CloseConn();
	    if (rs.next()) return rs.getLong(1);
	    else return null;
	} else {
	    return null;
	}
    }
    
    public void SaveLabDesc(String description, String ppId) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
	String sql = "INSERT INTO labTestResults (description,  labPatientPhysicianInfo_id, line_type) VALUES (? , ? , 'D')";
        Connection c = db.GetConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1,description);
        ps.setString(2,ppId);
        ps.executeUpdate();
	db.CloseConn();
    }
    
    public String saveLabPPInfo(String labReportInfo_id, String accession_num, String firstname, String lastname, String sex, String hin, String birthdate, String phone, String collDate) throws SQLException {
	Long id = 1L;
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	
	String sql = "insert into labPatientPhysicianInfo (labReportInfo_id, accession_num, patient_first_name, patient_last_name, patient_sex, patient_health_num, patient_dob, patient_phone, collection_date, lab_status, service_date, doc_num) values ('"
		+labReportInfo_id+"','"+accession_num+"','"+firstname+"','"+lastname+"','"+sex+"','"+hin+"','"+birthdate+"','"+phone+"','"+collDate+"','N','','')";
	db.RunSQL(sql);
	sql = "select max(id) from labPatientPhysicianInfo";
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) id = rs.getLong(1);
	
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
	Long id = 1L;
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "INSERT INTO labReportInformation (location_id, print_date, print_time) VALUES ('"+location_id+"', '"+print_date+"', '"+print_time+"')";
	db.RunSQL(sql);
	sql = "SELECT MAX(id) FROM labReportInformation";
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) id = rs.getLong(1);
	
	rs.close();
	db.CloseConn();
	
	return String.valueOf(id);
    }
}
