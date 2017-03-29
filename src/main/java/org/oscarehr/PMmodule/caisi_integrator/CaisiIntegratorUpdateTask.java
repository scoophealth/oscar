/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.caisi_integrator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.web.forms.IntegratorPausedException;
import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedAppointment;
import org.oscarehr.caisi_integrator.ws.CachedBillingOnItem;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedDemographicForm;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult;
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
import org.oscarehr.caisi_integrator.ws.FacilityIdLabResultCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.Gender;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
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
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.dao.IntegratorControlDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementMapDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.Consent;
import org.oscarehr.common.model.ConsentType;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.IntegratorConsent.ConsentStatus;

import org.oscarehr.common.model.IntegratorProgress;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.labs.LabIdAndType;
import org.oscarehr.managers.IntegratorPushManager;
import org.oscarehr.managers.PatientConsentManager;
import org.oscarehr.util.BenchmarkTimer;
import org.oscarehr.util.CxfClientUtilsOld;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.MiscUtilsOld;
import org.oscarehr.util.ShutdownException;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.springframework.beans.BeanUtils;
import org.w3c.dom.Document;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.EDocUtil.EDocSort;
import oscar.form.FrmLabReq07Record;
import oscar.log.LogAction;
import oscar.oscarLab.ca.all.web.LabDisplayHelper;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.DateUtils;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	private static final String INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY = "INTEGRATOR_UPDATE_PERIOD";
	private static final String INTEGRATOR_THROTTLE_DELAY_PROPERTIES_KEY = "INTEGRATOR_THROTTLE_DELAY";
	private static final long INTEGRATOR_THROTTLE_DELAY = Long.parseLong((String) OscarProperties.getInstance().get(INTEGRATOR_THROTTLE_DELAY_PROPERTIES_KEY));
	private static final long SLEEP_ON_ERROR = 300000;
	private static final double SLEEP_ON_ERROR_STEP = 1.5;

	private static boolean ISACTIVE_PATIENT_CONSENT_MODULE = Boolean.FALSE; 

	
	private static Timer timer = new Timer("CaisiIntegratorUpdateTask Timer", true);

	private int numberOfTimesRun = 0;

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
	private PreventionExtDao preventionExtDao = (PreventionExtDao) SpringUtils.getBean("preventionExtDao");
	private DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
	private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
	private AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
	private IntegratorControlDao integratorControlDao = (IntegratorControlDao) SpringUtils.getBean("integratorControlDao");
	private MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
	private MeasurementMapDao measurementMapDao = (MeasurementMapDao) SpringUtils.getBean("measurementMapDao");
	private DxresearchDAO dxresearchDao = (DxresearchDAO) SpringUtils.getBean("dxresearchDAO");
	private BillingONItemDao billingONItemDao = SpringUtils.getBean(BillingONItemDao.class);
	private EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");
	private EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");
	private GroupNoteDao groupNoteDao = (GroupNoteDao) SpringUtils.getBean("groupNoteDao");
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	private MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");
	private AllergyDao allergyDao = (AllergyDao) SpringUtils.getBean("allergyDao");
	private PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	private IntegratorPushManager integratorPushManager = SpringUtils.getBean(IntegratorPushManager.class);

	private UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	private PatientConsentManager patientConsentManager = (PatientConsentManager) SpringUtils.getBean(PatientConsentManager.class);
	private ConsentType consentType;
	
	private static TimerTask timerTask = null;

	private HashSet<Integer> programIds;
	
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

	@Override
	public void run() {
		
		if(isPushDisabled()) {
			logger.warn("skipping push to integrator because it's been disabled from the admin ui (Property table - DisableIntegratorPushes");
			return;	
		}
		
		numberOfTimesRun++;
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
	
		Provider p = providerDao.getProvider(OscarProperties.getInstance().getProperty("INTEGRATOR_USER", "-1"));
		Security security = new Security();
		security.setSecurityNo(0);
		
		loggedInInfo.setLoggedInProvider(p);
		loggedInInfo.setLoggedInSecurity(security);
		
		// If "push all patients that have consented" is set in the Integrator properties
		// this will ensure that only consenting patients will be pushed.
		// Note: the consent module must be activated in the Oscar properties file; and the Integrator Patient Consent program
		// must be set in Provider Properties database table.
		if( OscarProperties.getInstance().getBooleanProperty("USE_NEW_PATIENT_CONSENT_MODULE", "true") &&  userPropertyDao.getProp( UserProperty.INTEGRATOR_PATIENT_CONSENT ) != null ) {
			CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE = "1".equals( userPropertyDao.getProp( UserProperty.INTEGRATOR_PATIENT_CONSENT ).getValue() );
			// consenttype is the consent type from the patient consent manager used for this module .
			consentType = patientConsentManager.getConsentType( UserProperty.INTEGRATOR_PATIENT_CONSENT );
		}
		
		if(p == null) {
			logger.warn("INTEGRATOR_USER doesn't exist..please check properties file");
			return;
		}
		
		logger.debug("CaisiIntegratorUpdateTask starting #" + numberOfTimesRun+"  running as "+loggedInInfo.getLoggedInProvider());

		try {
			pushAllFacilities(loggedInInfo);
		} catch (ShutdownException e) {
			logger.debug("CaisiIntegratorUpdateTask received shutdown notice.");
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();

			logger.debug("CaisiIntegratorUpdateTask finished #" + numberOfTimesRun);
		}
	}

	public void pushAllFacilities(LoggedInInfo loggedInInfo) throws ShutdownException {
		List<Facility> facilities = facilityDao.findAll(true);

		for (Facility facility : facilities) {
			try {
				if (facility.isIntegratorEnabled()) {
					pushAllDataForOneFacility(loggedInInfo, facility);
					findChangedRecordsFromIntegrator(loggedInInfo, facility);
				}
			} catch (WebServiceException e) {
				if (CxfClientUtilsOld.isConnectionException(e)) {
					logger.warn("Error connecting to integrator. " + e.getMessage());
					logger.debug("Error connecting to integrator.", e);
				} else {
					logger.error("Unexpected error.", e);
				}
			} catch (ShutdownException e) {
				throw (e);
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}
	}

	private void pushAllDataForOneFacility(LoggedInInfo loggedInInfo, Facility facility) throws IOException, ShutdownException, IntegratorPausedException {
		logger.info("Start pushing data for facility : " + facility.getId() + " : " + facility.getName());

		// check all parameters are present
		String integratorBaseUrl = facility.getIntegratorUrl();
		String user = facility.getIntegratorUser();
		String password = facility.getIntegratorPassword();

		if (integratorBaseUrl == null || user == null || password == null) {
			logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
			return;
		}

		FacilityWs service = CaisiIntegratorManager.getFacilityWs(loggedInInfo, facility);
		CachedFacility cachedFacility = service.getMyFacility();

		// start at the beginning of time so by default everything is pushed
		Date lastDataUpdated = new Date(0);
		if (cachedFacility != null && cachedFacility.getLastDataUpdate() != null){
			lastDataUpdated = DateUtils.toDate(cachedFacility.getLastDataUpdate());
		}else{
			userPropertyDao.saveProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId(), "1");
		}

		// this needs to be set now, before we do any sends, this will cause anything updated after now to be resent twice but it's better than items being missed that were updated after this started.
		Date currentUpdateDate = new Date();

		//setup this list once for this facility
		List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());
		programIds = new HashSet<Integer>();
		for (Program program : programs) {
			programIds.add(program.getId());
		}
				
		// do all the sync work
		// in theory sync should only send changed data, but currently due to
		// the lack of proper data models, we don't have a reliable timestamp on when things change so we just push everything, highly inefficient but it works until we fix the
		// data model. The last update date is available though as per above...
		pushFacility(facility, lastDataUpdated);
		pushProviders(lastDataUpdated, facility);
		pushPrograms(lastDataUpdated, facility);
		
		// sync the Patient Consent tables. See method signature for details.
		if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
			this.pushPatientConsentTable(loggedInInfo, lastDataUpdated, facility);
		}
		
		pushAllDemographics(loggedInInfo, facility, lastDataUpdated,cachedFacility,programs);
		
		// all things updated successfully
		service.updateMyFacilityLastUpdateDate(DateUtils.toCalendar(currentUpdateDate));

		logger.info("Finished pushing data for facility : " + facility.getId() + " : " + facility.getName());
	}

	private void pushFacility(Facility facility, Date lastDataUpdated) throws MalformedURLException {
		if (facility.getLastUpdated().after(lastDataUpdated)) {
			logger.debug("pushing facility record");

			CachedFacility cachedFacility = new CachedFacility();
			BeanUtils.copyProperties(facility, cachedFacility);

			FacilityWs service = CaisiIntegratorManager.getFacilityWs(null, facility);
			service.setMyFacility(cachedFacility);
		} else {
			logger.debug("skipping facility record, not updated since last push");
		}
	}

	private void pushPrograms(Date lastDataUpdated, Facility facility) throws MalformedURLException, ShutdownException {
		// all are always sent so program deletions have be proliferated.
		//List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());
		List<Integer> programIds = programDao.getRecordsAddedAndUpdatedSinceTime(facility.getId(),lastDataUpdated);
		
		ArrayList<CachedProgram> cachedPrograms = new ArrayList<CachedProgram>();
		ProgramWs service = CaisiIntegratorManager.getProgramWs(null, facility);
		
		for (Integer programId : programIds) {
			Program program = programDao.getProgram(programId);
			
			logger.debug("pushing program : " + program.getId() + ':' + program.getName());
			MiscUtilsOld.checkShutdownSignaled();

			CachedProgram cachedProgram = new CachedProgram();

			BeanUtils.copyProperties(program, cachedProgram);

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setCaisiItemId(program.getId());
			cachedProgram.setFacilityIdIntegerCompositePk(pk);

			try {
				cachedProgram.setGender(Gender.valueOf(program.getManOrWoman().toUpperCase()));
			} catch (Exception e) {
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

		if(cachedPrograms.size()>0) {
			service.setCachedPrograms(cachedPrograms);
			throttleAndChecks();
		}
		
		List<Integer> allProgramIds = programDao.getRecordsByFacilityId(facility.getId());
		if(allProgramIds.size()>0) {
			service.deleteCachedProgramsMissingFromList(allProgramIds);
		}
	}

	private void pushProviders(Date lastDataUpdated, Facility facility) throws MalformedURLException, ShutdownException {
		ProviderWs service = CaisiIntegratorManager.getProviderWs(null, facility);

		List<String> providerIds = providerDao.getRecordsAddedAndUpdatedSinceTime(lastDataUpdated);
		providerIds.addAll(secUserRoleDao.getRecordsAddedAndUpdatedSinceTime(lastDataUpdated));
		
		Set<String> uniqueProviderIds = new HashSet<String>(providerIds);
		
		
		ArrayList<ProviderTransfer> providerTransfers = new ArrayList<ProviderTransfer>();
		int i = 0;
		
		for (String providerId : uniqueProviderIds) {
			logger.debug("Adding provider " + providerId + " for " + facility.getName());

			// copy provider basic data over
			Provider provider = providerDao.getProvider(providerId);
			if (provider == null) continue;

			ProviderTransfer providerTransfer = new ProviderTransfer();
			CachedProvider cachedProvider = new CachedProvider();

			BeanUtils.copyProperties(provider, cachedProvider);

			FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
			pk.setCaisiItemId(provider.getProviderNo());
			cachedProvider.setFacilityIdStringCompositePk(pk);

			providerTransfer.setCachedProvider(cachedProvider);

			// copy roles over
			List<SecUserRole> roles = secUserRoleDao.getUserRoles(providerId);
			for (SecUserRole role : roles) {
				Role integratorRole = IntegratorRoleUtils.getIntegratorRole(role.getRoleName());
				if (integratorRole != null) providerTransfer.getRoles().add(integratorRole);
			}

			providerTransfers.add(providerTransfer);
			
			if((++i % 50) == 0) {
				service.setCachedProviders(providerTransfers);
				throttleAndChecks();
				providerTransfers.clear();
			}
			
		}
		
		if(providerTransfers.size()>0) {
			service.setCachedProviders(providerTransfers);
			throttleAndChecks();
		}
	}
	
	private boolean isFullPush(Facility facility) {
		UserProperty fullPushProp = userPropertyDao.getProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId());
		
		if (OscarProperties.getInstance().isPropertyActive("INTEGRATOR_FORCE_FULL")) {
			fullPushProp.setValue("1");
		}
		
		if (fullPushProp != null && fullPushProp.getValue().equals("1")) {
			return true;
		}
		
		return false;
	}
	
	//all facilities
	private boolean isPushDisabled() {
		UserProperty prop = userPropertyDao.getProp(IntegratorPushManager.DISABLE_INTEGRATOR_PUSH_PROP);
		
		if(prop != null && "true".equals(prop.getValue())) {
			return true;
		}
		return false;
	}
	
	/**
	 * If the patient has not consented to participating in Integrator, remove it from the list of 
	 * demographics to be pushed.
	 * A check for if this action is desired by the user should be done first.
	 */
	private List<Integer> checkPatientConsent( List<Integer> demographicNoList ) {
		
		Set<Integer> consentedSet = new HashSet<Integer>();
		List<Integer> consentedDemographicNoList = new ArrayList<Integer>();
		
		for( int demographicNo : demographicNoList ) {
			if( checkPatientConsent( demographicNo ) ) {
				logger.info( "Adding consented Demographic " + demographicNo + " to the Integrator push list" );
				consentedSet.add( demographicNo );
			}
		}

		consentedDemographicNoList.addAll(consentedSet);
		
		return consentedDemographicNoList;
	}
	
	private boolean checkPatientConsent( int demographicNo ) {
		return patientConsentManager.hasPatientConsented( demographicNo, this.consentType );
	}

	private List<Integer> getDemographicIdsToPush(Facility facility, Date lastDataUpdated, List<Program> programs) {
		
		List<Integer> fullFacilitydemographicIds  = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		
		
		if ( isFullPush(facility) ) {
			logger.info("Integrator pushing ALL demographics");
			
			// check if patient consent module is active and then sort out all patients that 
			// have given consent
			if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
				logger.info("Integrator patient consent is active. Checking demographic list.");				
				return checkPatientConsent( fullFacilitydemographicIds );
			} else {
				return fullFacilitydemographicIds;
			}
						
		} else {
			logger.info("Integrator pushing only changed demographics");
			
			//Make a list of all ids that have a change in one of the subtypes...it's a bunch of queries
			//but it's still a much much faster than looping through all demographics in a Facility, and
			//running the push logic on all it's subtypes.
			Set<Integer> uniqueDemographicIdsWithSomethingNew = new HashSet<Integer>();
			uniqueDemographicIdsWithSomethingNew.addAll(demographicDao.getDemographicIdsAddedSince(lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(integratorConsentDao.findDemographicIdsByFacilitySince(facility.getId(),lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(caseManagementIssueDAO.getIssuesByProgramsSince(lastDataUpdated,programs));
			uniqueDemographicIdsWithSomethingNew.addAll(caseManagementNoteDAO.getNotesByFacilitySince(lastDataUpdated,programs));
			uniqueDemographicIdsWithSomethingNew.addAll(admissionDao.getAdmissionsByFacilitySince(facility.getId(), lastDataUpdated));
			
			//i can't limit these ones by facility, so this may return more patients than will ever get sent.
			uniqueDemographicIdsWithSomethingNew.addAll(this.preventionDao.findDemographicIdsAfterDatetime(lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(drugDao.findDemographicIdsUpdatedAfterDate(lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(this.appointmentDao.getAllDemographicNoSince(lastDataUpdated, programs));
			uniqueDemographicIdsWithSomethingNew.addAll(measurementDao.findDemographicIdsUpdatedAfterDate(lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(this.dxresearchDao.getByDemographicNoSince( lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(this.billingONItemDao.getDemographicNoSince( lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(this.eFormDataDao.findemographicIdSinceLastDate( lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(this.allergyDao.findDemographicIdsUpdatedAfterDate(lastDataUpdated));
			uniqueDemographicIdsWithSomethingNew.addAll(EDocUtil.listDemographicIdsSince(lastDataUpdated));
			
			try {
				uniqueDemographicIdsWithSomethingNew.addAll(FrmLabReq07Record.getDemogaphicIdsSince(lastDataUpdated));
			}catch(SQLException e) {
				logger.warn("problem with getting latest labreq07s",e);
			}
			
			uniqueDemographicIdsWithSomethingNew.addAll(patientLabRoutingDao.findDemographicIdsSince(lastDataUpdated));
			
		
			//handle deletes - backup.
			//so if a issue or something else that uses hard deletes gets deleted, the audit should have picked it up.
			uniqueDemographicIdsWithSomethingNew.addAll(DemographicDao.getDemographicIdsAlteredSinceTime(lastDataUpdated));

			Iterator<Integer> demoIterator = uniqueDemographicIdsWithSomethingNew.iterator();
			
	        while(demoIterator.hasNext()){ //Verify that the demographic is in the Facility
	                Integer demo = demoIterator.next();
	                if( ! fullFacilitydemographicIds.contains(demo) ){
	                	demoIterator.remove();
	                } 
	                
	                else if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE && ! checkPatientConsent( demo ) ) {
	    				logger.info("Integrator patient consent is active. Checking demographic list of changed demographics");				
	    				demoIterator.remove();
	    			}
	        }
			
	        if( isFullPush(facility) ){
				   userPropertyDao.saveProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId(), "0");
			}
			
			return(new ArrayList<Integer>(uniqueDemographicIdsWithSomethingNew));
			
		}
	}

	private void pushAllDemographics(LoggedInInfo loggedInInfo, Facility facility,Date lastDataUpdated, 
			CachedFacility cachedFacility, List<Program> programs) 
			throws MalformedURLException, ShutdownException, IntegratorPausedException {
		
		List<Integer> demographicIds = getDemographicIdsToPush(facility,lastDataUpdated, programs);

		//populate the DB with the information about this run. Update as we go along.
		//When complete, then set some status to show that the job is done.
		IntegratorProgress ip = null;
		if (isFullPush(facility)) {
			if((ip=integratorPushManager.getCurrentlyRunning()) != null) {
				//continue a previously running one
				demographicIds = integratorPushManager.findOutstandingDemographicNos(ip);
			} else {
				//insert demographic ids into the progress tables, and mark job
				ip = integratorPushManager.createNewPush(demographicIds);
				if(ip != null) {
					logger.info("marked start of full push by creating " + ip);
				}
			}
		} 
				
		long currentSleepOnErrorTime = SLEEP_ON_ERROR;
		
		DemographicWs demographicService = CaisiIntegratorManager.getDemographicWs(null, facility);
		List<Program> programsInFacility = programDao.getProgramsByFacilityId(facility.getId());
		List<String> providerIdsInFacility = providerDao.getProviderIds(facility.getId());

		long startTime = System.currentTimeMillis();
		int demographicPushCount = 0;
		for (Integer demographicId : demographicIds) {
			//if someone set the pause flag, we can leave the job.
			UserProperty up = userPropertyDao.getProp(IntegratorPushManager.INTEGRATOR_PAUSE_FULL_PUSH);
			if(up != null && "true".equals(up.getValue())) {
				throw new IntegratorPausedException("needed to exit as there was a pause detected");
			}
			
			demographicPushCount++;
			logger.debug("pushing demographic facilityId:" + facility.getId() + ", demographicId:" + demographicId + "  " + demographicPushCount + " of " + demographicIds.size());
			BenchmarkTimer benchTimer = new BenchmarkTimer("pushing demo facilityId:" + facility.getId() + ", demographicId:" + demographicId + "  " + demographicPushCount + " of " + demographicIds.size());

			try {
		
				demographicService.setLastPushDate(demographicId);
				
				pushDemographic(lastDataUpdated, facility, demographicService, demographicId, facility.getId());
				// it's safe to set the consent later so long as we default it to none when we send the original demographic data in the line above.
				benchTimer.tag("pushDemographic");
				
				// see new method pushPatientConsentTable() for reason.
				if( ! CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
					pushDemographicConsent(lastDataUpdated, facility, demographicService, demographicId);
					benchTimer.tag("pushDemographicConsent");
				}
				
				pushDemographicIssues(lastDataUpdated, facility, programsInFacility, demographicService, demographicId, cachedFacility);
				benchTimer.tag("pushDemographicIssues");
				pushDemographicPreventions(lastDataUpdated, facility, providerIdsInFacility, demographicService, demographicId);
				benchTimer.tag("pushDemographicPreventions");
				pushDemographicNotes(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushDemographicNotes");
				pushDemographicDrugs(lastDataUpdated, facility, providerIdsInFacility, demographicService, demographicId);
				benchTimer.tag("pushDemographicDrugs");
				pushAdmissions(lastDataUpdated, facility, programsInFacility, demographicService, demographicId);
				benchTimer.tag("pushAdmissions");
				pushAppointments(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushAppointments");
				pushMeasurements(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushMeasurements");
				pushDxresearchs(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushDxresearchs");
				
				// TODO Need additional methods for other billing regions here.
				if( OscarProperties.getInstance().isOntarioBillingRegion() ) {
					pushBillingItems(lastDataUpdated, facility, demographicService, demographicId);
					benchTimer.tag("pushBillingItems");
				}
				
				pushEforms(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushEforms");
				pushAllergies(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushAllergies");
				pushDocuments(loggedInInfo, lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushDocuments");
				pushForms(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushForms");
				pushLabResults(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("pushLabResults");

				if(ip != null) {
					integratorPushManager.updateItem(ip,demographicId);
				}
				
				logger.debug(benchTimer.report());

				DbConnectionFilter.releaseAllThreadDbResources();
				currentSleepOnErrorTime = SLEEP_ON_ERROR;
				
			} catch (IllegalArgumentException iae) {
				// continue processing demographics if date values in current demographic are bad
				// all other errors thrown by the above methods should indicate a failure in the service
				// connection at large -- continuing to process not possible
				// need some way of notification here.
				logger.error("Error updating demographic, continuing with Demographic batch", iae);
			} catch (ShutdownException e) {
				throw (e);
			} catch (Exception e) {
				
				Throwable cause = e.getCause();
				if( cause instanceof SocketException ) {
					
					currentSleepOnErrorTime = Math.round(currentSleepOnErrorTime * SLEEP_ON_ERROR_STEP);
					try {
	                    Thread.sleep(currentSleepOnErrorTime);
                    } catch (InterruptedException e1) {	                 
                    }
				}
				
				
				logger.error("Unexpected error.", e);
				//not so sure about this yet. We don't want to end up in a loop where an exception is being thrown
				//so we just keep retrying..this way a new job gets created.
				//integratorPushManager.setError(ip,e);
			}
			
		}
		
		if(ip != null) {
			integratorPushManager.completePush(ip,true);
		}
		
		logger.debug("Total pushAllDemographics :" + (System.currentTimeMillis() - startTime));
	}

	//TODO: DemographicExt are not sent?
	private void pushDemographic(Date lastDataUpdated, Facility facility, DemographicWs service, Integer demographicId, Integer facilityId) throws ShutdownException {
		DemographicTransfer demographicTransfer = new DemographicTransfer();

		// set demographic info
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		
		//TODO: we can remove this once we know our demographic id list is good
		if(demographic.getLastUpdateDate().before(lastDataUpdated)) return;
		

		String ignoreProperties[] = { "lastUpdateDate" };
		BeanUtils.copyProperties(demographic, demographicTransfer, ignoreProperties);

		demographicTransfer.setCaisiDemographicId(demographic.getDemographicNo());
		demographicTransfer.setBirthDate(demographic.getBirthDay());

		demographicTransfer.setHinType(demographic.getHcType());
		demographicTransfer.setHinVersion(demographic.getVer());
		demographicTransfer.setHinValidEnd(DateUtils.toGregorianCalendar(demographic.getHcRenewDate()));
		demographicTransfer.setHinValidStart(DateUtils.toGregorianCalendar(demographic.getEffDate()));
		demographicTransfer.setCaisiProviderId(demographic.getProviderNo());

		demographicTransfer.setStreetAddress(demographic.getAddress());
		demographicTransfer.setPhone1(demographic.getPhone());
		demographicTransfer.setPhone2(demographic.getPhone2());

		demographicTransfer.setLastUpdateDate(DateUtils.toGregorianCalendar(demographic.getLastUpdateDate()));

		try {
			demographicTransfer.setGender(Gender.valueOf(demographic.getSex().toUpperCase()));
		} catch (Exception e) {
			// do nothing, for now gender is on a "good luck" what ever you
			// get is what you get basis until the whole gender mess is sorted.
		}

		// set image
		ClientImage clientImage = clientImageDAO.getClientImage(demographicId);
		if (clientImage != null) {
			demographicTransfer.setPhoto(clientImage.getImage_data());
			demographicTransfer.setPhotoUpdateDate(DateUtils.toCalendar(clientImage.getUpdate_date()));
		}

		// set flag to remove demographic identity
		boolean rid = integratorControlDao.readRemoveDemographicIdentity(facilityId);
		demographicTransfer.setRemoveId(rid);

		// send the request
		service.setDemographic(demographicTransfer);
		throttleAndChecks();

		conformanceTestLog(facility, "Demographic", String.valueOf(demographicId));
	}
	
	/**
	 * This is an override to the pushDemographicConsent method.
	 *  
	 * Demographic information will not be pushed if a patient has revoked or modified their 
	 * consent through the Patient Consent Module for participation in the Integrator program,
	 * therefore the attached consents from the pushDemographicConsent table will not get pushed. 
	 * 
	 * This method ensures that the all current consents from the Patient Consent Module ()  
	 */
	private void pushPatientConsentTable(LoggedInInfo loggedinInfo, Date lastDataUpdated, Facility facility ) {
		
		DemographicWs demographicService = null;
		List<Consent> consents = null;
		List<IntegratorConsent> tempConsents = null;
			
		try {
			demographicService = CaisiIntegratorManager.getDemographicWs(null, facility);
			consents = patientConsentManager.getConsentsByTypeAndEditDate( loggedinInfo, this.consentType, lastDataUpdated );
			tempConsents = new ArrayList<IntegratorConsent>();
		} catch (MalformedURLException e) {
			logger.error("Connection error while updating patient consents", e);
		}
		
		logger.info("Updating last edited consent list after " +  lastDataUpdated);
		
		// Convert a Consent object into an IntegratorConsent object so that it 
		// the IntegratorConsent object can be converted to a transfer object to 
		// be consumed by the webservice.
		for( Consent consent : consents ) {
			
			logger.debug("Updating consent id: " +  consent.getId() );
			IntegratorConsent integratorConsent = CaisiIntegratorManager.makeIntegratorConsent(consent);
			integratorConsent.setFacilityId( facility.getId() );
			tempConsents.add( integratorConsent );
		}
		
		if( tempConsents != null && tempConsents.size() > 0 ) {
			pushDemographicConsent( tempConsents, demographicService );
		}
	}

	private void pushDemographicConsent(Date lastUpdatedData, Facility facility, DemographicWs demographicService, Integer demographicId) {
		// find the latest relevant consent that needs to be pushed.
		pushDemographicConsent( integratorConsentDao.findByFacilityAndDemographicSince(facility.getId(), demographicId,lastUpdatedData), demographicService );		
	}
	
	private void pushDemographicConsent( List<IntegratorConsent> tempConsents, DemographicWs demographicService ) {
		for (IntegratorConsent tempConsent : tempConsents) {
			if (tempConsent.getClientConsentStatus() == ConsentStatus.GIVEN || tempConsent.getClientConsentStatus() == ConsentStatus.REVOKED) {
				SetConsentTransfer consentTransfer = CaisiIntegratorManager.makeSetConsentTransfer(tempConsent);
				demographicService.setCachedDemographicConsent(consentTransfer);
				logger.debug("pushDemographicConsent:" + tempConsent.getId() + "," + tempConsent.getFacilityId() + "," + tempConsent.getDemographicId());
				// return;
			}
		}
	}

	private void pushDemographicIssues(Date lastDataUpdated, Facility facility, List<Program> programsInFacility, DemographicWs service, Integer demographicId, CachedFacility cachedFacility) throws ShutdownException {
		logger.debug("pushing demographicIssues facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDAO.getIssuesByDemographicSince(demographicId.toString(),lastDataUpdated);
		StringBuilder sentIds = new StringBuilder();
		if (caseManagementIssues.size() == 0) return;
		
		Properties prop = OscarProperties.getInstance();

		for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
			// don't send issue if it is not in our facility.
			logger.debug("Facility:" + facility.getName() + " - caseManagementIssue = " + caseManagementIssue.toString());
			if (caseManagementIssue.getProgram_id() == null || !isProgramIdInProgramList(programsInFacility, caseManagementIssue.getProgram_id())) continue;

			long issueId = caseManagementIssue.getIssue_id();
			Issue issue = issueDao.getIssue(issueId);
			CachedDemographicIssue cachedDemographicIssue = new CachedDemographicIssue();

			FacilityIdDemographicIssueCompositePk facilityDemographicIssuePrimaryKey = new FacilityIdDemographicIssueCompositePk();
			facilityDemographicIssuePrimaryKey.setCaisiDemographicId(caseManagementIssue.getDemographic_no());
			if( Issue.CUSTOM_ISSUE.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.CUSTOM_ISSUE); 
			}
			else if( Issue.SYSTEM.equalsIgnoreCase(issue.getType()) ){
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.SYSTEM);
			}
			else if( Issue.ICD_9.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.ICD_9);
			}
			else if( Issue.ICD_10.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.ICD_10);
			}
			else if( Issue.SNOMED.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.SNOMED);
			}
			else {
				logger.warn("UNKNOWN ISSUE TYPE. " + issue.getType() + " ID:" + issue.getId() + " SKIPPING...");
				continue;
			}
			
			facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
			cachedDemographicIssue.setFacilityDemographicIssuePk(facilityDemographicIssuePrimaryKey);

			BeanUtils.copyProperties(caseManagementIssue, cachedDemographicIssue);
			cachedDemographicIssue.setIssueDescription(issue.getDescription());
			cachedDemographicIssue.setIssueRole(IntegratorRoleUtils.getIntegratorRole(issue.getRole()));

			ArrayList<CachedDemographicIssue> issues = new ArrayList<CachedDemographicIssue>();
			issues.add(cachedDemographicIssue);
			service.setCachedDemographicIssues(issues);
			throttleAndChecks();

			sentIds.append("," + caseManagementIssue.getId());
		}
		
		//let integrator know our current and active list of issues for this patient. The integrator will delete all not found in this list in it's db.
		service.deleteCachedDemographicIssues(demographicId, caseManagementIssueDAO.getIssueIdsForIntegrator(cachedFacility.getIntegratorFacilityId(),demographicId));
		conformanceTestLog(facility, "CaseManagementIssue", sentIds.toString());
	}

	private void pushAdmissions(Date lastDataUpdated, Facility facility, List<Program> programsInFacility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing admissions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Admission> admissions = admissionDao.getAdmissionsByFacilitySince(demographicId, facility.getId(),lastDataUpdated);
		StringBuilder sentIds = new StringBuilder();
		if (admissions.size() == 0) return;
		ArrayList<CachedAdmission> cachedAdmissions = new ArrayList<CachedAdmission>();
		
		int i=0;
		for (Admission admission : admissions) {

			// don't send admission if it is not in our facility. yeah I know I'm double checking since it's selected
			// but the reality is I don't trust it and our facility segmentation is flakey at best so.. better to check again.
			logger.debug("Facility:" + facility.getName() + " - admissionId = " + admission.getId());
			if (!isProgramIdInProgramList(programsInFacility, admission.getProgramId())) continue;

			CachedAdmission cachedAdmission = new CachedAdmission();

			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(admission.getId().intValue());
			cachedAdmission.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedAdmission.setAdmissionDate(DateUtils.toCalendar(admission.getAdmissionDate()));
			cachedAdmission.setAdmissionNotes(admission.getAdmissionNotes());
			cachedAdmission.setCaisiDemographicId(demographicId);
			cachedAdmission.setCaisiProgramId(admission.getProgramId());
			cachedAdmission.setDischargeDate(DateUtils.toCalendar(admission.getDischargeDate()));
			cachedAdmission.setDischargeNotes(admission.getDischargeNotes());
			//TODO:missing status from the whole transfer?
			
			cachedAdmissions.add(cachedAdmission);
			
			
			if((++i % 50) == 0) {
				demographicService.setCachedAdmissions(cachedAdmissions);
				throttleAndChecks();
				cachedAdmissions.clear();
			}

			sentIds.append("," + admission.getId());
		}
		
		if(cachedAdmissions.size()>0) {
			demographicService.setCachedAdmissions(cachedAdmissions);
			throttleAndChecks();
		}

		conformanceTestLog(facility, "Admission", sentIds.toString());
	}

	private boolean isProgramIdInProgramList(List<Program> programList, int programId) {
		for (Program p : programList) {
			if (p.getId().intValue() == programId) return (true);
		}

		return (false);
	}

	private void pushDemographicPreventions(Date lastDataUpdated, Facility facility, List<String> providerIdsInFacility, DemographicWs service, Integer demographicId) throws ShutdownException, ParserConfigurationException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.debug("pushing demographicPreventions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		ArrayList<CachedDemographicPrevention> preventionsToSend = new ArrayList<CachedDemographicPrevention>();
		StringBuilder sentIds = new StringBuilder();

		// get all preventions
		// for each prevention, copy fields to an integrator prevention
		// need to copy ext info
		// add prevention to array list to send
		List<Prevention> localPreventions = preventionDao.findNotDeletedByDemographicIdAfterDatetime(demographicId,lastDataUpdated);
		
		for (Prevention localPrevention : localPreventions) {

			if (!providerIdsInFacility.contains(localPrevention.getCreatorProviderNo())) continue;

			CachedDemographicPrevention cachedDemographicPrevention = new CachedDemographicPrevention();
			cachedDemographicPrevention.setCaisiDemographicId(demographicId);
			cachedDemographicPrevention.setCaisiProviderId(localPrevention.getProviderNo());

			{
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(localPrevention.getId());
				cachedDemographicPrevention.setFacilityPreventionPk(pk);
			}

			cachedDemographicPrevention.setNextDate(DateUtils.toCalendar(localPrevention.getNextDate()));
			cachedDemographicPrevention.setPreventionDate(DateUtils.toCalendar(localPrevention.getPreventionDate()));
			cachedDemographicPrevention.setPreventionType(localPrevention.getPreventionType());
			cachedDemographicPrevention.setRefused(localPrevention.isRefused());
			cachedDemographicPrevention.setNever(localPrevention.isNever());

			// add ext info
			// ext info should be added to the attributes field as xml data
			Document doc = XmlUtils.newDocument("PreventionExt");
			HashMap<String, String> exts = preventionExtDao.getPreventionExt(localPrevention.getId());
			for (Map.Entry<String, String> entry : exts.entrySet()) {
				XmlUtils.appendChildToRoot(doc, entry.getKey(), entry.getValue());
			}
			cachedDemographicPrevention.setAttributes(XmlUtils.toString(doc, false));

			preventionsToSend.add(cachedDemographicPrevention);

			sentIds.append("," + localPrevention.getId());
		}

		if (preventionsToSend.size() > 0) {
			service.setCachedDemographicPreventions(preventionsToSend);
			throttleAndChecks();
			conformanceTestLog(facility, "Prevention", sentIds.toString());
		}

		//let integrator know our current and active list of preventions for this patient. The integrator will delete all not found in this list in it's db.
		service.deleteCachedDemographicPreventions(demographicId, preventionDao.findNonDeletedIdsByDemographic(demographicId));
		
	}

	private void pushDocuments(LoggedInInfo loggedInInfo, Date lastDataUpdated, Facility facility, DemographicWs demographicWs, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicDocuments facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		StringBuilder sentIds = new StringBuilder();

		List<EDoc> privateDocs = EDocUtil.listDocsSince(loggedInInfo, "demographic", demographicId.toString(), "all", EDocUtil.PRIVATE, EDocSort.OBSERVATIONDATE, "",lastDataUpdated);
		int i=0;
		for (EDoc eDoc : privateDocs) {
			sendSingleDocument(demographicWs, eDoc, demographicId);
			
			if((++i % 10) == 0) {
				throttleAndChecks();
			}
			
			sentIds.append("," + eDoc.getDocId());
		}
		
		if(sentIds.length()>0) {
			throttleAndChecks();
		}

		conformanceTestLog(facility, "EDoc", sentIds.toString());
	}

	private void sendSingleDocument(DemographicWs demographicWs, EDoc eDoc, Integer demographicId) {
		
		// send this document
		CachedDemographicDocument cachedDemographicDocument = new CachedDemographicDocument();
		FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
		facilityIdIntegerCompositePk.setCaisiItemId(Integer.parseInt(eDoc.getDocId()));
		cachedDemographicDocument.setFacilityIntegerPk(facilityIdIntegerCompositePk);

		cachedDemographicDocument.setAppointmentNo(eDoc.getAppointmentNo());
		cachedDemographicDocument.setCaisiDemographicId(demographicId);
		cachedDemographicDocument.setContentType(eDoc.getContentType());
		cachedDemographicDocument.setDocCreator(eDoc.getCreatorId());
		cachedDemographicDocument.setDocFilename(eDoc.getFileName());
		cachedDemographicDocument.setDocType(eDoc.getType());
		cachedDemographicDocument.setDocXml(eDoc.getHtml());
		cachedDemographicDocument.setNumberOfPages(eDoc.getNumberOfPages());
		cachedDemographicDocument.setObservationDate(DateUtils.toGregorianCalendarDate(eDoc.getObservationDate()));
		cachedDemographicDocument.setProgramId(eDoc.getProgramId());
		cachedDemographicDocument.setPublic1(Integer.parseInt(eDoc.getDocPublic()));
		cachedDemographicDocument.setResponsible(eDoc.getResponsibleId());
		cachedDemographicDocument.setReviewDateTime(DateUtils.toGregorianCalendar(eDoc.getReviewDateTimeDate()));
		cachedDemographicDocument.setReviewer(eDoc.getReviewerId());
		cachedDemographicDocument.setSource(eDoc.getSource());
		cachedDemographicDocument.setStatus("" + eDoc.getStatus());
		cachedDemographicDocument.setUpdateDateTime(DateUtils.toGregorianCalendar(eDoc.getDateTimeStampAsDate()));
		cachedDemographicDocument.setDescription(eDoc.getDescription());

		byte[] contents = EDocUtil.getFile(OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + '/' + eDoc.getFileName());
		if(contents == null) {
			logger.warn("Unable to send document - the file does not exist or can't be read!! " +  OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + '/' + eDoc.getFileName());
			return;
		}
		demographicWs.addCachedDemographicDocumentAndContents(cachedDemographicDocument, contents);
	}

	private void pushLabResults(Date lastDataUpdated, Facility facility, DemographicWs demographicWs, Integer demographicId) throws ShutdownException, ParserConfigurationException, UnsupportedEncodingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.debug("pushing pushLabResults facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		CommonLabResultData comLab = new CommonLabResultData();
		
		//we need to check the patient lab routing table on it's own too..the case where a lab is updated, but the link is done later
		OscarProperties op = OscarProperties.getInstance();
		String cml = op.getProperty("CML_LABS","false");
		String mds = op.getProperty("MDS_LABS","false");
		String pathnet = op.getProperty("PATHNET_LABS","false");
		String hl7text = op.getProperty("HL7TEXT_LABS","false");
		String epsilon = op.getProperty("Epsilon_LABS","false");
		
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		if("yes".equals(epsilon) || "yes".equals(cml))
			results.addAll(comLab.getCmlAndEpsilonLabResultsSince(demographicId, lastDataUpdated));
		if("yes".equals(mds))
			results.addAll(comLab.getMdsLabResultsSince(demographicId, lastDataUpdated));
		if("yes".equals(pathnet))
			results.addAll(comLab.getPathnetResultsSince(demographicId, lastDataUpdated));
		if("yes".equals(hl7text))
			results.addAll(comLab.getHl7ResultsSince(demographicId, lastDataUpdated));
		 
		for(LabIdAndType id:results) {
			logger.debug("id="+id.getLabId() + ",type=" + id.getLabType());
		}
	
		StringBuilder sentIds = new StringBuilder();
		
		if (results.size() == 0) return;

		
		for (LabIdAndType labIdAndType : results) {
			LabResultData lab = comLab.getLab(labIdAndType);
			if(lab != null) {
				CachedDemographicLabResult cachedDemographicLabResult = makeCachedDemographicLabResult(demographicId, lab);
				demographicWs.addCachedDemographicLabResult(cachedDemographicLabResult);

				throttleAndChecks();
				sentIds.append("," + lab.getLabPatientId() + ":" + lab.labType + ":" + lab.segmentID);
			} else {
				logger.warn("Lab missing!!! " + labIdAndType.getLabType() + ":"+ labIdAndType.getLabId());
			}
		}
		
		conformanceTestLog(facility, "LabResultData", sentIds.toString());

	}

	private CachedDemographicLabResult makeCachedDemographicLabResult(Integer demographicId, LabResultData lab) throws ParserConfigurationException, UnsupportedEncodingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		CachedDemographicLabResult cachedDemographicLabResult = new CachedDemographicLabResult();

		FacilityIdLabResultCompositePk pk = new FacilityIdLabResultCompositePk();
		// our attempt at making a fake pk....
		String key = LabDisplayHelper.makeLabKey(demographicId, lab.getSegmentID(), lab.labType, lab.getDateTime());
		pk.setLabResultId(key);
		cachedDemographicLabResult.setFacilityIdLabResultCompositePk(pk);

		cachedDemographicLabResult.setCaisiDemographicId(demographicId);
		cachedDemographicLabResult.setType(lab.labType);

		Document doc = LabDisplayHelper.labToXml(demographicId, lab);

		String data = XmlUtils.toString(doc, false);
		cachedDemographicLabResult.setData(data);

		return (cachedDemographicLabResult);
	}

	private void pushForms(Date lastDataUpdated, Facility facility, DemographicWs demographicWs, Integer demographicId) throws ShutdownException, SQLException, IOException, ParseException {
		logger.debug("pushing demographic forms facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		// TODO add additional methods for other forms here
		
		if( OscarProperties.getInstance().isOntarioBillingRegion() ) {
			// LabReq2007 and up is only used in Ontario
			pushLabReq2007(lastDataUpdated, facility, demographicWs, demographicId);	
		}
	}

	private void pushLabReq2007(Date lastDataUpdated, Facility facility, DemographicWs demographicWs, Integer demographicId) throws SQLException, ShutdownException, IOException, ParseException {
		List<Properties> records = FrmLabReq07Record.getPrintRecordsSince(demographicId,lastDataUpdated);
		if (records.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();

		for (Properties p : records) {
			logger.debug("pushing form labReq2007 : " + p.get("ID") + " : " + p.get("formEdited"));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(p.getProperty("formEdited"));

			// no change since last sync
			if (date != null && date.before(lastDataUpdated)) continue;

			CachedDemographicForm cachedDemographicForm = new CachedDemographicForm();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(Integer.parseInt(p.getProperty("ID")));
			cachedDemographicForm.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedDemographicForm.setCaisiDemographicId(demographicId);
			cachedDemographicForm.setCaisiProviderId(p.getProperty("provider_no"));
			cachedDemographicForm.setEditDate(DateUtils.toGregorianCalendar(date));
			cachedDemographicForm.setFormName("formLabReq07");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			p.store(baos, null);
			cachedDemographicForm.setFormData(baos.toString());

			demographicWs.addCachedDemographicForm(cachedDemographicForm);

			throttleAndChecks();
			sentIds.append("," + p.getProperty("ID"));
		}

		conformanceTestLog(facility, "formLabReq07", sentIds.toString());
	}

	private void pushDemographicNotes(Date lastDataUpdated, Facility facility, DemographicWs service, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicNotes facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());
		HashSet<Integer> programIds = new HashSet<Integer>();
		for (Program program : programs)
			programIds.add(program.getId());

		List<CaseManagementNote> localNotes = caseManagementNoteDAO.getNotesByDemographic(demographicId.toString());

		StringBuilder sentIds = new StringBuilder();

		for (CaseManagementNote localNote : localNotes) {	
			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || !programIds.contains(Integer.parseInt(localNote.getProgram_no()))) continue;

				// note hasn't changed since last sync
				if (localNote.getUpdate_date() != null && localNote.getUpdate_date().before(lastDataUpdated)) continue;

				CachedDemographicNote noteToSend = makeRemoteNote(localNote);
				ArrayList<CachedDemographicNote> notesToSend = new ArrayList<CachedDemographicNote>();
				notesToSend.add(noteToSend);
				service.setCachedDemographicNotes(notesToSend);

				sentIds.append("," + localNote.getId());
			} catch (NumberFormatException e) {
				logger.error("Unexpected error. ProgramNo=" + localNote.getProgram_no(), e);
			}
		}

		conformanceTestLog(facility, "CaseManagementNote", sentIds.toString());
		sentIds = new StringBuilder();

		// add group notes as well.
		logger.info("checking for group notes for " + demographicId);
		List<GroupNoteLink> noteLinks = groupNoteDao.findLinksByDemographic(demographicId);
		logger.info("found " + noteLinks.size() + " group notes for " + demographicId);
		for (GroupNoteLink noteLink : noteLinks) {
			int orginalNoteId = noteLink.getNoteId();
			CaseManagementNote localNote = caseManagementNoteDAO.getNote(Long.valueOf(orginalNoteId));
			localNote.setDemographic_no(String.valueOf(demographicId));

			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || !programIds.contains(Integer.parseInt(localNote.getProgram_no()))) continue;

				CachedDemographicNote noteToSend = makeRemoteNote(localNote);
				ArrayList<CachedDemographicNote> notesToSend = new ArrayList<CachedDemographicNote>();
				notesToSend.add(noteToSend);
				service.setCachedDemographicNotes(notesToSend);
				logger.info("adding group note to send");

				sentIds.append("," + noteLink.getId());
			} catch (NumberFormatException e) {
				logger.error("Unexpected error. ProgramNo=" + localNote.getProgram_no(), e);
			}

		}

		conformanceTestLog(facility, "GroupNoteLink", sentIds.toString());

		throttleAndChecks();
	}

	private CachedDemographicNote makeRemoteNote(CaseManagementNote localNote) {

		CachedDemographicNote note = new CachedDemographicNote();

		CachedDemographicNoteCompositePk pk = new CachedDemographicNoteCompositePk();
		pk.setUuid(localNote.getUuid() + ":" + localNote.getDemographic_no());
		note.setCachedDemographicNoteCompositePk(pk);

		note.setCaisiDemographicId(Integer.parseInt(localNote.getDemographic_no()));
		note.setCaisiProgramId(Integer.parseInt(localNote.getProgram_no()));
		note.setEncounterType(localNote.getEncounter_type());
		note.setNote(localNote.getNote());
		note.setObservationCaisiProviderId(localNote.getProviderNo());
		note.setObservationDate(DateUtils.toCalendar(localNote.getObservation_date()));
		note.setRole(localNote.getRoleName());
		note.setSigningCaisiProviderId(localNote.getSigning_provider_no());
		note.setUpdateDate(DateUtils.toCalendar(localNote.getUpdate_date()));

		List<NoteIssue> issues = note.getIssues();
		List<CaseManagementIssue> localIssues = caseManagementIssueNotesDao.getNoteIssues(localNote.getId().intValue());
		String issueCodeType;
		for (CaseManagementIssue caseManagementIssue : localIssues) {
			long issueId = caseManagementIssue.getIssue_id();
			Issue localIssue = issueDao.getIssue(issueId);
			issueCodeType = localIssue.getType();

			NoteIssue noteIssue = new NoteIssue();
			if( Issue.CUSTOM_ISSUE.equalsIgnoreCase(issueCodeType) ) { 
				noteIssue.setCodeType(CodeType.CUSTOM_ISSUE);
			}
			else if( Issue.ICD_10.equalsIgnoreCase(issueCodeType) ) { 
				noteIssue.setCodeType(CodeType.ICD_10);
			}
			else if( Issue.ICD_9.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.ICD_9);
			}
			else if( Issue.SNOMED.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.SNOMED);
			}
			else if( Issue.SYSTEM.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.SYSTEM);
			}
			else {
				continue;
			}
						
			noteIssue.setIssueCode(localIssue.getCode());
			issues.add(noteIssue);
		}

		return (note);
	}

	private void pushDemographicDrugs(Date lastDataUpdated, Facility facility, List<String> providerIdsInFacility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicDrugss facilityId:" + facility.getId() + ", demographicId:" + demographicId);
		StringBuilder sentIds = new StringBuilder();

		List<Drug> drugs = drugDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (drugs == null || drugs.size() == 0) return;

		if (drugs != null) {
			for (Drug drug : drugs) {
				if (!providerIdsInFacility.contains(drug.getProviderNo())) continue;

				CachedDemographicDrug cachedDemographicDrug = new CachedDemographicDrug();

				cachedDemographicDrug.setArchived(drug.isArchived());
				cachedDemographicDrug.setArchivedReason(drug.getArchivedReason());
				cachedDemographicDrug.setArchivedDate(DateUtils.toCalendar(drug.getArchivedDate()));
				cachedDemographicDrug.setAtc(drug.getAtc());
				cachedDemographicDrug.setBrandName(drug.getBrandName());
				cachedDemographicDrug.setCaisiDemographicId(drug.getDemographicId());
				cachedDemographicDrug.setCaisiProviderId(drug.getProviderNo());
				cachedDemographicDrug.setCreateDate(DateUtils.toCalendar(drug.getCreateDate()));
				cachedDemographicDrug.setCustomInstructions(drug.isCustomInstructions());
				cachedDemographicDrug.setCustomName(drug.getCustomName());
				cachedDemographicDrug.setDosage(drug.getDosage());
				cachedDemographicDrug.setDrugForm(drug.getDrugForm());
				cachedDemographicDrug.setDuration(drug.getDuration());
				cachedDemographicDrug.setDurUnit(drug.getDurUnit());
				cachedDemographicDrug.setEndDate(DateUtils.toCalendar(drug.getEndDate()));
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
				pk.setCaisiItemId(drug.getId());
				cachedDemographicDrug.setFacilityIdIntegerCompositePk(pk);
				cachedDemographicDrug.setFreqCode(drug.getFreqCode());
				cachedDemographicDrug.setGenericName(drug.getGenericName());
				cachedDemographicDrug.setLastRefillDate(DateUtils.toCalendar(drug.getLastRefillDate()));
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
				cachedDemographicDrug.setRxDate(DateUtils.toCalendar(drug.getRxDate()));
				if (drug.getScriptNo() != null) cachedDemographicDrug.setScriptNo(drug.getScriptNo());
				cachedDemographicDrug.setSpecial(drug.getSpecial());
				cachedDemographicDrug.setTakeMax(drug.getTakeMax());
				cachedDemographicDrug.setTakeMin(drug.getTakeMin());
				cachedDemographicDrug.setUnit(drug.getUnit());
				cachedDemographicDrug.setUnitName(drug.getUnitName());

				ArrayList<CachedDemographicDrug> drugsToSend = new ArrayList<CachedDemographicDrug>();
				drugsToSend.add(cachedDemographicDrug);
				demographicService.setCachedDemographicDrugs(drugsToSend);

				sentIds.append("," + drug.getId());
			}
		}

		// if (drugsToSend.size()>0) demographicService.setCachedDemographicDrugs(drugsToSend);

		throttleAndChecks();
		conformanceTestLog(facility, "Drug", sentIds.toString());
	}

	private void pushAllergies(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing demographicAllergies facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		AllergyDao allergyDao = (AllergyDao) SpringUtils.getBean("allergyDao");
		List<Allergy> allergies = allergyDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (allergies.size() == 0) return;
		ArrayList<CachedDemographicAllergy> cachedAllergies = new ArrayList<CachedDemographicAllergy>();
		
		StringBuilder sentIds = new StringBuilder();

		int i = 0;
		for (Allergy allergy : allergies) {
			CachedDemographicAllergy cachedAllergy = new CachedDemographicAllergy();

			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(allergy.getAllergyId());
			cachedAllergy.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);
			
			if(allergy.getAgccs() != null){
			   cachedAllergy.setAgccs(allergy.getAgccs());
			}
			if(allergy.getAgcsp() != null){
				cachedAllergy.setAgcsp(allergy.getAgcsp());
			}
			cachedAllergy.setAgeOfOnset(allergy.getAgeOfOnset());
			cachedAllergy.setCaisiDemographicId(demographicId);
			cachedAllergy.setDescription(allergy.getDescription());
			cachedAllergy.setEntryDate(DateUtils.toGregorianCalendar(allergy.getEntryDate()));
			
			if(allergy.getHiclSeqno() != null){
			   cachedAllergy.setHiclSeqNo(allergy.getHiclSeqno());
			}
			if(allergy.getHicSeqno() !=null){
				cachedAllergy.setHicSeqNo(allergy.getHicSeqno());
			}
			
			cachedAllergy.setLifeStage(allergy.getLifeStage());
			cachedAllergy.setOnSetCode(allergy.getOnsetOfReaction());
			if (allergy.getDrugrefId() != null) cachedAllergy.setPickId(Integer.parseInt(allergy.getDrugrefId()));
			cachedAllergy.setReaction(allergy.getReaction());
			cachedAllergy.setRegionalIdentifier(allergy.getRegionalIdentifier());
			cachedAllergy.setSeverityCode(allergy.getSeverityOfReaction());
			if (allergy.getStartDate() != null) cachedAllergy.setStartDate(DateUtils.toGregorianCalendar(allergy.getStartDate()));
			cachedAllergy.setTypeCode(allergy.getTypeCode());

			cachedAllergies.add(cachedAllergy);
			
			if((++i % 50) == 0) {
				demographicService.setCachedDemographicAllergies(cachedAllergies);
				throttleAndChecks();
				cachedAllergies.clear();
			}

			sentIds.append("," + allergy.getAllergyId());
		}
		
		if(cachedAllergies.size() > 0) {
			demographicService.setCachedDemographicAllergies(cachedAllergies);
			throttleAndChecks();	
		}

		conformanceTestLog(facility, "Allergy", sentIds.toString());
	}

	private void pushAppointments(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing appointments facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Appointment> appointments = appointmentDao.getAllByDemographicNoSince(demographicId,lastDataUpdated);
		if (appointments.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();
		ArrayList<CachedAppointment> cachedAppointments = new ArrayList<CachedAppointment>();
		
		int i=0;
		for (Appointment appointment : appointments) {
			
			CachedAppointment cachedAppointment = new CachedAppointment();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(appointment.getId());
			cachedAppointment.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedAppointment.setAppointmentDate(DateUtils.toCalendar(appointment.getAppointmentDate()));
			cachedAppointment.setCaisiDemographicId(demographicId);
			cachedAppointment.setCaisiProviderId(appointment.getProviderNo());
			cachedAppointment.setCreateDatetime(DateUtils.toCalendar(appointment.getCreateDateTime()));
			cachedAppointment.setEndTime(DateUtils.toCalendar(appointment.getEndTime()));
			cachedAppointment.setLocation(appointment.getLocation());
			cachedAppointment.setNotes(appointment.getNotes());
			cachedAppointment.setReason(appointment.getReason());
			cachedAppointment.setRemarks(appointment.getRemarks());
			cachedAppointment.setResources(appointment.getResources());
			cachedAppointment.setStartTime(DateUtils.toCalendar(appointment.getStartTime()));
			cachedAppointment.setStatus(appointment.getStatus());
			cachedAppointment.setStyle(appointment.getStyle());
			cachedAppointment.setType(appointment.getType());
			cachedAppointment.setUpdateDatetime(DateUtils.toCalendar(appointment.getUpdateDateTime()));

			cachedAppointments.add(cachedAppointment);
			
			if((++i % 50) == 0) {
				demographicService.setCachedAppointments(cachedAppointments);
				throttleAndChecks();
				cachedAppointments.clear();
			}
			sentIds.append("," + appointment.getId());
		}
		
		if(cachedAppointments.size()>0) {
			demographicService.setCachedAppointments(cachedAppointments);
			throttleAndChecks();
		}
		
		conformanceTestLog(facility, "Appointment", sentIds.toString());
	}

	private void pushDxresearchs(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing dxresearchs facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<Dxresearch> dxresearchs = dxresearchDao.getByDemographicNoSince(demographicId,lastDataUpdated);
		if (dxresearchs.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();
		ArrayList<CachedDxresearch> cachedDxresearchs = new ArrayList<CachedDxresearch>();
		
		int i=0;
		for (Dxresearch dxresearch : dxresearchs) {
			
			CachedDxresearch cachedDxresearch = new CachedDxresearch();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(dxresearch.getId().intValue());
			cachedDxresearch.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedDxresearch.setCaisiDemographicId(demographicId);
			cachedDxresearch.setDxresearchCode(dxresearch.getDxresearchCode());
			cachedDxresearch.setCodingSystem(dxresearch.getCodingSystem());
			cachedDxresearch.setStartDate(DateUtils.toCalendar(dxresearch.getStartDate()));
			cachedDxresearch.setUpdateDate(DateUtils.toCalendar(dxresearch.getUpdateDate()));
			cachedDxresearch.setStatus(String.valueOf(dxresearch.getStatus()));

			cachedDxresearchs.add(cachedDxresearch);
			
			if((++i % 50) == 0) {
				demographicService.setCachedDxresearch(cachedDxresearchs);
				throttleAndChecks();
				cachedDxresearchs.clear();
			}
			
			sentIds.append("," + dxresearch.getId());
		}
		
		if(cachedDxresearchs.size() > 0) {
			demographicService.setCachedDxresearch(cachedDxresearchs);
			throttleAndChecks();
		}

		
		conformanceTestLog(facility, "DxResearch", sentIds.toString());
	}

	private void pushBillingItems(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing billingitems facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<BillingONCHeader1> billingCh1s = billingONItemDao.getCh1ByDemographicNoSince(demographicId, lastDataUpdated);
		//TODO: don't think we need this based on my tests, but ideally, you'd want to check the timestamps of the billing items, and make sure you have a 
		//full list of billing headers..but I couldn't replicate editing a bill without the header being updated..so i'll just leave this
		//as a note.
		
		if (billingCh1s.size() == 0) return;

		ArrayList<CachedBillingOnItem> cachedBillingOnItems = new ArrayList<CachedBillingOnItem>();
		
		int i=0;

		for (BillingONCHeader1 billingCh1 : billingCh1s) {
			List<BillingONItem> billingItems = billingONItemDao.getBillingItemByCh1Id(billingCh1.getId());
			for (BillingONItem billingItem : billingItems) {
				MiscUtilsOld.checkShutdownSignaled();

				CachedBillingOnItem cachedBillingONItem = new CachedBillingOnItem();
				FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
				facilityIdIntegerCompositePk.setCaisiItemId(billingItem.getId());
				cachedBillingONItem.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

				cachedBillingONItem.setCaisiDemographicId(demographicId);
				cachedBillingONItem.setCaisiProviderId(billingCh1.getProviderNo());
				cachedBillingONItem.setApptProviderId(billingCh1.getApptProviderNo());
				cachedBillingONItem.setAsstProviderId(billingCh1.getAsstProviderNo());
				cachedBillingONItem.setAppointmentId(billingCh1.getAppointmentNo());
				cachedBillingONItem.setDx(billingItem.getDx());
				cachedBillingONItem.setDx1(billingItem.getDx1());
				cachedBillingONItem.setDx2(billingItem.getDx2());
				cachedBillingONItem.setServiceCode(billingItem.getServiceCode());
				cachedBillingONItem.setServiceDate(DateUtils.toCalendar(billingItem.getServiceDate()));
				cachedBillingONItem.setStatus(billingItem.getStatus());

				cachedBillingOnItems.add(cachedBillingONItem);
				
				if((++i % 50) == 0) {
					demographicService.setCachedBillingOnItem(cachedBillingOnItems);
					throttleAndChecks();
					cachedBillingOnItems.clear();
				}
			
			}
		}

		if(cachedBillingOnItems.size() > 0) {
			demographicService.setCachedBillingOnItem(cachedBillingOnItems);
			throttleAndChecks();
		}

	}

	private void pushEforms(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing eforms facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		List<EFormData> eformDatas = eFormDataDao.findByDemographicIdSinceLastDate(demographicId,lastDataUpdated);
		if (eformDatas.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();
		List<Integer> fdids = new ArrayList<Integer>();
		ArrayList<CachedEformData> cachedEformDatas = new ArrayList<CachedEformData>();
		
		int i = 0;
		for (EFormData eformData : eformDatas) {

			CachedEformData cachedEformData = new CachedEformData();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(eformData.getId());
			cachedEformData.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedEformData.setCaisiDemographicId(demographicId);
			cachedEformData.setFormDate(DateUtils.toCalendar(eformData.getFormDate()));
			cachedEformData.setFormTime(DateUtils.toCalendar(eformData.getFormTime()));
			cachedEformData.setFormId(eformData.getFormId());
			cachedEformData.setFormName(eformData.getFormName());
			cachedEformData.setFormData(eformData.getFormData());
			cachedEformData.setSubject(eformData.getSubject());
			cachedEformData.setStatus(eformData.isCurrent());
			cachedEformData.setFormProvider(eformData.getProviderNo());

			cachedEformDatas.add(cachedEformData);
			
			if((++i % 50) == 0) {
				demographicService.setCachedEformData(cachedEformDatas);
				throttleAndChecks();
				cachedEformDatas.clear();
			}
			
			
			sentIds.append("," + eformData.getId());
			fdids.add(eformData.getId());
		}
		
		if(cachedEformDatas.size()>0) {
			demographicService.setCachedEformData(cachedEformDatas);
			throttleAndChecks();
		}

		conformanceTestLog(facility, "EFormData", sentIds.toString());

		List<EFormValue> eFormValues = eFormValueDao.findByFormDataIdList(fdids);
		ArrayList<CachedEformValue> cachedEformValues = new ArrayList<CachedEformValue>();
		
		if (eFormValues.size() == 0) return;
		
		i = 0;
		for (EFormValue eFormValue : eFormValues) {
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
			
			if((++i % 50) == 0) {
				demographicService.setCachedEformValues(cachedEformValues);
				throttleAndChecks();
				cachedEformValues.clear();
			}
			
		}

		if(cachedEformValues.size() > 0) {
			demographicService.setCachedEformValues(cachedEformValues);
			throttleAndChecks();
		}
	}

	private void pushMeasurements(Date lastDataUpdated, Facility facility, DemographicWs demographicService, Integer demographicId) throws ShutdownException {
		logger.debug("pushing measurements facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");

		List<Measurement> measurements = measurementDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (measurements.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();

		for (Measurement measurement : measurements) {
			CachedMeasurement cachedMeasurement = new CachedMeasurement();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(measurement.getId());
			cachedMeasurement.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedMeasurement.setCaisiDemographicId(demographicId);
			cachedMeasurement.setCaisiProviderId(measurement.getProviderNo());
			cachedMeasurement.setComments(measurement.getComments());
			cachedMeasurement.setDataField(measurement.getDataField());
			cachedMeasurement.setDateEntered(DateUtils.toCalendar(measurement.getCreateDate()));
			cachedMeasurement.setDateObserved(DateUtils.toCalendar(measurement.getDateObserved()));
			cachedMeasurement.setMeasuringInstruction(measurement.getMeasuringInstruction());
			cachedMeasurement.setType(measurement.getType());

			ArrayList<CachedMeasurement> cachedMeasurements = new ArrayList<CachedMeasurement>();
			cachedMeasurements.add(cachedMeasurement);
			demographicService.setCachedMeasurements(cachedMeasurements);

			sentIds.append("," + measurement.getId());

			List<MeasurementsExt> measurementExts = measurementsExtDao.getMeasurementsExtByMeasurementId(measurement.getId());
			for (MeasurementsExt measurementExt : measurementExts) {
				MiscUtilsOld.checkShutdownSignaled();
				CachedMeasurementExt cachedMeasurementExt = new CachedMeasurementExt();
				FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
				fidIntegerCompositePk.setCaisiItemId(measurementExt.getId());
				cachedMeasurementExt.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

				cachedMeasurementExt.setMeasurementId(measurementExt.getMeasurementId());
				cachedMeasurementExt.setKeyval(measurementExt.getKeyVal());
				cachedMeasurementExt.setVal(measurementExt.getVal());

				ArrayList<CachedMeasurementExt> cachedMeasurementExts = new ArrayList<CachedMeasurementExt>();
				cachedMeasurementExts.add(cachedMeasurementExt);
				demographicService.setCachedMeasurementExts(cachedMeasurementExts);
			}

			MeasurementTypeDao measurementTypeDao = (MeasurementTypeDao) SpringUtils.getBean("measurementTypeDao");
			List<MeasurementType> measurementTypes = measurementTypeDao.findByType(measurement.getType());
			for (MeasurementType measurementType : measurementTypes) {
				MiscUtilsOld.checkShutdownSignaled();

				CachedMeasurementType cachedMeasurementType = new CachedMeasurementType();
				FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
				fidIntegerCompositePk.setCaisiItemId(measurementType.getId());
				cachedMeasurementType.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

				cachedMeasurementType.setType(measurementType.getType());
				cachedMeasurementType.setTypeDescription(measurementType.getTypeDescription());
				cachedMeasurementType.setMeasuringInstruction(measurementType.getMeasuringInstruction());

				ArrayList<CachedMeasurementType> cachedMeasurementTypes = new ArrayList<CachedMeasurementType>();
				cachedMeasurementTypes.add(cachedMeasurementType);
				demographicService.setCachedMeasurementTypes(cachedMeasurementTypes);
			}

			List<MeasurementMap> measurementMaps = measurementMapDao.getMapsByIdent(measurement.getType());
			for (MeasurementMap measurementMap : measurementMaps) {

				CachedMeasurementMap cachedMeasurementMap = new CachedMeasurementMap();
				FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
				fidIntegerCompositePk.setCaisiItemId(measurementMap.getId());
				cachedMeasurementMap.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);

				cachedMeasurementMap.setIdentCode(measurementMap.getIdentCode());
				cachedMeasurementMap.setLoincCode(measurementMap.getLoincCode());
				cachedMeasurementMap.setName(measurementMap.getName());
				cachedMeasurementMap.setLabType(measurementMap.getLabType());

				ArrayList<CachedMeasurementMap> cachedMeasurementMaps = new ArrayList<CachedMeasurementMap>();
				cachedMeasurementMaps.add(cachedMeasurementMap);
				demographicService.setCachedMeasurementMaps(cachedMeasurementMaps);
			}
		}

		throttleAndChecks();
		conformanceTestLog(facility, "Measurements", sentIds.toString());
	}
	
	
	/*
	1) demographicWs.getDemographicIdPushedAfterDateByRequestingFacility : 
		which gets the local demographicId's which have changed, it will traverse linked records so if a linked record changes, your local id is reported as changed.

	2) demographicWs.getDemographicsPushedAfterDate : 
		which is a raw listing of the direct records which have changed, i.e. (facilityId, oscarDemographicId).
	*/	
	private void findChangedRecordsFromIntegrator(LoggedInInfo loggedInInfo, Facility facility) throws MalformedURLException {//throws IOException, ShutdownException {
		logger.info("Start fetch data for facility : " + facility.getId() + " : " + facility.getName());
		boolean integratorLocalStore = OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE","yes");
		if(!integratorLocalStore){
			logger.info("local store not enabled");
			return;
		}
		DemographicWs demographicService = CaisiIntegratorManager.getDemographicWs(loggedInInfo, facility);
			
		Calendar nextTime = Calendar.getInstance();
		
		Date lastPushDate = new Date(0);
		try{
			UserProperty lastPull = userPropertyDao.getProp(UserProperty.INTEGRATOR_LAST_PULL_PRIMARY_EMR+"+"+facility.getId());
			lastPushDate.setTime(Long.parseLong(lastPull.getValue()));
		}catch(Exception epull){
			MiscUtils.getLogger().error("lastPull Error:",epull);
			lastPushDate = new Date(0);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastPushDate);
		
		List<Integer> demographicNos = demographicService.getDemographicIdPushedAfterDateByRequestingFacility(cal);
		
		if(demographicNos.isEmpty()){
			logger.debug("No demographics updated on the integrator");
		}else{
			logger.debug("demos changed "+demographicNos.size());
		}
		int demographicFetchCount = 0;
		for(Integer demographicNo:demographicNos){
			logger.debug("Demographic "+demographicNo+" updated on the integrator, primary emr ? ");
			DemographicExt demographicExt = demographicExtDao.getLatestDemographicExt(demographicNo, "primaryEMR");
			if (demographicExt != null && demographicExt.getValue().equals("1")){
				demographicFetchCount++;
				BenchmarkTimer benchTimer = new BenchmarkTimer("fetch and save for facilityId:" + facility.getId() + ", demographicId:" + demographicNo + "  " + demographicFetchCount + " of " + demographicNos.size());
				IntegratorFallBackManager.saveLinkNotes(loggedInInfo,demographicNo);
				benchTimer.tag("saveLinkedNotes");
				IntegratorFallBackManager.saveRemoteForms(loggedInInfo,demographicNo);
				benchTimer.tag("saveRemoteForms");
				
				
				
				IntegratorFallBackManager.saveDemographicIssues(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicIssues");
				IntegratorFallBackManager.saveDemographicPreventions(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicPreventions");
				IntegratorFallBackManager.saveDemographicDrugs(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicDrugs");
				IntegratorFallBackManager.saveAdmissions(loggedInInfo,demographicNo);
				benchTimer.tag("saveAdmissions");
				IntegratorFallBackManager.saveAppointments(loggedInInfo,demographicNo);
				benchTimer.tag("saveAppointments");
				IntegratorFallBackManager.saveAllergies(loggedInInfo,demographicNo);
				benchTimer.tag("saveAllergies");
				IntegratorFallBackManager.saveDocuments(loggedInInfo,demographicNo);
				benchTimer.tag("saveDocuments");
				IntegratorFallBackManager.saveLabResults(loggedInInfo,demographicNo);
				benchTimer.tag("saveLabResults");
 
				//These don't exist
				//IntegratorFallBackManager.saveMeasurements(demographicNo); // Not being displayed yet
				//IntegratorFallBackManager.saveDxresearchs(demographicNo);  //Not being displayed yet
				//IntegratorFallBackManager.saveBillingItems(demographicNo);//Not being displayed yet
				//IntegratorFallBackManager.saveEforms(demographicNo);//Not being displayed yet

				logger.debug(benchTimer.report());
			}
			userPropertyDao.saveProp(UserProperty.INTEGRATOR_LAST_PULL_PRIMARY_EMR+"+"+facility.getId(), "" + nextTime.getTime().getTime());
		}
		logger.info("End fetch data for facility : " + facility.getId() + " : " + facility.getName());
	}


	

	/**
	 * This method should not be used except during conformance testing. It will log all sends to the integrator. This is superfluous because all data is sent, we already know it's "all sent" even with out the logs.
	 */
	private static void conformanceTestLog(Facility facility, String dataType, String ids) {
		if (ConformanceTestHelper.enableConformanceOnlyTestFeatures) {
			ids = StringUtils.trimToNull(ids);
			if (ids != null) LogAction.addLogSynchronous(null, "Integrator Send", dataType, ids, facility.getIntegratorUrl());
		}
	}

	private static void throttleAndChecks() throws ShutdownException {
		MiscUtilsOld.checkShutdownSignaled();

		try {
			Thread.sleep(INTEGRATOR_THROTTLE_DELAY);
		} catch (InterruptedException e) {
			logger.error("Error", e);
		}
	}
}
