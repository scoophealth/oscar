package org.oscarehr.integration.fhir.resources;

import org.hl7.fhir.dstu3.model.BaseResource;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;

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

public class FhirConfiguration {
	
	private FhirDestination fhirDestination;
	private BaseResource targetResource;
	
	public FhirConfiguration( FhirDestination fhirDestination ) {
		this.fhirDestination = fhirDestination;
	}

	public FhirConfiguration( FhirDestination fhirDestination, BaseResource targetResource ) {
		this.fhirDestination = fhirDestination;
		this.targetResource = targetResource;
	}

	public FhirDestination getFhirDestination() {
		return fhirDestination;
	}

	public void setFhirDestination(FhirDestination fhirDestination) {
		this.fhirDestination = fhirDestination;
	}

	public BaseResource getTargetResource() {
		return targetResource;
	}

	public void setTargetResource(BaseResource targetResource) {
		this.targetResource = targetResource;
	}

}
