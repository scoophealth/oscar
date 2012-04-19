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

/**
 * Secprivilege entity.
 * 
 * @author JZhang
 */

public class Secprivilege implements java.io.Serializable {

	// Fields

	private Integer id;
	private String privilege;
	private String description;

	// Constructors

	/** default constructor */
	public Secprivilege() {
	}

	/** minimal constructor */
	public Secprivilege(Integer id, String privilege) {
		this.id = id;
		this.privilege = privilege;
	}

	/** full constructor */
	public Secprivilege(Integer id, String privilege, String description) {
		this.id = id;
		this.privilege = privilege;
		this.description = description;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
