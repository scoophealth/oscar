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
package oscar.oscarRx.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class RxPrescriptionImport {
    
    public static Long save(String providerNo, String demographicNo, String rxDate, String endDate, String writtenDate,
	    String createDate, String BN, String regionalId, String frequencyCode, String duration, String quantity,
	    int repeat, String lastRefillDate, String special, String route, String drugForm, String dosage,
	    int takemin, int takemax, String unit, boolean longTerm, boolean pastMed, int patientCompliance,
	    String outsideProviderName, String outsideProviderOhip, int seq_no) throws SQLException {
	Long id = null;
	String sql = "INSERT INTO drugs (provider_no, demographic_no, rx_date, end_date, written_date, create_date, " +
		"BN, regional_identifier, freqcode, duration, quantity, last_refill_date, special, route, drug_form, " +
		"dosage, unit, outside_provider_name, outside_provider_ohip, long_term, past_med, patient_compliance, " +
		"takemin, takemax, `repeat`, GCN_SEQNO, durunit, nosubs, prn, archived, custom_instructions)" +
		" VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,'D',false,false,0,1)";
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	Connection conn = db.getConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, providerNo);
	pstmt.setString(2, demographicNo);
	pstmt.setString(3, rxDate);
	pstmt.setString(4, endDate);
	pstmt.setString(5, writtenDate);
	pstmt.setString(6, createDate);
	pstmt.setString(7, BN);
	pstmt.setString(8, regionalId);
	pstmt.setString(9, frequencyCode);
	pstmt.setString(10, duration);
	pstmt.setString(11, quantity);
	pstmt.setString(12, lastRefillDate);
	pstmt.setString(13, special);
	pstmt.setString(14, route);
	pstmt.setString(15, drugForm);
	pstmt.setString(16, dosage);
	pstmt.setString(17, unit);
	pstmt.setString(18, outsideProviderName);
	pstmt.setString(19, outsideProviderOhip);
	pstmt.setBoolean(20, longTerm);
	pstmt.setBoolean(21, pastMed);
	pstmt.setInt(22, patientCompliance);
	pstmt.setInt(23, takemin);
	pstmt.setInt(24, takemax);
	pstmt.setInt(25, repeat);
	pstmt.setInt(26, seq_no);
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getLong(1);
	pstmt.close();
	conn.close();
	
	return id;
    }
}
