package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="messagelisttbl")
public class MessageList extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private long message;

	@Column(name="provider_no")
	private String providerNo;

	private String status;

	private int remoteLocation;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public long getMessage() {
    	return message;
    }

	public void setMessage(long message) {
    	this.message = message;
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

	public int getRemoteLocation() {
    	return remoteLocation;
    }

	public void setRemoteLocation(int remoteLocation) {
    	this.remoteLocation = remoteLocation;
    }



}
