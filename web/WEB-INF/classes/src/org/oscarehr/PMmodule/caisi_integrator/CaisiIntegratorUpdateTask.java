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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
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
import org.oscarehr.caisi_integrator.ws.ProviderWs;
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
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.ShutdownException;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Logger logger = LogManager.getLogger(CaisiIntegratorUpdateTask.class);

	private static final String INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY = "INTEGRATOR_UPDATE_PERIOD";

	private static Timer timer = new Timer("CaisiIntegratorUpdateTask Timer", true);
	private static TimerTask timerTask = null;

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

	static {
		// ensure cxf uses log4j
		System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Log4jLogger");
	}

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
			DbConnectionFilter.releaseThreadLocalDbConnection();

			logger.debug("CaisiIntegratorUpdateTask finished");
		}
	}

	public void pushAllFacilities() throws ShutdownException {
		List<Facility> facilities = facilityDao.findAll(null);

		for (Facility facility : facilities) {
			MiscUtils.checkShutdownSignaled();

			try {
				if (!facility.isDisabled() && facility.isIntegratorEnabled()) {
					pushAllFacilityData(facility);
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

	private void pushAllFacilityData(Facility facility) throws IOException, IllegalAccessException, InvocationTargetException, ShutdownException {
		logger.debug("Pushing data for facility : " + facility.getId() + " : " + facility.getName());

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

		// get the time here so there's a slight over lap in actual runtime and
		// activity time, other wise you'll have a gap, better to unnecessarily
		// send a few more records than to miss some.
		Date currentPushTime = new Date();

		// do all the sync work
		// in theory sync should only send changed data, but currently due to
		// the lack of proper data models, we don't have a reliable timestamp on when things change so we just push everything, highly inefficient but it works until we fix the
		// data model.
		pushFacility();
		pushPrograms(facility);
		pushProviders(facility);
		pushAllDemographics();

		// update late push time only if an exception didn't occur
		// re-get the facility as the sync time could be very long and changes
		// may have been made to the facility.
		facility = facilityDao.find(facility.getId());
		facility.setIntegratorLastPushTime(currentPushTime);
		facilityDao.merge(facility);
	}

	private void pushFacility() throws MalformedURLException, IllegalAccessException, InvocationTargetException {
		Facility facility=LoggedInInfo.loggedInInfo.get().currentFacility;
		
		if (facility.getIntegratorLastPushTime() == null || facility.getLastUpdated().after(facility.getIntegratorLastPushTime())) {
			logger.debug("pushing facility record");

			CachedFacility cachedFacility = new CachedFacility();
			BeanUtils.copyProperties(cachedFacility, facility);

			FacilityWs service = CaisiIntegratorManager.getFacilityWs();
			service.setMyFacility(cachedFacility);
		}
		else
		{
			logger.debug("skipping facility record (no changes)");
		}			
	}

	private void pushPrograms(Facility facility) throws MalformedURLException, IllegalAccessException, InvocationTargetException, ShutdownException {
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

		ProgramWs service = CaisiIntegratorManager.getProgramWs(facility.getId());
		service.setCachedPrograms(cachedPrograms);
	}

	private void pushProviders(Facility facility) throws MalformedURLException, IllegalAccessException, InvocationTargetException, ShutdownException {
		List<String> providerIds = ProviderDao.getProviderIds(facility.getId());

		ArrayList<CachedProvider> cachedProviders = new ArrayList<CachedProvider>();

		for (String providerId : providerIds) {
			MiscUtils.checkShutdownSignaled();
			logger.debug("Adding provider " + providerId + " for " + facility.getName());
			Provider provider = providerDao.getProvider(providerId);

			CachedProvider cachedProvider = new CachedProvider();

			BeanUtils.copyProperties(cachedProvider, provider);

			FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
			pk.setCaisiItemId(provider.getProviderNo());
			cachedProvider.setFacilityIdStringCompositePk(pk);

			cachedProviders.add(cachedProvider);
		}

		ProviderWs service = CaisiIntegratorManager.getProviderWs(facility.getId());
		service.setCachedProviders(cachedProviders);
	}

	private void pushAllDemographics() throws MalformedURLException, ShutdownException {
		Facility facility=LoggedInInfo.loggedInInfo.get().currentFacility;
		
		List<Integer> demographicIds = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		DemographicWs demographicService = CaisiIntegratorManager.getDemographicWs();
		List<Program> programsInFacility = programDao.getProgramsByFacilityId(facility.getId());
		List<String> providerIdsInFacility = ProviderDao.getProviderIds(facility.getId());

		for (Integer demographicId : demographicIds) {
			logger.debug("pushing demographic facilityId:" + facility.getId() + ", demographicId:" + demographicId);
			MiscUtils.checkShutdownSignaled();

			try {
				pushDemographic(demographicService, demographicId);
				// it's safe to set the consent later so long as we default it to none when we send the original demographic data in the line above.
				pushDemographicConsent(facility, demographicService, demographicId);
				pushDemographicIssues(facility, programsInFacility, demographicService, demographicId);
				pushDemographicPreventions(facility, providerIdsInFacility, demographicService, demographicId);
				pushDemographicNotes(facility, demographicService, demographicId);
				pushDemographicDrugs(facility, providerIdsInFacility, demographicService, demographicId);
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

	private void pushDemographic(DemographicWs service, Integer demographicId) throws IllegalAccessException, InvocationTargetException {
		DemographicTransfer demographicTransfer = new DemographicTransfer();

		// set demographic info
		Demographic demographic = demographicDao.getDemographicById(demographicId);

		BeanUtils.copyProperties(demographicTransfer, demographic);

		demographicTransfer.setCaisiDemographicId(demographic.getDemographicNo());
		demographicTransfer.setBirthDate(demographic.getBirthDay().getTime());

		demographicTransfer.setHinVersion(demographic.getVer());
		
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

		// send the request
		service.setDemographic(demographicTransfer);
	}

	private void pushDemographicConsent(Facility facility, DemographicWs demographicService, Integer demographicId)  {

		// find the latest relvent consent that needs to be pushed.
		List<IntegratorConsent> tempConsents=integratorConsentDao.findByFacilityAndDemographic(facility.getId(), demographicId);

		for (IntegratorConsent tempConsent : tempConsents)
		{
			if (tempConsent.getCreatedDate().before(facility.getIntegratorLastPushTime())) break;

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

			issues.add(cachedDemographicIssue);
		}

		if (issues.size() > 0) service.setCachedDemographicIssues(issues);
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

				// if it's a note we've already sent ignore it
				if (facility.getIntegratorLastPushTime()!=null && facility.getIntegratorLastPushTime().after(localNote.getUpdate_date())) continue;
				
				CachedDemographicNote noteToSend=makeRemoteNote(localNote, issueType);
				notesToSend.add(noteToSend);
			} catch (NumberFormatException e) {
	            logger.error("Unexpected error. ProgramNo="+localNote.getProgram_no(), e);
            }
		}
		
		if (notesToSend.size()>0) service.setCachedDemographicNotes(notesToSend);
	}

	private CachedDemographicNote makeRemoteNote(CaseManagementNote localNote, String issueType) {
		
		CachedDemographicNote note=new CachedDemographicNote();
		
		CachedDemographicNoteCompositePk pk=new CachedDemographicNoteCompositePk();
		pk.setUuid(localNote.getUuid());
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

	private void pushDemographicDrugs(Facility facility, List<String> providerIdsInFacility, DemographicWs demogrpahicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicDrugss facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Drug> drugs = drugDao.findByDemographicIdOrderByDate(demographicId, null);
		ArrayList<CachedDemographicDrug> drugsToSend = new ArrayList<CachedDemographicDrug>();
		if (drugs != null) {
			for (Drug drug : drugs) {
				MiscUtils.checkShutdownSignaled();

				if (!providerIdsInFacility.contains(drug.getProviderNo())) continue;

				CachedDemographicDrug cachedDemographicDrug = new CachedDemographicDrug();

				cachedDemographicDrug.setArchived(drug.isArchived());
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
				cachedDemographicDrug.setScriptNo(drug.getScriptNo());
				cachedDemographicDrug.setSpecial(drug.getSpecial());
				cachedDemographicDrug.setTakeMax(drug.getTakeMax());
				cachedDemographicDrug.setTakeMin(drug.getTakeMin());
				cachedDemographicDrug.setUnit(drug.getUnit());
				cachedDemographicDrug.setUnitName(drug.getUnitName());

				drugsToSend.add(cachedDemographicDrug);
			}
		}

		if (drugsToSend.size() > 0) demogrpahicService.setCachedDemographicDrugs(drugsToSend);
	}

}
