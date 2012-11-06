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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ichppccode")
public class Ichppccode extends AbstractCodeSystemModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ichppccode")
	private String id;

	@Column(name="diagnostic_code")
	private String diagnosticCode;

	private String description;

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getDiagnosticCode() {
    	return diagnosticCode;
    }

	public void setDiagnosticCode(String diagnosticCode) {
    	this.diagnosticCode = diagnosticCode;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	@Override
    public String getCode() {
	   return this.getId();
    }

	@Override
    public String getCodingSystem() {
	   return "ichppccode";
    }

	@Override
	public void setCode(String code) {
		this.setId(code);
	}

}
