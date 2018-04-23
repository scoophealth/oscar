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

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent;
import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.OscarFhirResource;
import org.oscarehr.integration.fhir.model.Sender;

/**
 * Use when the Communication resource is used to build a majority of the
 * message. 
 */
public class FhirCommunicationBuilder extends FhirMessageBuilder {

	public FhirCommunicationBuilder( OscarFhirConfigurationManager configurationManager ) {
		super( configurationManager );
		setCommunication( new org.hl7.fhir.dstu3.model.Communication() );
	}
	
	public FhirCommunicationBuilder( Sender sender, Destination destination ) {
		super( sender, destination );
		setCommunication( new org.hl7.fhir.dstu3.model.Communication() );
	}
	
	private void setCommunication( org.hl7.fhir.dstu3.model.Communication communication ) {
		Date timestamp = new Date( System.currentTimeMillis() );
		
		// Sender : The Sender Organization (Organization)
		OscarFhirResource<?,?> senderOscarFhirResource = getSender().getOscarFhirResource();
		if( senderOscarFhirResource != null ) {
			communication.getSender().setReference( senderOscarFhirResource.getContainedReferenceLink() );
			communication.getContained().add( (Resource) senderOscarFhirResource.getFhirResource() );
		}
		
		
		// Destination: The Destination as an Organization Resource.
		List<OscarFhirResource<?,?>> oscarFhirResources = this.getDestination().getOscarFhirResources();
		for(OscarFhirResource<?,?> oscarFhirResource : oscarFhirResources) {
			communication.addRecipient().setReference( oscarFhirResource.getContainedReferenceLink() );
			communication.getContained().add( (Resource) oscarFhirResource.getFhirResource() );
		}
	
		// Communication version Meta tag
		communication.getMeta().setLastUpdated( timestamp );
		
		// TODO Need to feed Oscar's URI into this. ID is random UUID for now. 
		communication.addIdentifier().setSystem( getSender().getEndpoint() )
			.setValue( UUID.randomUUID().toString() ); 
		
		// Timestamp Sent
		communication.setSent( timestamp );
		
		communication.setId( UUID.randomUUID().toString() );

		setWrapper( communication );
		
		// Initial communication status is INPROGRESS
		setStatus( CommunicationStatus.INPROGRESS );
	}
	
	public Communication getCommunication() {
		return ( Communication ) getWrapper();
	}

	public String getReason() {
		
		List<CodeableConcept> reasons = getCommunication().getReasonCode();
		StringBuilder reasonBuilder = null;
		for( CodeableConcept reason : reasons ) {
			if( reasonBuilder == null ) {
				reasonBuilder = new StringBuilder("");
			}
			reasonBuilder.append( reason.getText() );
			reasonBuilder.append( "\n" );
		}
		if( reasonBuilder != null ) {
			return reasonBuilder.toString();
		}
		return null;
	}

	public void setReason( String reason ) {
		getCommunication().getReasonCodeFirstRep().setText(reason);
	}

	public CommunicationStatus getStatus() {
		return getCommunication().getStatus();
	}

	public void setStatus(CommunicationStatus communicationStatus) {
		getCommunication().setStatus( communicationStatus );
	}
	
	/**
	 * The subject is usually the target patient.
	 * This method will set the subject reference link and the resource 
	 * as contained inside the communication resource
	 */
	public void setSubject( OscarFhirResource<?,?> oscarFhirResource ) {
		getCommunication().getSubject().setReference( oscarFhirResource.getContainedReferenceLink() );
		setSubject( oscarFhirResource.getFhirResource() );
	}
	
	/**
	 * The subject is usually the target patient.
	 * This method will set the subject resource as contained 
	 */
	public void setSubject( BaseResource patient ) {
		addResource( patient );
	}

	public BaseResource getSubject() {
		return getCommunication().getSubjectTarget();
	}

	private void addPayload( org.hl7.fhir.dstu3.model.Type type ) {
		getCommunication().addPayload( new CommunicationPayloadComponent( type ) );
	}
	
	/**
	 * Resources added to the Communication resource are always contained.
	 */
	@Override
	public void addResource( OscarFhirResource< ?,? > oscarFhirResource ) {
		addResource( oscarFhirResource.getFhirResource() );
	}
	
	@Override
	protected void addResource( BaseResource resource ) {
		getCommunication().getContained().add( (Resource) resource );
	}
	
	/**
	 * For adding a reference link to an external Communication.Payload resource.
	 */
	public void addAttachmentReference( Reference reference ) {
		addPayload( reference );
	}
	
	/**
	 * Add any variety of attachments to the Communication Payload.
	 * This attachment will be CONTAINED inside the Communication.Payload attribute.
	 */
	@Override
	public void addAttachment( Attachment attachment ) {
		addPayload( attachment );
	}


}
