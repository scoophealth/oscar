package org.oscarehr.hospitalReportManager.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMDocumentComment extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String providerNo;
	private Integer hrmDocumentId;
	private String comment;
	private Date commentTime;
	private Boolean deleted = false;
	
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

	public Integer getHrmDocumentId() {
    	return hrmDocumentId;
    }

	public void setHrmDocumentId(Integer hrmDocumentId) {
    	this.hrmDocumentId = hrmDocumentId;
    }

	public String getComment() {
    	return comment;
    }

	public void setComment(String comment) {
    	this.comment = comment;
    }

	public Date getCommentTime() {
    	return commentTime;
    }

	public void setCommentTime(Date commentTime) {
    	this.commentTime = commentTime;
    }

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Boolean isDeleted() {
		return this.deleted;
	}
	
}
