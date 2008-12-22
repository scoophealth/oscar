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
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicImage;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.caisi_integrator.ws.client.CachedProgram;
import org.oscarehr.caisi_integrator.ws.client.CachedProvider;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.client.FacilityWs;
import org.oscarehr.caisi_integrator.ws.client.PreventionExtTransfer;
import org.oscarehr.caisi_integrator.ws.client.ProgramWs;
import org.oscarehr.caisi_integrator.ws.client.ProviderWs;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Log logger = LogFactory.getLog(CaisiIntegratorUpdateTask.class);

	private static final String INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY = "INTEGRATOR_UPDATE_PERIOD";

	private static Timer timer = new Timer("CaisiIntegratorUpdateTask Timer", true);
	private static TimerTask timerTask = null;

	private CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private CaseManagementIssueDAO caseManagementIssueDAO = (CaseManagementIssueDAO) SpringUtils.getBean("caseManagementIssueDAO");
	private ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");
	private IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");
	private ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private PreventionDao preventionDao = (PreventionDao) SpringUtils.getBean("preventionDao");

	// private CaseManagementNoteDAO caseManagementNoteDAO = (CaseManagementNoteDAO) SpringUtils.getBean("CaseManagementNoteDAO");

	public static synchronized void startTask() {
		if (timerTask == null) {
			int period = 0;
			String periodStr = null;
			try {
				periodStr = (String) OscarProperties.getInstance().get(INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY);
				period = Integer.parseInt(periodStr);
			} catch (Exception e) {
				logger.error("CaisiIntegratorUpdateTask not scheduled, period is missing or invalid properties file : " + INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY + '=' + periodStr, e);
				return;
			}

			logger.info("Scheduling CaisiIntegratorUpdateTask for period : " + period);
			timerTask = new CaisiIntegratorUpdateTask();
			timer.schedule(timerTask, 10000, period);
		} else {
			logger.error("Start was called twice on this timer task object.", new Exception());
		}
	}

	public static synchronized void stopTask() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;

			logger.info("CaisiIntegratorUpdateTask has been unscheduled.");
		}
	}

	public void run() {
		logger.info("CaisiIntegratorUpdateTask starting");

		try {
			pushAllFacilities();
		} catch (WebServiceException e) {
			logger.warn("Error connecting to integrator. " + e.getMessage());
			logger.debug("Error connecting to integrator.", e);
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();

			logger.info("CaisiIntegratorUpdateTask finished)");
		}
	}

	public void pushAllFacilities() throws IOException, DatatypeConfigurationException, IllegalAccessException, InvocationTargetException {
		List<Facility> facilities = facilityDao.findAll(null);

		for (Facility facility : facilities) {
			if (facility.isDisabled() == false && facility.isIntegratorEnabled() == true) {
				pushAllFacilityData(facility);
			}
		}
	}

	private void pushAllFacilityData(Facility facility) throws IOException, DatatypeConfigurationException, IllegalAccessException, InvocationTargetException {
		logger.info("Pushing data for facility : " + facility.getId() + " : " + facility.getName());

		// check all parameters are present
		String integratorBaseUrl = facility.getIntegratorUrl();
		String user = facility.getIntegratorUser();
		String password = facility.getIntegratorPassword();

		if (integratorBaseUrl == null || user == null || password == null) {
			logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
			return;
		}

		// get the time here so there's a slight over lap in actual runtime and
		// activity time, other wise you'll have a gap, better to unnecessarily
		// send a few more records than to miss some.
		Date currentPushTime = new Date();

		// do all the sync work
		// in theory sync should only send changed data, but currently due to
		// the lack of proper data models, we don't have a reliable timestamp on when things change so we just push everything, highly inefficient but it works until we fix the
		// data model.
		pushFacility(facility);
		pushPrograms(facility);
		pushProviders(facility);
		pushAllDemographics(facility);

		// update late push time only if an exception didn't occur
		// re-get the facility as the sync time could be very long and changes
		// may have been made to the facility.
		facility = facilityDao.find(facility.getId());
		facility.setIntegratorLastPushTime(currentPushTime);
		facilityDao.merge(facility);
	}

	private void pushFacility(Facility facility) throws MalformedURLException {
		try {
			CachedFacility cachedFacility = new CachedFacility();
			BeanUtils.copyProperties(cachedFacility, facility);

			FacilityWs service = caisiIntegratorManager.getFacilityWs(facility.getId());

			logger.debug("pushing facility");
			service.setMyFacility(cachedFacility);
		} catch (IllegalAccessException e) {
			logger.error("Unexpected Error.", e);
		} catch (InvocationTargetException e) {
			logger.error("Unexpected Error.", e);
		}
	}

	private void pushPrograms(Facility facility) throws MalformedURLException {
		try {
			List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());

			ArrayList<CachedProgram> cachedPrograms = new ArrayList<CachedProgram>();

			for (Program program : programs) {
				CachedProgram cachedProgram = new CachedProgram();

				BeanUtils.copyProperties(cachedProgram, program);

				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(program.getId());
				cachedProgram.setFacilityIdIntegerCompositePk(pk);

				cachedProgram.setGender(program.getManOrWoman());
				if (program.isTransgender()) cachedProgram.setGender("T");

				cachedProgram.setMaxAge(program.getAgeMax());
				cachedProgram.setMinAge(program.getAgeMin());
				cachedProgram.setStatus(program.getProgramStatus());

				cachedPrograms.add(cachedProgram);
			}

			ProgramWs service = caisiIntegratorManager.getProgramWs(facility.getId());
			service.setCachedPrograms(cachedPrograms);
		} catch (IllegalAccessException e) {
			logger.error("Unexpected Error", e);
		} catch (InvocationTargetException e) {
			logger.error("Unexpected Error", e);
		}
	}

	private void pushProviders(Facility facility) throws MalformedURLException {
		try {
			List<String> providerIds = ProviderDao.getProviderIds(facility.getId());

			ArrayList<CachedProvider> cachedProviders = new ArrayList<CachedProvider>();

			for (String providerId : providerIds) {
				Provider provider = providerDao.getProvider(providerId);

				CachedProvider cachedProvider = new CachedProvider();

				BeanUtils.copyProperties(cachedProvider, provider);

				FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
				pk.setCaisiItemId(provider.getProviderNo());
				cachedProvider.setFacilityIdStringCompositePk(pk);

				cachedProviders.add(cachedProvider);
			}

			ProviderWs service = caisiIntegratorManager.getProviderWs(facility.getId());
			service.setCachedProviders(cachedProviders);
		} catch (IllegalAccessException e) {
			logger.error("Unexpected error.", e);
		} catch (InvocationTargetException e) {
			logger.error("Unexpected error.", e);
		}
	}

	private void pushAllDemographics(Facility facility) throws MalformedURLException, DatatypeConfigurationException, IllegalAccessException, InvocationTargetException {
		List<Integer> demographicIds = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		DemographicWs service = caisiIntegratorManager.getDemographicWs(facility.getId());

		for (Integer demographicId : demographicIds) {
			logger.debug("pushing demographic facilityId:" + facility.getId() + ", demographicId:" + demographicId);

			try {
				pushDemographic(facility, service, demographicId);
				pushDemographicIssues(facility, service, demographicId);
				pushDemographicImages(facility, service, demographicId);
				pushDemographicPreventions(facility, service, demographicId);
				pushDemographicNotes(facility, service, demographicId);
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}
	}

	private void pushDemographic(Facility facility, DemographicWs service, Integer demographicId) throws IllegalAccessException, InvocationTargetException, DatatypeConfigurationException {
		CachedDemographic cachedDemographic = new CachedDemographic();

		// set consent info
		IntegratorConsent consent = integratorConsentDao.findLatestByFacilityAndDemographic(facility.getId(), demographicId);
		if (consent != null) {
			BeanUtils.copyProperties(cachedDemographic, consent);
		}

		// set demographic info
		Demographic demographic = demographicDao.getDemographicById(demographicId);

		BeanUtils.copyProperties(cachedDemographic, demographic);

		FacilityIdIntegerCompositePk facilityDemographicPrimaryKey = new FacilityIdIntegerCompositePk();
		facilityDemographicPrimaryKey.setCaisiItemId(demographic.getDemographicNo());
		cachedDemographic.setFacilityIdIntegerCompositePk(facilityDemographicPrimaryKey);

		try {
			XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			if (demographic.getYearOfBirth() != null) cal.setYear(Integer.parseInt(demographic.getYearOfBirth()));
			if (demographic.getMonthOfBirth() != null) cal.setMonth(Integer.parseInt(demographic.getMonthOfBirth()));
			if (demographic.getDateOfBirth() != null) cal.setDay(Integer.parseInt(demographic.getDateOfBirth()));
			cachedDemographic.setBirthDate(cal);
		} catch (NumberFormatException e) {
			logger.error("Unexpected error.", e);
		}

		cachedDemographic.setHinVersion(demographic.getVer());
		cachedDemographic.setGender(demographic.getSex());

		// send the request
		service.setCachedDemographic(cachedDemographic);
	}

	private void pushDemographicImages(Facility facility, DemographicWs service, Integer demographicId) {
		logger.debug("pushing demographicImage facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		ClientImage clientImage = clientImageDAO.getClientImage(demographicId);
		if (clientImage == null) return;

		CachedDemographicImage cachedDemographicImage = new CachedDemographicImage();

		FacilityIdIntegerCompositePk facilityDemographicPrimaryKey = new FacilityIdIntegerCompositePk();
		facilityDemographicPrimaryKey.setCaisiItemId(demographicId);

		cachedDemographicImage.setFacilityIdIntegerCompositePk(facilityDemographicPrimaryKey);
		cachedDemographicImage.setImage(clientImage.getImage_data());

		service.setCachedDemographicImage(cachedDemographicImage);
	}

	private void pushDemographicIssues(Facility facility, DemographicWs service, Integer demographicId) {
		logger.debug("pushing demographicIssues facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDAO.getIssuesByDemographic(demographicId.toString());
		if (caseManagementIssues.size() == 0) return;

		ArrayList<CachedDemographicIssue> issues = new ArrayList<CachedDemographicIssue>();
		for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
			try {
				Issue issue = caseManagementIssue.getIssue();
				CachedDemographicIssue issueTransfer = new CachedDemographicIssue();

				FacilityIdDemographicIssueCompositePk facilityDemographicIssuePrimaryKey = new FacilityIdDemographicIssueCompositePk();
				facilityDemographicIssuePrimaryKey.setCaisiDemographicId(Integer.parseInt(caseManagementIssue.getDemographic_no()));
				facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
				issueTransfer.setFacilityDemographicIssuePk(facilityDemographicIssuePrimaryKey);

				BeanUtils.copyProperties(issueTransfer, caseManagementIssue);
				issueTransfer.setIssueDescription(issue.getDescription());

				issues.add(issueTransfer);
			} catch (IllegalAccessException e) {
				logger.error("Unexpected Error.", e);
			} catch (InvocationTargetException e) {
				logger.error("Unexpected Error.", e);
			}
		}

		service.setCachedDemographicIssues(issues);
	}

	private void pushDemographicPreventions(Facility facility, DemographicWs service, Integer demographicId) throws DatatypeConfigurationException {
		logger.debug("pushing demographicPreventions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		ArrayList<CachedDemographicPrevention> preventionsToSend = new ArrayList<CachedDemographicPrevention>();
		ArrayList<PreventionExtTransfer> preventionExtsToSend = new ArrayList<PreventionExtTransfer>();

		// get all preventions
		// for each prevention, copy fields to an integrator prevention
		// need to copy ext info
		// add prevention to array list to send
		List<Prevention> localPreventions = preventionDao.findNotDeletedByDemographicId(demographicId);
		for (Prevention localPrevention : localPreventions) {
			CachedDemographicPrevention cachedDemographicPrevention = new CachedDemographicPrevention();
			cachedDemographicPrevention.setCaisiDemographicId(demographicId);
			cachedDemographicPrevention.setCaisiProviderId(localPrevention.getProviderNo());

			{
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(localPrevention.getId());
				cachedDemographicPrevention.setFacilityPreventionPk(pk);
			}

			if (localPrevention.getNextDate() != null) {
				GregorianCalendar localCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
				localCalendar.setTimeInMillis(localPrevention.getNextDate().getTime());

				XMLGregorianCalendar soapCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(localCalendar);
				cachedDemographicPrevention.setNextDate(soapCalendar);
			}

			if (localPrevention.getPreventionDate() != null) {
				GregorianCalendar localCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
				localCalendar.setTimeInMillis(localPrevention.getPreventionDate().getTime());

				XMLGregorianCalendar soapCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(localCalendar);
				cachedDemographicPrevention.setPreventionDate(soapCalendar);
			}

			cachedDemographicPrevention.setPreventionType(localPrevention.getPreventionType());

			preventionsToSend.add(cachedDemographicPrevention);

			// add ext info
			Integer preventionId = localPrevention.getId();
			HashMap<String, String> localPreventionExts = preventionDao.getPreventionExt(preventionId);
			for (Map.Entry<String, String> entry : localPreventionExts.entrySet()) {
				PreventionExtTransfer preventionExtTransfer = new PreventionExtTransfer();
				preventionExtTransfer.setPreventionId(preventionId);
				preventionExtTransfer.setKey(entry.getKey());
				preventionExtTransfer.setValue(entry.getValue());
				preventionExtsToSend.add(preventionExtTransfer);
			}
		}

		service.setCachedDemographicPreventions(preventionsToSend, preventionExtsToSend);
	}

	private void pushDemographicNotes(Facility facility, DemographicWs service, Integer demographicId) {
		logger.debug("pushing demographicNotes facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		// List<CaseManagementNote> caseManagementNotes = caseManagementNoteDAO.findLatestUnlockedByDemographicIdProgramId(demographicId.toString());
		// if (caseManagementIssues.size() == 0) return;
		//
		// ArrayList<CachedDemographicIssue> issues = new ArrayList<CachedDemographicIssue>();
		// for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
		// try {
		// Issue issue = caseManagementIssue.getIssue();
		// CachedDemographicIssue issueTransfer = new CachedDemographicIssue();
		//
		// FacilityIdDemographicIssueCompositePk facilityDemographicIssuePrimaryKey = new FacilityIdDemographicIssueCompositePk();
		// facilityDemographicIssuePrimaryKey.setCaisiDemographicId(Integer.parseInt(caseManagementIssue.getDemographic_no()));
		// facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
		// issueTransfer.setFacilityDemographicIssuePk(facilityDemographicIssuePrimaryKey);
		//
		// BeanUtils.copyProperties(issueTransfer, caseManagementIssue);
		// issueTransfer.setIssueDescription(issue.getDescription());
		//
		// issues.add(issueTransfer);
		// }
		// catch (IllegalAccessException e) {
		// logger.error("Unexpected Error.", e);
		// }
		// catch (InvocationTargetException e) {
		// logger.error("Unexpected Error.", e);
		// }
		// }
		//
		// service.setCachedDemographicNotes(issues);
	}
}
