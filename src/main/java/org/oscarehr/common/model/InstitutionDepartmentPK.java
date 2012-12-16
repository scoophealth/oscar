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

public class InstitutionDepartmentPK implements Serializable {

	private Integer institutionId;
	private Integer departmentId;
	
	public InstitutionDepartmentPK() {
		
	}
	
	public InstitutionDepartmentPK(Integer institutionId, Integer departmentId) {
		this.institutionId = institutionId;
		this.departmentId = departmentId;
	}
	
	public Integer getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	@Override
	public String toString() {
		return ("institutionId=" + institutionId + ", departmentId=" + departmentId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			InstitutionDepartmentPK o1 = (InstitutionDepartmentPK) o;
			return ((institutionId .equals(o1.institutionId)) && (departmentId.equals(o1.departmentId)));
		} catch (RuntimeException e) {
			return (false);
		}
	}
	
}
