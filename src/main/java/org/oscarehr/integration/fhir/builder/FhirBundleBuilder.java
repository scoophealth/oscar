package org.oscarehr.integration.fhir.builder;
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
import java.util.UUID;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MessageHeader;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.AbstractOscarFhirResource;
import org.oscarehr.integration.fhir.model.Sender;

/*
 * {doco
  "resourceType" : "Bundle",
  // from Resource: id, meta, implicitRules, and language
  "identifier" : { Identifier }, // Persistent identifier for the bundle
  "type" : "<code>", // R!  document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection
  "total" : "<unsignedInt>", // C? If search, the total number of matches
  "link" : [{ // Links related to this Bundle
    "relation" : "<string>", // R!  See http://www.iana.org/assignments/link-relations/link-relations.xhtml#link-relations-1
    "url" : "<uri>" // R!  Reference details for the link
  }],
  "entry" : [{ // Entry in the bundle - will have a resource, or information
    "link" : [{ Content as for Bundle.link }], // Links related to this entry
    "fullUrl" : "<uri>", // Absolute URL for resource (server address, or UUID/OID)
    "resource" : { Resource }, // A resource in the bundle
    "search" : { // C? Search related information
      "mode" : "<code>", // match | include | outcome - why this is in the result set
      "score" : <decimal> // Search ranking (between 0 and 1)
    },
    "request" : { // C? Transaction Related Information
      "method" : "<code>", // R!  GET | POST | PUT | DELETE
      "url" : "<uri>", // R!  URL for HTTP equivalent of this entry
      "ifNoneMatch" : "<string>", // For managing cache currency
      "ifModifiedSince" : "<instant>", // For managing update contention
      "ifMatch" : "<string>", // For managing update contention
      "ifNoneExist" : "<string>" // For conditional creates
    },
    "response" : { // C? Transaction Related Information
      "status" : "<string>", // R!  Status response code (text optional)
      "location" : "<uri>", // The location, if the operation returns a location
      "etag" : "<string>", // The etag for the resource (if relevant)
      "lastModified" : "<instant>", // Server's date time modified
      "outcome" : { Resource } // OperationOutcome with hints and warnings (for batch/transaction)
    }
  }],
  "signature" : { Signature } // Digital Signature
}
 * 
 */

public class FhirBundleBuilder extends AbstractFhirMessageBuilder<Bundle> {

	/**
	 * Build a bundle without the header.  
	 * The header can be added in later with the resources. 
	 */
	public FhirBundleBuilder( MessageHeader messageHeader ) {
		super( messageHeader );
		setBundle( new Bundle() );
	}
	
	/**
	 * To build a bundle with a header from the sender and destination objects.
	 */
	public FhirBundleBuilder( Sender sender, Destination destination ) {
		super( sender, destination );
		setBundle( new Bundle() );
	}
	
	public FhirBundleBuilder( OscarFhirConfigurationManager configurationManager ) {
		super( configurationManager );
		setBundle( new Bundle() );
	}

	public Bundle getBundle() {
		return getWrapper();
	}

	private void setBundle( Bundle bundle ) {
		bundle.setId( UUID.randomUUID().toString() );
		bundle.setType( BundleType.MESSAGE );		
		setWrapper( bundle );
		initResources();
	}	
	
	private void initResources() {
		MessageHeader messageHeader = getMessageHeader();
		addResource( messageHeader );
	}
	
	public void addResources( HashSet< AbstractOscarFhirResource< ?,? > > oscarFhirResources ) {
		for( AbstractOscarFhirResource< ?,? > oscarFhirResource :  oscarFhirResources ) {
			addResource( oscarFhirResource );
		}
	}

	@Override
	public void addResource( BaseResource resource ) {
		getBundle().addEntry().setResource( (Resource) resource );
	}

	@Override
	protected void addAttachment( Attachment attachment ) {
		// unused	
	}

}
