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
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JdbcBillingErrorCodeImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingErrorCodeImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public Properties getBillingErrorCodeProp(String strServiceCode) {
		Properties ret = new Properties();
		String sql = "select * from billing_on_errorCode where code in (" + strServiceCode + ")";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret.setProperty(rs.getString("code"), rs.getString("description"));
			}
		} catch (SQLException e) {
			_logger.error("getBillingErrorCodeProp(sql = " + sql + ")");
		}
		return ret;
	}

	public String getCodeDesc(String strServiceCode) {
		String ret = null;
		String sql = "select * from billing_on_errorCode where code ='" + strServiceCode + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret = rs.getString("description");
			}
		} catch (SQLException e) {
			_logger.error("getCodeDesc(sql = " + sql + ")");
		}
		return ret;
	}

}
