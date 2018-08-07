/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DHIRSubmissionLog extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private Integer demographicNo;
	
	private Integer preventionId;
	
	private String submitterProviderNo;
	
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	private String transactionId;
	
	private String bundleId;
	
	private String response;
	
	private String clientRequestId;
	
	private String clientResponseId;
	
	@Override
	public Integer getId() {
		return id;
	}



	public Integer getDemographicNo() {
		return demographicNo;
	}



	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}



	public Integer getPreventionId() {
		return preventionId;
	}



	public void setPreventionId(Integer preventionId) {
		this.preventionId = preventionId;
	}



	public String getSubmitterProviderNo() {
		return submitterProviderNo;
	}



	public void setSubmitterProviderNo(String submitterProviderNo) {
		this.submitterProviderNo = submitterProviderNo;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Date getDateCreated() {
		return dateCreated;
	}



	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}



	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	public String getBundleId() {
		return bundleId;
	}



	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}



	public String getResponse() {
		return response;
	}



	public void setResponse(String response) {
		this.response = response;
	}



	public String getClientRequestId() {
		return clientRequestId;
	}



	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}



	public String getClientResponseId() {
		return clientResponseId;
	}



	public void setClientResponseId(String clientResponseId) {
		this.clientResponseId = clientResponseId;
	}
	
	
	
	
}
