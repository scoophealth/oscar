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
import org.oscarehr.common.dao.BillingONEAReportDao;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class JdbcBillingErrorRepImpl {
	private static final Logger _logger = MiscUtils.getLogger();
	private BillingONEAReportDao billingONEARReportDao = (BillingONEAReportDao)SpringUtils.getBean(BillingONEAReportDao.class);
	
	
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
		if(list == null) return retval;
		

		BillingErrorRepData obj = null;
		
		String sql = "select * from billing_on_eareport";
		for(int i = 0; i < list.size() ; i++) {
			BillingProviderData val = list.get(i);
			
			if(i == 0) 
				sql += " where ((providerohip_no='" + val.getOhipNo() + "' and group_no='" + val.getBillingGroupNo() + "' and specialty='" + val.getSpecialtyCode() + "')";
			else 
				sql+= " or (providerohip_no='" + val.getOhipNo() + "' and group_no='" + val.getBillingGroupNo() + "' and specialty='" + val.getSpecialtyCode() + "')";
		}
		
		String sqlFilename = "".equals(filename) ? "" : (" and report_name='" + filename + "' ");
		sql += ")";
		sql += "and code_date>='" + fromDate + "' and code_date<='" + toDate + "'" + sqlFilename + " order by code_date";

		
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
		List<BillingONEAReport>  bs = billingONEARReportDao.findByProviderOhipNoAndGroupNoAndSpecialtyAndProcessDate(val.getProviderohip_no(),val.getGroup_no(),val.getSpecialty(),ConversionUtils.fromDateString(val.getProcess_date()));
		for(BillingONEAReport b:bs) {
			billingONEARReportDao.remove(b.getId());
		}
		return true;
	}

	public int addErrorReportRecord(BillingErrorRepData val) {
		BillingONEAReport b = new BillingONEAReport();
		b.setProviderOHIPNo(val.providerohip_no);
		b.setGroupNo(val.group_no);
		b.setSpecialty(val.specialty);
		b.setProcessDate(ConversionUtils.fromDateString(val.process_date));
		b.setHin(val.hin);
		b.setVersion(val.ver);
		b.setDob(ConversionUtils.fromDateString(val.dob));
		b.setBillingNo(Integer.parseInt(val.billing_no));
		b.setRefNo(val.ref_no);
		b.setFacility(val.facility);
		b.setAdmittedDate(ConversionUtils.fromDateString(val.admitted_date));
		b.setClaimError(val.claim_error);
		b.setCode(val.code);
		b.setFee(val.fee);
		b.setUnit(val.unit);
		b.setCodeDate(ConversionUtils.fromDateString(val.code_date));
		b.setDx(val.dx);
		b.setExp(val.exp);
		b.setCodeError(val.code_error);
		b.setReportName(val.report_name);
		b.setStatus(val.status.toCharArray()[0]);
		b.setComment(val.comment);
	
		billingONEARReportDao.persist(b);
		
		return b.getId();
		
	}
	
	public boolean updateErrorReportStatus(String id, String val) {
		BillingONEAReport b = billingONEARReportDao.find(Integer.valueOf(id));
		if(b != null) {
			b.setStatus(val.toCharArray()[0]);
			billingONEARReportDao.merge(b);
		}
		return true;
	}


}
