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
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import oscar.util.DateUtils;

@Entity
@Table(name="tickler_comments")
public class TicklerComment extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="tickler_no",nullable=true)
	private Integer ticklerNo;
	
	@Lob
	private String message;
	
	@Column(name="provider_no",length=6)
	private String providerNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="provider_no", referencedColumnName="provider_no", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Provider provider;
	
	public TicklerComment() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	
	
	//this is web stuff :(
	
    public String getUpdateTime(Locale locale) {
        return DateUtils.formatTime(updateDate, locale);                       
    }
    
    public String getUpdateDateTime(Locale locale) {
        return DateUtils.formatDateTime(this.updateDate, locale);           
    }
    
    public String getUpdateDate(Locale locale) {
        return DateUtils.formatDate(this.updateDate, locale);
    }
    public boolean isUpdateDateToday() {
        return org.apache.commons.lang.time.DateUtils.isSameDay(updateDate, new Date());            
    }
}
