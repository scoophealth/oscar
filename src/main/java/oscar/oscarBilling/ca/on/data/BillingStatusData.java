/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class BillingStatusData {
	private static final Logger _logger = Logger.getLogger(BillingStatusData.class);

	public BillingStatusData() {
	}

	public ArrayList<Hashtable<String,Object>> getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo) {
		ArrayList<Hashtable<String,Object>> list = new ArrayList<Hashtable<String,Object>>();

		String providerQuery = "";
		String startDateQuery = "";
		String endDateQuery = "";
		String demoQuery = "";

		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			providerQuery = " and provider_no = '" + providerNo + "'";
		}

		if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
			startDateQuery = " and billing_date >= '" + startDate + "' ";
		}

		if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
			endDateQuery = " and billing_date <= '" + endDate + "' ";
		}
		if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
			demoQuery = " and demographic_no = '" + demoNo + "' ";
		}

		String sql = "select * from billing_on_cheader1 where status = '" + StringEscapeUtils.escapeSql(statusType)
				+ "' " + providerQuery + startDateQuery + endDateQuery + demoQuery;
		MiscUtils.getLogger().debug("bill status query " + sql);
		try {
			BillingONDataHelp db = new BillingONDataHelp();
			ResultSet rs = db.searchDBRecord(sql);
			while (rs.next()) {
				Hashtable<String,Object> h = new Hashtable<String,Object>();
				h.put("billing_no", "" + rs.getInt("id"));
				h.put("demographic_no", rs.getString("demographic_no"));
				h.put("status", rs.getString("status"));
				h.put("provider_no", rs.getString("provider_no"));
				h.put("demographic_name", rs.getString("demographic_name"));
				h.put("billing_date", rs.getString("billing_date"));
				h.put("billing_time", rs.getString("billing_time"));
				h.put("total", rs.getString("total"));
				h.put("clinic", rs.getString("clinic"));
				list.add(h);
			}
		} catch (Exception e) {
			_logger.error("getBills(sql = " + sql + ")");
		}
		return list;
	}
}
