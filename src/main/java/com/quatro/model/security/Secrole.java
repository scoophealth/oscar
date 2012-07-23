/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.model.security;

import java.util.Calendar;


public class Secrole implements java.io.Serializable {

	// Fields

	private Long id;
	private String roleName;
	private String description;
	private boolean active;   
	private String lastUpdateUser;
	private Calendar lastUpdateDate;
	
    private int orderByIndex;

	// Constructors

    public void setId(Long id) {
    	this.id = id;
    }
    public Long getId() {
    	return id;
    }
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Calendar getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Calendar lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public int getOrderByIndex() {
		return orderByIndex;
	}

	public void setOrderByIndex(int orderByIndex) {
		this.orderByIndex = orderByIndex;
	}

	/** default constructor */
	public Secrole() {
	}

	/** full constructor */
	public Secrole(String roleName, String description) {
		this.roleName = roleName;
		this.description = description;
	}

	// Property accessors

	

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return roleName;
	}
	
	public void setName(String name) {
		this.roleName = name;
	}

	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Secrole)) return false;

		Secrole r = (Secrole) obj;
		if (r.getId() == null || this.getId() == null) return false;
		if (this.getId().longValue() == r.getId().longValue()) return true;

		return false;
	}
}
