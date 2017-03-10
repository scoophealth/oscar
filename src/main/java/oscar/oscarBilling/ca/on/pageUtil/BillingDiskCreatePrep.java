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

package oscar.oscarBilling.ca.on.pageUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import oscar.oscarBilling.ca.on.data.BillingBatchHeaderData;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.oscarBilling.ca.on.data.BillingDiskNameData;
import oscar.oscarBilling.ca.on.data.BillingProviderData;
import oscar.oscarBilling.ca.on.data.JdbcBillingClaimImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingPageUtil;
import oscar.util.UtilDateUtilities;

public class BillingDiskCreatePrep {
	private static final Logger _logger = Logger.getLogger(BillingDiskCreatePrep.class);
	JdbcBillingClaimImpl dbObj = new JdbcBillingClaimImpl();
	Properties propProOHIP = null;

	public BillingDiskCreatePrep() {
		propProOHIP = getPropProviderOHIP();
	}

	public static Properties getPropProviderOHIP() {
		Properties ret = new Properties();
		JdbcBillingPageUtil dbObj = new JdbcBillingPageUtil();
		ret = dbObj.getPropProviderOHIP();
		return ret;
	}

	public List getCurSoloProvider() {
		JdbcBillingPageUtil dbObj = new JdbcBillingPageUtil();
		List ret = dbObj.getCurSoloProvider();
		return ret;
	}

	public List getCurGrpProvider() {
		JdbcBillingPageUtil dbObj = new JdbcBillingPageUtil();
		List ret = dbObj.getCurGrpProvider();
		return ret;
	}

	public List getProvider(String diskId) {
		JdbcBillingPageUtil dbObj = new JdbcBillingPageUtil();
		List ret = dbObj.getProvider(diskId);
		return ret;
	}

	public BillingProviderData getProviderObj(String providerNo) {
		JdbcBillingPageUtil dbObj = new JdbcBillingPageUtil();
		BillingProviderData ret = dbObj.getProviderObj(providerNo);
		return ret;
	}

	public String getOhipfilename(int diskId) {
		String ret = null;
		ret = dbObj.getOhipfilename(diskId);
		return ret;
	}

	public String getHtmlfilename(int diskId, String providerNo) {
		String ret = null;
		ret = dbObj.getHtmlfilename(diskId, providerNo);
		return ret;
	}

