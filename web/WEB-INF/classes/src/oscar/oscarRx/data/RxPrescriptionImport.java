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
package oscar.oscarRx.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class RxPrescriptionImport {
    
    public static Long save(String providerNo, String demographicNo, String rxDate, String endDate, String writtenDate,
	    String createDate, String BN, String regionalId, String frequencyCode, String duration, String quantity,
	    int repeat, String lastRefillDate, String special, String route, String drugForm, String dosage,
	    int takemin, int takemax, String unit, boolean longTerm, boolean pastMed, int patientCompliance,
	    String outsideProviderName, String outsideProviderOhip, int seq_no) throws SQLException {
	
	String sql = "INSERT INTO drugs (provider_no, demographic_no, rx_date, end_date, written_date, create_date, " +
		"BN, regional_identifier, freqcode, duration, quantity, last_refill_date, special, route, drug_form, " +
		"dosage, unit, outside_provider_name, outside_provider_ohip, long_term, past_med, patient_compliance, " +
		"takemin, takemax, `repeat`, GCN_SEQNO, durunit, nosubs, prn, archived, custom_instructions" +
		") VALUES ('" +
		providerNo + "','" + demographicNo + "','" + 
		rxDate + "','" + endDate + "','" + writtenDate + "','" + createDate + "','" + 
		BN + "','" + regionalId + "','" + frequencyCode + "','" + duration + "','" + 
		quantity + "','" + lastRefillDate + "','" + special + "','" + route + "','" + 
		drugForm + "','" + dosage + "','" + unit + "','" + 
		outsideProviderName + "','" + outsideProviderOhip + "'," + 
		longTerm + "," + pastMed + "," + patientCompliance + "," + 
		takemin + "," + takemax + "," + repeat + "," + seq_no + "," + 
		",'D',false,false,0,1)";

	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	db.RunSQL(sql);
	
	sql = "SELECT MAX(drugid) FROM drugs WHERE " +
		"provider_no='"		  + providerNo		+ "' AND " +
		"demographic_no='"	  + demographicNo	+ "' AND " +
		"rx_date='"		  + rxDate		+ "' AND " +
		"end_date='"		  + endDate		+ "' AND " +
		"written_date='"	  + writtenDate		+ "' AND " +
		"create_date='"		  + createDate		+ "' AND " +
		"BN='"			  + BN			+ "' AND " +
		"regional_identifier='"   + regionalId		+ "' AND " +
		"freqcode='"		  + frequencyCode	+ "' AND " +
		"duration='"		  + duration		+ "' AND " +
		"quantity='"		  + quantity		+ "' AND " +
		"last_refill_date='"	  + lastRefillDate	+ "' AND " +
		"special='"		  + special		+ "' AND " +
		"route='"		  + route		+ "' AND " +
		"drug_form='"		  + drugForm		+ "' AND " +
		"dosage='"		  + dosage		+ "' AND " +
		"unit='"		  + unit		+ "' AND " +
		"outside_provider_name='" + outsideProviderName + "' AND " +
		"outside_provider_ohip='" + outsideProviderOhip + "' AND " +
		"long_term="		  + longTerm		+ " AND " +
		"past_med="		  + pastMed		+ " AND " +
		"patient_compliance="	  + patientCompliance	+ " AND " +
		"takemin="		  + takemin		+ " AND " +
		"takemax="		  + takemax		+ " AND " +
		"`repeat`="		  + repeat		+ " AND " +
		"GCN_SEQNO="		  + seq_no		+ " AND " +
		"durunit='D' AND nosubs=false AND prn=false AND archived=0 AND custom_instructions=1";
	
	ResultSet rs = db.GetSQL(sql);
	db.CloseConn();
	if (rs.next()) return rs.getLong(1);
	else return null;
    }
}
