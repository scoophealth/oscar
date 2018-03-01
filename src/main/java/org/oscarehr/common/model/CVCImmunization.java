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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CVCImmunization extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private int versionId;
	
	private String snomedConceptId;
	private String displayName;
	private String picklistName;
	private Boolean generic;
	

	/* for tradename ones*/
	Integer prevalence;
	String parentConceptId;
	boolean ispa;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSnomedConceptId() {
		return snomedConceptId;
	}

	public void setSnomedConceptId(String snomedConceptId) {
		this.snomedConceptId = snomedConceptId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPicklistName() {
		return picklistName;
	}

	public void setPicklistName(String picklistName) {
		this.picklistName = picklistName;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public boolean isGeneric() {
		return generic;
	}

	public void setGeneric(boolean generic) {
		this.generic = generic;
	}

	public Integer getPrevalence() {
		return prevalence;
	}

	public void setPrevalence(Integer prevalence) {
		this.prevalence = prevalence;
	}

	public String getParentConceptId() {
		return parentConceptId;
	}

	public void setParentConceptId(String parentConceptId) {
		this.parentConceptId = parentConceptId;
	}

	public boolean isIspa() {
		return ispa;
	}

	public void setIspa(boolean ispa) {
		this.ispa = ispa;
	}
	

}
