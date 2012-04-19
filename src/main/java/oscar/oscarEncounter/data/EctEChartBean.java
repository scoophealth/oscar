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


package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctEChartBean {
	public EctEChartBean() {
	}
	public void setEChartBean(String demoNo) {
		demographicNo = demoNo;
		try {
			
			String sql = "select * from eChart where demographicNo=" + demoNo
					+ " ORDER BY eChartId DESC";
//         			+ " ORDER BY eChartId DESC limit 1";
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				eChartTimeStamp = rs.getTimestamp("timeStamp");
				socialHistory = oscar.Misc.getString(rs, "socialHistory");
				familyHistory = oscar.Misc.getString(rs, "familyHistory");
				medicalHistory = oscar.Misc.getString(rs, "medicalHistory");
				ongoingConcerns = oscar.Misc.getString(rs, "ongoingConcerns");
				reminders = oscar.Misc.getString(rs, "reminders");
				encounter = oscar.Misc.getString(rs, "encounter");
				subject = oscar.Misc.getString(rs, "subject");
				providerNo = oscar.Misc.getString(rs, "providerNo");
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
			MiscUtils.getLogger().error("Error", e);
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
