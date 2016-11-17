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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.ConsentDao;
import org.oscarehr.common.dao.ConsentTypeDao;
import org.oscarehr.common.model.Consent;
import org.oscarehr.common.model.ConsentType;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

/**
 * Manages the various consents required from patients for participation in specific programs
 * or to share health information with other providers.
 *
 */
@Service
public class PatientConsentManager {

	@Autowired
	private ConsentDao consentDao;
	
	@Autowired
	private ConsentTypeDao consentTypeDao;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;

	public PatientConsentManager () {
		// default constructor.
	}
	
	/**
	 * Set a patient consent based on the demographic, consent type and boolean consent.
	 * The "consented" parameter will not accept NULL.
	 * 
	 * True = patient fully consents
	 * False = patient has opted out (revoked)
	 * 
	 * This method first assumes that a Consent entry already exists for the given patient.
	 * If a Consent exists: then the Consent will be set according to the given boolean parameter.
	 * If a Consent does not exist: and the consented parameter is true - a new "consented" Consent will be added - a new 
	 * consent object will not be added if consented parameter is false.
	 * 
	 * This method sets the boolean "explicit" ( patient gave direct consent = true; patient consent was implied or assumed = false) 
	 * to a default TRUE.
	 */
	public void setConsent( LoggedInInfo loggedinInfo, int demographic_no, int consentTypeId, boolean consented ) {
		if( consented ) {
			addConsent(loggedinInfo, demographic_no, consentTypeId, consented);
		} else {
			optoutConsent( loggedinInfo, demographic_no, consentTypeId );
		}
	}

	/**
	 * Add a new Consent from a consenting patient by consentType id. 
	 * The given consent type id is cross checked in the available consentTypes.
	 * This method first assumes that a Consent entry already exists for the given patient and 
	 * then updates it as required.
	 * 
	 * The default state for Consent is FALSE. This means that a patient is assume non-consenting until
	 * a Consent is set. Therefore there is no need to set this if a patient has not expressed consent.
	 * 
	 * This method sets the boolean "explicit" ( patient gave direct consent = true; patient consent was implied or assumed = false) 
	 * to a default TRUE.
	 */
	public void addConsent( LoggedInInfo loggedinInfo, int demographic_no, int consentTypeId ) {
		addConsent( loggedinInfo, demographic_no, consentTypeId, Boolean.TRUE );
	}
	
	/**
	 * Add a new Consent from a consenting patient by consentType id. 
	 * The given consent type id is cross checked in the available consentTypes.
	 * The default state for Consent is FALSE. This means that a patient is assume non-consenting until
	 * a Consent is set. 
	 * 
	 * The extra parameter: explicit can be used to determine if this consent was implied (explicit=false) by the patient
	 * or if the consent was explicit (explicit=true).  In most cases the patient will always be required to give 
	 * a verbal or written explicit consent.  
	 * 
	 * The Explicit parameter will not accept a null value.
	 * EXPLICIT CONSENT: patient gave direct consent. explicit = true; 
	 * IMPLIED CONSENT: patient consent was implied or assumed. explicit = false 
	 */
	public void addConsent( LoggedInInfo loggedinInfo, int demographic_no, int consentTypeId, boolean explicit ) {
		
		if ( ! securityInfoManager.hasPrivilege(loggedinInfo, "_demographic", SecurityInfoManager.WRITE, demographic_no) ) {
			throw new RuntimeException("Unauthorised Access. Object[_demographic]");
		}
		
		LogAction.addLogSynchronous(loggedinInfo, "PatientConsentManager.addConsent", " Demographic: " + demographic_no);
		
		ConsentType consentType = getConsentTypeByConsentTypeId( consentTypeId );
		Consent consent = getConsentByDemographicAndConsentType( loggedinInfo, demographic_no, consentType );
		
		if( consent == null ) {
			consent = new Consent();
			consent.setConsentDate( new Date() );
		}
		
		if( consentType != null ) {						
			consent.setDemographicNo( demographic_no );
			consent.setConsentType( consentType );
			consent.setExplicit( explicit );
			consent.setOptout( Boolean.FALSE );
			consent.setLastEnteredBy( loggedinInfo.getLoggedInProviderNo() );
			consent.setOptoutDate( null );			
		}
		
		if( consent != null ) {
			consent.setEditDate( new Date( System.currentTimeMillis() ) );
		}
		
		if( consent.getId() == null ) {
			consentDao.persist(consent);
		} else if( consent.getId() > 0 ) {
			consentDao.merge(consent);
		}
	
	}
	
	/**
	 * Used for removing consent from a patient Consent that was previously consented.
	 * Ignored if the patient has never consented. 
	 * The normal state for consent is FALSE
	 */
	public void optoutConsent( LoggedInInfo loggedinInfo, int demographic_no, int consentTypeId ) {
			
		ConsentType consentType = getConsentTypeByConsentTypeId( consentTypeId );
		
		// use this manager method in order to reduce repetitive use of the security check. 
		Consent consent = getConsentByDemographicAndConsentType( loggedinInfo, demographic_no, consentType );

		if( consent != null ) {
			
			optoutConsent( loggedinInfo, consent );
		
			LogAction.addLogSynchronous(loggedinInfo, "PatientConsentManager.optoutConsent[demographic_no, consentID]", " Changing to Opt Out for Consent ConsentTypeId: " 
					+ consentTypeId + " Demographic: " + demographic_no);
		} 
		
	}
	
