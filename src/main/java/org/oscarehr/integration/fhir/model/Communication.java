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

public class Communication {

	private org.hl7.fhir.dstu3.model.Communication communication;

	
	public Communication( ) {
		
	}
	
	public Communication( org.hl7.fhir.dstu3.model.Communication communication ) {
		this.communication = communication;
	}

	public org.hl7.fhir.dstu3.model.Communication getCommunication() {
		return communication;
	}

	public void setCommunication(org.hl7.fhir.dstu3.model.Communication communication) {
		this.communication = communication;
	}

	
}
