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
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.integration.fhir.utils.MiscUtils;

/*
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
	{
	  "resourceType": "Organization",
	  "id": "OrgPHU1",
	  "identifier": [
	    {
	      "system": "[id-system-local-base]/ca-on-panorama-phu-id",
	      "value": "55"
	    }
	  ],
	  "name": "Toronto Public Health"
	}
*/

/*
    {
      "resourceType": "Organization",
      "id": "Clinic1",
      "identifier": [
        {
          "system": "urn:ietf:rfc:3986",
          "value": "CLINICTEST"
        }
      ],
      "name": "Family Health Team"
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
	
	public Organization( org.hl7.fhir.dstu3.model.Organization organization ) {
		super( new ProfessionalContact(), organization );
	}
	
	public Organization( org.oscarehr.common.model.Clinic clinic ) {
		super();
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

		ProfessionalContact professionalContact =  new ProfessionalContact();
		professionalContact.setId( null );
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
		Integer id = getOscarResource().getId();
		if( id == null ) {
			id = 1;
		}
		fhirResource.setId( "#Organization_" + id );	
	}

	@Override
	protected void setId(Contact model) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected void mapAttributes(org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		setId( fhirResource );
		setOranizationName( fhirResource );		
		setAddress( fhirResource );
		setTelecom( fhirResource );
		setIdentifier( fhirResource );
	}

	@Override
	protected void mapAttributes( Contact oscarResource ) {
		setOranizationName( oscarResource );		
		setAddress( oscarResource );
		setTelecom( oscarResource );
		setIdentifier( oscarResource );
	}

	@Override
	public List<Extension> getFhirExtensions() {
		return getFhirResource().getExtension();
	}

	@Override
	public List<Resource> getContainedFhirResources() {
		return getFhirResource().getContained();
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
		oscarResource.setAddress( MiscUtils.fhirAddressLineToString( address ) );
		oscarResource.setCity( address.getCity() );
		oscarResource.setProvince( address.getState() );
		oscarResource.setPostal( address.getPostalCode() );
	}
	
	private void setTelecom( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
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

	private void setIdentifier( org.hl7.fhir.dstu3.model.Organization fhirResource ) {
		if( getOscarResource() instanceof ProfessionalContact ) {
			fhirResource.addIdentifier()
				.setUse( IdentifierUse.OFFICIAL )
				.setSystem( "urn:ietf:rfc:3986" )
				.setValue( "Test Clinic's Official Registry ID" );
		}
	}
	
	private void setIdentifier( Contact oscarResource ) {
		( (ProfessionalContact) oscarResource).setCpso( MiscUtils.getFhirOfficialIdentifier( getFhirResource().getIdentifier() ));
	}

}
