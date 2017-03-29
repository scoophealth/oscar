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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.on.data.BillingClaimHeader1Data;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.oscarBilling.ca.on.data.BillingItemData;
import oscar.oscarBilling.ca.on.data.JdbcBillingClaimImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingPageUtil;
import oscar.util.UtilDateUtilities;

public class BillingSavePrep {
	private static final Logger _logger = Logger.getLogger(BillingReviewPrep.class);
	JdbcBillingClaimImpl dbObj = new JdbcBillingClaimImpl();
	int billingId = 0;

	// save a billing record
	@SuppressWarnings("rawtypes")
	public boolean addABillingRecord(LoggedInInfo loggedInInfo, Vector val) {
		boolean ret = false;
		BillingClaimHeader1Data claim1Obj = (BillingClaimHeader1Data) val.get(0);
		int billingNo = dbObj.addOneClaimHeaderRecord(loggedInInfo, claim1Obj);
		billingId = billingNo;
		if (billingNo == 0)
			return false;
		claim1Obj.setId(((Integer)billingId).toString());
		if (val.size() > 1) {
			ret = dbObj.addItemRecord((List) val.get(1), billingNo);
			if (!ret)
				return false;
		} else {
			_logger.error("No billing item for billing # " + billingNo);
		}

		return ret;
	}

	public boolean addPrivateBillExtRecord(HttpServletRequest requestData) {
		boolean ret = false;
		Map<String,String> val = getPrivateBillExtObj(requestData);
		ret = dbObj.add3rdBillExt(val, billingId);
		if (!ret)
			_logger.error("addPrivateBillExtRecord " + billingId);

		return ret;
	}


	@SuppressWarnings("unchecked")
	public boolean addPrivateBillExtRecord(HttpServletRequest requestData, Vector vecObj) {
		boolean ret = false;
		boolean rat = false;
		
		@SuppressWarnings("unused")
		Map<String,String> val = getPrivateBillExtObj(requestData);
		ret = dbObj.add3rdBillExt(val, billingId, vecObj);
		if (!ret)
			_logger.error("addPrivateBillExtRecord " + billingId);

		return ret;
	}
	

	
	@SuppressWarnings("unchecked")
	public void addOhipInvoiceTrans(Vector vecObj) {
		dbObj.addCreateOhipInvoiceTrans((BillingClaimHeader1Data) vecObj.get(0), (List<BillingItemData>) vecObj.get(1));
	}
	
	// set appt to B
	public boolean updateApptStatus(String apptNo, String status, String userNo) {
		boolean ret = (new JdbcBillingPageUtil()).updateApptStatus(apptNo, status, userNo);
		return ret;
	}

