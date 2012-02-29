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
@Table(name="fileUploadCheck")
public class FileUploadCheck extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="encounter_no")
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	private String filename;

	private String md5sum;

	@Column(name="date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

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

	public String getFilename() {
    	return filename;
    }

	public void setFilename(String filename) {
    	this.filename = filename;
    }

	public String getMd5sum() {
    	return md5sum;
    }

	public void setMd5sum(String md5sum) {
    	this.md5sum = md5sum;
    }

	public Date getDateTime() {
    	return dateTime;
    }

	public void setDateTime(Date dateTime) {
    	this.dateTime = dateTime;
    }


}
