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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.oscarehr.common.dao.CVCImmunizationDao;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.CVCImmunization;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.integration.fhir.interfaces.ImmunizationInterface;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.util.SpringUtils;


/*
  {doco
  "resourceType" : "Immunization",
  // from Resource: id, meta, implicitRules, and language
  // from DomainResource: text, contained, extension, and modifierExtension
  "identifier" : [{ Identifier }], // Business identifier
  "status" : "<code>", // R!  completed | entered-in-error
  "notGiven" : <boolean>, // R!  Flag for whether immunization was given
  "vaccineCode" : { CodeableConcept }, // R!  Vaccine product administered
  "patient" : { Reference(Patient) }, // R!  Who was immunized
  "encounter" : { Reference(Encounter) }, // Encounter administered as part of
  "date" : "<dateTime>", // Vaccination administration date
  "primarySource" : <boolean>, // R!  Indicates context the data was recorded in
  "reportOrigin" : { CodeableConcept }, // Indicates the source of a secondarily reported record
  "location" : { Reference(Location) }, // Where vaccination occurred
  "manufacturer" : { Reference(Organization) }, // Vaccine manufacturer
  "lotNumber" : "<string>", // Vaccine lot number
  "expirationDate" : "<date>", // Vaccine expiration date
  "site" : { CodeableConcept }, // Body site vaccine  was administered
  "route" : { CodeableConcept }, // How vaccine entered body
  "doseQuantity" : { Quantity(SimpleQuantity) }, // Amount of vaccine administered
  "practitioner" : [{ // Who performed event
    "role" : { CodeableConcept }, // What type of performance was done
    "actor" : { Reference(Practitioner) } // R!  Individual who was performing
  }],
  "note" : [{ Annotation }], // Vaccination notes
  "explanation" : { // Administration/non-administration reasons
    "reason" : [{ CodeableConcept }], // Why immunization occurred
    "reasonNotGiven" : [{ CodeableConcept }] // Why immunization did not occur
  },
  "reaction" : [{ // Details of a reaction that follows immunization
    "date" : "<dateTime>", // When reaction started
    "detail" : { Reference(Observation) }, // Additional information on reaction
    "reported" : <boolean> // Indicates self-reported reaction
  }],
  "vaccinationProtocol" : [{ // What protocol was followed
    "doseSequence" : "<positiveInt>", // Dose number within series
    "description" : "<string>", // Details of vaccine protocol
    "authority" : { Reference(Organization) }, // Who is responsible for protocol
    "series" : "<string>", // Name of vaccine series
    "seriesDoses" : "<positiveInt>", // Recommended number of doses for immunity
    "targetDisease" : [{ CodeableConcept }], // R!  Disease immunized against
    "doseStatus" : { CodeableConcept }, // R!  Indicates if dose counts towards immunity
    "doseStatusReason" : { CodeableConcept } // Why dose does (not) count
  }]
}
 */

/**
 * 
 * constraint: Oscar class must implement ImmunizationInterface.
 *
 */
