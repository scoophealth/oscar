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
import org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent;
import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.model.AbstractOscarFhirResource;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.Sender;

/**
 * Use when the Communication resource is used to build a majority of the
 * message. 
 */
public class FhirCommunicationBuilder extends AbstractFhirMessageBuilder<Communication> {

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
		AbstractOscarFhirResource<?,?> senderOscarFhirResource = getSender().getOscarFhirResource();
		if( senderOscarFhirResource != null ) {
			communication.getSender().setReference( senderOscarFhirResource.getContainedReferenceLink() );
			communication.getContained().add( (Resource) senderOscarFhirResource.getFhirResource() );
		} else {
			communication.getSender().setReference("#Organization" + getSender().getClinic().getId());
		}
		
		
		// Destination: The Destination as an Organization Resource.
		List<AbstractOscarFhirResource<?,?>> oscarFhirResources = this.getDestination().getOscarFhirResources();
		for(AbstractOscarFhirResource<?,?> oscarFhirResource : oscarFhirResources) {
			communication.addRecipient().setReference( oscarFhirResource.getContainedReferenceLink() );
			communication.getContained().add( (Resource) oscarFhirResource.getFhirResource() );
		}
	
		// Communication version Meta tag
		communication.getMeta().setLastUpdated( timestamp );
		
		// TODO Need to feed Oscar's URI into this. ID is random UUID for now.
		//Identifier id = communication.addIdentifier();
		//id.setSystem("http://hl7.org/fhir/v2/0203")
		//	.setValue( UUID.randomUUID().toString() ); 
		
		// Timestamp Sent
		communication.setSent( timestamp );
		setWrapper( communication );
		setID( UUID.randomUUID().toString() );
		setEndpointIdentifier(UUID.randomUUID().toString());
		
		// Initial communication status is INPROGRESS
		setStatus( CommunicationStatus.COMPLETED );
		
		//communication.setLanguage("en-US");
		// set the sender attribute automatically from the preset Sender Resource.
		setContainedSender(getSender());
		
		// set the destination atttribute automatically from the Destination Resource.
		addContainedRecipients(getDestination());
	}

	public Communication getCommunication() {
		return getWrapper();
	}
	
	public void addContainedRecipients(Destination destination) {
		if(destination != null) {
			addContainedRecipients(destination.getOscarFhirResources());
		}		
	}
	
	/**
	 * Add the list of Recipient Resources to the Communication Resource 
	 * These are populated automatically if they are contained in the Destination entity. 
	 */
	public void addContainedRecipients(List<AbstractOscarFhirResource<?,?>> oscarFhirResources) {
		for(AbstractOscarFhirResource<?,?> oscarFhirResource : oscarFhirResources) {
			addRecipientReference(oscarFhirResource.getContainedReferenceLink());
			addContainedRecipient(oscarFhirResource);
		}
	}
	
	public void addContainedRecipient(AbstractOscarFhirResource<?,?> oscarFhirResource) {
		addContainedResource(oscarFhirResource.getFhirResource());
	}
	
	public void addRecipientReference(String referenceLink) {
		getCommunication().addRecipient().setReference(referenceLink);
	}
	
	/**
	 * Inject a sender object.
	 * This method will reference the Organization Resource contained in the
	 * Sender object. The Organization will represent and be contained as  
	 * the sender attribute of this Communication Resource 
	 */
	public void setContainedSender(Sender sender) {
		if(sender != null) {
			setContainedSender(sender.getOscarFhirResource());
		}
	}
	
	/**
	 * Inject an Organization Oscar-FHIR transport entity to set as a contained Organization Resource 
	 * for the Communication.sender attribute. 
	 * The reference link and resource are set automatically as contained.  
	 */
	public void setContainedSender(AbstractOscarFhirResource<Organization,Clinic> senderOscarFhirResource) {
		if( senderOscarFhirResource != null ) {	
			setSenderReference(senderOscarFhirResource.getContainedReferenceLink());
			setContainedSender(senderOscarFhirResource.getFhirResource());
		}
	}
	
	/**
	 * Set the reference link for the Organization Resource for the Communication.sender attribute.
	 * Can be a contained link: ie; #Organization/id123
	 * Or can be a relative link ie: /Organization/id123
	 * Add an Organization Resource with the setContainedSender() method if 
	 * this is a link for a contained Organization Resource.
	 */
	public void setSenderReference(String referenceLink) {
		if(referenceLink != null) {
			getCommunication().getSender().setReference( referenceLink );
		}
	}
	
	/**
	 * The Organization resource that will be contained within this Communication Resource.
	 */
	public void setContainedSender(Organization organization) {
		if( organization != null ) {
			addContainedResource(organization);
		}
	}
	
	/**
	 * Sets a UUID to identify this communication 
	 * Cannot be modified. 
	 */
	private void setID(String id) {
		getWrapper().setId(id);
	}
	
	/**
	 * Identifier is automatically set with a UUID. 
	 * However it can be changed with this modifier.
	 */
	public void setEndpointIdentifier(String identifier) {
		getCommunication()
			.addIdentifier()
			.setSystem("http://hl7.org/fhir/v2/0203")
			.setValue(identifier); 
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

	/**
	 * Set a text based reason for sending this message.
	 * This method is an mutable modifier 
	 * ie: the last reason added will be overwritten.
	 */
	public void setReason( String reason ) {
		getCommunication().getReasonCodeFirstRep().setText(reason);
	}

	public CommunicationStatus getStatus() {
		return getCommunication().getStatus();
	}

	/**
	 * Status is set to CommunicationStatus.INPROGRESS by default.
	 */
	public void setStatus(CommunicationStatus communicationStatus) {
		getCommunication().setStatus( communicationStatus );
	}
	
	/**
	 * The subject is usually the target patient.
	 * This method will set the subject reference link and the resource 
	 * as contained inside the communication resource
	 */
	public void setSubject( AbstractOscarFhirResource<?,?> oscarFhirResource ) {
		getCommunication().getSubject().setReference( oscarFhirResource.getContainedReferenceLink() );
		setSubject( oscarFhirResource.getFhirResource() );
	}
	
	/**
	 * The subject is usually the target patient.
	 * This method will set the subject resource as contained 
	 */
	public void setSubject( BaseResource patient ) {
		addContainedResource( patient );
	}

	public BaseResource getSubject() {
		return getCommunication().getSubjectTarget();
	}

	private void addPayload( org.hl7.fhir.dstu3.model.Type type ) {
		getCommunication().addPayload( new CommunicationPayloadComponent( type ) );
	}
	
	/**
	 * NOTE: resources added to the Communication resource will always be contained.
	 */
	@Override
	public void addResource( AbstractOscarFhirResource< ?,? > oscarFhirResource ) {
		addResource( oscarFhirResource.getFhirResource() );
	}
	
	@Override
	public void addResource(BaseResource resource) {
		addContainedResource(resource);		
	}
	
	private void addContainedResource( BaseResource resource ) {
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
