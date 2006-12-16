package org.oscarehr.survey.model.oscar;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class OscarFormInstance {
	private Long id;
	private long formId;
	private String description;
	private Date dateCreated;
	private long userId;
	private String username;
	private long clientId;
	private Set data = new HashSet();
	
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public Set getData() {
		return data;
	}
	public void setData(Set data) {
		this.data = data;
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
	public long getFormId() {
		return formId;
	}
	public void setFormId(long formId) {
		this.formId = formId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
