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

	public Secrole() {
	}
	
	public Secrole(String roleName, String description) {
		this.roleName = roleName;
		this.description = description;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((lastUpdateUser == null) ? 0 : lastUpdateUser.hashCode());
		result = prime * result + orderByIndex;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Secrole other = (Secrole) obj;
		if (active != other.active) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null) return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate)) return false;
		if (lastUpdateUser == null) {
			if (other.lastUpdateUser != null) return false;
		} else if (!lastUpdateUser.equals(other.lastUpdateUser)) return false;
		if (orderByIndex != other.orderByIndex) return false;
		if (roleName == null) {
			if (other.roleName != null) return false;
		} else if (!roleName.equals(other.roleName)) return false;
		return true;
	}

}
