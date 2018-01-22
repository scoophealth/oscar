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

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.common.Gender;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.fhir.interfaces.ResourceModifierFilterInterface;
import org.oscarehr.integration.fhir.utils.EnumMappingUtil;
import org.oscarehr.integration.fhir.utils.MiscUtils;

/*
 *{
  "resourceType" : "Patient",
  // from Resource: id, meta, implicitRules, and language
  // from DomainResource: text, contained, extension, and modifierExtension
  "identifier" : [{ Identifier }], // An identifier for this patient
  "active" : <boolean>, // Whether this patient's record is in active use
  "name" : [{ HumanName }], // A name associated with the patient
  "telecom" : [{ ContactPoint }], // A contact detail for the individual
  "gender" : "<code>", // male | female | other | unknown
  "birthDate" : "<date>", // The date of birth for the individual
  // deceased[x]: Indicates if the individual is deceased or not. One of these 2:
  "deceasedBoolean" : <boolean>,
  "deceasedDateTime" : "<dateTime>",
  "address" : [{ Address }], // Addresses for the individual
  "maritalStatus" : { CodeableConcept }, // Marital (civil) status of a patient
  // multipleBirth[x]: Whether patient is part of a multiple birth. One of these 2:
  "multipleBirthBoolean" : <boolean>,
  "multipleBirthInteger" : <integer>,
  "photo" : [{ Attachment }], // Image of the patient
  "contact" : [{ // A contact party (e.g. guardian, partner, friend) for the patient
    "relationship" : [{ CodeableConcept }], // The kind of relationship
    "name" : { HumanName }, // A name associated with the contact person
    "telecom" : [{ ContactPoint }], // A contact detail for the person
    "address" : { Address }, // Address for the contact person
    "gender" : "<code>", // male | female | other | unknown
    "organization" : { Reference(Organization) }, // C? Organization that is associated with the contact
    "period" : { Period } // The period during which this contact person or organization is valid to be contacted relating to this patient
  }],
  "animal" : { // This patient is known to be an animal (non-human)
    "species" : { CodeableConcept }, // R!  E.g. Dog, Cow
    "breed" : { CodeableConcept }, // E.g. Poodle, Angus
    "genderStatus" : { CodeableConcept } // E.g. Neutered, Intact
  },
  "communication" : [{ // A list of Languages which may be used to communicate with the patient about his or her health
    "language" : { CodeableConcept }, // R!  The language which can be used to communicate with the patient about his or her health 
    "preferred" : <boolean> // Language preference indicator
  }],
  "careProvider" : [{ Reference(Organization|Practitioner) }], // Patient's nominated primary care provider
  "managingOrganization" : { Reference(Organization) }, // Organization that is the custodian of the patient record
  "link" : [{ // Link to another patient resource that concerns the same actual person
    "other" : { Reference(Patient) }, // R!  The other patient resource that the link refers to
    "type" : "<code>" // R!  replace | refer | seealso - type of link
  }]
} 

EXAMPLE:

{
  "resourceType": "Patient",
  "id": "Patient1",
  "contained": [
    {
      "resourceType": "Organization",
      "id": "OrgSchool1",
      "identifier": [
        {
          "system": "[id-system-local-base]/ca-on-panorama-school-id",
          "value": "10001"
        }
      ],
      "name": "Dublin Heights Elementary and Middle School"
    },
  "identifier": [
	{
	  "use": "official",
	  "system": "[id-system-global-base]/ca-on-patient-hcn",
	  "value": "9393881587"
	},
	{
	  "use": "secondary",
	  "system": "[id-system-local-base]/ca-on-panorama-immunization-id",
	  "value": "10000123"
	}
  ],
  "name": [
	{
	  "use": "official",
	  "family": [
		"Doe"
	  ],
	  "given": [
		"John",
		"W."
	  ]
	}
  ],
  "gender": "male",
  "birthDate": "2012-02-14",
  "address": [
	{
	  "extension": [
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber",
		  "valueString": "535"
		},
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetName",
		  "valueString": "Sheppard"
		},
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetNameType",
		  "valueString": "Avenue"
		},
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-direction",
		  "valueString": "West"
		},
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-unitID",
		  "valueString": "1907"
		},
		{
		  "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-postBox",
		  "valueString": "1234"
		},
		{
		  "url": "[base-structure]/ca-on-address-rural-route",
		  "valueString": "66"
		},
		{
		  "url": "[base-structure]/ca-on-address-station",
		  "valueString": "A"
		},
		{
		  "url": "[base-structure]/ca-on-address-retail-postal-office",
		  "valueString": "123"
		}
	  ],
	  "use": "home",
	  "line": [
	    "535 Sheppard Avenue West, Unit 1234",
	    "RR 66, Station A, RPO 123"
	  ],
	  "city": "Toronto",
	  "state": "ON",
	  "postalCode": "M3H4X8"
	}
  ],
  "contact": [
	{
	  "organization": {
		"reference": "Organization/OrgSchool1"
	  }
	}
  ]
}

 */
