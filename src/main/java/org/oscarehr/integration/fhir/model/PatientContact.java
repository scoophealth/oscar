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
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.primitive.PositiveIntDt;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * Converts an Oscar Contact entity into a FHIR Patient.Contact
 *  Patient.
 *  "contact" : [{ // A contact party (e.g. guardian, partner, friend) for the patient
    "relationship" : [{ CodeableConcept }], // The kind of relationship
    "name" : { HumanName }, // A name associated with the contact person
    "telecom" : [{ ContactPoint }], // A contact detail for the person
    "address" : { Address }, // Address for the contact person
    "gender" : "<code>", // male | female | other | unknown
    "organization" : { Reference(Organization) }, // C? Organization that is associated with the contact
    "period" : { Period } // The period during which this contact person or organization is valid to be contacted relating to this patient
  }],
 * 
 * Extracts the Location info from a HealthcareService.
 * {
	  "resourceType" : "HealthcareService",
	  // from Resource: id, meta, implicitRules, and language
	  // from DomainResource: text, contained, extension, and modifierExtension
	  "identifier" : [{ Identifier }], // External identifiers for this item
	  "providedBy" : { Reference(Organization) }, // Organization that provides this service
	  "serviceCategory" : { CodeableConcept }, // Broad category of service being performed or delivered
	  "serviceType" : [{ // Specific service delivered or performed
	    "type" : { CodeableConcept }, // R!  Type of service delivered or performed
	    "specialty" : [{ CodeableConcept }] // Specialties handled by the Service Site
	  }],
	  "location" : { Reference(Location) }, // R!  Location where service may be provided
	  "serviceName" : "<string>", // Description of service as presented to a consumer while searching
	  "comment" : "<string>", // Additional description and/or any specific issues not covered elsewhere
	  "extraDetails" : "<string>", // Extra details about the service that can't be placed in the other fields
	  "photo" : { Attachment }, // Facilitates quick identification of the service
	  "telecom" : [{ ContactPoint }], // Contacts related to the healthcare service
	  "coverageArea" : [{ Reference(Location) }], // Location(s) service is inteded for/available to
	  "serviceProvisionCode" : [{ CodeableConcept }], // Conditions under which service is available/offered
	  "eligibility" : { CodeableConcept }, // Specific eligibility requirements required to use the service
	  "eligibilityNote" : "<string>", // Describes the eligibility conditions for the service
	  "programName" : ["<string>"], // Program Names that categorize the service
	  "characteristic" : [{ CodeableConcept }], // Collection of characteristics (attributes)
	  "referralMethod" : [{ CodeableConcept }], // Ways that the service accepts referrals
	  "publicKey" : "<string>", // PKI Public keys to support secure communications
	  "appointmentRequired" : <boolean>, // If an appointment is required for access to this service
	  "availableTime" : [{ // Times the Service Site is available
	    "daysOfWeek" : ["<code>"], // mon | tue | wed | thu | fri | sat | sun
	    "allDay" : <boolean>, // Always available? e.g. 24 hour service
	    "availableStartTime" : "<time>", // Opening time of day (ignored if allDay = true)
	    "availableEndTime" : "<time>" // Closing time of day (ignored if allDay = true)
	  }],
	  "notAvailable" : [{ // Not available during this time due to provided reason
	    "description" : "<string>", // R!  Reason presented to the user explaining why time not available
	    "during" : { Period } // Service not availablefrom this date
	  }],
	  "availabilityExceptions" : "<string>" // Description of availability exceptions
	}
 * @param <T>
 */
public class PatientContact extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.Patient, Contact >  {
	
	private org.oscarehr.common.model.Contact contact;
	private ProfessionalSpecialist professionalSpecialist;
	private org.oscarehr.common.model.DemographicContact demographicContact;
	private org.oscarehr.common.model.DemographicContact[] demographicContacts;
	private org.oscarehr.common.model.Contact[] contacts;
	private ca.uhn.fhir.model.dstu2.resource.Patient.Contact[] fhirContacts;
	private ca.uhn.fhir.model.dstu2.resource.Patient.Contact fhirContact;
	private List<CodeableConceptDt> contactRelationships; 

	public PatientContact( org.oscarehr.common.model.Contact[] contacts ) {
		this.setContacts( contacts );
		this.setFhirContacts( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact[ contacts.length ] );
	}
	
	public PatientContact( org.oscarehr.common.model.Contact contact ) {
		setContact( contact );
		setFhirContact( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact() );
	}
	
	/**
	 * Demographic Contact wraps around a Contact entity. This wrapper contains meta data
	 * about the contact, such as relationship and legal status.
	 */
	public PatientContact( org.oscarehr.common.model.DemographicContact demographicContact ) {
		setDemographicContact( demographicContact );
		setFhirContactFromDemographicContact( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact() );
	}
	
