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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="tickler_update")
public class TicklerUpdate extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="tickler_no")
	private Integer ticklerNo;
	
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private Tickler.STATUS status;
	
	@Column(length=6)
	private String assignedTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date serviceDate;
	
	@Column(length=6)
	private String priority;
	
	@Column(name="provider_no",length=6)
	private String providerNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="provider_no", referencedColumnName="provider_no", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Provider provider;

	public TicklerUpdate() {
		updateDate = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTicklerNo() {
		return ticklerNo;
	}

	public void setTicklerNo(Integer ticklerNo) {
		this.ticklerNo = ticklerNo;
	}

	public Tickler.STATUS getStatus() {
		return status;
	}

	public void setStatus(Tickler.STATUS status) {
		this.status = status;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
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

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	
    public void setStatusAsChar(char s) {
    	if(s == 'A' || s == 'a')
    		setStatus(Tickler.STATUS.A);
    	else if(s == 'C' || s == 'c')
    		setStatus(Tickler.STATUS.C);
    	else if(s == 'D' || s == 'd')
    		setStatus(Tickler.STATUS.D);
    	else
    		throw new IllegalArgumentException("Invalid status");
    }
	
}
