package org.oscarehr.integration.fhir.api;
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

import java.util.HashSet;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MessageHeader;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.integration.fhir.builder.FhirBundleBuilder;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.manager.OscarFhirResourceManager;
import org.oscarehr.integration.fhir.model.AbstractOscarFhirResource;
import org.oscarehr.integration.fhir.resources.Settings;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;
import org.oscarehr.integration.fhir.resources.constants.Region;
import org.oscarehr.util.LoggedInInfo;


public class DHIR {

	private static Settings SETTINGS = new Settings( FhirDestination.DHIR, Region.ON );
	
	/**
	 * Get the FhirBundleBuilder Object for all immunizations
	 * Useful for adding additional resources or adjusting the message structure.
	 */
	public static synchronized FhirBundleBuilder getFhirBundleBuilder( LoggedInInfo loggedInInfo, int demographicNo ) {
		OscarFhirConfigurationManager configurationManager = new OscarFhirConfigurationManager( loggedInInfo, SETTINGS );
		HashSet<AbstractOscarFhirResource<?,?>> resourceList = new HashSet<AbstractOscarFhirResource<?,?>>();
		org.oscarehr.integration.fhir.model.Patient patient = OscarFhirResourceManager.getPatientByDemographicNumber( configurationManager, demographicNo );
		
		// The patient is the focus resource for this type of bundle. A reference link will be inserted into the MessageHeader.focus
		patient.setFocusResource( Boolean.TRUE );
		
		org.hl7.fhir.dstu3.model.Organization publicHealthUnit = OscarFhirResourceManager.getPublicHealthUnit( configurationManager, demographicNo );

		OscarFhirResourceManager.getImmunizationResourceBundle( configurationManager, patient, resourceList );	
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( configurationManager );
		
		if(publicHealthUnit != null) {
			Reference reference = new Reference();
			reference.setReference( String.format( "%s/%s", ((Resource) publicHealthUnit).getResourceType(), publicHealthUnit.getId() ) );
			reference.setResource( publicHealthUnit );
			fhirBundleBuilder.setMessageHeaderResponsible( reference );
			fhirBundleBuilder.addResource(publicHealthUnit);
		} else {
			MiscUtils.getLogger().warn("Patient and Clinic default PHU ID is missing. Check Oscar properties file for Default PHU setting.");
		}
		
		fhirBundleBuilder.addResources(resourceList);
		
		return fhirBundleBuilder;
	}
	
	/**
	 * Get the FhirBundleBuilder Object for a specific immunization
	 * Useful for adding additional resources or adjusting the message structure.
	 */
	public static synchronized FhirBundleBuilder getFhirBundleBuilder( LoggedInInfo loggedInInfo, int demographicNo, int preventionId ) {
		
		//TODO this will be set in a properties file later. The default is false anyway. 
		SETTINGS.setIncludeSenderEndpoint( Boolean.TRUE ); 
		
		OscarFhirConfigurationManager configurationManager = new OscarFhirConfigurationManager( loggedInInfo, SETTINGS );
		HashSet<AbstractOscarFhirResource<?,?>> resourceList = new HashSet<AbstractOscarFhirResource<?,?>>();
		org.oscarehr.integration.fhir.model.Patient patient = OscarFhirResourceManager.getPatientByDemographicNumber( configurationManager, demographicNo );
		org.oscarehr.integration.fhir.model.SubmittingPractitioner submittingPractitioner = new org.oscarehr.integration.fhir.model.SubmittingPractitioner( configurationManager.getLoggedInInfo().getLoggedInProvider(), configurationManager );
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( configurationManager );
		
		// Public Health Unit requirement for Ontario submissions. 
		org.hl7.fhir.dstu3.model.Organization publicHealthUnit = OscarFhirResourceManager.getPublicHealthUnit( configurationManager, demographicNo );
			
		// the patient is the focus resource for this type of bundle. A reference link will be inserted into the MessageHeader.focus
		patient.setFocusResource( Boolean.TRUE );
		
		// set the immunizations into the resource list
		OscarFhirResourceManager.getImmunizationResourceBundle( configurationManager, patient, preventionId, resourceList );			
		
		if(publicHealthUnit != null) {
			Reference reference = new Reference();
			reference.setReference( String.format( "%s/%s", ((Resource) publicHealthUnit).getResourceType(), publicHealthUnit.getId() ) );
			reference.setResource( publicHealthUnit );
			fhirBundleBuilder.setMessageHeaderResponsible( reference );
			fhirBundleBuilder.addResource(publicHealthUnit);
		} else {
			MiscUtils.getLogger().warn("Patient and Clinic default PHU ID is missing. Check Oscar properties file for Default PHU setting.");
		}

		resourceList.add(submittingPractitioner);
		fhirBundleBuilder.addResources(resourceList);

		return fhirBundleBuilder;
	}
	
	
	/**
	 * Get the FHIR Resource Bundle.
	 * Useful if needing to extract and send specific resources separately from the bundle. 
	 * ie: send the MessageHeader as a reference back to this bundle resource.
	 */
	public static Bundle getBundleResource( LoggedInInfo loggedInInfo, int demographicNo, int preventionId ) {
		return DHIR.getFhirBundleBuilder( loggedInInfo, demographicNo, preventionId ).getBundle();
	}
	
	/**
	 * Get the raw JSON string of the entire Bundle message.
	 * For a single transmission payload.
	 */
	public static String getMessageJSON( LoggedInInfo loggedInInfo, int demographicNo, int preventionId ) {
		return DHIR.getFhirBundleBuilder( loggedInInfo, demographicNo, preventionId ).getMessageJson();
	}
	
	/**
	 * The Message Header from this bundle message. 
	 * This is useful for retrieving the destination endpoint, etc...
	 */
	public static MessageHeader getMessageHeader( LoggedInInfo loggedInInfo, int demographicNo, int preventionId) {
		return DHIR.getFhirBundleBuilder( loggedInInfo, demographicNo, preventionId).getMessageHeader();
	}

}