public class Patient extends OscarFhirResource< org.hl7.fhir.dstu3.model.Patient, Demographic > {

	public Patient( org.oscarehr.common.model.Demographic from ) {
		super( new org.hl7.fhir.dstu3.model.Patient(), from );
	}

	public Patient( org.hl7.fhir.dstu3.model.Patient from ) {
		super( new Demographic(), from );
	}

	@Override
	public List<Resource> getContainedFhirResources() {
		return getFhirResource().getContained();
	}

	@Override
	protected void mapAttributes( Demographic demographic ) {
		setName( demographic );		
		setGender( demographic );
		setAddress( demographic );
		setTelecom( demographic );
		setBirthdate( demographic );
		setHIN( demographic ); 
	}
	
	@Override
	protected void mapAttributes( org.hl7.fhir.dstu3.model.Patient patient, ResourceModifierFilterInterface filter ) {
		
		System.out.println( filter.getMessage() );
		
		setName( patient );		
		setGender( patient );
		setAddress( patient );
		setTelecom( patient );
		setBirthdate( patient );
		setIdentifier( patient ); // ie: HIN and demographic_no
		setLanguage( patient );
	}
	
	@Override
	protected void setId( org.hl7.fhir.dstu3.model.Patient patient ) {	
		patient.setId( getOscarResource().getDemographicNo() + "" );
	}

	private void setName( org.hl7.fhir.dstu3.model.Patient patient ) {
		patient.addName().setUse( NameUse.OFFICIAL )
			.setFamily( getOscarResource().getLastName() )
			.addGiven( getOscarResource().getFirstName() )
			.addPrefix( getOscarResource().getTitle() )
			.getExtensionFirstRep().setUrl("http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier");
	}
	
	private void setName( Demographic demographic ) {
		List<HumanName> humanNames = getFhirResource().getName();
		for( HumanName humanName : humanNames ) {
			if( NameUse.OFFICIAL.equals( humanName.getUse() ) ) {
				demographic.setFirstName( humanName.getGivenAsSingleString() );
				demographic.setLastName( humanName.getFamily() );
			}
		}		
	}

	private void setGender( org.hl7.fhir.dstu3.model.Patient patient ) {
		Gender gender = Gender.valueOf( getOscarResource().getSex().toUpperCase() );
		patient.setGender( EnumMappingUtil.genderToAdministrativeGender( gender ) );
	}
	
	private void setGender( Demographic demographic ) {
		org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender gender = getFhirResource().getGender();
		demographic.setSex( EnumMappingUtil.administrativeGenderToGender( gender ).name() );
	}
	
	private void setAddress( org.hl7.fhir.dstu3.model.Patient patient ) {
		patient.addAddress()
				.setUse( AddressUse.HOME )				
				.addLine( getOscarResource().getAddress() )
				.setCity( getOscarResource().getCity() )
				.setState( getOscarResource().getProvince() )
				.setPostalCode( getOscarResource().getPostal() );
	}
	