	public int createNewSoloDiskName(String providerNo, String creator) {
		int ret = 0;
		String ohipNo = propProOHIP.getProperty(providerNo);
		// set up obj
		String groupNo = "";
		String temp[] = getCurSoloMonthCodeBatchNum(ohipNo);
		BillingDiskNameData diskName = new BillingDiskNameData();
		diskName.setMonthCode(temp[0]);
		diskName.setBatchcount(temp[1]);

		diskName.setGroupno(groupNo);
		diskName.setOhipfilename(getSoloOhipfilename(ohipNo, temp[0], temp[1]));
		diskName.setCreator(creator);
		diskName.setClaimrecord("");
		diskName.setCreatedatetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));
		diskName.setStatus(BillingDataHlp.BILLINGFILE_STATUS_UNCERT);
		diskName.setTotal("");
		diskName.setHtmlfilename(getSoloHtmlfilename(ohipNo, temp[0], temp[1]));

		Vector vecTemp = new Vector();
		vecTemp.add(ohipNo);
		diskName.setProviderohipno(vecTemp);
		vecTemp = new Vector();
		vecTemp.add(providerNo);
		diskName.setProviderno(vecTemp);
		vecTemp = new Vector();
		vecTemp.add(BillingDataHlp.BILLINGFILE_STATUS_UNCERT);
		diskName.setVecStatus(vecTemp);
		vecTemp = new Vector();
		vecTemp.add("");
		diskName.setVecClaimrecord(vecTemp);
		diskName.setVecTotal(vecTemp);

		ret = dbObj.addBillingDiskName(diskName);
		return ret;
	}

	public int createNewGrpDiskName(List providerNo, List ohipNo, String groupNo, String creator) {
		int ret = 0;
		// set up obj
		String temp[] = getCurGrpMonthCodeBatchNum(groupNo);
		BillingDiskNameData diskName = new BillingDiskNameData();
		diskName.setMonthCode(temp[0]);
		diskName.setBatchcount(temp[1]);

		String groupno = (groupNo != null && groupNo.length() == 4) ? groupNo : "";
		diskName.setGroupno(groupno);
		diskName.setOhipfilename(getGrpOhipfilename(groupno, temp[0], temp[1]));
		diskName.setCreator(creator);
		diskName.setClaimrecord("");
		diskName.setCreatedatetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));
		diskName.setStatus(BillingDataHlp.BILLINGFILE_STATUS_UNCERT);
		diskName.setTotal("");
		diskName.setHtmlfilename(getGrpHtmlfilename(ohipNo, groupno, temp[0], temp[1]));
		diskName.setProviderohipno((Vector) ohipNo);
		diskName.setProviderno((Vector) providerNo);
		Vector vecTemp = new Vector();
		vecTemp.add(BillingDataHlp.BILLINGFILE_STATUS_UNCERT);
		diskName.setVecStatus(vecTemp);
		vecTemp = new Vector();
		vecTemp.add("");
		diskName.setVecClaimrecord(vecTemp);
		diskName.setVecTotal(vecTemp);

		ret = dbObj.addBillingDiskName(diskName);
		return ret;
	}

	public boolean updateSoloDiskName(String diskId, String creator) {
		boolean ret = false;
		// set up obj
		// String groupNo = "";

		// get diskName obj
		BillingDiskNameData diskName = dbObj.getDisknameObj(diskId);
		dbObj.addRepoDiskName(diskName);

		// diskName.setGroupno(groupNo);
		diskName.setCreator(creator);
		diskName.setClaimrecord("");
		// diskName.setCreatedatetime(UtilDateUtilities.getToday("yyyy-MM-dd
		// HH:mm:ss"));
		diskName.setStatus(BillingDataHlp.BILLINGFILE_STATUS_UNCERT);
		diskName.setTotal("");

		ret = dbObj.updateDiskName(diskName);
		return ret;
	}

	public int createBatchHeader(BillingProviderData providerData, String disk_id, String moh_office, String seqNum,
			String creator) {
		int ret = 0;
		BillingBatchHeaderData obj = new BillingBatchHeaderData();
		obj.setDisk_id(disk_id);
		obj.setTransc_id(BillingDataHlp.BATCHHEADER_TRANSACTIONIDENTIFIER);
		obj.setRec_id(BillingDataHlp.BATCHHEADER_REORDIDENTIFICATION);
		obj.setSpec_id(BillingDataHlp.BATCHHEADER_SPECID);
		obj.setMoh_office(moh_office);

		String batchid = UtilDateUtilities.getToday("yyyyMMdd") + getDefaultRightJust("0", 4, seqNum);
		obj.setBatch_id(batchid);
		obj.setOperator("");
		obj.setGroup_num(providerData.getBillingGroupNo());
		obj.setProvider_reg_num(providerData.getOhipNo());
		obj.setSpecialty(providerData.getSpecialtyCode());
		obj.setH_count("");
		obj.setR_count("");
		obj.setT_count("");
		obj.setBatch_date(UtilDateUtilities.getToday("yyyy-MM-dd"));

		String strDateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
		obj.setCreatedatetime(strDateTime);
		obj.setUpdatedatetime(strDateTime);
		obj.setCreator(creator);
		obj.setAction(BillingDataHlp.BILLINGACTION_CREATE);
		obj.setComment("");
		ret = dbObj.addOneBatchHeaderRecord(obj);
		return ret;
	}

	public int updateBatchHeader(BillingProviderData providerData, String disk_id, String moh_office, String seqNum,
			String creator) {
		boolean ret = false;
		BillingBatchHeaderData obj = dbObj.getBatchHeaderObj(providerData, disk_id);
		dbObj.addRepoBatchHeader(obj);
		obj.setDisk_id(disk_id);
		obj.setTransc_id(BillingDataHlp.BATCHHEADER_TRANSACTIONIDENTIFIER);
		obj.setRec_id(BillingDataHlp.BATCHHEADER_REORDIDENTIFICATION);
		obj.setSpec_id(BillingDataHlp.BATCHHEADER_SPECID);
		obj.setMoh_office(moh_office);

		String batchid = UtilDateUtilities.getToday("yyyyMMdd") + getDefaultRightJust("0", 4, seqNum);
		obj.setBatch_id(batchid);
		obj.setOperator("");
		obj.setGroup_num(providerData.getBillingGroupNo());
		obj.setProvider_reg_num(providerData.getOhipNo());
		obj.setSpecialty(providerData.getSpecialtyCode());
		obj.setH_count("");
		obj.setR_count("");
		obj.setT_count("");
		obj.setBatch_date(UtilDateUtilities.getToday("yyyy-MM-dd"));

		String strDateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
		// obj.setCreatedatetime(strDateTime);
		obj.setUpdatedatetime(strDateTime);
		obj.setCreator(creator);
		obj.setAction(BillingDataHlp.BILLINGACTION_UPDATE);
		obj.setComment("");
		ret = dbObj.updateBatchHeaderRecord(obj);
		int retval = ret ? Integer.parseInt(obj.getId()) : 0;
		return retval;
	}
	
	private Vector getSoloHtmlfilename(String ohipNo, String monthCode, String batchNum) {
		Vector ret = new Vector();
		String diskName = "H" + monthCode + ohipNo + "_" + getDefaultRightJust("0", 3, batchNum) + ".html";
		ret.add(diskName);
		return ret;
	}

	private Vector getGrpHtmlfilename(List ohipNo, String groupNo, String monthCode, String batchNum) {
		Vector ret = new Vector();
		for (int i = 0; i < ohipNo.size(); i++) {
			String diskName = "H" + monthCode + groupNo + "_" + ohipNo.get(i) + "_"
					+ getDefaultRightJust("0", 3, batchNum) + ".html";
			ret.add(diskName);
		}
		return ret;
	}

	private String getSoloOhipfilename(String ohipNo, String monthCode, String batchNum) {
		String ret = null;
		String diskName = "H" + monthCode + ohipNo + "." + getDefaultRightJust("0", 3, batchNum);
		ret = diskName;
		return ret;
	}

	private String getGrpOhipfilename(String groupNo, String monthCode, String batchNum) {
		String ret = null;
		String diskName = "H" + monthCode + groupNo + "." + getDefaultRightJust("0", 3, batchNum);
		ret = diskName;
		return ret;
	}

	private String getDefaultRightJust(String ch, int num, String val) {
		String ret = "";
		for (int i = 0; i < num; i++) {
			ret += ch;
		}
		int n = val.length();
		ret = ret.substring(0, num - n) + val;
		return ret;
	}

	private String[] getCurSoloMonthCodeBatchNum(String ohipNo) {
		String[] ret = new String[2];
		GregorianCalendar now = new GregorianCalendar();
		int curMonth = (now.get(Calendar.MONTH) + 1);
		String curMonthCode = BillingDataHlp.getPropMonthCode().getProperty("" + curMonth);
		String[] last = dbObj.getLatestSoloMonthCodeBatchNum(ohipNo);

		if (last != null && curMonthCode.equals(last[0])) {
			ret[0] = curMonthCode;
			ret[1] = "" + (Integer.parseInt(last[1]) + 1);
		} else {
			ret[0] = curMonthCode;
			ret[1] = "1";
		}
		return ret;
	}

	private String[] getCurGrpMonthCodeBatchNum(String groupNo) {
		String[] ret = new String[2];
		GregorianCalendar now = new GregorianCalendar();
		// int curYear = now.get(Calendar.YEAR); int curDay =
		// now.get(Calendar.DAY_OF_MONTH);
		int curMonth = (now.get(Calendar.MONTH) + 1);
		String curMonthCode = BillingDataHlp.getPropMonthCode().getProperty("" + curMonth);
		String[] last = dbObj.getLatestGrpMonthCodeBatchNum(groupNo);

		if (last != null && curMonthCode.equals(last[0])) {
			ret[0] = curMonthCode;
			ret[1] = "" + (Integer.parseInt(last[1]) + 1);
		} else {
			ret[0] = curMonthCode;
			ret[1] = "1";
		}
		return ret;
	}
}
