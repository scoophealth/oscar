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

import org.oscarehr.integration.fhir.resources.constants.ContactType;

@Entity
public class ProfessionalContact extends Contact {
	
	private String specialty;	
	private String cpso;
	
	//to be used on a site specific basis. Any additional ids should be done with other_id
	private String systemId;
	
	public ProfessionalContact() {
		setContactType( ContactType.professional );
	}
	
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	
	public String getCpso() {
		return cpso;
	}

	public void setCpso(String cpso) {
		this.cpso = cpso;
	}
	
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String toString() {
		return "ProfessionalRelationship - id:"+ getId();
	}
	
	@Override
	public void setProviderCpso(String providerCPSO) {
		this.setCpso(providerCPSO);
		
	}

	@Override
	public String getProviderCpso() {
		return this.getProviderCpso();
	}
	
	/**
	 * Organization name is a first name for now. 
	 * An Organization field should be added to the database.
	 */
	@Override
	public void setOrganizationName(String organizationName) {
		this.setFirstName( organizationName );
	}

	@Override
	public String getOrganizationName() {
		return ( this.getFirstName() + " " + this.getLastName() );
	}
	
	@Override
	public void setLocationCode(String locationCode) {
		setSystemId(locationCode);		
	}

	@Override
	public String getLocationCode() {
		return this.getSystemId();
	}

}
