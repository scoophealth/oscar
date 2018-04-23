package org.oscarehr.integration.fhir.manager;
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

import java.util.ArrayList;
import java.util.List;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.model.OscarFhirResource;
import org.oscarehr.integration.fhir.model.PerformingPractitioner;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.managers.ProviderManager2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.log.LogAction;

@Service
public class OscarFhirResourceManager {
	
	@Autowired
	private static PreventionManager preventionManager ;
	@Autowired
	private static DemographicManager demographicManager;
	@Autowired
	private static ProviderManager2 providerManager;
	
	public static final List< org.oscarehr.integration.fhir.model.Immunization > getImmunizationsByDemographicNo( OscarFhirConfigurationManager configurationManager, int demographicNo ) {
		//TODO what kind of security check goes here?
		List< org.oscarehr.integration.fhir.model.Immunization > immunizations = null;
		List<Prevention> preventions = preventionManager.getPreventionsByDemographicNo( configurationManager.getLoggedInInfo(), demographicNo );
		
		if( preventions != null ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getImmunizationsByDemographicNo", "Retrieved Immunization list for FHIR transport " + preventions.toString() );
					
			for( Prevention prevention : preventions ) {
				
				if( prevention.isImmunization() ) {
					
					if( immunizations == null ) {
						immunizations = new ArrayList< org.oscarehr.integration.fhir.model.Immunization >();							
					}
					org.oscarehr.integration.fhir.model.Immunization immunization = new org.oscarehr.integration.fhir.model.Immunization( prevention, configurationManager );
					immunizations.add( immunization );
				}
			}
		}
		
		return immunizations;
	}
	
	public static final org.oscarehr.integration.fhir.model.Patient getPatientByDemographicNumber( OscarFhirConfigurationManager configurationManager, int demographic_no ) {
		Demographic demographic = demographicManager.getDemographic( configurationManager.getLoggedInInfo(), demographic_no);
		org.oscarehr.integration.fhir.model.Patient patient = null;
		
		if( demographic != null ) {
			patient = new org.oscarehr.integration.fhir.model.Patient( demographic, configurationManager );
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getPatientByDemographicNumber", "Retrieved demographic " + demographic_no  + " " + patient.toString() );
		}
		
		return patient;
	}
	
	public static final List<org.oscarehr.integration.fhir.model.Patient> getPatientsByPHN( OscarFhirConfigurationManager configurationManager, String hcn, String hcnType ) {
		List<Demographic> demographicList = demographicManager.getActiveDemosByHealthCardNo( configurationManager.getLoggedInInfo(), hcn, hcnType );
		List<org.oscarehr.integration.fhir.model.Patient> patientList = null;
		
		if( demographicList != null ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getPatientsByPHN", "Retrieved demographic hcn " + hcn + " " + demographicList.toString() );
				
			for( Demographic demographic : demographicList ) {
				
				if( patientList == null ) {
					patientList = new ArrayList<org.oscarehr.integration.fhir.model.Patient>();
				}
				org.oscarehr.integration.fhir.model.Patient patient = new org.oscarehr.integration.fhir.model.Patient( demographic, configurationManager );
				patientList.add( patient );
			}		
		}
		
		return patientList;
	}
	
	public static final org.oscarehr.integration.fhir.model.PerformingPractitioner getPerformingPractitionerByProviderNumber( OscarFhirConfigurationManager configurationManager, String providerNo ) {
		Provider provider = providerManager.getProvider( configurationManager.getLoggedInInfo(), providerNo );
		org.oscarehr.integration.fhir.model.PerformingPractitioner practitioner = null;
		
		if( provider != null ) {
			practitioner = new org.oscarehr.integration.fhir.model.PerformingPractitioner( provider, configurationManager );
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getProviderByProviderNumber", "Retrieved provider " + providerNo + " " + provider.toString() );
		}
		
		return practitioner;
	}
	
	public static final org.oscarehr.integration.fhir.model.Practitioner getPractitionerByProviderNumber( OscarFhirConfigurationManager configurationManager, String providerNo ) {
		Provider provider = providerManager.getProvider( configurationManager.getLoggedInInfo(), providerNo );
		org.oscarehr.integration.fhir.model.Practitioner practitioner = null;
		
		if( provider != null ) {
			practitioner = new org.oscarehr.integration.fhir.model.Practitioner( provider, configurationManager );
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getProviderByProviderNumber", "Retrieved provider " + providerNo + " " + provider.toString() );
		}
		
		return practitioner;
	}
	
	/**
	 * Builds a list of linked resources of Immunization data by patient for insertion into a message Bundle 
	 * Contains:
	 * - Immunizations
	 * - Patient
	 * - SubmittingPractitioner
	 * - PerformingPractitioner
	 */
	public static final List<OscarFhirResource<?,?>> getImmunizationResourceBundle( OscarFhirConfigurationManager configurationManager, org.oscarehr.integration.fhir.model.Patient patient ) {
		
		List<org.oscarehr.integration.fhir.model.Immunization> immunizations = OscarFhirResourceManager.getImmunizationsByDemographicNo( configurationManager, patient.getOscarResource().getDemographicNo() );
		org.oscarehr.integration.fhir.model.SubmittingPractitioner submittingPractitioner = new org.oscarehr.integration.fhir.model.SubmittingPractitioner( configurationManager.getLoggedInInfo().getLoggedInProvider(), configurationManager );
	
		List<OscarFhirResource<?,?>> resourceList = OscarFhirResourceManager.setPerformingPractitionerAndPatient( configurationManager, immunizations, patient );
		resourceList.add( submittingPractitioner );
		resourceList.add( patient );
		
		return resourceList;
	}
	
	public static final List<OscarFhirResource<?,?>> setPerformingPractitionerAndPatient( 
			OscarFhirConfigurationManager configurationManager, 
			List<org.oscarehr.integration.fhir.model.Immunization> immunizations, 
			org.oscarehr.integration.fhir.model.Patient patient ) {
		
		List<OscarFhirResource<?,?>> resourceList = new ArrayList<OscarFhirResource<?,?>>();

		if( immunizations != null && ! immunizations.isEmpty() ) {

			String previousPerformingProviderNo = null; 
					
			for( org.oscarehr.integration.fhir.model.Immunization immunization : immunizations ) {
				
				String performingProviderNo = immunization.getOscarResource().getProviderNo();
				PerformingPractitioner performingPractitioner = OscarFhirResourceManager.getPerformingPractitionerByProviderNumber( configurationManager, performingProviderNo );
	
				if( performingPractitioner != null ) {
					immunization.addPerformingPractitioner( performingPractitioner.getReference() );
					
					if( ! performingProviderNo.equals( previousPerformingProviderNo ) ) {
						resourceList.add( performingPractitioner );
					}
					
					previousPerformingProviderNo = performingProviderNo;
				}
				
				immunization.setPatientReference( patient.getReference() );
				resourceList.add( immunization );				
			}
		}
		
		return resourceList;
	}

}
