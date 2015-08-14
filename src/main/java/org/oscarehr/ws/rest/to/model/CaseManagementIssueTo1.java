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
package org.oscarehr.ws.rest.to.model;

import java.util.Date;

import org.oscarehr.casemgmt.model.Issue;

public class CaseManagementIssueTo1 {

	protected Long id;
	protected String demographic_no;
	protected long issue_id;
	protected boolean acute;
	protected boolean certain;
	protected boolean major;
	protected boolean resolved;
	protected String type;
	protected Date update_date = new Date();
	protected Issue issue;
	protected Integer program_id = null;
	
	protected boolean unchecked=false;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public long getIssue_id() {
		return issue_id;
	}
	public void setIssue_id(long issue_id) {
		this.issue_id = issue_id;
	}
	public boolean isAcute() {
		return acute;
	}
	public void setAcute(boolean acute) {
		this.acute = acute;
	}
	public boolean isCertain() {
		return certain;
	}
	public void setCertain(boolean certain) {
		this.certain = certain;
	}
	public boolean isMajor() {
		return major;
	}
	public void setMajor(boolean major) {
		this.major = major;
	}
	public boolean isResolved() {
		return resolved;
	}
	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public Integer getProgram_id() {
		return program_id;
	}
	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}
	public boolean isUnchecked() {
		return unchecked;
	}
	public void setUnchecked(boolean unchecked) {
		this.unchecked = unchecked;
	}
	
	

}
