package org.oscarehr.billing.CA.ON.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billing_on_3rdPartyAddress")
public class Billing3rdPartyAddress extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String attention;

	@Column(name="company_name")
	private String companyName;

	private String address;

	private String city;

	private String province;

	@Column(name="postcode")
	private String postalCode;

	private String telephone;

	private String fax;

	@Override
    public Integer getId() {
		return id;
	}

	public String getAttention() {
    	return attention;
    }

	public void setAttention(String attention) {
    	this.attention = attention;
    }

	public String getCompanyName() {
    	return companyName;
    }

	public void setCompanyName(String companyName) {
    	this.companyName = companyName;
    }

	public String getAddress() {
    	return address;
    }

	public void setAddress(String address) {
    	this.address = address;
    }

	public String getCity() {
    	return city;
    }

	public void setCity(String city) {
    	this.city = city;
    }

	public String getProvince() {
    	return province;
    }

	public void setProvince(String province) {
    	this.province = province;
    }

	public String getPostalCode() {
    	return postalCode;
    }

	public void setPostalCode(String postalCode) {
    	this.postalCode = postalCode;
    }

	public String getTelephone() {
    	return telephone;
    }

	public void setTelephone(String telephone) {
    	this.telephone = telephone;
    }

	public String getFax() {
    	return fax;
    }

	public void setFax(String fax) {
    	this.fax = fax;
    }


}
