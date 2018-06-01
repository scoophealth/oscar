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
package org.oscarehr.integration.fhir.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Communication;
import org.oscarehr.integration.born.BORNWbXmlGenerator;
import org.oscarehr.integration.fhir.builder.FhirCommunicationBuilder;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.manager.OscarFhirResourceManager;
import org.oscarehr.integration.fhir.resources.Settings;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;
import org.oscarehr.integration.fhir.resources.constants.Region;
import org.oscarehr.util.LoggedInInfo;

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

public class BORN {
	private static Settings SETTINGS = new Settings( FhirDestination.BORN, Region.ON );
	
	public static synchronized Set<Communication> getCommunicationResources(LoggedInInfo loggedInInfo, int[] demographicNos) {
		Set<Communication> communicationSet = null;
		Communication communication = null;
		
		for(int demographicNo : demographicNos) {			
			communication = getCommunication(loggedInInfo, demographicNo);
			
			if(communicationSet == null) {
				communicationSet = new HashSet<Communication>();
			}
			
			if(communication != null) {
				communicationSet.add(communication);
			}
		}
		
		if(communicationSet == null) {
			communicationSet = Collections.emptySet();
		}
		
		return communicationSet;
	}
	
	/**
	 * Returns a complete Communication resource for transmission through the BORN BIS
	 */
	public static synchronized Communication getCommunication(LoggedInInfo loggedInInfo, int demographicNo) {
		Communication communication = null;
		FhirCommunicationBuilder fhirCommunicationBuilder = getFhirCommunicationBuilder(loggedInInfo, demographicNo);
		
		if(fhirCommunicationBuilder != null) {
			communication = fhirCommunicationBuilder.getCommunication();
		}
		
		return communication;
	}
	
	public static synchronized FhirCommunicationBuilder getFhirCommunicationBuilder(LoggedInInfo loggedInInfo, int demographicNo) {
		OscarFhirConfigurationManager configurationManager = new OscarFhirConfigurationManager( loggedInInfo, SETTINGS );		
		FhirCommunicationBuilder fhirCommunicationBuilder = new FhirCommunicationBuilder(configurationManager);

		// Get BORN resource bundle from the resource manager. 
		
		// HashSet<AbstractOscarFhirResource<?,?>> resourceList = new HashSet<AbstractOscarFhirResource<?,?>>();
		
		// Patient
		org.oscarehr.integration.fhir.model.Patient patient = OscarFhirResourceManager.getPatientByDemographicNumber( configurationManager, demographicNo );
		
		// Practitioner
		org.oscarehr.integration.fhir.model.Practitioner practitioner = OscarFhirResourceManager.getDemographicMostResponsiblePractitioner(configurationManager, demographicNo);
		
		patient.addGeneralPractitioner(practitioner);
		fhirCommunicationBuilder.setSubject(patient);
		
		// Clinical Data
		BORNWbXmlGenerator xmlGenerator = new BORNWbXmlGenerator();
		xmlGenerator.init(demographicNo);
		
		//TODO: HIN Cannot be NULL or Empty
		
		//TODO: bla bla bla...
		
		// fhirCommunicationBuilder.addAttachment(xmlGenerator // get stream etc...);

		return fhirCommunicationBuilder;
	}
}
