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
package org.oscarehr.PMmodule.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.oscarehr.common.model.AbstractModel;

/**
 * VacanyTemplate entity. @author azhou
 */
@Entity
@Table(name = "vacancy_template")
public class VacancyTemplate extends AbstractModel<Integer> implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="TEMPLATE_ID", unique=true, nullable=false)
	private Integer id;	
	@Column(name = "NAME")
	private String name;
	@Column(name = "ACTIVE")
	private boolean active;
	@Column(name = "WL_PROGRAM_ID")
	private Integer wlProgramId;

	// Constructors

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/** default constructor */
	public VacancyTemplate() {
	}

	/** full constructor */
	public VacancyTemplate(Integer wlProgramId, String name, Boolean active) {
		this.wlProgramId = wlProgramId;
		this.name = name;
		this.active = active;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getWlProgramId() {
    	return wlProgramId;
    }

	public void setWlProgramId(Integer wlProgramId) {
    	this.wlProgramId = wlProgramId;
    }

	
}
