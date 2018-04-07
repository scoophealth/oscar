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
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ProfessionalContact;
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
 * Maps data from DSTU3 Organization to Oscar Contact/ProfessionalContact and visa versa 
 *
 * An Oscar Model Clinic class can be consumed by a unique constructor. This class 
 * can also cast any model to an Oscar Model Clinic class through the castToClinic method.
 *
 */
public class Organization 
	extends OscarFhirResource< org.hl7.fhir.dstu3.model.Organization, org.oscarehr.common.model.Contact > {

	private org.oscarehr.common.model.Clinic clinic;

	public Organization( org.oscarehr.common.model.Contact contact ) {
		super( new org.hl7.fhir.dstu3.model.Organization(), contact );
	}
	
	public Organization( org.oscarehr.common.model.Contact contact, OscarFhirConfigurationManager configurationManager  ) {
		super( new org.hl7.fhir.dstu3.model.Organization(), contact, configurationManager );
	}
	
	public Organization( org.hl7.fhir.dstu3.model.Organization organization ) {
		super( new ProfessionalContact(), organization );
	}
	
	public Organization( org.oscarehr.common.model.Clinic clinic ) {
		super();
		setClinic( clinic );
	}
	
	public Organization( org.oscarehr.common.model.Clinic clinic, OscarFhirConfigurationManager configurationManager ) {
		super( configurationManager );
		setClinic( clinic );
	}

	public <T extends AbstractModel<?> > org.oscarehr.common.model.Clinic castToClinic() {
		Contact contact = getOscarResource();
		if( contact != null ) {
			Clinic clinic = new Clinic();
			clinic.setClinicName( contact.getAddress() );
			clinic.setClinicAddress( contact.getAddress2() );
			clinic.setClinicCity( contact.getCity() );
			clinic.setClinicProvince( contact.getProvince() );
			clinic.setClinicPostal( contact.getPostal() );
			
			if( contact instanceof ProfessionalContact ) {
				clinic.setClinicLocationCode( ((ProfessionalContact) contact).getCpso() );
			}
			
			clinic.setClinicFax( contact.getFax() );
			clinic.setClinicPhone( contact.getWorkPhone() );

			this.clinic = clinic;
		}
		return clinic;
	}

	private void setClinic( org.oscarehr.common.model.Clinic clinic ) {

		ProfessionalContact professionalContact = new ProfessionalContact();
		professionalContact.setId( clinic.getId() );
		professionalContact.setAddress( clinic.getClinicName() );
		professionalContact.setAddress2( clinic.getClinicAddress() );
		professionalContact.setCity( clinic.getClinicCity() );
		professionalContact.setProvince( clinic.getClinicProvince() );
		professionalContact.setPostal( clinic.getClinicPostal() );
		professionalContact.setFax( clinic.getClinicFax() );
		professionalContact.setWorkPhone( clinic.getClinicPhone() );
		professionalContact.setCpso( clinic.getClinicLocationCode() );
		
		setResource( new org.hl7.fhir.dstu3.model.Organization(), professionalContact );

		this.clinic = clinic;
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
	protected void mapAttributes( Contact oscarResource ) {
		setOranizationName( oscarResource );		
		setAddress( oscarResource );
		setTelecom( oscarResource );
		setIdentifier( oscarResource );
	}

	private void setOranizationName( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		fhirResource.setName( getOscarResource().getAddress() );
	}
	
	private void setOranizationName( Contact oscarResource ) {
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
	
	private void setAddress( Contact oscarResource ) {
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
	
	private void setTelecom( Contact oscarResource ) {		
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
		if( getOscarResource() instanceof ProfessionalContact ) {
			identifier.setSystem("").setValue( "" );
		}
		setIdentifier( identifier );
	}
	
	private void setIdentifier( Identifier identifier ) {
		if( getFhirResource().getIdentifier() != null ) {
			getFhirResource().getIdentifier().clear();
		}	
		getFhirResource().addIdentifier( identifier );
	}

	private void setIdentifier( Contact oscarResource ) {
		( (ProfessionalContact) oscarResource).setCpso( FhirUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() ));
	}
	
	/**
	 * Set a Public Health Unit Id as this organization's identifier.
	 * 
	 * This is mainly used for setting the identifier for ONTARIO's PHU ids. 
	 * Each patient in Oscar is assinged a specific PHU ID according to the patient's location in the province.
	 * Each Clinic is assigned a specific PHU ID according to the clinic's location in the province.
	 * 
	 * Please create an overhead for identifiers that have a different nomenclature, yet behave the same way.
	 */
	public void setOrganizationPHUID( String phuId ) {
		Identifier identifier = new Identifier();
		identifier.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-panorama-phu-id" ).setValue( phuId );
	}

}
