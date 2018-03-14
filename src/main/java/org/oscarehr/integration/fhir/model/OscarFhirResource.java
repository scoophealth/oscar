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

import java.util.UUID;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.resources.FhirConfiguration;
import org.oscarehr.integration.fhir.resources.ResourceAttributeFilter;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import ca.uhn.fhir.context.FhirContext;


public abstract class OscarFhirResource< FHIR extends org.hl7.fhir.dstu3.model.BaseResource, OSCAR extends AbstractModel<?> > {
	
	private static FhirContext fhirContext = FhirContext.forDstu3();
	private FHIR fhirResource;
	private OSCAR oscarResource;
	private OscarFhirConfigurationManager configurationManager;
	
	/**
	 * Map attributes from an Oscar resource into a FHIR Resource. 
	 * 
	 */
	protected abstract void mapAttributes( FHIR fhirResource );
	
	/**
	 * Map attributes from a FHIR resource into an Oscar Resource. 
	 */
	protected abstract void mapAttributes( OSCAR oscarResource );

	protected OscarFhirResource() {
		//default constructor
	}
	
	protected OscarFhirResource( OSCAR to, FHIR from ){
		setResource( to, from );
	}
	
	protected OscarFhirResource( FHIR to, OSCAR from ) {
		setResource( to, from );
	}
	
	protected OscarFhirResource( OscarFhirConfigurationManager configurationManager ) {
		this.configurationManager = configurationManager;
	}

	protected OscarFhirResource( FHIR to, OSCAR from, OscarFhirConfigurationManager configurationManager ) {
		this.configurationManager = configurationManager;
		setResource( to, from );
	}
	
	/**
	 * Set the reference id of the FHIR Resource.  
	 * This can be set with the unique id of the Oscar Resource (ie: demographic_no) or can be 
	 * generated uniquely. 
	 * In any case, the id is a requirement. 
	 */
	protected void setId( FHIR fhirResource ) {
		fhirResource.setId( UUID.randomUUID().toString() );
	}

	protected void setResource( FHIR to, OSCAR from ) {
		this.oscarResource = from;
		setFhirResource( to );
	}
		
	protected void setResource( OSCAR to, FHIR from ) {
		this.fhirResource = from;
		setOscarResource( to );
	}
	
	public FHIR getFhirResource() {
		return fhirResource;
	}
	
	protected void setFhirResource( FHIR fhirResource ) {		
		if( configurationManager != null ) {
			configurationManager.setTargetResource( fhirResource );
		}
		
		if( this.oscarResource != null ) {
			mapAttributes( fhirResource );
		}
		
		setId( fhirResource );
		this.fhirResource = fhirResource;
	}

	public OSCAR getOscarResource() {
		return this.oscarResource;
	}
	
	protected void setOscarResource( OSCAR oscarResource ) {		
		if( this.fhirResource != null ) {
			mapAttributes( oscarResource );
		}
		
		this.oscarResource = oscarResource;
	}

	public static FhirContext getFhirContext() {
		return fhirContext;
	}
	
	public static void setFhirContext(FhirContext fhirContext) {
		OscarFhirResource.fhirContext = fhirContext;
	}
	
	public String getFhirJSON() {
		return getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString( getFhirResource() );
	}
	
	public String getFhirXML() {
		return getFhirContext().newXmlParser().encodeResourceToString( getFhirResource() );
	}
	
	public Reference getReference() {
		Reference reference = new Reference();
		reference.setReference( getReferenceLink() );
		reference.setResource( getFhirResource() );
		return reference;
	}
	
	public String getReferenceLink() {
		return String.format( "%s/%s", ((Resource) getFhirResource()).getResourceType(), getFhirResource().getId() );
	}
	
	public String getContainedReferenceLink() {
		// format the id of the Resource for a contained resource.  
		String resourceType = ( (Resource) getFhirResource() ).getResourceType().name();
		String referenceLink = String.format( "%s%s", resourceType, getFhirResource().getId().replaceAll( resourceType, "" ) );
		getFhirResource().setId( referenceLink );
		return ( "#" + referenceLink );
	}

	protected ResourceAttributeFilter getResourceAttributeFilter() {
		ResourceAttributeFilter resourceAttributeFilter = null;
		if( this.configurationManager != null  ) {
			resourceAttributeFilter = configurationManager.getResourceAttributeFilter();
		}
		return resourceAttributeFilter;
	}

	public FhirConfiguration getFhirConfiguration() {
		FhirConfiguration fhirConfiguration = null; 
		if( configurationManager != null ) {
			fhirConfiguration = configurationManager.getFhirConfiguration();
		}
		return fhirConfiguration;
	}

	public OscarFhirConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
	
	protected boolean include( Enum<?> attribute ) {
		boolean pass = Boolean.TRUE;
		if( getResourceAttributeFilter() != null ) {
			pass = getResourceAttributeFilter().includeAttribute( attribute.name() );
		}
		return pass;
	}
	
}
