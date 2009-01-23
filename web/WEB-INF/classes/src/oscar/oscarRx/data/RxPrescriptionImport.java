  
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
    String providerNo = "";
    String demographicNo = "";
    String rxDate = "";
    String endDate = "";
    String BN = "";
    int takemin = 0;
    int takemax = 0;
    String regionalId = "";
    String frequencyCode = "";
    String duration = "";
    String durationUnit = "D";
    String quantity = "";
    int repeat = 0;
    String lastRefillDate = "";
    String special = "";
    boolean nosubs = false;
    boolean prn = false;
    boolean archived = false;
    String route = "";
    String drugForm = "";
    String createDate = "";
    String dosage = "";
    String unit = "";
    int seq_no = 1;
    boolean longTerm = false;
    boolean pastMed = false;
    int patientCompliance = 0;
    
    
    public RxPrescriptionImport(String providerNo, String demographicNo, String rxDate, String endDate, String BN, String regionalId, String frequencyCode,
	    String duration, String quantity, int repeat, String lastRefillDate, String special, String route, String drugForm, String createDate,
	    String dosage, int takemin, int takemax, String unit, boolean longTerm, boolean pastMed, int patientCompliance, int seq_no) {
	this.providerNo = providerNo;
	this.demographicNo = demographicNo;
	this.rxDate = rxDate;
	this.endDate = endDate;
	this.BN = BN;
	this.takemin = takemin;
	this.takemax = takemax;
	this.regionalId = regionalId;
	this.frequencyCode = frequencyCode;
	this.duration = duration;
	this.quantity = quantity;
	this.repeat = repeat;
	this.lastRefillDate = lastRefillDate;
	this.special = special;
	this.route = route;
	this.drugForm = drugForm;
	this.createDate = createDate;
	this.dosage = dosage;
	this.unit = unit;
	this.seq_no = seq_no;
	this.longTerm = longTerm;
	this.pastMed = pastMed;
	this.patientCompliance = patientCompliance;
    }
    
    public void Save() throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "INSERT INTO drugs (provider_no, demographic_no, rx_date, end_date, BN, takemin, takemax, regional_identifier, " +
		"freqcode, duration, durunit, quantity, special, route, drug_form, create_date, dosage, unit, `repeat`, " +
		"last_refill_date, nosubs, prn, archived, GCN_SEQNO, long_term, past_med, patient_compliance, custom_instructions) VALUES ('" +
		this.providerNo + "','" + this.demographicNo + "','" + this.rxDate + "','" + this.endDate + "','" + 
		this.BN + "'," + this.takemin + "," + this.takemax + ",'" + this.regionalId + "','" + this.frequencyCode + "','" + this.duration + "','" + 
		this.durationUnit + "','" + this.quantity + "','" + this.special + "','" + this.route + "','" + this.drugForm + "','" + 
		this.createDate + "','" + this.dosage + "','" + this.unit + "'," + this.repeat + ",'" + this.lastRefillDate + "'," + 
		this.nosubs + "," + this.prn + "," + this.archived + "," + this.seq_no + "," + this.longTerm + "," + this.pastMed + "," + this.patientCompliance + ",1)";

	db.RunSQL(sql);
	db.CloseConn();
    }
    
    public Long getImportedDrugId() throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "SELECT MAX(drugid) FROM drugs WHERE " +
		"provider_no='"		+ this.providerNo	 + "' AND " +
		"demographic_no='"	+ this.demographicNo	 + "' AND " +
		"rx_date='"		+ this.rxDate		 + "' AND " +
		"end_date='"		+ this.endDate		 + "' AND " +
		"BN='"			+ this.BN		 + "' AND " +
		"takemin="		+ this.takemin		 + " AND "  +
		"takemax="		+ this.takemax		 + " AND "  +
		"regional_identifier='" + this.regionalId	 + "' AND " +
		"freqcode='"		+ this.frequencyCode	 + "' AND " +
		"duration='"		+ this.duration		 + "' AND " +
		"durunit='"		+ this.durationUnit	 + "' AND " +
		"quantity='"		+ this.quantity		 + "' AND " +
		"special='"		+ this.special		 + "' AND " +
		"route='"		+ this.route		 + "' AND " +
		"drug_form='"		+ this.drugForm		 + "' AND " +
		"create_date='"		+ this.createDate	 + "' AND " +
		"dosage='"		+ this.dosage		 + "' AND " +
		"unit='"		+ this.unit		 + "' AND " +
		"`repeat`="		+ this.repeat		 + " AND "  +
		"last_refill_date='"	+ this.lastRefillDate	 + "' AND " +
		"nosubs="		+ this.nosubs		 + " AND "  +
		"prn="			+ this.prn		 + " AND "  +
		"archived="		+ this.archived		 + " AND "  +
		"GCN_SEQNO="		+ this.seq_no		 + " AND "  +
		"long_term="		+ this.longTerm		 + " AND "  +
		"past_med="		+ this.pastMed		 + " AND "  +
		"patient_compliance="	+ this.patientCompliance + " AND "  +
		"custom_instructions=1";
	ResultSet rs = db.GetSQL(sql);
	db.CloseConn();
	if (rs.next()) return rs.getLong(1);
	else return null;
    }
}
