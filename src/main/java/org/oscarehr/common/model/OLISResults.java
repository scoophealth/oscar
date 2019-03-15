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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OLISResults extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String requestingHICProviderNo;
	
	private String queryType;
	
	private String results;
	
	private String hash;
	
	private String status;
	
	private String uuid;
	
	private String query;
	
	private String providerNo;
	
	private Integer demographicNo;
	
	private String queryUuid;
	
	public Integer getId() {
		return id;
	}


	public String getProviderNo() {
		return providerNo;
	}


	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}


	public String getQueryType() {
		return queryType;
	}


	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}


	public String getResults() {
		return results;
	}


	public void setResults(String results) {
		this.results = results;
	}


	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getRequestingHICProviderNo() {
		return requestingHICProviderNo;
	}


	public void setRequestingHICProviderNo(String requestingHICProviderNo) {
		this.requestingHICProviderNo = requestingHICProviderNo;
	}


	public Integer getDemographicNo() {
		return demographicNo;
	}


	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}


	public String getQueryUuid() {
		return queryUuid;
	}


	public void setQueryUuid(String queryUuid) {
		this.queryUuid = queryUuid;
	}
	
	
	
}
