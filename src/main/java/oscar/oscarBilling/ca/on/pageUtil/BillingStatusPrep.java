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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import oscar.oscarBilling.ca.on.data.BillingClaimHeader1Data;
import oscar.oscarBilling.ca.on.data.JdbcBillingReviewImpl;

public class BillingStatusPrep {
	//private static final Logger _logger = Logger.getLogger(BillingStatusPrep.class);
	private static final String ANY_PROVIDER = "all";
	private static final String ANY_STATUS_TYPE = "%";
	private static final String ANY_SERVICE_CODE = "%";
	private static final String ANY_BILLING_FORM = "---";
	public static final String ANY_VISIT_LOCATION = "0000";

	// JdbcBillingRAImpl dbObj = new JdbcBillingRAImpl();

	
	
	public List<BillingClaimHeader1Data> getBills(String[] billTypes, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String visitLocation,String paymentStartDate, String paymentEndDate) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		
		billTypes = billTypes == null || billTypes.length == 0 ? null : billTypes;
		statusType = statusType == null || statusType.length() == 0 || statusType.equals(ANY_STATUS_TYPE) ? null : statusType;
		providerNo = providerNo == null || providerNo.length() == 0 || providerNo.equals(ANY_PROVIDER) ? null : providerNo;
		startDate = startDate == null || startDate.length() == 0 ? null : startDate;
		endDate = endDate == null || endDate.length() == 0 ? null : endDate;
		demoNo = demoNo == null || demoNo.length() == 0 ? null : demoNo;
		visitLocation = visitLocation == null || visitLocation.length() == 0 || visitLocation.equals(ANY_VISIT_LOCATION) ? null : visitLocation;
		paymentStartDate = paymentStartDate == null || paymentStartDate.length() == 0 ? null : paymentStartDate;
		paymentEndDate = paymentEndDate == null || paymentEndDate.length() == 0 ? null : paymentEndDate;
		
		return bObj.getBill(billTypes, statusType, providerNo, startDate, endDate, demoNo, visitLocation, paymentStartDate,paymentEndDate);
	}

	
	public List<BillingClaimHeader1Data> getBills(String[] billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCodeParams, String dx, String visitType, String billingForm, String visitLocation, String paymentStartDate, String paymentEndDate) {
		return getBillsWithSorting(billType,statusType,providerNo,startDate,endDate,demoNo,serviceCodeParams,dx,visitType,billingForm,visitLocation,null,null, paymentStartDate,paymentEndDate);
	}
	
	public List<BillingClaimHeader1Data> getBillsWithSorting(String[] billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCodeParams, String dx, String visitType, String billingForm, String visitLocation, String sortName, String sortOrder,
			String paymentStartDate, String paymentEndDate) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		billType = billType == null || billType.length == 0 ? null : billType;
		statusType = statusType == null || statusType.length() == 0 || statusType.equals(ANY_STATUS_TYPE) ? null : statusType;
		providerNo = providerNo == null || providerNo.length() == 0 || providerNo.equals(ANY_PROVIDER) ? null : providerNo;
		startDate = startDate == null || startDate.length() == 0 ? null : startDate;
		endDate = endDate == null || endDate.length() == 0 ? null : endDate;
		demoNo = demoNo == null || demoNo.length() == 0 ? null : demoNo;
		dx = dx == null || dx.length() < 2 ? null : dx;
		visitType = visitType == null || visitType.length() < 2 ? null : visitType;
		serviceCodeParams = serviceCodeParams == null || serviceCodeParams.length() == 0 || serviceCodeParams.equals(ANY_SERVICE_CODE) ? null : 
			serviceCodeParams.toUpperCase();
		billingForm = billingForm == null || billingForm.length() == 0 || billingForm.equals(ANY_BILLING_FORM) ? null : billingForm;
		visitLocation = visitLocation == null || visitLocation.length() == 0 || visitLocation.equals(ANY_VISIT_LOCATION) ? null : visitLocation;

		paymentStartDate = paymentStartDate == null || paymentStartDate.length() == 0 ? null : paymentStartDate;
		paymentEndDate = paymentEndDate == null || paymentEndDate.length() == 0 ? null : paymentEndDate;
		
		List<String> serviceCodeList = bObj.mergeServiceCodes(serviceCodeParams, billingForm);
		List<BillingClaimHeader1Data> retval = bObj.getBillWithSorting(billType, statusType, providerNo, startDate, endDate, demoNo, serviceCodeList, dx, visitType, visitLocation,sortName,sortOrder,paymentStartDate,paymentEndDate);
		return retval;
	}

	
	public List<LabelValueBean> listBillingForms() {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
        List<LabelValueBean> billingFormsList = bObj.listBillingForms();
        if(billingFormsList == null) billingFormsList = new ArrayList<LabelValueBean>();
		return billingFormsList;
	}
}
