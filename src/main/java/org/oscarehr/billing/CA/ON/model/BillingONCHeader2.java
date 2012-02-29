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

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "billing_on_cheader2")
public class BillingONCHeader2 extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="ch1_id")
	private Integer ch1Id;

	@Column(name="transc_id")
	private String transactionId;

	@Column(name="rec_id")
	private String recordId;

	private String hin;

	@Column(name="last_name")
	private String lastName;

	@Column(name="first_name")
	private String firstName;

	private String sex;

	private String province;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;



	public String getTransactionId() {
    	return transactionId;
    }

	public void setTransactionId(String transactionId) {
    	this.transactionId = transactionId;
    }

	public String getRecordId() {
    	return recordId;
    }

	public void setRecordId(String recordId) {
    	this.recordId = recordId;
    }

	public String getHin() {
    	return hin;
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getLastName() {
    	return lastName;
    }

	public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

	public String getFirstName() {
    	return firstName;
    }

	public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

	public String getSex() {
    	return sex;
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public String getProvince() {
    	return province;
    }

	public void setProvince(String province) {
    	this.province = province;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }

	@Override
    public Integer getId() {
	    return id;
    }

	public Integer getCh1Id() {
    	return ch1Id;
    }

	public void setCh1Id(Integer ch1Id) {
    	this.ch1Id = ch1Id;
    }



}
