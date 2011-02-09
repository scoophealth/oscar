package org.oscarehr.PMmodule.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.OcanStaffForm;

@Entity
public class OcanSubmissionLog extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="submissionId")
	private Integer id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitDateTime;
	
	private String result;
	
	private String resultMessage;
	
	private String submissionData;
	
	private String transactionId;
	
	@Transient
	private List<OcanStaffForm> records = new ArrayList<OcanStaffForm>();
	
	public OcanSubmissionLog() {
		
	}
	
	public Integer getId() {
		return id;
	}

	public Date getSubmitDateTime() {
		return submitDateTime;
	}

	public void setSubmitDateTime(Date submitDateTime) {
		this.submitDateTime = submitDateTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSubmissionData() {
		return submissionData;
	}

	public void setSubmissionData(String submissionData) {
		this.submissionData = submissionData;
	}
	
	public List<OcanStaffForm> getRecords() {
		return records;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	
	
}
