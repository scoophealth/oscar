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
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class JdbcBillingCodeImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingCodeImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public List getBillingCodeAttr(String serviceCode) {
		List ret = new Vector();
		String sql = "select * from billingservice b where b.service_code='" + serviceCode + "' and b.billingservice_no = (select max(b2.billingservice_no) from billingservice b2 where b.service_code = b2.service_code and b2.billingservice_date <= now())";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret.add(serviceCode);
				ret.add(rs.getString("description"));
				ret.add(rs.getString("value"));
				ret.add(rs.getString("percentage"));
				ret.add(rs.getString("billingservice_date"));
                                ret.add(rs.getString("gstFlag"));
			}
		} catch (SQLException e) {
			_logger.error("getBillingCodeAttr(sql = " + sql + ")");
		}
		return ret;
	}

	public Properties getCodeDescByNames(List serviceCodeNames) {
		Properties ret = new Properties();
		String xTemp = "";
		for (int i = 0; i < serviceCodeNames.size(); i++) {
			xTemp += "'" + serviceCodeNames.get(i) + "',";
		}
		xTemp = xTemp.substring(0, xTemp.length() - 1);
		String sql = "select * from billingservice b where b.service_code in (" + xTemp + ")";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret.setProperty(rs.getString("service_code"), rs.getString("description"));
			}
		} catch (SQLException e) {
			_logger.error("getCodeDescByNames(sql = " + sql + ")");
		}
		return ret;
	}

	public boolean updateCodeByName(String serviceCode, String description, String value, String percentage,
			String billingservice_date, String gstFlag) {
		boolean ret = false;
		String sql = "update billingservice set description='" + StringEscapeUtils.escapeSql(description) + "', ";
		sql += " value='" + value + "', ";
		sql += " percentage='" + percentage + "', ";
                sql += " gstFlag='" + gstFlag + "', ";
		sql += " billingservice_date='" + billingservice_date + "' ";
		sql += "where service_code='" + serviceCode + "'";
		_logger.info("updateCodeByName(sql = " + sql + ")");
		ret = dbObj.updateDBRecord(sql);
		if (!ret) {
			_logger.error("updateCodeByName(sql = " + sql + ")");
		}
		return ret;
	}

	public int addCodeByStr(String serviceCode, String description, String value, String percentage,
			String billingservice_date, String gstFlag) {
		int ret = 0;
		String sql = "insert into billingservice (service_compositecode, service_code, description, value, percentage, gstFlag, billingservice_date) values ('', '";
		sql += serviceCode + "', '";
		sql += StringEscapeUtils.escapeSql(description) + "', '";
		sql += value + "', '";
		sql += percentage + "', '";
                sql += gstFlag + "', '";
		sql += billingservice_date + "' )";
		_logger.info("addCodeByStr(sql = " + sql + ")");
		ret = dbObj.saveBillingRecord(sql);
		if (ret == 0) {
			_logger.error("addCodeByStr(sql = " + sql + ")");
		}
		return ret;
	}

	public boolean deletePrivateCode(String serviceCode) {
		boolean ret = false;
		String sql = "delete from billingservice where service_code='" + serviceCode + "'";
		_logger.info("deletePrivateCode(sql = " + sql + ")");
		ret = dbObj.updateDBRecord(sql);
		if (!ret) {
			_logger.error("deletePrivateCode(sql = " + sql + ")");
		}
		return ret;
	}

	public List getPrivateBillingCodeDesc() {
		List ret = new Vector();
		String sql = "select * from billingservice where service_code like '\\_%' order by service_code";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret.add(rs.getString("service_code"));
				ret.add(rs.getString("description"));
			}
		} catch (SQLException e) {
			_logger.error("getPrivateBillingCodeDesc(sql = " + sql + ")");
		}
		return ret;
	}

}
