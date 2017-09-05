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

public class MessageHeader {

}
