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

import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.exception.MandatoryAttributeException;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.util.MiscUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;

public class Practitioner extends AbstractOscarFhirResource<org.hl7.fhir.dstu3.model.Practitioner, org.oscarehr.common.model.Provider> {

	public enum LicenseType { CPSO, CNORNP, CNORN, CNORPN, OCP, DEFAULT, CMO }

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
	protected final void mapAttributes( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		
		//Mandatory
		setName( fhirResource );
				
		try {
			setIdentifier( fhirResource );
		} catch (MandatoryAttributeException e) {
			MiscUtils.getLogger().error( "Provider number " + getOscarResource().getProviderNo() + " is missing a mandatory attribute value for practitioner number" );
		}

		if( include( OptionalFHIRAttribute.qualification ) ) {
			setQualification( fhirResource );
		}
		
		if( include( OptionalFHIRAttribute.telecom ) ) {
			
			if( include( OptionalFHIRAttribute.workPhone ) ) {
				setWorkPhone( fhirResource );
			}
			
			if( include( OptionalFHIRAttribute.otherphone ) ) {
				setOtherPhone( fhirResource );
			}
			
			if( include( OptionalFHIRAttribute.email ) ) {
				setEmail( fhirResource );
			}						
		}
	}

	@Override
	protected final void mapAttributes( Provider oscarResource ) {
		oscarResource.setProviderNo( getFhirResource().getId() );
		oscarResource.setFirstName( getFhirResource().getNameFirstRep().getGivenAsSingleString() );
		oscarResource.setLastName( getFhirResource().getNameFirstRep().getFamily() );
	}

	private final void setName( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		fhirResource.addName()
		.setFamily( getOscarResource().getLastName() )
		.addGiven( getOscarResource().getFirstName() );
	}
	
	/**
	 * In some programs the Practitioner number will be a hard requirement.
	 * @throws MandatoryAttributeException 
	 * 
	 */
	protected void setIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) throws MandatoryAttributeException {
		
		String practitionerNumber = getOscarResource().getPractitionerNo();
		
		if( ( practitionerNumber == null || practitionerNumber.isEmpty() ) && isMandatory( MandatoryFHIRAttribute.practitionerNo ) ) {
			throw new MandatoryAttributeException();
		} 
		
		//TODO these codes cannot be hard coded like this. Temporary hack
		String licensetype = getOscarResource().getPractitionerNoType();
		
		if( licensetype == null || licensetype == "" )  {
			licensetype = LicenseType.DEFAULT.name();
		}

		switch( LicenseType.valueOf( licensetype ) ) {
		case CNORN:
		case CNORPN:
		case CNORNP:
			setNurseIdentifier( fhirResource, practitionerNumber );
			break;
		case CPSO: setDoctorIdentifier( fhirResource, practitionerNumber );
			break;
		case OCP: setPharmacistIdentifier( fhirResource, practitionerNumber );
			break;
		case CMO: setMidwifeIdentifier( fhirResource, practitionerNumber );
			break;
		case DEFAULT: fhirResource.addIdentifier().setSystem( "" ).setValue( practitionerNumber );
			break;		
		}

	}
	
	protected void setNurseIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource, String practitionerNumber ) {
		fhirResource.addIdentifier()
		.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-nurse" )
		.setValue( practitionerNumber );		
	}
	
	protected void setDoctorIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource, String practitionerNumber ) {
		fhirResource.addIdentifier()
		.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-physician" )
		.setValue( practitionerNumber );
	}
	
	protected void setPharmacistIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource, String practitionerNumber ) {
		fhirResource.addIdentifier()
		.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-pharmacist" )
		.setValue( practitionerNumber );
	}
	
	protected void setMidwifeIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource, String practitionerNumber ) {
		fhirResource.addIdentifier()
		.setSystem( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-midwife" )
		.setValue( practitionerNumber );
	}
	
	protected void setQualification( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		
		//TODO these codes cannot be hard coded like this. Temporary hack
		String licensetype = getOscarResource().getPractitionerNoType();
		String designation = null;
		
		if( LicenseType.CNORN.name().equals( licensetype ) ) {
			designation =  "RN";
		}
		
		if( LicenseType.CNORPN.name().equals( licensetype ) ) {
			designation =  "RPN";
		}
		
		if( LicenseType.CNORNP.name().equals( licensetype ) ) {
			designation =  "RNP";
		}
		
		if( LicenseType.CPSO.name().equals( licensetype ) ) {
			designation = "MD";
		}
		
		if( LicenseType.OCP.name().equals( licensetype ) ) {
			designation = "PHARM";
		}
		
		if( LicenseType.CMO.name().equals( licensetype ) ) {
			designation = "RM";
		}
		
		
		if(designation != null) {
			fhirResource.addQualification().getCode().addCoding()
				.setSystem("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-immunizations-practitioner-designation")
				.setCode( designation )
				.setDisplay( designation );
		}
	}
	
	
	protected void setWorkPhone( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		if(StringUtils.isEmpty(getOscarResource().getWorkPhone())) {
			return;
		}
		fhirResource.addTelecom()
			.setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getWorkPhone() );
	}
	
	protected void setOtherPhone( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		if(StringUtils.isEmpty(getOscarResource().getPhone())) {
			return;
		}
		fhirResource.addTelecom()
			.setUse( ContactPointUse.NULL )
			.setSystem( ContactPointSystem.PHONE )
			.setValue( getOscarResource().getPhone() );
	}
	
	protected void setEmail( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		if(StringUtils.isEmpty(getOscarResource().getEmail())) {
			return;
		}
		fhirResource.addTelecom()
			.setUse( ContactPointUse.WORK )
			.setSystem( ContactPointSystem.EMAIL )
			.setValue( getOscarResource().getEmail() );
	}

}
