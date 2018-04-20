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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Identifier;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.model.Immunization;
import org.oscarehr.integration.fhir.model.OscarFhirResource;
import org.oscarehr.integration.fhir.model.PerformingPractitioner;
import org.oscarehr.integration.fhir.resources.types.PublicHealthUnitType;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.LookupListManager;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Service;
import oscar.OscarProperties;
import oscar.log.LogAction;

@Service
public class OscarFhirResourceManager {
	

	public static final List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > getImmunizationsByDemographicNo( OscarFhirConfigurationManager configurationManager, int demographicNo ) {
		PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);
		
		//TODO what kind of security check goes here?
		List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > immunizations = null;
		List<Prevention> preventions = preventionManager.getPreventionsByDemographicNo( configurationManager.getLoggedInInfo(), demographicNo );
		
		if( preventions != null ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getImmunizationsByDemographicNo", "Retrieved Immunization list for FHIR transport "  );
					
			for( Prevention prevention : preventions ) {
				
				if( prevention.isImmunization() ) {
					
					if( immunizations == null ) {
						immunizations = new ArrayList< org.oscarehr.integration.fhir.model.Immunization<Prevention> >();							
					}
					org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization = new org.oscarehr.integration.fhir.model.Immunization<Prevention>( prevention, configurationManager );
					immunizations.add( immunization );
				}
			}
		}
		
