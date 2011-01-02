package oscar.oscarBilling.ca.on.pageUtil;

import java.util.Date;
import java.util.List;

/**
* @author Eugene Katyukhin
*/
public class PatientEndYearStatementInvoiceBean {
	private Date invoiceDate;
	private int invoiceNo;
	private String invoiced;
	private String paid;
	private List<PatientEndYearStatementServiceBean> services;
	
	public PatientEndYearStatementInvoiceBean() { }

	public PatientEndYearStatementInvoiceBean(int invoiceNo, Date invoiceDate,
			String invoiced, String paid) {
		super();
		this.invoiceDate = invoiceDate;
		this.invoiceNo = invoiceNo;
		this.invoiced = invoiced;
		this.paid = paid;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(int invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public List<PatientEndYearStatementServiceBean> getServices() {
		return services;
	}

	public void setServices(List<PatientEndYearStatementServiceBean> services) {
		this.services = services;
	}

}
