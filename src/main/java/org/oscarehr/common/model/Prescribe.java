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
@Table(name="prescribe")
public class Prescribe extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="prescribe_no")
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="prescribe_date")
	@Temporal(TemporalType.DATE)
	private Date prescribeDate;

	@Column(name="prescribe_time")
	@Temporal(TemporalType.TIME)
	private Date prescribeTime;

	private String content;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getPrescribeDate() {
    	return prescribeDate;
    }

	public void setPrescribeDate(Date prescribeDate) {
    	this.prescribeDate = prescribeDate;
    }

	public Date getPrescribeTime() {
    	return prescribeTime;
    }

	public void setPrescribeTime(Date prescribeTime) {
    	this.prescribeTime = prescribeTime;
    }

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }


}
