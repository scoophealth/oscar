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


package oscar.oscarLab.ca.on;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.oscarehr.util.DbConnectionFilter;

import oscar.util.UtilDateUtilities;

public class LabResultImport {
   
    public static void SaveLabDesc(String description, String ppId) throws SQLException {
	String sql = "INSERT INTO labTestResults (description,  labPatientPhysicianInfo_id, line_type) VALUES (? , ? , 'D')";
        
        Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,description);
        ps.setString(2,ppId);
        ps.executeUpdate();
	ps.close();
	conn.close();
    }
    
    public static String saveLabPatientPhysicianInfo(String labReportInfo_id, String accession_num, String collDate, String firstname, String lastname, String sex, String hin, String birthdate, String phone) throws SQLException {
	String id = "";
	String sql = "INSERT INTO labPatientPhysicianInfo (labReportInfo_id, accession_num, patient_first_name, patient_last_name, patient_sex, patient_health_num, patient_dob, patient_phone, collection_date, service_date, lab_status, lastUpdateDate)" +
						 " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'F',now())";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, labReportInfo_id);
	pstmt.setString(2, accession_num);
	pstmt.setString(3, firstname);
	pstmt.setString(4, lastname);
	pstmt.setString(5, sex);
	pstmt.setString(6, hin);
	pstmt.setString(7, birthdate);
	pstmt.setString(8, phone);
	pstmt.setString(9, collDate);
	pstmt.setString(10, collDate);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getString(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
    
    public static String saveLabReportInfo(String location_id) throws SQLException {
	String id = "";
	String print_date = UtilDateUtilities.DateToString(new Date(),"yyyy-MM-dd");
	String print_time = UtilDateUtilities.DateToString(new Date(),"HH:mm:ss");
	String sql = "INSERT INTO labReportInformation (location_id, print_date, print_time) VALUES (?,?,?)";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, location_id);
	pstmt.setString(2, print_date);
	pstmt.setString(3, print_time);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getString(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
    
    public static String saveLabTestResults(String title, String testName, String abn, String minimum, String maximum, String result, String units, String description, String location, String ppId, String linetype, String last) throws SQLException {
	String id = "";
	String sql = "INSERT INTO labTestResults (title, test_name, abn, minimum, maximum, result, units, description, location_id, labPatientPhysicianInfo_id, line_type, last)" +
					" VALUES (?,?,?, ?,?,?, ?,?,?, ?,?,?)";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, title);
	pstmt.setString(2, testName);
	pstmt.setString(3, abn);
	pstmt.setString(4, minimum);
	pstmt.setString(5, maximum);
	pstmt.setString(6, result);
	pstmt.setString(7, units);
	pstmt.setString(8, description);
	pstmt.setString(9, location);
	pstmt.setString(10, ppId);
	pstmt.setString(11, linetype);
	pstmt.setString(12, last);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getString(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
    
    public static Long savePatientLabRouting(String demo_no, String lab_no) throws SQLException {
	Long id = null;
	String sql = "INSERT INTO patientLabRouting (demographic_no, lab_no, lab_type,created) values (?, ?, 'CML',now())";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, demo_no);
	pstmt.setString(2, lab_no);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getLong(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
    
    public static Long saveProviderLabRouting(String provider_no, String lab_no, String status, String comment, String timestamp) throws SQLException {
	Long id = null;
	if (timestamp==null || ("").equals(timestamp)) timestamp=null;
	String sql = "INSERT INTO providerLabRouting (provider_no, lab_no, status, comment, timestamp, lab_type) values (?,?,?,?,?,'CML')";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, provider_no);
	pstmt.setString(2, lab_no);
	pstmt.setString(3, status);
	pstmt.setString(4, comment);
	pstmt.setString(5, timestamp);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getLong(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
}
