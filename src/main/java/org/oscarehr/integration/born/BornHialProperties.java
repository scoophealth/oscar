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
package org.oscarehr.integration.born;

public class BornHialProperties {
	
	private String idValue;
	private String idCodingSystem;
	
	private String setIdValue;
	private String setIdCodingSystem;
	
	private String organization;
	private String organizationName;
	
	public String getIdValue() {
		return idValue;
	}
	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}
	public String getIdCodingSystem() {
		return idCodingSystem;
	}
	public void setIdCodingSystem(String idCodingSystem) {
		this.idCodingSystem = idCodingSystem;
	}
	public String getSetIdValue() {
		return setIdValue;
	}
	public void setSetIdValue(String setIdValue) {
		this.setIdValue = setIdValue;
	}
	public String getSetIdCodingSystem() {
		return setIdCodingSystem;
	}
	public void setSetIdCodingSystem(String setIdCodingSystem) {
		this.setIdCodingSystem = setIdCodingSystem;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	

}
