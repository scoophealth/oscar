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

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarBilling.ca.on.data.BillingClaimHeader1Data;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.oscarBilling.ca.on.data.BillingItemData;
import oscar.oscarBilling.ca.on.data.JdbcBillingClaimImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingCodeImpl;
import oscar.util.UtilDateUtilities;

public class BillingSpecPrep {
	private static final Logger _logger = Logger.getLogger(BillingSpecPrep.class);
	JdbcBillingClaimImpl dbObj = new JdbcBillingClaimImpl();

	// save a billing record
	public boolean addABillingRecord(LoggedInInfo loggedInInfo, Vector val) {
		boolean ret = false;
		BillingClaimHeader1Data claim1Obj = (BillingClaimHeader1Data) val.get(0);
		int billingNo = dbObj.addOneClaimHeaderRecord(loggedInInfo, claim1Obj);
		if (billingNo == 0)
			return false;
		if (val.size() > 1) {
			ret = dbObj.addItemRecord((List) val.get(1), billingNo);
			if (!ret)
				return false;
		} else {
			_logger.error("No billing item for billing # " + billingNo);
		}

		return ret;
	}

	// ret - Vector claimheader1data, itemdata
	public Vector getBillingClaimObj(HttpServletRequest requestData) {
		Vector ret = new Vector();
		BillingClaimHeader1Data claim1Header = getClaimHeader1Obj(requestData);
		ret.add(claim1Header);
		BillingItemData[] itemData = getItemObj(requestData);

		List aL = new Vector();
		for (int i = 0; i < itemData.length; i++) {
			aL.add(itemData[i]);
		}
		ret.add(aL);
		return ret;
	}

	// ret - Vector claimheader1data, itemdata
	public Vector getBillingClaimInrObj(HttpServletRequest requestData) {
		Vector ret = new Vector();
		BillingClaimHeader1Data claim1Header = getClaimHeader1InrObj(requestData);
		ret.add(claim1Header);
		BillingItemData[] itemData = getItemInrObj(requestData);

		List aL = new Vector();
		for (int i = 0; i < itemData.length; i++) {
			aL.add(itemData[i]);
		}
		ret.add(aL);
		return ret;
	}

	private BillingClaimHeader1Data getClaimHeader1Obj(HttpServletRequest val) {
		BillingClaimHeader1Data claim1Header = new BillingClaimHeader1Data();

		claim1Header.setTransc_id(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
		claim1Header.setRec_id(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);
		String hin = getHinVer(val.getParameter("demo_hin"))[0];
		String ver = getHinVer(val.getParameter("demo_hin"))[1];
		claim1Header.setHin(hin);
		claim1Header.setVer(ver);
		claim1Header.setDob(val.getParameter("demo_dob"));
		// acc_num - billing no
		String hctype = val.getParameter("demo_hctype").equals("null") ? "ON" : val.getParameter("demo_hctype").equals(
				"") ? "ON" : val.getParameter("demo_hctype");

		claim1Header.setPay_program(getPayProgram(val.getParameter("xml_billtype"), hctype));
		claim1Header.setPayee(val.getParameter("payMethod") != null ? val.getParameter("payMethod")
				: BillingDataHlp.CLAIMHEADER1_PAYEE);
		claim1Header.setRef_num("");

		claim1Header.setFacilty_num(val.getParameter("clinic_ref_code"));
		claim1Header.setAdmission_date("");

		claim1Header.setRef_lab_num("");
		claim1Header.setMan_review("");

		claim1Header.setLocation(val.getParameter("clinicNo"));

		claim1Header.setDemographic_no(val.getParameter("functionid"));
		claim1Header.setProviderNo(val.getParameter("provider").substring(
				val.getParameter("provider").indexOf("|") + 1));
		claim1Header.setAppointment_no(val.getParameter("appointment_no")); // appointment_no;
		claim1Header.setDemographic_name(val.getParameter("demo_name"));
		String temp[] = getPatientLF(val.getParameter("demo_name"));
		claim1Header.setLast_name(temp[0]);
		claim1Header.setFirst_name(temp[1]);
		claim1Header.setSex(val.getParameter("demo_sex"));
		claim1Header.setProvince(hctype);

		claim1Header.setBilling_date(val.getParameter("apptDate"));
		claim1Header.setBilling_time("00:00:00");
		claim1Header.setUpdate_datetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));

