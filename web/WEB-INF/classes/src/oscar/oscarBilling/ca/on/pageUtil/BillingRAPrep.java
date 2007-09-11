/*
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Yi Li
 */
package oscar.oscarBilling.ca.on.pageUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import oscar.oscarBilling.ca.on.data.JdbcBillingRAImpl;

public class BillingRAPrep {
	private static final Logger _logger = Logger.getLogger(BillingRAPrep.class);
	JdbcBillingRAImpl dbObj = new JdbcBillingRAImpl();

	// ret - Vector = || ||
	public List getProviderListFromRAReport(String raNo) {
		List ret = dbObj.getProviderListFromRAReport(raNo);
		return ret;
	}

	public List getRAErrorReport(String raNo, String providerOhipNo, String notErrorCode) {
		List ret = dbObj.getRAErrorReport(raNo, providerOhipNo, notErrorCode);
		return ret;
	}

	public List getRABillingNo4Code(String raNo, String codes) {
		List ret = dbObj.getRABillingNo4Code(raNo, codes);
		return ret;
	}

        public List getRASummary(String raNo, String providerOhipNo, List OBbilling_no, List CObilling_no) {
	   return getRASummary(raNo, providerOhipNo, OBbilling_no, CObilling_no,null); 
        }
        
	public List getRASummary(String raNo, String providerOhipNo, List OBbilling_no, List CObilling_no,Map map) {
		List rett = new Vector();
		List ret = dbObj.getRASummary(raNo, providerOhipNo);
		double dCFee = 0.0;
		double dPFee = 0.0;
		BigDecimal BigCTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigPTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigOBTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigCOTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigHTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigLocalHTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigOTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigLTotal = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
		// Billing No Provider Patient HIN Service Date Service Code Invoiced :
		// new BigDecimal(0)
		// Paid Clinic Pay Hospital Pay OB Error
		for (int j = 0; j < ret.size(); j++) {
			Properties prop = (Properties) ret.get(j);
			String proName = prop.getProperty("proName");
			String servicecode = prop.getProperty("servicecode");
			String servicedate = prop.getProperty("servicedate");
			servicedate = servicedate.length() == 8 ? (servicedate.substring(0, 4) + "-" + servicedate.substring(4, 6)
					+ "-" + servicedate.substring(6)) : servicedate;
			prop.setProperty("servicedate", servicedate);
			String serviceno = prop.getProperty("serviceno");
			String explain = prop.getProperty("explain");
			String amountsubmit = prop.getProperty("amountsubmit");
			String amountpay = prop.getProperty("amountpay");
			String location = prop.getProperty("location");
			String localServiceDate = prop.getProperty("localServiceDate");
			String demo_name = prop.getProperty("demo_name");
			String demo_hin = prop.getProperty("demo_hin");
			String account = prop.getProperty("account");
			String clinicPay = "";
			String hospitalPay = "";
			String obPay = "";

			dCFee = Double.parseDouble(amountsubmit);
			BigDecimal bdCFee = new BigDecimal(dCFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigCTotal = BigCTotal.add(bdCFee);

			dPFee = Double.parseDouble(amountpay);
			BigDecimal bdPFee = new BigDecimal(dPFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigPTotal = BigPTotal.add(bdPFee);
			String COflag = "0";
			String OBflag = "0";

			// set flag
			for (int i = 0; i < OBbilling_no.size(); i++) {
				String sqlRAOB = (String) OBbilling_no.get(i);
				if (sqlRAOB.compareTo(account) == 0) {
					OBflag = "1";
					break;
				}
			}
			for (int i = 0; i < CObilling_no.size(); i++) {
				String sqlRACO = (String) CObilling_no.get(i);
				if (sqlRACO.compareTo(account) == 0) {
					COflag = "1";
					break;
				}
			}

			if (OBflag.equals("1")) {
				String amountOB = amountpay;
				double dOBFee = Double.parseDouble(amountOB);
				BigDecimal bdOBFee = new BigDecimal(dOBFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigOBTotal = BigOBTotal.add(bdOBFee);
                                obPay = amountpay;
			} else {
				obPay = "N/A";
				// amountOB = "N/A";
			}

			if (COflag.equals("1")) {
				String amountCO = amountpay;
				double dCOFee = Double.parseDouble(amountCO);
				BigDecimal bdCOFee = new BigDecimal(dCOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigCOTotal = BigCOTotal.add(bdCOFee);
			} else {
				// amountCO = "N/A";
			}

			if (explain.compareTo("") == 0 || explain == null) {
				explain = "**";
				prop.setProperty("explain", explain);
			}

			if (location.compareTo("02") == 0) {
				double dHFee = Double.parseDouble(amountpay);
				BigDecimal bdHFee = new BigDecimal(dHFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigHTotal = BigHTotal.add(bdHFee);
				clinicPay = "N/A";
				hospitalPay = amountpay;

				// is local for hospital
				if (demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
					BigLocalHTotal = BigLocalHTotal.add(bdHFee);
				}
			} else {
				if (location.compareTo("00") == 0 && demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
					double dFee = Double.parseDouble(amountpay);
					BigDecimal bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigTotal = BigTotal.add(bdFee);
					clinicPay = amountpay;
					hospitalPay = "N/A";
				} else {
					double dOFee = Double.parseDouble(amountpay);
					BigDecimal bdOFee = new BigDecimal(dOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigOTotal = BigOTotal.add(bdOFee);
					clinicPay = "N/A";
					hospitalPay = "N/A";
				}
			}
			prop.setProperty("clinicPay", clinicPay);
			prop.setProperty("hospitalPay", hospitalPay);
			prop.setProperty("obPay", obPay);
			rett.add(prop);
		}

		BigLTotal = BigLTotal.add(BigTotal);
		BigLTotal = BigLTotal.add(BigLocalHTotal);
		Properties prop = new Properties();
		prop.setProperty("servicecode", "Total");
		prop.setProperty("amountsubmit", BigCTotal.toString());
		prop.setProperty("amountpay", BigPTotal.toString());
		prop.setProperty("clinicPay", BigTotal.toString());
		prop.setProperty("hospitalPay", BigHTotal.toString());
		prop.setProperty("obPay", BigOBTotal.toString());
		rett.add(prop);

                if (map != null){
                   map.put("xml_local", BigLTotal );
                   map.put("xml_total", BigPTotal ); 
                   map.put("xml_other_total", BigOTotal);
                   map.put("xml_ob_total", BigOBTotal);
                   map.put("xml_co_total", BigCOTotal );
                }
                ///end
                
                
                
                
		return rett;
	}

	public List getRANoErrorBill(String raNo, String providerOhipNo, String noErrorCodes, String errorCodes) {
		List ret = new Vector();
		List errorBill = dbObj.getRAError35(raNo, providerOhipNo, errorCodes); // !=i2,
																				// ...
		List noErrorBill = dbObj.getRAError35(raNo, providerOhipNo, noErrorCodes); // =
																					// I2,
																					// ...
		for (int i = 0; i < noErrorBill.size(); i++) {
			String errorAccount = (String) noErrorBill.get(i);
			if (!errorBill.contains(errorAccount)) {
				ret.add(errorAccount);
			}
		}
		return ret;
	}

	public boolean updateBillingStatus(String id, String status) {
		boolean ret = dbObj.updateBillingStatus(id, status);
		return ret;
	}

	// /////////////////////////
	/*
	 * public boolean updateBillingStatus(String id, String status) { while
	 * (rsdemo.next()) { account = rsdemo.getString("billing_no"); eFlag="1";
	 * for (int i=0; i< errorBill.size(); i++){ errorAccount = (String)
	 * errorBill.get(i); if(errorAccount.compareTo(account)==0) { eFlag = "0";
	 * break; } }
	 * 
	 * if(eFlag.compareTo("1")==0) noErrorBill.add(account); } public Vector
	 * getServiceCodeReviewVec(Vector vecCode, Vector vecUnit, Vector vecAt) {
	 * Vector ret = new Vector(); BillingReviewCodeItem codeItem = null;
	 * 
	 * for (int i = 0; i < vecCode.size(); i++) { if (((String)
	 * vecCode.get(i)).equals("")) continue; // get fee String fee =
	 * dbObj.getCodeFee((String) vecCode.get(i)); // judge fee if (fee == null) {
	 * codeItem = new BillingReviewCodeItem(); codeItem.setCodeName((String)
	 * vecCode.get(i)); codeItem.setCodeUnit((String) vecUnit.get(i));
	 * codeItem.setCodeFee("0"); codeItem.setCodeTotal("0"); codeItem.setMsg("<b>No
	 * this code in the database!!!</b>"); ret.add(codeItem);
	 * _logger.error("getServiceCodeReviewVec: No this code in the database! " +
	 * vecCode.get(i)); continue; } // if perc. code if (fee.equals(".00") ||
	 * fee.equals("")) continue; // calculate fee BigDecimal bigCodeFee = new
	 * BigDecimal(fee); // System.out.println((String) vecUnit.get(i) + "big
	 * bigCodeFee: " + // bigCodeFee.toString()); BigDecimal bigCodeUnit = new
	 * BigDecimal((String) vecUnit.get(i)); BigDecimal bigCodeAt = new
	 * BigDecimal((String) vecAt.get(i)); // System.out.println("big bigCodeAt: " +
	 * (String) vecAt.get(i)); BigDecimal bigFee =
	 * bigCodeFee.multiply(bigCodeUnit); // System.out.println("big fee1: " +
	 * bigFee.toString()); bigFee = bigFee.multiply(bigCodeAt); //
	 * System.out.println("big fee2: " + bigFee.toString()); bigFee =
	 * bigFee.setScale(2, BigDecimal.ROUND_HALF_UP); // bigFee =
	 * bigFee.round(new MathContext(2)); System.out.println("big end: " +
	 * bigFee.toString());
	 * 
	 * codeItem = new BillingReviewCodeItem(); codeItem.setCodeName((String)
	 * vecCode.get(i)); codeItem.setCodeUnit((String) vecUnit.get(i));
	 * codeItem.setCodeFee(fee); codeItem.setCodeTotal(bigFee.toString());
	 * codeItem.setMsg(""); ret.add(codeItem); } return ret; } // get perc code
	 * item display public Vector getPercCodeReviewVec(Vector vecCode, Vector
	 * vecReviewCodeItem) { Vector ret = new Vector(); // no perc. code if
	 * (vecCode.size() == vecReviewCodeItem.size()) return ret; //
	 * BillingReviewCodeItem codeItem = null; BillingReviewPercItem percItem =
	 * null; Vector vecCodeFee = new Vector(); for (int i = 0; i <
	 * vecReviewCodeItem.size(); i++) { vecCodeFee.add(((BillingReviewCodeItem)
	 * vecReviewCodeItem.get(i)).getCodeFee()); }
	 * 
	 * for (int i = 0; i < vecCode.size(); i++) { if (((String)
	 * vecCode.get(i)).equals("")) continue; // not perc. code if (i <
	 * vecReviewCodeItem.size() && ((String)
	 * vecCode.get(i)).equals(((BillingReviewCodeItem) vecReviewCodeItem.get(i))
	 * .getCodeName())) { continue; } // take perc. code // get fee String fee =
	 * dbObj.getPercFee((String) vecCode.get(i));
	 * 
	 * if (fee == null) { percItem = new BillingReviewPercItem();
	 * percItem.setCodeName((String) vecCode.get(i)); percItem.setCodeUnit("1");
	 * percItem.setCodeFee("0"); percItem.setCodeMinFee("");
	 * percItem.setCodeMaxFee(""); percItem.setVecCodeFee(new Vector());
	 * percItem.setVecCodeTotal(new Vector()); percItem.setMsg("<b>No this
	 * perc. code in the database!!!</b>"); ret.add(percItem);
	 * _logger.error("getServiceCodeReviewVec: No this perc. code in the
	 * database! " + vecCode.get(i)); continue; } // calculate fee Vector
	 * vecCodeTotal = new Vector(); for (int j = 0; j < vecCodeFee.size(); j++) {
	 * BigDecimal bigCodeFee = new BigDecimal((String) vecCodeFee.get(j)); //
	 * BigDecimal bigCodeUnit = new BigDecimal((String) // vecUnit.get(i)); //
	 * BigDecimal bigCodeAt = new BigDecimal((String) vecAt.get(i)); BigDecimal
	 * bigFee = bigCodeFee.multiply(new BigDecimal(fee)); //
	 * System.out.println("big fee1: " + bigFee.toString()); // bigFee =
	 * bigFee.multiply(bigCodeAt); // System.out.println("big fee2: " +
	 * bigFee.toString()); bigFee = bigFee.setScale(4,
	 * BigDecimal.ROUND_HALF_UP); // bigFee = bigFee.round(new MathContext(2));
	 * System.out.println("big end: " + bigFee.toString());
	 * vecCodeTotal.add(bigFee.toString()); } // get min/max fee String[] mFee =
	 * dbObj.getPercMinMaxFee((String) vecCode.get(i)); percItem = new
	 * BillingReviewPercItem(); percItem.setCodeName((String) vecCode.get(i));
	 * percItem.setCodeUnit("1"); percItem.setCodeFee(fee);
	 * percItem.setCodeMinFee(mFee[0]); percItem.setCodeMaxFee(mFee[1]);
	 * percItem.setVecCodeFee(vecCodeFee);
	 * percItem.setVecCodeTotal(vecCodeTotal); percItem.setMsg("");
	 * ret.add(percItem); } return ret; } // ret[0],[1],[2] - Vector vecCode,
	 * Vector vecUnit, Vector vecAt public Vector[]
	 * getRequestCodeVec(HttpServletRequest requestData, String paramNameCode,
	 * String paramNameUnit, String paramNameAt, int numItem) { Vector[] ret =
	 * new Vector[3]; ret[0] = new Vector(); ret[1] = new Vector(); ret[2] = new
	 * Vector();
	 * 
	 * for (int i = 0; i < numItem; i++) { if
	 * ("".equals(requestData.getParameter(paramNameCode + i))) continue;
	 * ret[0].add(requestData.getParameter(paramNameCode + i));
	 * ret[1].add(defaultParamValue(requestData.getParameter(paramNameUnit +
	 * i))); ret[2].add(defaultParamValue(requestData.getParameter(paramNameAt +
	 * i))); } return ret; } // ret[0],[1],[2] - Vector vecCode, Vector vecUnit,
	 * Vector vecAt - from form // checkbox // this way for no sequence order //
	 * should change to col1, col2, col3 scan and get a sequence order public
	 * Vector[] getRequestFormCodeVec(HttpServletRequest requestData, String
	 * paramNameCode, String paramNameUnit, String paramNameAt) { Vector[] ret =
	 * new Vector[3]; ret[0] = new Vector(); ret[1] = new Vector(); ret[2] = new
	 * Vector();
	 * 
	 * for (Enumeration e = requestData.getParameterNames();
	 * e.hasMoreElements();) { String temp = e.nextElement().toString(); if
	 * (temp.startsWith(paramNameCode) && (temp.length() == 9 ||
	 * temp.startsWith(paramNameCode + "_")) && !temp.equals("xml_vdate")) { //
	 * _logger.info(requestData.getParameter(temp) + // "getRequestFormCodeVec:" +
	 * temp); ret[0].add(temp.substring(4));
	 * ret[1].add(defaultParamValue(paramNameUnit));
	 * ret[2].add(defaultParamValue(paramNameAt)); } } return ret; }
	 * 
	 * public List getMRIList(String sDate, String eDate, String status) {
	 * JdbcBillingClaimImpl dbObj = new JdbcBillingClaimImpl(); List ret =
	 * dbObj.getMRIList(sDate, eDate, status); return ret; } // ret - Vector = || ||
	 * public List getProviderBillingStr() { JdbcBillingPageUtil dbObj = new
	 * JdbcBillingPageUtil(); List ret = dbObj.getCurProviderStr(); return ret; } //
	 * default value to 1 if it is empty private String defaultParamValue(String
	 * val) { String ret = "1"; if (val != null && !val.equals("")) { ret = val; }
	 * 
	 * return ret; }
	 */
}
