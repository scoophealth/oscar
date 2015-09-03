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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.caisi.model.OptionsBean;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.oscarehr.PMmodule.model.Program;

@Entity
@Table(name="custom_filter")
public class CustomFilter extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="provider_no",length=6)
	private String providerNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_date")
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_date")
	private Date endDate;
	
	@Column(length=1)
	private String status;
	
	@Column(length=20)
	private String priority;
	
	@Column(name="demographic_no",length=20)
	private String demographicNo;
	
	@Column(length=10,nullable=true)
	private String programId;
	
	@Column(length=255)
	private String name;
	
	@Column(nullable=true)
	private Boolean shortcut = false;
	
	@Column(length=255)
	private String message;
	
	@OneToMany( fetch=FetchType.EAGER)
	@JoinTable(name="custom_filter_providers",
    joinColumns=
        @JoinColumn(name="filter_id", referencedColumnName="id"),
    inverseJoinColumns=
        @JoinColumn(name="provider_no", referencedColumnName="provider_no")
    )
	private Set<Provider> providers = new HashSet<Provider>();
	
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="custom_filter_assignees",
    joinColumns=
        @JoinColumn(name="filter_id", referencedColumnName="id"),
    inverseJoinColumns=
        @JoinColumn(name="provider_no", referencedColumnName="provider_no")
    )
	private Set<Provider> assignees = new HashSet<Provider>();
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="programId", referencedColumnName="id", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Program program;
	
	
	@Transient
	private String client;
	@Transient
	private String mrp;
	@Transient
	private String sort_order = "asc";
	@Transient
	private String demographic_webName;
	
    public static List<OptionsBean> statusList;
    public static List<OptionsBean> priorityList;
   

    static {
            priorityList = new ArrayList<OptionsBean>();
            //priorityList.add(new OptionsBean(""));
            priorityList.add(new OptionsBean("Normal"));
            priorityList.add(new OptionsBean("High"));
            priorityList.add(new OptionsBean("Low"));

            statusList = new ArrayList<OptionsBean>();
            //statusList.add(new OptionsBean("",""));
            statusList.add(new OptionsBean("Active", "A"));
            statusList.add(new OptionsBean("Completed", "C"));
            statusList.add(new OptionsBean("Deleted", "D"));
    }

    
	public CustomFilter() {
		this(false);
	}
	
	public CustomFilter(boolean noEndDate) {
		setStatus("A");
		setPriority(null);
		if(!noEndDate) {
			setEndDate(new Date());
		}
		providers = new HashSet<Provider>();
		assignees = new HashSet<Provider>();
		setSort_order("asc");	
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getShortcut() {
		return shortcut;
	}
	
	public Boolean isShortcut() {
		return shortcut;
	}

	public void setShortcut(Boolean shortcut) {
		this.shortcut = shortcut;
	}

	public Set<Provider> getProviders() {
		return providers;
	}

	public void setProviders(Set<Provider> providers) {
		this.providers = providers;
	}

	public Set<Provider> getAssignees() {
		return assignees;
	}

	public void setAssignees(Set<Provider> assignees) {
		this.assignees = assignees;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
	//ported over from the old object
	
	/* have to do this */
	public void setStartDateWeb(String data) {
		if (data == null || data.length() == 0) {
			data = "1900-01-01";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setStartDate(formatter.parse(data));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid service date, use yyyy-MM-dd");
		}
	}

	public String getStartDateWeb() {
		if (getStartDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getStartDate());
		}
		return "";
	}

	public void setEndDateWeb(String data) {
		if (data == null || data.length() == 0) {
			data = "8888-12-31";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setEndDate(formatter.parse(data));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid service date, use yyyy-MM-dd");
		}
	}

	public String getEndDateWeb() {
		if (getEndDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getEndDate());
		}
		return "";
	}
	
	public String getAssignee() {
		if (getAssignees().size() > 0) {
			Provider p = getAssignees().iterator().next();
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
		if (getProviders().size() > 0) {
			Provider p = getProviders().iterator().next();
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
	
	public String getMrp() {
		return mrp;
	}

	public void setMrp(String providerNumber) {
		mrp = providerNumber;
	}
	
	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}


	public String getDemographic_webName() {
		return demographic_webName;
	}


	public void setDemographic_webName(String demographic_webName) {
		this.demographic_webName = demographic_webName;
	}


	public void setShortcut(boolean shortcut) {
		this.shortcut = shortcut;
	}

	
}
