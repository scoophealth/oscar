/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

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
	
	private String submissionType;
	
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

	public String getSubmissionType() {
    	return submissionType;
    }

	public void setSubmissionType(String submissionType) {
    	this.submissionType = submissionType;
    }
	
	
	
}
