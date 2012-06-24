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

package org.caisi.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import java.util.Locale;
import org.oscarehr.util.LocaleUtils;

public class Tickler extends BaseObject {
	private Long tickler_no;
	private String demographic_no;
	private Integer program_id;
	private String message;
	private char status;
	private Date update_date;
	private Date service_date;
	private String creator;
	private String priority;
	private String task_assigned_to;
	private String task_assigned_to_name;
	private Set comments = new HashSet();
	private Set updates = new HashSet();
	private Demographic demographic;
	private Provider provider;
	private Provider assignee;
	private Program program;
	
	private String demographic_webName;
	
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Date getService_date() {
		return service_date;
	}
	public void setService_date(Date service_date) {
		this.service_date = service_date;
	}
        public String getStatusDesc(Locale locale) {
            String statusStr = "";            
            if (this.status == 'A'){
                statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stActive");
            }
            else if (this.status == 'C') {               
                statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stComplete");
            }
            else if (this.status == 'D') {                
                statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stDeleted");
            }
            return statusStr;
        }
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getTask_assigned_to() {
		return task_assigned_to;
	}
	public void setTask_assigned_to(String task_assigned_to) {
		this.task_assigned_to = task_assigned_to;
	}
	public Long getTickler_no() {
		return tickler_no;
	}
	public void setTickler_no(Long tickler_no) {
		this.tickler_no = tickler_no;
	}
        public String getUpdateDate() {
		if(getUpdate_date() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getUpdate_date());
		}
		return "";
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Set getComments() {
		return comments;
	}
	public void setComments(Set comments) {
		this.comments = comments;
	}
	public Set getUpdates() {
		return updates;
	}
	public void setUpdates(Set updates) {
		this.updates = updates;
	}
	public Demographic getDemographic() {
		return demographic;
	}
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public Provider getAssignee() {
		return assignee;
	}
	public void setAssignee(Provider assignee) {
		this.assignee = assignee;
	}
	
	
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	
	/* have to do this */
	public void setServiceDate(String data) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setService_date(formatter.parse(data));
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid service date, use yyyy-MM-dd");
		}
	}
	
	public String getServiceDate() {
		if(getService_date() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getService_date());
		}
		return "";
	}
	public String getDemographic_webName() {
		return demographic_webName;
	}
	public void setDemographic_webName(String demographic_webName) {
		this.demographic_webName = demographic_webName;
	}
	public String getTask_assigned_to_name() {
		return task_assigned_to_name;
	}
	public void setTask_assigned_to_name(String task_assigned_to_name) {
		this.task_assigned_to_name = task_assigned_to_name;
	}

	/* add the time to the date
	 * expects string '00:00 AM|PM'
	 */
	public void setServiceTime(String time) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
		Date d = formatter.parse(getServiceDate() + " " + time);
		setService_date(d);
	}
    public Integer getProgram_id() {
        return program_id;
    }
    public void setProgram_id(Integer program_id) {
        this.program_id = program_id;
    }
	
}