	// get appt status
	public String getApptStatus(String apptNo) {
		String ret = (new JdbcBillingPageUtil()).getApptStatus(apptNo);
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
	public Vector getBillingClaimHospObj(HttpServletRequest requestData, String service_date, String total,
			Vector vecServiceCode, Vector vecServiceCodeUnit, Vector vecServiceCodePrice) {
		Vector ret = new Vector();
		BillingClaimHeader1Data claim1Header = getClaimHeader1HospObj(requestData, service_date, total);
		ret.add(claim1Header);
		BillingItemData[] itemData = getItemHospObj(requestData, vecServiceCode, vecServiceCodeUnit,
				vecServiceCodePrice, service_date);

		List aL = new Vector();
		for (int i = 0; i < itemData.length; i++) {
			aL.add(itemData[i]);
		}
		ret.add(aL);
		return ret;
	}



	private BillingClaimHeader1Data getClaimHeader1Obj(HttpServletRequest val) {
                String billtype = val.getParameter("xml_billtype");

		BillingClaimHeader1Data claim1Header = new BillingClaimHeader1Data();

		claim1Header.setTransc_id(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
		claim1Header.setRec_id(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);

                if( !billtype.substring(0,3).equals("BON") ) {
                    claim1Header.setHin(val.getParameter("hin"));
                    claim1Header.setVer(val.getParameter("ver"));
                    claim1Header.setDob(val.getParameter("demographic_dob"));
                    claim1Header.setAppointment_no(val.getParameter("appointment_no")); // appointment_no;
                    claim1Header.setDemographic_name(val.getParameter("demographic_name"));
                    String temp[] = getPatientLF(val.getParameter("demographic_name"));
                    claim1Header.setLast_name(temp[0]);
                    claim1Header.setFirst_name(temp[1]);
                    claim1Header.setSex(val.getParameter("sex"));
                    claim1Header.setProvince(val.getParameter("hc_type"));
                }
                else {
                    claim1Header.setHin("");
                    claim1Header.setVer("");
                    claim1Header.setDob("");
                    claim1Header.setAppointment_no(""); // appointment_no;
                    claim1Header.setDemographic_name("");
                    claim1Header.setLast_name("");
                    claim1Header.setFirst_name("");
                    claim1Header.setSex("");
                    claim1Header.setProvince("ON");
                }

		// acc_num - billing no
		claim1Header.setPay_program(getPayProgram(val.getParameter("xml_billtype"), val.getParameter("hc_type")));
		claim1Header.setPayee(val.getParameter("payMethod") != null ? val.getParameter("payMethod")
				: BillingDataHlp.CLAIMHEADER1_PAYEE);
		claim1Header.setRef_num(val.getParameter("referralCode"));

		claim1Header.setFacilty_num(val.getParameter("xml_location").substring(0, 4));
		claim1Header.setAdmission_date(val.getParameter("xml_vdate"));

		claim1Header.setRef_lab_num("");
		claim1Header.setMan_review(val.getParameter("m_review") != null ? val.getParameter("m_review") : "");

		if(val.getParameter("xml_slicode") != null) {
			claim1Header.setLocation(val.getParameter("xml_slicode").trim());
		}

		claim1Header.setDemographic_no(val.getParameter("demographic_no"));
		claim1Header.setProviderNo(val.getParameter("xml_provider").substring(0,
				val.getParameter("xml_provider").indexOf("|")));

		claim1Header.setBilling_date(val.getParameter("service_date"));
		claim1Header.setBilling_time(val.getParameter("start_time"));
		claim1Header.setUpdate_datetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));
		claim1Header.setTotal(val.getParameter("total"));
		String paid = "";
		if (val.getParameter("submit").equalsIgnoreCase("Settle")) {
			paid = val.getParameter("total");
		} else if (val.getParameter("submit").equalsIgnoreCase("Save & Print Invoice")
				|| val.getParameter("submit").equalsIgnoreCase("Settle & Print Invoice")
				|| val.getParameter("submit").equalsIgnoreCase("Save")
				|| val.getParameter("submit").equalsIgnoreCase("Save & Add Another Bill")) {
			paid = val.getParameter("total_payment");
		}
		claim1Header.setPaid(paid);
		claim1Header.setStatus(getStatus(val.getParameter("submit"), val.getParameter("xml_billtype")));
		claim1Header.setComment(val.getParameter("comment") != null ? val.getParameter("comment") : "");
		claim1Header.setVisittype(val.getParameter("xml_visittype").substring(0, 2));
		claim1Header.setProvider_ohip_no(val.getParameter("xml_provider").substring(
				val.getParameter("xml_provider").indexOf("|") + 1));
		claim1Header.setProvider_rma_no("");
		claim1Header.setApptProvider_no(val.getParameter("apptProvider_no"));
		claim1Header.setAsstProvider_no("");
		claim1Header.setCreator((String) val.getSession().getAttribute("user"));

		claim1Header.setClinic(val.getParameter("site"));

		return claim1Header;
	}

