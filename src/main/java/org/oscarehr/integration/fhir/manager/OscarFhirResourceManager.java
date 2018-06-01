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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Identifier;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.fhir.model.Immunization;
import org.oscarehr.integration.fhir.model.AbstractOscarFhirResource;
import org.oscarehr.integration.fhir.model.PerformingPractitioner;
import org.oscarehr.integration.fhir.model.Practitioner;
import org.oscarehr.integration.fhir.resources.types.PublicHealthUnitType;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.LookupListManager;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Service;
import oscar.OscarProperties;
import oscar.log.LogAction;

@Service
public class OscarFhirResourceManager {
	
	/**
	 * 
	 * @param configurationManager
	 * @param demographicNo
	 * @return List< org.oscarehr.integration.fhir.model.Immunization<Prevention> >
	 */
	public static final List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > getImmunizationsByDemographicNo( OscarFhirConfigurationManager configurationManager, int demographicNo ) {
		PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);
		
		//TODO what kind of security check goes here?
		
		List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > immunizations = null;
		List<Prevention> preventions = preventionManager.getPreventionsByDemographicNo( configurationManager.getLoggedInInfo(), demographicNo );
		
		if( preventions != null ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getImmunizationsByDemographicNo", "Retrieved Immunization list for FHIR transport "  );
					
			for( Prevention prevention : preventions ) {
				
				//TODO there needs to be a better method to identify an ISPA Immunization.  This "isImmunization" method can be changed
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

	/**
	 * 
	 * @param configurationManager
	 * @param preventionId
	 * @return org.oscarehr.integration.fhir.model.Immunization<Prevention>
	 */
	public static final org.oscarehr.integration.fhir.model.Immunization<Prevention> getImmunizationById( OscarFhirConfigurationManager configurationManager, int preventionId) {
		PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);
		Prevention prevention = preventionManager.getPrevention(configurationManager.getLoggedInInfo(), preventionId);
	
		org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization = null;

		if( prevention != null ) {
			LogAction.addLogSynchronous( configurationManager.getLoggedInInfo(), "OscarFhirResourceManager.getImmunizationsByDemographicNo", "Retrieved Immunization list for FHIR transport "  );
			immunization = new org.oscarehr.integration.fhir.model.Immunization<Prevention>( prevention, configurationManager );		
		}
		
		return immunization;
	}
	
	/**
	 * 
	 * @param configurationManager
	 * @param demographic_no
	 * @return  org.oscarehr.integration.fhir.model.Patient
	 */
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
	
	/**
	 * 
	 * @param configurationManager
	 * @param hcn
	 * @param hcnType
	 * @return List<org.oscarehr.integration.fhir.model.Patient>
	 */
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
	
	public static final org.oscarehr.integration.fhir.model.Practitioner getDemographicMostResponsiblePractitioner( OscarFhirConfigurationManager configurationManager, int demographic_no ) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		Demographic demographic = demographicManager.getDemographic(configurationManager.getLoggedInInfo(), demographic_no);
		Provider mrp = demographic.getProvider();
		List<Provider> providerList = Collections.emptyList();
		Practitioner practitioner = null;
		
		if(mrp == null) {
			providerList = demographicManager.getDemographicMostResponsibleProviders(configurationManager.getLoggedInInfo(), demographic_no);
		}
		
		if(! providerList.isEmpty()) {
			mrp = providerList.get(0); 
		}
		
		if(mrp != null) {
			practitioner = new Practitioner(mrp, configurationManager);
		}
		
		return practitioner;
	}
	
	/**
	 * 
	 * @param configurationManager
	 * @param providerNo
	 * @return org.oscarehr.integration.fhir.model.PerformingPractitioner
	 */
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
	
	/**
	 * 
	 * @param configurationManager
	 * @param providerNo
	 * @return org.oscarehr.integration.fhir.model.Practitioner
	 */
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
	 * The returned HashSet contains:
	 * - Immunizations
	 * - Patient
	 * - SubmittingPractitioner
	 * - PerformingPractitioner
	 */
	public static final HashSet<AbstractOscarFhirResource<?,?>> getImmunizationResourceBundle( OscarFhirConfigurationManager configurationManager, org.oscarehr.integration.fhir.model.Patient patient, HashSet<AbstractOscarFhirResource<?,?>> resourceList ) {
		
		List< org.oscarehr.integration.fhir.model.Immunization<Prevention> > immunizations = OscarFhirResourceManager.getImmunizationsByDemographicNo( configurationManager, patient.getOscarResource().getDemographicNo() );
		if( immunizations != null ) {
			 OscarFhirResourceManager.linkPerformingPractitionerAndPatient( configurationManager, immunizations, patient, resourceList );
		}
		
		return resourceList;
	}
	
