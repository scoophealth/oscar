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
public class OLISQueryLog extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String initiatingProviderNo;
	
	private String queryType;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date queryExecutionDate;
	
	private String uuid;

	private String requestingHIC;
	
	private Integer demographicNo;
	

	public Integer getId() {
		return id;
	}


	public String getInitiatingProviderNo() {
		return initiatingProviderNo;
	}


	public void setInitiatingProviderNo(String initiatingProviderNo) {
		this.initiatingProviderNo = initiatingProviderNo;
	}


	public String getQueryType() {
		return queryType;
	}


	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}


	public Date getQueryExecutionDate() {
		return queryExecutionDate;
	}


	public void setQueryExecutionDate(Date queryExecutionDate) {
		this.queryExecutionDate = queryExecutionDate;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getRequestingHIC() {
		return requestingHIC;
	}


	public void setRequestingHIC(String requestingHIC) {
		this.requestingHIC = requestingHIC;
	}


	public Integer getDemographicNo() {
		return demographicNo;
	}


	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	
}
