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
@Table(name="demographicSets")
public class DesAnnualReviewPlan extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="des_no")
	private Integer id;

	@Column(name="des_date")
	@Temporal(TemporalType.DATE)
	private Date desDate;

	@Column(name="des_time")
	@Temporal(TemporalType.TIME)
	private Date desTime;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="form_no")
	private int formNo;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="risk_content")
	private String riskContent;

	@Column(name="checklist_content")
	private String checklistContent;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public Date getDesDate() {
    	return desDate;
    }

	public void setDesDate(Date desDate) {
    	this.desDate = desDate;
    }

	public Date getDesTime() {
    	return desTime;
    }

	public void setDesTime(Date desTime) {
    	this.desTime = desTime;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public int getFormNo() {
    	return formNo;
    }

	public void setFormNo(int formNo) {
    	this.formNo = formNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getRiskContent() {
    	return riskContent;
    }

	public void setRiskContent(String riskContent) {
    	this.riskContent = riskContent;
    }

	public String getChecklistContent() {
    	return checklistContent;
    }

	public void setChecklistContent(String checklistContent) {
    	this.checklistContent = checklistContent;
    }


}
