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
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Reference;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.integration.fhir.utils.FhirUtils;


public class RelatedPerson extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.RelatedPerson, org.oscarehr.common.model.Contact > {
	
	public RelatedPerson( org.oscarehr.common.model.Contact contact ) {
		super( new org.hl7.fhir.dstu3.model.RelatedPerson(), contact );
	}
	
	public RelatedPerson( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		super( new Contact(), relatedPerson );
	}
	
	public RelatedPerson( org.oscarehr.common.model.Demographic demographic ) {
		this( new Patient( demographic ) );
	}
	
	public RelatedPerson( Patient patient ) {
		super();
		setPatient( patient );
	}

	/**
	 * This patient is represents a relationship to another (or the same) patient
	 * in focus.
	 * 
	 * Example: if a child's immunization record is being sent to a health authority. 
	 * 
	 * The child's responsible parent would be inserted here. The child would be 
	 * the patient in focus. aka: the patient that this patient refers to. 
	 * 
	 * If there is no relationship, then the patient would be inserted here with a
	 * relationship to ONESELF and a reference to the data contained within. 
	 * 
	 */
	private void setPatient( Patient patient ) {
		
		Contact contact =  new Contact();
		Demographic demographic = patient.getOscarResource();
		
		contact.setId( null );
		contact.setFirstName( demographic.getFirstName() );
		contact.setLastName( demographic.getLastName() );
		contact.setAddress( demographic.getAddress() );
		contact.setCity( demographic.getCity() );
		contact.setProvince( demographic.getProvince() );
		contact.setPostal( demographic.getPostal() );
		contact.setResidencePhone( demographic.getPhone() );
		contact.setWorkPhone( demographic.getPhone2() );
		
		setResource( new org.hl7.fhir.dstu3.model.RelatedPerson(), contact );
		
		Reference reference = new Reference();
		reference.setResource( patient.getFhirResource() );
		getFhirResource().setPatient( reference );
		
		getFhirResource().setPatientTarget( patient.getFhirResource() );
		
		getFhirResource().getRelationship()
			.addCoding()
			.setSystem("http://hl7.org/fhir/v3/RoleCode")
			.setCode( "ONESELF" );
	}

	@Override
	protected void mapAttributes(org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		setName( relatedPerson );		
		setAddress( relatedPerson );
		setTelecom( relatedPerson );
		setIdentifier( relatedPerson );
		// setRelationship( relatedPerson );
	}
	
	@Override
	protected void mapAttributes( Contact contact ) {
		setName( contact );		
		setAddress( contact );
		setTelecom( contact );
		setIdentifier( contact );
//		setRelationship( contact );
	}
	
	private void setName(org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		relatedPerson.addName()
			.setFamily( getOscarResource().getLastName() )
			.addGiven( getOscarResource().getFirstName() );
	}
	
	private void setName( Contact contact ) {
		contact.setFirstName( getFhirResource().getNameFirstRep().getGivenAsSingleString() );
		contact.setLastName( getFhirResource().getNameFirstRep().getFamily() );
	}
	
	private void setAddress( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		relatedPerson.addAddress()
			.setUse(AddressUse.NULL)
			.addLine( getOscarResource().getAddress() )
			.addLine( getOscarResource().getAddress2() )
			.setCity( getOscarResource().getCity() )
			.setState( getOscarResource().getProvince())
			.setPostalCode( getOscarResource().getPostal() );
	}
	
	private void setAddress( Contact contact ) {
		Address address = getFhirResource().getAddressFirstRep();
		contact.setAddress( FhirUtils.fhirAddressLineToString( address ) );
		contact.setCity( address.getCity() );
		contact.setProvince( address.getState() );
		contact.setPostal( address.getPostalCode() );
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		String home = getOscarResource().getResidencePhone();
		String work = getOscarResource().getWorkPhone();
		String fax = getOscarResource().getFax();
		String email = getOscarResource().getEmail();
		
		if( home != null ) {
			relatedPerson.addTelecom()
				.setSystem( ContactPointSystem.PHONE )
				.setUse( ContactPointUse.HOME )
				.setValue( home );
		}
		if( work != null ) {
			relatedPerson.addTelecom()
				.setSystem( ContactPointSystem.PHONE )
				.setUse( ContactPointUse.WORK )
				.setValue( work );
		}
		if( fax != null ) {
			relatedPerson.addTelecom()
				.setSystem( ContactPointSystem.FAX )
				.setUse( ContactPointUse.WORK )
				.setValue( fax );
		}
		
		if( email != null ) {
			relatedPerson.addTelecom()
				.setSystem( ContactPointSystem.EMAIL )
				.setValue( email );
		}
	}
	
	private void setTelecom( Contact contact ) {		
		List<ContactPoint> contactPointList = getFhirResource().getTelecom();
		contact.setWorkPhone( FhirUtils.getFhirPhone( contactPointList ) );
		contact.setFax( FhirUtils.getFhirFax( contactPointList ) );
	}

	private void setIdentifier( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		if( getOscarResource() instanceof ProfessionalContact ) {
			relatedPerson.addIdentifier()
				.setUse( IdentifierUse.OFFICIAL )
				.setSystem( "[id-system-local-base]/ca-on-panorama-phu-id" )
				.setValue( ( (ProfessionalContact) getOscarResource() ).getCpso() );
		}
	}
	
	private void setIdentifier( Contact contact ) {
		( (ProfessionalContact) contact).setCpso( FhirUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() ));
	}
	
	public void setRelationship( org.hl7.fhir.dstu3.model.RelatedPerson relatedPerson ) {
		//TODO: how to set a relationship here.
		relatedPerson.getRelationship()
			.addCoding()
			.setSystem("http://hl7.org/fhir/v3/RoleCode")
			.setCode( "RoleCode" );
	}
	

}
