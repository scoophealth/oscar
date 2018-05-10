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
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Identifier;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.integration.fhir.interfaces.ContactInterface;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.utils.FhirUtils;

/*
 *  {doco
  "resourceType" : "Organization",
  // from Resource: id, meta, implicitRules, and language
  // from DomainResource: text, contained, extension, and modifierExtension
  "identifier" : [{ Identifier }], // C? Identifies this organization  across multiple systems
  "active" : <boolean>, // Whether the organization's record is still in active use
  "type" : [{ CodeableConcept }], // Kind of organization
  "name" : "<string>", // C? Name used for the organization
  "alias" : ["<string>"], // A list of alternate names that the organization is known as, or was known as in the past
  "telecom" : [{ ContactPoint }], // C? A contact detail for the organization
  "address" : [{ Address }], // C? An address for the organization
  "partOf" : { Reference(Organization) }, // The organization of which this organization forms a part
  "contact" : [{ // Contact for the organization for a certain purpose
    "purpose" : { CodeableConcept }, // The type of contact
    "name" : { HumanName }, // A name associated with the contact
    "telecom" : [{ ContactPoint }], // Contact details (telephone, email, etc.)  for a contact
    "address" : { Address } // Visiting or postal addresses for the contact
  }],
  "endpoint" : [{ Reference(Endpoint) }] // Technical endpoints providing access to services operated for the organization
}
 */

/**
 * Any Organizational unit that has compiled and is in stewardship of patient data.
 * 
 */
public class Organization<T extends AbstractModel<Integer> & ContactInterface> extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.Organization, T> {

	public Organization( T contact ) {
		super( new org.hl7.fhir.dstu3.model.Organization(), contact );
	}
	
	public Organization( T clinic, OscarFhirConfigurationManager configurationManager  ) {
		super( new org.hl7.fhir.dstu3.model.Organization(), clinic, configurationManager );
	}

	@SuppressWarnings("unchecked")
	public Organization( org.hl7.fhir.dstu3.model.Organization organization ) {
		super( (T) new ProfessionalContact(), organization );
	}
	
	@Override
	protected void setId( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		Integer intId = getOscarResource().getId();
		if( intId != null ) {
			fhirResource.setId( intId + "" );
		} else {
			super.setId( fhirResource );
		}
	}

	@Override
	protected void mapAttributes(org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		// mandatory
		setFHIRIdentifier();
		
		//optional
		if( include( OptionalFHIRAttribute.oranizationName ) ) {
			setOranizationName( fhirResource );		
		}
		
		if( include( OptionalFHIRAttribute.address ) ) {
			setAddress( fhirResource );
		}
		
		if( include( OptionalFHIRAttribute.telecom ) ) {	
			setTelecom( fhirResource );
		}
			
	}

	@Override
	protected void mapAttributes( T oscarResource ) {
		setOranizationName( oscarResource );		
		setAddress( oscarResource );
		setTelecom( oscarResource );
		setIdentifier( oscarResource );
	}

	private void setOranizationName( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		fhirResource.setName( getOscarResource().getAddress() );
	}
	
	private void setOranizationName( ContactInterface oscarResource ) {
		oscarResource.setAddress( getFhirResource().getName() );
	}
	
	private void setAddress( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		fhirResource.addAddress()
			.setUse(AddressUse.NULL)
			.addLine( getOscarResource().getAddress2() )
			.setCity( getOscarResource().getCity() )
			.setState( getOscarResource().getProvince())
			.setPostalCode( getOscarResource().getPostal() );
	}
	
	private void setAddress( ContactInterface oscarResource ) {
		Address address = getFhirResource().getAddressFirstRep();
		oscarResource.setAddress( FhirUtils.fhirAddressLineToString( address ) );
		oscarResource.setCity( address.getCity() );
		oscarResource.setProvince( address.getState() );
		oscarResource.setPostal( address.getPostalCode() );
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		fhirResource.addTelecom()
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getWorkPhone() );
		
		if( include( OptionalFHIRAttribute.fax ) ) {	
			fhirResource.addTelecom()
				.setSystem( ContactPointSystem.FAX )
				.setValue( getOscarResource().getFax() );
		}
	}
	
	private void setTelecom( ContactInterface oscarResource ) {		
		List<ContactPoint> contactPointList = getFhirResource().getTelecom();
		oscarResource.setWorkPhone( FhirUtils.getFhirPhone( contactPointList ) );
		oscarResource.setFax( FhirUtils.getFhirFax( contactPointList ) );
	}

	/**
	 * Sets the official unique identifier that is referenced - and relative to - external resources
	 * This is the default identifier.  This is overriden by alternative identifiers set after instantiation. 
	 */
	private void setFHIRIdentifier() {
		Identifier identifier = new Identifier();
		identifier.setSystem("").setValue( "" );
		setIdentifier( identifier );
	}
	
	protected void setIdentifier( Identifier identifier ) {
		if( getFhirResource().getIdentifier() != null ) {
			getFhirResource().getIdentifier().clear();
		}	
		getFhirResource().addIdentifier( identifier );
	}

	private void setIdentifier( ContactInterface oscarResource ) {
		oscarResource.setProviderCpso( FhirUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() ));
	}

}