	private BillingItemData[] getItemObj(HttpServletRequest val) {
		int itemNum = Integer.parseInt(val.getParameter("totalItem"));
		BillingItemData[] claimItem = new BillingItemData[itemNum];
		// _logger.info("No billing item for billing # " + itemNum);

		for (int i = 0; i < itemNum; i++) {
			claimItem[i] = new BillingItemData();
			claimItem[i].setTransc_id(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
			claimItem[i].setRec_id(BillingDataHlp.ITEM_REORDIDENTIFICATION);
			claimItem[i].setService_code(val.getParameter("xserviceCode_" + i));
			if(val.getParameter("xsliCode_" + i)!=null) {
				claimItem[i].setLocation(val.getParameter("xsliCode_" + i));
			}
			claimItem[i].setFee(val.getParameter("percCodeSubtotal_" + i));
			claimItem[i].setSer_num(getDefaultUnit(val.getParameter("xserviceUnit_" + i)));
			claimItem[i].setService_date(val.getParameter("service_date"));
			claimItem[i].setDx(val.getParameter("dxCode"));
			claimItem[i].setDx1(val.getParameter("dxCode1"));
			claimItem[i].setDx2(val.getParameter("dxCode2"));
			if(val.getParameter("paid_"+i)!=null){
				claimItem[i].setPaid(val.getParameter("paid_"+i));
			}else{
				claimItem[i].setPaid("0.00");	
			}
			//claimItem[i].setRefund(val.getParameter("refund"));
			if(val.getParameter("discount_"+i)!=null){
				claimItem[i].setDiscount(val.getParameter("discount_"+i));
			}else{
				claimItem[i].setDiscount("0.00");
			}
			if(val.getParameter("xml_billtype").substring(0,3).matches(BillingDataHlp.BILLINGMATCHSTRING_3RDPARTY)) {
				claimItem[i].setStatus("P");
			} else {
				claimItem[i].setStatus("O");
			}
		}

		return claimItem;
	}

	private BillingClaimHeader1Data getClaimHeader1HospObj(HttpServletRequest val, String service_date, String total) {
		BillingClaimHeader1Data claim1Header = new BillingClaimHeader1Data();

		claim1Header.setTransc_id(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
		claim1Header.setRec_id(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);
		String hin = getHinVer(val.getParameter("hin"))[0];
		String ver = getHinVer(val.getParameter("hin"))[1];
		claim1Header.setHin(hin);
		claim1Header.setVer(ver);

		claim1Header.setDob(val.getParameter("demographic_dob"));
		// acc_num - billing no
		claim1Header.setPay_program(getPayProgram(val.getParameter("xml_billtype"), val.getParameter("hc_type")));
		claim1Header.setPayee(val.getParameter("payMethod") != null ? val.getParameter("payMethod")
				: BillingDataHlp.CLAIMHEADER1_PAYEE);
		claim1Header.setRef_num(val.getParameter("referralCode"));

		claim1Header.setFacilty_num(val.getParameter("xml_location").substring(0, 4));
		claim1Header.setAdmission_date(val.getParameter("xml_vdate"));

		claim1Header.setRef_lab_num("");
		claim1Header.setMan_review("");

		claim1Header.setLocation(val.getParameter("xml_slicode").trim());

		claim1Header.setDemographic_no(val.getParameter("demographic_no"));
		if(org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {		
			claim1Header.setProviderNo(val.getParameter("xml_provider").substring(0, val.getParameter("xml_provider").indexOf("|")));
		} else {
			claim1Header.setProviderNo(val.getParameter("xml_provider"));
		}
		
		claim1Header.setAppointment_no(val.getParameter("appointment_no"));
		claim1Header.setDemographic_name(val.getParameter("demographic_name"));
		String temp[] = getPatientLF(val.getParameter("demographic_name"));
		claim1Header.setLast_name(temp[0]);
		claim1Header.setFirst_name(temp[1]);
		claim1Header.setSex(val.getParameter("sex"));
		claim1Header.setProvince(val.getParameter("hc_type"));

		claim1Header.setBilling_date(service_date);
		claim1Header.setBilling_time(val.getParameter("start_time"));
		claim1Header.setUpdate_datetime(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss"));
		claim1Header.setTotal(total);
		claim1Header.setPaid("");
		claim1Header.setStatus(getStatus("", val.getParameter("xml_billtype")));
		claim1Header.setComment(val.getParameter("comment") != null ? val.getParameter("comment") : "");
		claim1Header.setVisittype(val.getParameter("xml_visittype").substring(0, 2));
		claim1Header.setProvider_ohip_no(val.getParameter("proOHIPNO"));
		claim1Header.setProvider_rma_no("");
		claim1Header.setApptProvider_no(val.getParameter("apptProvider_no"));
		claim1Header.setAsstProvider_no("");
		claim1Header.setCreator((String) val.getSession().getAttribute("user"));
		claim1Header.setClinic(val.getParameter("site"));
		
		return claim1Header;
	}

	private BillingItemData[] getItemHospObj(HttpServletRequest val, Vector vecServiceCode, Vector vecServiceCodeUnit,
			Vector vecServiceCodePrice, String service_date) {
		int itemNum = vecServiceCode.size();
		BillingItemData[] claimItem = new BillingItemData[itemNum];
		// _logger.info("No billing item for billing # " + itemNum);

		for (int i = 0; i < vecServiceCode.size(); i++) { // recordCount
			BigDecimal bdEachPrice = new BigDecimal((String) vecServiceCodePrice.get(i)).setScale(
					2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdEachUnit = new BigDecimal((String) vecServiceCodeUnit.get(i)).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			BigDecimal bdEachTotal = bdEachPrice.multiply(bdEachUnit).setScale(2, BigDecimal.ROUND_HALF_UP);

			claimItem[i] = new BillingItemData();
			claimItem[i].setTransc_id(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
			claimItem[i].setRec_id(BillingDataHlp.ITEM_REORDIDENTIFICATION);

			claimItem[i].setService_code((String) vecServiceCode.get(i));
			claimItem[i].setFee("" + bdEachTotal);
			claimItem[i].setSer_num(getDefaultUnit((String) vecServiceCodeUnit.get(i)));
			claimItem[i].setService_date(service_date);
			claimItem[i].setDx(getDefaultSpace(val.getParameter("dxCode")));
			claimItem[i].setDx1(getDefaultSpace(val.getParameter("dxCode1")));
			claimItem[i].setDx2(getDefaultSpace(val.getParameter("dxCode2")));
			claimItem[i].setPaid(getDefaultSpace(val.getParameter("payment")));
			claimItem[i].setRefund(getDefaultSpace(val.getParameter("refund")));
			claimItem[i].setDiscount(getDefaultSpace(val.getParameter("discount")));
			claimItem[i].setStatus("O");
		}
		return claimItem;
	}

	private Map getPrivateBillExtObj(HttpServletRequest val) {
		Map<String,String> valsMap = new HashMap<String,String>();
		valsMap.put("demographic_no",val.getParameter("demographic_no"));
		valsMap.put("billTo",val.getParameter("billto"));
		valsMap.put("total_discount", val.getParameter("total_discount"));
		valsMap.put("remitTo",val.getParameter("remitto"));
                valsMap.put("total",val.getParameter("gstBilledTotal"));
                if (val.getParameter("submit").equalsIgnoreCase("Settle & Print Invoice")) {
                    valsMap.put("total_payment", val.getParameter("total_payment"));
                }
                else {
                    valsMap.put("total_payment", val.getParameter("total_payment"));
                }
		valsMap.put("refund",val.getParameter("refund"));
                valsMap.put("provider_no",val.getParameter("provider_no"));
                valsMap.put("gst",val.getParameter("gst"));

		if (val.getParameter("payMethod") != null) {
			valsMap.put("payMethod",val.getParameter("payMethod"));
		} else {
			valsMap.put("payMethod", "1");
		}
		
		if (val.getParameter("payment_date") != null) {
			valsMap.put("payment_date", val.getParameter("payment_date"));
		}
		return valsMap;
	}

	// HCP/WCB/RMB/NOT/PAT/...
	private String getPayProgram(String val, String hcType) {
		String ret = val.substring(0, 3);
		if (val.startsWith("PAT")) {
			ret = BillingDataHlp.CLAIMHEADER1_PAYMENTPROGRAM_PRIVATE;
		} else if (val.startsWith("ODP")) {
			ret = hcType.equals("ON") ? "HCP" : "RMB";
		} else if (val.startsWith("BON")) {
                        ret = "HCP";
                }
		return ret;
	}

	private String getStatus(String submit, String payProg) {
		String ret = "O";
		if (submit.startsWith("Settle")) {
			ret = "S";
		} else if (payProg.startsWith("NOT")) {
			ret = "N";
		} else if( payProg.startsWith("BON") ) {
                        ret = "I";
                } else if( payProg.startsWith("PAT")) {
                        ret = "P";
		} else if( payProg.startsWith("WCB")) {
			ret = "W";
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

	// 1-default
	private String getDefaultUnit(String val) {
		String ret = "".equals(val) ? "1" : val;
		return ret;
	}

	// ""-default
	private String getDefaultSpace(String val) {
		String ret = val == null ? "" : val;
		return ret;
	}

	// 1-hin 2-ver
	private String[] getHinVer(String val) {
		String[] ret = { "", "" };
		MiscUtils.getLogger().debug("Hinver: " + val);
		if (val != null) {
			for (int i = 0; i < val.length(); i++) {
				if (("" + val.charAt(i)).matches("\\d")) {
					ret[0] += val.charAt(i);
				} else {
					ret[1] += val.charAt(i);
				}
			}
		}
		return ret;
	}

	public int getBillingId() {
		return billingId;
	}

	public void setBillingId(int billingId) {
		this.billingId = billingId;
	}

}
