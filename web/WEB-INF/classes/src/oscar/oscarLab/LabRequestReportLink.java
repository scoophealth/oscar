 /**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of FileUploadCheck
 *
 *
 */

package oscar.oscarLab;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class LabRequestReportLink {
    
    public static Hashtable getLinkByReport(String reportTable, Long reportId) throws SQLException {
	Hashtable link = new Hashtable();
	
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "SELECT * FROM labRequestReportLink WHERE report_table='" + reportTable+"'" +
							    " AND report_id="  + reportId;
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) {
	    link.put("id", rs.getLong("id"));
	    link.put("request_table", rs.getString("request_table"));
	    link.put("request_id", rs.getLong("request_id"));
	    link.put("request_date", rs.getDate("request_date"));
	    link.put("report_table", reportTable);
	    link.put("report_id", reportId);
	}
	return link;
    }
    
    public static String getRequestDate(String id) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "SELECT request_date FROM labRequestReportLink WHERE id=" + id;
	ResultSet rs = db.GetSQL(sql);
	Date requestDate = null;
	if (rs.next()) {
	    requestDate = rs.getDate(1);
	}
	return UtilDateUtilities.DateToString(requestDate,"yyyy-MM-dd");
    }
    
    public static Long getIdByReport(String reportTable, Long reportId) throws SQLException {
	Hashtable link = getLinkByReport(reportTable, reportId);
	return (Long)link.get("id");
    }
    
    public static void save(String requestTable, Long requestId, String requestDate, String reportTable, Long reportId) throws SQLException {
	if (requestDate==null || ("").equals(requestDate)) requestDate="0001-01-01";
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "INSERT INTO labRequestReportLink (request_table,request_id,request_date,report_table,report_id) VALUES ('" +
		     requestTable+"',"+requestId+",'"+requestDate+"','"+reportTable+"',"+reportId+")";
	db.RunSQL(sql);
    }
    
    public static void update(Long id, String requestTable, Long requestId, String requestDate) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "UPDATE labRequestReportLink SET request_table='" + requestTable + "'" +
						" AND request_id=" + requestId +
						" AND request_date='" + requestDate + "'" +
						" WHERE id=" + id;
	db.RunSQL(sql);
    }
}
