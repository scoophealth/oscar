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
import javax.persistence.Table;

@Entity
@Table(name = "preventionsExt")
public class PreventionExt extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(name = "prevention_id")
	private Integer preventionId = null;

	private String keyval = null;
	private String val = null;

	
	public Integer getPreventionId() {
		return preventionId;
	}
	
	public void setPreventionId(Integer preventionId) {
		this.preventionId = preventionId;
	}
	
	public String getkeyval() {
		return keyval;
	}
	
	public void setKeyval(String keyval) {
		this.keyval = keyval;
	}
	
	public String getVal() {
		return val;
	}
	
	public void setVal(String val) {
		this.val = val;
	}

	@Override
    public Integer getId() {
		return id;
	}
}