	/**
	 * Used for removing consent from a patient that previously consented.
	 */
	public void optoutConsent( LoggedInInfo loggedinInfo, Consent consent ) {
		
		if( consent == null ) {
			return;
		}
		
		optoutConsent( loggedinInfo, consent.getId() );
		
	}

	/**
	 * Used for removing consent from a patient that previously consented. For a Consent object.
	 */
	public void optoutConsent( LoggedInInfo loggedinInfo, int consentId ) {
		
		if ( ! securityInfoManager.hasPrivilege(loggedinInfo, "_demographic", SecurityInfoManager.WRITE, null) ) {
			throw new RuntimeException("Unauthorised Access. Object[_demographic]");
		}
		
		LogAction.addLogSynchronous(loggedinInfo, "PatientConsentManager.optoutConsent[consentID]", " ConsentId: " + consentId);
		
		Consent consent = consentDao.find( consentId );
	
		if( consent != null ) {
			Date date = new Date( System.currentTimeMillis() );
			consent.setOptout(Boolean.TRUE);
			consent.setOptoutDate( date );
			consent.setEditDate( date );
			consentDao.merge(consent);
		}
		
	}
	
	/**
	 * Returns a list of all the patient consent types currently active. 
	 */
	public List<ConsentType> getConsentTypes() {
			
		List<ConsentType> consentTypeList = new ArrayList<ConsentType>();
		int count = consentTypeDao.getCountAll();
		if ( count > 0 ) {
			consentTypeList = consentTypeDao.findAll(0, count);
		}

		return consentTypeList;
	}
	
	/**
	 * Returns a consent type by the consent type id. 
	 * This can be used to determine the consent program for a consent type id.
	 */
	public ConsentType getConsentTypeByConsentTypeId( int consentTypeId ) {		
		return consentTypeDao.find( consentTypeId );
	}
	
	/**
	 * Returns a ConsentType by the consent program type. Used to get the id of a ConsentType 
	 * by its program name. 
	 * 
	 */
	public ConsentType getConsentType( String type ) {		
		return consentTypeDao.findConsentType( type );
	}

	/**
	 * Returns a list of patient consents given by a specified patient for a specific ConsentType program.
	 * Find the ConsentType object first with getConsentType( String type )
	 */
	public Consent getConsentByDemographicAndConsentType( LoggedInInfo loggedinInfo, int demographic_no, ConsentType consentType ) {
		if ( ! securityInfoManager.hasPrivilege(loggedinInfo, "_demographic", SecurityInfoManager.READ, demographic_no) ) {
			throw new RuntimeException("Unauthorised Access. Object[_demographic]");
		}
		
		if( consentType == null ) {
			return null;
		}
		
		LogAction.addLogSynchronous( loggedinInfo, "PatientConsentManager.getConsentByDemographicAndConsentType",
				" Demographic: " + demographic_no + " ConsentTypeId: " + consentType.getId());
		
		return consentDao.findByDemographicAndConsentTypeId( demographic_no, consentType.getId() );
	}
	
	/**
	 * Returns a list of all the consentTypes/programs this patient has consented.
	 */
	public List<Consent> getAllConsentsByDemographic( LoggedInInfo loggedinInfo, int demographic_no ) {
		
		if ( ! securityInfoManager.hasPrivilege(loggedinInfo, "_demographic", SecurityInfoManager.READ, demographic_no) ) {
			throw new RuntimeException("Unauthorised Access. Object[_demographic]");
		}
		
		List<Consent> consent = consentDao.findByDemographic(demographic_no);
		
		LogAction.addLogSynchronous( loggedinInfo, "PatientConsentManager.getAllConsentsByDemographic",
				" Demographic: " + demographic_no );
				
		return consent;		
	}
	
	/**
	 * A boolean determination for if the patient has consented to the given ConsentType/program. 
	 * Find the ConsentType object first with getConsentType( String type )
	 */
	public Boolean hasPatientConsented( int demographic_no, ConsentType consentType ) {
		
		if( consentType == null ) {
			return Boolean.FALSE;
		}

		Consent consent = consentDao.findByDemographicAndConsentTypeId( demographic_no, consentType.getId() );

		if( consent == null ) {
			return Boolean.FALSE;
		}
		
		return consent.getPatientConsented();
	}
	
	/**
	 * Get all consents by the type indicated that were edited after the date given.
	 */
	public List<Consent> getConsentsByTypeAndEditDate( LoggedInInfo loggedinInfo, ConsentType consentType, Date editedAfter ) {

		// TODO Integrator uses this method. Not sure how to handle situations when Integrator is using the -1 system id.
		// At current none of these entries reveal any private patient information.
//		if ( ! securityInfoManager.hasPrivilege(loggedinInfo, "_demographic", SecurityInfoManager.READ, null) ) {
//			return null;
//		}
		
		List<Consent> consentList = consentDao.findLastEditedByConsentTypeId( consentType.getId(), editedAfter );
		
		LogAction.addLogSynchronous( loggedinInfo, "PatientConsentManager.getConsentsByTypeAndEditDate",
				" Demographic: " + consentType );
		
		return consentList;
	}
	
}
