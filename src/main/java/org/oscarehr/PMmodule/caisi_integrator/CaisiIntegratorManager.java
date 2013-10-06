/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.caisi_integrator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedMeasurement;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.ConnectException_Exception;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.DemographicWsService;
import org.oscarehr.caisi_integrator.ws.DuplicateHinExceptionException;
import org.oscarehr.caisi_integrator.ws.FacilityConsentPair;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.FacilityWsService;
import org.oscarehr.caisi_integrator.ws.GetConsentTransfer;
import org.oscarehr.caisi_integrator.ws.HnrWs;
import org.oscarehr.caisi_integrator.ws.HnrWsService;
import org.oscarehr.caisi_integrator.ws.InvalidHinExceptionException;
import org.oscarehr.caisi_integrator.ws.ProgramWs;
import org.oscarehr.caisi_integrator.ws.ProgramWsService;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.caisi_integrator.ws.ProviderWsService;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.caisi_integrator.ws.ReferralWsService;
import org.oscarehr.caisi_integrator.ws.SetConsentTransfer;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.oscarehr.util.CxfClientUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;
import org.oscarehr.util.SessionConstants;

/**
 * This class is a manager for integration related functionality. <br />
 * <br />
 * Disregarding the current code base which may not properly conform to the standards... <br />
 * <br />
 * All privacy related data access should be logged (read and write). As a result, if data is cached locally, the cached read must also be logged locally. If we currently can not log the access locally for what ever reason (such as current schema is a mess
 * and we don't have a good logging facility), then the data should not be cached and individual requests should be made to the service provider. This presumes the service provider conforms to access logging standards and therefore you push the logging
 * responsibility to the other application since we are currently unable to provide that locally. When and if a good local logging facility is made available, local caching can then occur. <br />
 * <br />
 * Note that not all data is privacy related, examples include a Facility and it's information is not covered under the regulations for privacy of an individual. Note also that for some reason we're only concerned about client privacy, providers seem to
 * not be covered and or have no expectation of privacy (although we could be wrong in this interpretation).
 */
public class CaisiIntegratorManager {

	/** only non-audited data should be cached in here */
	private static QueueCache<String, Object> basicDataCache=new QueueCache<String, Object>(4, 100, DateUtils.MILLIS_PER_HOUR);
	
	/** data put here should be segmented by the requesting provider as part of the cache key */
	private static QueueCache<String, Object> segmentedDataCache=new QueueCache<String, Object>(4, 100, DateUtils.MILLIS_PER_HOUR);
	
	public static void setIntegratorOffline(boolean status){
		HttpSession session = LoggedInInfo.loggedInInfo.get().session;
		if(status){
		session.setAttribute(SessionConstants.INTEGRATOR_OFFLINE,true);
		}else{
			session.removeAttribute(SessionConstants.INTEGRATOR_OFFLINE);
		}
	}
	
	public static void checkForConnectionError(Throwable exception){
		Throwable rootException = ExceptionUtils.getRootCause(exception);
		MiscUtils.getLogger().debug("Exception: "+exception.getClass().getName()+" --- "+rootException.getClass().getName());
				
		if (rootException instanceof java.net.ConnectException){
			setIntegratorOffline(true);
		}
	}
	
	public static boolean isIntegratorOffline(){
		HttpSession session = LoggedInInfo.loggedInInfo.get().session;
		Object object = session.getAttribute(SessionConstants.INTEGRATOR_OFFLINE);
		if (object != null){
			return true;
		}
		return false;
	}
	
	public static boolean isEnableIntegratedReferrals() {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
		return(facility.isIntegratorEnabled() && facility.isEnableIntegratedReferrals());
	}

	private static void addAuthenticationInterceptor(Facility facility, Object wsPort) {
		Client cxfClient = ClientProxy.getClient(wsPort);
		cxfClient.getOutInterceptors().add(new AuthenticationOutWSS4JInterceptorForIntegrator(facility.getIntegratorUser(), facility.getIntegratorPassword()));
	}

	private static URL buildURL(Facility facility, String servicePoint) throws MalformedURLException {
		return (new URL(facility.getIntegratorUrl() + '/' + servicePoint + "?wsdl"));
	}

