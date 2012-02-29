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
@Table(name="studylogin")
public class StudyLogin extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="study_no")
	private int studyNo;

	@Column(name="remote_login_url")
	private String remoteLoginUrl;

	@Column(name="url_name_username")
	private String urlNameUsername;

	@Column(name="url_name_password")
	private String urlNamePassword;

	private String username;

	private String password;

	private int current1;

	private String creator;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getStudyNo() {
    	return studyNo;
    }

	public void setStudyNo(int studyNo) {
    	this.studyNo = studyNo;
    }

	public String getRemoteLoginUrl() {
    	return remoteLoginUrl;
    }

	public void setRemoteLoginUrl(String remoteLoginUrl) {
    	this.remoteLoginUrl = remoteLoginUrl;
    }

	public String getUrlNameUsername() {
    	return urlNameUsername;
    }

	public void setUrlNameUsername(String urlNameUsername) {
    	this.urlNameUsername = urlNameUsername;
    }

	public String getUrlNamePassword() {
    	return urlNamePassword;
    }

	public void setUrlNamePassword(String urlNamePassword) {
    	this.urlNamePassword = urlNamePassword;
    }

	public String getUsername() {
    	return username;
    }

	public void setUsername(String username) {
    	this.username = username;
    }

	public String getPassword() {
    	return password;
    }

	public void setPassword(String password) {
    	this.password = password;
    }

	public int getCurrent1() {
    	return current1;
    }

	public void setCurrent1(int current1) {
    	this.current1 = current1;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }


}
