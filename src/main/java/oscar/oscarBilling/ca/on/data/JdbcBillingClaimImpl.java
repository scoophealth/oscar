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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.ON.dao.BillingONHeaderDao;
import org.oscarehr.billing.CA.ON.model.BillingONHeader;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class JdbcBillingClaimImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingClaimImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();
	
	private BillingONHeaderDao dao = SpringUtils.getBean(BillingONHeaderDao.class);
	private BillingONCHeader1Dao cheaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);

	SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

	public int addOneBatchHeaderRecord(BillingBatchHeaderData val) {
		BillingONHeader b = new BillingONHeader();
		b.setDiskId(Integer.parseInt(val.disk_id));
		b.setTransactionId(val.transc_id);
		b.setRecordId(val.rec_id);
		b.setSpecId(val.spec_id);
		b.setMohOffice(val.moh_office);
		b.setBatchId(val.batch_id);
		b.setOperator(val.operator);
		b.setGroupNum(val.group_num);
		b.setProviderRegNum(val.provider_reg_num);
		b.setSpecialty(val.specialty);
		b.sethCount(val.h_count);
		b.setrCount(val.r_count);
		b.settCount(val.t_count);
		b.setBatchDate(new Date());
		b.setCreateDateTime(new Date());
		b.setUpdateDateTime(new Date());
		b.setCreator(val.creator);
		b.setAction(val.action);
		b.setComment(val.comment);
		
		dao.persist(b);
		
		return b.getId();
	}

	public int addOneClaimHeaderRecord(BillingClaimHeader1Data val) {
		BillingONCHeader1 b = new BillingONCHeader1();
		b.setHeaderId(0);
		b.setTranscId(val.transc_id);
		b.setRecId(val.rec_id);
		b.setHin(val.hin);
		b.setVer(val.ver);
		b.setDob(val.dob);
		b.setPayProgram(val.pay_program);
		b.setPayee(val.payee);
		b.setRefNum(val.ref_num);
		b.setFaciltyNum(val.facilty_num);
		if(val.admission_date.length()>0)
			try{
				b.setAdmissionDate(dateformatter.parse(val.admission_date));
			}catch(ParseException e){/*empty*/}
		
		b.setRefLabNum(val.ref_lab_num);
		b.setManReview(val.man_review);
		b.setLocation(val.location);
		b.setDemographicNo(Integer.parseInt(val.demographic_no));
		b.setProviderNo(val.provider_no);
		b.setAppointmentNo(Integer.parseInt(val.appointment_no));
		b.setDemographicName(val.demographic_name);
		b.setSex(val.sex);
		b.setProvince(val.province);
		if(val.billing_date.length()>0)
			try {
				b.setBillingDate(dateformatter.parse(val.billing_date));
			}catch(ParseException e){/*empty*/}
		if(val.billing_time.length()>0)
			try {
				b.setBillingTime(timeFormatter.parse(val.billing_time));
			}catch(ParseException e){/*empty*/}

		b.setTotal(Long.parseLong(val.total));
		b.setPaid(Long.parseLong(val.paid));
		b.setStatus(val.status);
		b.setComment(val.comment);
		b.setVisitType(val.visittype);
		b.setProviderOhipNo(val.provider_ohip_no);
		b.setProviderRmaNo(val.provider_rma_no);
		b.setApptProviderNo(val.apptProvider_no);
		b.setAsstProviderNo(val.asstProvider_no);
		b.setCreator(val.creator);
		b.setClinic(val.clinic);
		
		cheaderDao.persist(b);
		
		return b.getId();
	}

	public boolean addItemRecord(List lVal, int id) {
	
		boolean retval = true;
		for (int i = 0; i < lVal.size(); i++) {
			BillingItemData val = (BillingItemData) lVal.get(i);
			
			BillingONItem b = new BillingONItem();
			b.setCh1Id(id);
			b.setTranscId(val.transc_id);
			b.setRecId(val.rec_id);
			b.setServiceCode(val.service_code);
			b.setFee(val.fee);
			b.setServiceCount(val.ser_num);
			if(val.service_date.length()>0)
				try {
					b.setServiceDate(dateformatter.parse(val.service_date));
				}catch(ParseException e){/*empty*/}
			b.setDx(val.dx);
			b.setDx1(val.dx1);
			b.setDx2(val.dx2);
			b.setStatus(val.status);
			
			itemDao.persist(b);
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
					+ mVal.get(temp[i]) + "', '" + dateTime + "', '1', null )";
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

	public String[] getLatestSoloMonthCodeBatchNum(String providerNo) {
		String[] retval = null;
		String sql = "select monthCode, batchcount from billing_on_diskname d, billing_on_filename f where f.providerohipno='"
				+ providerNo + "' and d.id=f.disk_id order by d.id desc limit 1";
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
