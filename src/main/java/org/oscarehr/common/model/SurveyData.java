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
@Table(name="surveyData")
public class SurveyData extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="surveyDataId")
	private Integer id;

	private String surveyId;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="provider_no")
	private String providerNo;

	private String status;

	@Column(name="survey_date")
	@Temporal(TemporalType.DATE)
	private Date surveyDate;

	private String answer;

	private int processed;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSurveyId() {
    	return surveyId;
    }

	public void setSurveyId(String surveyId) {
    	this.surveyId = surveyId;
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

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public Date getSurveyDate() {
    	return surveyDate;
    }

	public void setSurveyDate(Date surveyDate) {
    	this.surveyDate = surveyDate;
    }

	public String getAnswer() {
    	return answer;
    }

	public void setAnswer(String answer) {
    	this.answer = answer;
    }

	public int getProcessed() {
    	return processed;
    }

	public void setProcessed(int processed) {
    	this.processed = processed;
    }


}
