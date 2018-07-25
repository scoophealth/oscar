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
import org.oscarehr.integration.fhir.resources.constants.ActorType;

public class SubmittingPractitioner extends Practitioner {

	/**
	 * The clinic practitioner whom is responsible for the patient and the submission of this patients
	 * record into an external registry. 
	 * Example: this is the practitioner whom has an Ontario ONEID account and is participating with sending patient data
	 * to an external registry. 
	 * 
	 * ConfigurationManager is required.
	 */
	public SubmittingPractitioner(Provider provider, OscarFhirConfigurationManager configurationManager) {
		super(provider, configurationManager);
		setActor( ActorType.submitting );

	}
	
	@Override
	protected final void setId(org.hl7.fhir.dstu3.model.Practitioner fhirResource) {
		fhirResource.setId( "S" + getOscarResource().getProviderNo() );		
	}
	
	@Override
	protected final void setQualification( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		if( include( OptionalFHIRAttribute.qualification ) ) {
			super.setQualification( fhirResource );
		}
	}
	
	@Override
	protected final void setIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) throws MandatoryAttributeException {		
		if( isMandatory( MandatoryFHIRAttribute.oneid ) ) {
			setOntarioOneIdentifier( fhirResource );
		} else {
			super.setIdentifier(fhirResource);
		}
	}
	
	/**
	 * Specifically used for the Ontario OneID program. Only added in the SubmittingPractitioner.
	 */
	protected final void setOntarioOneIdentifier( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) throws MandatoryAttributeException {

		String oneid = getConfigurationManager().getLoggedInInfo().getLoggedInSecurity().getOneIdEmail();
		
		if( oneid == null || oneid.isEmpty() ) {
			throw new MandatoryAttributeException( "Missing mandatory FHIR attribute OneID for Submitting Practitioner" );
		} else {
			fhirResource.addIdentifier()
			.setSystem("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-provider-oneid")
			.setValue( oneid );
		}	
	}
	
	@Override
	protected final void setOtherPhone( org.hl7.fhir.dstu3.model.Practitioner fhirResource ) {
		if( include( OptionalFHIRAttribute.otherphone ) ) {
			super.setOtherPhone( fhirResource );
		}
	}

}
