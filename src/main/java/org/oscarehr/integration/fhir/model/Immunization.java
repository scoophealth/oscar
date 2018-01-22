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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.integration.fhir.interfaces.ImmunizationInterface;

/*

CLINICIAN SUBMISSION

{
  "resourceType": "Immunization",
  "id": "Immunization02",
  "status": "completed",
  "date": "2016-02-14T10:22:00-05:00",
  "vaccineCode": {
	"coding": [[
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

public class Immunization 
extends OscarFhirResource< org.hl7.fhir.dstu3.model.Immunization, org.oscarehr.common.model.Prevention > {

	private static final Pattern measurementValuePattern = Pattern.compile("^([0-9])*(\\.)*([0-9])*");

	public Immunization( ImmunizationInterface<Prevention> from ){
		super( new org.hl7.fhir.dstu3.model.Immunization(), (Prevention) from );
	}

	public Immunization( org.hl7.fhir.dstu3.model.Immunization from ) {
		super( new Prevention(), from );
	}

	@Override
	public List<Resource> getContainedFhirResources() {
		return getFhirResource().getContained();
	}

	@Override
	protected void mapAttributes( org.oscarehr.common.model.Prevention prevention ) {
		// constraint: Prevention must implement the ImmunizationInterface.
		setStatus( prevention );
		setAdministrationDate( prevention );
		setVaccineCode( prevention );
		setPatientReference( prevention );
		setRefused( prevention );
		setReported( prevention );
		setLotNumber( prevention );
		setExpirationDate( prevention );
		setSite( prevention );
		setDose( prevention );
		setRoute( prevention );
		setAnnotation( prevention );
	}

	@Override
	protected void mapAttributes( org.hl7.fhir.dstu3.model.Immunization immunization ) {
		// constraint: Prevention must implement the ImmunizationInterface.
		setStatus( immunization );
		setAdministrationDate( immunization );
		setVaccineCode( immunization );
		setPatientReference( immunization );
		setRefused( immunization );
		setReported( immunization );
		setLotNumber( immunization );
		setExpirationDate( immunization );
		setSite( immunization );
		setDose( immunization );
		setRoute( immunization );
		setAnnotation( immunization );
		setReason( immunization );
		setIsPrimarySource( immunization );
	}

	@Override
	protected void setId( org.hl7.fhir.dstu3.model.Immunization fhirResource ) {
		if( getOscarResource() != null && getOscarResource().getId() != null ) {
			fhirResource.setId( getOscarResource().getId() + "" );
		} else {
			super.setId(fhirResource);
		}
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

	private void setStatus(  ImmunizationInterface<Prevention> prevention ) {
		//TODO: how should all the various status' be set in Oscar?  ie: isNever, isRefused, isDeleted, isNeverReason
	}

	private void setAdministrationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setDate( getOscarResource().getPreventionDate() );
	}

	private void setAdministrationDate( ImmunizationInterface<Prevention> prevention ){
		prevention.setImmunizationDate( getFhirResource().getDate() );
	}

	private void setVaccineCode( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.getVaccineCode().addCoding()
		.setSystem("http://hl7.org/fhir/sid/cvx")
		.setCode( getOscarResource().getImmunizationType() )
		.setDisplay( getOscarResource().getManufacture() + " " + getOscarResource().getName() );
	}

	private void setVaccineCode( ImmunizationInterface<Prevention> prevention ){
		//TODO: for now, it is unknown how Oscar will handle incoming immunization codes. Suggested is SNOMED so a translation 
		// table will need to be built.
	}

	private void setPatientReference( org.hl7.fhir.dstu3.model.Immunization immunization ){
		//TODO: this will be the URI that references back to the patient. This is different when the Patient is contained.
		// somehow this will include the demographic number
		Reference reference = new Reference();
		reference.setDisplay( getOscarResource().getDemographicId() + "");
		immunization.setPatient( reference );
	}

	private void setPatientReference(  ImmunizationInterface<Prevention> prevention ){
		//TODO: this will be the demographic number. Not sure that it can be set here.
	}

	private void setRefused( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setNotGiven( getOscarResource().getImmunizationRefused() );
	}

	private void setRefused(  ImmunizationInterface<Prevention> prevention ){
		prevention.setImmunizationRefused( getFhirResource().getNotGiven() );
	}

	private void setReported( org.hl7.fhir.dstu3.model.Immunization immunization ){
		//TODO: will need to check what determines if the immunization is reported or not.
	}

	private void setReported(  ImmunizationInterface<Prevention> prevention ){
		//TODO: will need to check what determines if the immunization is reported or not.
	}

	private void setLotNumber( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setLotNumber( getOscarResource().getLotNo() );
	}

	private void setLotNumber(  ImmunizationInterface<Prevention> prevention ){
		prevention.setLotNo( getFhirResource().getLotNumber() );
	}

	private void setExpirationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
		//TODO: lot expiration is not provided in Oscar's Prevention model. This may need to be referenced
	}

	private void setExpirationDate(  ImmunizationInterface<Prevention> prevention ){
		//TODO: lot expiration is not provided in Oscar's Prevention model. This may need to be referenced
	}

	private void setSite( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.getSite().setText(getOscarResource().getSite()).addCoding()
		.setSystem("http://hl7.org/fhir/v3/ActSite");
	}

	private void setSite(  ImmunizationInterface<Prevention> prevention ){
		prevention.setSite( getFhirResource().getSite().getText() );
	}

	private void setDose( org.hl7.fhir.dstu3.model.Immunization immunization ){
		String dose = getOscarResource().getDose();
		Matcher matcher = measurementValuePattern.matcher(dose);
		String number = "";
		Double value = 0.0;
		if( matcher.find() ) {
			number = matcher.group(0);
			if( ! number.isEmpty() ) {
				value = Double.parseDouble( number );
			}
		}
		String unit = dose.replace(number, "").trim();
		immunization.getDoseQuantity().setValue(value).setUnit(unit);
	}

	private void setDose(  ImmunizationInterface<Prevention> prevention ){
		prevention.setDose( getFhirResource().getDoseQuantity().getValue().toString() + " " + getFhirResource().getDoseQuantity().getUnit() );
	}

	private void setRoute( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.getRoute().addCoding()
		.setSystem("http://hl7.org/fhir/v3/RouteOfAdministration")
		.setCode( getOscarResource().getRoute() )
		.setDisplay( getOscarResource().getRoute() );		
	}

	private void setRoute( ImmunizationInterface<Prevention> prevention ){
		prevention.setSite( getFhirResource().getRoute().getText() );
	}

	private void setAnnotation( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.addNote().setText( getOscarResource().getImmunizationRefusedReason() );
		immunization.addNote().setText( getOscarResource().getComment() );
	}

	private void setAnnotation(  ImmunizationInterface<Prevention> prevention ){
		StringBuilder note = new StringBuilder("");
		note.append( getFhirResource().getLocation() );

		for( org.hl7.fhir.dstu3.model.Annotation annotation : getFhirResource().getNote() ) {
			note.append( annotation.getText() );
		}

		prevention.setComment( note.toString() );
	}
	
	private void setReason( org.hl7.fhir.dstu3.model.Immunization immunization ) {
		immunization.getExplanation().addReason().addCoding()
			.setSystem("[code-system-local-base]/ca-on-immunizations-reason")
			.setCode( "routine" )
			.setDisplay( "Routine" );
	}
	
	/**
	 * True if the Immunization was administered by this clinician at this clinic.
	 * For now this is hard coded to True as there is no way in Oscar to determine this. 
	 */
	private void setIsPrimarySource( org.hl7.fhir.dstu3.model.Immunization immunization ) {	
		immunization.setPrimarySource( Boolean.TRUE );
	}
	
	public void setAdministeringProvider( Reference reference ) {
		getFhirResource().addPractitioner().setActor( reference )
		.getRole().addCoding()
			.setSystem("http://hl7.org/fhir/v2/0443")
			.setCode("AP")
			.setDisplay("AdministeringProvider");
	}

}