	/**
	 * Demographic Contact wraps around a Contact entity. This wrapper contains meta data
	 * about the contact, such as relationship and legal status.
	 */
	public PatientContact( org.oscarehr.common.model.DemographicContact[] demographicContacts ) {
		setDemographicContacts( demographicContacts );
		setFhirContactFromDemographicContact( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact[ demographicContacts.length ] );
	}

	/**
	 * Extract the contact info from a FHIR Location or Organization resource.
	 */
	public PatientContact( IBaseResource fhirResource ) {

		if( fhirResource instanceof Location ) {
			setContactFromLocation( ( Location ) fhirResource );
		}
		
		if( fhirResource instanceof Organization ) {
			setContactFromOrganization( ( Organization ) fhirResource );
		}
	
		if( this.contact != null ) {
			setProfessionalSpecialist( new ProfessionalSpecialist() );
		}

	}
	
	@Override
	protected void mapAttributes(Patient fhirResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mapAttributes( Contact oscarResource ) {
		// TODO Auto-generated method stub		
	}
	

	/**
	 * Get the Oscar Entity Contact.
	 */
	public org.oscarehr.common.model.Contact getContact() {
		return contact;
	}

	private void setContact( org.oscarehr.common.model.Contact contact ) {
		this.contact = contact;
	}

	public ca.uhn.fhir.model.dstu2.resource.Patient.Contact getFhirContact() {
		return fhirContact;
	}

	public org.oscarehr.common.model.Contact[] getContacts() {
		return contacts;
	}

	private void setContacts( org.oscarehr.common.model.Contact[] contacts) {
		this.contacts = contacts;
	}

	public org.oscarehr.common.model.DemographicContact getDemographicContact() {
		return demographicContact;
	}

	private void setDemographicContact(org.oscarehr.common.model.DemographicContact demographicContact) {
		this.demographicContact = demographicContact;
	}

	public org.oscarehr.common.model.DemographicContact[] getDemographicContacts() {
		return demographicContacts;
	}

	private void setDemographicContacts(org.oscarehr.common.model.DemographicContact[] demographicContacts) {
		this.demographicContacts = demographicContacts;
	}

	public ca.uhn.fhir.model.dstu2.resource.Patient.Contact[] getFhirContacts() {
		return fhirContacts;
	}

	public ProfessionalSpecialist getProfessionalSpecialist() {
		return professionalSpecialist;
	}

	private void setProfessionalSpecialist(ProfessionalSpecialist professionalSpecialist) {		
		professionalSpecialist.setReferralNo( ( (ProfessionalContact ) contact ).getCpso() );
		professionalSpecialist.setLastName( contact.getLastName() );

		String address2 = contact.getAddress2();
		String city = contact.getCity();
		String province = contact.getProvince();
		String postal = contact.getPostal();
		
		StringBuilder stringBuilder = new StringBuilder("");
		stringBuilder.append( contact.getAddress() );
		
		if( address2 != null ) {
			stringBuilder.append( address2 );
		}
		
		if( city != null ) {
			stringBuilder.append( "," + city );
		}
		
		if( province != null ) {
			stringBuilder.append( "," + province );
		}
		
		if( postal != null ) {
			stringBuilder.append( "," + postal );
		}
		
		professionalSpecialist.setStreetAddress( stringBuilder.toString() );
			
		professionalSpecialist.setLastUpdated( contact.getUpdateDate() );
		professionalSpecialist.setPhoneNumber( contact.getWorkPhone() );
		professionalSpecialist.setFaxNumber( contact.getFax() );
		professionalSpecialist.setWebSite( contact.getNote() );
		professionalSpecialist.setAnnotation( contact.getNote() );
		professionalSpecialist.setEmailAddress( contact.getEmail() );
		
		this.professionalSpecialist = professionalSpecialist;
	}

	public List<CodeableConceptDt> getContactRelationships() {
		return contactRelationships;
	}

	public void setContactRelationships(List<CodeableConceptDt> contactRelationships) {
		this.contactRelationships = contactRelationships;
	}

	private void setFhirContactFromDemographicContact( ca.uhn.fhir.model.dstu2.resource.Patient.Contact contact ) {
		if( demographicContact != null ) {
			setContact( demographicContact.getDetails() );			
			contact.addRelationship().addCoding().setCode("Relationship").setDisplay( demographicContact.getRole() );
			contact.addRelationship().addCoding().setCode("Emergency").setDisplay( demographicContact.getEc() );			
			contact.addRelationship().addCoding().setCode("Legal").setDisplay( demographicContact.getSdm() );
			setFhirContact( contact );
		}
	}
	
	private void setFhirContactFromDemographicContact( ca.uhn.fhir.model.dstu2.resource.Patient.Contact[] contacts ) {
		if( demographicContacts != null ) {
			for( int i = 0; i < demographicContacts.length; i++ ) {
				this.setDemographicContact( demographicContacts[i] );
				this.setFhirContactFromDemographicContact( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact() );
				contacts[i] = this.fhirContact;
				this.setDemographicContact( null );
				this.setFhirContact( null );
			}
		}
		this.fhirContacts = contacts;
	}

	private void setFhirContacts(ca.uhn.fhir.model.dstu2.resource.Patient.Contact[] fhirContacts) {
		if( contacts != null ) {
			for( int i = 0; i < contacts.length; i++ ) {
				this.setContact( contacts[i] );
				this.setFhirContact( new ca.uhn.fhir.model.dstu2.resource.Patient.Contact() );
				fhirContacts[i] = this.fhirContact;
				this.setContact( null );
				this.setFhirContact( null );
			}
		}
		this.fhirContacts = fhirContacts;
	}
	
	private void setContactFromOrganization( Organization organization ) {
		
		AddressDt mainAddress = organization.getAddressFirstRep();
		String address = mainAddress.getText();
		String address2 = "";

		if( address == null  ) {
			address = mainAddress.getLineFirstRep().getValue();			
			List<StringDt> addressLines = organization.getAddressFirstRep().getLine(); 
			address = addressLines.get(0).getValueAsString();
			if( addressLines.size() > 1 ) {
				address2 = addressLines.get(1).getValueAsString();
			}
		}
//		setContact( organization.getName(), 
//				address,
//				address2,
//				mainAddress.getCity(), 
//				mainAddress.getState(), 
//				mainAddress.getPostalCode(),
//				organization.getTelecom() );
	}
	
	private void setContactFromLocation( Location location ) {
		
		String address = location.getAddress().getText();
		String address2 = "";
		if( address == null ) {
			List<StringDt> addressLines = location.getAddress().getLine(); 
			address = addressLines.get(0).getValueAsString();
			if( addressLines.size() > 1 ) {
				address2 = addressLines.get(1).getValueAsString();
			}
		}
//		
//		setContact( location.getName(), 
//				address,
//				address2,
//				location.getAddress().getCity(), 
//				location.getAddress().getState(), 
//				location.getAddress().getPostalCode(),
//				identifier,
//				location.getTelecom() );
	}
//	
//	private void setContact( String lastName, String address, String address2, String city, 
//			String province, String postal, String uniqueIdentifier, List<ContactPointDt> telecom  ) {
//		
//		setContact( new ProfessionalContact() );
//		
//		contact.setLastName( lastName );
//		contact.setAddress( address );
//		contact.setAddress2( address2 );
//		contact.setCity( city );
//		contact.setProvince( province );
//		contact.setUpdateDate( new Date( System.currentTimeMillis() ) );
//		contact.setPostal( postal );
//		
//		( ( ProfessionalContact ) contact ).setCpso( uniqueIdentifier );
//
//		for( ContactPointDt contactPoint : telecom ) {
//			String value = contactPoint.getValue();
//			switch( contactPoint.getSystemElement().getValueAsEnum() ) {
//			case PHONE : contact.setWorkPhone( value );
//				break;
//			case FAX : contact.setFax( value );
//				break;
//			case EMAIL : contact.setEmail( value );
//				break;
//			case PAGER : contact.setCellPhone( value );
//				break;
//			case URL : contact.setNote( value );
//				break;
//			default:
//				break;
//			}
//		}
//	}

	/**
	 * Get a FHIR Contact object.
	 */
	private void setFhirContact( ca.uhn.fhir.model.dstu2.resource.Patient.Contact fhirContact ) {

		if( fhirContact != null && getContact() != null ) {
			fhirContact.getName().addFamily( getContact().getLastName() );
			fhirContact.getName().addGiven( getContact().getFirstName() );
			fhirContact.getAddress().addLine( getContact().getAddress() );
			fhirContact.getAddress().setCity( getContact().getCity() );
			fhirContact.getAddress().setState( getContact().getProvince() );
			fhirContact.getAddress().setPostalCode( getContact().getPostal() );
			
			fhirContact.addTelecom()
				.setSystem( ContactPointSystemEnum.PHONE )
				.setValue( getContact().getResidencePhone() )
				.setRank( new PositiveIntDt(1) );
			
			fhirContact.addTelecom()
				.setSystem( ContactPointSystemEnum.PHONE )
				.setValue( getContact().getWorkPhone() )
				.setRank( new PositiveIntDt(2) );
		
			fhirContact.addTelecom()
				.setSystem( ContactPointSystemEnum.FAX )
				.setValue( getContact().getFax() );
			
			fhirContact.addTelecom()
				.setSystem( ContactPointSystemEnum.EMAIL)
				.setValue( getContact().getEmail() )
				.setUse( ContactPointUseEnum.HOME );
			
			if( this.contactRelationships != null ) {
				fhirContact.getRelationship().addAll( this.contactRelationships );
			}
		}
		this.fhirContact = fhirContact;
	}


}
