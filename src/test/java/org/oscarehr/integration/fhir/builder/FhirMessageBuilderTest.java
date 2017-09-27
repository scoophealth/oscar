package org.oscarehr.integration.fhir.builder;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.hl7.fhir.dstu3.model.Immunization;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ImmunizationInterface;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.model.OscarFhirResource;
import org.oscarehr.integration.fhir.model.Patient;

import ca.uhn.fhir.context.FhirContext;

public class FhirMessageBuilderTest {

	private static FhirMessageBuilder fhirMessageBuilder;
	private static String vendorName = "Colcamex Resources";
	private static String softwareName = "Oscar 15";
	private static String buildName = "custom build 1";
	private static String senderEndpoint = "https://my.oscartest.com/oscar_test";
	private static String destinationName = "Immunization Data Centre";
	private static String destinationEndpoint = "https://the.datacentre.com/fhir/immun/data/";
	private static String destinationName2 = "Another Destination";
	private static String destinationEndpoint2 = "https://the.another.com/fhir/immun/data/";
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		// SENDER
		Clinic clinic = new Clinic();
		clinic.setClinicAddress("123 Clinic Street");
		clinic.setClinicCity("Vancouver");
		clinic.setClinicProvince("BC");
		clinic.setClinicPhone("778-567-3445");
		clinic.setClinicFax("778-343-3453");
		clinic.setClinicName("Test Medical Clinic");

		Sender sender = new Sender(vendorName,softwareName,buildName,senderEndpoint);
		sender.addClinic( clinic );
		
		// DESTINATION
		// if there is only one destination it can be added through the constructor
		// Use the addMessageDestination to add more.
		Destination destination = new Destination(destinationName,destinationEndpoint);
		destination.addDestination(destinationName2, destinationEndpoint2);
		
		// pass the Sender and Destination through the constructor if the builder is expected to 
		// produce a MessageHeader.
		fhirMessageBuilder = new FhirMessageBuilder( sender, destination );
		
		// set the reason for the message
		fhirMessageBuilder.setReason( "The reason for this message is to test the FHIR builder operation." );
		
		// communication status
		fhirMessageBuilder.setCommunicationStatus(CommunicationStatus.COMPLETED);
		
		List<OscarFhirResource<?, ?>> resourceList = new ArrayList<OscarFhirResource<?, ?>>();
		
		// IMMUNIZATION
		ImmunizationInterface<Prevention> immunization = new Prevention();
		
		immunization.setImmunizationDate( new Date(System.currentTimeMillis() ) );
		immunization.setImmunizationRefused(Boolean.FALSE);
		immunization.setImmunizationRefusedReason("Didnt want it.");
		immunization.setComment("This is a comment");
		immunization.setDose("20cc");
		immunization.setImmunizationType("HPV");
		immunization.setSite("LD");
		immunization.setRoute("IM");
		immunization.setLotNo("123456");
		immunization.setManufacture("Pfizer");
		immunization.setName("HPV Vaccine");
		
		// convert the Oscar Model into a FHIR Resource.
		OscarFhirResource<Immunization, Prevention> oscarFhirResource = new org.oscarehr.integration.fhir.model.Immunization( immunization );	
		
		
		// PATIENT
		Demographic demographic = new Demographic();
		demographic.setDemographicNo( 122343 );
		demographic.setTitle( "Mr" );
		demographic.setSex( "M" );
		demographic.setFirstName( "Dennis" );
		demographic.setLastName( "Warren" );
		demographic.setAddress( "123 Abc Street");
		demographic.setCitizenship( "Vancouver" );
		demographic.setProvince( "BC" );
		demographic.setPhone( "604-555-1212" );
		demographic.setPhone2( "604-555-5555" );
		demographic.setHin("9876446854");
		demographic.setSpokenLanguage("English");
		Calendar birthdate = Calendar.getInstance();
		birthdate.set(1969, 6, 18);
		demographic.setBirthDay(birthdate);
		
		Patient patient = new Patient( demographic );		
		
		//PRACTITIONER
		Provider provider = new Provider();
		provider.setProviderNo("8879");
		provider.setFirstName( "Doug" );
		provider.setLastName( "Ross" );
		provider.setHsoNo( "12342" );
		provider.setOhipNo( "12342" );
		
		// Practitioner practitioner = new Practitioner( provider );
		
		// Even though this is discouraged. The Practitioner Resource can be contained by the Patient Resource
		patient.addCareProvider( provider );
		
		resourceList.add( oscarFhirResource );
		resourceList.add( patient );
		
		// add the resource to the message.
		fhirMessageBuilder.addResources( resourceList );
		fhirMessageBuilder.attachPDF("Attaching a PDF document");
		fhirMessageBuilder.attachRichText( "This is some rich text \n" );
		fhirMessageBuilder.attachText( "This is a bit of plain text" );

	}

	@AfterClass
	public static void tearDownAfterClass() {
		fhirMessageBuilder = null;
	}

	// @Test
	public void testGetMessageHeader() {		
		System.out.println( fhirMessageBuilder.getMessageHeader().getSource().getEndpoint() );
	}
	
	// @Test
	public void testGetMessageHeaderJson() {		
		System.out.println( fhirMessageBuilder.getMessageHeaderJson() );
	}

	// @Test
	public void testGetCommunication() {
		System.out.println( fhirMessageBuilder.getCommunication().getContained() );
	}
	
	@Test
	public void testGetCommunicationJson() {
		System.out.println( fhirMessageBuilder.getCommunicationJson() );
	}

	// @Test
	public void testGetSender() {	
		assertEquals( vendorName, fhirMessageBuilder.getSender().getVendorName() );
		assertEquals( softwareName, fhirMessageBuilder.getSender().getSoftwareName() );
		assertEquals( buildName, fhirMessageBuilder.getSender().getVersionSignature() );
		assertEquals( senderEndpoint, fhirMessageBuilder.getSender().getEndpoint() );		
	}

	// @Test
	public void testGetDestination() {		
		System.out.println( fhirMessageBuilder.getDestination() );
		assertEquals( destinationEndpoint2, fhirMessageBuilder.getDestination().getDestinations().get( destinationName2 ) );
		assertEquals( destinationEndpoint, fhirMessageBuilder.getDestination().getDestinations().get( destinationName ) );
	}

	// @Test
	public void testGetResources() {		
		System.out.println( fhirMessageBuilder.getCommunication().getContained() );
		System.out.println( FhirContext.forDstu3().newJsonParser().setPrettyPrint(true).encodeResourceToString( fhirMessageBuilder.getCommunication().getContained().get(0) ) );
	}

}
