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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedAppointment;
import org.oscarehr.caisi_integrator.ws.CachedBillingOnItem;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.CachedDxresearch;
import org.oscarehr.caisi_integrator.ws.CachedEformData;
import org.oscarehr.caisi_integrator.ws.CachedEformValue;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedMeasurement;
import org.oscarehr.caisi_integrator.ws.CachedMeasurementExt;
import org.oscarehr.caisi_integrator.ws.CachedMeasurementMap;
import org.oscarehr.caisi_integrator.ws.CachedMeasurementType;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.CodeType;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.Gender;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.caisi_integrator.ws.PreventionExtTransfer;
import org.oscarehr.caisi_integrator.ws.ProgramWs;
import org.oscarehr.caisi_integrator.ws.ProviderTransfer;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.caisi_integrator.ws.Role;
import org.oscarehr.caisi_integrator.ws.SetConsentTransfer;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.OscarAppointment;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;
import org.oscarehr.dx.dao.DxResearchDAO;
import org.oscarehr.dx.model.DxResearch;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.ShutdownException;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.appt.AppointmentDao;
import oscar.facility.IntegratorControlDao;
import oscar.oscarBilling.ca.on.dao.BillingOnItemDao;
import oscar.oscarBilling.ca.on.model.BillingOnCHeader1;
import oscar.oscarBilling.ca.on.model.BillingOnItem;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementMapDao;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementTypeDao;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsExtDao;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsHibernateDao;
import oscar.oscarEncounter.oscarMeasurements.model.Measurementmap;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;
import oscar.oscarEncounter.oscarMeasurements.model.Measurementtype;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	private static final String INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY = "INTEGRATOR_UPDATE_PERIOD";

	private static Timer timer = new Timer("CaisiIntegratorUpdateTask Timer", true);

	private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private CaseManagementIssueDAO caseManagementIssueDAO = (CaseManagementIssueDAO) SpringUtils.getBean("caseManagementIssueDAO");
	private IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
	private CaseManagementNoteDAO caseManagementNoteDAO = (CaseManagementNoteDAO) SpringUtils.getBean("CaseManagementNoteDAO");
	private CaseManagementIssueNotesDao caseManagementIssueNotesDao = (CaseManagementIssueNotesDao) SpringUtils.getBean("caseManagementIssueNotesDao");
	private ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");
	private IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");
	private ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private PreventionDao preventionDao = (PreventionDao) SpringUtils.getBean("preventionDao");
	private DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
	private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
	private AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
    private AppointmentDao appointmentDao = (AppointmentDao) SpringUtils.getBean("appointmentDao");
    private IntegratorControlDao integratorControlDao = (IntegratorControlDao) SpringUtils.getBean("integratorControlDao");
    private MeasurementsHibernateDao measurementsDao = (MeasurementsHibernateDao) SpringUtils.getBean("measurementsDao");
    private MeasurementsExtDao measurementsExtDao = (MeasurementsExtDao) SpringUtils.getBean("measurementsExtDao");
    private MeasurementTypeDao measurementTypeDao = (MeasurementTypeDao) SpringUtils.getBean("measurementTypeDao");
    private MeasurementMapDao measurementMapDao = (MeasurementMapDao) SpringUtils.getBean("measurementMapDao");
    private DxResearchDAO dxresearchDao = (DxResearchDAO) SpringUtils.getBean("dxResearchDao");
    private BillingOnItemDao billingOnItemDao = (BillingOnItemDao) SpringUtils.getBean("billingOnItemDao");
    private EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");
    private EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");
    private GroupNoteDao groupNoteDao = (GroupNoteDao)SpringUtils.getBean("groupNoteDao");
    	
	private static TimerTask timerTask = null;

	static {
		// ensure cxf uses log4j
		System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Log4jLogger");
	}

	public static synchronized void startTask() {
		if (timerTask == null) {
			long period = 0;
			String periodStr = null;
			try {
				periodStr = (String) OscarProperties.getInstance().get(INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY);
                                period = Long.parseLong(periodStr);
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
		logger.debug("CaisiIntegratorUpdateTask starting");

		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();

		try {
			pushAllFacilities();
		} catch (ShutdownException e) {
			logger.debug("CaisiIntegratorUpdateTask received shutdown notice.");
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} finally {
			LoggedInInfo.loggedInInfo.remove();
			DbConnectionFilter.releaseAllThreadDbResources();

			logger.debug("CaisiIntegratorUpdateTask finished");
		}
	}

	public void pushAllFacilities() throws ShutdownException {
		List<Facility> facilities = facilityDao.findAll(true);

		for (Facility facility : facilities) {
			MiscUtils.checkShutdownSignaled();

			try {
				if (facility.isIntegratorEnabled()) {
					pushAllDataForOneFacility(facility);
				}
			} catch (WebServiceException e) {
				if (CxfClientUtils.isConnectionException(e)) {
					logger.warn("Error connecting to integrator. " + e.getMessage());
					logger.debug("Error connecting to integrator.", e);
				} else {
					logger.error("Unexpected error.", e);
				}
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}
	}

	private void pushAllDataForOneFacility(Facility facility) throws IOException, IllegalAccessException, InvocationTargetException, ShutdownException {
		logger.info("Start pushing data for facility : " + facility.getId() + " : " + facility.getName());

		// set working facility
		LoggedInInfo.loggedInInfo.get().currentFacility=facility;
		
		// check all parameters are present
		String integratorBaseUrl = facility.getIntegratorUrl();
		String user = facility.getIntegratorUser();
		String password = facility.getIntegratorPassword();

		if (integratorBaseUrl == null || user == null || password == null) {
			logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
			return;
		}

		FacilityWs service = CaisiIntegratorManager.getFacilityWs();
		CachedFacility cachedFacility=service.getMyFacility();
		
		// set to the beginning of time so by default everything is pushed
		Date lastDataUpdated=new Date(0); 
		if (cachedFacility!=null && cachedFacility.getLastDataUpdate()!=null) lastDataUpdated=cachedFacility.getLastDataUpdate();
		
		// this needs to be set now, before we do any sends, this will cause anything updated after now to be resent twice but it's better than items being missed that were updated after this started.
		Date currentUpdateDate=new Date();
		
		// do all the sync work
		// in theory sync should only send changed data, but currently due to
		// the lack of proper data models, we don't have a reliable timestamp on when things change so we just push everything, highly inefficient but it works until we fix the
		// data model. The last update date is available though as per above...
		pushFacility(lastDataUpdated);
		pushProviders(lastDataUpdated, facility);
		pushPrograms(lastDataUpdated, facility);
		pushAllDemographics(lastDataUpdated);
		
		// all things updated successfully
		service.updateMyFacilityLastUpdateDate(currentUpdateDate);

		logger.info("Finished pushing data for facility : " + facility.getId() + " : " + facility.getName());
	}

	private void pushFacility(Date lastDataUpdated) throws MalformedURLException, IllegalAccessException, InvocationTargetException {
		Facility facility=LoggedInInfo.loggedInInfo.get().currentFacility;

		if (facility.getLastUpdated().after(lastDataUpdated))
		{
			logger.debug("pushing facility record");
	
			CachedFacility cachedFacility = new CachedFacility();
			BeanUtils.copyProperties(cachedFacility, facility);
	
			FacilityWs service = CaisiIntegratorManager.getFacilityWs();
			service.setMyFacility(cachedFacility);
		}
		else
		{
			logger.debug("skipping facility record, not updated since last push");
		}
	}

	private void pushPrograms(Date lastDataUpdated, Facility facility) throws MalformedURLException, IllegalAccessException, InvocationTargetException, ShutdownException {
		// all are always sent so program deletions have be proliferated.
		List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());

		ArrayList<CachedProgram> cachedPrograms = new ArrayList<CachedProgram>();

		for (Program program : programs) {
			logger.debug("pushing program : "+program.getId()+':'+program.getName());
			MiscUtils.checkShutdownSignaled();

			CachedProgram cachedProgram = new CachedProgram();

			BeanUtils.copyProperties(cachedProgram, program);

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setCaisiItemId(program.getId());
			cachedProgram.setFacilityIdIntegerCompositePk(pk);

			try
			{
				cachedProgram.setGender(Gender.valueOf(program.getManOrWoman().toUpperCase()));
			}
			catch (Exception e)
			{
				// do nothing, we can't assume anything is right or wrong with genders 
				// until the whole mess is sorted out, for now it's a what you get is 
				// what you get
			}

			if (program.isTransgender()) cachedProgram.setGender(Gender.T);

			cachedProgram.setMaxAge(program.getAgeMax());
			cachedProgram.setMinAge(program.getAgeMin());
			cachedProgram.setStatus(program.getProgramStatus());

			cachedPrograms.add(cachedProgram);
		}

		ProgramWs service = CaisiIntegratorManager.getProgramWs(facility);
		service.setCachedPrograms(cachedPrograms);
	}

	private void pushProviders(Date lastDataUpdated, Facility facility) throws MalformedURLException, IllegalAccessException, InvocationTargetException, ShutdownException {
		List<String> providerIds = ProviderDao.getProviderIds(facility.getId());

		ArrayList<ProviderTransfer> providerTransfers = new ArrayList<ProviderTransfer>();

		for (String providerId : providerIds) {
			MiscUtils.checkShutdownSignaled();
			logger.debug("Adding provider " + providerId + " for " + facility.getName());

			// copy provider basic data over
			Provider provider = providerDao.getProvider(providerId);
                        if (provider == null) continue;

			ProviderTransfer providerTransfer=new ProviderTransfer();
			CachedProvider cachedProvider = new CachedProvider();

			BeanUtils.copyProperties(cachedProvider, provider);

			FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
			pk.setCaisiItemId(provider.getProviderNo());
			cachedProvider.setFacilityIdStringCompositePk(pk);

			providerTransfer.setCachedProvider(cachedProvider);
			
			// copy roles over
			List<SecUserRole> roles=secUserRoleDao.getUserRoles(providerId);
			for (SecUserRole role : roles)
			{
				Role integratorRole=IntegratorRoleUtils.getIntegratorRole(role.getRoleName());
				if (integratorRole!=null) providerTransfer.getRoles().add(integratorRole);
			}
			
			// add to list
			providerTransfers.add(providerTransfer);
		}

		ProviderWs service = CaisiIntegratorManager.getProviderWs(facility);
//		service.setCachedProviders(providerTransfers);
		writeToIntegrator(providerTransfers, service, ProviderTransfer.class.getName());
	}

	private void pushAllDemographics(Date lastDataUpdated) throws MalformedURLException, ShutdownException {
		Facility facility=LoggedInInfo.loggedInInfo.get().currentFacility;
		
		List<Integer> demographicIds = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		DemographicWs demographicService = CaisiIntegratorManager.getDemographicWs();
		List<Program> programsInFacility = programDao.getProgramsByFacilityId(facility.getId());
		List<String> providerIdsInFacility = ProviderDao.getProviderIds(facility.getId());

		for (Integer demographicId : demographicIds) {
			logger.debug("pushing demographic facilityId:" + facility.getId() + ", demographicId:" + demographicId);
			MiscUtils.checkShutdownSignaled();

			try {
				pushDemographic(demographicService, demographicId, facility.getId());
				// it's safe to set the consent later so long as we default it to none when we send the original demographic data in the line above.
				pushDemographicConsent(facility, demographicService, demographicId);
				pushDemographicIssues(facility, programsInFacility, demographicService, demographicId);
				pushDemographicPreventions(facility, providerIdsInFacility, demographicService, demographicId);
				pushDemographicNotes(facility, demographicService, demographicId);
				pushDemographicDrugs(facility, providerIdsInFacility, demographicService, demographicId);
				pushAdmissions(facility, programsInFacility, demographicService, demographicId);
				pushAppointments(facility, demographicService, demographicId);
				pushMeasurements(facility, demographicService, demographicId);
				pushDxresearchs(facility, demographicService, demographicId);
				pushBillingItems(facility, demographicService, demographicId);
				pushEforms(facility, demographicService, demographicId);

			} catch (IllegalArgumentException iae) {
				// continue processing demographics if date values in current demographic are bad
				// all other errors thrown by the above methods should indicate a failure in the service
				// connection at large -- continuing to process not possible
				// need some way of notification here.
				logger.error("Error updating demographic, continuing with Demographic batch", iae);
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}
	}

	private void pushDemographic(DemographicWs service, Integer demographicId, Integer facilityId) throws IllegalAccessException, InvocationTargetException {
		DemographicTransfer demographicTransfer = new DemographicTransfer();

		// set demographic info
		Demographic demographic = demographicDao.getDemographicById(demographicId);

                //The following line copy 6 fields: FirstName,LastName,City,Province,Hin,Sin
		BeanUtils.copyProperties(demographicTransfer, demographic);

		demographicTransfer.setCaisiDemographicId(demographic.getDemographicNo());
		demographicTransfer.setBirthDate(demographic.getBirthDay().getTime());

		demographicTransfer.setHinType(demographic.getHcType());
		demographicTransfer.setHinVersion(demographic.getVer());
		demographicTransfer.setCaisiProviderId(demographic.getProviderNo());
		
		try
		{
			demographicTransfer.setGender(Gender.valueOf(demographic.getSex().toUpperCase()));
		}
		catch (Exception e)
		{
			// do nothing, for now gender is on a "good luck" what ever you
			// get is what you get basis until the whole gender mess is sorted.
		}
		
		// set image
		ClientImage clientImage = clientImageDAO.getClientImage(demographicId);
		if (clientImage != null) {
			demographicTransfer.setPhoto(clientImage.getImage_data());
			demographicTransfer.setPhotoUpdateDate(clientImage.getUpdate_date());
		}

		// set flag to remove demographic identity
		boolean rid = integratorControlDao.readRemoveDemographicIdentity(facilityId);
		demographicTransfer.setRemoveId(rid);

		// send the request
		service.setDemographic(demographicTransfer);
	}

	private void pushDemographicConsent(Facility facility, DemographicWs demographicService, Integer demographicId)  {

		// find the latest relvent consent that needs to be pushed.
		List<IntegratorConsent> tempConsents=integratorConsentDao.findByFacilityAndDemographic(facility.getId(), demographicId);

		for (IntegratorConsent tempConsent : tempConsents)
		{
			if (tempConsent.getClientConsentStatus()==ConsentStatus.GIVEN || tempConsent.getClientConsentStatus()==ConsentStatus.REVOKED)
			{
				SetConsentTransfer consentTransfer=CaisiIntegratorManager.makeSetConsentTransfer(tempConsent);				
				demographicService.setCachedDemographicConsent(consentTransfer);
				logger.debug("pushDemographicConsent:"+tempConsent.getId()+","+tempConsent.getFacilityId()+","+tempConsent.getDemographicId());
				return;
			}
		}
	}
	
	private void pushDemographicIssues(Facility facility, List<Program> programsInFacility, DemographicWs service, Integer demographicId) throws IllegalAccessException, InvocationTargetException, ShutdownException {
		logger.debug("pushing demographicIssues facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDAO.getIssuesByDemographic(demographicId.toString());
		if (caseManagementIssues.size() == 0) return;

		ArrayList<CachedDemographicIssue> issues = new ArrayList<CachedDemographicIssue>();
		for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
			MiscUtils.checkShutdownSignaled();

			// don't send issue if it is not in our facility.
			logger.debug("Facility:" + facility.getName() + " - caseManagementIssue = " + caseManagementIssue.toString());
			if (caseManagementIssue.getProgram_id() == null || !isProgramIdInProgramList(programsInFacility, caseManagementIssue.getProgram_id())) continue;

			long issueId=caseManagementIssue.getIssue_id();
			Issue issue = issueDao.getIssue(issueId);
			CachedDemographicIssue cachedDemographicIssue = new CachedDemographicIssue();

			FacilityIdDemographicIssueCompositePk facilityDemographicIssuePrimaryKey = new FacilityIdDemographicIssueCompositePk();
			facilityDemographicIssuePrimaryKey.setCaisiDemographicId(Integer.parseInt(caseManagementIssue.getDemographic_no()));
			facilityDemographicIssuePrimaryKey.setCodeType(CodeType.ICD_10); // temporary hard code hack till we sort this out
			facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
			cachedDemographicIssue.setFacilityDemographicIssuePk(facilityDemographicIssuePrimaryKey);

			BeanUtils.copyProperties(cachedDemographicIssue, caseManagementIssue);
			cachedDemographicIssue.setIssueDescription(issue.getDescription());
			cachedDemographicIssue.setIssueRole(IntegratorRoleUtils.getIntegratorRole(issue.getRole()));

			issues.add(cachedDemographicIssue);
		}

//		if (issues.size()>0) service.setCachedDemographicIssues(issues);
		writeToIntegrator(issues, service, CachedDemographicIssue.class.getName());
	}

	private void pushAdmissions(Facility facility, List<Program> programsInFacility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing admissions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Admission> admissions = admissionDao.getAdmissionsByFacility(demographicId, facility.getId());
		if (admissions.size() == 0) return;

		ArrayList<CachedAdmission> cachedAdmissions = new ArrayList<CachedAdmission>();
		for (Admission admission : admissions) {
			MiscUtils.checkShutdownSignaled();

			// don't send admission if it is not in our facility. yeah I know I'm double checking since it's selected
			// but the reality is I don't trust it and our facility segmentation is flakey at best so.. better to check again.
			logger.debug("Facility:" + facility.getName() + " - admissionId = " + admission.getId());
			if (!isProgramIdInProgramList(programsInFacility, admission.getProgramId())) continue;

			CachedAdmission cachedAdmission = new CachedAdmission();

			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(admission.getId().intValue());
			cachedAdmission.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedAdmission.setAdmissionDate(admission.getAdmissionDate());
			cachedAdmission.setAdmissionNotes(admission.getAdmissionNotes());
			cachedAdmission.setCaisiDemographicId(demographicId);
			cachedAdmission.setCaisiProgramId(admission.getProgramId());
			cachedAdmission.setDischargeDate(admission.getDischargeDate());
			cachedAdmission.setDischargeNotes(admission.getDischargeNotes());

			cachedAdmissions.add(cachedAdmission);
		}

//		if (cachedAdmissions.size()>0) demographicService.setCachedAdmissions(cachedAdmissions);
		writeToIntegrator(cachedAdmissions, demographicService, CachedAdmission.class.getName());
	}

	private boolean isProgramIdInProgramList(List<Program> programList, int programId) {
		for (Program p : programList) {
			if (p.getId().intValue() == programId) return (true);
		}

		return (false);
	}

	private void pushDemographicPreventions(Facility facility, List<String> providerIdsInFacility, DemographicWs service, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicPreventions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		ArrayList<CachedDemographicPrevention> preventionsToSend = new ArrayList<CachedDemographicPrevention>();
		ArrayList<PreventionExtTransfer> preventionExtsToSend = new ArrayList<PreventionExtTransfer>();

		// get all preventions
		// for each prevention, copy fields to an integrator prevention
		// need to copy ext info
		// add prevention to array list to send
		List<Prevention> localPreventions = preventionDao.findNotDeletedByDemographicId(demographicId);
		for (Prevention localPrevention : localPreventions) {
			MiscUtils.checkShutdownSignaled();

			if (!providerIdsInFacility.contains(localPrevention.getCreatorProviderNo())) continue;

			CachedDemographicPrevention cachedDemographicPrevention = new CachedDemographicPrevention();
			cachedDemographicPrevention.setCaisiDemographicId(demographicId);
			cachedDemographicPrevention.setCaisiProviderId(localPrevention.getProviderNo());

			{
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(localPrevention.getId());
				cachedDemographicPrevention.setFacilityPreventionPk(pk);
			}

			cachedDemographicPrevention.setNextDate(localPrevention.getNextDate());
			cachedDemographicPrevention.setPreventionDate(localPrevention.getPreventionDate());

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

		if (preventionsToSend.size() > 0) service.setCachedDemographicPreventions(preventionsToSend, preventionExtsToSend);
	}

	private void pushDemographicNotes(Facility facility, DemographicWs service, Integer demographicId) {
		logger.debug("pushing demographicNotes facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());
		HashSet<Integer> programIds=new HashSet<Integer>();
		for (Program program : programs) programIds.add(program.getId());
		
		List<CaseManagementNote> localNotes = caseManagementNoteDAO.getNotesByDemographic(demographicId.toString());
		
		String issueType = OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE");
		if (issueType!=null) issueType=issueType.toUpperCase();

		ArrayList<CachedDemographicNote> notesToSend = new ArrayList<CachedDemographicNote>();
		
		for (CaseManagementNote localNote : localNotes)
		{
			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || !programIds.contains(Integer.parseInt(localNote.getProgram_no()))) continue;

				CachedDemographicNote noteToSend=makeRemoteNote(localNote, issueType);
				notesToSend.add(noteToSend);
			} catch (NumberFormatException e) {
	            logger.error("Unexpected error. ProgramNo="+localNote.getProgram_no(), e);
		            }
		}

		//add group notes as well.
		logger.info("checking for group notes for " + demographicId);
		List<GroupNoteLink> noteLinks = groupNoteDao.findLinksByDemographic(demographicId);
		logger.info("found " + noteLinks.size() + " group notes for " + demographicId);
		for(GroupNoteLink noteLink:noteLinks) {
			int orginalNoteId = noteLink.getNoteId();
			CaseManagementNote localNote = caseManagementNoteDAO.getNote(Long.valueOf(orginalNoteId));
			localNote.setDemographic_no(String.valueOf(demographicId));
			
			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || !programIds.contains(Integer.parseInt(localNote.getProgram_no()))) continue;
	
				CachedDemographicNote noteToSend=makeRemoteNote(localNote, issueType);
				notesToSend.add(noteToSend);
				logger.info("adding group note to send");
			} catch (NumberFormatException e) {
	            logger.error("Unexpected error. ProgramNo="+localNote.getProgram_no(), e);
            }	
			
		}
		
		
//		if (notesToSend.size()>0) service.setCachedDemographicNotes(notesToSend);
		writeToIntegrator(notesToSend, service, CachedDemographicNote.class.getName());
	}

	private CachedDemographicNote makeRemoteNote(CaseManagementNote localNote, String issueType) {
		
		CachedDemographicNote note=new CachedDemographicNote();
		
		CachedDemographicNoteCompositePk pk=new CachedDemographicNoteCompositePk();
		pk.setUuid(localNote.getUuid() + ":" + localNote.getDemographic_no());
		note.setCachedDemographicNoteCompositePk(pk);
		
		note.setCaisiDemographicId(Integer.parseInt(localNote.getDemographic_no()));
		note.setCaisiProgramId(Integer.parseInt(localNote.getProgram_no()));
		note.setEncounterType(localNote.getEncounter_type());
		note.setNote(localNote.getNote());
		note.setObservationCaisiProviderId(localNote.getProviderNo());
		note.setObservationDate(localNote.getObservation_date());
		note.setRole(localNote.getRoleName());
		note.setSigningCaisiProviderId(localNote.getSigning_provider_no());
		note.setUpdateDate(localNote.getUpdate_date());

		List<NoteIssue> issues=note.getIssues();
		List<CaseManagementIssue> localIssues=caseManagementIssueNotesDao.getNoteIssues(localNote.getId().intValue());
		for (CaseManagementIssue caseManagementIssue : localIssues)
		{
			long issueId=caseManagementIssue.getIssue_id();
			Issue localIssue = issueDao.getIssue(issueId);

			NoteIssue noteIssue=new NoteIssue();
			if ("ICD10".equalsIgnoreCase(issueType)) noteIssue.setCodeType(CodeType.ICD_10); // temporary hard code hack till we sort this out
			noteIssue.setIssueCode(localIssue.getCode());
			issues.add(noteIssue);
		}
		
	    return(note);
    }

	private void pushDemographicDrugs(Facility facility, List<String> providerIdsInFacility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicDrugss facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Drug> drugs = drugDao.findByDemographicIdOrderByDate(demographicId, null);
		ArrayList<CachedDemographicDrug> drugsToSend = new ArrayList<CachedDemographicDrug>();
		if (drugs != null) {
			for (Drug drug : drugs) {
				MiscUtils.checkShutdownSignaled();

				if (!providerIdsInFacility.contains(drug.getProviderNo())) continue;

				CachedDemographicDrug cachedDemographicDrug = new CachedDemographicDrug();

				cachedDemographicDrug.setArchived(drug.isArchived());
				cachedDemographicDrug.setArchivedReason(drug.getArchivedReason());
				cachedDemographicDrug.setArchivedDate(drug.getArchivedDate());
				cachedDemographicDrug.setAtc(drug.getAtc());
				cachedDemographicDrug.setBrandName(drug.getBrandName());
				cachedDemographicDrug.setCaisiDemographicId(drug.getDemographicId());
				cachedDemographicDrug.setCaisiProviderId(drug.getProviderNo());
				cachedDemographicDrug.setCreateDate(drug.getCreateDate());
				cachedDemographicDrug.setCustomInstructions(drug.isCustomInstructions());
				cachedDemographicDrug.setCustomName(drug.getCustomName());
				cachedDemographicDrug.setDosage(drug.getDosage());
				cachedDemographicDrug.setDrugForm(drug.getDrugForm());
				cachedDemographicDrug.setDuration(drug.getDuration());
				cachedDemographicDrug.setDurUnit(drug.getDurUnit());
				cachedDemographicDrug.setEndDate(drug.getEndDate());
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(drug.getId());
				cachedDemographicDrug.setFacilityIdIntegerCompositePk(pk);
				cachedDemographicDrug.setFreqCode(drug.getFreqCode());
				cachedDemographicDrug.setGenericName(drug.getGenericName());
				cachedDemographicDrug.setLastRefillDate(drug.getLastRefillDate());
				cachedDemographicDrug.setLongTerm(drug.getLongTerm());
				cachedDemographicDrug.setMethod(drug.getMethod());
				cachedDemographicDrug.setNoSubs(drug.isNoSubs());
				cachedDemographicDrug.setPastMed(drug.getPastMed());
				cachedDemographicDrug.setPatientCompliance(drug.getPatientCompliance());
				cachedDemographicDrug.setPrn(drug.isPrn());
				cachedDemographicDrug.setQuantity(drug.getQuantity());
				cachedDemographicDrug.setRegionalIdentifier(drug.getRegionalIdentifier());
				cachedDemographicDrug.setRepeats(drug.getRepeat());
				cachedDemographicDrug.setRoute(drug.getRoute());
				cachedDemographicDrug.setRxDate(drug.getRxDate());
                                if (drug.getScriptNo()!=null) cachedDemographicDrug.setScriptNo(drug.getScriptNo());
				cachedDemographicDrug.setSpecial(drug.getSpecial());
				cachedDemographicDrug.setTakeMax(drug.getTakeMax());
				cachedDemographicDrug.setTakeMin(drug.getTakeMin());
				cachedDemographicDrug.setUnit(drug.getUnit());
				cachedDemographicDrug.setUnitName(drug.getUnitName());

				drugsToSend.add(cachedDemographicDrug);
			}
		}

//		if (drugsToSend.size()>0) demographicService.setCachedDemographicDrugs(drugsToSend);
		writeToIntegrator(drugsToSend, demographicService, CachedDemographicDrug.class.getName());
	}

	private void pushAppointments(Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
            logger.debug("pushing appointments facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            List<OscarAppointment> appointments = appointmentDao.getAllByDemographicNo(demographicId);
            if (appointments.size()==0) return;

            ArrayList<CachedAppointment> cachedAppointments = new ArrayList<CachedAppointment>();
            for (OscarAppointment appointment : appointments) {
                MiscUtils.checkShutdownSignaled();

                CachedAppointment cachedAppointment = new CachedAppointment();
                FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                facilityIdIntegerCompositePk.setCaisiItemId(appointment.getAppointment_no());
                cachedAppointment.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                cachedAppointment.setAppointmentDate(appointment.getAppointment_date());
                cachedAppointment.setCaisiDemographicId(demographicId);
                cachedAppointment.setCaisiProviderId(appointment.getProvider_no());
                cachedAppointment.setCreateDatetime(appointment.getCreatedatetime());
                cachedAppointment.setEndTime(appointment.getEnd_time());
                cachedAppointment.setLocation(appointment.getLocation());
                cachedAppointment.setNotes(appointment.getNotes());
                cachedAppointment.setReason(appointment.getReason());
                cachedAppointment.setRemarks(appointment.getRemarks());
                cachedAppointment.setResources(appointment.getResources());
                cachedAppointment.setStartTime(appointment.getStart_time());
                cachedAppointment.setStatus(appointment.getStatus());
                cachedAppointment.setStyle(appointment.getStyle());
                cachedAppointment.setType(appointment.getType());
                cachedAppointment.setUpdateDatetime(appointment.getUpdatedatetime());

                cachedAppointments.add(cachedAppointment);
            }
//			if (cachedAppointments.size()>0) demographicService.setCachedAppointments(cachedAppointments);
			writeToIntegrator(cachedAppointments, demographicService, CachedAppointment.class.getName());
        }

        private void pushDxresearchs(Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
            logger.debug("pushing dxresearchs facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            List<DxResearch> dxresearchs = dxresearchDao.getByDemographicNo(demographicId);
            if (dxresearchs.size()==0) return;

            ArrayList<CachedDxresearch> cachedDxresearchs = new ArrayList<CachedDxresearch>();
            for (DxResearch dxresearch : dxresearchs) {
                MiscUtils.checkShutdownSignaled();

                CachedDxresearch cachedDxresearch = new CachedDxresearch();
                FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                facilityIdIntegerCompositePk.setCaisiItemId(dxresearch.getId().intValue());
                cachedDxresearch.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                cachedDxresearch.setCaisiDemographicId(demographicId);
                cachedDxresearch.setDxresearchCode(dxresearch.getCode());
                cachedDxresearch.setCodingSystem(dxresearch.getCodingSystem());
                cachedDxresearch.setStartDate(dxresearch.getStartDate());
                cachedDxresearch.setUpdateDate(dxresearch.getUpdateDate());
                cachedDxresearch.setStatus(dxresearch.getStatus());

                cachedDxresearchs.add(cachedDxresearch);
            }
//			if (cachedDxresearchs.size()>0) demographicService.setCachedDxresearch(cachedDxresearchs);
			writeToIntegrator(cachedDxresearchs, demographicService, CachedDxresearch.class.getName());
        }

        private void pushBillingItems(Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
            logger.debug("pushing billingitems facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            List<BillingOnCHeader1> billingCh1s = billingOnItemDao.getCh1ByDemographicNo(demographicId);
            if (billingCh1s.size()==0) return;

            ArrayList<CachedBillingOnItem> cachedBillingOnItems = new ArrayList<CachedBillingOnItem>();
            for (BillingOnCHeader1 billingCh1 : billingCh1s) {
                List<BillingOnItem> billingItems = billingOnItemDao.getBillingItemByCh1Id(billingCh1.getId());
                for (BillingOnItem billingItem : billingItems) {
                    MiscUtils.checkShutdownSignaled();

                    CachedBillingOnItem cachedBillingOnItem = new CachedBillingOnItem();
                    FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                    facilityIdIntegerCompositePk.setCaisiItemId(billingItem.getId());
                    cachedBillingOnItem.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                    cachedBillingOnItem.setCaisiDemographicId(demographicId);
                    cachedBillingOnItem.setCaisiProviderId(billingCh1.getProvider_no());
                    cachedBillingOnItem.setApptProviderId(billingCh1.getApptProvider_no());
                    cachedBillingOnItem.setAsstProviderId(billingCh1.getAsstProvider_no());
                    cachedBillingOnItem.setAppointmentId(billingCh1.getAppointment_no());
                    cachedBillingOnItem.setDx(billingItem.getDx());
                    cachedBillingOnItem.setDx1(billingItem.getDx1());
                    cachedBillingOnItem.setDx2(billingItem.getDx2());
                    cachedBillingOnItem.setServiceCode(billingItem.getService_code());
                    cachedBillingOnItem.setServiceDate(billingItem.getService_date());
                    cachedBillingOnItem.setStatus(billingItem.getStatus());

                    cachedBillingOnItems.add(cachedBillingOnItem);
                }
            }
//			if (cachedBillingOnItems.size()>0) demographicService.setCachedBillingOnItem(cachedBillingOnItems);
			writeToIntegrator(cachedBillingOnItems, demographicService, CachedBillingOnItem.class.getName());
        }

        private void pushEforms(Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
            logger.debug("pushing eforms facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            List<EFormData> eformDatas = eFormDataDao.findByDemographicId(demographicId);
            if (eformDatas.size()==0) return;

            ArrayList<CachedEformData> cachedEformDatas = new ArrayList<CachedEformData>();
            for (EFormData eformData : eformDatas) {
                MiscUtils.checkShutdownSignaled();

                CachedEformData cachedEformData = new CachedEformData();
                FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                facilityIdIntegerCompositePk.setCaisiItemId(eformData.getId());
                cachedEformData.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                cachedEformData.setCaisiDemographicId(demographicId);
                cachedEformData.setFormDate(eformData.getFormDate());
                cachedEformData.setFormTime(eformData.getFormTime());
                cachedEformData.setFormId(eformData.getFormId());
                cachedEformData.setFormName(eformData.getFormName());
                cachedEformData.setFormData(eformData.getFormData());
                cachedEformData.setSubject(eformData.getSubject());
                cachedEformData.setStatus(eformData.isCurrent());
                cachedEformData.setFormProvider(eformData.getProviderNo());

                cachedEformDatas.add(cachedEformData);
            }

            List<EFormValue> eFormValues = eFormValueDao.findByDemographicId(demographicId);
            if (eFormValues.size()==0) return;

            ArrayList<CachedEformValue> cachedEformValues = new ArrayList<CachedEformValue>();
            for (EFormValue eFormValue : eFormValues) {
                MiscUtils.checkShutdownSignaled();

                CachedEformValue cachedEformValue = new CachedEformValue();
                FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                facilityIdIntegerCompositePk.setCaisiItemId(eFormValue.getId());
                cachedEformValue.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                cachedEformValue.setCaisiDemographicId(demographicId);
                cachedEformValue.setFormId(eFormValue.getFormId());
                cachedEformValue.setFormDataId(eFormValue.getFormDataId());
                cachedEformValue.setVarName(eFormValue.getVarName());
                cachedEformValue.setVarValue(eFormValue.getVarValue());

                cachedEformValues.add(cachedEformValue);
            }

//			if (cachedEformDatas.size()>0) demographicService.setCachedEformData(cachedEformDatas);
//			if (cachedEformValues.size()>0) demographicService.setCachedEformValues(cachedEformValues);
			writeToIntegrator(cachedEformDatas, demographicService, CachedEformData.class.getName());
			writeToIntegrator(cachedEformValues, demographicService, CachedEformValue.class.getName());
        }

	private void pushMeasurements(Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
            logger.debug("pushing measurements facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            List<Measurements> measurements = measurementsDao.getMeasurementsByDemo(demographicId);
            if (measurements.size()==0) return;

            ArrayList<CachedMeasurement>     cachedMeasurements     = new ArrayList<CachedMeasurement>();
            ArrayList<CachedMeasurementExt>  cachedMeasurementExts  = new ArrayList<CachedMeasurementExt>();
            ArrayList<CachedMeasurementType> cachedMeasurementTypes = new ArrayList<CachedMeasurementType>();
            ArrayList<CachedMeasurementMap>  cachedMeasurementMaps  = new ArrayList<CachedMeasurementMap>();

            for (Measurements measurement : measurements) {
                MiscUtils.checkShutdownSignaled();

                CachedMeasurement cachedMeasurement = new CachedMeasurement();
                FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
                facilityIdIntegerCompositePk.setCaisiItemId(measurement.getId());
                cachedMeasurement.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

                cachedMeasurement.setCaisiDemographicId(demographicId);
                cachedMeasurement.setCaisiProviderId(measurement.getProviderNo());
                cachedMeasurement.setComments(measurement.getComments());
                cachedMeasurement.setDataField(measurement.getDataField());
                cachedMeasurement.setDateEntered(measurement.getDateEntered());
                cachedMeasurement.setDateObserved(measurement.getDateObserved());
                cachedMeasurement.setMeasuringInstruction(measurement.getMeasuringInstruction());
                cachedMeasurement.setType(measurement.getType());

                cachedMeasurements.add(cachedMeasurement);

                List<MeasurementsExt> measurementExts = measurementsExtDao.getMeasurementsExtByMeasurementId(measurement.getId());
                for (MeasurementsExt measurementExt : measurementExts) {
                    MiscUtils.checkShutdownSignaled();
                    CachedMeasurementExt cachedMeasurementExt = new CachedMeasurementExt();
                    FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
                    fidIntegerCompositePk.setCaisiItemId(measurementExt.getId());
                    cachedMeasurementExt.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

                    cachedMeasurementExt.setMeasurementId(measurementExt.getMeasurementId());
                    cachedMeasurementExt.setKeyval(measurementExt.getKeyVal());
                    cachedMeasurementExt.setVal(measurementExt.getVal());

                    cachedMeasurementExts.add(cachedMeasurementExt);
                }

                List<Measurementtype> measurementTypes = measurementTypeDao.getByType(measurement.getType());
                for (Measurementtype measurementType : measurementTypes) {
                    MiscUtils.checkShutdownSignaled();
                    if (inList(measurementType, cachedMeasurementTypes)) continue;

                    CachedMeasurementType cachedMeasurementType = new CachedMeasurementType();
                    FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
                    fidIntegerCompositePk.setCaisiItemId(measurementType.getId());
                    cachedMeasurementType.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

                    cachedMeasurementType.setType(measurementType.getType());
                    cachedMeasurementType.setTypeDescription(measurementType.getTypeDescription());
                    cachedMeasurementType.setMeasuringInstruction(measurementType.getMeasuringInstruction());

                    cachedMeasurementTypes.add(cachedMeasurementType);

                }

                List<Measurementmap> measurementMaps = measurementMapDao.getMapsByIdent(measurement.getType());
                for (Measurementmap measurementMap : measurementMaps) {
                    if (inList(measurementMap, cachedMeasurementMaps)) continue;

                    CachedMeasurementMap cachedMeasurementMap = new CachedMeasurementMap();
                    FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
                    fidIntegerCompositePk.setCaisiItemId(measurementMap.getId());
                    cachedMeasurementMap.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

                    cachedMeasurementMap.setIdentCode(measurementMap.getIdentCode());
                    cachedMeasurementMap.setLoincCode(measurementMap.getLoincCode());
                    cachedMeasurementMap.setName(measurementMap.getName());
                    cachedMeasurementMap.setLabType(measurementMap.getLabType());

                    cachedMeasurementMaps.add(cachedMeasurementMap);

                }
            }
/*
            if (cachedMeasurements.size()>0) demographicService.setCachedMeasurements(cachedMeasurements);
            if (cachedMeasurementExts.size()>0) demographicService.setCachedMeasurementExts(cachedMeasurementExts);
            if (cachedMeasurementTypes.size()>0) demographicService.setCachedMeasurementTypes(cachedMeasurementTypes);
            if (cachedMeasurementMaps.size()>0) demographicService.setCachedMeasurementMaps(cachedMeasurementMaps);
 */
			writeToIntegrator(cachedMeasurements, demographicService, CachedMeasurement.class.getName());
			writeToIntegrator(cachedMeasurementExts, demographicService, CachedMeasurementExt.class.getName());
			writeToIntegrator(cachedMeasurementTypes, demographicService, CachedMeasurementType.class.getName());
			writeToIntegrator(cachedMeasurementMaps, demographicService, CachedMeasurementMap.class.getName());
        }

        private boolean inList(Measurementtype measurementType, List<CachedMeasurementType> cachedMeasurementTypes) {
                if (measurementType==null || cachedMeasurementTypes==null || cachedMeasurementTypes.size()==0) {
                    return false;
                }
                boolean retrn = false;
                for (CachedMeasurementType cmType : cachedMeasurementTypes) {
                    if (measurementType.getId()==cmType.getFacilityIdIntegerCompositePk().getCaisiItemId()) {
                        retrn = true;
                        break;
                    }
                }
                return retrn;
        }

        private boolean inList(Measurementmap measurementMap, List<CachedMeasurementMap> cachedMeasurementMaps) {
                if (measurementMap==null || cachedMeasurementMaps==null || cachedMeasurementMaps.size()==0) {
                    return false;
                }
                boolean retrn = false;
                for (CachedMeasurementMap cmMap : cachedMeasurementMaps) {
                    if (measurementMap.getId()==cmMap.getFacilityIdIntegerCompositePk().getCaisiItemId()) {
                        retrn = true;
                        break;
                    }
                }
                return retrn;
        }

        private void writeToIntegrator(ArrayList dataList, Object ws, String dataType) {
            ArrayList partList = new ArrayList();
            for (int i=0; i<dataList.size(); i++) {
                partList.add(dataList.get(i));
                if (i%50==0 || i+1==dataList.size()) {

					if (dataType.equals(ProviderTransfer.class.getName()))
							((ProviderWs) ws).setCachedProviders(partList);
					else if (dataType.equals(CachedDemographicIssue.class.getName()))
							((DemographicWs) ws).setCachedDemographicIssues(partList);
					else if (dataType.equals(CachedDemographicNote.class.getName()))
							((DemographicWs) ws).setCachedDemographicNotes(partList);
					else if (dataType.equals(CachedDemographicDrug.class.getName()))
							((DemographicWs) ws).setCachedDemographicDrugs(partList);
					else if (dataType.equals(CachedAdmission.class.getName()))
							((DemographicWs) ws).setCachedAdmissions(partList);
					else if (dataType.equals(CachedAppointment.class.getName()))
							((DemographicWs) ws).setCachedAppointments(partList);
					else if (dataType.equals(CachedMeasurement.class.getName()))
							((DemographicWs) ws).setCachedMeasurements(partList);
					else if (dataType.equals(CachedMeasurementExt.class.getName()))
							((DemographicWs) ws).setCachedMeasurementExts(partList);
					else if (dataType.equals(CachedMeasurementType.class.getName()))
							((DemographicWs) ws).setCachedMeasurementTypes(partList);
					else if (dataType.equals(CachedMeasurementMap.class.getName()))
							((DemographicWs) ws).setCachedMeasurementMaps(partList);
					else if (dataType.equals(CachedDxresearch.class.getName()))
							((DemographicWs) ws).setCachedDxresearch(partList);
					else if (dataType.equals(CachedBillingOnItem.class.getName()))
							((DemographicWs) ws).setCachedBillingOnItem(partList);
					else if (dataType.equals(CachedEformData.class.getName()))
							((DemographicWs) ws).setCachedEformData(partList);
					else if (dataType.equals(CachedEformValue.class.getName()))
							((DemographicWs) ws).setCachedEformValues(partList);

                    partList.clear();
                }
            }
        }
}
