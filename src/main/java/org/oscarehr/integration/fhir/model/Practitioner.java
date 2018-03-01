package org.oscarehr.integration.fhir.model;
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

import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.interfaces.ResourceModifierFilterInterface;

import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;

import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;

public class Practitioner extends OscarFhirResource<org.hl7.fhir.dstu3.model.Practitioner, org.oscarehr.common.model.Provider> {

	private Clinic location;

	public Practitioner( Provider provider ) {
		super( new org.hl7.fhir.dstu3.model.Practitioner(), provider );
	}
	
	public Practitioner( org.hl7.fhir.dstu3.model.Practitioner practitioner ) {
		super(new Provider(), practitioner );
	}

	@Override
	public List<Resource> getContainedFhirResources() {
		return getFhirResource().getContained();
	}
	

	@Override
	protected void setId(org.hl7.fhir.dstu3.model.Practitioner fhirResource) {
		fhirResource.setId( getOscarResource().getProviderNo() );		
	}

	@Override
	protected void mapAttributes( org.hl7.fhir.dstu3.model.Practitioner fhirResource, ResourceModifierFilterInterface filter) {
		setName( fhirResource );
		setIdentifier( fhirResource );
		setQualification( fhirResource );
		setWorkPhone( fhirResource );
	}

	@Override
	protected void mapAttributes( Provider oscarResource ) {
		oscarResource.setProviderNo( getFhirResource().getId() );
		oscarResource.setFirstName( getFhirResource().getNameFirstRep().getGivenAsSingleString() );
		oscarResource.setLastName( getFhirResource().getNameFirstRep().getFamily() );
	}

	public Clinic getLocation() {
		return location;
	}

	/**
	 * This is a contained resource.
	 */
	public void setLocation( Clinic location ) {
		Organization organization = new Organization( location );
		List<Address> address = organization.getFhirResource().getAddress();	
		getFhirResource().setAddress( address );

		this.location = location;
	}
	
	private void setName( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		fhirResource.addName()
		.setFamily( getOscarResource().getLastName() )
		.addGiven( getOscarResource().getFirstName() );
	}
	
	private void setIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		
		//TODO: need to identify the type of provider. ie: nurse or md.
		
		String practitionerNumber = getOscarResource().getPractitionerNo();
		
		if( practitionerNumber == null || practitionerNumber.isEmpty() ) {
			practitionerNumber = getOscarResource().getHsoNo();
		}
		
		if( practitionerNumber == null || practitionerNumber.isEmpty() ) {
			practitionerNumber = getOscarResource().getOhipNo();
		}

		if( practitionerNumber != null ) {
			fhirResource.addIdentifier()
				.setUse( IdentifierUse.OFFICIAL )
				.setSystem("http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-physician")
				.setValue( practitionerNumber );
		}
	}
	
	private void setQualification( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		fhirResource.addQualification().getCode().addCoding()
			.setSystem("[code-system-local-base]/ca-on-immunizations-practitioner-designation")
			.setCode("")
			.setDisplay("Still need to determine how to do.");
	}
	
	private void setWorkPhone( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		fhirResource.addTelecom()
			.setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getWorkPhone() );
	}

}
