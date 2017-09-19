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

import org.hl7.fhir.dstu3.model.Enumerations.ResourceType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.common.model.AbstractModel;

import ca.uhn.fhir.context.FhirContext;


public abstract class OscarFhirResource< FHIR extends org.hl7.fhir.dstu3.model.BaseResource, OSCAR extends AbstractModel<?> > {

	private FhirContext fhirContext;
	private FHIR fhirResource;
	private OSCAR oscarResource;
		
	protected abstract void setId( FHIR fhirResource );
	protected abstract void setId( OSCAR oscarResource );
	protected abstract void mapAttributes( FHIR fhirResource );
	protected abstract void mapAttributes( OSCAR oscarResource );
	public abstract List<Extension> getFhirExtensions();
	public abstract List<Resource> getContainedFhirResources();
	
	protected OscarFhirResource() {
		// default constructor. Use at own risk.
	}
	
	protected OscarFhirResource( OSCAR to, FHIR from ){
		setResource( to, from );
	}
	
	protected OscarFhirResource( FHIR to, OSCAR from ) {
		setResource( to, from );
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
			mapAttributes( fhirResource );
		}

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
		return fhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString( getFhirResource() );
	}
	
	public String getFhirXML() {
		return fhirContext.newXmlParser().encodeResourceToString( getFhirResource() );
	}
}
