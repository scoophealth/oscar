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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.io.IOUtils;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.caisi_integrator.dao.CachedAdmission;
import org.oscarehr.caisi_integrator.dao.CachedAppointment;
import org.oscarehr.caisi_integrator.dao.CachedBillingOnItem;
import org.oscarehr.caisi_integrator.dao.CachedDemographic.Gender;
import org.oscarehr.caisi_integrator.dao.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.dao.CachedDemographicForm;
import org.oscarehr.caisi_integrator.dao.CachedDemographicHL7LabResult;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.dao.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNote;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.dao.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.dao.CachedDxresearch;
import org.oscarehr.caisi_integrator.dao.CachedEformData;
import org.oscarehr.caisi_integrator.dao.CachedEformValue;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedMeasurement;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementExt;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementMap;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementType;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.CachedProvider;
import org.oscarehr.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.FacilityIdLabResultCompositePk;
import org.oscarehr.caisi_integrator.dao.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.dao.NoteIssue;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.Role;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.ImportLog;
import org.oscarehr.caisi_integrator.ws.transfer.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.transfer.ProviderTransfer;
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
import org.oscarehr.common.dao.Hl7TextMessageDao;
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
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.IntegratorFileLog;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.labs.LabIdAndType;
import org.oscarehr.managers.IntegratorFileLogManager;
import org.oscarehr.managers.IntegratorPushManager;
import org.oscarehr.managers.PatientConsentManager;
import org.oscarehr.util.BenchmarkTimer;
import org.oscarehr.util.CxfClientUtilsOld;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.springframework.beans.BeanUtils;
import org.w3c.dom.Document;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.form.FrmLabReq07Record;
import oscar.log.LogAction;
import oscar.oscarLab.ca.all.web.LabDisplayHelper;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

public class CaisiIntegratorUpdateTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();
	
	private static final String COMPRESSION_SHELL_SCRIPT = "build_doc_zip.sh";
	private static final String COMPRESSED_DOCUMENTS_APPENDAGE = "-Docs";

	private static final String INTEGRATOR_UPDATE_PERIOD_PROPERTIES_KEY = "INTEGRATOR_UPDATE_PERIOD";

	private static boolean ISACTIVE_PATIENT_CONSENT_MODULE = Boolean.FALSE; 
	
	private ObjectOutputStream out = null;

	private static String outputDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR").trim();
	
	private PrintWriter documentMetaWriter;
	
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
	
	private UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	private PatientConsentManager patientConsentManager = (PatientConsentManager) SpringUtils.getBean(PatientConsentManager.class);
	private IntegratorFileLogManager integratorFileLogManager = SpringUtils.getBean(IntegratorFileLogManager.class);
	private MeasurementTypeDao measurementTypeDao = (MeasurementTypeDao) SpringUtils.getBean("measurementTypeDao");
	
	
	private ConsentType consentType;
	
	private static TimerTask timerTask = null;

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
		
		if(p == null) {
			logger.warn("INTEGRATOR_USER doesn't exist..please check properties file");
			return;
		}
		
		// If "push all patients that have consented" is set in the Integrator properties - this will ensure that only consenting patients will be pushed.
		// Note: the consent module must be activated in the Oscar properties file; and the Integrator Patient Consent program must be set in Provider Properties database table.
		if( OscarProperties.getInstance().getBooleanProperty("USE_NEW_PATIENT_CONSENT_MODULE", "true") &&  userPropertyDao.getProp( UserProperty.INTEGRATOR_PATIENT_CONSENT ) != null ) {
			CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE = "1".equals( userPropertyDao.getProp( UserProperty.INTEGRATOR_PATIENT_CONSENT ).getValue() );
			// consenttype is the consent type from the patient consent manager used for this module .
			consentType = patientConsentManager.getConsentType( UserProperty.INTEGRATOR_PATIENT_CONSENT );
		}
		
		logger.debug("CaisiIntegratorUpdateTask starting #" + numberOfTimesRun+"  running as "+loggedInInfo.getLoggedInProvider());

		try {
			pushAllFacilities(loggedInInfo);
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();

			logger.debug("CaisiIntegratorUpdateTask finished #" + numberOfTimesRun);
		}
	}

	public void pushAllFacilities(LoggedInInfo loggedInInfo)  {
		List<Facility> facilities = facilityDao.findAll(true);

		for (Facility facility : facilities) {
			try {
				if (facility.isIntegratorEnabled()) {
					pushAllDataForOneFacility(loggedInInfo, facility);
				}
			} catch (WebServiceException e) {
				if (CxfClientUtilsOld.isConnectionException(e)) {
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
	
	

	private void pushAllDataForOneFacility(LoggedInInfo loggedInInfo, Facility facility) throws IOException {
		logger.info("Start pushing data for facility : " + facility.getId() + " : " + facility.getName());

		// check all parameters are present
		String integratorBaseUrl = facility.getIntegratorUrl();
		String user = facility.getIntegratorUser();
		String password = facility.getIntegratorPassword();

		if (integratorBaseUrl == null || user == null || password == null) {
			logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
			return;
		}
		
		FacilityWs facilityWs = CaisiIntegratorManager.getFacilityWs(loggedInInfo, facility);
		org.oscarehr.caisi_integrator.ws.CachedFacility cachedFacility = facilityWs.getMyFacility();
		
		// sync the integrator logs and the most recent entry
		IntegratorFileLog lastFile = updateLogs(loggedInInfo, facility);

		// start at the beginning of time (aka jan 1, 1970) so by default everything is pushed
		Date lastDataUpdated = new Date(0);

		// set the date threshold.
		if (lastFile != null && lastFile.getCurrentDate() != null){
			lastDataUpdated = lastFile.getCurrentDate();
		}
		
		logger.info("Last data snapshot date " + lastDataUpdated);

		// Sync Oscar and Integrator Consent tables via web services. This is required before pushing any patient data. 
		// This is important because a patient file is NOT pushed if the consent is revoked - and therefore will not be updated with Integrator. 
		if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
			this.pushPatientConsentTable(loggedInInfo, lastDataUpdated, facility);
		}

		// this needs to be set now, before we do any sends, this will cause anything updated after now to be resent twice but it's better than items being missed that were updated after this started.
		Date currentUpdateDate = new Date();

		//setup this list once for this facility
		List<Program> programs = programDao.getProgramsByFacilityId(facility.getId());
	
		String documentDir = getOutputDirectory();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateOnFile = formatter.format(currentUpdateDate);
		String filename = "IntegratorPush_" + facility.getId() + "_" + dateOnFile;
		
		// set to collect a manifest of documents (and maybe labs) during the push process.
		// This manifest will be used to create a zipped package after the the initial serialization process is completed. 
		Set<Path> documentPaths = new HashSet<Path>();
		
		logger.info("This data snapshot will be timestamped with " + currentUpdateDate);
		
		try {
			//create the first file
			out = new ObjectOutputStream(new FileOutputStream(new File(documentDir + File.separator + filename + ".1.ser")));
		
			File documentMetaFile = new File(documentDir + File.separator + filename + "_documentMeta.txt");
			documentMetaWriter = new PrintWriter(new FileWriter(documentMetaFile));
			
			IntegratorFileHeader header = new IntegratorFileHeader();
			header.setDate(currentUpdateDate);
			header.setLastDate(lastDataUpdated);
			header.setDependsOn(lastFile!=null?lastFile.getChecksum():null);
			header.setCachedFacilityId(cachedFacility!=null?cachedFacility.getIntegratorFacilityId():null);
			header.setCachedFacilityName(cachedFacility!=null?cachedFacility.getName():null);
			header.setUsername(facility.getIntegratorUser());
			out.writeUnshared(header);
			
			pushFacility(out,facility, lastDataUpdated);
			pushProviders(out,lastDataUpdated, facility);
			pushPrograms(out,lastDataUpdated, facility);
			
			IOUtils.closeQuietly(out);
			out = new ObjectOutputStream(new FileOutputStream(new File(documentDir + File.separator + filename + ".2.ser")));
			
			int currentFileNumber = pushAllDemographics(documentDir + File.separator + filename, loggedInInfo, facility, lastDataUpdated, cachedFacility, programs, documentPaths);

			IOUtils.closeQuietly(out);
			out = new ObjectOutputStream(new FileOutputStream(new File(documentDir + File.separator + filename + "."+(++currentFileNumber)+".ser")));
			
			IntegratorFileFooter footer =new IntegratorFileFooter();

			out.writeUnshared(footer);
			
		}catch(IOException e) {
			logger.error("Error writing to integrator file. Cannot use the file needed to store the data. Aborting.", e);
			return;
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(documentMetaWriter);
		}
		
		//creates a zip (.zipTemp) and returns the checksum for the log
		String checksum = null;
		try {
			checksum = completeFile(documentDir,filename);
		} catch(Exception e) {
			throw new IOException(e);
		}
		
		//TODO is there any need to log or transmit a checksum for the documents? Technically the manifest should confirm them all?
		try {
			completeDocumentFile(documentDir, filename, documentPaths);
		} catch(Exception e) {
			throw new IOException(e);
		}
				
		//save this log
		integratorFileLogManager.saveNewFileData(filename + ".zip",checksum,lastDataUpdated,currentUpdateDate);
		
		//publish the file(s)
		try {
			Files.move(Paths.get(documentDir + File.separator + filename + ".zipTemp"), Paths.get(documentDir + File.separator + filename + ".zip"), StandardCopyOption.REPLACE_EXISTING);	
			
			Path documentzip = Paths.get(documentDir + File.separator + filename + COMPRESSED_DOCUMENTS_APPENDAGE + ".zipTemp");
			if(Files.exists(documentzip)) {
				Files.move(documentzip, Paths.get(documentDir + File.separator + filename + COMPRESSED_DOCUMENTS_APPENDAGE + ".zip"), StandardCopyOption.REPLACE_EXISTING);	
			}
		}catch(Exception e) {
			logger.error("Error renaming file",e);	
		}
				
		
		logger.info("Finished pushing data for facility : " + facility.getId() + " : " + facility.getName());
	}


	private void pushFacility(ObjectOutputStream out, Facility facility, Date lastDataUpdated) throws MalformedURLException, IOException {
		if (facility.getLastUpdated().after(lastDataUpdated)) {
			logger.debug("pushing facility record");

			CachedFacility cachedFacility = new CachedFacility();
			BeanUtils.copyProperties(facility, cachedFacility);

			
			out.writeUnshared(cachedFacility);
		} else {
			logger.debug("skipping facility record, not updated since last push");
		}
	}

	private void pushPrograms(ObjectOutputStream out, Date lastDataUpdated, Facility facility) throws MalformedURLException, IOException {
		List<Integer> programIds = programDao.getRecordsAddedAndUpdatedSinceTime(facility.getId(),lastDataUpdated);
		
		ArrayList<CachedProgram> cachedPrograms = new ArrayList<CachedProgram>();
		
		for (Integer programId : programIds) {
			Program program = programDao.getProgram(programId);
			
			logger.debug("pushing program : " + program.getId() + ':' + program.getName());
			
			CachedProgram cachedProgram = new CachedProgram();

			BeanUtils.copyProperties(program, cachedProgram);

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setCaisiItemId(program.getId());
			cachedProgram.setFacilityIdIntegerCompositePk(pk);

			try {
				cachedProgram.setGender(Gender.valueOf(program.getManOrWoman().toUpperCase()));
			} catch (Exception e) {
				
			}

			if (program.isTransgender()) cachedProgram.setGender(Gender.T);

			cachedProgram.setMaxAge(program.getAgeMax());
			cachedProgram.setMinAge(program.getAgeMin());
			cachedProgram.setStatus(program.getProgramStatus());

			cachedPrograms.add(cachedProgram);
		}

		if(cachedPrograms.size()>0) {
			out.writeUnshared(cachedPrograms);
		}
		
		List<Integer> allProgramIds = programDao.getRecordsByFacilityId(facility.getId());
		if(allProgramIds.size()>0) {
			out.writeUnshared(new ProgramDeleteIdWrapper(allProgramIds) );
		}
	}

	private void pushProviders(ObjectOutputStream out, Date lastDataUpdated, Facility facility) throws MalformedURLException, IOException {
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
			providerTransfer.setRoles(new ArrayList<Role>());
			CachedProvider cachedProvider = new CachedProvider();

			BeanUtils.copyProperties(provider, cachedProvider);

			FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
			pk.setCaisiItemId(provider.getProviderNo());
			cachedProvider.setFacilityIdStringCompositePk(pk);

			providerTransfer.setCachedProvider(cachedProvider);

			// copy roles over
			List<SecUserRole> roles = secUserRoleDao.getUserRoles(providerId);
			for (SecUserRole role : roles) {
				Role integratorRole = IntegratorRoleUtils.getIntegratorRole2(role.getRoleName());
				if (integratorRole != null) providerTransfer.getRoles().add(integratorRole);
			}

			providerTransfers.add(providerTransfer);
			
			if((++i % 50) == 0) {
				out.writeUnshared(providerTransfers);
				providerTransfers.clear();
			}
			
		}
		
		if(providerTransfers.size()>0) {
			out.writeUnshared(providerTransfers);
		}
	}
	
	private boolean isFullPush(Facility facility) {
		UserProperty fullPushProp = userPropertyDao.getProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId());
		
		if (OscarProperties.getInstance().isPropertyActive("INTEGRATOR_FORCE_FULL")) {
			return true;
		}
		
		if (fullPushProp != null && fullPushProp.getValue().equals("1")) {
			userPropertyDao.saveProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId(), "0"); 
			return true;
		}
		
		return false;
	}
	

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
				logger.debug( "Adding consented Demographic " + demographicNo + " to the Integrator push list" );
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
	
		List<Integer> fullFacilitydemographicIds = null;
		
		fullFacilitydemographicIds = DemographicDao.getDemographicIdsAdmittedIntoFacility(facility.getId());
		
		if ( isFullPush(facility) || lastDataUpdated.getTime() == 0 ) {
			logger.info("Integrator pushing ALL demographics");
			
			// check if patient consent module is active and then sort out all patients that 
			// have given consent
			if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
				logger.debug("Integrator patient consent is active. Checking demographic list.");				
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
	                if(! fullFacilitydemographicIds.contains(demo) ){
	                	demoIterator.remove();
	                } 
	                
	                else if( CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE && ! checkPatientConsent( demo ) ) {
	    				logger.debug("Integrator patient consent is active. Checking demographic list of changed demographics");				
	    				demoIterator.remove();
	    			}
	        }
	       
			
			return(new ArrayList<Integer>(uniqueDemographicIdsWithSomethingNew));
			
		}
	}


	protected int pushAllDemographics(String parentFilename, LoggedInInfo loggedInInfo, Facility facility,Date lastDataUpdated, 
			org.oscarehr.caisi_integrator.ws.CachedFacility cachedFacility, List<Program> programs, Set<Path> documentPaths) 
			throws IOException {
		
		List<Integer> demographicIds = getDemographicIdsToPush(facility,lastDataUpdated, programs);
		List<Program> programsInFacility = programDao.getProgramsByFacilityId(facility.getId());
		List<String> providerIdsInFacility = providerDao.getProviderIds(facility.getId());


		
		long startTime = System.currentTimeMillis();
		int demographicPushCount = 0;
		
		String documentDir = getOutputDirectory();
		int currentFileNumber = 2;
		
		boolean rid = integratorControlDao.readRemoveDemographicIdentity(facility.getId());
		
		
		//we could parallelize this. basically we want X records per file. So we just assign each unit of work to be
		//a set of demographicIds which will end up being a single file. so we need to know the file number and the ids for it
		
		
		for (Integer demographicId : demographicIds) {
		
			
			
			demographicPushCount++;

			BenchmarkTimer benchTimer = new BenchmarkTimer("pushing demo facilityId:" + facility.getId() + ", demographicId:" + demographicId + "  " + demographicPushCount + " of " + demographicIds.size());

			String filename = "IntegratorPush_" + facility.getId() + "_" + demographicId + ".ser";				
			ObjectOutputStream demoOut = null;
						
			try {
				
				if((demographicPushCount % 500) == 0) {
					IOUtils.closeQuietly(out);
					out = new ObjectOutputStream(new FileOutputStream(new File(parentFilename + "." +  (++currentFileNumber) + ".ser")));
					logger.info("starting a new file ("+currentFileNumber+")");
				}
				
				demoOut = new ObjectOutputStream(new FileOutputStream(new File(documentDir + File.separator + filename)));

				pushDemographic(demoOut,lastDataUpdated, facility, demographicId,rid);
				benchTimer.tag("pushDemographic");
				
				// Use alternate method for patient consents if the Patient Consent Module is off. 
				if( ! CaisiIntegratorUpdateTask.ISACTIVE_PATIENT_CONSENT_MODULE ) {
					pushDemographicConsent(demoOut,lastDataUpdated, facility, demographicId);
					benchTimer.tag("pushDemographicConsent");
				}
				
				pushDemographicIssues(demoOut,lastDataUpdated, facility, programsInFacility, demographicId, cachedFacility);
				benchTimer.tag("pushDemographicIssues");
				
				pushDemographicPreventions(demoOut,lastDataUpdated, facility, providerIdsInFacility, demographicId);
				benchTimer.tag("pushDemographicPreventions");
				
				pushDemographicNotes(demoOut,lastDataUpdated, facility, demographicId, programsInFacility);
				benchTimer.tag("pushDemographicNotes");
				
				pushDemographicDrugs(demoOut,lastDataUpdated, facility, providerIdsInFacility, demographicId);
				benchTimer.tag("pushDemographicDrugs");
				
				pushAdmissions(demoOut,lastDataUpdated, facility, programsInFacility, demographicId);
				benchTimer.tag("pushAdmissions");
				
				pushAppointments(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushAppointments");
				
				pushMeasurements(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushMeasurements");
				
				pushDxresearchs(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushDxresearchs");
				
				
				if( OscarProperties.getInstance().isOntarioBillingRegion() ) {
					pushBillingItems(demoOut,lastDataUpdated, facility, demographicId);
					benchTimer.tag("pushBillingItems");
				}

				pushEforms(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushEforms");

				pushAllergies(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushAllergies");
				
				pushDocuments(demoOut,loggedInInfo, lastDataUpdated, facility, demographicId, documentPaths);
				benchTimer.tag("pushDocuments");
				
				pushForms(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushForms");
				
				pushLabResults(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushLabResults");
				
				pushHL7LabResults(demoOut,lastDataUpdated, facility, demographicId);
				benchTimer.tag("pushHL7LabResults");
				
				
				logger.debug(benchTimer.report());

				DbConnectionFilter.releaseAllThreadDbResources();
				
				demoOut.flush();
				
			} catch( IOException e) {
				logger.error("Error creating patient file. Cannot create the file to send to integrator for this patient ("+demographicId+"), skipping.", e);
				cleanFile(filename);
				continue;
			} catch (IllegalArgumentException iae) {
				// continue processing demographics if date values in current demographic are bad
				// all other errors thrown by the above methods should indicate a failure in the service
				// connection at large -- continuing to process not possible
				// need some way of notification here.
				logger.error("Error updating demographic "+demographicId+", continuing with Demographic batch", iae);
				cleanFile(filename);
				continue;
			} catch (Exception e) {
				logger.error("Unexpected error during processing of " + demographicId, e);
				cleanFile(filename);
				continue;
			} finally {
				IOUtils.closeQuietly(demoOut);
			}
			
			
			//Now we add the completed demographic to the main file, and delete the demographic one.
			ObjectInputStream ois = null;
			FileInputStream fis = null;
			
			try {
				File f = new File(documentDir + File.separator + filename);
				fis = new FileInputStream(f);
				ois = new ObjectInputStream(fis);
				
				Object obj = null;
				
				while(true) {
					try {
						obj = ois.readUnshared();
						
					}catch(EOFException eofEx) {
						break;
					}
					out.writeUnshared(obj);
				}
				
				
				obj = null;
				fis.close();
				ois.close();
				
				fis = null;
				ois = null;
			
			}catch(ClassNotFoundException e) {
				throw new RuntimeException("This should never happen",e);
			}catch(IOException e) {
				throw e;
			} finally {
				IOUtils.closeQuietly(fis);
				IOUtils.closeQuietly(ois);
			}
			//the exceptions above could corrupt the file, so we have to abandon by rethrowing an exception up instead of a continue
		
			//delete the file
			boolean deleted = new File(documentDir + File.separator + filename).delete();
			if(!deleted) {
				logger.warn("unable to delete temp demographic file");
			}
		}
		
		logger.debug("Total pushAllDemographics :" + (System.currentTimeMillis() - startTime));
		return currentFileNumber;
	}
	
	private void cleanFile(String filename) {
		new File(filename).delete();
	}

	//TODO: DemographicExt are not sent
	private void pushDemographic(ObjectOutputStream demoOut, Date lastDataUpdated, Facility facility, Integer demographicId, boolean rid) throws IOException {
		DemographicTransfer demographicTransfer = new DemographicTransfer();

		// set demographic info
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		
		//we can remove this once we know our demographic id list is good
		if(demographic.getLastUpdateDate().before(lastDataUpdated)) return;
		

		String ignoreProperties[] = { "lastUpdateDate" };
		BeanUtils.copyProperties(demographic, demographicTransfer, ignoreProperties);

		demographicTransfer.setCaisiDemographicId(demographic.getDemographicNo());
		demographicTransfer.setBirthDate(demographic.getBirthDay().getTime());

		demographicTransfer.setHinType(demographic.getHcType());
		demographicTransfer.setHinVersion(demographic.getVer());
		demographicTransfer.setHinValidEnd(demographic.getHcRenewDate());
		demographicTransfer.setHinValidStart(demographic.getEffDate());
		demographicTransfer.setCaisiProviderId(demographic.getProviderNo());

		demographicTransfer.setStreetAddress(demographic.getAddress());
		demographicTransfer.setPhone1(demographic.getPhone());
		demographicTransfer.setPhone2(demographic.getPhone2());

		demographicTransfer.setLastUpdateDate(demographic.getLastUpdateDate());

		try {
			demographicTransfer.setGender(Gender.valueOf(demographic.getSex().toUpperCase()));
		} catch (Exception e) {
			logger.warn("Error mapping gender on demographic " + demographic.getDemographicNo() + "(" + demographic.getSex() + ")");
		}

		// set image
		ClientImage clientImage = clientImageDAO.getClientImage(demographicId);
		if (clientImage != null) {
			demographicTransfer.setPhoto(clientImage.getImage_data());
			demographicTransfer.setPhotoUpdateDate(clientImage.getUpdate_date());
		}

		// set flag to remove demographic identity
		demographicTransfer.setRemoveId(rid);
		
		demoOut.writeUnshared(demographicTransfer);

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
		List<Consent> consents = patientConsentManager.getConsentsByTypeAndEditDate( loggedinInfo, this.consentType, lastDataUpdated );
		logger.debug("Updating last edited consent list after " +  lastDataUpdated);

		if(consents != null) {
			for( Consent consent : consents ) {				
				try {
					CaisiIntegratorManager.pushConsent(loggedinInfo, facility, consent);
					logger.debug("pushDemographicConsent:" + consent.getId() + "," + facility.getId() + "," + consent.getDemographicNo());
				} catch (MalformedURLException e) {
					logger.error("Error while pushing consent via webservices. Consent ID: " + consent.getId(), e);
					continue;
				}
			}
		}
	}

	private void pushDemographicConsent(ObjectOutputStream out, Date lastUpdatedData, Facility facility,  Integer demographicId) throws IOException {
		// find the latest relevant consent that needs to be pushed.
		List<IntegratorConsent> integratorConsentList = integratorConsentDao.findByFacilityAndDemographicSince(facility.getId(), demographicId, lastUpdatedData);
		for(IntegratorConsent integratorConsent : integratorConsentList) {
			org.oscarehr.caisi_integrator.ws.transfer.SetConsentTransfer consentTransfer = CaisiIntegratorManager.makeSetConsentTransfer2(integratorConsent);
			out.writeUnshared(consentTransfer);
		}
		
	}

	private void pushDemographicIssues(ObjectOutputStream out, Date lastDataUpdated, Facility facility, List<Program> programsInFacility, Integer demographicId, org.oscarehr.caisi_integrator.ws.CachedFacility cachedFacility) throws IOException {
		logger.debug("pushing demographicIssues facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.issues.disabled", "false"))) {
			return;
		}
		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDAO.getIssuesByDemographicSince(demographicId.toString(),lastDataUpdated);
		StringBuilder sentIds = new StringBuilder();
		if (caseManagementIssues.size() == 0) return;
		
	
		for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
			// don't send issue if it is not in our facility.
			//logger.debug("Facility:" + facility.getName() + " - caseManagementIssue = " + caseManagementIssue.toString());
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
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.ICD9);
			}
			else if( Issue.ICD_10.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.ICD10);
			}
			else if( Issue.SNOMED.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.SNOMED);
			}
			else if( Issue.SNOMED_CORE.equalsIgnoreCase(issue.getType()) ) {
				facilityDemographicIssuePrimaryKey.setCodeType(CodeType.SNOMED_CORE);
			}
			else {
				logger.warn("UNKNOWN ISSUE TYPE. " + issue.getType() + " ID:" + issue.getId() + " SKIPPING...");
				continue;
			}
			
			facilityDemographicIssuePrimaryKey.setIssueCode(issue.getCode());
			cachedDemographicIssue.setFacilityDemographicIssuePk(facilityDemographicIssuePrimaryKey);

			BeanUtils.copyProperties(caseManagementIssue, cachedDemographicIssue);
			cachedDemographicIssue.setIssueDescription(issue.getDescription());
			cachedDemographicIssue.setIssueRole(IntegratorRoleUtils.getIntegratorRole2(issue.getRole()));

			ArrayList<CachedDemographicIssue> issues = new ArrayList<CachedDemographicIssue>();
			issues.add(cachedDemographicIssue);

			out.writeUnshared(issues);
			sentIds.append("," + caseManagementIssue.getId());
			
			facilityDemographicIssuePrimaryKey = null;
			cachedDemographicIssue = null;
			issues = null;
			
		}
		
		if(cachedFacility != null) {

			List<FacilityIdDemographicIssueCompositePk> b = new ArrayList<FacilityIdDemographicIssueCompositePk>();
			for(org.oscarehr.caisi_integrator.ws.FacilityIdDemographicIssueCompositePk c: caseManagementIssueDAO.getIssueIdsForIntegrator(cachedFacility.getIntegratorFacilityId(),demographicId)) {
				FacilityIdDemographicIssueCompositePk n = new FacilityIdDemographicIssueCompositePk();
				n.setCaisiDemographicId(c.getCaisiDemographicId());
				n.setCodeType(CodeType.valueOf(c.getCodeType().value()));
				n.setIntegratorFacilityId(c.getIntegratorFacilityId());
				n.setIssueCode(c.getIssueCode());
				
				b.add(n);
			}
			DeleteCachedDemographicIssuesWrapper wrapper = new DeleteCachedDemographicIssuesWrapper(demographicId, b);
			out.writeUnshared(wrapper);
		}
		conformanceTestLog(facility, "CaseManagementIssue", sentIds.toString());
	}

	private void pushAdmissions(ObjectOutputStream out,Date lastDataUpdated, Facility facility, List<Program> programsInFacility, Integer demographicId) throws IOException {
		logger.debug("pushing admissions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.admissions.disabled", "false"))) {
			return;
		}
		
		List<Admission> admissions = admissionDao.getAdmissionsByFacilitySince(demographicId, facility.getId(),lastDataUpdated);
		StringBuilder sentIds = new StringBuilder();
		if (admissions.size() == 0) return;
		ArrayList<CachedAdmission> cachedAdmissions = new ArrayList<CachedAdmission>();
		
		for (Admission admission : admissions) {

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
			//missing status from the whole transfer?
			
			cachedAdmissions.clear();
			cachedAdmissions.add(cachedAdmission);
			out.writeUnshared(cachedAdmissions);
			
			sentIds.append("," + admission.getId());
		}
		
		conformanceTestLog(facility, "Admission", sentIds.toString());
	}

	private boolean isProgramIdInProgramList(List<Program> programList, int programId) {
		for (Program p : programList) {
			if (p.getId().intValue() == programId) return (true);
		}

		return (false);
	}

	private void pushDemographicPreventions(ObjectOutputStream demoOut,Date lastDataUpdated, Facility facility, List<String> providerIdsInFacility, Integer demographicId) throws IOException, ParserConfigurationException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.debug("pushing demographicPreventions facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.preventions.disabled", "false"))) {
			return;
		}
		
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

			
			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setCaisiItemId(localPrevention.getId());
			cachedDemographicPrevention.setFacilityPreventionPk(pk);
			

			cachedDemographicPrevention.setNextDate(localPrevention.getNextDate());
			cachedDemographicPrevention.setPreventionDate(localPrevention.getPreventionDate());
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

			preventionsToSend.clear();
			preventionsToSend.add(cachedDemographicPrevention);

			demoOut.writeUnshared(preventionsToSend);
			
			cachedDemographicPrevention = null;
			pk = null;
			doc = null;
			
			
			sentIds.append("," + localPrevention.getId());
		}
		conformanceTestLog(facility, "Prevention", sentIds.toString());

		//let integrator know our current and active list of preventions for this patient. The integrator will delete all not found in this list in it's db.
		DeleteCachedDemographicPreventionsWrapper wrapper = new DeleteCachedDemographicPreventionsWrapper(demographicId, preventionDao.findNonDeletedIdsByDemographic(demographicId));
		demoOut.writeUnshared(wrapper);
		
		wrapper = null;
	}

	private void pushDocuments(ObjectOutputStream out,LoggedInInfo loggedInInfo, Date lastDataUpdated, Facility facility, Integer demographicId, Set<Path> documentPaths) throws IOException, ParseException  {

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.documents.disabled", "false"))) {
			logger.debug("Pushing documents is disabled: integrator.send.documents.disabled = false");
			return;
		}
		
		logger.debug("pushing demographicDocuments edited after " + lastDataUpdated + " for facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		// Get ALL PRIVATE documents with a LAST DATE EDITED on or after the lastDataUpdated date for a specific DEMOGRAPHIC (no need to sort them).
		List<EDoc> privateDocs = EDocUtil.listAllDemographicDocsSince(loggedInInfo, demographicId, lastDataUpdated);
		
		StringBuilder sentIds = new StringBuilder();

		for (EDoc eDoc : privateDocs) {
			
			Path documentPath = sendSingleDocument(out, eDoc, demographicId);
			
			if(documentPath == null) {
				continue;
			}

			// Add this confirmed file path to the manifest.
			if(documentPaths != null) {
				documentPaths.add(documentPath);
			}
			
			documentMetaWriter.println( eDoc.getDocId()
					+ "," 
					+ demographicId 
					+ "," 
					+ documentPath.getFileName());

			sentIds.append("," + eDoc.getDocId());
		}

		conformanceTestLog(facility, "EDoc", sentIds.toString());

	}

	private Path sendSingleDocument(ObjectOutputStream out, EDoc eDoc, Integer demographicId) throws IOException, ParseException  {
		
		// ensure the document is actually in the file system
		Path documentPath = Paths.get( OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + eDoc.getFileName() );
		if(! Files.exists(documentPath) ) {
			logger.warn("Unable to send document - the file does not exist or can't be read!! " 
					+  OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + '/' + eDoc.getFileName());
			return null;
		}
		
		// send this document
		CachedDemographicDocument cachedDemographicDocument = new CachedDemographicDocument();
		FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
		facilityIdIntegerCompositePk.setCaisiItemId(Integer.parseInt(eDoc.getDocId()));
		cachedDemographicDocument.setFacilityIntegerPk(facilityIdIntegerCompositePk);

		if(eDoc.getAppointmentNo() != null) {
			cachedDemographicDocument.setAppointmentNo(eDoc.getAppointmentNo());
		}
		cachedDemographicDocument.setCaisiDemographicId(demographicId);
		cachedDemographicDocument.setContentType(eDoc.getContentType());
		cachedDemographicDocument.setDocCreator(eDoc.getCreatorId());
		cachedDemographicDocument.setDocFilename(eDoc.getFileName());
		cachedDemographicDocument.setDocType(eDoc.getType());
		cachedDemographicDocument.setDocXml(eDoc.getHtml());
		cachedDemographicDocument.setNumberOfPages(eDoc.getNumberOfPages());
		cachedDemographicDocument.setObservationDate( org.oscarehr.util.DateUtils.parseIsoDateAsCalendar(eDoc.getObservationDate()).getTime() );
		cachedDemographicDocument.setProgramId(eDoc.getProgramId());
		cachedDemographicDocument.setPublic1(Integer.parseInt(eDoc.getDocPublic()));
		cachedDemographicDocument.setResponsible(eDoc.getResponsibleId());
		cachedDemographicDocument.setReviewDateTime(eDoc.getReviewDateTimeDate());
		cachedDemographicDocument.setReviewer(eDoc.getReviewerId());
		cachedDemographicDocument.setSource(eDoc.getSource());
		cachedDemographicDocument.setStatus("" + eDoc.getStatus());
		cachedDemographicDocument.setUpdateDateTime(eDoc.getDateTimeStampAsDate());
		cachedDemographicDocument.setDescription(eDoc.getDescription());

//		byte[] contents = EDocUtil.getFile(OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + '/' + eDoc.getFileName());
//		if(contents == null) {
//			logger.warn("Unable to send document - the file does not exist or can't be read!! " +  OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + '/' + eDoc.getFileName());
//			return;
//		}
		out.writeUnshared(cachedDemographicDocument);

		return documentPath;
		
	}

	private void pushHL7LabResults(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing pushHL7LabResults facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.labs.disabled", "false"))) {
			return;
		}
		
		CommonLabResultData comLab = new CommonLabResultData();
		
		//TODO:we need to check the patient lab routing table on it's own too..the case where a lab is updated, but the link is done later
		OscarProperties op = OscarProperties.getInstance();
		String hl7text = op.getProperty("HL7TEXT_LABS","false");
		  
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		
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
				CachedDemographicHL7LabResult cachedDemographicLabResult = makeCachedDemographicHL7LabResult(demographicId, lab);
				out.writeUnshared(cachedDemographicLabResult);
				sentIds.append("," + lab.getLabPatientId() + ":" + lab.labType + ":" + lab.segmentID);
			} else {
				logger.warn("Lab missing!!! " + labIdAndType.getLabType() + ":"+ labIdAndType.getLabId());
			}
		}
		
		conformanceTestLog(facility, "LabResultData", sentIds.toString());

	}
	
	private void pushLabResults(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException, ParserConfigurationException, UnsupportedEncodingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.debug("pushing pushLabResults facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.labs.disabled", "false"))) {
			return;
		}
		
		CommonLabResultData comLab = new CommonLabResultData();
		
		//TODO:we need to check the patient lab routing table on it's own too..the case where a lab is updated, but the link is done later
		OscarProperties op = OscarProperties.getInstance();
		String cml = op.getProperty("CML_LABS","false");
		String mds = op.getProperty("MDS_LABS","false");
		String pathnet = op.getProperty("PATHNET_LABS","false");
		String epsilon = op.getProperty("Epsilon_LABS","false");
		
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		if("yes".equals(epsilon) || "yes".equals(cml))
			results.addAll(comLab.getCmlAndEpsilonLabResultsSince(demographicId, lastDataUpdated));
		if("yes".equals(mds))
			results.addAll(comLab.getMdsLabResultsSince(demographicId, lastDataUpdated));
		if("yes".equals(pathnet))
			results.addAll(comLab.getPathnetResultsSince(demographicId, lastDataUpdated));
		 
		for(LabIdAndType id:results) {
			logger.debug("id="+id.getLabId() + ",type=" + id.getLabType());
		}
	
		StringBuilder sentIds = new StringBuilder();
		
		if (results.size() == 0) return;

		
		for (LabIdAndType labIdAndType : results) {
			LabResultData lab = comLab.getLab(labIdAndType);
			if(lab != null) {
				CachedDemographicLabResult cachedDemographicLabResult = makeCachedDemographicLabResult(demographicId, lab);
				out.writeUnshared(cachedDemographicLabResult);
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
	
	private CachedDemographicHL7LabResult makeCachedDemographicHL7LabResult(Integer demographicId, LabResultData lab) 
	{
		CachedDemographicHL7LabResult cachedDemographicLabResult = new CachedDemographicHL7LabResult();

		FacilityIdLabResultCompositePk pk = new FacilityIdLabResultCompositePk();
		// our attempt at making a fake pk....
		String key = LabDisplayHelper.makeLabKey(demographicId, lab.getSegmentID(), lab.labType, lab.getDateTime());
		pk.setLabResultId(key);
		cachedDemographicLabResult.setFacilityIdLabResultCompositePk(pk);

		cachedDemographicLabResult.setCaisiDemographicId(demographicId);
		cachedDemographicLabResult.setType(lab.labType);


		Hl7TextMessageDao hl7TextMessageDao = SpringUtils.getBean(Hl7TextMessageDao.class);
		Hl7TextMessage tMsg = hl7TextMessageDao.find(Integer.parseInt(lab.getSegmentID()));
		
		if(tMsg == null) {
			return null;
		}
		cachedDemographicLabResult.setData(tMsg.getBase64EncodedeMessage());

		return (cachedDemographicLabResult);
	}

	private void pushForms(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws SQLException, IOException, ParseException {
		logger.debug("pushing demographic forms facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.forms.disabled", "false"))) {
			return;
		}
		
		if( OscarProperties.getInstance().isOntarioBillingRegion() ) {
			// LabReq2007 and up is only used in Ontario
			pushLabReq2007(out, lastDataUpdated, facility, demographicId);	
		}
	}

	private void pushLabReq2007(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws SQLException, IOException, ParseException {
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
			cachedDemographicForm.setEditDate(date);
			cachedDemographicForm.setFormName("formLabReq07");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			p.store(baos, null);
			cachedDemographicForm.setFormData(baos.toString());

			out.writeUnshared(cachedDemographicForm);
			sentIds.append("," + p.getProperty("ID"));
		}

		conformanceTestLog(facility, "formLabReq07", sentIds.toString());
	}

	private void pushDemographicNotes(ObjectOutputStream demoOut, Date lastDataUpdated, Facility facility, Integer demographicId, List<Program> programs) throws IOException {
		logger.debug("pushing demographicNotes facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.notes.disabled", "false"))) {
			return;
		}
		
		HashSet<Integer> programIds = new HashSet<Integer>();
		for (Program program : programs)
			programIds.add(program.getId());

		List<CaseManagementNote> localNotes = caseManagementNoteDAO.getNotesByDemographic(demographicId.toString());

		StringBuilder sentIds = new StringBuilder();
		ArrayList<CachedDemographicNote> notesToSend = new ArrayList<CachedDemographicNote>();
		
		for (CaseManagementNote localNote : localNotes) {	
			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || (localNote.getProgram_no() != null && localNote.getProgram_no().length()>0 && !programIds.contains(Integer.parseInt(localNote.getProgram_no())))) continue;

				// note hasn't changed since last sync
				if (localNote.getUpdate_date() != null && localNote.getUpdate_date().before(lastDataUpdated)) continue;

				CachedDemographicNote noteToSend = makeRemoteNote(localNote);
				notesToSend.clear();
				notesToSend.add(noteToSend);
				demoOut.writeUnshared(notesToSend);

				noteToSend = null;
				
				sentIds.append("," + localNote.getId());
			} catch (NumberFormatException e) {
				logger.error("Unexpected error. ProgramNo=" + localNote.getProgram_no(), e);
			}
		}

		conformanceTestLog(facility, "CaseManagementNote", sentIds.toString());
		sentIds = new StringBuilder();

		// add group notes as well.
		logger.debug("checking for group notes for " + demographicId);
		List<GroupNoteLink> noteLinks = groupNoteDao.findLinksByDemographic(demographicId);
		logger.debug("found " + noteLinks.size() + " group notes for " + demographicId);
		for (GroupNoteLink noteLink : noteLinks) {
			int orginalNoteId = noteLink.getNoteId();
			CaseManagementNote localNote = caseManagementNoteDAO.getNote(Long.valueOf(orginalNoteId));
			localNote.setDemographic_no(String.valueOf(demographicId));

			try {
				// if it's locked or if it's not in this facility ignore it.
				if (localNote.isLocked() || !programIds.contains(Integer.parseInt(localNote.getProgram_no()))) continue;

				CachedDemographicNote noteToSend = makeRemoteNote(localNote);
				notesToSend.clear();
				notesToSend.add(noteToSend);
				demoOut.writeUnshared(notesToSend);
				logger.debug("adding group note to send");

				sentIds.append("," + noteLink.getId());
			} catch (NumberFormatException e) {
				logger.error("Unexpected error. ProgramNo=" + localNote.getProgram_no(), e);
			}

		}

		conformanceTestLog(facility, "GroupNoteLink", sentIds.toString());

	}

	private CachedDemographicNote makeRemoteNote(CaseManagementNote localNote) {

		CachedDemographicNote note = new CachedDemographicNote();

		CachedDemographicNoteCompositePk pk = new CachedDemographicNoteCompositePk();
		pk.setUuid(localNote.getUuid() + ":" + localNote.getDemographic_no());
		note.setCachedDemographicNoteCompositePk(pk);

		note.setCaisiDemographicId(Integer.parseInt(localNote.getDemographic_no()));
		if(localNote.getProgram_no() != null && localNote.getProgram_no().length()>0) {
			note.setCaisiProgramId(Integer.parseInt(localNote.getProgram_no()));
		}
		note.setEncounterType(localNote.getEncounter_type());
		note.setNote(localNote.getNote());
		note.setObservationCaisiProviderId(localNote.getProviderNo());
		note.setObservationDate(localNote.getObservation_date());
		note.setRole(localNote.getRoleName());
		note.setSigningCaisiProviderId(localNote.getSigning_provider_no());
		note.setUpdateDate(localNote.getUpdate_date());

		Set<NoteIssue> issues = note.getIssues();
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
				noteIssue.setCodeType(CodeType.ICD10);
			}
			else if( Issue.ICD_9.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.ICD9);
			}
			else if( Issue.SNOMED.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.SNOMED);
			}
			else if( Issue.SNOMED_CORE.equalsIgnoreCase(issueCodeType) ) {
				noteIssue.setCodeType(CodeType.SNOMED_CORE);
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

	private void pushDemographicDrugs(ObjectOutputStream out, Date lastDataUpdated, Facility facility, List<String> providerIdsInFacility, Integer demographicId) throws IOException {
		logger.debug("pushing demographicDrugss facilityId:" + facility.getId() + ", demographicId:" + demographicId);
		
		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.drugs.disabled", "false"))) {
			return;
		}
		
		StringBuilder sentIds = new StringBuilder();

		List<Drug> drugs = drugDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (drugs == null || drugs.size() == 0) return;

		if (drugs != null) {
			for (Drug drug : drugs) {
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
				if (drug.getScriptNo() != null) cachedDemographicDrug.setScriptNo(drug.getScriptNo());
				cachedDemographicDrug.setSpecial(drug.getSpecial());
				cachedDemographicDrug.setTakeMax(drug.getTakeMax());
				cachedDemographicDrug.setTakeMin(drug.getTakeMin());
				cachedDemographicDrug.setUnit(drug.getUnit());
				cachedDemographicDrug.setUnitName(drug.getUnitName());

				ArrayList<CachedDemographicDrug> drugsToSend = new ArrayList<CachedDemographicDrug>();
				drugsToSend.add(cachedDemographicDrug);
				out.writeUnshared(drugsToSend);
				sentIds.append("," + drug.getId());
			}
		}

		
		conformanceTestLog(facility, "Drug", sentIds.toString());
	}

	private void pushAllergies(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing demographicAllergies facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.allergies.disabled", "false"))) {
			return;
		}
		
		AllergyDao allergyDao = (AllergyDao) SpringUtils.getBean("allergyDao");
		List<Allergy> allergies = allergyDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (allergies.size() == 0) return;
		ArrayList<CachedDemographicAllergy> cachedAllergies = new ArrayList<CachedDemographicAllergy>();
		
		StringBuilder sentIds = new StringBuilder();

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
			cachedAllergy.setEntryDate(allergy.getEntryDate());
			
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
			if (allergy.getStartDate() != null) cachedAllergy.setStartDate(allergy.getStartDate());
			cachedAllergy.setTypeCode(allergy.getTypeCode());

			cachedAllergies.add(cachedAllergy);
			sentIds.append("," + allergy.getAllergyId());
		}
		
		if(cachedAllergies.size() > 0) {
			out.writeUnshared(cachedAllergies);
		}

		conformanceTestLog(facility, "Allergy", sentIds.toString());
	}

	private void pushAppointments(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException  {
		logger.debug("pushing appointments facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.appointments.disabled", "false"))) {
			return;
		}
		
		List<Appointment> appointments = appointmentDao.getAllByDemographicNoSince(demographicId,lastDataUpdated);
		if (appointments.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();
		ArrayList<CachedAppointment> cachedAppointments = new ArrayList<CachedAppointment>();
		
		for (Appointment appointment : appointments) {
			
			CachedAppointment cachedAppointment = new CachedAppointment();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(appointment.getId());
			cachedAppointment.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedAppointment.setAppointmentDate(appointment.getAppointmentDate());
			cachedAppointment.setCaisiDemographicId(demographicId);
			cachedAppointment.setCaisiProviderId(appointment.getProviderNo());
			cachedAppointment.setCreateDatetime(appointment.getCreateDateTime());
			cachedAppointment.setEndTime(appointment.getEndTime());
			cachedAppointment.setLocation(appointment.getLocation());
			cachedAppointment.setNotes(appointment.getNotes());
			cachedAppointment.setReason(appointment.getReason());
			cachedAppointment.setRemarks(appointment.getRemarks());
			cachedAppointment.setResources(appointment.getResources());
			cachedAppointment.setStartTime(appointment.getStartTime());
			cachedAppointment.setStatus(appointment.getStatus());
			cachedAppointment.setStyle(appointment.getStyle());
			cachedAppointment.setType(appointment.getType());
			cachedAppointment.setUpdateDatetime(appointment.getUpdateDateTime());

			cachedAppointments.clear();
			cachedAppointments.add(cachedAppointment);
			out.writeUnshared(cachedAppointments);
			sentIds.append("," + appointment.getId());
		}
		
		conformanceTestLog(facility, "Appointment", sentIds.toString());
	}

	private void pushDxresearchs(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing dxresearchs facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.dx.disabled", "false"))) {
			return;
		}
		
		List<Dxresearch> dxresearchs = dxresearchDao.getByDemographicNoSince(demographicId,lastDataUpdated);
		if (dxresearchs.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();
		ArrayList<CachedDxresearch> cachedDxresearchs = new ArrayList<CachedDxresearch>();
		
		for (Dxresearch dxresearch : dxresearchs) {
			
			CachedDxresearch cachedDxresearch = new CachedDxresearch();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(dxresearch.getId().intValue());
			cachedDxresearch.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedDxresearch.setCaisiDemographicId(demographicId);
			cachedDxresearch.setDxresearchCode(dxresearch.getDxresearchCode());
			cachedDxresearch.setCodingSystem(dxresearch.getCodingSystem());
			cachedDxresearch.setStartDate(dxresearch.getStartDate());
			cachedDxresearch.setUpdateDate(dxresearch.getUpdateDate());
			cachedDxresearch.setStatus(String.valueOf(dxresearch.getStatus()));

			cachedDxresearchs.add(cachedDxresearch);		
			sentIds.append("," + dxresearch.getId());
		}
		
		out.writeUnshared(cachedDxresearchs);
		
		
		conformanceTestLog(facility, "DxResearch", sentIds.toString());
	}

	private void pushBillingItems(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing billingitems facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.billing.disabled", "false"))) {
			return;
		}
		
		List<BillingONCHeader1> billingCh1s = billingONItemDao.getCh1ByDemographicNoSince(demographicId, lastDataUpdated);
		//don't think we need this based on my tests, but ideally, you'd want to check the timestamps of the billing items, and make sure you have a 
		//full list of billing headers..but I couldn't replicate editing a bill without the header being updated..so i'll just leave this
		//as a note.
		
		if (billingCh1s.size() == 0) return;

		ArrayList<CachedBillingOnItem> cachedBillingOnItems = new ArrayList<CachedBillingOnItem>();
		
		
		for (BillingONCHeader1 billingCh1 : billingCh1s) {
			List<BillingONItem> billingItems = billingONItemDao.getBillingItemByCh1Id(billingCh1.getId());
			for (BillingONItem billingItem : billingItems) {
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
				cachedBillingONItem.setServiceDate(billingItem.getServiceDate());
				cachedBillingONItem.setStatus(billingItem.getStatus());

				cachedBillingOnItems.add(cachedBillingONItem);
			}
			out.writeUnshared(cachedBillingOnItems);
		}

	}

	private void pushEforms(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing eforms facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.eforms.disabled", "false"))) {
			return;
		}
		
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
			cachedEformData.setFormDate(eformData.getFormDate());
			cachedEformData.setFormTime(eformData.getFormTime());
			cachedEformData.setFormId(eformData.getFormId());
			cachedEformData.setFormName(eformData.getFormName());
			cachedEformData.setFormData(eformData.getFormData());
			cachedEformData.setSubject(eformData.getSubject());
			cachedEformData.setStatus(eformData.isCurrent());
			cachedEformData.setFormProvider(eformData.getProviderNo());

			cachedEformDatas.add(cachedEformData);
			
			if((++i % 50) == 0) {
				out.writeUnshared(cachedEformDatas);
				cachedEformDatas.clear();
			}
			
			
			sentIds.append("," + eformData.getId());
			fdids.add(eformData.getId());
		}
		
		if(cachedEformDatas.size()>0) {
			out.writeUnshared(cachedEformDatas);
		}

		conformanceTestLog(facility, "EFormData", sentIds.toString());

		if("false".equals(OscarProperties.getInstance().getProperty("integrator.send.eform_values.disabled", "true"))) {
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
					out.writeUnshared(cachedEformValues);
					cachedEformValues.clear();
				}
				
			}
	
			if(cachedEformValues.size() > 0) {
				out.writeUnshared(cachedEformValues);
			}
		}
	}

	private void pushMeasurements(ObjectOutputStream out, Date lastDataUpdated, Facility facility, Integer demographicId) throws IOException {
		logger.debug("pushing measurements facilityId:" + facility.getId() + ", demographicId:" + demographicId);

		if("true".equals(OscarProperties.getInstance().getProperty("integrator.send.measurements.disabled", "false"))) {
			return;
		}
		
		List<Measurement> measurements = measurementDao.findByDemographicIdUpdatedAfterDate(demographicId, lastDataUpdated);
		if (measurements.size() == 0) return;

		StringBuilder sentIds = new StringBuilder();

		ArrayList<CachedMeasurement> cachedMeasurements = new ArrayList<CachedMeasurement>();
		
		
		for (Measurement measurement : measurements) {
			CachedMeasurement cachedMeasurement = new CachedMeasurement();
			FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
			facilityIdIntegerCompositePk.setCaisiItemId(measurement.getId());
			cachedMeasurement.setFacilityIdIntegerCompositePk(facilityIdIntegerCompositePk);

			cachedMeasurement.setCaisiDemographicId(demographicId);
			cachedMeasurement.setCaisiProviderId(measurement.getProviderNo());
			cachedMeasurement.setComments(measurement.getComments());
			cachedMeasurement.setDataField(measurement.getDataField());
			cachedMeasurement.setDateEntered(measurement.getCreateDate());
			cachedMeasurement.setDateObserved(measurement.getDateObserved());
			cachedMeasurement.setMeasuringInstruction(measurement.getMeasuringInstruction());
			cachedMeasurement.setType(measurement.getType());

			cachedMeasurements.clear();
			cachedMeasurements.add(cachedMeasurement);
			out.writeUnshared(cachedMeasurements);

			sentIds.append("," + measurement.getId());

			if("false".equals(OscarProperties.getInstance().getProperty("integrator.send.measurementExts.disabled", "true"))) {
				List<MeasurementsExt> measurementExts = measurementsExtDao.getMeasurementsExtByMeasurementId(measurement.getId());
				for (MeasurementsExt measurementExt : measurementExts) {
					CachedMeasurementExt cachedMeasurementExt = new CachedMeasurementExt();
					FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
					fidIntegerCompositePk.setCaisiItemId(measurementExt.getId());
					cachedMeasurementExt.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);
	
					cachedMeasurementExt.setMeasurementId(measurementExt.getMeasurementId());
					cachedMeasurementExt.setKeyval(measurementExt.getKeyVal());
					cachedMeasurementExt.setVal(measurementExt.getVal());
	
					ArrayList<CachedMeasurementExt> cachedMeasurementExts = new ArrayList<CachedMeasurementExt>();
					cachedMeasurementExts.add(cachedMeasurementExt);
					out.writeUnshared(cachedMeasurementExts);
				}
			}
			if("false".equals(OscarProperties.getInstance().getProperty("integrator.send.measurementTypes.disabled", "true"))) {
				List<MeasurementType> measurementTypes = measurementTypeDao.findByType(measurement.getType());
				for (MeasurementType measurementType : measurementTypes) {
					CachedMeasurementType cachedMeasurementType = new CachedMeasurementType();
					FacilityIdIntegerCompositePk fidIntegerCompositePk = new FacilityIdIntegerCompositePk();
					fidIntegerCompositePk.setCaisiItemId(measurementType.getId());
					cachedMeasurementType.setFacilityIdIntegerCompositePk(fidIntegerCompositePk);
	
					cachedMeasurementType.setType(measurementType.getType());
					cachedMeasurementType.setTypeDescription(measurementType.getTypeDescription());
					cachedMeasurementType.setMeasuringInstruction(measurementType.getMeasuringInstruction());
	
					ArrayList<CachedMeasurementType> cachedMeasurementTypes = new ArrayList<CachedMeasurementType>();
					cachedMeasurementTypes.add(cachedMeasurementType);
					out.writeUnshared(cachedMeasurementTypes);
				}
			}
			if("false".equals(OscarProperties.getInstance().getProperty("integrator.send.measurementMaps.disabled", "true"))) {
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
					out.writeUnshared(cachedMeasurementMaps);
				}
			}
		}

		conformanceTestLog(facility, "Measurements", sentIds.toString());
	}
	
	
	/*
	1) demographicWs.getDemographicIdPushedAfterDateByRequestingFacility : 
		which gets the local demographicId's which have changed, it will traverse linked records so if a linked record changes, your local id is reported as changed.

	2) demographicWs.getDemographicsPushedAfterDate : 
		which is a raw listing of the direct records which have changed, i.e. (facilityId, oscarDemographicId).
	*/	
	protected void findChangedRecordsFromIntegrator(LoggedInInfo loggedInInfo, Facility facility) throws MalformedURLException {//throws IOException, ShutdownException {
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
	
	/**
	 * This is a redirect method to determine compression by the server shell or by Java process. 
	 */
	private final String completeDocumentFile(final String parentDir, final String parentFile, final Set<Path> documentPaths)  {
		
		Path processFile = Paths.get(parentDir, COMPRESSION_SHELL_SCRIPT);
		String checksum = null;
		
		if(Files.exists(processFile)) 
		{
			checksum = completeDocumentFile(parentDir, parentFile);
		} 
		else if(! documentPaths.isEmpty()) 
		{
			//TODO consider splitting this thread so that the entire process is not held up with large document archives. 
			checksum = zipDocumentFiles(parentDir, parentFile, documentPaths);
		}
		
		return checksum;
	}

	/**
	 * This is an option to "off-load" some of the compression workload by invoking 
	 * a script from the server shell.  
	 * The script must be named build_doc_zip.sh and located 
	 * in the parent directory of this process.
	 * The script should also produce a checksum file for use in the logs. 
	 * This may involve more intervention and security hardening from the Oscar support provider.
	 * 
	 * WARNING: this method has not been tested in production as of June 15, 2018
	 */
	private final String completeDocumentFile(final String parentDir, final String parentFile)  {
		logger.info("creating document zip file");
		String checksum = null;
		BufferedReader bufferedReader = null;
		try {
			File processFile = new File(parentDir + File.separator + COMPRESSION_SHELL_SCRIPT);
			
			// is this method secure? It loads and executes a shell script from the user accessible 
			// OscarDocuments directory. 
			ProcessBuilder processBuilder = new ProcessBuilder(processFile.getAbsolutePath(),parentFile).inheritIO();
			
			Process process = processBuilder.start();
			
			process.waitFor();
			
			if(logger.isDebugEnabled()) {
				bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			    String data = bufferedReader.readLine();
			    logger.debug(data);
			}
			
			// The shell script will need to create a checksum file in the file system named [filename].md5
			// The MD5 hash shall be written to the first line. 
			File checksumfile = new File(parentDir + File.separator + parentFile + ".md5");
			if(checksumfile.exists()) {
				bufferedReader = new BufferedReader(new FileReader(checksumfile));
				checksum = bufferedReader.readLine();
				logger.debug("Found MD5 for document zip file " + processFile.getAbsolutePath());
			}
			
		}catch(Exception e) {
			logger.error("Error",e);
		} finally {
		    IOUtils.closeQuietly(bufferedReader);
		}
		
		logger.info("done creating document zip file");
			
		return checksum;
	}

	private String completeFile(String parentDir,final String parentFile) throws NoSuchAlgorithmException, IOException {
		
		String[] files = new File(parentDir).list(new FilenameFilter(){
		    public boolean accept(File dir, String name) {
		    	if(name.startsWith(parentFile) && !name.equals(parentFile + COMPRESSED_DOCUMENTS_APPENDAGE + ".zip")) {
		    		return true;
		    	}
		    	if(name.indexOf("documentMeta.txt") != -1) {
		    		return true;
		    	}
		    	return false;
		    }
		});
		logger.info("CREATING ZIP FILE NOW");
		createZipFile(parentDir,parentFile,files);
		//delete those files since they are zipped
		for(int x=0;x<files.length;x++) {
			new File(parentDir + File.separator + files[x]).delete();
		}
		
		//Create checksum for this file
		File file = new File(parentDir + File.separator + parentFile + ".zipTemp");
		MessageDigest md5Digest = MessageDigest.getInstance("MD5");
		
		 
		//Get the checksum
		return getFileChecksum(md5Digest, file);

	}
	
	protected void createTarFile(String parentDir, String parentFile, String[] files) {
		 FileOutputStream fOut = null;
		 BufferedOutputStream bOut = null;
		 GzipCompressorOutputStream gzOut = null;
		 TarArchiveOutputStream tOut = null;
		 
		 try {
			 fOut = new FileOutputStream(new File(parentDir + File.separator + parentFile + ".tar"));
		     bOut = new BufferedOutputStream(fOut);
		     gzOut = new GzipCompressorOutputStream(bOut);
		     tOut = new TarArchiveOutputStream(gzOut);
		     
			 for(int x=0;x<files.length;x++) {
				 File f = new File(parentDir + File.separator + files[x]);
				 TarArchiveEntry original = new TarArchiveEntry( f);
				 tOut.putArchiveEntry(original);
				 IOUtils.copy(new FileInputStream(f), tOut);
				 tOut.closeArchiveEntry();
			 }
			 
			 tOut.finish();
		 }catch(Exception e) {
			 logger.error("Error",e);
		 } finally {
			 IOUtils.closeQuietly(tOut);
		 }
	}
		
	protected void createZipFile(final String parentDir, final String parentFile, String[] files) {
		String destination = parentDir + File.separator + parentFile + ".zipTemp";		
		logger.info("creating zip file " + destination);
		ZipOutputStream out = null;
		
		try {
			out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
			out.setMethod(ZipOutputStream.DEFLATED);
			
			for(String file : files) {
				addZipFile(parentDir + File.separator + file, out, file);
			}
		} catch(Exception e) {
			logger.error("Error creating zip file",e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * Compresses all the document files listed in the documentPaths manifest and then returns a checksum. 
	 */
	private final String zipDocumentFiles(final String parentDir, final String parentFile, final Set<Path> documentPaths) {
		
		Path destination = Paths.get(parentDir, parentFile + COMPRESSED_DOCUMENTS_APPENDAGE + ".zipTemp");
		logger.info("creating document zip file " + destination.toAbsolutePath().toString());
		ZipOutputStream out = null;
		String checksum = null;
		
		try {
			out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination.toFile())));
			out.setMethod(ZipOutputStream.DEFLATED);
					
			for(Path documentPath : documentPaths) {			
				addZipFile(documentPath.toAbsolutePath().toString(), out, documentPath.getFileName().toString());
			}
			
			checksum = getFileChecksum(MessageDigest.getInstance("MD5"), destination.toFile());
			
		} catch (IOException e) {
			logger.error("Error creating zip file for document manifest " + documentPaths, e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error creating checksum for document zip file " + documentPaths, e);
		} finally {
			IOUtils.closeQuietly(out);
		}
		
		return checksum;
	}
	
	private void addZipFile(final String source, final ZipOutputStream destination, final String filename) throws IOException {

		byte data[] = new byte[1024];
		//out.putNextEntry(new ZipEntry(files[x].getName()));
		FileInputStream fi = new FileInputStream(source);
		BufferedInputStream origin = new BufferedInputStream(fi, 1024);
        
		ZipEntry entry = new ZipEntry(filename);
		destination.putNextEntry(entry);
        int count;
        while((count = origin.read(data, 0, 1024)) != -1) {
        	destination.write(data, 0, count);
        }
        origin.close(); 
	}
		
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(file);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0; 
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    }
	     
	    //close the stream; We don't need it now.
	    fis.close();
	     
	    //Get the hash's bytes
	    byte[] bytes = digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    //return complete hash
	   return sb.toString();
	}

	/**
	 * Defaults to DOCUMENT_DIR when INTEGRATOR_OUTPUT_DIR is not set.
	 * @return
	 */
	public static String getOutputDirectory() {
		String integratorOutputDirectory = OscarProperties.getInstance().getProperty("INTEGRATOR_OUTPUT_DIR");
		if(StringUtils.isNotEmpty(integratorOutputDirectory)) {
			outputDirectory = integratorOutputDirectory;
		}
		
		if(outputDirectory.endsWith(File.separator)) {
			outputDirectory = outputDirectory.substring(0, outputDirectory.length() -1);
		}
		
		return outputDirectory;
	}

	public static void setOutputDirectory(String outputDirectory) {
		CaisiIntegratorUpdateTask.outputDirectory = outputDirectory;
	}
	
	private final IntegratorFileLog updateLogs(final LoggedInInfo loggedInInfo, final Facility facility) {
				
		// get the date from the last log entry 
		IntegratorFileLog lastFile = null;
		List<IntegratorFileLog> integratorFileLogList = integratorFileLogManager.getStatusNotCompleteOrError(loggedInInfo);
		FacilityWs facilityWs = null;
		
		if(! integratorFileLogList.isEmpty()) {
			lastFile = integratorFileLogList.get(0);
		}
		
		try {
			facilityWs = CaisiIntegratorManager.getFacilityWs(loggedInInfo, facility);
		} catch (MalformedURLException e) {
			logger.error("Connection error while syncing file logs", e);
		} 

		// sort out the current status' in the integrator file log with the integrator log.
		for(IntegratorFileLog integratorFileLog : integratorFileLogList) {
			List<ImportLog> importLogList = null;
			
			if(facilityWs != null) {
				importLogList = facilityWs.getImportLogByFilenameAndChecksum(integratorFileLog.getFilename(), integratorFileLog.getChecksum());
			}
			
			if(importLogList == null) {
				continue;
			}
			
			for(ImportLog importLog : importLogList) {
				integratorFileLog.setIntegratorStatus(importLog.getStatus());
				integratorFileLogManager.updateIntegratorFileLog(loggedInInfo, integratorFileLog);
			}
		}

		return lastFile;
	}
}

