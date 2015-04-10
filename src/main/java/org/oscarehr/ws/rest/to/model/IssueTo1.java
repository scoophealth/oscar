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


@XmlRootElement(name="issue")
public class IssueTo1 implements Serializable{
	
	public static String CUSTOM_ISSUE = "userDefined";
	public static String SYSTEM = "system";
	public static String ICD_9 = "ICD9";
	public static String ICD_10 = "ICD10";
	public static String SNOMED = "SNOMED";
	
	  
	  
	private Long id;
	private String code;
	private String description;
	private String role;
	private Date update_date;
	private String priority;
    private String type;
    private Integer sortOrderId;
    private boolean issueChange;
    
	public Integer getSortOrderId() {
    	return sortOrderId;
    }

	public void setSortOrderId(Integer sortOrderId) {
    	this.sortOrderId = sortOrderId;
    }

	/*public Issue() {
		update_date = new Date();
		sortOrderId=0;
	}*/
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
        
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public boolean isIssueChange() {
	    return issueChange;
    }

	public void setIssueChange(boolean issueChange) {
	    this.issueChange = issueChange;
    }
	
	/*
    public String getUpdate_date_web() {        
        if(update_date==null)
                return null;
        else
                return formatter.format(update_date);
    }
    public void setUpdate_date_web(String update_date_s) {

    formatter.setLenient(false);
        try{
                        if(update_date_s!=null)
                                this.update_date = formatter.parse(update_date_s);
                        else
                                this.update_date = new Date();
            }catch(Exception e){ MiscUtils.getLogger().error("Invalid issue update date", e);}
        }
	*/
}
