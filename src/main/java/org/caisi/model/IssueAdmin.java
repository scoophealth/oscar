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

import java.util.Date;

import org.oscarehr.util.MiscUtils;

public class IssueAdmin extends BaseObject {
  private Long id;
  private String code;
  private String description;
  private String role;
  private Date update_date;
  private static java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-DD");
    /*
     Generate your getters and setters using your favorite IDE:
     In Eclipse:
     Right-click -> Source -> Generate Getters and Setters
    */

    public IssueAdmin() {
    	this.update_date = new Date();
    }

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
        return this.update_date;
    }
    public void setUpdate_date(Date update_date) {
    	this.update_date = update_date;       
    }
    public String getUpdate_date_web() {	
	if(update_date==null)
		return null;
	else
        	return formatter.format(update_date);
    }
    public void setUpdate_date_web(String update_date_s) {
	//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//sdf.setLenient(false);
	//SimpleDateFormat sdf = new SimpleDateFormat();
    formatter.setLenient(false);
	try{
			if(update_date_s!=null)
				this.update_date = formatter.parse(update_date_s);
			else
				this.update_date = new Date();
	    }catch(Exception e){ MiscUtils.getLogger().error("Invalid issue update date", e);}
	}
}
