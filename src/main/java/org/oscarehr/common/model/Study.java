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
@Table(name="study")
public class Study extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="study_no")
	private Integer id;

	@Column(name="study_name")
	private String studyName;

	@Column(name="study_link")
	private String studyLink;

	private String description;

	@Column(name="form_name")
	private String formName;

	private int current1;

	@Column(name="remote_serverurl")
	private String remoteServerUrl;

	@Column(name="provider_no")
	private String providerNo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getStudyName() {
    	return studyName;
    }

	public void setStudyName(String studyName) {
    	this.studyName = studyName;
    }

	public String getStudyLink() {
    	return studyLink;
    }

	public void setStudyLink(String studyLink) {
    	this.studyLink = studyLink;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getFormName() {
    	return formName;
    }

	public void setFormName(String formName) {
    	this.formName = formName;
    }

	public int getCurrent1() {
    	return current1;
    }

	public void setCurrent1(int current1) {
    	this.current1 = current1;
    }

	public String getRemoteServerUrl() {
    	return remoteServerUrl;
    }

	public void setRemoteServerUrl(String remoteServerUrl) {
    	this.remoteServerUrl = remoteServerUrl;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }




}
