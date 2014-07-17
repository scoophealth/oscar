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
package org.oscarehr.ticklers.web;

import org.oscarehr.common.PaginationQuery;

public class TicklerQuery extends PaginationQuery {
	private static final long serialVersionUID = 5994830654027801723L;

	private String startDateStr;
	private String endDateStr;
	private String status = "A";
	private String mrp;	
	private String assignee;
	private String demographicNo;
	private String providerNo;
	private String withOption;
	
	private String provider;
	private String message;
	private String client;
	private String programId;
	private String[] providers;
	private String[] assignees;
	private String[] sites;
	private String[] mrps;

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
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getMrp() {
		return mrp;
	}
	public void setMrp(String mrp) {
		this.mrp = mrp;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String[] getMrps() {
		return mrps;
	}
	public void setMrps(String[] mrps) {
		this.mrps = mrps;
	}
	public String[] getProviders() {
		return providers;
	}
	public void setProviders(String[] providers) {
		this.providers = providers;
	}
	public String[] getAssignees() {
		return assignees;
	}
	public void setAssignees(String[] assignees) {
		this.assignees = assignees;
	}
	public String[] getSites() {
		return sites;
	}
	public void setSites(String[] sites) {
		this.sites = sites;
	}
	public String getWithOption() {
		return withOption;
	}
	public void setWithOption(String withOption) {
		this.withOption = withOption;
	}
}