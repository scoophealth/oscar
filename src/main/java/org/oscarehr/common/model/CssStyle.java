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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * The persistent class for the cssStyles database table.
 * 
 */
@Entity
@Table(name="cssStyles")
public class CssStyle extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ACTIVE = "A";
	public static final String DELETED = "D";
	private int id;
	private String name;
	private String style;
	private String status;

    public CssStyle() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(length=255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


    @Lob()
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


	public String getStatus() {
	    return status;
    }


	public void setStatus(String status) {
	    this.status = status;
    }

}
