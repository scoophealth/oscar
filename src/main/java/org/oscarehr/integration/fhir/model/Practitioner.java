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
import org.oscarehr.integration.fhir.exception.MandatoryAttributeException;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.util.MiscUtils;
import java.util.List;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;

public class Practitioner extends OscarFhirResource<org.hl7.fhir.dstu3.model.Practitioner, org.oscarehr.common.model.Provider> {

	private enum OptionalFHIRAttribute { practitionerNo, workPhone, qualification, oneid }
	private enum LicenseType { CPSO, CNO }
	/**
	 * Performer: a practitioner whom executed the medical service on a patient
	 * Submitter: the practitioner whom is most responsible for the patient
	 */
	public enum ActorType {performer, sumbitter}
	
	private ActorType actor;
	private Clinic location;

	public Practitioner( Provider provider ) {
		super( new org.hl7.fhir.dstu3.model.Practitioner(), provider );
	}
	
	public Practitioner( Provider provider, OscarFhirConfigurationManager configurationManager ) {
		super( new org.hl7.fhir.dstu3.model.Practitioner(), provider, configurationManager );
	}
	
	public Practitioner( org.hl7.fhir.dstu3.model.Practitioner practitioner ) {
		super(new Provider(), practitioner );
	}

	@Override
	protected void setId(org.hl7.fhir.dstu3.model.Practitioner fhirResource) {
		fhirResource.setId( getOscarResource().getProviderNo() );		
	}

	@Override
	protected void mapAttributes( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		
		//Mandatory
		setName( fhirResource );
				
		try {
			setIdentifier( fhirResource );
		} catch (MandatoryAttributeException e) {
			MiscUtils.getLogger().error( "Provider number " + getOscarResource().getProviderNo() + " is missing a mandatory attribute value for practitioner number" );
		}

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
	
	/**
	 * In some programs the Practitioner number will be a hard requirement.
	 * @throws MandatoryAttributeException 
	 * 
	 */
	private void setIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) throws MandatoryAttributeException {
		
		String practitionerNumber = getOscarResource().getPractitionerNo();
		
		if( ( practitionerNumber == null || practitionerNumber.isEmpty() ) && isMandatory( OptionalFHIRAttribute.practitionerNo ) ) {
			throw new MandatoryAttributeException();
		} 
		
		//TODO these codes cannot be hard coded like this. Temporary hack
		String licensetype = getOscarResource().getPractitionerNoType();
		String uri = "";
		
		if( LicenseType.CNO.name().equals( licensetype ) ) {
			uri =  "http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-nurse";
		}
		
		if( LicenseType.CPSO.name().equals( licensetype ) ) {
			uri = "http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-physician";
		}

		fhirResource.addIdentifier()
			.setSystem( uri )
			.setValue( practitionerNumber );

	}
	
	private void setQualification( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		
		//TODO these codes cannot be hard coded like this. Temporary hack
		String licensetype = getOscarResource().getPractitionerNoType();
		String designation = "OD";
		
		if( LicenseType.CNO.name().equals( licensetype ) ) {
			designation =  "RN";
		}
		
		if( LicenseType.CPSO.name().equals( licensetype ) ) {
			designation = "MD";
		}
				
		fhirResource.addQualification().getCode().addCoding()
			.setSystem("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-immunizations-practitioner-designation")
			.setCode( designation )
			.setDisplay( designation );
	}
	
	
	private void setWorkPhone( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		fhirResource.addTelecom()
			.setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getWorkPhone() );
	}

	public ActorType getActor() {
		return actor;
	}

	public void setActor(ActorType actor) {
		// TODO still not sure on this.  The provider profile should be able to indicate if 
		// what kind of actor this is.
		this.actor = actor;
	}

}
