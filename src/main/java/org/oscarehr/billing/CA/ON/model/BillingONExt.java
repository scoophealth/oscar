package org.oscarehr.billing.CA.ON.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="billing_on_ext")
public class BillingONExt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="key_val")
	private String keyVal;

	private String value;

	@Column(name="date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

	private String status;

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

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getKeyVal() {
    	return keyVal;
    }

	public void setKeyVal(String keyVal) {
    	this.keyVal = keyVal;
    }

	public String getValue() {
    	return value;
    }

	public void setValue(String value) {
    	this.value = value;
    }

	public Date getDateTime() {
    	return dateTime;
    }

	public void setDateTime(Date dateTime) {
    	this.dateTime = dateTime;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }


}