		JdbcBillingCodeImpl tObj = new JdbcBillingCodeImpl();
		String total;
		double runningTotal = 0.0;
		String[] codes = val.getParameter("svcCode").split(",");
		List tL;
		for(int idx = 0; idx < codes.length; idx++  ) {
			tL = tObj.getBillingCodeAttr(codes[idx].trim());
			total = (tL != null && tL.size()>0) ? (String) tL.get(2) : "0.0";
			runningTotal += Double.parseDouble(total);
		}
		
		if( (int)runningTotal == 0 ) {
			total = "";
		}
		else {
			total = String.valueOf(runningTotal);
		}
		
		claim1Header.setTotal(total);
		claim1Header.setPaid("");
		claim1Header.setStatus(getStatus(val.getParameter("xml_billtype")));
		claim1Header.setComment("");
		claim1Header.setVisittype(val.getParameter("xml_visittype").substring(0, 2));

		claim1Header.setProvider_ohip_no(val.getParameter("provider").substring(0,
				val.getParameter("provider").indexOf("|")));
		claim1Header.setProvider_rma_no("");
		claim1Header.setApptProvider_no(val.getParameter("apptProvider"));
		claim1Header.setAsstProvider_no("");
		claim1Header.setCreator((String) val.getSession().getAttribute("user"));

