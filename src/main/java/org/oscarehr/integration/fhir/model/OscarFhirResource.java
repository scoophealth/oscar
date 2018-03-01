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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hl7.fhir.dstu3.model.Enumerations.ResourceType;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.integration.fhir.interfaces.ResourceModifierFilterInterface;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import ca.uhn.fhir.context.FhirContext;


public abstract class OscarFhirResource< FHIR extends org.hl7.fhir.dstu3.model.BaseResource, OSCAR extends AbstractModel<?> > 
 {

	private FhirContext fhirContext;
	private FHIR fhirResource;
	private OSCAR oscarResource;
	private ResourceModifierFilterInterface filter;

	/**
	 * Map attributes from an Oscar resource into a FHIR Resource. 
	 */
	protected abstract void mapAttributes( FHIR fhirResource, ResourceModifierFilterInterface filter );
	
	/**
	 * Map attributes from a FHIR resource into an Oscar Resource. 
	 */
	protected abstract void mapAttributes( OSCAR oscarResource );
	
	/**
	 * Pulls a list of resources that are contained (or embedded) inside the root resource.
	 * For instance a Most Responsible Practitioner may be contained inside a Patient resource
	 * 
	 * There shouldn't be a need to use this too often as contained resources are discouraged in FHIR.
	 */
	public abstract List<Resource> getContainedFhirResources();
	
	protected OscarFhirResource() {
		setFhirContext( FhirContext.forDstu3() );
	}
	
	protected OscarFhirResource( OSCAR to, FHIR from ){
		setFhirContext( FhirContext.forDstu3() );
		setResource( to, from );
	}
	
	protected OscarFhirResource( FHIR to, OSCAR from ) {
		setFhirContext( FhirContext.forDstu3() );
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
	
	public final List<Resource> getContainedFhirResources( ResourceType resourceType ) {
		List<Resource> resources = getContainedFhirResources();
		List<Resource> filteredResources = null;
		if( resources == null || resources.isEmpty() ) {
			return null;
		}
		
		for( Resource resource : resources ) {
			
			if( filteredResources == null ) {
				filteredResources = new ArrayList<Resource>();
			}
			
			if( resourceType.equals( resource.getResourceType() ) ) {
				filteredResources.add( resource );
			}
		}
		
		return filteredResources;
	} 
	
	public final Resource getContainedFhirResource( String resourceId ) {
		
		List<Resource> resources = getContainedFhirResources();
		Resource filteredResource = null;
		if( resources == null || resources.isEmpty() ) {
			return null;
		}
		
		for( Resource resource : resources ) {
			if( resourceId.equalsIgnoreCase( resource.getId() ) ) {
				filteredResource = resource;
				break;
			}
		}
		
		return filteredResource;
	}
	
	protected void setResource( FHIR to, OSCAR from ) {
		this.oscarResource = from;
		// this.filter = getFilter( to.getClass() );
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
		if( this.oscarResource != null ) {
			mapAttributes( fhirResource, filter );
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

	public FhirContext getFhirContext() {
		return fhirContext;
	}
	
	public void setFhirContext(FhirContext fhirContext) {
		this.fhirContext = fhirContext;
	}
	
	public String getFhirJSON() {
		return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString( getFhirResource() );
	}
	
	public String getFhirXML() {
		return fhirContext.newXmlParser().encodeResourceToString( getFhirResource() );
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

//	@Override
//	public ResourceModifierFilter getFilter( Class<?> clazz ) {
//		return getFilter( clazz );
//	}
//	
//	@Override
//	public String getMessage() {
//		return getMessage();
//	}
}
