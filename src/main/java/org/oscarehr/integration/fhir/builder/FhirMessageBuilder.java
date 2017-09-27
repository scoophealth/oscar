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
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent;
import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.MessageHeader;
import org.hl7.fhir.dstu3.model.Reference;
import org.oscarehr.integration.fhir.model.OscarFhirResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;


/*
{
	  "resourceType": "MessageHeader",
	  "id": "1cbdfb97-5859-48a4-8301-d54eab818d68",
	  "timestamp": "2017-01-04T07:39:34.000-04:00",
	  "event": {
	    "system": "http://hl7.org/fhir/message-type",
	    "code": "MedicationAdministration-Recording"
	  },
	  "source": {
	    "name": "Some EMR",
	    "software": "EMR1",
	    "version": "3.1.45.AABB",
	    "endpoint": "https://www.someemr1.com/api/fhir"
	  },
	  "destination": [
	    {
	      "name": "DHIR",
	      "endpoint": "https://wsgateway.prod.ehealthontario.ca/API/FHIR/Immunizations/v1/"
	    }
	  ],
	  "data": [
	    {
	      "reference": "Communication/Communication1",
	      "reference": "Patient/Patient1",
	      "reference": "Immunization/Immunization01",
	      "reference": "Immunization/Immunization02"
	    }
	  ]
	}
*/


/*
{
  "resourceType": "Communication",
  "meta": {
    "lastUpdated": "2016-06-13T07:39:34.000-04:00"
  },
  "contained": [
    {
      "resourceType": "Patient",
      "id": "Patient1",
      "identifier": [
        {
          "use": "official",
          "system": "[id-system-global-base]/ca-on-patient-hcn",
          "value": "9393881587"
        },
        {
          "use": "secondary",
          "system": "[code-system-global-base]/v2/0203",
	   "code": "MR",
          "value": "10000123"
        }
      ],
      "name": [
        {
          "use": "official",
          "family": [
            "Doe"
          ],
          "given": [
      		"John", {
        "value": "Jacob",
       	 "extension": {
          "url": "http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier",
       	   "valueCode": "MID" 
        }
      		}
    ]
        }
      ],
      "gender": "male",
      "birthDate": "2012-02-14",
      "telecom": [
        {
          "system": "phone",
          "value": "416-444-4444",
          "use": "home"
        }
      ],
"address": [
        {
          "extension": [
            {
              "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber",
              "valueString": "535"
            },
            {
              "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetName",
              "valueString": "Sheppard"
            },
            {
              "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetNameType",
              "valueString": "Avenue"
            },
            {
              "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-direction",
              "valueString": "West"
            },
            {
              "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-unitID",
              "valueString": "1907"
            }
          ],
          "use": "home",
          "line": [
            "Second address line"
          ],
          "city": "Toronto",
          "state": "ON",
          "postalCode": "M3H4X8"
        }
      ],
"communication": [
        {
          "language": "en-US"
        }
      ],
"managingOrganization": [
        {
          "reference": "#Clinic1"
        }
      ]
    },
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
    },
    {
	"resourceType": "Practioner",
	"id": " Practioner1",
	"identifier": [
        {
          "system": "https://www.hl7.org/FHIR/valueset-identifier-type.html",
	   "type": "PRN",
          "value": "123456"
        }
      ] ,
      "name": [
	 {
	   "family": "Welby",
	   "given": "Marcus"
	 }
]
    }
  ],
  "identifier": [
    {
      "system": "http://hl7.org/fhir/v2/0203",
      "code": "XX",
      "value": "2345678901"
    }
  ],
  "sender": {
    "reference": "#Clinic1"
  },
  "payload": [
    {
      "contentAttachment": {
        "contentType": "text/plain",
        "data": "UGF5bG9hZCBEYXRhIEdvZXMgSGVyZQ==",
	 "title": "Well Baby"
      }
    }
  ],
  "status": "completed",
  "sent": "2016-06-13T14:10:50-04:00",
  "received": "2016-06-13T14:10:50-04:00",
  "subject": {
    "reference": "#Patient1"
  }

*/



/**
 * Builds a FHIR message with the given OscarFhirResources.
 */
public class FhirMessageBuilder {