		return claim1Header;
	}

	private BillingItemData[] getItemObj(HttpServletRequest val) {
		String tmp = val.getParameter("svcCode");
		String[] codes = tmp.split(",");
		BillingItemData[] claimItem = new BillingItemData[codes.length];
		
		for( int idx = 0; idx < claimItem.length; ++idx ) {
			claimItem[idx] = new BillingItemData();
			claimItem[idx].setTransc_id(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
			claimItem[idx].setRec_id(BillingDataHlp.ITEM_REORDIDENTIFICATION);
			claimItem[idx].setService_code(codes[idx].trim());
			claimItem[idx]
					.setFee((String) (new JdbcBillingCodeImpl()).getBillingCodeAttr(codes[idx].trim()).get(2));
			claimItem[idx].setSer_num("1");
			claimItem[idx].setService_date(val.getParameter("apptDate"));
			claimItem[idx].setDx(val.getParameter("dxCode"));
			claimItem[idx].setDx1("");
			claimItem[idx].setDx2("");
			claimItem[idx].setStatus("O");
		}

		return claimItem;
	}

	private BillingClaimHeader1Data getClaimHeader1InrObj(HttpServletRequest val) {
		BillingClaimHeader1Data claim1Header = new BillingClaimHeader1Data();

		claim1Header.setTransc_id(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
		claim1Header.setRec_id(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);
		String hin = getHinVer(val.getParameter("demo_hin"))[0];
		String ver = getHinVer(val.getParameter("demo_hin"))[1];
		claim1Header.setHin(hin);
		claim1Header.setVer(ver);
		claim1Header.setDob(val.getParameter("demo_dob"));
		// acc_num - billing no
		String hctype = val.getParameter("demo_hctype")==null ? "ON" : (val.getParameter("demo_hctype").equals(
				"") ? "ON" : val.getParameter("demo_hctype"));

		claim1Header.setPay_program(getPayProgram(val.getParameter("xml_billtype"), hctype));
		claim1Header.setPayee(val.getParameter("payMethod") != null ? val.getParameter("payMethod")
				: BillingDataHlp.CLAIMHEADER1_PAYEE);
		claim1Header.setRef_num("");

		claim1Header.setFacilty_num("");
		claim1Header.setAdmission_date("");

		claim1Header.setRef_lab_num("");
		claim1Header.setMan_review("");

		claim1Header.setLocation(val.getParameter("clinicNo"));

		claim1Header.setDemographic_no(val.getParameter("functionid"));
		claim1Header.setProviderNo(val.getParameter("provider").substring(
				val.getParameter("provider").indexOf("|") + 1));
		claim1Header.setAppointment_no(val.getParameter("appointment_no")); // appointment_no;
		claim1Header.setDemographic_name(val.getParameter("demo_name"));
		String temp[] = getPatientLF(val.getParameter("demo_name"));
		claim1Header.setLast_name(temp[0]);
		claim1Header.setFirst_name(temp[1]);
		claim1Header.setSex(val.getParameter("demo_sex"));
		claim1Header.setProvince(hctype);

		claim1Header.setBilling_date(val.getParameter("apptDate"));
		claim1Header.setBilling_time("00:00:00");
		claim1Header.setUpdate_datetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));

		JdbcBillingCodeImpl tObj = new JdbcBillingCodeImpl();
		String total = "";
		List tL = tObj.getBillingCodeAttr(val.getParameter("svcCode"));
		total = tL != null ? (String) tL.get(2) : "";

		claim1Header.setTotal(total);
		claim1Header.setPaid("");
		claim1Header.setStatus(getStatus(val.getParameter("xml_billtype")));
		claim1Header.setComment("");
		claim1Header.setVisittype(val.getParameter("xml_visittype").substring(0, 2));

		claim1Header.setProvider_ohip_no(val.getParameter("provider").substring(0,
				val.getParameter("provider").indexOf("|")));
		claim1Header.setProvider_rma_no("");
		claim1Header.setApptProvider_no(val.getParameter("apptProvider"));
		claim1Header.setAsstProvider_no("");
		claim1Header.setCreator((String) val.getSession().getAttribute("user"));

		return claim1Header;
	}

	private BillingItemData[] getItemInrObj(HttpServletRequest val) {
		BillingItemData[] claimItem = new BillingItemData[1];
		// _logger.info("No billing item for billing # " + itemNum);
		claimItem[0] = new BillingItemData();
		claimItem[0].setTransc_id(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
		claimItem[0].setRec_id(BillingDataHlp.ITEM_REORDIDENTIFICATION);
		claimItem[0].setService_code(val.getParameter("svcCode"));
		claimItem[0]
				.setFee((String) (new JdbcBillingCodeImpl()).getBillingCodeAttr(val.getParameter("svcCode")).get(2));
		claimItem[0].setSer_num("1");
		claimItem[0].setService_date(val.getParameter("apptDate"));
		claimItem[0].setDx(val.getParameter("dxCode"));
		claimItem[0].setDx1("");
		claimItem[0].setDx2("");
		claimItem[0].setStatus("O");

		return claimItem;
	}

	// 1-hin 2-ver
	private String[] getHinVer(String val) {
		String[] ret = { "", "" };
		for (int i = 0; i < val.length(); i++) {
			if (("" + val.charAt(i)).matches("\\d")) {
				ret[0] += val.charAt(i);
			} else {
				ret[1] += val.charAt(i);
			}
		}

		return ret;
	}

	// HCP/WCB/RMB/NOT/PAT/...
	private String getPayProgram(String val, String hcType) {
		String ret = val.substring(0, 3);
		if (val.startsWith("PAT")) {
			ret = BillingDataHlp.CLAIMHEADER1_PAYMENTPROGRAM_PRIVATE;
		} else if (val.startsWith("ODP")) {
			ret = hcType.equals("ON") ? "HCP" : "RMB";
		}
		return ret;
	}

	// 1-last name 9, 2-first name 5
	private String[] getPatientLF(String val) {
		String[] ret = new String[2];
		if (val.indexOf(",") >= 0) {
			ret = val.split(",");
			ret[0] = ret[0].replaceAll("\\W", "");
			ret[0] = ret[0].length() > 9 ? ret[0].substring(0, 9) : ret[0];
			ret[1] = ret[1].replaceAll("\\W", "");
			ret[1] = ret[1].length() > 5 ? ret[1].substring(0, 5) : ret[1];
		}

		return ret;
	}

	private String getStatus(String payProg) {
		String ret = "O";
		if (payProg.startsWith("NOT")) {
			ret = "N";
		}
		return ret;
	}

}
