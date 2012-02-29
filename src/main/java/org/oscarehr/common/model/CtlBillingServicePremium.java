package org.oscarehr.common.model;

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
@Table(name="ctl_billingservice_premium")
public class CtlBillingServicePremium extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="servicetype_name")
	private String serviceTypeName;

	@Column(name="service_code")
	private String serviceCode;

	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name="update_date")
	private Date updateDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getServiceTypeName() {
    	return serviceTypeName;
    }

	public void setServiceTypeName(String serviceTypeName) {
    	this.serviceTypeName = serviceTypeName;
    }

	public String getServiceCode() {
    	return serviceCode;
    }

	public void setServiceCode(String serviceCode) {
    	this.serviceCode = serviceCode;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public Date getUpdateDate() {
    	return updateDate;
    }

	public void setUpdateDate(Date updateDate) {
    	this.updateDate = updateDate;
    }




}
