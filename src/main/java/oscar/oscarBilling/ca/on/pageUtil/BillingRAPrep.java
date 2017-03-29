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

	public List<Properties> getRAErrorReport(String raNo, String providerOhipNo, String[] notErrorCode) {
		List<Properties> ret = dbObj.getRAErrorReport(raNo, providerOhipNo, notErrorCode);
		return ret;
	}

	public List<String> getRABillingNo4Code(String raNo, String codes) {
		List<String> ret = dbObj.getRABillingNo4Code(raNo, codes);
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
		BigDecimal BigCTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigPTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigOBTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigCOTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigHTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigLocalHTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigOTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal BigLTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		// Billing No Provider Patient HIN Service Date Service Code Invoiced :
		// new BigDecimal(0)
		// Paid Clinic Pay Hospital Pay OB Error
		for (int j = 0; j < ret.size(); j++) {
			Properties prop = (Properties) ret.get(j);	
			String servicedate = prop.getProperty("servicedate");
			servicedate = servicedate.length() == 8 ? (servicedate.substring(0, 4) + "-" + servicedate.substring(4, 6)
					+ "-" + servicedate.substring(6)) : servicedate;
			prop.setProperty("servicedate", servicedate);
			
			String explain = prop.getProperty("explain");
			String amountsubmit = prop.getProperty("amountsubmit");
			String amountpay = prop.getProperty("amountpay");
			String location = prop.getProperty("location");
			String localServiceDate = prop.getProperty("localServiceDate");
			
			String demo_hin = prop.getProperty("demo_hin");
			String account = prop.getProperty("account");
			String clinicPay = "";
			String hospitalPay = "";
			String obPay = "";

			dCFee = Double.parseDouble(amountsubmit);
			BigDecimal bdCFee = new BigDecimal(amountsubmit).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigCTotal = BigCTotal.add(bdCFee);

			dPFee = Double.parseDouble(amountpay);
			BigDecimal bdPFee = new BigDecimal(amountpay).setScale(2, BigDecimal.ROUND_HALF_UP);
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
				BigDecimal bdOBFee = new BigDecimal(amountOB).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigOBTotal = BigOBTotal.add(bdOBFee);
                                obPay = amountpay;
			} else {
				obPay = "N/A";
				// amountOB = "N/A";
			}

			if (COflag.equals("1")) {
				String amountCO = amountpay;
				double dCOFee = Double.parseDouble(amountCO);
				BigDecimal bdCOFee = new BigDecimal(amountCO).setScale(2, BigDecimal.ROUND_HALF_UP);
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
				BigDecimal bdHFee = new BigDecimal(amountpay).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigHTotal = BigHTotal.add(bdHFee);
				clinicPay = "N/A";
				hospitalPay = "N/A";

				// is local for hospital
				if (demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
					BigLocalHTotal = BigLocalHTotal.add(bdHFee);
					hospitalPay = amountpay;
				}
			} else {
				if (location.compareTo("00") == 0 && demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
					double dFee = Double.parseDouble(amountpay);
					BigDecimal bdFee = new BigDecimal(amountpay).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigTotal = BigTotal.add(bdFee);
					clinicPay = amountpay;
					hospitalPay = "N/A";
				} else {
					double dOFee = Double.parseDouble(amountpay);
					BigDecimal bdOFee = new BigDecimal(amountpay).setScale(2, BigDecimal.ROUND_HALF_UP);
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
}
