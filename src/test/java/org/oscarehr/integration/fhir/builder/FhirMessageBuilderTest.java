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

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Reference;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.interfaces.ImmunizationInterface;
import org.oscarehr.integration.fhir.model.ClinicalImpression;
import org.oscarehr.integration.fhir.model.Destination;
import org.oscarehr.integration.fhir.model.Immunization;
import org.oscarehr.integration.fhir.model.Organization;
import org.oscarehr.integration.fhir.model.OscarFhirResource;
import org.oscarehr.integration.fhir.model.Patient;
import org.oscarehr.integration.fhir.model.Practitioner;


import ca.uhn.fhir.context.FhirContext;

public class FhirMessageBuilderTest {

	private static String destinationName = "BORN Immunization Data Centre";
	private static String destinationEndpoint = "https://the.datacentre.com/fhir/immun/data/";
	
	private static String destinationName2 = "DHIR";
	private static String destinationEndpoint2 = "https://wsgateway.prod.ehealthontario.ca/API/FHIR/Immunizations/v1/";
	
	private static Clinic clinic;

	private static Provider provider;
	private static Provider provider2;
	
	private static Demographic demographic;
	private static ImmunizationInterface<Prevention> prevention;
	private static ImmunizationInterface<Prevention> prevention2;
	
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
		clinic.setClinicName("Test Medical Clinic");

				
		// IMMUNIZATION
		prevention = new Prevention();

		prevention.setImmunizationDate( new Date(System.currentTimeMillis() ) );
		prevention.setImmunizationRefused(Boolean.FALSE);
		prevention.setComment("This is a comment");
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
		
		provider2 = new Provider();
		provider2.setProviderNo("1000");
		provider2.setFirstName( "Nurse" );
		provider2.setLastName( "Betty" );

	}

	@AfterClass
	public static void tearDownAfterClass() {
		clinic = null;
		provider = null;
		demographic = null;
		prevention = null;

	}
	
