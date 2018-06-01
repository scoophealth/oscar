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
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.oscarehr.common.Gender;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.utils.EnumMappingUtil;
import org.oscarehr.integration.fhir.utils.FhirUtils;

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
*/

public class Patient extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.Patient, org.oscarehr.common.model.Demographic > {

	public Patient( org.oscarehr.common.model.Demographic from ) {
		super( new org.hl7.fhir.dstu3.model.Patient(), from );
	}
	
	public Patient( org.oscarehr.common.model.Demographic from,  OscarFhirConfigurationManager configurationManager) {
		super( new org.hl7.fhir.dstu3.model.Patient(), from, configurationManager );
	}

	public Patient( org.hl7.fhir.dstu3.model.Patient from ) {
		super( new Demographic(), from );
	}

	@Override
	protected void mapAttributes( Demographic demographic ) {
		setPatientIdentifier( demographic ); 
		setOfficialName( demographic );		
		setGender( demographic );
		setAddress( demographic );
		setTelecom( demographic );
		setBirthdate( demographic );
		setLanguage(demographic);
	}
	
	@Override
	protected void mapAttributes( org.hl7.fhir.dstu3.model.Patient patient ) {
		setPatientIdentifier( patient ); 
		setOfficialName( patient );		
		setGender( patient );
		setAddress( patient );
		setTelecom( patient );
		setBirthdate( patient );
		setLanguage(patient);
	}
	
	@Override
	protected void setId( org.hl7.fhir.dstu3.model.Patient patient ) {	
		patient.setId( getOscarResource().getDemographicNo() + "" );
	}

	private void setOfficialName( org.hl7.fhir.dstu3.model.Patient patient ) {
		HumanName humanName = new HumanName();
		
		//mandatory
		humanName.setFamily( getOscarResource().getLastName() );
		humanName.addGiven( getOscarResource().getFirstName() );
		
		//optional
		if( include( OptionalFHIRAttribute.namePrefix ) ) {
			humanName.addPrefix( getOscarResource().getTitle() );
		}
		
		if( include( OptionalFHIRAttribute.nameUse ) ) {
			humanName.setUse( NameUse.OFFICIAL );
		}
		
		if( include( OptionalFHIRAttribute.nameExtension ) ) {
			humanName.getExtensionFirstRep().setUrl( "http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier" );
		}
		
		patient.addName( humanName );
	}
	
