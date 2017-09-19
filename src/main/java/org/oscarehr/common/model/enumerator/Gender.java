package org.oscarehr.common.model.enumerator;
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

import java.io.Serializable;
import javax.persistence.*;

import org.oscarehr.common.model.AbstractModel;


/**
 * Model class for the lst_gender database table. 
 * Contains an enumerated class for Gender code.
 * 
 */
@Entity
@Table(name="lst_gender")
@NamedQuery(name="Gender.findAll", query="SELECT g FROM Gender g")
public class Gender extends AbstractModel<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Enumerated(EnumType.STRING)
	private String code;

	private String description;

	private int displayorder;

	private byte isactive;

	public Gender() {
		// default
	}
	
	@Override
	public String getId() {
		return this.code;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

	public byte getIsactive() {
		return this.isactive;
	}

	public void setIsactive(byte isactive) {
		this.isactive = isactive;
	}

}