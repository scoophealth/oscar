  
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

import oscar.oscarDB.*;
import oscar.oscarRx.util.*;
import oscar.oscarProvider.data.*;
import oscar.OscarProperties;

import java.util.*;
import java.sql.*;
import org.apache.commons.lang.*;

public class RxPrescriptionImport {
    String providerNo = "";
    String demographicNo = "";
    String rxDate = "";
    String endDate = "";
    String BN = "";
    String regionalId = "";
    String frequencyCode = "";
    String duration = "";
    String durationUnit = "D";
    String quantity = "";
    int repeat = 0;
    String special = "";
    boolean nosubs = false;
    boolean prn = false;
    boolean archived = false;
    String route = "";
    String createDate = "";
    String dosage = "";
    String unit = "";
    int seq_no = 1;
    
    public RxPrescriptionImport(String providerNo, String demographicNo, String rxDate, String endDate,
	    String BN, String regionalId, String frequencyCode, String duration, String quantity, int repeat, 
	    String special, String route, String createDate, String dosage, String unit, int seq_no) {
	this.providerNo = providerNo;
	this.demographicNo = demographicNo;
	this.rxDate = rxDate;
	this.endDate = endDate;
	this.BN = BN;
	this.regionalId = regionalId;
	this.frequencyCode = frequencyCode;
	this.duration = duration;
	this.quantity = quantity;
	this.repeat = repeat;
	this.special = special;
	this.route = route;
	this.createDate = createDate;
	this.dosage = dosage;
	this.unit = unit;
	this.seq_no = seq_no;
    }
    
    public void Save() {
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "INSERT INTO drugs (provider_no, demographic_no, rx_date, end_date, BN, " +
		    "regional_identifier, freqcode, duration, durunit, quantity, special, route, " +
		    "create_date, dosage, unit, `repeat`, nosubs, prn, archived, GCN_SEQNO) VALUES ('" +
		    this.providerNo + "','" + this.demographicNo + "','" + this.rxDate + "','" + this.endDate + "','" + 
		    this.BN + "','" + this.regionalId + "','" + this.frequencyCode + "','" + this.duration + "','" + 
		    this.durationUnit + "','" + this.quantity + "','" + this.special + "','" + this.route + "','" + 
		    this.createDate + "','" + this.dosage + "','" + this.unit + "'," + 
		    this.repeat + "," + this.nosubs + "," + this.prn + "," + this.archived + "," + this.seq_no + ")";
	    
	    db.RunSQL(sql);
            db.CloseConn();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