//	@After
//	public void tearDownAfter() {
//		fhirMessageBuilder = null;
//	}

	// @Test
	public void testGetBISFormattedMessage() {
		System.out.println( ">>>-- testGetBISFormattedMessage() -->");
		System.out.println();
				
		// Collect the required resources. 
		Organization organization = new Organization( clinic );
		Practitioner practitioner = new Practitioner( provider );
		ClinicalImpression clinicalImpression = new ClinicalImpression( "<xml>This is a test of a clinical annotation</xml>" );
		clinicalImpression.setDescription("Well Baby");
		Patient patient = new Patient( demographic );
		
		Destination destination = new Destination(destinationName,destinationEndpoint);
		// pass the Sender and Destination through the constructor and the MessageBuilder Class will build the MessageHeader.
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );
		
		// set all the resource links according to BIS documentation
		fhirBundleBuilder.getMessageHeader().addFocus().setReference( patient.getReferenceLink() );
		fhirBundleBuilder.getMessageHeader().getResponsible().setReference( organization.getReferenceLink() );
		fhirBundleBuilder.getMessageHeader().getAuthor().setReference( practitioner.getReferenceLink() );
		fhirBundleBuilder.getMessageHeader().getSender().setDisplay("CLINICBORN");
		
		patient.getFhirResource().getManagingOrganization().setReference( organization.getReferenceLink() );
		
		clinicalImpression.getFhirResource().getSubject().setReference( patient.getReferenceLink() );
		
		// compile a list of OscarFhirResources.
		List<OscarFhirResource<?, ?>> resourceList = new ArrayList<OscarFhirResource<?, ?>>();
		resourceList.add( patient );
		resourceList.add( organization );
		resourceList.add( practitioner );
		resourceList.add( clinicalImpression );
		
		// add the resource list to the message.
		fhirBundleBuilder.addResources( resourceList );

		System.out.println( fhirBundleBuilder.getMessageJson() );
	}
	
	@Test
	public void testGetDHIRFormattedMessage() {
		System.out.println( ">>>-- testGetDHIRFormattedMessage() -->");	
		System.out.println();
		
		// Collect the required resources. 
		Organization responsible = new Organization( clinic );
		Practitioner nurse = new Practitioner( provider2 );
		Practitioner mrp = new Practitioner( provider );
		Immunization measles = new Immunization( prevention );
		Immunization hpv = new Immunization( prevention2 );
		Patient patient = new Patient( demographic );
		
		Destination destination = new Destination(destinationName2,destinationEndpoint2);
		
		// pass the Sender and Destination through the constructor and the MessageBuilder Class will build the MessageHeader.
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );
		
		// alternate method for setting the messageHeader reference links.
		fhirBundleBuilder.addMessageHeaderFocus( patient.getReference() );
		fhirBundleBuilder.setMessageHeaderSender( clinic.getClinicName() ); // this should come from the Sender object.
		fhirBundleBuilder.setMessageHeaderAuthor( mrp.getReference() );
		fhirBundleBuilder.setMessageHeaderResponsible( responsible.getReference() );
		
		measles.getFhirResource().setPatient( patient.getReference() );
		measles.setAdministeringProvider( mrp.getReference() );
		
		hpv.getFhirResource().setPatient( patient.getReference() );
		hpv.setAdministeringProvider( nurse.getReference() );
		
		// compile a list of OscarFhirResources.
		List<OscarFhirResource<?, ?>> resourceList = new ArrayList< OscarFhirResource<?, ?> >();
		resourceList.add( patient );
		resourceList.add( responsible );		
		resourceList.add( nurse );
		resourceList.add( mrp );
		resourceList.add( measles );
		resourceList.add( hpv );
		
		// add the resource list to the message.
		fhirBundleBuilder.addResources( resourceList );
		
		System.out.println( fhirBundleBuilder.getMessageJson() );
	}
	
	@Test
	public void testGetReferences() {
		System.out.println( ">>>-- testGetReferences() -->");
		System.out.println();
		
		// Collect the required resources. 
		Organization organization = new Organization( clinic );
		Practitioner practitioner = new Practitioner( provider );
		Immunization immunization = new Immunization( prevention );
		Patient patient = new Patient( demographic );
		
		Destination destination = new Destination(destinationName, destinationEndpoint);
		
		// pass the Sender and Destination through the constructor and the MessageBuilder Class will build the MessageHeader.
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );

		// compile a list of OscarFhirResources.
		List<OscarFhirResource<?, ?>> resourceList = new ArrayList< OscarFhirResource<?, ?> >();
		resourceList.add( patient );
		resourceList.add( organization );
		resourceList.add( practitioner );
		resourceList.add( immunization );
		
		// add the resource list to the message.
		fhirBundleBuilder.addResources( resourceList );	
		
		Map<?,?> references = fhirBundleBuilder.getReferences();
		Set<?> keySet = references.keySet();
		for( Object key : keySet) {
			System.out.println( "Reference Key: " + key );
			System.out.println( "Reference Link: " +  ( (Reference) references.get(key) ).getReference() );
		}
	}

	// @Test
	public void testGetSender() {
		System.out.println( ">>>-- testGetSender() -->");
		System.out.println();
		
		Destination destination = new Destination(destinationName, destinationEndpoint);
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );
//		assertEquals( vendorName, fhirBundleBuilder.getSender().getVendorName() );
//		assertEquals( softwareName, fhirBundleBuilder.getSender().getSoftwareName() );
//		assertEquals( buildName, fhirBundleBuilder.getSender().getVersionSignature() );
//		assertEquals( senderEndpoint, fhirBundleBuilder.getSender().getEndpoint() );		
	}

	// @Test
	public void testGetDestination() {	
		System.out.println( ">>>-- testGetDestination() -->");
		System.out.println();
		
		Destination destination = new Destination(destinationName, destinationEndpoint);
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );
		System.out.println( fhirBundleBuilder.getDestination() );
		assertEquals( destinationEndpoint, fhirBundleBuilder.getDestination().getDestinations().get( destinationName ) );

	}

	// @Test
	public void testGetResources() {
		System.out.println( ">>>-- testGetResources() -->");
		System.out.println();
		
		Destination destination = new Destination(destinationName, destinationEndpoint);
		FhirBundleBuilder fhirBundleBuilder = new FhirBundleBuilder( SenderFactory.getSender(), destination );
		System.out.println( fhirBundleBuilder.getBundle() );
		System.out.println( FhirContext.forDstu3().newJsonParser().setPrettyPrint(true).encodeResourceToString( fhirBundleBuilder.getResources().get(0).getFhirResource() ) );
	}

}
