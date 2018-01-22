package org.oscarehr.integration.fhir.builder;

import java.sql.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent;
import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;

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


import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.Sender;

public class FhirCommunicationBuilder extends FhirBundleBuilder {
	
	private Communication communication;
	
	public FhirCommunicationBuilder( Sender sender, Destination destination ) {
		super( sender, destination );
		this.communication = new Communication();
	}
	
	protected void setResource( BaseResource resource ) {

		// Recipient : Entity sending to

		// Subject : Patient this communication is related to. (Patient)
		if( resource instanceof org.hl7.fhir.dstu3.model.Patient ) {
			
			communication.getSubject().setResource( resource );

		// Sender : the RelatedPerson is put here.  ie: mother of child.
		} else if( resource instanceof org.hl7.fhir.dstu3.model.RelatedPerson ) {
		
			communication.getSender().setResource( resource );
		
		// all other Resources.  Attachment, Immunization, Notes etc... 
		} else {
			
			addPayloadResource( resource );
			
		}
	}
	
	public Communication getCommunication() {
		return communication;
	}
	
	public String getCommunicationJson() {
		return resourceToJson( this.communication );
	}

	private void setCommunication( Communication communication ) {
		Date timestamp = new Date(System.currentTimeMillis());
		
		// Sender : The Sender Organization (Organization)
		communication.getSender().setResource( this.getSender().getFhirResource() );
		
		// Destination: The Destination as an Organization Resource.
		List<BaseResource> fhirResources = this.getDestination().getFhirResources();
		for(BaseResource fhirResource : fhirResources) {
			communication.addRecipient().setResource( fhirResource );
		}
	
		// Communication version Meta tag
//		communication.getMeta().setVersionId( fhirContext.getVersion().getVersion().getFhirVersionString() );

		//TODO: need to find out what the Identifier is and how to set it in Oscar for tracking 
		// for now it is set to random
		communication.addIdentifier().setUse(IdentifierUse.OFFICIAL)
			.setSystem("[oscar URI]")
			.setValue( IdType.newRandomUuid().toString() ); 
		
		// TODO: is there a status for sent communications. ie: in progress??
		communication.setStatus( CommunicationStatus.INPROGRESS );
		
		// Timestamp Sent
		communication.setSent( timestamp );

		this.communication = communication;
	}
	
	
	public String getReason() {
		List<CodeableConcept> reasons = communication.getReasonCode();
		StringBuilder reasonBuilder = null;
		for( CodeableConcept reason : reasons ) {
			if( reasonBuilder == null ) {
				reasonBuilder = new StringBuilder("");
			}
			reasonBuilder.append( reason.getText() );
			reasonBuilder.append( "\n" );
		}
		if( reasonBuilder != null ) {
			reason = reasonBuilder.toString();
		}
		return reason;
	}

	public void setReason( String reason ) {
		communication.getReasonCodeFirstRep().setText(reason);
		this.reason = reason;
	}

	public CommunicationStatus getCommunicationStatus() {
		this.communicationStatus = communication.getStatus();
		return this.communicationStatus; 
	}

	public void setCommunicationStatus(CommunicationStatus communicationStatus) {
		communication.setStatus( communicationStatus );
		this.communicationStatus = communicationStatus;
	}

	/**
	 * Add any resource to the communication payload.
	 */
	public void addPayloadResource( BaseResource resource ) {
		CommunicationPayloadComponent communicationPayloadComponent = new CommunicationPayloadComponent();
		Reference reference = new Reference();
		reference.setId( resource.getId() );
		reference.setResource( resource );
		communicationPayloadComponent.setContent( reference );
		communication.addPayload( communicationPayloadComponent );
	}
	
	/**
	 * Add any variety of attachments to the Communication Payload.
	 */
	public void addAttachment( Attachment attachment ) {
		communication.addPayload().setContent( attachment );
	}
}