	public static FacilityWs getFacilityWs() throws MalformedURLException {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;

		FacilityWsService service = new FacilityWsService(buildURL(facility, "FacilityService"));
		FacilityWs port = service.getFacilityWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	

	public static boolean haveAllRemoteFacilitiesSyncedIn(int seconds) throws MalformedURLException {
		return haveAllRemoteFacilitiesSyncedIn(seconds,true);
	}
    		
	public static boolean haveAllRemoteFacilitiesSyncedIn(int seconds,boolean useCachedData) throws MalformedURLException {
		boolean synced = true;
		Calendar timeConsideredStale = Calendar.getInstance();
		
		timeConsideredStale.add(Calendar.SECOND, -seconds);
		
		List<CachedFacility> remoteFacilities=  getRemoteFacilities(useCachedData);
		for(CachedFacility remoteFacility : remoteFacilities){
			Calendar lastDataUpdate = remoteFacility.getLastDataUpdate();
			if( lastDataUpdate == null || timeConsideredStale.after(lastDataUpdate)){
				return false;
			}
		}
		return synced;
	}

	public static List<CachedFacility> getRemoteFacilities() throws MalformedURLException {
		return getRemoteFacilities(true);
	}
	
    public static List<CachedFacility> getRemoteFacilities(boolean useCachedData) throws MalformedURLException {
    	
		@SuppressWarnings("unchecked")
		List<CachedFacility> results=(List<CachedFacility>) basicDataCache.get("ALL_FACILITIES");
    	    	
    	if (!useCachedData  || results==null)
    	{
			FacilityWs facilityWs = getFacilityWs();
			results = Collections.unmodifiableList(facilityWs.getAllFacility());
			basicDataCache.put("ALL_FACILITIES", results);
    	}
    	
		return (results);
	}	
    
	
   public static CachedFacility getRemoteFacility(int remoteFacilityId) throws MalformedURLException {
		for (CachedFacility facility : getRemoteFacilities()) {
			if (facility.getIntegratorFacilityId().equals(remoteFacilityId)) return (facility);
		}

		return (null);
	}

	public static DemographicWs getDemographicWs() throws MalformedURLException {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
		
		DemographicWsService service = new DemographicWsService(buildURL(facility, "DemographicService"));
		DemographicWs port = service.getDemographicWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static ProgramWs getProgramWs() throws MalformedURLException {
		return(getProgramWs(LoggedInInfo.loggedInInfo.get().currentFacility));
	}

	public static ProgramWs getProgramWs(Facility facility) throws MalformedURLException {
		ProgramWsService service = new ProgramWsService(buildURL(facility, "ProgramService"));
		ProgramWs port = service.getProgramWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

    public static List<CachedProgram> getAllPrograms() throws MalformedURLException
	{
    	@SuppressWarnings("unchecked")
		List<CachedProgram> allPrograms=(List<CachedProgram>) basicDataCache.get("ALL_PROGRAMS");
		
		if (allPrograms==null)
		{
			allPrograms=Collections.unmodifiableList(getProgramWs().getAllPrograms());
			basicDataCache.put("ALL_PROGRAMS", allPrograms);
		}
		
		return(allPrograms);			
	}
	
	/**
	 * @param type should not be null
	 * @return a list of cached programs matching the program type
	 */
	public static ArrayList<CachedProgram> getRemotePrograms(String type) throws MalformedURLException {
		ArrayList<CachedProgram> results = new ArrayList<CachedProgram>();

		for (CachedProgram cachedProgram : getAllPrograms()) {
			if (type.equals(cachedProgram.getType())) results.add(cachedProgram);
		}

		return (results);
	}

	public static CachedProgram getRemoteProgram(FacilityIdIntegerCompositePk remoteProgramPk) throws MalformedURLException {
		List<CachedProgram> programs = getAllPrograms();

		for (CachedProgram cachedProgram : programs) {
			if (facilityIdIntegerPkEquals(cachedProgram.getFacilityIdIntegerCompositePk(), remoteProgramPk)) {
				return (cachedProgram);
			}
		}

		return (null);
	}

	private static boolean facilityIdIntegerPkEquals(FacilityIdIntegerCompositePk o1, FacilityIdIntegerCompositePk o2) {
		try {
			return (o1.getIntegratorFacilityId().equals(o2.getIntegratorFacilityId()) && o1.getCaisiItemId().equals(o2.getCaisiItemId()));
		} catch (RuntimeException e) {
			return (false);
		}
	}

	public static List<CachedProgram> getRemoteProgramsAcceptingReferrals() throws MalformedURLException {
		List<CachedProgram> filteredResults = new ArrayList<CachedProgram>();

		ProgramWs programWs = getProgramWs();
		List<CachedProgram> results = programWs.getAllProgramsAllowingIntegratedReferrals();
		for (CachedProgram result : results) {
			if (!result.getType().equals("community")) {
				filteredResults.add(result);
			}
		}

		return (results);
	}

	public static ProviderWs getProviderWs() throws MalformedURLException {
		return (getProviderWs(LoggedInInfo.loggedInInfo.get().currentFacility));
	}

	public static ProviderWs getProviderWs(Facility facility) throws MalformedURLException {
		ProviderWsService service = new ProviderWsService(buildURL(facility, "ProviderService"));
		ProviderWs port = service.getProviderWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

    public static List<CachedProvider> getAllProviders() throws MalformedURLException {
		
    	@SuppressWarnings("unchecked")
		List<CachedProvider> results=(List<CachedProvider>) basicDataCache.get("ALL_PROVIDERS");

    	if (results==null)
    	{
			ProviderWs providerWs = getProviderWs();
			results = Collections.unmodifiableList(providerWs.getAllProviders());
			basicDataCache.put("ALL_PROVIDERS", results);
    	}
    	
		return (results);
	}

	public static CachedProvider getProvider(FacilityIdStringCompositePk remoteProviderPk) throws MalformedURLException {
		List<CachedProvider> providers = getAllProviders();

		for (CachedProvider cachedProvider : providers) {
			if (facilityProviderPrimaryKeyEquals(cachedProvider.getFacilityIdStringCompositePk(), remoteProviderPk)) {
				return (cachedProvider);
			}
		}

		return (null);
	}

	private static boolean facilityProviderPrimaryKeyEquals(FacilityIdStringCompositePk o1, FacilityIdStringCompositePk o2) {
		try {
			return (o1.getIntegratorFacilityId().equals(o2.getIntegratorFacilityId()) && o1.getCaisiItemId().equals(o2.getCaisiItemId()));
		} catch (RuntimeException e) {
			return (false);
		}
	}

	public static ReferralWs getReferralWs() throws MalformedURLException {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;

		ReferralWsService service = new ReferralWsService(buildURL(facility, "ReferralService"));
		ReferralWs port = service.getReferralWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static HnrWs getHnrWs() throws MalformedURLException {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;

		HnrWsService service = new HnrWsService(buildURL(facility, "HnrService"));
		HnrWs port = service.getHnrWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static List<MatchingClientScore> searchHnrForMatchingClients(MatchingClientParameters matchingClientParameters) throws MalformedURLException, ConnectException_Exception {
		HnrWs hnrWs = getHnrWs();
		List<MatchingClientScore> potentialMatches = hnrWs.getMatchingHnrClients(matchingClientParameters);
		return (potentialMatches);
	}

	public static org.oscarehr.hnr.ws.Client getHnrClient(Integer linkingId) throws MalformedURLException, ConnectException_Exception {
		HnrWs hnrWs = getHnrWs();
		org.oscarehr.hnr.ws.Client client = hnrWs.getHnrClient(linkingId);
		return (client);
	}

	public static Integer setHnrClient(org.oscarehr.hnr.ws.Client hnrClient) throws MalformedURLException, DuplicateHinExceptionException, InvalidHinExceptionException, ConnectException_Exception {
		HnrWs hnrWs = getHnrWs();
		return (hnrWs.setHnrClientData(hnrClient));
	}

	/**
	 * You can not determine which facility "you are" from the facility listing. You must use this method to determine which facility you authenticated as.
	 */
	public static CachedFacility getCurrentRemoteFacility() throws MalformedURLException {
		FacilityWs facilityWs = getFacilityWs();
		CachedFacility cachedFacility = facilityWs.getMyFacility();
		return (cachedFacility);
	}

	public static GetConsentTransfer getConsentState(Integer localDemographicId) throws MalformedURLException {
		CachedFacility localIntegratorFacility=getCurrentRemoteFacility();
		
		return (getConsentState(localIntegratorFacility.getIntegratorFacilityId(), localDemographicId));
	}

	public static GetConsentTransfer getConsentState(Integer integratorFacilityId, Integer caisiDemographicId) throws MalformedURLException {		
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(integratorFacilityId);
		pk.setCaisiItemId(caisiDemographicId);
		
		DemographicWs demographicWs = getDemographicWs();
		GetConsentTransfer getConsentTransfer = demographicWs.getConsentState(pk);
		return (getConsentTransfer);
	}

	public static void pushConsent(IntegratorConsent consent) throws MalformedURLException	{
		if (consent.getClientConsentStatus()==ConsentStatus.GIVEN || consent.getClientConsentStatus()==ConsentStatus.REVOKED)
		{
			SetConsentTransfer consentTransfer=makeSetConsentTransfer(consent);				
			getDemographicWs().setCachedDemographicConsent(consentTransfer);
		}
	}

	protected static SetConsentTransfer makeSetConsentTransfer(IntegratorConsent consent) {
		SetConsentTransfer consentTransfer = new SetConsentTransfer();
		consentTransfer.setConsentStatus(consent.getClientConsentStatus().name());
		consentTransfer.setCreatedDate(MiscUtils.toCalendar(consent.getCreatedDate()));
		consentTransfer.setDemographicId(consent.getDemographicId());
		consentTransfer.setExcludeMentalHealthData(consent.isExcludeMentalHealthData());
		consentTransfer.setExpiry(MiscUtils.toCalendar(consent.getExpiry()));

		for (Entry<Integer, Boolean> entry : consent.getConsentToShareData().entrySet()) {
			FacilityConsentPair pair = new FacilityConsentPair();
			pair.setRemoteFacilityId(entry.getKey());
			pair.setShareData(entry.getValue());
			consentTransfer.getConsentToShareData().add(pair);
		}

		return (consentTransfer);
	}

    public static List<CachedDemographicNote> getLinkedNotes(Integer demographicNo) throws MalformedURLException 
    {
 		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String sessionCacheKey="LINKED_NOTES:"+loggedInInfo.currentFacility.getId()+":"+loggedInInfo.loggedInProvider.getProviderNo()+":"+demographicNo;

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> linkedNotes=(List<CachedDemographicNote>) segmentedDataCache.get(sessionCacheKey);
		
		if (linkedNotes==null)
		{
			DemographicWs demographicWs = getDemographicWs();
			linkedNotes = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicNotes(demographicNo));
			segmentedDataCache.put(sessionCacheKey, linkedNotes);
		}
		
		return (linkedNotes);
	}
    
    
  
      public static List<CachedDemographicPrevention> getLinkedPreventions(Integer demographicNo) throws MalformedURLException{
 		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String sessionCacheKey="LINKED_PREVS:"+loggedInInfo.currentFacility.getId()+":"+loggedInInfo.loggedInProvider.getProviderNo()+":"+demographicNo;

		@SuppressWarnings("unchecked")
		List<CachedDemographicPrevention> remotePreventions=(List<CachedDemographicPrevention>) segmentedDataCache.get(sessionCacheKey);
		
		if (remotePreventions==null)
		{
			DemographicWs demographicWs = getDemographicWs();
			remotePreventions = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicPreventionsByDemographicId(demographicNo));
			segmentedDataCache.put(sessionCacheKey, remotePreventions);
		}
		
		return (remotePreventions);
	} 
      
      public static List<CachedMeasurement> getLinkedMeasurements(Integer demographicNo) throws MalformedURLException{
   		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
  		String sessionCacheKey="LINKED_MEASUREMENTS:"+loggedInInfo.currentFacility.getId()+":"+loggedInInfo.loggedInProvider.getProviderNo()+":"+demographicNo;

  		@SuppressWarnings("unchecked")
  		List<CachedMeasurement> remoteMeasurements=(List<CachedMeasurement>) segmentedDataCache.get(sessionCacheKey);
  		
  		if (remoteMeasurements==null)
  		{
  			DemographicWs demographicWs = getDemographicWs();
  			remoteMeasurements = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicMeasurementByDemographicId(demographicNo));
  			segmentedDataCache.put(sessionCacheKey, remoteMeasurements);
  		}
  		
  		return (remoteMeasurements);
  	}   
      
  
    public static List<CachedDemographicNote> getLinkedNotesMetaData(Integer demographicNo) throws MalformedURLException 
    {
 		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String sessionCacheKey="LINKED_NOTES_META:"+loggedInInfo.currentFacility.getId()+":"+loggedInInfo.loggedInProvider.getPractitionerNo()+":"+demographicNo;

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> linkedNotes=(List<CachedDemographicNote>) segmentedDataCache.get(sessionCacheKey);
		
		if (linkedNotes==null)
		{
			DemographicWs demographicWs = getDemographicWs();
			linkedNotes = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicNoteMetaData(demographicNo));
			segmentedDataCache.put(sessionCacheKey, linkedNotes);
		}
		
		return (linkedNotes);
	}
    
    public static List<CachedDemographicNote> getLinkedNotes(List<CachedDemographicNoteCompositePk> ids) throws MalformedURLException 
    {
 		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String sessionCacheKey="LINKED_NOTES_META:"+loggedInInfo.currentFacility.getId()+":"+loggedInInfo.loggedInProvider.getPractitionerNo()+":"+ids;

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> linkedNotes=(List<CachedDemographicNote>) segmentedDataCache.get(sessionCacheKey);
		
		if (linkedNotes==null)
		{
			DemographicWs demographicWs = getDemographicWs();
			linkedNotes = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicNotesByIds(ids));
			segmentedDataCache.put(sessionCacheKey, linkedNotes);
		}
		
		return (linkedNotes);
	}
	    
    /**
     * The purpose of this method is to retrieve a remote demographic record and populate it in a local demographic object. It is ready to be persisted.
     * We do not automatically persist it as there appears to be some code logic where a subsequent approval process may choose not to persist it.
     */
    public static Demographic makeUnpersistedDemographicObjectFromRemoteEntry(int remoteFacilityId, int remoteDemographicId) throws MalformedURLException
    {
		DemographicWs demographicWs = getDemographicWs();
		DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);

		Demographic demographic = new Demographic();
		demographic.setFirstName(demographicTransfer.getFirstName());
		demographic.setLastName(demographicTransfer.getLastName());
		
		if (demographicTransfer.getBirthDate()!=null) demographic.setBirthDay(demographicTransfer.getBirthDate());		
		if (demographicTransfer.getGender()!=null) demographic.setSex(demographicTransfer.getGender().name());

		copyDemographicFieldsIfNotNull(demographicTransfer, demographic);
				
		demographic.setPatientStatus("AC");
		demographic.setDateJoined(new Date());
		demographic.setRosterDate(new Date());
		
		return(demographic);
    }
    
