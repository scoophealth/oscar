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
import org.oscarehr.caisi_integrator.ws.client.CachedFacilityInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedProgramInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedProviderInfo;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.FacilityProgramPrimaryKey;
import org.oscarehr.caisi_integrator.ws.client.FacilityProviderPrimaryKey;
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

	public List<CachedFacilityInfo> getRemoteFacilities(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedFacilityInfo> results = (List<CachedFacilityInfo>) simpleTimeCache.get(facilityId, "ALL_REMOTE_FACILITIES");

		if (results == null) {
			FacilityInfoWs facilityInfoWs = getFacilityInfoWs(facilityId);
			results = facilityInfoWs.getAllFacilityInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_FACILITIES", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedFacilityInfo>(results));
	}

	public CachedFacilityInfo getRemoteFacility(int myFacilityId, int remoteFacilityId) throws MalformedURLException
	{
		for (CachedFacilityInfo facility : getRemoteFacilities(myFacilityId))
		{
			if (facility.getFacilityId().equals(remoteFacilityId)) return(facility);
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

	public List<CachedProgramInfo> getRemotePrograms(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgramInfo> results = (List<CachedProgramInfo>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS");

		if (results == null) {
			ProgramInfoWs programInfoWs = getProgramInfoWs(facilityId);
			results = programInfoWs.getAllProgramInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgramInfo>(results));
	}

	public CachedProgramInfo getRemoteProgram(int facilityId, FacilityProgramPrimaryKey remoteProgramPk) throws MalformedURLException {
		List<CachedProgramInfo> programs = getRemotePrograms(facilityId);
		
		for (CachedProgramInfo cachedProgramInfo : programs) {
			if (facilityProgramPrimaryKeyEquals(cachedProgramInfo.getFacilityProgramPrimaryKey(), remoteProgramPk)) {
				return (cachedProgramInfo);
			}
		}

		return (null);
	}

    private static boolean facilityProgramPrimaryKeyEquals(FacilityProgramPrimaryKey o1, FacilityProgramPrimaryKey o2)
	{
		try
        {
	        return(o1.getFacilityId().equals(o2.getFacilityId()) && o1.getFacilityProgramId().equals(o2.getFacilityProgramId()));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

	
	public List<CachedProgramInfo> getRemoteProgramsAcceptingReferrals(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProgramInfo> results = (List<CachedProgramInfo>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS");

		if (results == null) {
			ProgramInfoWs programInfoWs = getProgramInfoWs(facilityId);
			results = programInfoWs.getAllProgramInfoAllowingIntegratedReferrals();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROGRAMS_ACCEPTING_REFERRALS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProgramInfo>(results));
	}

	public ProviderInfoWs getProviderInfoWs(int facilityId) throws MalformedURLException {
		Facility facility = getLocalFacility(facilityId);

		ProviderInfoWsService service = new ProviderInfoWsService(buildURL(facility, "ProviderInfoService"));
		ProviderInfoWs port = service.getProviderInfoWsPort();

		SslUtils.configureSsl(port);
		addAuthenticationInterceptor(facility, port);

		return (port);
	}

	public List<CachedProviderInfo> getAllProviderInfos(int facilityId) throws MalformedURLException {
		@SuppressWarnings("unchecked")
		List<CachedProviderInfo> results = (List<CachedProviderInfo>) simpleTimeCache.get(facilityId, "ALL_REMOTE_PROVIDERS");

		if (results == null) {
			ProviderInfoWs providerInfoWs = getProviderInfoWs(facilityId);
			results = providerInfoWs.getAllProviderInfo();
			simpleTimeCache.put(facilityId, "ALL_REMOTE_PROVIDERS", results);
		}

		// cloned so alterations don't affect the cached data
		return (new ArrayList<CachedProviderInfo>(results));
	}

	public CachedProviderInfo getProviderInfo(int facilityId, FacilityProviderPrimaryKey remoteProviderPk) throws MalformedURLException
	{
		List<CachedProviderInfo> providers=getAllProviderInfos(facilityId);
		
		for (CachedProviderInfo cachedProviderInfo : providers) {
			if (facilityProviderPrimaryKeyEquals(cachedProviderInfo.getFacilityProviderPrimaryKey(), remoteProviderPk)) {
				return (cachedProviderInfo);
			}
		}

		return (null);
	}
	
    private static boolean facilityProviderPrimaryKeyEquals(FacilityProviderPrimaryKey o1, FacilityProviderPrimaryKey o2)
	{
		try
        {
	        return(o1.getFacilityId().equals(o2.getFacilityId()) && o1.getFacilityProviderId().equals(o2.getFacilityProviderId()));
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