	/**
	 * Builds a list of linked resources of Immunization data by patient for insertion into a message Bundle 
	 * The returned HashSet contains:
	 * - Immunizations
	 * - Patient
	 * - SubmittingPractitioner
	 * - PerformingPractitioner
	 */
	public static final HashSet<AbstractOscarFhirResource<?,?>> getImmunizationResourceBundle( OscarFhirConfigurationManager configurationManager, org.oscarehr.integration.fhir.model.Patient patient, int preventionId, HashSet<AbstractOscarFhirResource<?,?>> resourceList ) {
		
		org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization = OscarFhirResourceManager.getImmunizationById( configurationManager, preventionId);
		if( immunization != null) {
			OscarFhirResourceManager.linkPerformingPractitionerAndPatient( configurationManager, immunization, patient, resourceList  );
		} else {
			MiscUtils.getLogger().warn( "Requested Immunization id " + preventionId + " was not found.");
		}
		return resourceList;
	}
	
	/**
	 * 
	 * @param configurationManager
	 * @param demographicNo
	 * @return org.hl7.fhir.dstu3.model.Organization
	 */
	public static final org.hl7.fhir.dstu3.model.Organization getPublicHealthUnit( OscarFhirConfigurationManager configurationManager, int demographicNo ) {
		
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		DemographicExt demographicExt = demographicManager.getDemographicExt(configurationManager.getLoggedInInfo(), demographicNo, DemographicExt.DemographicProperty.PHU);
		String phuId = null;
		org.hl7.fhir.dstu3.model.Organization organization = null;
		
		if(demographicExt != null) {
			phuId = demographicExt.getValue();
		}
		
		PublicHealthUnitType publicHealthUnitType = getPublicHealthUnitType( configurationManager, phuId );
		
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
	
	
	/******* PRIVATE HELPER METHODS BELOW THIS LINE *******/
	
	
	/**
	 * Helper method intended for use from inside the class.
	 * 
	 * @param configurationManager
	 * @param immunization
	 * @param patient
	 * @param resourceList
	 * @return HashSet<OscarFhirResource<?,?>>
	 */
	private static final HashSet<AbstractOscarFhirResource<?,?>> linkPerformingPractitionerAndPatient( 
			OscarFhirConfigurationManager configurationManager, 
			org.oscarehr.integration.fhir.model.Immunization<Prevention> immunization, 
			org.oscarehr.integration.fhir.model.Patient patient, HashSet<AbstractOscarFhirResource<?,?>> resourceList) {
		
		String performingProviderNo = immunization.getOscarResource().getProviderNo();
		if(performingProviderNo != null && !"-1".equals(performingProviderNo) ) {
			PerformingPractitioner performingPractitioner = OscarFhirResourceManager.getPerformingPractitionerByProviderNumber( configurationManager, performingProviderNo );
			if( performingPractitioner != null ) {
				immunization.addPerformingPractitioner( performingPractitioner.getReference() );
				resourceList.add( performingPractitioner );
			}	
		} else if (performingProviderNo != null && "-1".equals(performingProviderNo)) {
			Provider provider = new Provider();
			provider.setProviderNo(UUID.randomUUID().toString().substring(0,8));
			PerformingPractitioner performingPractitioner = new org.oscarehr.integration.fhir.model.PerformingPractitioner( provider, configurationManager );
			immunization.addPerformingPractitioner( performingPractitioner.getReference() );
			resourceList.add( performingPractitioner );
		}
		
		immunization.setPatientReference( patient.getReference() );
		resourceList.add( patient );
		resourceList.add( immunization );
		
		return resourceList;
	}
	
	/**
	 * Helper method. Intended for use inside the class.
	 * 
	 * @param configurationManager
	 * @param immunizations
	 * @param patient
	 * @param resourceList
	 * @return HashSet<OscarFhirResource<?,?>>
	 */
	private static final HashSet<AbstractOscarFhirResource<?,?>> linkPerformingPractitionerAndPatient( 
			OscarFhirConfigurationManager configurationManager, 
			List<org.oscarehr.integration.fhir.model.Immunization<Prevention>> immunizations, 
			org.oscarehr.integration.fhir.model.Patient patient, HashSet<AbstractOscarFhirResource<?,?>> resourceList ) {

		if( immunizations != null && ! immunizations.isEmpty() ) {		
			for( Immunization<Prevention> immunization : immunizations ) {				
				linkPerformingPractitionerAndPatient( configurationManager, immunization, patient, resourceList );			
			}
		}
		
		return resourceList;
	}
	
	/**
	 * Helper method. For use inside the class. 
	 * 
	 * @param configurationManager
	 * @param phuId
	 * @return
	 */
	private static final PublicHealthUnitType getPublicHealthUnitType( OscarFhirConfigurationManager configurationManager, String phuId ) {
		
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