	private void setOfficialName( Demographic demographic ) {
		List<HumanName> humanNames = getFhirResource().getName();
		for( HumanName humanName : humanNames ) {
			demographic.setFirstName( humanName.getGivenAsSingleString() );
			demographic.setLastName( humanName.getFamily() );
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
	
	private void setLanguage( org.hl7.fhir.dstu3.model.Patient patient ) {
		if( include( OptionalFHIRAttribute.language ) ) {	
			//if("English".equals(getOscarResource().getOfficialLanguage()) || "".equals(getOscarResource().getOfficialLanguage()) ) {
				CodeableConcept cc = new CodeableConcept();
				Coding c = cc.addCoding();
				c.setSystem("https://www.hl7.org/fhir/valueset-languages.html");
				c.setCode("en-US");
				patient.addCommunication().setLanguage(cc);
			//}
		}
	}
	private void setLanguage( Demographic demographic ) {
		if(getFhirResource().getCommunication() != null && !getFhirResource().getCommunication().isEmpty()) {
			String language = getFhirResource().getCommunication().get(0).getLanguage().getCoding().get(0).getCode();
			if(language != null && language.startsWith("en-")) {
				demographic.setOfficialLanguage("English");
			}
		}
	}
	
	private void setAddress( org.hl7.fhir.dstu3.model.Patient patient ) {
		if( include( OptionalFHIRAttribute.address ) ) {	
			patient.addAddress()
				.setUse( AddressUse.HOME )				
				.addLine( getOscarResource().getAddress() )
				.setCity( getOscarResource().getCity() )
				.setState( getOscarResource().getProvince() )
				.setPostalCode( getOscarResource().getPostal() );
		}
	}	
	
	private void setAddress( Demographic demographic ) {
		Address address = getFhirResource().getAddressFirstRep();
		demographic.setAddress( FhirUtils.fhirAddressLineToString( address ) );
		demographic.setCity(address.getCity());
		demographic.setProvince(address.getState());
		demographic.setPostal(address.getPostalCode());
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.Patient patient ) {
		
		patient.addTelecom().setUse( ContactPointUse.HOME )
		.setSystem( ContactPointSystem.PHONE )
		.setValue( getOscarResource().getPhone() );
			
		//optional
		if( include( OptionalFHIRAttribute.workPhone ) ) {	
			patient.addTelecom().setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getPhone2() );
		}
		
		if( include( OptionalFHIRAttribute.email ) ) {
			patient.addTelecom().setUse( ContactPointUse.HOME )
			.setSystem( ContactPointSystem.EMAIL)
			.setValue( getOscarResource().getEmail() );
		}	
	}
	
	private void setTelecom( Demographic demographic ) {
		List<ContactPoint> contactList = getFhirResource().getTelecom();
		demographic.setPhone( FhirUtils.getFhirPhone(contactList) );
		demographic.setEmail( FhirUtils.getFhirEmail(contactList) );
	}
	
	private void setBirthdate( org.hl7.fhir.dstu3.model.Patient patient ) {
		patient.setBirthDate( new Date( getOscarResource().getBirthDay().getTimeInMillis() ) );
	}
	
	private void setBirthdate( Demographic demographic ) {
		Calendar birthdate = Calendar.getInstance();
		birthdate.setTime( getFhirResource().getBirthDate() );
		demographic.setBirthDay( birthdate );
	}
	
	private void setPatientIdentifier( org.hl7.fhir.dstu3.model.Patient patient ) {		
		patient.addIdentifier()
			.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-patient-hcn" )
			.setValue( getOscarResource().getHin() );
		
		if( include( OptionalFHIRAttribute.mrn ) ) {	
			Identifier id = patient.addIdentifier();
			id.setSystem("2.16.840.1.113883.3.239.23.269");
			CodeableConcept type = new CodeableConcept();
			type.addCoding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR");
			id.setType(type);
			id.setValue(getOscarResource().getId().toString());
			
		}
	}

	private void setPatientIdentifier( Demographic demographic ) {
		String hin = FhirUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() );
		demographic.setHin( hin );
	}

	/**
	 * Only for embedded (or contained) CareProvider resources. Some FHIR Implementers discourage this.
	 */
	public void addGeneralPractitioner( org.oscarehr.common.model.Provider careProvider ) {	
		addGeneralPractitioner( new org.oscarehr.integration.fhir.model.Practitioner( careProvider ) );
	}

	public void addGeneralPractitioner( org.oscarehr.integration.fhir.model.Practitioner careProvider ) {
		addGeneralPractitioner( careProvider.getFhirResource() );
	}
	
	/**
	 * Only for contained Organization resources. ie: clinic
	 */
	public void addGeneralPractitioner( org.oscarehr.integration.fhir.model.Organization<?> organization ) {
		addGeneralPractitioner( (IBaseResource) organization.getFhirResource() );
	}
	
	public void addGeneralPractitioner( org.hl7.fhir.dstu3.model.Organization organization ) {		
		addGeneralPractitioner( (IBaseResource) organization );
	}

	private void addGeneralPractitioner( IBaseResource resource ) {
		getFhirResource().addGeneralPractitioner().setResource( resource );
	}
	
	public void addGeneralPractitionerReference( Reference reference ) {
		getFhirResource().getGeneralPractitioner().add( reference );
	}
	
	public void addGeneralPractitionerReference( String reference ) {
		getFhirResource().addGeneralPractitioner().setReference( reference );
	}
	
	/**
	 * Set the managing organization.
	 */
	public void setManagingOrganization( org.oscarehr.common.model.Clinic clinic ) {
		setManagingOrganization( new Organization<org.oscarehr.common.model.Clinic>( clinic ) );
	}
	
	/**
	 * This will set an Organization Resource as contained.
	 * For external Resources use setManagingOrganizationReference.
	 */
	public void setManagingOrganization( Organization<?> organization ) {
		setManagingOrganizationReference( organization.getContainedReferenceLink() );
		getFhirResource().getManagingOrganization().setResource( organization.getFhirResource() );
	}
	
	public void setManagingOrganizationReference( Reference reference ) {
		setManagingOrganizationReference( reference.getReference() );
	}
	
	public void setManagingOrganizationReference( String reference ) {
		getFhirResource().getManagingOrganization().setReference( reference );
	}

}
