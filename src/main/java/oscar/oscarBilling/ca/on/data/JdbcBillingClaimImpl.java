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
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;

public class JdbcBillingClaimImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingClaimImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public int addOneBatchHeaderRecord(BillingBatchHeaderData val) {
		int retval = 0;
		String sql = "insert into billing_on_header values(\\N, " + "'" + val.disk_id + "'," + "'" + val.transc_id
				+ "'," + "'" + val.rec_id + "'," + "'" + val.spec_id + "'," + "'" + val.moh_office + "'," + "'"
				+ val.batch_id + "'," + "'" + val.operator + "'," + "'" + val.group_num + "'," + "'"
				+ val.provider_reg_num + "'," + "'" + val.specialty + "'," + "'" + val.h_count + "'," + "'"
				+ val.r_count + "'," + "'" + val.t_count + "'," + "'" + val.batch_date + "'," + "'"
				+ val.createdatetime + "'," + "'" + val.updatedatetime + "'," + "'" + val.creator + "'," + "'"
				+ val.action + "'," + "'" + val.comment + "')";
		_logger.info("addOneBatchHeaderRecord(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval == 0) {
			_logger.error("addOneBatchHeaderRecord(sql = " + sql + ")");
		}
		return retval;
	}

	public int addOneClaimHeaderRecord(BillingClaimHeader1Data val) {
		int retval = 0;
		String sql = "insert into billing_on_cheader1 values(\\N, 0," + "'" + val.transc_id + "'," + "'" + val.rec_id
				+ "'," + "'" + val.hin
				+ "',"
				+ "'"
				+ val.ver
				+ "',"
				+ "'"
				+ val.dob
				+ "',"
				+
				// "'" + val.acc_num + "'," +
				"'" + val.pay_program + "'," + "'" + val.payee + "'," + "'" + val.ref_num + "'," + "'"
				+ val.facilty_num + "'," + "'" + val.admission_date + "'," + "'" + val.ref_lab_num + "'," + "'"
				+ val.man_review + "'," + "'" + val.location + "'," + "'" + val.demographic_no + "'," + "'"
				+ val.provider_no + "'," + "'" + val.appointment_no + "'," + "'"
				+ StringEscapeUtils.escapeSql(val.demographic_name) + "'," + "'" + val.sex + "'," + "'" + val.province
				+ "'," + "'" + val.billing_date + "'," + "'" + val.billing_time + "'," + "'" + val.total + "'," + "'"
				+ val.paid + "'," + "'" + val.status + "'," + "'" + StringEscapeUtils.escapeSql(val.comment) + "',"
				+ "'" + val.visittype + "'," + "'" + val.provider_ohip_no + "'," + "'" + val.provider_rma_no + "',"
				+ "'" + val.apptProvider_no + "'," + "'" + val.asstProvider_no + "'," + "'" + val.creator + "', \\N, "
				+ (val.clinic==null?"null":"'"+val.clinic+"'")+" )";

		_logger.info("addOneClaimHeaderRecord(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval > 0) {
			// add claim header 2 record, if needed
			if ("RMB".equals(val.pay_program)) {
				sql = "insert into billing_on_cheader2 values(\\N, " + retval + " ," + "'" + val.transc_id + "',"
						+ "'R'," + "'" + val.hin + "'," + "'" + val.last_name + "'," + "'" + val.first_name + "',"
						+ "'" + val.sex + "'," + "'" + val.province + "', \\N )";
				_logger.info("addOneClaimHeaderRecord2(sql = " + sql + ")");
				dbObj.saveBillingRecord(sql);
			}
		} else {
			_logger.error("addOneClaimHeaderRecord(sql = " + sql + ")");
			retval = 0;
		}

		return retval;
	}

	public boolean addItemRecord(List lVal, int id) {
		boolean retval = true;
		for (int i = 0; i < lVal.size(); i++) {
			BillingItemData val = (BillingItemData) lVal.get(i);
			String sql = "insert into billing_on_item values(\\N, " + id + ", '" + val.transc_id + "', '" + val.rec_id
					+ "', '" + val.service_code + "', '" + val.fee + "', '" + val.ser_num + "', '" + val.service_date
					+ "', '" + val.dx + "', '" + val.dx1 + "', '" + val.dx2 + "', '" + val.status + "', \\N )";
			retval = dbObj.updateDBRecord(sql);
			if (!retval) {
				_logger.error("addItemRecord(sql = " + sql + ")");
				return retval;
			}
		}
		return retval;
	}

	public boolean add3rdBillExt(Map<String,String>mVal, int id) {
		boolean retval = true;
		String[] temp = { "billTo", "remitTo", "total", "payment", "refund", "provider_no", "gst", "payDate", "payMethod"};
		String demoNo = mVal.get("demographic_no");
		String dateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
                mVal.put("payDate", dateTime);
		for (int i = 0; i < temp.length; i++) {
			String sql = "insert into billing_on_ext values(\\N, " + id + "," + demoNo + ", '" + temp[i] + "', '"
					+ StringEscapeUtils.escapeSql(mVal.get(temp[i])) + "', '" + dateTime + "', '1' )";
			retval = dbObj.updateDBRecord(sql);
			if (!retval) {
				_logger.error("add3rdBillExt(sql = " + sql + ")");
				return retval;
			}
		}
		return retval;
	}

	public int addOneItemRecord(BillingItemData val) {
		int retval = 0;
		String sql = "insert into billing_on_item values(\\N, " + val.ch1_id + ", '" + val.transc_id + "', '"
				+ val.rec_id + "', '" + val.service_code + "', '" + val.fee + "', '" + val.ser_num + "', '"
				+ val.service_date + "', '" + val.dx + "', '" + val.dx1 + "', '" + val.dx2 + "', '" + val.status
				+ "', \\N )";
		retval = dbObj.saveBillingRecord(sql);
		if (retval == 0) {
			_logger.error("addOneItemRecord(sql = " + sql + ")");
		}
		return retval;
	}

	// add disk file
	public int addBillingDiskName(BillingDiskNameData val) {
		int retval = 0;
		String sql = "insert into billing_on_diskname values(\\N, " + "'" + val.monthCode + "'," + " " + val.batchcount
				+ " ," + "'" + val.ohipfilename + "'," + "'" + val.groupno + "'," + "'" + val.creator + "'," + "'"
				+ val.claimrecord + "'," + "'" + val.createdatetime + "'," + "'" + val.status + "'," + "'" + val.total
				+ "', \\N )";
		_logger.info("addBillingDiskName(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				sql = "insert into billing_on_filename values(\\N, " + retval + " ," + "'" + val.htmlfilename.get(i)
						+ "'," + "'" + val.providerohipno.get(i) + "'," + "'" + val.providerno.get(i) + "'," + "'"
						+ val.vecClaimrecord.get(0) + "'," + "'" + val.vecStatus.get(0) + "'," + "'"
						+ val.vecTotal.get(0) + "', \\N )";
				_logger.info("addBillingDiskName2(sql = " + sql + ")");
				dbObj.saveBillingRecord(sql);
			}

		} else {
			_logger.error("addBillingDiskName(sql = " + sql + ")");
			retval = 0;
		}

		return retval;
	}

	/*
	 * // add disk file public List addBillingDiskName(BillingDiskNameData val) {
	 * List ret = new Vector(); int retval = 0; Vector ohipName =
	 * val.getOhipfilename();
	 * 
	 * for (int j = 0; j < ohipName.size(); j++) { String sql = "insert into
	 * billing_on_diskname values(\\N, 0," + "'" + val.monthCode + "'," + " " +
	 * val.batchcount + " ," + "'" + val.ohipfilename.get(j) + "'," + "'" +
	 * val.groupno + "'," + "'" + val.creator + "'," + "'" + val.claimrecord +
	 * "'," + "'" + val.createdatetime + "'," + "'" + val.status + "'," + "'" +
	 * val.total + "', \\N )"; _logger.info("addBillingDiskName(sql = " + sql +
	 * ")"); retval = dbObj.saveBillingRecord(sql);
	 * 
	 * if (retval > 0) { // add filenames, if needed for (int i = 0; i <
	 * val.providerohipno.size(); i++) { sql = "insert into billing_on_filename
	 * values(\\N, " + retval + " ," + "'" + val.htmlfilename.get(i) + "'," +
	 * "'" + val.providerohipno.get(i) + "'," + "'" + val.vecClaimrecord.get(i) +
	 * "'," + "'" + val.vecStatus.get(i) + "'," + "'" + val.vecTotal.get(i) +
	 * "', \\N )"; _logger.info("addOneClaimHeaderRecord2(sql = " + sql + ")");
	 * dbObj.saveBillingRecord(sql); }
	 * 
	 * ret.add("" + retval); } else { _logger.error("addBillingDiskName(sql = " +
	 * sql + ")"); retval = 0; } }
	 * 
	 * return ret; }
	 */
	// get monthCode, batchcount
	public String[] getLatestSoloMonthCodeBatchNum(String providerNo) {
		String[] retval = null;
		String sql = "select monthCode, batchcount from billing_on_diskname d, billing_on_filename f where f.providerohipno='"
				+ providerNo + "' and d.groupNo='' and d.id=f.disk_id order by d.id desc limit 1";
		// _logger.info("getLatestSoloMonthCodeBatchNum(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			if (rs.next()) {
				retval = new String[2];
				retval[0] = rs.getString("monthCode");
				retval[1] = "" + rs.getInt("batchcount");
			}
		} catch (SQLException e) {
			_logger.error("getLatestSoloMonthCodeBatchNum(sql = " + sql + ")");
			retval = null;
		}

		return retval;
	}

	public String[] getLatestGrpMonthCodeBatchNum(String groupNo) {
		String[] retval = null;
		String sql = "select monthCode, batchcount from billing_on_diskname where groupNo='" + groupNo
				+ "' order by createdatetime desc limit 1";
		// _logger.info("getLatestGrpMonthCodeBatchNum(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			if (rs.next()) {
				retval = new String[2];
				retval[0] = rs.getString("monthCode");
				retval[1] = "" + rs.getInt("batchcount");
			}
		} catch (SQLException e) {
			_logger.error("getLatestGrpMonthCodeBatchNum(sql = " + sql + ")");
			retval = null;
		}

		return retval;
	}

	public String getPrevDiskCreateDate(String diskId) {
		String retval = null;
		String curDate = "";
		String groupNo = "";
		String sql = "select createdatetime, groupno from billing_on_diskname where id=" + diskId;
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			if (rs.next()) {
				curDate = rs.getString("createdatetime");
				groupNo = rs.getString("groupno");

				sql = "select createdatetime from billing_on_diskname where createdatetime<'" + curDate
						+ "' and groupno='" + groupNo + "' order by createdatetime desc limit 1";
				rs = dbObj.searchDBRecord(sql);
				if (rs.next()) {
					retval = rs.getString("createdatetime");
					retval = retval.substring(0, 10);
				}
			}
		} catch (SQLException e) {
			_logger.error("getPrevDiskCreateDate(sql = " + sql + ")");
			retval = null;
		}

		return retval;
	}

	public String getDiskCreateDate(String diskId) {
		String retval = null;
		String sql = "select createdatetime from billing_on_diskname where id=" + diskId;
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			if (rs.next()) {
				retval = rs.getString("createdatetime");
				retval = retval.substring(0, 10);
			}
		} catch (SQLException e) {
			_logger.error("getDiskCreateDate(sql = " + sql + ")");
			retval = null;
		}

		return retval;
	}

	public List getMRIList(String sDate, String eDate, String status) {
		List retval = new Vector();
		BillingDiskNameData obj = null;
		String sql = "select * from billing_on_diskname where createdatetime>='" + sDate + "' and createdatetime<='"
				+ eDate + "' and status in (" + status + ") order by createdatetime desc ";
		// _logger.info("getMRIList(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj = new BillingDiskNameData();
				obj.setId("" + rs.getInt("id"));
				obj.setMonthCode(rs.getString("monthCode"));
				obj.setBatchcount("" + rs.getInt("batchcount"));
				obj.setOhipfilename(rs.getString("ohipfilename"));
				obj.setGroupno(rs.getString("groupno"));
				obj.setClaimrecord(rs.getString("claimrecord"));
				obj.setCreatedatetime(rs.getString("createdatetime"));
				obj.setUpdatedatetime(rs.getString("timestamp"));
				obj.setStatus(rs.getString("status"));
				obj.setTotal(rs.getString("total"));

				sql = "select * from billing_on_filename where disk_id =" + rs.getInt("id") + " and status in ("
						+ status + ") order by id desc ";
				// _logger.info("getMRIList(sql = " + sql + ")");
				ResultSet rs1 = dbObj.searchDBRecord(sql);
				Vector vecHtmlfilename = new Vector();
				Vector vecProviderohipno = new Vector();
				Vector vecProviderno = new Vector();
				Vector vecClaimrecord = new Vector();
				Vector vecStatus = new Vector();
				Vector vecTotal = new Vector();
				Vector vecFilenameId = new Vector();
				while (rs1.next()) {
					vecFilenameId.add("" + rs1.getInt("id"));
					vecHtmlfilename.add(rs1.getString("htmlfilename"));
					vecProviderohipno.add(rs1.getString("providerohipno"));
					vecProviderno.add(rs1.getString("providerno"));
					vecClaimrecord.add(rs1.getString("claimrecord"));
					vecStatus.add(rs1.getString("status"));
					vecTotal.add(rs1.getString("total"));
				}

				obj.setVecFilenameId(vecFilenameId);
				obj.setHtmlfilename(vecHtmlfilename);
				obj.setProviderohipno(vecProviderohipno);
				obj.setProviderno(vecProviderno);
				obj.setVecClaimrecord(vecClaimrecord);
				obj.setVecStatus(vecStatus);
				obj.setVecTotal(vecTotal);
				retval.add(obj);
			}
		} catch (SQLException e) {
			_logger.error("getMRIList(sql = " + sql + ")");
			retval = null;
		}

		return retval;
	}

	public String getOhipfilename(int diskId) {
		String obj = "";
		String sql = "select ohipfilename from billing_on_diskname where id=" + diskId;
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj = rs.getString("ohipfilename");
			}
		} catch (SQLException e) {
			_logger.error("getOhipfilename(sql = " + sql + ")");
			obj = null;
		}

		return obj;
	}

	public String getHtmlfilename(int diskId, String providerNo) {
		String obj = "";
		String sql = "select htmlfilename from billing_on_filename where disk_id=" + diskId + " and providerno='"
				+ providerNo + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj = rs.getString("htmlfilename");
			}
		} catch (SQLException e) {
			_logger.error("getHtmlfilename(sql = " + sql + ")");
			obj = null;
		}

		return obj;
	}

	public BillingDiskNameData getDisknameObj(String diskId) {
		BillingDiskNameData obj = new BillingDiskNameData();
		String sql = "select * from billing_on_diskname where id=" + diskId;
		// _logger.info("BillingDiskNameData(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj.setId("" + rs.getInt("id"));
				obj.setMonthCode(rs.getString("monthCode"));
				obj.setBatchcount("" + rs.getInt("batchcount"));
				obj.setOhipfilename(rs.getString("ohipfilename"));
				obj.setGroupno(rs.getString("groupno"));
				obj.setClaimrecord(rs.getString("creator"));
				obj.setClaimrecord(rs.getString("claimrecord"));
				obj.setCreatedatetime(rs.getString("createdatetime"));
				obj.setStatus(rs.getString("status"));
				obj.setTotal(rs.getString("total"));
				obj.setUpdatedatetime(rs.getString("timestamp"));

				sql = "select * from billing_on_filename where disk_id =" + rs.getInt("id")
						+ " and status !='D' order by id desc ";
				// _logger.info("getMRIList(sql = " + sql + ")");
				ResultSet rs1 = dbObj.searchDBRecord(sql);
				Vector vecHtmlfilename = new Vector();
				Vector vecProviderohipno = new Vector();
				Vector vecProviderno = new Vector();
				Vector vecClaimrecord = new Vector();
				Vector vecStatus = new Vector();
				Vector vecTotal = new Vector();
				Vector vecFilenameId = new Vector();
				while (rs1.next()) {
					vecFilenameId.add("" + rs1.getInt("id"));
					vecHtmlfilename.add(rs1.getString("htmlfilename"));
					vecProviderohipno.add(rs1.getString("providerohipno"));
					vecProviderno.add(rs1.getString("providerno"));
					vecClaimrecord.add(rs1.getString("claimrecord"));
					vecStatus.add(rs1.getString("status"));
					vecTotal.add(rs1.getString("total"));
				}

				obj.setVecFilenameId(vecFilenameId);
				obj.setHtmlfilename(vecHtmlfilename);
				obj.setProviderohipno(vecProviderohipno);
				obj.setProviderno(vecProviderno);
				obj.setVecClaimrecord(vecClaimrecord);
				obj.setVecStatus(vecStatus);
				obj.setVecTotal(vecTotal);
			}
		} catch (SQLException e) {
			_logger.error("BillingDiskNameData(sql = " + sql + ")");
			obj = null;
		}

		return obj;
	}

	public int addRepoDiskName(BillingDiskNameData val) {
		int retval = 0;
		String sql = "insert into billing_on_repo values(\\N, " + " " + val.id + " ," + "'billing_on_diskname'," + "'"
				+ val.monthCode + "|" + val.batchcount + "|" + val.ohipfilename + "|" + val.groupno + "|" + val.creator
				+ "|" + val.claimrecord + "|" + val.createdatetime + "|" + val.status + "|" + val.total + "|"
				+ val.updatedatetime + "', '" + val.updatedatetime + "')";
		_logger.info("addRepoDiskName(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				sql = "insert into billing_on_repo values(\\N, " + val.vecFilenameId.get(i) + " ,"
						+ "'billing_on_filename'," + "'" + val.id + "|" + val.htmlfilename.get(i) + "|"
						+ val.providerohipno.get(i) + "|" + val.providerno.get(i) + "|" + val.vecClaimrecord.get(0)
						+ "|" + val.vecStatus.get(0) + "|" + val.vecTotal.get(0) + "|" + val.updatedatetime + "', '"
						+ val.updatedatetime + "')";
				_logger.info("addRepoDiskName2(sql = " + sql + ")");
				dbObj.saveBillingRecord(sql);
			}
		} else {
			_logger.error("addRepoDiskName(sql = " + sql + ")");
			retval = 0;
		}
		return retval;
	}

	public boolean updateDiskName(BillingDiskNameData val) {
		boolean retval = false;
		String sql = "update billing_on_diskname set creator='" + val.creator + "' where id=" + val.getId();
		_logger.info("updateDiskName(sql = " + sql + ")");
		retval = dbObj.updateDBRecord(sql);

		if (retval) {
			// for (int i = 0; i < val.providerohipno.size(); i++) {
			// sql = "update billing_on_filename creator='" + val.creator + "'
			// where id=" + val.vecFilenameId.get(i);
			// _logger.info("updateDiskName2(sql = " + sql + ")");
			// dbObj.updateDBRecord(sql);
			// }
		} else {
			_logger.error("updateDiskName(sql = " + sql + ")");
			retval = false;
		}
		return retval;
	}

	public BillingBatchHeaderData getBatchHeaderObj(BillingProviderData providerData, String disk_id) {
		BillingBatchHeaderData obj = new BillingBatchHeaderData();
		String sql = "select * from billing_on_header where disk_id='" + disk_id + "' and provider_reg_num='"
				+ providerData.getOhipNo() + "'";
		// _logger.info("getBatchHeaderObj(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				obj.setId("" + rs.getInt("id"));
				obj.setDisk_id(disk_id);
				obj.setTransc_id(rs.getString("transc_id"));
				obj.setRec_id(rs.getString("rec_id"));
				obj.setSpec_id(rs.getString("spec_id"));
				obj.setMoh_office(rs.getString("moh_office"));

				obj.setBatch_id(rs.getString("batch_id"));
				obj.setOperator(rs.getString("operator"));
				obj.setGroup_num(rs.getString("group_num"));
				obj.setProvider_reg_num(rs.getString("provider_reg_num"));
				obj.setSpecialty(rs.getString("specialty"));
				obj.setH_count(rs.getString("h_count"));
				obj.setR_count(rs.getString("r_count"));
				obj.setT_count(rs.getString("t_count"));
				obj.setBatch_date(rs.getString("batch_date"));

				obj.setCreatedatetime(rs.getString("createdatetime"));
				obj.setUpdatedatetime(rs.getString("updatedatetime"));
				obj.setCreator(rs.getString("creator"));
				obj.setAction(rs.getString("action"));
				obj.setComment(rs.getString("comment"));
			}
		} catch (SQLException e) {
			_logger.error("getBatchHeaderObj(sql = " + sql + ")");
			obj = null;
		}
		return obj;
	}

	public int addRepoBatchHeader(BillingBatchHeaderData val) {
		int retval = 0;
		String sql = "insert into billing_on_repo values(\\N, " + " " + val.id + " ," + "'billing_on_header'," + "'"
				+ val.disk_id + "|" + val.transc_id + "|" + val.rec_id + "|" + val.spec_id + "|" + val.moh_office + "|"
				+ val.batch_id + "|" + val.operator + "|" + val.group_num + "|" + val.provider_reg_num + "|"
				+ val.specialty + "|" + val.h_count + "|" + val.r_count + "|" + val.t_count + "|" + val.batch_date
				+ "|" + val.createdatetime + "|" + val.updatedatetime + "|" + val.creator + "|" + val.action + "|"
				+ val.comment + "', '" + val.updatedatetime + "')";
		_logger.info("addRepoBatchHeader(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval > 0) {
		} else {
			_logger.error("addRepoBatchHeader(sql = " + sql + ")");
			retval = 0;
		}
		return retval;
	}

	// TODO more update data
	public boolean updateBatchHeaderRecord(BillingBatchHeaderData val) {
		boolean retval = false;

		String sql = "update billing_on_header set moh_office='" + val.moh_office + "', batch_id='" + val.batch_id
				+ "', specialty='" + val.specialty + "', creator='" + val.creator + "', updatedatetime='"
				+ val.getUpdatedatetime() + "', action='" + val.getAction() + "', comment='" + val.getComment()
				+ "' where id=" + val.getId();
		_logger.info("updateBatchHeaderRecord(sql = " + sql + ")");
		retval = dbObj.updateDBRecord(sql);
		if (retval) {
		} else {
			_logger.error("updateBatchHeaderRecord(sql = " + sql + ")");
			retval = false;
		}
		return retval;
	}
        

}
