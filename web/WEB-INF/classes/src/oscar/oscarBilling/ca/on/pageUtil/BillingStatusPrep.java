package oscar.oscarBilling.ca.on.pageUtil;

import java.util.List;

import org.apache.log4j.Logger;

import oscar.oscarBilling.ca.on.data.JdbcBillingReviewImpl;

public class BillingStatusPrep {
	private static final Logger _logger = Logger.getLogger(BillingStatusPrep.class);

	// JdbcBillingRAImpl dbObj = new JdbcBillingRAImpl();

	public List getBills(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		billType = billType == null || billType.length() == 0 ? "" : " and pay_program in (" + billType + ")";
		statusType = statusType == null || statusType.length() == 0 || statusType.equals("%") ? "" : " and status = '"
				+ statusType + "'";
		providerNo = providerNo == null || providerNo.length() == 0 ? "" : " and provider_no ='" + providerNo + "'";
		startDate = startDate == null || startDate.length() == 0 ? "" : " and billing_date >= '" + startDate + "'";
		endDate = endDate == null || endDate.length() == 0 ? "" : " and billing_date <= '" + endDate + "'";
		demoNo = demoNo == null || demoNo.length() == 0 ? "" : " and demographic_no=" + demoNo;
		List retval = bObj.getBill(billType, statusType, providerNo, startDate, endDate, demoNo);

		return retval;
	}

	public List getBills(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCode, String dx, String visitType) {
		JdbcBillingReviewImpl bObj = new JdbcBillingReviewImpl();
		billType = billType == null || billType.length() == 0 ? "" : " and pay_program in (" + billType + ")";
		statusType = statusType == null || statusType.length() == 0 || statusType.equals("%") ? "" : " and status = '"
				+ statusType + "'";
		providerNo = providerNo == null || providerNo.length() == 0 ? "" : " and provider_no ='" + providerNo + "'";
		startDate = startDate == null || startDate.length() == 0 ? "" : " and billing_date >= '" + startDate + "'";
		endDate = endDate == null || endDate.length() == 0 ? "" : " and billing_date <= '" + endDate + "'";
		demoNo = demoNo == null || demoNo.length() == 0 ? "" : " and demographic_no=" + demoNo;
		dx = dx == null || dx.length() < 2 ? "" : " and dx='" + dx + "'";
		visitType = visitType == null || visitType.length() < 2 ? "" : " and visittype='" + visitType + "'";
		List retval = bObj.getBill(billType, statusType, providerNo, startDate, endDate, demoNo, serviceCode, dx,
				visitType);

		return retval;
	}
}
