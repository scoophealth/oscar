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

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormTo1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer demographicNo;
	
	private int formId;
	
	private String type;
	private String name;
	private String subject;
	private String status;
	private Date date;
    private Boolean showLatestFormOnly;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public int getFormId() {
	    return formId;
    }

	public void setFormId(int formId) {
	    this.formId = formId;
    }

	public String getType() {
	    return type;
    }

	public void setType(String type) {
	    this.type = type;
    }

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }

	public String getSubject() {
	    return subject;
    }

	public void setSubject(String subject) {
	    this.subject = subject;
    }

	public String getStatus() {
	    return status;
    }

	public void setStatus(String status) {
	    this.status = status;
    }

	public Date getDate() {
	    return date;
    }

	public void setDate(Date date) {
	    this.date = date;
    }

	public Boolean getShowLatestFormOnly() {
	    return showLatestFormOnly;
    }

	public void setShowLatestFormOnly(Boolean showLatestFormOnly) {
	    this.showLatestFormOnly = showLatestFormOnly;
    }

	public static FormTo1 create( Integer id,int demographicNo,int formId,String type,String name,String subject,String status,Date date,Boolean showLatestFormOnly){
		FormTo1 formTo1 = new FormTo1();
		formTo1.id = id;
		formTo1.demographicNo = demographicNo;
		formTo1.formId = formId;
		formTo1.type = type;
		formTo1.name = name;
		formTo1.subject = subject;
		formTo1.status = status;
		formTo1.date = date;
		formTo1.showLatestFormOnly = showLatestFormOnly;
		return formTo1;
	}
	
	
}
