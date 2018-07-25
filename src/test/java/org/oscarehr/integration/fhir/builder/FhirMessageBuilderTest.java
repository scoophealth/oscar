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
// import static org.junit.Assert.*;


import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import org.hl7.fhir.dstu3.model.Attachment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.integration.fhir.manager.OscarFhirConfigurationManager;
import org.oscarehr.integration.fhir.model.ClinicalImpression;
import org.oscarehr.integration.fhir.model.Immunization;
import org.oscarehr.integration.fhir.model.AbstractOscarFhirResource;
import org.oscarehr.integration.fhir.model.Patient;
import org.oscarehr.integration.fhir.model.PerformingPractitioner;
import org.oscarehr.integration.fhir.model.Practitioner;
import org.oscarehr.integration.fhir.model.SubmittingPractitioner;
import org.oscarehr.integration.fhir.resources.Settings;
import org.oscarehr.integration.fhir.resources.constants.FhirDestination;
import org.oscarehr.integration.fhir.resources.constants.Region;
import org.oscarehr.util.LoggedInInfo;

public class FhirMessageBuilderTest {

	private static Clinic clinic;

	private static Provider provider;
	private static Provider nurse;
	private static Provider doctor;
	
	private static Demographic demographic;
	private static Prevention prevention;
	private static Prevention prevention2;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		// create some resources for testing. 
		
		// SENDER
		clinic = new Clinic();
		clinic.setId( 4321 );
		clinic.setClinicAddress("123 Clinic Street");
		clinic.setClinicCity("Vancouver");
		clinic.setClinicProvince("BC");
		clinic.setClinicPhone("778-567-3445");
		clinic.setClinicFax("778-343-3453");
		clinic.setClinicName( "Test Medical Clinic" );

				
		// IMMUNIZATION
		prevention = new Prevention();

		prevention.setImmunizationDate( new Date(System.currentTimeMillis() ) );
		prevention.setImmunizationRefused(Boolean.FALSE);
		prevention.setComment("This is a comment");
		prevention.setVaccineCode("SM1234527");
		prevention.setDose("10cc");
		prevention.setImmunizationType("T");
		prevention.setSite("LD");
		prevention.setRoute("IM");
		prevention.setLotNo("667234");
		prevention.setManufacture("Pfizer");
		prevention.setName("Tetanus Vaccine");
		
		
		prevention2 = new Prevention();

		prevention2.setImmunizationDate( new Date(System.currentTimeMillis() ) );
		prevention2.setImmunizationRefused(Boolean.FALSE);
		prevention2.setImmunizationRefusedReason("Didnt want it.");
		prevention2.setComment("This is a comment");
		prevention2.setVaccineCode("SM4567445527");
		prevention2.setDose("20cc");
		prevention2.setImmunizationType("HPV");
		prevention2.setSite("LD");
		prevention2.setRoute("IM");
		prevention2.setLotNo("123456");
		prevention2.setManufacture("Pfizer");
		prevention2.setName("HPV Vaccine");
		

		// PATIENT
		demographic = new Demographic();
		demographic.setDemographicNo( 122343 );
		demographic.setTitle( "Mr" );
		demographic.setSex( "M" );
		demographic.setFirstName( "Dennis" );
		demographic.setLastName( "Warren" );
		demographic.setAddress( "123 Abc Street");
		demographic.setCity( "Vancouver" );
		demographic.setProvince( "BC" );
		demographic.setPostal("V6E4G7");
		demographic.setPhone( "604-555-1212" );
		demographic.setPhone2( "604-555-5555" );
		demographic.setHin("9876446854");
		demographic.setSpokenLanguage("English");
		Calendar birthdate = Calendar.getInstance();
		birthdate.set(1969, 6, 18);
		demographic.setBirthDay(birthdate);

				
		
		//PRACTITIONER
		provider = new Provider();
		provider.setProviderNo("8879");
		provider.setFirstName( "Doug" );
		provider.setLastName( "Ross" );
		provider.setPractitionerNo("12342");
		provider.setHsoNo( "12342" );
		provider.setOhipNo( "12342" );
		provider.setPhone("604-290-2343");
		provider.setWorkPhone("604-333-2343");
		provider.setPractitionerNoType("CPSO");
		
		nurse = new Provider();
		nurse.setProviderNo("6768");
		nurse.setFirstName( "Nurse" );
		nurse.setLastName( "Betty" );
		nurse.setPractitionerNo("345645");
		nurse.setPhone("645-290-1235");
		nurse.setWorkPhone("604-333-2343");
		nurse.setPractitionerNoType("CNO");
		

