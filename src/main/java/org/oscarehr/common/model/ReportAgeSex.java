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
@Table(name="reportagesex")
public class ReportAgeSex extends AbstractModel<Integer>  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	private int age;

	private String roster;

	private String sex;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="reportdate")
	@Temporal(TemporalType.DATE)
	private Date reportDate;

	private String status;

	@Column(name="date_joined")
	@Temporal(TemporalType.DATE)
	private Date dateJoined;

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

	public int getAge() {
    	return age;
    }

	public void setAge(int age) {
    	this.age = age;
    }

	public String getRoster() {
    	return roster;
    }

	public void setRoster(String roster) {
    	this.roster = roster;
    }

	public String getSex() {
    	return sex;
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getReportDate() {
    	return reportDate;
    }

	public void setReportDate(Date reportDate) {
    	this.reportDate = reportDate;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public Date getDateJoined() {
    	return dateJoined;
    }

	public void setDateJoined(Date dateJoined) {
    	this.dateJoined = dateJoined;
    }


}
