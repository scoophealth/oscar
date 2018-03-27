package org.oscarehr.integration.fhir.resources;
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

import org.hl7.fhir.dstu3.model.BaseResource;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;

public class FhirConfiguration {
	
	private FhirDestination fhirDestination;
	private BaseResource targetResource;
	
	public Object enumValue;
	
	public FhirConfiguration( FhirDestination fhirDestination ) {
//		this.fhirDestination = fhirDestination;
//		try {
//			Class enumClass = Class.forName( OntarioURI.class.getSimpleName() );
//			enumValue = Enum.valueOf(enumClass, "BASE");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//		}
	}

	public FhirConfiguration( FhirDestination fhirDestination, BaseResource targetResource ) {
		this.fhirDestination = fhirDestination;
		this.targetResource = targetResource;
	}
	
//	private static < T extends Enum<T> & RegionalURI > T getRegionalURI( Class<T> clazz ) {
//		return Class.forName( OntarioURI.class.getSimpleName() );
//	}

	public FhirDestination getFhirDestination() {
		return fhirDestination;
	}

	public void setFhirDestination(FhirDestination fhirDestination) {
		this.fhirDestination = fhirDestination;
	}

	public BaseResource getTargetResource() {
		return targetResource;
	}

//	public void setTargetResource(BaseResource targetResource) {
//		this.targetResource = targetResource;
//		this.enumClass.
//	}

}
