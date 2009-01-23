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

public class RxAllergyImport {
   
    public Long Save(String demographicNo, String entryDate, String description, String typeCode, String reaction, String startDate, String severity, String drugrefId) throws SQLException {
	boolean b = false;            
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
	String sql = "INSERT INTO allergies (demographic_no, entry_date, DESCRIPTION, TYPECODE, reaction, start_date, severity_of_reaction, drugref_id) VALUES ('"
		+ demographicNo + "','" + entryDate + "','" + description + "','" + typeCode + "','" + reaction + "','" + startDate + "','" + severity + "','" + drugrefId + "')";
	if (db.RunSQL(sql)) {
	    sql = "SELECT MAX(allergyid) FROM allergies WHERE " +
		    "demographic_no='"	     + demographicNo + "' AND " +
		    "entry_date='"	     + entryDate     + "' AND " +
		    "DESCRIPTION='"	     + description   + "' AND " +
		    "TYPECODE='"	     + typeCode	     + "' AND " +
		    "reaction='"	     + reaction	     + "' AND " +
		    "start_date='"	     + startDate     + "' AND " +
		    "severity_of_reaction='" + severity	     + "' AND " +
		    "drugref_id='"	     + drugrefId     + "'";
	    ResultSet rs = db.GetSQL(sql);
	    db.CloseConn();
	    
	    if (rs.next()) return rs.getLong(1);
	    else return null;
	} else {
	    return null;
	}
     }
}
