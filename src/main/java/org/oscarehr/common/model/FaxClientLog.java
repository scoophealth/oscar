package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class FaxClientLog extends AbstractModel<Integer>{
	@Column(name="faxLogId")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String providerNo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	private String result;
	private String requestId;
	private String faxId;


	@Override
    public Integer getId() {
	   return id;
    }


	public String getProviderNo() {
    	return providerNo;
    }


	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }


	public Date getStartTime() {
    	return startTime;
    }


	public void setStartTime(Date startTime) {
    	this.startTime = startTime;
    }


	public Date getEndTime() {
    	return endTime;
    }


	public void setEndTime(Date endTime) {
    	this.endTime = endTime;
    }


	public String getResult() {
    	return result;
    }


	public void setResult(String result) {
    	this.result = result;
    }


	public String getRequestId() {
    	return requestId;
    }


	public void setRequestId(String requestId) {
    	this.requestId = requestId;
    }


	public String getFaxId() {
    	return faxId;
    }


	public void setFaxId(String faxId) {
    	this.faxId = faxId;
    }



}
