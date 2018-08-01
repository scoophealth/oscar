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


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent;
import org.oscarehr.common.model.Clinic;

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
	private String senderName;
	private AbstractOscarFhirResource<org.hl7.fhir.dstu3.model.Organization, Clinic> oscarFhirResource;
	private org.hl7.fhir.dstu3.model.Organization organization;
	private Clinic clinic;
	
	public Sender() {
		setMessageSourceComponent( new MessageSourceComponent() );
	}
	
	public Sender( String vendorName, String softwareName, String versionSignature ) {
		setMessageSourceComponent( new MessageSourceComponent() );
		setVendorName(vendorName);
		setSoftwareName(softwareName);
		setVersionSignature(versionSignature);
	}
	
	public Sender( String vendorName, String softwareName, String versionSignature, String endpoint ) {
		setMessageSourceComponent( new MessageSourceComponent() );
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

	public org.hl7.fhir.dstu3.model.Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization( org.hl7.fhir.dstu3.model.Organization organization ) {
		this.organization = organization;
	}

	public org.oscarehr.common.model.Clinic getClinic() {
		return this.clinic;
	}

	public void setClinic( org.oscarehr.common.model.Clinic clinic ) {
		if(clinic != null) {
			setSenderName( clinic.getClinicName() );
			setOscarFhirResource(new Organization<Clinic>(clinic));
		}
		this.clinic = clinic;
	}
	
	public AbstractOscarFhirResource<org.hl7.fhir.dstu3.model.Organization, Clinic> getOscarFhirResource() {
		return oscarFhirResource;
	}

	public void setOscarFhirResource( AbstractOscarFhirResource<org.hl7.fhir.dstu3.model.Organization, Clinic> oscarFhirResource ) {
		this.organization = oscarFhirResource.getFhirResource();
		this.clinic = oscarFhirResource.getOscarResource();
		this.oscarFhirResource = oscarFhirResource;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	 public String toString() {
	     return ReflectionToStringBuilder.toString(this);
	 }
}
