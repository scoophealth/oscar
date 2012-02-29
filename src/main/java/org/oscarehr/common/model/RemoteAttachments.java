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
@Table(name="remoteAttachments")
public class RemoteAttachments extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="messageid")
	private int messageId;

	private String savedBy;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Temporal(TemporalType.TIME)
	private Date time;

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

	public int getMessageId() {
    	return messageId;
    }

	public void setMessageId(int messageId) {
    	this.messageId = messageId;
    }

	public String getSavedBy() {
    	return savedBy;
    }

	public void setSavedBy(String savedBy) {
    	this.savedBy = savedBy;
    }

	public Date getDate() {
    	return date;
    }

	public void setDate(Date date) {
    	this.date = date;
    }

	public Date getTime() {
    	return time;
    }

	public void setTime(Date time) {
    	this.time = time;
    }


}
