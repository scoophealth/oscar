package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ctl_billingservice")
public class CtlBillingService extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="servicetype_name")
	private String serviceTypeName;
	@Column(name="servicetype")
	private String serviceType;
	@Column(name="service_code")
	private String serviceCode;
	@Column(name="service_group_name")
	private String serviceGroupName;
	@Column(name="service_group")
	private String serviceGroup;
	private String status;
	@Column(name="service_order")
	private int serviceOrder;
	
	@Override
	public Integer getId() {
		return id;
	}


	public String getServiceTypeName() {
    	return serviceTypeName;
    }


	public void setServiceTypeName(String serviceTypeName) {
    	this.serviceTypeName = serviceTypeName;
    }


	public String getServiceType() {
    	return serviceType;
    }


	public void setServiceType(String serviceType) {
    	this.serviceType = serviceType;
    }


	public String getServiceCode() {
    	return serviceCode;
    }


	public void setServiceCode(String serviceCode) {
    	this.serviceCode = serviceCode;
    }


	public String getServiceGroupName() {
    	return serviceGroupName;
    }


	public void setServiceGroupName(String serviceGroupName) {
    	this.serviceGroupName = serviceGroupName;
    }


	public String getServiceGroup() {
    	return serviceGroup;
    }


	public void setServiceGroup(String serviceGroup) {
    	this.serviceGroup = serviceGroup;
    }


	public String getStatus() {
    	return status;
    }


	public void setStatus(String status) {
    	this.status = status;
    }


	public int getServiceOrder() {
    	return serviceOrder;
    }


	public void setServiceOrder(int serviceOrder) {
    	this.serviceOrder = serviceOrder;
    }
	
	
}
