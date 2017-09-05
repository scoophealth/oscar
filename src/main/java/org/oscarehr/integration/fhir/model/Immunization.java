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


import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.common.model.Prevention;
import ca.uhn.fhir.context.FhirContext;

/*
 * 
 * PUBLIC SUBMISSION
{
  "resourceType": "Immunization",
  "id": "Immunization01",
  "status": "completed",
  "date": "2016-02-14T10:22:00-05:00",
  "_date": {
	"extension": [
	  {
		"url": "[base-structure]/ca-on-estimated",
		"valueBoolean": true
	  }
	]
  },
  "vaccineCode": {
	"coding": [
	  {
		"system": "http://snomed.info/sct",
		"code": "61153008",
		"display": "MMR measles + mumps + rubella unspecified"
	  }
	]
  },
  "patient": {
	"reference": "#Patient1"
  },
  "wasNotGiven": false,
  "reported": true,
  "location": {
    "display": "Canada, Ontario"
  },
  "lotNumber": "AAJN11K",
  "expirationDate": "2017-02-15",
  "note": [{
	"text": "Was given MMR vaccine in a walk-in clinic"
  }]
}

CLINICIAN SUBMISSION

{
  "resourceType": "Immunization",
  "id": "Immunization02",
  "status": "completed",
  "date": "2016-02-14T10:22:00-05:00",
  "vaccineCode": {
	"coding": [
	  {
		"system": "http://snomed.info/sct",
		"code": "61153008",
		"display": "MMR measles + mumps + rubella unspecified"
	  }
	]
  },
  "patient": {
	"reference": "Patient/Patient1"
  },
  "wasNotGiven": false,
  "reported": false,
  "lotNumber": "AAJN11K",
  "expirationDate": "2017-02-15",
  "site": {
	
  },
  "route": {
	
  },
  "doseQuantity": {
	
  }
}

*/

public class Immunization extends OscarFhirResource< org.hl7.fhir.dstu3.model.Immunization,  org.oscarehr.common.model.Prevention > {

		public Immunization( org.oscarehr.common.model.Prevention from ){
			super( new org.hl7.fhir.dstu3.model.Immunization(), from );
			setFhirContext( FhirContext.forDstu3() );
		}
		
		public Immunization( org.hl7.fhir.dstu3.model.Immunization from ) {
			super( new Prevention(), from );
		}

		@Override
		public List<Extension> getFhirExtensions() {
			return getFhirResource().getExtension();
		}

		@Override
		public List<Resource> getContainedFhirResources() {
			return getFhirResource().getContained();
		}
		
		@Override
		protected void mapAttributes( org.oscarehr.common.model.Prevention prevention ) {
			setStatus( prevention );
			setAdministrationDate( prevention );
			setExtension( prevention );
			setVaccineCode( prevention );
			setPatientReference( prevention );
			setRefused( prevention );
			setReported( prevention );
			setPerformedBy( prevention );
			setLotNumber( prevention );
			setExpirationDate( prevention );
			setSite( prevention );
			setDose( prevention );
			setAnnotation( prevention );
		}

		@Override
		protected void mapAttributes( org.hl7.fhir.dstu3.model.Immunization immunization ) {
			setId( immunization );
			setStatus( immunization );
			setAdministrationDate( immunization );
			setExtension( immunization );
			setVaccineCode( immunization );
			setPatientReference( immunization );
			setRefused( immunization );
			setReported( immunization );
			setPerformedBy( immunization );
			setLotNumber( immunization );
			setExpirationDate( immunization );
			setSite( immunization );
			setDose( immunization );
			setAnnotation( immunization );
		}
		
		@Override
		protected void setId( org.oscarehr.common.model.Prevention prevention ) {
			// will an incoming id be required to be set into a prevention??
		}

		
		@Override
		protected void setId( org.hl7.fhir.dstu3.model.Immunization immunization ) {
			IdType id = new IdType();
			id.applyTo( immunization );
		}
		
		/**
		 * Status of the immunization. Options are Completed or NULL
		 * When this status is coded as NULL it is assumed that the immunization was refused or 
		 * omitted.
		 * It is assumed that this method will never consume Preventions coded as deleted.
		 */
		private void setStatus( org.hl7.fhir.dstu3.model.Immunization immunization  ) {
						
			ImmunizationStatus immunizationStatus = ImmunizationStatus.NULL;
			
			if( ! getOscarResource().isNever() && ! getOscarResource().isRefused() ) {
				immunizationStatus = ImmunizationStatus.COMPLETED;
			}
			immunization.setStatus( immunizationStatus );
		}
		
		private void setStatus( org.oscarehr.common.model.Prevention prevention ) {

		}
		
		private void setAdministrationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
			immunization.setDate( getOscarResource().getPreventionDate() );
		}
		
		private void setAdministrationDate( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setExtension( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: extensions are used in public submissions
		}
		
		private void setExtension( org.oscarehr.common.model.Prevention prevention ){
			//TODO: extensions are used in public submissions. Will this be required to validate incoming Immunizations?
		}
		
		private void setVaccineCode( org.hl7.fhir.dstu3.model.Immunization immunization ){
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setSystem( "http://snomed.info/sct" )
				.setCode("") 
				.setDisplay( getOscarResource().getPreventionType() );
			//TODO: will need to provide the SNOMED code - or whatever system we choose.
			immunization.setVaccineCode( code );
		}
		
		private void setVaccineCode( org.oscarehr.common.model.Prevention prevention ){
			
		}

		private void setPatientReference( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: this will be the URI that references back to the patient. This is different when the Patient is contained.
			// somehow this will include the demographic number
			Reference reference = new Reference();
			reference.setDisplay( getOscarResource().getDemographicId() + "");
			immunization.setPatient( reference );
		}
		
		private void setPatientReference( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setRefused( org.hl7.fhir.dstu3.model.Immunization immunization ){
			immunization.setNotGiven( getOscarResource().isRefused() );
		}
		
		private void setRefused( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setReported( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: will need to check with clinics on how(if) they report their immunizations.
		}
		
		private void setReported( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setPerformedBy( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: it's not clear at this point if this is required.
		}
		
		private void setPerformedBy( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setLotNumber( org.hl7.fhir.dstu3.model.Immunization immunization ){
			// immunization.setLotNumber( );
			//TODO: this cannot be done until the Prevention model extends the PreventionExt model
			// OR at least until a new Immunization bean is created.
		}
		
		private void setLotNumber( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setExpirationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: lot expiration is not provided in Oscar's prevention model
		}
		
		private void setExpirationDate( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setSite( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: this cannot be done until the Prevention model extends the PreventionExt model
		}
		
		private void setSite( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setDose( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: this cannot be done until the Prevention model extends the PreventionExt model
		}
		
		private void setDose( org.oscarehr.common.model.Prevention prevention ){
			
		}
		
		private void setAnnotation( org.hl7.fhir.dstu3.model.Immunization immunization ){
			//TODO: this cannot be done until the Prevention model extends the PreventionExt model
		}
		
		private void setAnnotation( org.oscarehr.common.model.Prevention prevention ){
			
		}

}
