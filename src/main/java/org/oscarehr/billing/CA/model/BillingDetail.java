package org.oscarehr.billing.CA.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billingdetail")
public class BillingDetail extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="billing_dt_no")
	private Integer id;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_desc")
	private String serviceDesc;

	@Column(name="billing_amount")
	private String billingAmount;

	@Column(name="diagnostic_code")
	private String diagnosticCode;

	@Column(name="appointment_date")
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;

	private String status;

	@Column(name="billingunit")
	private String billingUnit;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getBillingNo() {
    	return billingNo;
    }

	public void setBillingNo(int billingNo) {
    	this.billingNo = billingNo;
    }

	public String getServiceCode() {
    	return serviceCode;
    }

	public void setServiceCode(String serviceCode) {
    	this.serviceCode = serviceCode;
    }

	public String getServiceDesc() {
    	return serviceDesc;
    }

	public void setServiceDesc(String serviceDesc) {
    	this.serviceDesc = serviceDesc;
    }

	public String getBillingAmount() {
    	return billingAmount;
    }

	public void setBillingAmount(String billingAmount) {
    	this.billingAmount = billingAmount;
    }

	public String getDiagnosticCode() {
    	return diagnosticCode;
    }

	public void setDiagnosticCode(String diagnosticCode) {
    	this.diagnosticCode = diagnosticCode;
    }

	public Date getAppointmentDate() {
    	return appointmentDate;
    }

	public void setAppointmentDate(Date appointmentDate) {
    	this.appointmentDate = appointmentDate;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getBillingUnit() {
    	return billingUnit;
    }

	public void setBillingUnit(String billingUnit) {
    	this.billingUnit = billingUnit;
    }


}
