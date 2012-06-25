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
import java.util.Vector;

import org.apache.log4j.Logger;

public class JdbcBillingErrorRepImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingErrorRepImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();
	JdbcBillingLog dbLog = new JdbcBillingLog();

	public List getErrorRecords(BillingProviderData val, String fromDate, String toDate, String filename) {
		List retval = new Vector();
		BillingErrorRepData obj = null;
		String sqlFilename = "".equals(filename) ? "" : (" and report_name='" + filename + "' ");
		String sql = "select * from billing_on_eareport where providerohip_no='" + val.getOhipNo() + "' and group_no='"
				+ val.getBillingGroupNo() + "' and specialty='" + val.getSpecialtyCode() + "' and code_date>='"
				+ fromDate + "' and code_date<='" + toDate + "'" + sqlFilename + " order by code_date";

		// _logger.info("getErrorRecords(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj = new BillingErrorRepData();
				obj.setId("" + rs.getInt("id"));
				obj.setBilling_no("" + rs.getInt("billing_no"));
				obj.setProviderohip_no(rs.getString("providerohip_no"));
				obj.setGroup_no(rs.getString("group_no"));
				obj.setSpecialty(rs.getString("specialty"));
				obj.setProcess_date(rs.getString("process_date"));
				obj.setHin(rs.getString("hin"));
				obj.setVer(rs.getString("ver"));
				obj.setDob(rs.getString("dob"));
				obj.setRef_no(rs.getString("ref_no"));
				obj.setFacility(rs.getString("facility"));
				obj.setAdmitted_date(rs.getString("admitted_date"));
				obj.setClaim_error(rs.getString("claim_error"));
				obj.setCode(rs.getString("code"));
				obj.setFee(rs.getString("fee"));
				obj.setUnit(rs.getString("unit"));
				obj.setCode_date(rs.getString("code_date"));
				obj.setDx(rs.getString("dx"));
				obj.setExp(rs.getString("exp"));
				obj.setCode_error(rs.getString("code_error"));
				obj.setReport_name(rs.getString("report_name"));
				obj.setStatus(rs.getString("status"));
				obj.setComment(rs.getString("comment"));
				retval.add(obj);
			}
		} catch (SQLException e) {
			_logger.error("getErrorRecords(sql = " + sql + ")");
		}

		return retval;
	}

        public List getErrorRecords(List<BillingProviderData> list, String fromDate, String toDate, String filename) {
		List retval = new Vector();
		BillingErrorRepData obj = null;
		String sqlFilename = "".equals(filename) ? "" : (" and report_name='" + filename + "' ");
		if(list == null) return retval;
		String sql = "select * from billing_on_eareport";
		for(int i = 0; i < list.size() ; i++) {
			BillingProviderData val = list.get(i);
			if(i == 0) sql += " where ((providerohip_no='" + val.getOhipNo() + "' and group_no='"
				+ val.getBillingGroupNo() + "' and specialty='" + val.getSpecialtyCode() + "')";
			else sql+= " or (providerohip_no='" + val.getOhipNo() + "' and group_no='"
				+ val.getBillingGroupNo() + "' and specialty='" + val.getSpecialtyCode() + "')";
		}
		sql += ") and code_date>='" + fromDate + "' and code_date<='" + toDate + "'" + sqlFilename + " order by code_date";

		// _logger.info("getErrorRecords(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj = new BillingErrorRepData();
				obj.setId("" + rs.getInt("id"));
				obj.setBilling_no("" + rs.getInt("billing_no"));
				obj.setProviderohip_no(rs.getString("providerohip_no"));
				obj.setGroup_no(rs.getString("group_no"));
				obj.setSpecialty(rs.getString("specialty"));
				obj.setProcess_date(rs.getString("process_date"));
				obj.setHin(rs.getString("hin"));
				obj.setVer(rs.getString("ver"));
				obj.setDob(rs.getString("dob"));
				obj.setRef_no(rs.getString("ref_no"));
				obj.setFacility(rs.getString("facility"));
				obj.setAdmitted_date(rs.getString("admitted_date"));
				obj.setClaim_error(rs.getString("claim_error"));
				obj.setCode(rs.getString("code"));
				obj.setFee(rs.getString("fee"));
				obj.setUnit(rs.getString("unit"));
				obj.setCode_date(rs.getString("code_date"));
				obj.setDx(rs.getString("dx"));
				obj.setExp(rs.getString("exp"));
				obj.setCode_error(rs.getString("code_error"));
				obj.setReport_name(rs.getString("report_name"));
				obj.setStatus(rs.getString("status"));
				obj.setComment(rs.getString("comment"));
				retval.add(obj);
			}
		} catch (SQLException e) {
			_logger.error("getErrorRecords(sql = " + sql + ")");
		}

		return retval;
	}

	public boolean deleteErrorReport(BillingErrorRepData val) {
		boolean retval = false;
		String sql = "delete from billing_on_eareport where providerohip_no='" + val.getProviderohip_no()
				+ "' and group_no='" + val.getGroup_no() + "' and specialty='" + val.getSpecialty()
				+ "' and process_date='" + val.getProcess_date() + "'";
		_logger.info("deleteErrorReport(sql = " + sql + ")");
		retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("deleteErrorReport(sql = " + sql + ")");
		}
		return retval;
	}

	public int addErrorReportRecord(BillingErrorRepData val) {
		int retval = 0;
		String sql = "insert into billing_on_eareport values(\\N, " + "'" + val.providerohip_no + "','" + val.group_no
				+ "','" + val.specialty + "','" + val.process_date + "','" + val.hin + "','" + val.ver + "','"
				+ val.dob + "', " + val.billing_no + " ,'" + val.ref_no + "','" + val.facility + "','"
				+ val.admitted_date + "','" + val.claim_error + "','" + val.code + "','" + val.fee + "','" + val.unit
				+ "','" + val.code_date + "','" + val.dx + "','" + val.exp 
				+ "','" + val.code_error + "','" + val.report_name + "','" + val.status 
				+ "','" + val.comment + "')";
		_logger.info("addErrorReportRecord(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval > 0) {
		} else {
			_logger.error("addErrorReportRecord(sql = " + sql + ")");
			retval = 0;
		}
		return retval;
	}
	
	public boolean updateErrorReportStatus(String id, String val) {
		boolean retval = false;
		String sql = "update billing_on_eareport set status='" + val + "' where id=" + id;
		_logger.info("updateErrorReportStatus(sql = " + sql + ")");
		retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("updateErrorReportStatus(sql = " + sql + ")");
		}
		return retval;
	}
	
	public boolean deleteOneErrorReport(String id) {
		boolean retval = false;
		String sql = "delete from billing_on_eareport where id=" + id;
		_logger.info("deleteOneErrorReport(sql = " + sql + ")");
		retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("deleteOneErrorReport(sql = " + sql + ")");
		}
		return retval;
	}


}
