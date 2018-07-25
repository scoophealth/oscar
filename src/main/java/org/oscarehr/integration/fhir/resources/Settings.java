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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;
import org.oscarehr.integration.fhir.resources.constants.Region;

/** 
 * TODO this will eventually load a properties file of settings.
 *
 */
public class Settings {
	
	private boolean includeSenderEndpoint;
	private Region region;
	private FhirDestination fhirDestination;
	
	public Settings( FhirDestination fhirDestination , Region region ) {
		this.region = region;
		this.fhirDestination = fhirDestination;
	}

	public boolean isIncludeSenderEndpoint() {
		return includeSenderEndpoint;
	}

	public void setIncludeSenderEndpoint(boolean includeSenderEndpoint) {
		this.includeSenderEndpoint = includeSenderEndpoint;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public FhirDestination getFhirDestination() {
		return fhirDestination;
	}

	public void setFhirDestination(FhirDestination fhirDestination) {
		this.fhirDestination = fhirDestination;
	}
	
	 public String toString() {
	     return ReflectionToStringBuilder.toString(this);
	 }

}
