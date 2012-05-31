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
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import oscar.oscarBilling.ca.on.data.JdbcBilling3rdPartImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingCodeImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingCorrection;
import oscar.oscarBilling.ca.on.data.JdbcBillingPageUtil;

public class BillingCorrectionPrep {
	private static final Logger _logger = Logger
			.getLogger(BillingCorrectionPrep.class);

	JdbcBillingCorrection dbObj = new JdbcBillingCorrection();

	// 
	public List getBillingRecordObj(String id) {
		List ret = dbObj.getBillingRecordObj(id);
		return ret;
	}

	// get error code
	public List getBillingExplanatoryList(String id) {
		List ret = dbObj.getBillingExplanatoryList(id);
		return ret;
	}

	// get rejected code
	public List getBillingRejectList(String id) {
		List ret = dbObj.getBillingRejectList(id);
		return ret;
	}	
	
		public void setInactive(String key, HttpServletRequest request) {
			JdbcBilling3rdPartImpl tobj = new JdbcBilling3rdPartImpl();
			String billingNo = request.getParameter("xml_billing_no");
			tobj.updateKeyStatus(billingNo, key, JdbcBilling3rdPartImpl.INACTIVE);
		}

        /*
         * Need to use billing extension table to capture data for invoices in
         * addition to 3rd party bills
         */
        public void updateExt(String key, HttpServletRequest request) {
            update3rdPartyItem(key, request);
        }

        public boolean update3rdPartyItem(String key, HttpServletRequest request) {
		boolean ret = true;
		JdbcBilling3rdPartImpl tobj = new JdbcBilling3rdPartImpl();
		String billingNo = request.getParameter("xml_billing_no");
		if( tobj.keyExists(billingNo, key) ) {
                    ret = tobj.updateKeyValue(billingNo, key, request.getParameter(key));
                }
                else {
                    ret = tobj.add3rdBillExt(billingNo, request.getParameter("demoNo"), key, request.getParameter(key));
                }
		return ret;
	}

	public boolean update3rdPartyItem(HttpServletRequest request) {
		boolean ret = true;
		JdbcBilling3rdPartImpl tobj = new JdbcBilling3rdPartImpl();
		String billingNo = request.getParameter("xml_billing_no");
		tobj.updateKeyValue(billingNo, "payment", request
				.getParameter("payment"));
		tobj
				.updateKeyValue(billingNo, "refund", request
						.getParameter("refund"));
		return ret;
	}

	

	// billing correction
	public String getBillingCodeDesc(String codeName) {
		String ret = null;
		JdbcBillingCodeImpl dbCodeObj = new JdbcBillingCodeImpl();
		List descL = dbCodeObj.getBillingCodeAttr(codeName);
		ret = descL.size() > 1 ? (String) descL.get(1) : "Unknown";
		return ret;
	}

	// billing correction
	public Properties getBillingCodeDesc(List codeName) {
		JdbcBillingCodeImpl dbCodeObj = new JdbcBillingCodeImpl();
		Properties ret = new Properties();
		for (int i = 0; i < codeName.size(); i++) {
			List descL = dbCodeObj.getBillingCodeAttr((String) codeName.get(i));
			String desc = descL.size() > 1 ? (String) descL.get(1) : "Unknown";
			ret.setProperty((String) codeName.get(i), desc);
		}
		return ret;
	}

	// from billing correction
	public List getBillingClaimHeaderObj(String ch1Id) {
		List recordObj = null;
		recordObj = getBillingRecordObj(ch1Id);
		return recordObj;
	}
		
	// for appt unbill; 0 - id, 1 - status
	public List getBillingNoStatusByAppt(String apptNo) {
		List ret = dbObj.getBillingCH1NoStatusByAppt(apptNo);
		return ret;
	}

	public List getBillingNoStatusByBillNo(String billNo) {
	       List ret = dbObj.getBillingCH1NoStatusByBillNo(billNo);
	       return ret;
	}
	
	// for appt unbill;
	public boolean deleteBilling(String id, String status, String providerNo) {
		boolean ret = dbObj.updateBillingStatus(id, status, providerNo);
		return ret;
	}

	public List getFacilty_num() {
		JdbcBillingPageUtil dbPageObj = new JdbcBillingPageUtil();
		List ret = dbPageObj.getFacilty_num();
		return ret;
	}	
}
