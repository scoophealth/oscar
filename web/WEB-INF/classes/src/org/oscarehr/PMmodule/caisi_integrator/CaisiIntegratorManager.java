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

/**
 */

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
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdProviderIdCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.ProgramInfoWs;
import org.oscarehr.caisi_integrator.ws.client.ProgramInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.ProviderInfoWs;
import org.oscarehr.caisi_integrator.ws.client.ProviderInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.ReferralWs;
import org.oscarehr.caisi_integrator.ws.client.ReferralWsService;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.FacilitySegmentedTimeClearedHashMap;

public class CaisiIntegratorManager {

	private FacilityDao facilityDao;

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	/**
	 * This is a simple cache mechanism which removes objects based on time.
	 */
	private static FacilitySegmentedTimeClearedHashMap simpleTimeCache = new FacilitySegmentedTimeClearedHashMap(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

	public boolean isIntegratorEnabled(int facilityId) {
		Facility facility = getLocalFacility(facilityId);
		if (facility != null && facility.isIntegratorEnabled() == true)
			return (true);
		else
			return (false);
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

	public FacilityInfoWs getFacilityInfoWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		FacilityInfoWsService service = new FacilityInfoWsService(buildURL(facility, "FacilityInfoService"));
		FacilityInfoWs port = service.getFacilityInfoWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedFacility> getRemoteFacilities(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedFacility> results = (List<CachedFacility>) simpleTimeCache.get(facilityId, "ALL_REMOTE_FACILITIES");

		if (results == null) {
			FacilityInfoWs facilityInfoWs = getFacilityInfoWs(facilityId);
			results = facilityInfoWs.getAllFacilityInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_FACILITIES", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedFacility>(results));
	}

	public CachedFacility getRemoteFacility(int myFacilityId, int remoteFacilityId) throws MalformedURLException
	{
		for (CachedFacility facility : getRemoteFacilities(myFacilityId))
		{
			if (facility.getIntegratorFacilityId().equals(remoteFacilityId)) return(facility);
		}
		
		return(null);
	}
	
	public DemographicInfoWs getDemographicInfoWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		DemographicInfoWsService service = new DemographicInfoWsService(buildURL(facility, "DemographicInfoService"));
		DemographicInfoWs port = service.getDemographicInfoWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public ProgramInfoWs getProgramInfoWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProgramInfoWsService service = new ProgramInfoWsService(buildURL(facility, "ProgramInfoService"));
		ProgramInfoWs port = service.getProgramInfoWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedProgram> getRemotePrograms(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgram> results = (List<CachedProgram>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS");

		if (results == null) {
			ProgramInfoWs programInfoWs = getProgramInfoWs(facilityId);
			results = programInfoWs.getAllProgramInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgram>(results));
	}

	public CachedProgram getRemoteProgram(int facilityId, FacilityIdIntegerCompositePk remoteProgramPk) throws MalformedURLException {
		List<CachedProgram> programs = getRemotePrograms(facilityId);
		
		for (CachedProgram cachedProgram : programs) {
			if (facilityProgramPrimaryKeyEquals(cachedProgram.getFacilityIdIntegerCompositePk(), remoteProgramPk)) {
				return (cachedProgram);
			}
		}

		return (null);
	}

    private static boolean facilityProgramPrimaryKeyEquals(FacilityIdIntegerCompositePk o1, FacilityIdIntegerCompositePk o2)
	{
		try
        {
	        return(o1.getIntegratorFacilityId().equals(o2.getIntegratorFacilityId()) && o1.getCaisiItemId().equals(o2.getCaisiItemId()));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

	
	public List<CachedProgram> getRemoteProgramsAcceptingReferrals(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgram> results = (List<CachedProgram>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS");

		if (results == null) {
			ProgramInfoWs programInfoWs = getProgramInfoWs(facilityId);
			results = programInfoWs.getAllProgramInfoAllowingIntegratedReferrals();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgram>(results));
	}

	public ProviderInfoWs getProviderInfoWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProviderInfoWsService service = new ProviderInfoWsService(buildURL(facility, "ProviderInfoService"));
		ProviderInfoWs port = service.getProviderInfoWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedProvider> getAllProviderInfos(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProvider> results = (List<CachedProvider>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROVIDERS");

		if (results == null) {
			ProviderInfoWs providerInfoWs = getProviderInfoWs(facilityId);
			results = providerInfoWs.getAllProviderInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROVIDERS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProvider>(results));
	}

	public CachedProvider getProviderInfo(int facilityId, FacilityIdProviderIdCompositePk remoteProviderPk) throws MalformedURLException
	{
		List<CachedProvider> providers=getAllProviderInfos(facilityId);
		
		for (CachedProvider cachedProvider : providers) {
			if (facilityProviderPrimaryKeyEquals(cachedProvider.getFacilityIdProviderIdCompositePk(), remoteProviderPk)) {
				return (cachedProvider);
			}
		}

		return (null);
	}
	
    private static boolean facilityProviderPrimaryKeyEquals(FacilityIdProviderIdCompositePk o1, FacilityIdProviderIdCompositePk o2)
	{
		try
        {
	        return(o1.getIntegratorFacilityId().equals(o2.getIntegratorFacilityId()) && o1.getCaisiProviderId().equals(o2.getCaisiProviderId()));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

	public ReferralWs getReferralWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ReferralWsService service = new ReferralWsService(buildURL(facility, "ReferralService"));
		ReferralWs port = service.getReferralWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

}