    public static void copyDemographicFieldsIfNotNull(DemographicTransfer demographicTransfer, Demographic demographic)
    {
		if (demographicTransfer.getBirthDate()!=null) demographic.setBirthDay(demographicTransfer.getBirthDate());
		if (demographicTransfer.getCity()!=null) demographic.setCity(demographicTransfer.getCity());
		if (demographicTransfer.getFirstName()!=null) demographic.setFirstName(demographicTransfer.getFirstName());
		if (demographicTransfer.getGender()!=null) demographic.setSex(demographicTransfer.getGender().name());
		if (demographicTransfer.getHin()!=null) demographic.setHin(demographicTransfer.getHin());
		if (demographicTransfer.getHinType()!=null) demographic.setHcType(demographicTransfer.getHinType());
		if (demographicTransfer.getHinVersion()!=null) demographic.setVer(demographicTransfer.getHinVersion());
		if (demographicTransfer.getHinValidStart()!=null) demographic.setEffDate(demographicTransfer.getHinValidStart().getTime());
		if (demographicTransfer.getHinValidEnd()!=null) demographic.setHcRenewDate(demographicTransfer.getHinValidEnd().getTime());
		if (demographicTransfer.getLastName()!=null) demographic.setLastName(demographicTransfer.getLastName());
		if (demographicTransfer.getProvince()!=null) demographic.setProvince(demographicTransfer.getProvince());
		if (demographicTransfer.getSin()!=null) demographic.setSin(demographicTransfer.getSin());
		if (demographicTransfer.getStreetAddress()!=null) demographic.setAddress(demographicTransfer.getStreetAddress());
		if (demographicTransfer.getPhone1()!=null) demographic.setPhone(demographicTransfer.getPhone1());
		if (demographicTransfer.getPhone2()!=null) demographic.setPhone2(demographicTransfer.getPhone2());
    }
}