		return immunizations;
	}

	
	public static final org.oscarehr.integration.fhir.model.Immunization<Prevention> getImmunizationByDemographicNoAndId( OscarFhirConfigurationManager configurationManager, int demographicNo , int preventionId) {
		PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);
		Prevention prevention = preventionManager.getPrevention(configurationManager.getLoggedInInfo(), preventionId);
		org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization = null;

		if( prevention != null || prevention.getDemographicId() == demographicNo ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getImmunizationsByDemographicNo", "Retrieved Immunization list for FHIR transport "  );
			immunization = new org.oscarehr.integration.fhir.model.Immunization<Prevention>( prevention, configurationManager );		
		}
		
		return immunization;
	}
	
	public static final org.oscarehr.integration.fhir.model.Patient getPatientByDemographicNumber( OscarFhirConfigurationManager configurationManager, int demographic_no ) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		
		Demographic demographic = demographicManager.getDemographic( configurationManager.getLoggedInInfo(), demographic_no);
		org.oscarehr.integration.fhir.model.Patient patient = null;
		
		if( demographic != null ) {
			patient = new org.oscarehr.integration.fhir.model.Patient( demographic, configurationManager );
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getPatientByDemographicNumber", "Retrieved demographic " + demographic_no  + " " + patient.toString() );
		}
		
		return patient;
	}
	
	public static final List<org.oscarehr.integration.fhir.model.Patient> getPatientsByPHN( OscarFhirConfigurationManager configurationManager, String hcn, String hcnType ) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		
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
		ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
		
		Provider provider = providerManager.getProvider( configurationManager.getLoggedInInfo(), providerNo );
		org.oscarehr.integration.fhir.model.PerformingPractitioner practitioner = null;
		
		if( provider != null ) {
			practitioner = new org.oscarehr.integration.fhir.model.PerformingPractitioner( provider, configurationManager );
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getProviderByProviderNumber", "Retrieved provider " + providerNo + " " + provider.toString() );
		}
		
		return practitioner;
	}
	
	public static final org.oscarehr.integration.fhir.model.Practitioner getPractitionerByProviderNumber( OscarFhirConfigurationManager configurationManager, String providerNo ) {
		ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
		
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
	public static final HashSet<OscarFhirResource<?,?>> getImmunizationResourceBundle( OscarFhirConfigurationManager configurationManager, org.oscarehr.integration.fhir.model.Patient patient, HashSet<OscarFhirResource<?,?>> resourceList ) {
		
		List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > immunizations = OscarFhirResourceManager.getImmunizationsByDemographicNo( configurationManager, patient.getOscarResource().getDemographicNo() );
		return OscarFhirResourceManager.setPerformingPractitionerAndPatient( configurationManager, immunizations, patient, resourceList );
	}
	
	/**
	 * Builds a list of linked resources of Immunization data by patient for insertion into a message Bundle 
	 * Contains:
	 * - Immunizations
	 * - Patient
	 * - SubmittingPractitioner
	 * - PerformingPractitioner
	 */
	public static final HashSet<OscarFhirResource<?,?>> getImmunizationResourceBundle( OscarFhirConfigurationManager configurationManager, org.oscarehr.integration.fhir.model.Patient patient, int preventionId, HashSet<OscarFhirResource<?,?>> resourceList ) {
		
		org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization = OscarFhirResourceManager.getImmunizationByDemographicNoAndId( configurationManager, patient.getOscarResource().getDemographicNo() , preventionId);
		return OscarFhirResourceManager.setPerformingPractitionerAndPatient( configurationManager, immunization, patient, resourceList  );
	}
	
	public static final HashSet<OscarFhirResource<?,?>> setPerformingPractitionerAndPatient( 
			OscarFhirConfigurationManager configurationManager, 
			org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization, 
			org.oscarehr.integration.fhir.model.Patient patient, HashSet<OscarFhirResource<?,?>> resourceList) {
		
		String performingProviderNo = immunization.getOscarResource().getProviderNo();
		PerformingPractitioner performingPractitioner = OscarFhirResourceManager.getPerformingPractitionerByProviderNumber( configurationManager, performingProviderNo );
		if( performingPractitioner != null ) {
			immunization.addPerformingPractitioner( performingPractitioner.getReference() );
			resourceList.add( performingPractitioner );
		}
		immunization.setPatientReference( patient.getReference() );
		resourceList.add( patient );
		resourceList.add( immunization );
		
		return resourceList;
	}
	
	public static final HashSet<OscarFhirResource<?,?>> setPerformingPractitionerAndPatient( 
			OscarFhirConfigurationManager configurationManager, 
			List<org.oscarehr.integration.fhir.model.Immunization<Prevention>> immunizations, 
			org.oscarehr.integration.fhir.model.Patient patient, HashSet<OscarFhirResource<?,?>> resourceList ) {

		if( immunizations != null && ! immunizations.isEmpty() ) {		
			for( Immunization<Prevention> immunization : immunizations ) {				
				setPerformingPractitionerAndPatient( configurationManager, immunization, patient, resourceList );			
			}
		}
		
		return resourceList;
	}
	
	public static final org.hl7.fhir.dstu3.model.Organization getPublicHealthUnit( OscarFhirConfigurationManager configurationManager, String phuId ) {
		PublicHealthUnitType publicHealthUnitType  = getPublicHealthUnitType( configurationManager, phuId );
		org.hl7.fhir.dstu3.model.Organization organization = new org.hl7.fhir.dstu3.model.Organization();
		if( publicHealthUnitType != null ) {
			organization = new org.hl7.fhir.dstu3.model.Organization();
			organization.setId( UUID.randomUUID().toString() );
			Identifier identifier = new Identifier();
			identifier.setSystem( publicHealthUnitType.getSystemURI() ).setValue( publicHealthUnitType.getId() );
			organization.addIdentifier( identifier );
			organization.setName( publicHealthUnitType.getName() );
		}		
		return organization;
	}
	
	public static final PublicHealthUnitType getPublicHealthUnitType( OscarFhirConfigurationManager configurationManager, String phuId ) {
		PublicHealthUnitType publicHealthUnitType = null;
		LookupListItem lookupListItem = null;
		
		if( phuId == null || phuId.isEmpty() ) {
			phuId = OscarProperties.getInstance().getProperty( PublicHealthUnitType.PhuKey.default_phu.name(), null );
		}

		LookupListManager lookupListManager = SpringUtils.getBean(LookupListManager.class);
		LookupList lookupList = lookupListManager.findLookupListByName( configurationManager.getLoggedInInfo(), PublicHealthUnitType.PhuKey.phu.name() );			
			
		if( lookupList != null ) {
			lookupListItem = lookupListManager.findLookupListItemByLookupListIdAndValue(configurationManager.getLoggedInInfo(), lookupList.getId(), phuId);
		} 
		
		if( lookupListItem != null ) {
			publicHealthUnitType = new PublicHealthUnitType( lookupListItem.getValue(), lookupListItem.getLabel() );
			
			// TODO inject the system URI from the configuration manager.
			
			publicHealthUnitType.setSystemURI( "https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-panorama-phu-id" );
		}
						
		return publicHealthUnitType;
	}

}
