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

import org.apache.commons.lang.time.DateUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.caisi_integrator.ws.client.CachedProgram;
import org.oscarehr.caisi_integrator.ws.client.CachedProvider;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.DemographicWsService;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityWsService;
import org.oscarehr.caisi_integrator.ws.client.ProgramWs;
import org.oscarehr.caisi_integrator.ws.client.ProgramWsService;
import org.oscarehr.caisi_integrator.ws.client.ProviderWs;
import org.oscarehr.caisi_integrator.ws.client.ProviderWsService;
import org.oscarehr.caisi_integrator.ws.client.ReferralWs;
import org.oscarehr.caisi_integrator.ws.client.ReferralWsService;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.hnr.ws.client.HnrWs;
import org.oscarehr.hnr.ws.client.HnrWsService;
import org.oscarehr.util.FacilitySegmentedTimeClearedHashMap;

/**
 * This class is a manager for integration related functionality.
 * <br /><br />
 * Disregarding the current code base which may not properly conform to the standards...
 * <br /><br />
 * All privacy related data access should be logged (read and write). As a result, if data is cached locally, the cached read must also be 
 * logged locally. If we currently can not log the access locally for what ever reason (such as current
 * schema is a mess and we don't have a good logging facility), then the data should not be cached and 
 * individual requests should be made to the service provider. This presumes the service provider
 * conforms to access logging standards and therefore you push the logging responsibility to the other
 * application since we are currently unable to provide that locally. When and if a good local logging 
 * facility is made available, local caching can then occur. 
 * <br /><br />
 * Note that not all data is privacy related, examples include a Facility and it's information
 * is not covered under the regulations for privacy of an individual. Note also that for some
 * reason we're only concerned about client privacy, providers seem to not be covered and or have 
 * no expectation of privacy (although we could be wrong in this interpretation). 
 */
public class CaisiIntegratorManager {

	private FacilityDao facilityDao;

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	/**
	 * This is a simple cache mechanism which removes objects based on time.
	 * All data is segmented via the requesting facility. i.e. this is not
	 * data cached on a remote facility basis, it's on a viewing facility basis.
	 * As an example for "providers", the cached data is NOT facility and the list of 
	 * providers at each facility. It is the viewers facility, followed by a list 
	 * of providers at all other facilities. i.e. This does mean there is duplicated
	 * data as in 2 viewing facilities may cache the same provider from the third 
	 * facility; however due to the nature of the segmentation and permissions
	 * not all cases are so simple and the "view" of the remote data can be different
	 * depending on who is viewing it; therefore, we must cache the view. 
	 */
	private static FacilitySegmentedTimeClearedHashMap facilitySegmentedSimpleTimeCache = new FacilitySegmentedTimeClearedHashMap(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

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
		Client cxfClient = ClientProxy.getClient(wsPort);
		cxfClient.getOutInterceptors().add(new AuthenticationOutInterceptor(facility.getIntegratorUser(), facility.getIntegratorPassword()));
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
}
