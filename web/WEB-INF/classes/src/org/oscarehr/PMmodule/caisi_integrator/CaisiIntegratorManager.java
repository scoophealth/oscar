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
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.CommunityIssueWs;
import org.oscarehr.caisi_integrator.ws.CommunityIssueWsService;
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
import org.oscarehr.caisi_integrator.ws.IssueTransfer;
import org.oscarehr.caisi_integrator.ws.NoteTransfer;
import org.oscarehr.caisi_integrator.ws.ProgramWs;
import org.oscarehr.caisi_integrator.ws.ProgramWsService;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.caisi_integrator.ws.ProviderWsService;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.caisi_integrator.ws.ReferralWsService;
import org.oscarehr.caisi_integrator.ws.SetConsentTransfer;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.oscarehr.util.FacilityProviderSegmentedTimeClearedHashMap;
import org.oscarehr.util.FacilitySegmentedTimeClearedHashMap;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

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

	private static Logger log = Logger.getLogger(CaisiIntegratorManager.class);
	private static FacilityDao facilityDao=(FacilityDao)SpringUtils.getBean("facilityDao");

	/**
	 * This is a simple cache mechanism which removes objects based on time. All data is segmented via the requesting facility. i.e. this is not data cached on a remote facility basis, it's on a viewing facility basis. As an example for "providers", the
	 * cached data is NOT facility and the list of providers at each facility. It is the viewers facility, followed by a list of providers at all other facilities. i.e. This does mean there is duplicated data as in 2 viewing facilities may cache the same
	 * provider from the third facility; however due to the nature of the segmentation and permissions not all cases are so simple and the "view" of the remote data can be different depending on who is viewing it; therefore, we must cache the view.
	 */
	private static FacilitySegmentedTimeClearedHashMap<Object> facilitySegmentedSimpleTimeCache = new FacilitySegmentedTimeClearedHashMap<Object>(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

	/**
	 * This caching mechanism uses the key=hnrClient.linkingId, value=hnrClient. Note for auditing purposes the cache must be segmented by facility and provider.
	 */
	private static FacilityProviderSegmentedTimeClearedHashMap<org.oscarehr.hnr.ws.Client> hnrClientCache = new FacilityProviderSegmentedTimeClearedHashMap<org.oscarehr.hnr.ws.Client>(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

	/**
	 * This caching mechanism uses the key=demographicId
	 */
	private static FacilityProviderSegmentedTimeClearedHashMap<org.oscarehr.caisi_integrator.ws.GetConsentTransfer> integratorConsentState = new FacilityProviderSegmentedTimeClearedHashMap<org.oscarehr.caisi_integrator.ws.GetConsentTransfer>(
	        DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

	public static boolean isEnableIntegratedReferrals() {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
		return(facility.isIntegratorEnabled() && facility.isEnableIntegratedReferrals());
	}

	public static boolean isEnableHealthNumberRegistry() {
		Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
		return(facility.isIntegratorEnabled() && facility.isEnableHealthNumberRegistry());
	}

	private static Facility getLocalFacility(int facilityId) {
		return (facilityDao.find(facilityId));
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

	public static void flushCachedFacilityInfo() {
		facilitySegmentedSimpleTimeCache.remove(LoggedInInfo.loggedInInfo.get().currentFacility.getId(), "ALL_REMOTE_FACILITIES");
	}

	public static List<CachedFacility> getRemoteFacilities() throws MalformedURLException {
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		@SuppressWarnings("unchecked")
		List<CachedFacility> results = (List<CachedFacility>) facilitySegmentedSimpleTimeCache.get(loggedInInfo.currentFacility.getId(), "ALL_REMOTE_FACILITIES");

		if (results == null) {
			FacilityWs facilityWs = getFacilityWs();
			results = facilityWs.getAllFacility();
			facilitySegmentedSimpleTimeCache.put(loggedInInfo.currentFacility.getId(), "ALL_REMOTE_FACILITIES", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedFacility>(results));
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

	public static List<IssueTransfer> getRemoteIssues(int demographicId) {
		try {
			DemographicWs demographicWs = getDemographicWs();
			List<IssueTransfer> results = demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicId, OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE"));

			return (results);
		} catch (Exception e) // remote issues unavailable for some reason
		{
			log.error("Unable to retrieve remote issues, defaulting to empty list", e);
			return new ArrayList<IssueTransfer>();
		}
	}

	public static CommunityIssueWs getCommunityIssueWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		CommunityIssueWsService service = new CommunityIssueWsService(buildURL(facility, "CommunityIssueService"));
		CommunityIssueWs port = service.getCommunityIssueWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static ArrayList<String> getCommunityIssueCodeList(int facilityId, String type) {
		Facility facility = facilityDao.find(facilityId);
		if (facility.isIntegratorEnabled()) {
			try {
				CommunityIssueWs communityIssueWs = getCommunityIssueWs(facilityId);
				return (ArrayList<String>) communityIssueWs.getCommunityIssueCodeList(type);
			} catch (Exception e) {
				log.error("Unable to retrieve community issue code list", e);
				return null;
			}
		} else {
			return null;
		}
	}

	public static List<NoteTransfer> getRemoteNotes(int demographicId, List<IssueTransfer> remoteIssues) {
		try {
			DemographicWs demographicWs = getDemographicWs();
			List<NoteTransfer> notes = demographicWs.getCommunityNotes(Integer.valueOf(demographicId), OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE"), remoteIssues);
			log.debug("retrieved " + notes.size() + " notes from the integrator" );
			return notes;
		} catch (Exception e) {
			log.error("Unable to retrieve remote issues, defaulting to empty list", e);
			return new ArrayList<NoteTransfer>();
		}

	}

	public static ProgramWs getProgramWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProgramWsService service = new ProgramWsService(buildURL(facility, "ProgramService"));
		ProgramWs port = service.getProgramWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static ArrayList<CachedProgram> getRemotePrograms(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgram> results = (List<CachedProgram>) facilitySegmentedSimpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS");

		if (results == null) {
			ProgramWs programWs = getProgramWs(facilityId);
			results = programWs.getAllPrograms();
			facilitySegmentedSimpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgram>(results));
	}

	/**
	 * @param facilityId the callers facilityId
	 * @param type should not be null
	 * @return a list of cached programs matching the program type
	 */
	public static ArrayList<CachedProgram> getRemotePrograms(int facilityId, String type) throws MalformedURLException {
		ArrayList<CachedProgram> results = new ArrayList<CachedProgram>();

		for (CachedProgram cachedProgram : getRemotePrograms(facilityId)) {
			if (type.equals(cachedProgram.getType())) results.add(cachedProgram);
		}

		return (results);
	}

	public static CachedProgram getRemoteProgram(int facilityId, FacilityIdIntegerCompositePk remoteProgramPk) throws MalformedURLException {
		List<CachedProgram> programs = getRemotePrograms(facilityId);

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

	public static List<CachedProgram> getRemoteProgramsAcceptingReferrals(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgram> results = (List<CachedProgram>) facilitySegmentedSimpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS");
		List<CachedProgram> filteredResults = new ArrayList<CachedProgram>();

		if (results == null) {
			ProgramWs programWs = getProgramWs(facilityId);
			results = programWs.getAllProgramsAllowingIntegratedReferrals();
			for (CachedProgram result : results) {
				if (!result.getType().equals("community")) {
					filteredResults.add(result);
				}
			}
			facilitySegmentedSimpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS", filteredResults);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgram>(results));
	}

	public static ProviderWs getProviderWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProviderWsService service = new ProviderWsService(buildURL(facility, "ProviderService"));
		ProviderWs port = service.getProviderWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static List<CachedProvider> getAllProviders(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProvider> results = (List<CachedProvider>) facilitySegmentedSimpleTimeCache.get(facilityId, "ALL_REMOTE_PROVIDERS");

		if (results == null) {
			ProviderWs providerWs = getProviderWs(facilityId);
			results = providerWs.getAllProviders();
			facilitySegmentedSimpleTimeCache.put(facilityId, "ALL_REMOTE_PROVIDERS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProvider>(results));
	}

	public static CachedProvider getProvider(int facilityId, FacilityIdStringCompositePk remoteProviderPk) throws MalformedURLException {
		List<CachedProvider> providers = getAllProviders(facilityId);

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

	public static ReferralWs getReferralWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ReferralWsService service = new ReferralWsService(buildURL(facility, "ReferralService"));
		ReferralWs port = service.getReferralWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static HnrWs getHnrWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		HnrWsService service = new HnrWsService(buildURL(facility, "HnrService"));
		HnrWs port = service.getHnrWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public static List<MatchingClientScore> searchHnrForMatchingClients(Facility facility, Provider provider, MatchingClientParameters matchingClientParameters) throws MalformedURLException, ConnectException_Exception {
		HnrWs hnrWs = getHnrWs(facility.getId());
		List<MatchingClientScore> potentialMatches = hnrWs.getMatchingHnrClients(matchingClientParameters);

		for (MatchingClientScore temp : potentialMatches)
			hnrClientCache.put(facility.getId(), provider.getProviderNo(), temp.getClient().getLinkingId(), temp.getClient());

		return (potentialMatches);
	}

	public static org.oscarehr.hnr.ws.Client getHnrClient(Integer linkingId) throws MalformedURLException, ConnectException_Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		org.oscarehr.hnr.ws.Client client = hnrClientCache.get(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), linkingId);

		if (client == null) {
			HnrWs hnrWs = getHnrWs(loggedInInfo.currentFacility.getId());
			client = hnrWs.getHnrClient(linkingId);
			if (client != null) hnrClientCache.put(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), linkingId, client);
		}

		return (client);
	}

	public static Integer setHnrClient(org.oscarehr.hnr.ws.Client hnrClient) throws MalformedURLException, DuplicateHinExceptionException, InvalidHinExceptionException, ConnectException_Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		if (hnrClient.getLinkingId() != null) hnrClientCache.remove(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), hnrClient.getLinkingId());

		HnrWs hnrWs = getHnrWs(loggedInInfo.currentFacility.getId());
		return (hnrWs.setHnrClientData(hnrClient));
	}

	public static CachedFacility getCurrentRemoteFacility() throws MalformedURLException {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		CachedFacility cachedFacility = (CachedFacility) facilitySegmentedSimpleTimeCache.get(loggedInInfo.currentFacility.getId(), "MY_REMOTE_FACILITY");

		if (cachedFacility == null) {
			FacilityWs facilityWs = getFacilityWs();
			cachedFacility = facilityWs.getMyFacility();
			if (cachedFacility != null) facilitySegmentedSimpleTimeCache.put(loggedInInfo.currentFacility.getId(), "MY_REMOTE_FACILITY", cachedFacility);
		}

		return (cachedFacility);
	}

	public static GetConsentTransfer getConsentState(Integer localDemographicId) throws MalformedURLException {
		CachedFacility localIntegratorFacility=getCurrentRemoteFacility();
		
		return (getConsentState(localIntegratorFacility.getIntegratorFacilityId(), localDemographicId));
	}

	public static GetConsentTransfer getConsentState(Integer integratorFacilityId, Integer caisiDemographicId) throws MalformedURLException {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(integratorFacilityId);
		pk.setCaisiItemId(caisiDemographicId);
		
		GetConsentTransfer getConsentTransfer = integratorConsentState.get(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), pk);

		if (getConsentTransfer == null) {
			DemographicWs demographicWs = getDemographicWs();
			getConsentTransfer = demographicWs.getConsentState(pk);
			if (getConsentTransfer != null) integratorConsentState.put(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), pk, getConsentTransfer);
		}

		return (getConsentTransfer);
	}

	public static void pushConsent(IntegratorConsent consent) throws MalformedURLException	{
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		if (consent.getClientConsentStatus()==ConsentStatus.GIVEN || consent.getClientConsentStatus()==ConsentStatus.REVOKED)
		{
			SetConsentTransfer consentTransfer=makeSetConsentTransfer(consent);				
			getDemographicWs().setCachedDemographicConsent(consentTransfer);
		}
		
		integratorConsentState.remove(loggedInInfo.currentFacility.getId(), loggedInInfo.loggedInProvider.getProviderNo(), consent.getDemographicId());
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
}
