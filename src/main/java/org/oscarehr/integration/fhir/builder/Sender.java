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


import org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent;
import org.hl7.fhir.dstu3.model.Organization;
import org.oscarehr.common.model.Contact;
import org.oscarehr.integration.fhir.model.OscarFhirResource;

/*
"source": {
	    "name": "Some EMR",
	    "software": "EMR1",
	    "version": "3.1.45.AABB",
	    "endpoint": "https://www.someemr1.com/api/fhir"
	  }
 */

/*

    {
      "resourceType": "Organization",
      "id": "Clinic1",
      "identifier": [
        {
          "system": "urn:ietf:rfc:3986",
          "value": "CLINICTEST"
        }
      ],
      "name": "Family Health Team"
    }

*/

/**
 * A class that references a message senders header information.
 * This Object builds a FHIR MessageSourceComponent
 *
 * Future intentions of this class is for it to auto populate specific data 
 * according to its environment. For instance grabbing data from the OscarProperties settings.
 *
 */
public class Sender {

	private MessageSourceComponent messageSourceComponent;
	private String vendorName;
	private String softwareName;
	private String versionSignature;
	private String endpoint;
	private OscarFhirResource<Organization, Contact> oscarFhirResource;
	
	public Sender() {
		setMessageSourceComponent( new MessageSourceComponent() );
	}
	
	public Sender( String vendorName, String softwareName, String versionSignature, String endpoint ) {
		this();
		setVendorName(vendorName);
		setSoftwareName(softwareName);
		setVersionSignature(versionSignature);
		setEndpoint(endpoint);
	}

	public MessageSourceComponent getMessageSourceComponent() {
		return messageSourceComponent;
	}

	/**
	 * Returns a component object that can be used inside FHIR resources.
	 */
	private void setMessageSourceComponent( MessageSourceComponent messageSourceComponent ) {
		this.messageSourceComponent = messageSourceComponent;
	}

	public String getVendorName() {
		return vendorName;
	}

	/**
	 * The vendor name for this Oscar Software product 
	 * ie: Oscar EMR or Oscar Service Provider name.
	 */
	public void setVendorName(String vendorName) {
		getMessageSourceComponent().setName( vendorName );
		this.vendorName = vendorName;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	/**
	 * The name of this Oscar software product
	 * ie: CAISI, Oscar 12, Oscar 15.
	 */
	public void setSoftwareName(String softwareName) {
		getMessageSourceComponent().setSoftware(softwareName);
		this.softwareName = softwareName;
	}

	public String getVersionSignature() {
		return versionSignature;
	}

	/**
	 * The version signature of this Oscar Software
	 * ie: version 15 build 657 2017
	 */
	public void setVersionSignature(String versionSignature) {
		getMessageSourceComponent().setVersion(versionSignature);
		this.versionSignature = versionSignature;
	}

	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * The end point connection to this Oscar server.
	 * ie: https://my.oscarserver.ca/oscar/ws/etc
	 */
	public void setEndpoint(String endpoint) {
		getMessageSourceComponent().setEndpoint(endpoint);
		this.endpoint = endpoint;
	}

	public org.hl7.fhir.dstu3.model.Organization getFhirOrganization() {
		org.hl7.fhir.dstu3.model.Organization organization = null; 
		if( getOscarFhirResource() != null ) {
			organization = getOscarFhirResource().getFhirResource();
		}
		return organization;
	}

	public void setFhirOrganization( org.hl7.fhir.dstu3.model.Organization fhirOrganizationResource) {
		if( oscarFhirResource == null ) {
			setOscarFhirResource( new org.oscarehr.integration.fhir.model.Organization( fhirOrganizationResource ) );
		}
	}
	
	public org.oscarehr.common.model.Contact getContact() {
		org.oscarehr.common.model.Contact contact = null;	
		if( getOscarFhirResource() != null ) {
			contact = getOscarFhirResource().getOscarResource();
		}
		return contact;
	}
	
	public void addContact( org.oscarehr.common.model.Contact contact ) {	
		if( oscarFhirResource == null ) {
			setOscarFhirResource( new org.oscarehr.integration.fhir.model.Organization( contact ) );
		}
	}

	public org.oscarehr.common.model.Clinic getClinic() {
		org.oscarehr.common.model.Clinic oscarClinicModel = null;	
		if( getOscarFhirResource() != null ) {
			org.oscarehr.integration.fhir.model.Organization organization = (org.oscarehr.integration.fhir.model.Organization) getOscarFhirResource(); 
			oscarClinicModel = organization.castToClinic();
		}
		return oscarClinicModel;
	}

	public void addClinic( org.oscarehr.common.model.Clinic clinic ) {	
		if( oscarFhirResource == null ) {
			setOscarFhirResource( new org.oscarehr.integration.fhir.model.Organization( clinic ) );
		}
	}
	
	public OscarFhirResource<Organization, Contact> getOscarFhirResource() {
		return oscarFhirResource;
	}

	public void setOscarFhirResource(OscarFhirResource<Organization, Contact> oscarFhirResource) {
		this.oscarFhirResource = oscarFhirResource;
	}

}