public class Immunization<T extends AbstractModel<Integer> & ImmunizationInterface > 
	extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.Immunization, T> {

	private static final Pattern measurementValuePattern = Pattern.compile("^([0-9])*(\\.)*([0-9])*");
	private boolean isHistorical;
	
	public Immunization( T from ){
		super( new org.hl7.fhir.dstu3.model.Immunization(), from );
	}
	
	public Immunization( T from,  OscarFhirConfigurationManager configurationManager ){
		super( new org.hl7.fhir.dstu3.model.Immunization(), from, configurationManager );
	}

	@SuppressWarnings("unchecked")
	public Immunization( org.hl7.fhir.dstu3.model.Immunization from ) {
		super( (T) new Prevention(), from );
	}
	
	@Override
	protected void mapAttributes( T immunization ) {
		
		// this is important to initialize easy access of the hash list of properties populated from the PreventionExt table.
		if( immunization instanceof Prevention ) {
			( (Prevention) immunization ).setPreventionExtendedProperties();
		}
		
		setAdministrationDate( immunization );
		setVaccineCode( immunization );
		setVaccineCode2( immunization );
		setRefused( immunization );
		setLotNumber( immunization );
		setExpirationDate( immunization );
		setSite( immunization );
		setDose( immunization );
		setRoute( immunization );
		setAnnotation( immunization );
	}

	@Override
	protected final void mapAttributes(org.hl7.fhir.dstu3.model.Immunization immunization ) {
		
		// this is important to initialize easy access of the hash list of properties populated from the PreventionExt table.
		if( getOscarResource() instanceof Prevention ) {
			( (Prevention) getOscarResource() ).setPreventionExtendedProperties();
		}
		
		// this is a particular requirement for the DHIR - but it may have other applications. 
		if( include( OptionalFHIRAttribute.dateIsEstimated ) ) {
			setHistorical( getOscarResource().isHistorical(14) );
		}
		
		//mandatory
		setIsPrimarySource( immunization );
		setStatus( immunization );
		setAdministrationDate( immunization );
		setVaccineCode( immunization );
		setVaccineCode2( immunization );
		setRefused( immunization );
		setLotNumber( immunization );
		setExpirationDate( immunization );
		setSite( immunization );
		setDose( immunization );
		setRoute( immunization );
		setReason( immunization );

		// optional
		if( include( OptionalFHIRAttribute.annotation ) ) {
			setAnnotation( immunization );
		}

		if( ! immunization.getPrimarySource() ) {
			setReportOrigin( immunization );
		}
	}
	
	/**
	 * Returns the Oscar patient Id for whom this immunization was for.
	 */
	public int getDemographicNo() {
		return getOscarResource().getDemographicId();
	}

	@Override
	protected final void setId( org.hl7.fhir.dstu3.model.Immunization fhirResource ) {
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

		if( getOscarResource().isComplete() ) {
			immunizationStatus = ImmunizationStatus.COMPLETED;
		}
		
		immunization.setStatus( immunizationStatus );
	}

	/**
	 * The extension URI for the administration date indicates if the immunization date was estimated.
	 * 
	 */
	private void setAdministrationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
		
		immunization.setDate( getOscarResource().getImmunizationDate() );

		if( include( OptionalFHIRAttribute.dateIsEstimated ) ) {
			
			//TODO the number of days to estimate a historical date will need to fetched from the configuration settings.
			
			BooleanType estimated = new BooleanType();
			estimated.setValue( isHistorical() );
			
			immunization.getDateElement()
				.addExtension()
				.setUrl("https://ehealthontario.ca/API/FHIR/StructureDefinition/ca-on-extension-estimated-date")
				.setValue( estimated );
		}

	}

	private void setAdministrationDate( ImmunizationInterface immunization ){
		immunization.setImmunizationDate( getFhirResource().getDate() );
	}

	/**
	 * SNOMED is a fixed (static) system in Oscar.
	 */
	private void setVaccineCode( org.hl7.fhir.dstu3.model.Immunization immunization ){
		CVCImmunizationDao cvcImmDao = SpringUtils.getBean(CVCImmunizationDao.class);
	
		if(!StringUtils.isEmpty(getOscarResource().getVaccineCode2())) {
			immunization.getVaccineCode().addCoding()
			.setSystem("http://snomed.info/sct")
			.setCode( getOscarResource().getVaccineCode2() )
			.setDisplay((getOscarResource().getName()).trim());
		} else {
				
			if(!StringUtils.isEmpty(getOscarResource().getVaccineCode())) {
				String display = getOscarResource().getName().trim();
				if(StringUtils.isEmpty(display) || !StringUtils.isEmpty(getOscarResource().getVaccineCode2())) {
					CVCImmunization cvcImm = cvcImmDao.findBySnomedConceptId(getOscarResource().getVaccineCode());
					if(cvcImm != null) {
						display = cvcImm.getDisplayName();
					}
				}
				
				immunization.getVaccineCode().addCoding()
				.setSystem("http://snomed.info/sct")
				.setCode( getOscarResource().getVaccineCode() )
				.setDisplay( display );
			}
		}
	}

	private void setVaccineCode( ImmunizationInterface immunization ){
		immunization.setVaccineCode( getFhirResource().getVaccineCode().getCodingFirstRep().getCode() );
	}
	
	/**
	 * SNOMED is a fixed (static) system in Oscar.
	 */
	private void setVaccineCode2( org.hl7.fhir.dstu3.model.Immunization immunization ){
		/*
		if(!StringUtils.isEmpty(getOscarResource().getVaccineCode2())) {
			immunization.getVaccineCode().addCoding()
			.setSystem("http://snomed.info/sct")
			.setCode( getOscarResource().getVaccineCode2() )
			.setDisplay((getOscarResource().getName()).trim());
		}
		*/
	}

	private void setVaccineCode2( ImmunizationInterface immunization ){
		//immunization.setVaccineCode2( getFhirResource().getVaccineCode().getCoding().get(1).getCode() );
	}
	

	private void setRefused( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setNotGiven( getOscarResource().getImmunizationRefused() );
	}

	private void setRefused(  ImmunizationInterface immunization ){
		immunization.setImmunizationRefused( getFhirResource().getNotGiven() );
	}

	private void setLotNumber( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setLotNumber( getOscarResource().getLotNo() );
	}

	private void setLotNumber(  ImmunizationInterface immunization ){
		immunization.setLotNo( getFhirResource().getLotNumber() );
	}

	private void setExpirationDate( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.setExpirationDate(  getOscarResource().getExpiryDate() );
	}

	private void setExpirationDate(  ImmunizationInterface immunization ){
		immunization.setExpiryDate( getFhirResource().getExpirationDate() );
	}

	/**
	 * This is the body part - or location - the immunization was given.
	 */
	private void setSite( org.hl7.fhir.dstu3.model.Immunization immunization ){
		if(!StringUtils.isEmpty(getOscarResource().getSite())) {
			immunization.getSite().setText( mapSite(getOscarResource().getSite()));
		}
	}
	
	private String mapSite(String oscarSite) {
		return oscarSite;
		/*
		if(oscarSite == null) {
			return "";
		}
		
		if("Superior Deltoid Lt".equals(oscarSite)) {
			return "JiON_LID";
		} else if("Inferior Deltoid Lt".equals(oscarSite)) {
			return "JiON_LLD";
		} else if("Anterolateral Thigh Lt".equals(oscarSite)) {
			return "LATH";
		} else if("Gluteal Lt".equals(oscarSite)) {
			return "LDG";
		} else if("Superior Deltoid Rt".equals(oscarSite)) {
			return "JiON_RID";
		} else if("Inferior Deltoid Rt".equals(oscarSite)) {
			return "JiON_RLD";
		} else if("Anterolateral Thigh Rt".equals(oscarSite)) {
			return "RATH";
		} else if("Gluteal Rt".equals(oscarSite)) {
			return "RDG";
		} else if("Arm Lt".equals(oscarSite)) {
			return "JiON_AL";
		} else if("Arm Rt".equals(oscarSite)) {
			return "JiON_AR";
		} else if("Unknown".equals(oscarSite)) {
			return "UK";
		} else if("Mouth".equals(oscarSite)) {
			return "MOUTH";
		} else if("Deltoid Lt".equals(oscarSite)) {
			return "JiON_LD";
		} else if("Deltoid Rt".equals(oscarSite)) {
			return "JiON_RD";
		} else if("Naris Lt".equals(oscarSite)) {
			return "JiON_LN";
		} else if("Naris Rt".equals(oscarSite)) {
			return "JiON_RN";
		} else if("Forearm Lt".equals(oscarSite)) {
			return "JiON_LFA";
		} else if("Forearm Rt".equals(oscarSite)) {
			return "JiON_RFA";
		} else if("Other".equals(oscarSite)) {
			return "JiON_OTHER";
		} else if("Nares (Lt and Rt)".equals(oscarSite)) {
			return "JiON_Nares_L";
		}
			
		return oscarSite;
		*/
	}

	private void setSite(  ImmunizationInterface immunization ){
		if(!StringUtils.isEmpty(getFhirResource().getSite().getText())) {
			immunization.setSite( mapSite(getFhirResource().getSite().getText()) );
		}
	}

	private void setDose( org.hl7.fhir.dstu3.model.Immunization immunization ){
		String dose = getOscarResource().getDose();
		
		if(StringUtils.isEmpty(dose)) {
			return;
		}
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

	private void setDose(  ImmunizationInterface immunization ){
		immunization.setDose( getFhirResource().getDoseQuantity().getValue().toString() + " " + getFhirResource().getDoseQuantity().getUnit() );
	}

	private void setRoute( org.hl7.fhir.dstu3.model.Immunization immunization ){
		if(!StringUtils.isEmpty(getOscarResource().getRoute())) {
			
			String oscarRouteCode = getOscarResource().getRoute();
			String code = oscarRouteCode;
			String display = getOscarResource().getRoute();
			if("ID".equals(oscarRouteCode)) {
				code = "372464004";
				display = "Intradermal: ID";
			} else if("IM".equals(oscarRouteCode)) {
				code = "78421000";
				display="Intramuscular: IM";
			} else if("IN".equals(oscarRouteCode)) {
				code = "46713006";
				display="Intranasal: IN";
			} else if("PO".equals(oscarRouteCode)) {
				code = "26643006";
				display="Oral: PO";
			} else if("SC".equals(oscarRouteCode)) {
				code = "34206005";
				display="Subcutaneous: SC";
			}
			
			immunization.getRoute().addCoding()
			.setSystem("http://snomed.info/sct")
			.setCode( code )
			.setDisplay( display );		
		}
	}
	
	private void setRoute( ImmunizationInterface immunization ){
		if(!StringUtils.isEmpty(getFhirResource().getRoute().getText())) {
			immunization.setRoute( getFhirResource().getRoute().getText() );
		}
	}

	private void setAnnotation( org.hl7.fhir.dstu3.model.Immunization immunization ){
		immunization.addNote().setText( getOscarResource().getImmunizationRefusedReason() );
		immunization.addNote().setText( getOscarResource().getComment() );
	}

	private void setAnnotation(  ImmunizationInterface immunization ){
		StringBuilder note = new StringBuilder("");
		note.append( getFhirResource().getLocation() );

		for( org.hl7.fhir.dstu3.model.Annotation annotation : getFhirResource().getNote() ) {
			note.append( annotation.getText() );
		}

		immunization.setComment( note.toString() );
	}
	
	/**
	 * For now the immunization reason is hard coded to routine.
	 */
	private void setReason( org.hl7.fhir.dstu3.model.Immunization immunization ) {
		immunization.getExplanation().addReason().addCoding()
			.setSystem("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-immunizations-reason")
			.setCode( "routine" )
			.setDisplay( "Routine" );
	}
	
	/**
	 * Determined if the user has selected if the immunization has been "Done Externally".
	 * 
	 * If the dateIsEstimated is enabled. This value will also be set to NOT be a primary source.
	 * 
	 */
	private void setIsPrimarySource( org.hl7.fhir.dstu3.model.Immunization immunization ) {	
		
		boolean primarySource = getOscarResource().isPrimarySource();

		if( include( OptionalFHIRAttribute.dateIsEstimated ) ) {
			if( primarySource ) {
				primarySource = !isHistorical();
			}
		}
		
		immunization.setPrimarySource( primarySource );
	}
	
	/**
	 * This method is triggered when the user has indicated that the immunization has been performed externally.
	 * 
	 * The display value is changed if the dateIsEstimated is enabled.
	 * 
	 * The source of the data when the report of the immunization event is not based on 
	 * information from the person who administered the vaccine.
	 */
	private void setReportOrigin( org.hl7.fhir.dstu3.model.Immunization immunization ) {	
		String provider = "provider";
		String display = "other";
		
		if( include( OptionalFHIRAttribute.dateIsEstimated ) ) {
			if( isHistorical() ) {
				provider = "record";
			}
		}
		
		if( "provider".equals(provider)  && !StringUtils.isEmpty(getOscarResource().getProviderName())) {
			display = getOscarResource().getProviderName();
		}
		
		immunization.getReportOrigin().addCoding()
			.setSystem("http://hl7.org/fhir/immunization-origin")
			.setCode( provider )
			.setDisplay( display );		
	}
	
	/**
	 * All practitioners added here are ALWAYS the administering provider.
	 * This is the provider that gave the immunization.
	 */
	public void addPerformingPractitioner( Reference reference ) {
		getFhirResource().addPractitioner()
			.setActor( reference )
			.getRole().addCoding()
				.setSystem("http://hl7.org/fhir/v2/0443")
				.setCode("AP")
				.setDisplay("AdministeringProvider");
	}
	
	/**
	 * This will add a reference link to any involved practitioner. 
	 * Not to be confused with administering provider.
	 */
	public void addPractitioner( Reference reference ) {
		getFhirResource().addPractitioner().setActor( reference );
	}
	
	/**
	 * This is a reference link to whom the immunization was given to. 
	 */
	public void setPatientReference( Reference reference ) {
		getFhirResource().setPatient( reference );
	}

	public boolean isHistorical() {
		return isHistorical;
	}

	public void setHistorical(boolean isHistorical) {
		this.isHistorical = isHistorical;
	}

}
