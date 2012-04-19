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
 * Secobjectname entity.
 * 
 * @author JZhang
 */

public class Secobjectname implements java.io.Serializable {

	// Fields

	private String objectname;
	private String description;
	private Integer orgapplicable;

	// Constructors

	/** default constructor */
	public Secobjectname() {
	}

	/** minimal constructor */
	public Secobjectname(String objectname) {
		this.objectname = objectname;
	}

	/** full constructor */
	public Secobjectname(String objectname, String description,
			Integer orgapplicable) {
		this.objectname = objectname;
		this.description = description;
		this.orgapplicable = orgapplicable;
	}

	// Property accessors

	public String getObjectname() {
		return this.objectname;
	}

	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrgapplicable() {
		return this.orgapplicable;
	}

	public void setOrgapplicable(Integer orgapplicable) {
		this.orgapplicable = orgapplicable;
	}

}
