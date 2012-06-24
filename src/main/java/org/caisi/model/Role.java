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

public class Role extends BaseObject implements Comparable<Role>{
	private Long id;
	private String name="";
	/** this signifies if it's a user defined role or one that's shipped with the caisi system */
	private boolean userDefined=true;
	private String oscar_name;
	private Date update_date;
	
	public Role() {
		update_date = new Date();
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
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getOscar_name() {
		return oscar_name;
	}
	public void setOscar_name(String oscar_name) {
		this.oscar_name = oscar_name;
	}
	public int compareTo(Role o) {
	    return(name.compareTo(o.name));
    }
    public boolean isUserDefined() {
        return userDefined;
    }
    public void setUserDefined(boolean userDefined) {
        this.userDefined = userDefined;
    }
	
}
