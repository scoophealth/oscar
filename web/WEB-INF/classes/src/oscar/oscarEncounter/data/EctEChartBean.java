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
package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import oscar.oscarDB.DBHandler;

public class EctEChartBean {
	public EctEChartBean() {
	}
	public void setEChartBean(String demoNo) {
		demographicNo = demoNo;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql = "select * from eChart where demographicNo=" + demoNo
					+ " ORDER BY eChartId DESC";
//         			+ " ORDER BY eChartId DESC limit 1";
			ResultSet rs = db.GetSQL(sql);
			if (rs.next()) {
				eChartTimeStamp = rs.getTimestamp("timeStamp");
				socialHistory = db.getString(rs,"socialHistory");
				familyHistory = db.getString(rs,"familyHistory");
				medicalHistory = db.getString(rs,"medicalHistory");
				ongoingConcerns = db.getString(rs,"ongoingConcerns");
				reminders = db.getString(rs,"reminders");
				encounter = db.getString(rs,"encounter");
				subject = db.getString(rs,"subject");
				providerNo = db.getString(rs,"providerNo");
			} else {
				eChartTimeStamp = null;
				socialHistory = "";
				familyHistory = "";
				medicalHistory = "";
				ongoingConcerns = "";
				reminders = "";
				encounter = "";
				subject = "";
				providerNo = "";
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public Date eChartTimeStamp;
	public String providerNo;
	public String userName;
	public String demographicNo;
	public String socialHistory;
	public String familyHistory;
	public String medicalHistory;
	public String ongoingConcerns;
	public String reminders;
	public String encounter;
	public String subject;
}