	//private static Logger logger = MiscUtils.getLogger();
	private static FhirContext fhirContext = FhirContext.forDstu3();
	private MessageHeader messageHeader;
	private Communication communication;
	private Sender sender;
	private Destination destination; 
	private List<OscarFhirResource< ?, ? > > resources;
	private String reason;
	private CommunicationStatus communicationStatus;
	
	/**
	 * Constructor to add a MessageHeader to the message using the Sender and Destination 
	 * information
	 */
	public FhirMessageBuilder( Sender sender, Destination destination ) {

		if( sender != null && destination != null ) {
			setSender( sender );
			setDestination( destination );		
			setMessageHeader( new MessageHeader() );
		}
		
		setCommunication( new Communication() );
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	
	public String getMessageHeaderJson() {
		return resourceToJson( messageHeader );
	}

	/**
	 * This method is constrained to availability of the Sender and Destination Objects.
	 */
	private void setMessageHeader(MessageHeader messageHeader) {	
		messageHeader.setId( IdDt.newRandomUuid() );
		messageHeader.getEvent()
			.setSystem("http://hl7.org/fhir/message-type")
			.setCode("MedicationAdministration-Recording");
		//TODO: these cannot be hard coded.
		messageHeader.setTimestamp( new Date(System.currentTimeMillis() ) );
		messageHeader.setSource( getSender().getMessageSourceComponent() );
		messageHeader.setDestination( getDestination().getMessageDestinationComponents() );

		this.messageHeader = messageHeader;

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
		communication.getSender().setResource( this.getSender().getFhirOrganization() );
		
		// Communication version Meta tag
		communication.getMeta().setVersionId( fhirContext.getVersion().getVersion().getFhirVersionString() );

		//TODO: need to find out what the Identifier is and how to set it in Oscar for tracking 
		// for now it is set to random
		communication.addIdentifier().setUse(IdentifierUse.OFFICIAL)
			.setSystem("[oscar URI]")
			.setValue( IdType.newRandomUuid().toString() ); 
		
		// TODO: is there a status for sent communications. ie: in progress??
		
		// Timestamp Sent
		communication.setSent( timestamp );

		this.communication = communication;
	}

	public Sender getSender() {
		return sender;
	}

	private void setSender(Sender sender) {
		this.sender = sender;
	}

	public Destination getDestination() {
		return destination;
	}

	private void setDestination(Destination destination) {
		this.destination = destination;
	}

	public List< OscarFhirResource< ?, ? > > getResources() {
		if( this.resources == null ) {
			resources = new ArrayList<OscarFhirResource< ?, ? > >();
		}
		return resources;
	}

	public void addResources( List< OscarFhirResource< ?, ? > > oscarFhirResources ) {
		for( OscarFhirResource<?,?> oscarFhirResource :  oscarFhirResources ) {
			addResource( oscarFhirResource );
		}
	}
	
	private void setResource( BaseResource resource ) {

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

	public void addResource( OscarFhirResource< ?, ? > oscarFhirResource ) { 
		if( oscarFhirResource != null ) {
			addResource( oscarFhirResource.getFhirResource() );
			getResources().add( oscarFhirResource );
		}
	}
	
	public void addResource( BaseResource resource ) {
		if( resource != null ) {
			setResource( resource );
		}
	}
	
	private static final String resourceToJson( org.hl7.fhir.dstu3.model.BaseResource resource ) {
		if( resource == null ) {
			return "";
		}
		return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString( resource );
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

	public void setReason(String reason) {
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

	public void attachPDF( String pdf ) {
		attachPDF( pdf.getBytes() );
	}
	
	public void attachPDF( byte[] bytes ) {
		Attachment attachment = new Attachment();
		attachment.setTitle("PDF Attachment");
		attachment.setContentType( "application/pdf" );
		attachment.setData( bytes );
		addAttachment( attachment );
	}
	
	public void attachRichText( String rtf ) {
		Attachment attachment = new Attachment();
		attachment.setTitle("Rich Text Attachment");
		attachment.setContentType( "text/rtf" );
		attachment.setData( rtf.getBytes() );
		addAttachment( attachment );
	}
	
	public void attachText( String text ) {
		Attachment attachment = new Attachment();
		attachment.setTitle("Text Attachment");
		attachment.setContentType( "text/plain" );
		attachment.setData( text.getBytes() );
		addAttachment( attachment );
	}
}
