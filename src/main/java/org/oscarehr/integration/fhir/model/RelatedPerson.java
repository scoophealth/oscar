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


import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.integration.fhir.utils.MiscUtils;



/*
{
	  "resourceType": "RelatedPerson",
	  "id": "RelatedPerson1",
	  "patient": {
		"reference": "Patient/Patient1"
	  },
	  "relationship": {
		"coding": [
		  {
			"system": "http://hl7.org/fhir/v3/RoleCode",
			"code": "ONESELF"
		  }
		]
	  },
	  "name": {
		"family": [
		  "Doe"
		],
		"given": [
		  "Jane"
		]
	  },
	  "telecom": [
		{
		  "system": "email",
		  "value": "mom@parent.com"
		},
		{
		  "system": "phone",
		  "value": "416-555-5555",
		  "use": "mobile"
		},
		{
		  "system": "phone",
		  "value": "416-444-4444",
		  "use": "home"
		}
	  ]
	}
*/

public class RelatedPerson extends OscarFhirResource< org.hl7.fhir.dstu3.model.RelatedPerson, org.oscarehr.common.model.Contact > {
	
	public RelatedPerson( org.oscarehr.common.model.Contact contact ) {
		super( new org.hl7.fhir.dstu3.model.RelatedPerson(), contact );
	}
	
	public RelatedPerson( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		super( new Contact(), relatedPerson );
	}

	@Override
	protected void setId(org.hl7.fhir.dstu3.model.RelatedPerson fhirResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setId(Contact oscarResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mapAttributes(org.hl7.fhir.dstu3.model.RelatedPerson fhirResource) {
		setName( fhirResource );		
		setAddress( fhirResource );
		setTelecom( fhirResource );
		setIdentifier( fhirResource );
		// setRelationship( fhirResource );
	}

	@Override
	protected void mapAttributes(Contact oscarResource) {
		setName( oscarResource );		
		setAddress( oscarResource );
		setTelecom( oscarResource );
		setIdentifier( oscarResource );
		setRelationship( oscarResource );
	}


	@Override
	public List<Extension> getFhirExtensions() {
		return getFhirResource().getExtension();
	}

	@Override
	public List<Resource> getContainedFhirResources() {
		return getFhirResource().getContained();
	}
	
	private void setName(org.hl7.fhir.dstu3.model.RelatedPerson fhirResource ) {
		fhirResource.addName()
			.setFamily( getOscarResource().getLastName() )
			.addGiven( getOscarResource().getFirstName() );
	}
	
	private void setName( Contact oscarResource ) {
		oscarResource.setFirstName( getFhirResource().getNameFirstRep().getGivenAsSingleString() );
		oscarResource.setLastName( getFhirResource().getNameFirstRep().getFamily() );
	}
	
	private void setAddress( org.hl7.fhir.dstu3.model.RelatedPerson fhirResource ) {
		fhirResource.addAddress()
			.setUse(AddressUse.NULL)
			.addLine( getOscarResource().getAddress2() )
			.setCity( getOscarResource().getCity() )
			.setState( getOscarResource().getProvince())
			.setPostalCode( getOscarResource().getPostal() );
	}
	
	private void setAddress( Contact oscarResource ) {
		Address address = getFhirResource().getAddressFirstRep();
		oscarResource.setAddress( MiscUtils.fhirAddressLineToString( address ) );
		oscarResource.setCity( address.getCity() );
		oscarResource.setProvince( address.getState() );
		oscarResource.setPostal( address.getPostalCode() );
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.RelatedPerson fhirResource ) {
		fhirResource.addTelecom()
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getWorkPhone() );
		
		fhirResource.addTelecom()
			.setSystem( ContactPointSystem.FAX )
			.setValue( getOscarResource().getFax() );
	}
	
	private void setTelecom( Contact oscarResource ) {		
		List<ContactPoint> contactPointList = getFhirResource().getTelecom();
		oscarResource.setWorkPhone( MiscUtils.getFhirPhone( contactPointList ) );
		oscarResource.setFax( MiscUtils.getFhirFax( contactPointList ) );
	}

	private void setIdentifier( org.hl7.fhir.dstu3.model.RelatedPerson fhirResource ) {
		if( getOscarResource() instanceof ProfessionalContact ) {
			fhirResource.addIdentifier()
				.setUse( IdentifierUse.OFFICIAL )
				.setSystem( "[id-system-local-base]/ca-on-panorama-phu-id" )
				.setValue( ( (ProfessionalContact) getOscarResource() ).getCpso() );
		}
	}
	
	private void setIdentifier( Contact oscarResource ) {
		( (ProfessionalContact) oscarResource).setCpso( MiscUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() ));
	}
	
	public void setRelationship( org.hl7.fhir.dstu3.model.RelatedPerson fhirResource ) {
		//TODO: how to set a relationship here.
		fhirResource.getRelationship()
			.addCoding()
			.setSystem("http://hl7.org/fhir/v3/RoleCode")
			.setCode( "RoleCode" );
	}
	
	private void setRelationship(  Contact oscarResource ) {
			//TODO: how to set a relationship here.
	}
}
