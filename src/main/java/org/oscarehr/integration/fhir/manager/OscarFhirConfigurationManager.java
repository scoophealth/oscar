package org.oscarehr.integration.fhir.manager;
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

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.oscarehr.integration.fhir.builder.DestinationFactory;
import org.oscarehr.integration.fhir.builder.ResourceAttributeFilterFactory;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.resources.FhirConfiguration;
import org.oscarehr.integration.fhir.resources.ResourceAttributeFilter;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;
import org.oscarehr.util.MiscUtils;

public class OscarFhirConfigurationManager {

	private static Logger logger = MiscUtils.getLogger();
	
	private FhirDestination fhirDestination;
	private BaseResource targetResource;
	private Destination destination;
	private ResourceAttributeFilter resourceAttributeFilter;
	private FhirConfiguration fhirConfiguration;
	
	/**
	 * Every configuration Object is set based on the FHIR destination.
	 */
	public OscarFhirConfigurationManager( FhirDestination fhirDestination ) {
		this.fhirDestination = fhirDestination;
		this.destination = DestinationFactory.getDestination( fhirDestination );
	}
	
	public BaseResource getTargetResource() {
		return targetResource;
	}
	
	public void setTargetResource( BaseResource targetResource ) {
		
		logger.info( "Setting resource attribute filter for " + targetResource.getClass().getName() );
		
		resourceAttributeFilter = ResourceAttributeFilterFactory.getFilter( fhirDestination, targetResource );	
		// TODO FHIR CONFIGURATION HERE.
		this.targetResource = targetResource;
	}
	
	public Destination getDestination() {
		return destination;
	}

	public ResourceAttributeFilter getResourceAttributeFilter() {
		return resourceAttributeFilter; 
	}
	
	/**
	 * FHIR Configuration has 3 states:
	 * Global:  all FHIR config attributes
	 * Location: all FHIR config attributes specific to the destination.
	 * Resource: all FHIR config attributes specific to the destination and FHIR resource
	 */
	public FhirConfiguration getFhirConfiguration( ) {
		return this.fhirConfiguration;
	}
	
}