		doctor = new Provider();
		doctor.setProviderNo("6433");
		doctor.setFirstName( "Doctor" );
		doctor.setLastName( "Sharp" );
		doctor.setPractitionerNo("457888");
		doctor.setHsoNo( "12342" );
		doctor.setOhipNo( "12342" );
		doctor.setPhone("604-333-2343");
		doctor.setWorkPhone("604-333-2343");
		doctor.setPractitionerNoType("CPSO");
	}

	@AfterClass
	public static void tearDownAfterClass() {
		clinic = null;
		provider = null;
		demographic = null;
		prevention = null;
	}


	/*
	 * BIS formatted messages use a Communication resource. 
	 */
	@Test
	public void testGetBISFormattedMessage() {

		System.out.println( ">>>-- testGetBISFormattedMessage() -->");
		System.out.println();
		
		LoggedInInfo loggedInInfo = new LoggedInInfo();
		Security security = new Security();
		security.setOneIdEmail( "oneid@oneidemail.com" );
		loggedInInfo.setLoggedInProvider (provider );
		loggedInInfo.setLoggedInSecurity(security);
		
		Settings settings = new Settings(FhirDestination.BORN, Region.ON);
		
		OscarFhirConfigurationManager configurationManager = new OscarFhirConfigurationManager( loggedInInfo, settings );
		// normally this is done inside the Configuration manager but this test will not instantiate a DAO
		configurationManager.getSender().setClinic(clinic);
		
		Patient patient = new Patient( demographic, configurationManager );
		Practitioner practitioner = new Practitioner( provider, configurationManager );
				
		// Get the ClinicalImpresson as the Attachment resource for this message. ClinicalImpression is created 
		// to automatically map patient medical annotations. In this case it is being customized after instantiation.
		ClinicalImpression clinicalImpression = new ClinicalImpression( "<xml>This is a test of a clinical annotation</xml>" );
		clinicalImpression.setDescription( "Well Baby" );

		// The communication.sender attribute is set automatically.
		FhirCommunicationBuilder fhirCommunicationBuilder = new FhirCommunicationBuilder( configurationManager );
				
		// this one is tricky.  The patient's managing organization Organization resource is contained inside the Communication resource.
		// and is also represented as the Communication.sender.  So the link needs to be external. 
		// patient.setManagingOrganizationReference( SenderFactory.getSender().getOscarFhirResource().getContainedReferenceLink() );

		// Practitioner is referenced from inside the patient. It is contained inside the Communication resource.
		patient.addGeneralPractitionerReference( practitioner.getContainedReferenceLink() );
		fhirCommunicationBuilder.addResource( practitioner );

		// Patient is contained in the communication.subject attribute
		fhirCommunicationBuilder.setSubject( patient );
		
		// The Attachment resource can be copied from the ClinicalImpression resource.
		// an Attachment can also be added directly through 1 of 4 methods. I.E.:
		// fhirCommunicationBuilder.attachXML( "<xml>This is a test of a clinical annotation</xml>" , "Well Baby" );
		fhirCommunicationBuilder.addAttachment( clinicalImpression.copyToAttachement( new Attachment() ) );
		
		System.out.println( fhirCommunicationBuilder.getMessageJson() );
	}
	
	// @Test
	public void testGetDHIRFormattedMessage() {
		System.out.println( ">>>-- testGetDHIRFormattedMessage() -->");	
		System.out.println();
		
		LoggedInInfo loggedInInfo = new LoggedInInfo();
		Security security = new Security();
		security.setOneIdEmail( "oneid@oneidemail.com" );
		loggedInInfo.setLoggedInProvider (provider );
		loggedInInfo.setLoggedInSecurity(security);
		
		Settings settings = new Settings(FhirDestination.DHIR, Region.ON);
		settings.setIncludeSenderEndpoint( Boolean.FALSE );
		
		// set up the configuration for a DHIR type transmission.
		OscarFhirConfigurationManager configurationManager = new OscarFhirConfigurationManager( loggedInInfo, settings );
		
		Patient patient = new Patient( demographic, configurationManager );
		
		patient.setFocusResource( Boolean.TRUE );

		// The doctor type should be identified in the provider profile. 
		PerformingPractitioner performing = new PerformingPractitioner( provider, configurationManager ); // this could be a nurse or the same as the submitting
		
		// A nurse should be identified in the provider profile.
		PerformingPractitioner performing2 = new PerformingPractitioner( provider, configurationManager ); // this second one is the nurse.
		
		// this is the MRP or the provider in charge.
		// this practitioner must be active AND have a working ONEid code.
		SubmittingPractitioner submitting = new SubmittingPractitioner( provider, configurationManager ); 
		
		Immunization<Prevention> measles = new Immunization<Prevention>( prevention, configurationManager );
		Immunization<Prevention> hpv = new Immunization<Prevention>( prevention2, configurationManager );
	
		// pass the configuration manager into a new FHIR Bundle message builder.
		// the configuration manager will set the Bundle Header automatically. 
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( configurationManager );

		measles.getFhirResource().setPatient( patient.getReference() );
		measles.addPerformingPractitioner( performing.getReference() );
		
		hpv.getFhirResource().setPatient( patient.getReference() );
		hpv.addPerformingPractitioner( performing2.getReference() );
		
		// list of OscarFhirResources. This is only to demonstrate that 
		// resources can be contained in an array.
		HashSet<AbstractOscarFhirResource<?, ?>> resourceList = new HashSet< AbstractOscarFhirResource<?, ?> >();
		resourceList.add( patient );		
		resourceList.add( performing );
		resourceList.add( performing2 );
		resourceList.add( submitting );
		resourceList.add( measles );
		resourceList.add( hpv );
		
		// add the resource list to the message.
		fhirBundleBuilder.addResources( resourceList );
		
		System.out.println( fhirBundleBuilder.getMessageJson() );
	}


}
