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

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.CodeType;
import org.oscarehr.caisi_integrator.ws.CommunityIssueWs;
import org.oscarehr.caisi_integrator.ws.CommunityIssueWsService;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.DemographicWsService;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.FacilityWsService;
import org.oscarehr.caisi_integrator.ws.HnrWs;
import org.oscarehr.caisi_integrator.ws.HnrWsService;
import org.oscarehr.caisi_integrator.ws.IssueTransfer;
import org.oscarehr.caisi_integrator.ws.NoteTransfer;
import org.oscarehr.caisi_integrator.ws.ProgramWs;
import org.oscarehr.caisi_integrator.ws.ProgramWsService;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.caisi_integrator.ws.ProviderWsService;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.caisi_integrator.ws.ReferralWsService;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.oscarehr.util.FacilityProviderSegmentedTimeClearedHashMap;
import org.oscarehr.util.FacilitySegmentedTimeClearedHashMap;

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
	private FacilityDao facilityDao;

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

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

	public boolean isIntegratorEnabled(int facilityId) {
		Facility facility = getLocalFacility(facilityId);
		if (facility != null && facility.isIntegratorEnabled() == true) return (true);
		else return (false);
	}

	public boolean isEnableIntegratedReferrals(int facilityId) {
		Facility facility = getLocalFacility(facilityId);
		if (facility != null && facility.isIntegratorEnabled() && facility.isEnableIntegratedReferrals() == true) return (true);
		else return (false);
	}

	public boolean isEnableHealthNumberRegistry(int facilityId) {
		Facility facility = getLocalFacility(facilityId);
		if (facility != null && facility.isIntegratorEnabled() && facility.isEnableHealthNumberRegistry() == true) return (true);
		else return (false);
	}

	private Facility getLocalFacility(int facilityId) {
		return (facilityDao.find(facilityId));
	}

	private void addAuthenticationInterceptor(Facility facility, Object wsPort) {
		CxfClientUtils.addWSS4JAuthentication(facility.getIntegratorUser(), facility.getIntegratorPassword(), wsPort);
	}

	private URL buildURL(Facility facility, String servicePoint) throws MalformedURLException {
		return (new URL(facility.getIntegratorUrl() + '/' + servicePoint + "?wsdl"));
	}

	public FacilityWs getFacilityWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		FacilityWsService service = new FacilityWsService(buildURL(facility, "FacilityService"));
		FacilityWs port = service.getFacilityWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public void flushCachedFacilityInfo(int facilityId) {
		facilitySegmentedSimpleTimeCache.remove(facilityId, "ALL_REMOTE_FACILITIES");
	}

	public List<CachedFacility> getRemoteFacilities(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedFacility> results = (List<CachedFacility>) facilitySegmentedSimpleTimeCache.get(facilityId, "ALL_REMOTE_FACILITIES");

		if (results == null) {
			FacilityWs facilityWs = getFacilityWs(facilityId);
			results = facilityWs.getAllFacility();
			facilitySegmentedSimpleTimeCache.put(facilityId, "ALL_REMOTE_FACILITIES", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedFacility>(results));
	}

	public CachedFacility getRemoteFacility(int myFacilityId, int remoteFacilityId) throws MalformedURLException {
		for (CachedFacility facility : getRemoteFacilities(myFacilityId)) {
			if (facility.getIntegratorFacilityId().equals(remoteFacilityId)) return (facility);
		}

		return (null);
	}

	public DemographicWs getDemographicWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		DemographicWsService service = new DemographicWsService(buildURL(facility, "DemographicService"));
		DemographicWs port = service.getDemographicWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<IssueTransfer> getRemoteIssues(int facilityId, int demographicId) throws MalformedURLException
	{
		try
		{
			DemographicWs demographicWs = getDemographicWs(facilityId);
			List<IssueTransfer> results = (List<IssueTransfer>)demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicId,OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE"));
			
			// this is done for cached lists
			// cloned so alterations don't affect the cached data
			return (new ArrayList<IssueTransfer>(results));
		}
		catch(Exception e) // remote issues unavailable for some reason
		{
			log.error("Unable to retrieve remote issues, defaulting to empty list", e);
			return new ArrayList<IssueTransfer>();
		}
	}
	
	public CommunityIssueWs getCommunityIssueWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		CommunityIssueWsService service = new CommunityIssueWsService(buildURL(facility, "CommunityIssueService"));
		CommunityIssueWs port = service.getCommunityIssueWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public ArrayList<String> getCommunityIssueCodeList(int facilityId, String type) throws MalformedURLException
	{
		Facility facility = facilityDao.find(facilityId);
		if (facility.isIntegratorEnabled())
		{
			try
			{
				CommunityIssueWs communityIssueWs = getCommunityIssueWs(facilityId);
				return (ArrayList<String>)communityIssueWs.getCommunityIssueCodeList(type);
			}
			catch(Exception e)
			{
				log.error("Unable to retrieve community issue code list", e);
				return null;
			}
		}
		else 
		{
			return null;
		}
	}
	
	public List<NoteTransfer> getRemoteNotes(int facilityId, int demographicId, List<IssueTransfer> remoteIssues) throws MalformedURLException
	{
		try
		{
			DemographicWs demographicWs = getDemographicWs(facilityId);
			List<NoteTransfer> notes = (List<NoteTransfer>)demographicWs.getCommunityNotes(Integer.valueOf(demographicId), OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE"), remoteIssues);
			
			return notes;
		}
		catch(Exception e)
		{
			log.error("Unable to retrieve remote issues, defaulting to empty list", e);
			return new ArrayList<NoteTransfer>();
		}
			
	}
	
	public ProgramWs getProgramWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProgramWsService service = new ProgramWsService(buildURL(facility, "ProgramService"));
		ProgramWs port = service.getProgramWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedProgram> getRemotePrograms(int facilityId) throws MalformedURLException {
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

	public CachedProgram getRemoteProgram(int facilityId, FacilityIdIntegerCompositePk remoteProgramPk) throws MalformedURLException {
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

	public List<CachedProgram> getRemoteProgramsAcceptingReferrals(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgram> results = (List<CachedProgram>) facilitySegmentedSimpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS");

		if (results == null) {
			ProgramWs programWs = getProgramWs(facilityId);
			results = programWs.getAllProgramsAllowingIntegratedReferrals();
			facilitySegmentedSimpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgram>(results));
	}

	public ProviderWs getProviderWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProviderWsService service = new ProviderWsService(buildURL(facility, "ProviderService"));
		ProviderWs port = service.getProviderWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedProvider> getAllProviders(int facilityId) throws MalformedURLException {
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

	public CachedProvider getProvider(int facilityId, FacilityIdStringCompositePk remoteProviderPk) throws MalformedURLException {
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

	public ReferralWs getReferralWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ReferralWsService service = new ReferralWsService(buildURL(facility, "ReferralService"));
		ReferralWs port = service.getReferralWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public HnrWs getHnrWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		HnrWsService service = new HnrWsService(buildURL(facility, "HnrService"));
		HnrWs port = service.getHnrWsPort();

		CxfClientUtils.configureClientConnection(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<MatchingClientScore> searchHnrForMatchingClients(Facility facility, Provider provider, MatchingClientParameters matchingClientParameters) throws DatatypeConfigurationException, MalformedURLException {
		HnrWs hnrWs = getHnrWs(facility.getId());
		List<MatchingClientScore> potentialMatches = hnrWs.getMatchingHnrClients(getProviderAuditString(facility, provider), matchingClientParameters);

		for (MatchingClientScore temp : potentialMatches)
			hnrClientCache.put(facility.getId(), provider.getProviderNo(), temp.getClient().getLinkingId(), temp.getClient());

		return (potentialMatches);
	}

	public org.oscarehr.hnr.ws.Client getHnrClient(Facility facility, Provider provider, Integer linkingId) throws MalformedURLException {
		org.oscarehr.hnr.ws.Client client = hnrClientCache.get(facility.getId(), provider.getProviderNo(), linkingId);

		if (client == null) {
			HnrWs hnrWs = getHnrWs(facility.getId());
			client = hnrWs.getHnrClient(getProviderAuditString(facility, provider), linkingId);
			if (client != null) hnrClientCache.put(facility.getId(), provider.getProviderNo(), linkingId, client);
		}

		return (client);
	}

	public Integer setHnrClient(Facility facility, Provider provider, org.oscarehr.hnr.ws.Client hnrClient) throws MalformedURLException {
		if (hnrClient.getLinkingId()!=null) hnrClientCache.remove(facility.getId(), provider.getProviderNo(), hnrClient.getLinkingId());

		HnrWs hnrWs = getHnrWs(facility.getId());
		return(hnrWs.setHnrClientData(getProviderAuditString(facility, provider), hnrClient));
	}

	private static String getProviderAuditString(Facility facility, Provider provider) {
		return "facility=" + facility.getName() + ", provider=" + provider.getFormattedName();
	}
}
