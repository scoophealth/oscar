package org.oscarehr.survey.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SurveyTestInstance extends BaseObject {
	private Long id;
	private long surveyId;
	private Date dateCreated;
	private long userId;
	private long clientId;
	private Set data = new HashSet();
	
	public Set getData() {
		return data;
	}
	public void setData(Set data) {
		this.data = data;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
