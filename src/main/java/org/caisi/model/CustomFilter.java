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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Provider;

/**
* Object representation of 'custom_filter' table in OSCAR
* @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
*
*/
public class CustomFilter extends BaseObject {
	/* standard stuff */
	private Long id;
	private String name;
	private String demographic_no;
	private String demographic_webName;

	private Date start_date;
	private Date end_date;

	private String sort_order;


	private String status;
	private String priority;

	private String mrp;
	private Set providers;
	private Set assignees;
	private Program program;

	private String custom;

	private String client;

	public static List<OptionsBean> statusList;
	public static List<OptionsBean> priorityList;


	private String providerNo;
    private boolean shortcut;
	private String programId;

	static {
		priorityList = new ArrayList<OptionsBean>();
		//priorityList.add(new OptionsBean(""));
		priorityList.add(new OptionsBean("Normal"));
		priorityList.add(new OptionsBean("High"));
		priorityList.add(new OptionsBean("Low"));

		statusList = new ArrayList<OptionsBean>();
		//statusList.add(new OptionsBean("",""));
		statusList.add(new OptionsBean("Active","A"));
		statusList.add(new OptionsBean("Completed","C"));
		statusList.add(new OptionsBean("Deleted","D"));
	}

	public CustomFilter() {
		providers = new HashSet();
		assignees = new HashSet();
		setStatus("A");
		setPriority("Normal");
		this.setEnd_date(new Date());
		setSort_order("asc");
	}
	
	public String getMrp() {
		return mrp;
	}
	
	public void setMrp(String providerNumber) {
		mrp = providerNumber;		
	}

	public Set getAssignees() {
		return assignees;
	}
	public void setAssignees(Set assignees) {
		this.assignees = assignees;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set getProviders() {
		return providers;
	}
	public void setProviders(Set providers) {
		this.providers = providers;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	/* have to do this */
	public void setStartDate(String data) {
		if(data == null || data.length()==0) {
			data = "1900-01-01";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setStart_date(formatter.parse(data));
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid service date, use yyyy-MM-dd");
		}
	}

	public String getStartDate() {
		if(getStart_date() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getStart_date());
		}
		return "";
	}

	public void setEndDate(String data) {
		if(data == null || data.length()==0) {
			data = "8888-12-31";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setEnd_date(formatter.parse(data));
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid service date, use yyyy-MM-dd");
		}
	}

	public String getEndDate() {
		if(getEnd_date() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getEnd_date());
		}
		return "";
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getAssignee() {
		if(getAssignees().size()>0) {
			Provider p = (Provider)getAssignees().iterator().next();
			return p.getProviderNo();
		}
		return null;
	}
	public void setAssignee(String assignee) {
		Provider p = new Provider();
		p.setProviderNo(assignee);
		this.getAssignees().clear();
		this.getAssignees().add(p);
	}
	public String getProvider() {
		if(getProviders().size()>0) {
			Provider p = (Provider)getProviders().iterator().next();
			return p.getProviderNo();
		}
		return null;
	}
	public void setProvider(String provider) {
		Provider p = new Provider();
		p.setProviderNo(provider);
		this.getProviders().clear();
		this.getProviders().add(p);
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}

	public String getDemographic_webName() {
		return demographic_webName;
	}

	public void setDemographic_webName(String demographic_webName) {
		this.demographic_webName = demographic_webName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String provider_no) {
		this.providerNo = provider_no;
	}

	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}

    public boolean isShortcut() {
        return shortcut;
    }

    public void setShortcut(boolean shortcut) {
        this.shortcut = shortcut;
    }

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}


}
