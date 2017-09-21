package org.oscarehr.integration.fhir.builder;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Communication.CommunicationStatus;
import org.hl7.fhir.dstu3.model.Immunization;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.ImmunizationInterface;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.integration.fhir.model.OscarFhirResource;

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
		
		// Sender data can be added through the constructor or by individual modifiers.
		Sender sender = new Sender(vendorName,softwareName,buildName,senderEndpoint);
		
		// if there is only one destination it can be added through the constructor
		// Use the addMessageDestination to add more.
		Destination destination = new Destination(destinationName,destinationEndpoint);
		destination.addDestination(destinationName2, destinationEndpoint2);
		
		// pass the Sender and Destination through the constructor if the builder is expected to 
		// produce a MessageHeader.
		fhirMessageBuilder = new FhirMessageBuilder( sender, destination );
		
		// set the reason for the message
		fhirMessageBuilder.setReason( "The reason for this message is to text the FHIR builder operations." );
		
		// communication status
		fhirMessageBuilder.setCommunicationStatus(CommunicationStatus.COMPLETED);
		
		// build new Oscar resource to send
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
		List<OscarFhirResource<?, ?>> resourceList = new ArrayList<OscarFhirResource<?, ?>>();
		resourceList.add(oscarFhirResource);
		
		// add the resource to the message.
		fhirMessageBuilder.addResources( resourceList );

	}

	@AfterClass
	public static void tearDownAfterClass() {
		fhirMessageBuilder = null;
	}

	@Test
	public void testGetMessageHeader() {		
		System.out.println( fhirMessageBuilder.getMessageHeader().getSource().getEndpoint() );
	}
	
	@Test
	public void testGetMessageHeaderJson() {		
		System.out.println( fhirMessageBuilder.getMessageHeaderJson() );
	}

	@Test
	public void testGetCommunication() {
		System.out.println( fhirMessageBuilder.getCommunication().getContained() );
	}
	
	@Test
	public void testGetCommunicationJson() {
		System.out.println( fhirMessageBuilder.getCommunicationJson() );
	}

	@Test
	public void testGetSender() {	
		assertEquals( vendorName, fhirMessageBuilder.getSender().getVendorName() );
		assertEquals( softwareName, fhirMessageBuilder.getSender().getSoftwareName() );
		assertEquals( buildName, fhirMessageBuilder.getSender().getVersionSignature() );
		assertEquals( senderEndpoint, fhirMessageBuilder.getSender().getEndpoint() );		
	}

	@Test
	public void testGetDestination() {		
		System.out.println( fhirMessageBuilder.getDestination() );
		assertEquals( destinationEndpoint2, fhirMessageBuilder.getDestination().getDestinations().get( destinationName2 ) );
		assertEquals( destinationEndpoint, fhirMessageBuilder.getDestination().getDestinations().get( destinationName ) );
	}

	@Test
	public void testGetResources() {		
		System.out.println( fhirMessageBuilder.getResources() );
	}

}
