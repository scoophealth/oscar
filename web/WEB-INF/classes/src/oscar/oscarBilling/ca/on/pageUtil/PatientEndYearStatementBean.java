package oscar.oscarBilling.ca.on.pageUtil;

import java.util.Date;

public class PatientEndYearStatementBean {
	private String patientName;
	private String patientNo;
	private String hin;
	private String address;
	private String phone;
	private String invoiced = "0.00";
	private String paid = "0.00";
	private String count = "0";
	private Date fromDate;
	private Date toDate;
//	private String fromDateParam;
//	private String todateParam;
	
	public PatientEndYearStatementBean(String patientName, String patientNo, String hin,
			String address, String phone) {
		super();
		this.patientName = patientName;
		this.patientNo = patientNo;
		this.hin = hin;
		this.address = address;
		this.phone = phone;
	}
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getHin() {
		return hin;
	}
	public void setHin(String hin) {
		this.hin = hin;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getInvoiced() {
		return invoiced;
	}

	public void setInvoiced(String invoiced) {
		this.invoiced = invoiced;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
/*
	public String getFromDateParam() {
		return fromDateParam;
	}

	public void setFromDateParam(String fromDateParam) {
		this.fromDateParam = fromDateParam;
	}

	public String getTodateParam() {
		return todateParam;
	}

	public void setTodateParam(String todateParam) {
		this.todateParam = todateParam;
	}
*/
}