	private void setAddress( Demographic demographic ) {
		Address address = getFhirResource().getAddressFirstRep();
		demographic.setAddress( MiscUtils.fhirAddressLineToString( address ) );
		demographic.setCity(address.getCity());
		demographic.setProvince(address.getState());
		demographic.setPostal(address.getPostalCode());
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.Patient patient ) {
			patient.addTelecom().setUse( ContactPointUse.HOME )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getPhone() )
			.setRank( 1 );
			
			patient.addTelecom().setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getPhone2() )
			.setRank( 2 );

			patient.addTelecom().setUse( ContactPointUse.HOME )
			.setSystem( ContactPointSystem.EMAIL)
			.setValue( getOscarResource().getEmail() )
			.setUse( ContactPointUse.HOME );
	}
	
	private void setTelecom( Demographic demographic ) {
		List<ContactPoint> contactList = getFhirResource().getTelecom();
		demographic.setPhone( MiscUtils.getFhirPhone(contactList) );
		demographic.setEmail( MiscUtils.getFhirEmail(contactList) );
	}
	
	private void setBirthdate( org.hl7.fhir.dstu3.model.Patient patient ) {
		patient.setBirthDate( new Date( getOscarResource().getBirthDay().getTimeInMillis() ) );
	}
	
	private void setBirthdate( Demographic demographic ) {
		Calendar birthdate = Calendar.getInstance();
		birthdate.setTime( getFhirResource().getBirthDate() );
		demographic.setBirthDay( birthdate );
	}
	
	private void setIdentifier( org.hl7.fhir.dstu3.model.Patient patient ) {		
		//TODO: ensure that the system identifier is region specific.
		patient.addIdentifier().setUse( IdentifierUse.OFFICIAL )
			.setSystem( "http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-patient-hcn" )
			.setValue( getOscarResource().getHin() );
		
		patient.addIdentifier().setUse( IdentifierUse.SECONDARY )
		.setSystem("[oscar URI]")
		.setValue( getOscarResource().getDemographicNo() + "" );
	}

	private void setHIN( Demographic demographic ) {
		String hin = MiscUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() );
		demographic.setHin( hin );
	}
	
	private void setLanguage(  org.hl7.fhir.dstu3.model.Patient patient ) {
		PatientCommunicationComponent communication = new PatientCommunicationComponent();
		//TODO: this should translate the languages contained in the Demographic
		communication.getLanguage().setText("en-US");
		patient.addCommunication(communication);
	}
	
	/**
	 * Only for embedded (or contained) CareProvider resources. Some FHIR Implementers discourage this.
	 */
	public void addCareProvider( org.oscarehr.common.model.Provider careProvider ) {	
		addCareProvider( new org.oscarehr.integration.fhir.model.Practitioner( careProvider ) );
	}

	public void addCareProvider( org.hl7.fhir.dstu3.model.Practitioner careProvider ) {
		getFhirResource().addGeneralPractitioner().setResource( careProvider );
	}

	public void addCareProvider( org.oscarehr.integration.fhir.model.Practitioner careProvider ) {
		addCareProvider( careProvider.getFhirResource() );
	}
	
	/**
	 * Only for contained Organization resources. ie: clinic
	 */
	public void addOrganization( org.hl7.fhir.dstu3.model.Organization organization ) {
		getFhirResource().addGeneralPractitioner().setResource( organization  );
	}
	
	public void addOrganization( org.oscarehr.integration.fhir.model.Organization organization ) {
		addOrganization( organization.getFhirResource() );
	}
	

	public void addOrganization( org.oscarehr.common.model.Clinic clinic ) {
		addOrganization( new Organization( clinic ) );
	}
	
	public void addOrganization( org.oscarehr.common.model.Contact clinic ) {
		addOrganization( new Organization( clinic ) );
	}
	
	public void addManagingOrganizationReference( Reference reference ) {
		addManagingOrganizationReference( reference.getReference() );
	}
	
	public void addManagingOrganizationReference( String reference ) {
		getFhirResource().getManagingOrganization().setReference( reference );
	}

}
