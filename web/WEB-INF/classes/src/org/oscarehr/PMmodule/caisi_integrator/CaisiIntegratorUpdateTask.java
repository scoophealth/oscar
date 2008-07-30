/*
 * 
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.DemographicDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicImage;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.client.CachedFacilityInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedProgramInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedProviderInfo;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityDemographicIssuePrimaryKey;
import org.oscarehr.caisi_integrator.ws.client.FacilityDemographicPrimaryKey;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityProgramPrimaryKey;
import org.oscarehr.caisi_integrator.ws.client.FacilityProviderPrimaryKey;
import org.oscarehr.caisi_integrator.ws.client.ProgramInfoWs;
import org.oscarehr.caisi_integrator.ws.client.ProviderInfoWs;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.util.DbConnectionFilter;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Log logger = LogFactory.getLog(CaisiIntegratorUpdateTask.class);

	private CaisiIntegratorManager caisiIntegratorManager;
	private FacilityDao facilityDao;
	private DemographicDao demographicDao;
	private CaseManagementIssueDAO caseManagementIssueDAO;
	private ClientImageDAO clientImageDAO;
	private IntegratorConsentDao integratorConsentDao;
	private ProgramDao programDao;
	private ProviderDao providerDao;

	public void setCaisiIntegratorManager(CaisiIntegratorManager mgr) {
		this.caisiIntegratorManager = mgr;
	}

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	public void setCaseManagementIssueDAO(CaseManagementIssueDAO caseManagementIssueDAO) {
		this.caseManagementIssueDAO = caseManagementIssueDAO;
	}

	public void setClientImageDAO(ClientImageDAO clientImageDAO) {
		this.clientImageDAO = clientImageDAO;
	}

	public void setIntegratorConsentDao(IntegratorConsentDao integratorConsentDao) {
		this.integratorConsentDao = integratorConsentDao;
	}

	public void setProgramDao(ProgramDao programDao) {
		this.programDao = programDao;
	}

	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public void run() {
		logger.debug("CaisiIntegratorUpdateTask starting");

		try {
			List<Facility> facilities = facilityDao.findAll(null);

			for (Facility facility : facilities) {
				if (facility.isDisabled() == false && facility.isIntegratorEnabled() == true) {
					pushAllFacilityData(facility);
				}
			}
		} catch (WebServiceException e) {
			logger.warn("Error connecting to integrator. " + e.getMessage());
			logger.debug("Error connecting to integrator.", e);
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();

			logger.debug("CaisiIntegratorUpdateTask finished)");
		}
	}

	private void pushAllFacilityData(Facility facility) throws IOException, DatatypeConfigurationException {
		logger.debug("Pushing data for facility : " + facility.getId());

		// check all parameters are present
		String integratorBaseUrl = facility.getIntegratorUrl();
		String user = facility.getIntegratorUser();
		String password = facility.getIntegratorPassword();

		if (integratorBaseUrl == null || user == null || password == null) {
			logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url="
					+ integratorBaseUrl);
			return;
		}

		// get the time here so there's a slight over lap in actual runtime and
		// activity time, other wise you'll have a gap, better to unnecessarily
		// send a few more records than to miss some.
		Date currentPushTime = new Date();

		// do all the sync work
		pushFacilityInfo(facility);
		DemographicInfoWs service = caisiIntegratorManager.getDemographicInfoWs(facility.getId());
		List<Integer> demographicIds = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		pushDemographics(facility, service, demographicIds);
		pushDemographicsIssues(facility, service, demographicIds);
		pushDemographicsImages(facility, service, demographicIds);
		pushPrograms(facility);
		pushProviders(facility);

		// update late push time only if an exception didn't occur
		// re-get the facility as the sync time could be very long and changes
		// may have been made to the facility.
		facility = facilityDao.find(facility.getId());
		facility.setIntegratorLastPushTime(currentPushTime);
		facilityDao.merge(facility);
	}

	private void pushProviders(Facility facility) {
		try {
			List<String> providerIds = ProviderDao.getProviderIds(facility.getId());

			ArrayList<CachedProviderInfo> cachedProviderInfos = new ArrayList<CachedProviderInfo>();

			for (String providerId : providerIds) {
				Provider provider = providerDao.getProvider(providerId);

				CachedProviderInfo cachedProviderInfo = new CachedProviderInfo();

				BeanUtils.copyProperties(cachedProviderInfo, provider);

				FacilityProviderPrimaryKey pk = new FacilityProviderPrimaryKey();
				pk.setFacilityProviderId(provider.getProviderNo());
				cachedProviderInfo.setFacilityProviderPrimaryKey(pk);

				cachedProviderInfos.add(cachedProviderInfo);
			}

			ProviderInfoWs service = caisiIntegratorManager.getProviderInfoWs(facility.getId());
			service.setCachedProviderInfos(cachedProviderInfos);
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	private void pushPrograms(Facility facility) {
		try {
			List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());

			ArrayList<CachedProgramInfo> cachedProgramInfos = new ArrayList<CachedProgramInfo>();

			for (Program program : programs) {
				CachedProgramInfo cachedProgramInfo = new CachedProgramInfo();

				BeanUtils.copyProperties(cachedProgramInfo, program);

				FacilityProgramPrimaryKey pk = new FacilityProgramPrimaryKey();
				pk.setFacilityProgramId(program.getId());
				cachedProgramInfo.setFacilityProgramPrimaryKey(pk);

				cachedProgramInfo.setGender(program.getManOrWoman());
				if (program.isTransgender())
					cachedProgramInfo.setGender("T");

				cachedProgramInfo.setMaxAge(program.getAgeMax());
				cachedProgramInfo.setMinAge(program.getAgeMin());
				cachedProgramInfo.setStatus(program.getProgramStatus());

				cachedProgramInfos.add(cachedProgramInfo);
			}

			ProgramInfoWs service = caisiIntegratorManager.getProgramInfoWs(facility.getId());
			service.setCachedProgramInfos(cachedProgramInfos);
		} catch (Exception e) {
			logger.error("Unexpected Error", e);
		}
	}

	private void pushDemographicsImages(Facility facility, DemographicInfoWs service, List<Integer> demographicIds) {
		for (Integer demographicId : demographicIds) {
			pushDemographicImages(facility, service, demographicId);
		}
	}

	private void pushDemographicImages(Facility facility, DemographicInfoWs service, Integer demographicId) {
		logger.debug("pushing demographicImage facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		ClientImage clientImage = clientImageDAO.getClientImage(demographicId.toString());
		if (clientImage == null)
			return;

		CachedDemographicImage cachedDemographicImage = new CachedDemographicImage();

		FacilityDemographicPrimaryKey facilityDemographicPrimaryKey = new FacilityDemographicPrimaryKey();
		facilityDemographicPrimaryKey.setFacilityDemographicId(demographicId);

		cachedDemographicImage.setFacilityDemographicPrimaryKey(facilityDemographicPrimaryKey);
		cachedDemographicImage.setImage(clientImage.getImage_data());

		service.setCachedDemographicImage(cachedDemographicImage);
	}

	private void pushDemographicsIssues(Facility facility, DemographicInfoWs service, List<Integer> demographicIds) {
		for (Integer demographicId : demographicIds) {
			pushDemographicIssues(facility, service, demographicId);
		}
	}

	private void pushDemographicIssues(Facility facility, DemographicInfoWs service, Integer demographicId) {
		logger.debug("pushing demographicIssue facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDAO.getIssuesByDemographic(demographicId.toString());
		if (caseManagementIssues.size() == 0)
			return;

		ArrayList<CachedDemographicIssue> issueTransfers = new ArrayList<CachedDemographicIssue>();
		for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
			try {
				Issue issue = caseManagementIssue.getIssue();
				CachedDemographicIssue issueTransfer = new CachedDemographicIssue();

				FacilityDemographicIssuePrimaryKey facilityDemographicIssuePrimaryKey = new FacilityDemographicIssuePrimaryKey();
				facilityDemographicIssuePrimaryKey.setFacilityDemographicId(Integer.parseInt(caseManagementIssue.getDemographic_no()));
				facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
				issueTransfer.setFacilityDemographicIssuePrimaryKey(facilityDemographicIssuePrimaryKey);

				BeanUtils.copyProperties(issueTransfer, caseManagementIssue);			
				issueTransfer.setIssueDescription(issue.getDescription());

				issueTransfers.add(issueTransfer);
			} catch (Exception e) {
				logger.error("Unexpected Error.", e);
			}
		}

		service.setCachedDemographicIssues(issueTransfers);
	}

	private void pushDemographics(Facility facility, DemographicInfoWs service, List<Integer> demographicIds) throws MalformedURLException, DatatypeConfigurationException {
		for (Integer demographicId : demographicIds) {
			logger.debug("pushing demographicInfo facilityId:" + facility.getId() + ", demographicId:" + demographicId);

			Demographic demographic = demographicDao.getDemographicById(demographicId);

			CachedDemographicInfo cachedDemographicInfo = new CachedDemographicInfo();

			FacilityDemographicPrimaryKey facilityDemographicPrimaryKey = new FacilityDemographicPrimaryKey();
			facilityDemographicPrimaryKey.setFacilityDemographicId(demographic.getDemographicNo());
			cachedDemographicInfo.setFacilityDemographicPrimaryKey(facilityDemographicPrimaryKey);

			XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			if (demographic.getYearOfBirth() != null)
				cal.setYear(Integer.parseInt(demographic.getYearOfBirth()));
			if (demographic.getMonthOfBirth() != null)
				cal.setMonth(Integer.parseInt(demographic.getMonthOfBirth()));
			if (demographic.getDateOfBirth() != null)
				cal.setDay(Integer.parseInt(demographic.getDateOfBirth()));
			cachedDemographicInfo.setBirthDate(cal);

			cachedDemographicInfo.setCity(demographic.getCity());
			cachedDemographicInfo.setFirstName(demographic.getFirstName());
			cachedDemographicInfo.setGender(demographic.getSex());
			cachedDemographicInfo.setHin(demographic.getHin());
			cachedDemographicInfo.setLastName(demographic.getLastName());
			cachedDemographicInfo.setProvince(demographic.getProvince());
			cachedDemographicInfo.setSin(demographic.getSin());

			IntegratorConsent consent = integratorConsentDao.findLatestByFacilityAndDemographic(facility.getId(), demographicId);
			if (consent != null) {
				cachedDemographicInfo.setConsentToBasicPersonalId(consent.isConsentToBasicPersonalId());
				cachedDemographicInfo.setConsentToHealthCardId(consent.isConsentToHealthCardId());
				cachedDemographicInfo.setConsentToIssues(consent.isConsentToIssues());
				cachedDemographicInfo.setConsentToNotes(consent.isConsentToNotes());
				cachedDemographicInfo.setConsentToStatistics(consent.isConsentToStatistics());
				cachedDemographicInfo.setRestrictConsentToHic(consent.isRestrictConsentToHic());
			}

			service.setCachedDemographicInfo(cachedDemographicInfo);
		}
	}

	private void pushFacilityInfo(Facility facility) throws IOException {

		try {
			CachedFacilityInfo cachedFacilityInfo = new CachedFacilityInfo();
			BeanUtils.copyProperties(cachedFacilityInfo, facility);

			FacilityInfoWs service = caisiIntegratorManager.getFacilityInfoWs(facility.getId());

			logger.debug("pushing facilityInfo");
			service.setMyFacilityInfo(cachedFacilityInfo);
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}
}
