/*
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.PMmodule.caisi_integrator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.ConnectException_Exception;
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
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.TimeClearedHashMap;

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
 * <br />
 * We will attempt to cache data on a per provider level as well, we will put this cached data in the sessions space. To keep things organised,
 * we will put a TimeClearedHashMap in the session space and with in that hash map we will cache data. 
 */
public class CaisiIntegratorManager {

	private static final String INTEGRATOR_DATA_SESSION_CACHE_KEY="INTEGRATOR_DATA_SESSION_CACHE_KEY";
	private static final long MAX_SESSION_CACHE_DATA_TIME=DateUtils.MILLIS_PER_HOUR;
	
	/** only non-audited data should be cached in here */
	private static Map<String, Object> basicDataCache=Collections.synchronizedMap(new TimeClearedHashMap<String, Object>(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR));
	
	public static boolean isEnableIntegratedReferrals() {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
		return(facility.isIntegratorEnabled() && facility.isEnableIntegratedReferrals());
	}

	private static void addAuthenticationInterceptor(Facility facility, Object wsPort) {
		CxfClientUtils.addWSS4JAuthentication(facility.getIntegratorUser(), facility.getIntegratorPassword(), wsPort);
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

    public static List<CachedFacility> getRemoteFacilities() throws MalformedURLException {
		
    	@SuppressWarnings("unchecked")
		List<CachedFacility> results=(List<CachedFacility>) basicDataCache.get("ALL_FACILITIES");
    	
    	if (results==null)
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

    private static List<CachedProvider> getAllProviders() throws MalformedURLException {
		
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
		consentTransfer.setCreatedDate(consent.getCreatedDate());
		consentTransfer.setDemographicId(consent.getDemographicId());
		consentTransfer.setExcludeMentalHealthData(consent.isExcludeMentalHealthData());
		consentTransfer.setExpiry(consent.getExpiry());

		for (Entry<Integer, Boolean> entry : consent.getConsentToShareData().entrySet()) {
			FacilityConsentPair pair = new FacilityConsentPair();
			pair.setRemoteFacilityId(entry.getKey());
			pair.setShareData(entry.getValue());
			consentTransfer.getConsentToShareData().add(pair);
		}

		return (consentTransfer);
	}

    public static List<CachedDemographicNote> getLinkedNotes(Integer demographicNo) throws MalformedURLException {
		
		String sessionCacheKey="LINKED_NOTES:"+demographicNo;

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> linkedNotes=(List<CachedDemographicNote>) getFromSessionCache(sessionCacheKey);
		
		if (linkedNotes==null)
		{
			DemographicWs demographicWs = getDemographicWs();
			linkedNotes = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicNotes(demographicNo));
			putInSessionCache(sessionCacheKey, linkedNotes);
		}
		
		return (linkedNotes);
	}
	
	public static void clearSessionCache()
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo!=null && loggedInInfo.session!=null) loggedInInfo.session.removeAttribute(INTEGRATOR_DATA_SESSION_CACHE_KEY);
	}
	
	/**
	 * This method should get the existin session cache or create one of none exists. It will return null if this is 
	 * run from a non-session thread.
	 */
	private static TimeClearedHashMap<String, Object> getSessionCache()
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo!=null && loggedInInfo.session!=null)
		{
			@SuppressWarnings("unchecked")
			TimeClearedHashMap<String, Object> sessionCache=(TimeClearedHashMap<String, Object>) loggedInInfo.session.getAttribute(INTEGRATOR_DATA_SESSION_CACHE_KEY);
			
			if (sessionCache==null)
			{
				sessionCache=new TimeClearedHashMap<String, Object>(MAX_SESSION_CACHE_DATA_TIME, MAX_SESSION_CACHE_DATA_TIME);
				loggedInInfo.session.setAttribute(INTEGRATOR_DATA_SESSION_CACHE_KEY, sessionCache);
			}
			
			return(sessionCache);
		}
		
		return(null);
	}
	
    private static void putInSessionCache(String key, Object value)
	{
		TimeClearedHashMap<String, Object> sessionCache=getSessionCache();
		
		if (sessionCache!=null) sessionCache.put(key, value);
	}

    private static Object getFromSessionCache(String key)
	{
		TimeClearedHashMap<String, Object> sessionCache=getSessionCache();
		
		if (sessionCache!=null) return(sessionCache.get(key));
		
		return(null);
	}
}
