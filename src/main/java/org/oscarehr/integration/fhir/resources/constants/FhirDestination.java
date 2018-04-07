package org.oscarehr.integration.fhir.resources.constants;
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


public enum FhirDestination {

	// add more locations here in this format: ID (["Title or Description"], ["endpoint"] )
	BORN ( "BORN Immunization Data Centre", "https://the.datacentre.com/fhir/immun/data/" ),
	DHIR ( "DHIR", "https://wsgateway.prod.ehealthontario.ca/API/FHIR/Immunizations/v3/clinician/" );
		
	private final String title;
	private final String endpoint;
	
	private FhirDestination( String title, String endpoint ) {
		this.title = title;
		this.endpoint = endpoint;
	}
	
	public final String title() { return title; }
	public final String endpoint() { return endpoint; }

}
