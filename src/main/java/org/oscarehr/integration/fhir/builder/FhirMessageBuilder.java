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

import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MessageHeader;


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
	  "id": "Communication1”,
	  "meta": {
	    "lastUpdated": "2016-06-13T07:39:34.000-04:00"
	  },
	  "contained": [
	    {
	      "resourceType": "RelatedPerson",
	      "id": "RelatedPerson1",
	      "patient": {
	        "reference": "Patient/Patient1"
	      },
	      "relationship": {
	        "coding": [
	          {
	            "system": "http://hl7.org/fhir/v3/RoleCode",
	            "code": "PRNGUARD"
	          }
	        ]
	      },
	      "name": {
	        "family": [
	          "Doe"
	        ],
	        "given": [
	          "Jane"
	        ]
	      },
	      "telecom": [
	        {
	          "system": "email",
	          "value": "mom@parent.com"
	        },
	        {
	          "system": "phone",
	          "value": "416-555-5555",
	          "use": "mobile"
	        },
	        {
	          "system": "phone",
	          "value": "416-444-4444",
	          "use": "home"
	        }
	      ]
	    },
	    {
	      "resourceType": "Organization",
	      "id": "OrgPHU1",
	      "identifier": [
	        {
	          "system": "[id-system-local-base]/ca-on-panorama-phu-id",
	          "value": "55"
	        }
	      ],
	      "name": "Toronto Public Health"
	    }
	  ],
	  "identifier": [
	    {
	      "system": "[id-system-local-base]/ca-on-panorama-imm-submission-id",
	      "value": "1000212"
	    }
	  ],
	  "sender": {
	    "reference": "#RelatedPerson1"
	  },
	  "recipient": [
	    {
	      "reference": "#OrgPHU1"
	    }
	  ],
	  "payload": [
	    {
	      "contentAttachment": {
	        "contentType": "application/zip",
	        "data": "…"
	      }
	    },
	    {
	      "contentReference":  {
	        "reference": "Immunization/Immunization01"
	      }
	    },
	    {
	      "contentReference":  {
	        "reference": "Immunization/Immunization02"
	      }
	    }
	  ],
	  "status": "completed",
	  "sent": "2016-06-13T14:10:50-04:00",
	  "received": "2016-06-13T14:10:50-04:00",
	  "reason": [
	    {
	      "text": "Letter From PHU"
	    }
	  ],
	  "subject": {
	    "reference": "Patient/Patient1"
	  }
	}
*/



/**
 * Builds a FHIR message with the given OscarFhirResources.
 */
public class FhirMessageBuilder {

	//private static Logger logger = MiscUtils.getLogger();
	
	private MessageHeader messageHeader;
	private Communication communication;
	private Sender sender;
	private Destination destination; 
	private List<org.hl7.fhir.dstu3.model.BaseResource> resources;
	
	/**
	 * Constructor to add a MessageHeader to the message using the Sender and Destination 
	 * information
	 */
	public FhirMessageBuilder( Sender sender, Destination destination ) {
		this();
		
		if( sender != null && destination != null ) {
			setSender(sender);
			setDestination(destination);		
			setMessageHeader( new MessageHeader() );
		}
	}
	
	/**
	 * Constructor for a regular message using the FHIR Communication resource.
	 */
	public FhirMessageBuilder() {
		setCommunication( new Communication() );	
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	private void setMessageHeader(MessageHeader messageHeader) {	
		
		messageHeader.setId( new IdType() );
		messageHeader.getEvent()
			.setSystem("http://hl7.org/fhir/message-type")
			.setCode("MedicationAdministration-Recording");
		
		messageHeader.setTimestamp( new Date(System.currentTimeMillis() ) );
		messageHeader.setSource( getSender().getMessageSourceComponent() );
		messageHeader.setDestination( getDestination().getMessageDestinationComponents() );
	
		// set data references as they are added to this object. 
		
		this.messageHeader = messageHeader;

	}

	public Communication getCommunication() {
		return communication;
	}

	private void setCommunication( Communication communication ) {
		communication.getMeta().addTag( "lastUpdated", new Date(System.currentTimeMillis()).toString(), "" );
		communication.setId( new IdType() );
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

	public List<org.hl7.fhir.dstu3.model.BaseResource> getResources() {
		return resources;
	}

	public void setResources(List<org.hl7.fhir.dstu3.model.BaseResource> resources) {
		this.resources = resources;
	}

}
