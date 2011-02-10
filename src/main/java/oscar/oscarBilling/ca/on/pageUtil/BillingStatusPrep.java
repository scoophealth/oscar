package oscar.oscarBilling.ca.on.pageUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import oscar.oscarBilling.ca.on.data.JdbcBillingReviewImpl;

public class BillingStatusPrep {
	private static final Logger _logger = Logger.getLogger(BillingStatusPrep.class);
	private static final String ANY_PROVIDER = "all";
	private static final String ANY_STATUS_TYPE = "%";
	private static final String ANY_SERVICE_CODE = "%";
	private static final String ANY_BILLING_FORM = "---";

	// JdbcBillingRAImpl dbObj = new JdbcBillingRAImpl();

	public List getBills(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		billType = billType == null || billType.length() == 0 ? "" : " and pay_program in (" + billType + ")";
		statusType = statusType == null || statusType.length() == 0 || statusType.equals(ANY_STATUS_TYPE) ? "" : " and status = '"
				+ statusType + "'";
		providerNo = providerNo == null || providerNo.length() == 0 || providerNo.equals(ANY_PROVIDER) ? "" : " and provider_no ='" + providerNo + "'";
		startDate = startDate == null || startDate.length() == 0 ? "" : " and billing_date >= '" + startDate + "'";
		endDate = endDate == null || endDate.length() == 0 ? "" : " and billing_date <= '" + endDate + "'";
		demoNo = demoNo == null || demoNo.length() == 0 ? "" : " and demographic_no=" + demoNo;
		List retval = bObj.getBill(billType, statusType, providerNo, startDate, endDate, demoNo);

		return retval;
	}

	public List getBills(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCodeParams, String dx, String visitType, String billingForm) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		billType = billType == null || billType.length() == 0 ? "" : " and ch1.pay_program in (" + billType + ")";
		statusType = statusType == null || statusType.length() == 0 || statusType.equals(ANY_STATUS_TYPE) ? "" : " and ch1.status = '"
				+ statusType + "'";
		providerNo = providerNo == null || providerNo.length() == 0 || providerNo.equals(ANY_PROVIDER) ? "" : " and ch1.provider_no ='" + providerNo + "'";
		startDate = startDate == null || startDate.length() == 0 ? "" : " and ch1.billing_date >= '" + startDate + "'";
		endDate = endDate == null || endDate.length() == 0 ? "" : " and ch1.billing_date <= '" + endDate + "'";
		demoNo = demoNo == null || demoNo.length() == 0 ? "" : " and ch1.demographic_no=" + demoNo;
		dx = dx == null || dx.length() < 2 ? "" : " and ch1.dx='" + dx + "'";
		visitType = visitType == null || visitType.length() < 2 ? "" : " and ch1.visittype='" + visitType + "'";
		serviceCodeParams = serviceCodeParams == null || serviceCodeParams.length() == 0 || serviceCodeParams.equals(ANY_SERVICE_CODE) ? "" : serviceCodeParams.toUpperCase();
		billingForm = billingForm == null || billingForm.length() == 0 || billingForm.equals(ANY_BILLING_FORM) ? "" : billingForm;

		List<String> serviceCodeList = bObj.mergeServiceCodes(serviceCodeParams, billingForm);
		String serviceCodes = "";
		if(serviceCodeList  != null && serviceCodeList.size()>0) {
			for(String serviceCode : serviceCodeList) {
				if(serviceCodes.length() == 0) serviceCodes = " and (" +serviceCode;
				else serviceCodes += " or " + serviceCode;
			}
			serviceCodes += ")";
		}

		List retval = bObj.getBill(billType, statusType, providerNo, startDate, endDate, demoNo, serviceCodes, dx,
				visitType);

		return retval;
	}

	public List getBills(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCode, String dx, String visitType) {
			return getBills(billType, statusType, providerNo, startDate, endDate,
					demoNo, serviceCode, dx, visitType, null);
	}
	public List<LabelValueBean> listBillingForms() {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
        List<LabelValueBean> billingFormsList = bObj.listBillingForms();
        if(billingFormsList == null) billingFormsList = new ArrayList<LabelValueBean>();
		return billingFormsList;
	}
}
