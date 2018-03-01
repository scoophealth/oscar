package org.oscarehr.integration.fhir.builder;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BaseResource;
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
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MessageHeader;
//import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.Sender;

/* EXAMPLE BUNDLE:
{
"resourceType": "Bundle",
"id": "10bb101f-a121-4264-a920-67be9cb82c74",
"type": "message", "entry": [
{
"resource": {
	"resourceType": "MessageHeader",
	"id": "1cbdfb97-5859-48a4-8301-d54eab818d68",
	"event": {
		"system": "http://hl7.org/fhir/message-type", 
		"code": "observation-provide"
		},
	"destination": [
		{
			"name": "BORN",
			"endpoint": "https://hial.bornontario.ca/FHIR/Patient/"
		}
	],
	"sender": {
		"display": "CLINICBORN"
	},
	"author": {
		"reference": "Practitioner/Practioner1"
	},
	"timestamp": "2017-01-04T07:39:34.000-04:00",
	"source": {
		"name": "Some EMR",
		"software": "EMR1", 
		"version": "3.1.45.AABB"
	},
	"responsible": {
		"reference": "Organization/Clinic1"
	},
	"focus": [
		{
		"reference": "Patient/Patient1"
		}
	]
}
},
{
"resource": {
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
}
},
{
"resource": {
	"resourceType": "ClinicalImpression",
	"id": "ClinicalImpression1",
	"status": "completed",
   	"subject": "Patient/Patient1",
	"description": "Well Baby",
	"summary": "UGF5bG9hZCBEYXRhIEdvZXMgSGVyZQ=="
}
},
{
"resource": {
	"resourceType": "Practioner",
	"id": " Practioner1",
	"identifier": [
        {
			"system": " http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-physician",
			"value": "123456"
        }
     ],
     "name": [
	 {
		"family": "Welby",
		"given": "Marcus"
	 }
	]
}
},
{
"resource": {
	"resourceType": "Organization",
    "id": "Clinic1",
    "name": "CLINICTEST"
}
}
]
}

 */
public class FhirBundleBuilder extends FhirMessageBuilder {

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

	public Bundle getBundle() {
		return (Bundle) getWrapper();
	}

	private void setBundle( Bundle bundle ) {
		bundle.setId( UUID.randomUUID().toString() );
		bundle.setType( BundleType.MESSAGE );		
		bundle.addEntry().setResource( getMessageHeader() );
		setWrapper( bundle );
	}	

	@Override
	protected void addResource( BaseResource resource ) {
		getBundle().addEntry().setResource( (Resource) resource );
	}

	@Override
	protected void addAttachment( Attachment attachment ) {
		// TODO Auto-generated method stub		
	}

}
