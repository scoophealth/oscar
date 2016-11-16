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

package org.oscarehr.casemgmt.service;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProgramQueueDao;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.utility.ProgramAccessCache;
import org.oscarehr.PMmodule.utility.RoleCache;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.casemgmt.common.EChartNoteEntry;
import org.oscarehr.casemgmt.dao.CaseManagementCPPDAO;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.model.ProviderExt;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.CaseManagementTmpSaveDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.EChartDao;
import org.oscarehr.common.dao.EncounterWindowDao;
import org.oscarehr.common.dao.HashAuditDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.dao.MsgDemoMapDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProviderExtDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.CaseManagementTmpSave;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.EncounterWindow;
import org.oscarehr.common.model.HashAudit;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.MsgDemoMap;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.quatro.model.security.Secrole;
import com.quatro.service.security.RolesManager;

import oscar.OscarProperties;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
//import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.util.ConversionUtils;
import oscar.util.DateUtils;

@Transactional
public class CaseManagementManager {

	public final int SIGNATURE_SIGNED = 1;
	public final int SIGNATURE_VERIFY = 2;

	private String issueAccessType = "access";
	private CaseManagementNoteDAO caseManagementNoteDAO;
	private CaseManagementNoteExtDAO caseManagementNoteExtDAO;
	private CaseManagementNoteLinkDAO caseManagementNoteLinkDAO;
	private CaseManagementIssueDAO caseManagementIssueDAO;
	private IssueDAO issueDAO;
	private CaseManagementCPPDAO caseManagementCPPDAO;
	private DemographicDao demographicDao;
	private ProviderExtDao providerExtDao;
	private RoleProgramAccessDAO roleProgramAccessDAO;
	private RolesManager roleManager;
	private CaseManagementTmpSaveDao caseManagementTmpSaveDao;
	private AdmissionManager admissionManager;
	private HashAuditDao hashAuditDao;
	private UserPropertyDAO userPropertyDAO;
	private DxresearchDAO dxresearchDAO;
	private ProgramProviderDAO programProviderDao;
	private ProgramAccessDAO programAccessDAO;
	private SecRoleDao secRoleDao;
	private ProgramQueueDao programQueueDao;
	private DocumentDao documentDao;
	private AppointmentArchiveDao appointmentArchiveDao;
	private DxDao dxDao;
	private ProgramManager2 programManager2;
	@Autowired
	private SecurityInfoManager securityInfoManager;

	private boolean enabled;

	private static final Logger logger = MiscUtils.getLogger();

	private EChartDao eChartDao = null;
	private EncounterWindowDao encounterWindowDao;
	
	
	public void setAppointmentArchiveDao(AppointmentArchiveDao appointmentArchiveDao) {
		this.appointmentArchiveDao = appointmentArchiveDao;
	}
	
	public void setDxDao(DxDao dxDao) {
		this.dxDao = dxDao;
	}
	
	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}
	
	
	public CaseManagementIssue getIssueByIssueCode(String demo, String issue_code) {
		return this.caseManagementIssueDAO.getIssuebyIssueCode(demo, issue_code);
	}

	/*
	 * check to see if issue has been saved for this demo beforeif it has return issue; else return null
	 */
	public CaseManagementIssue getIssueById(String demo, String issue_id) {
		return this.caseManagementIssueDAO.getIssuebyId(demo, issue_id);
	}

	private ProgramManager programManager = null;

	public void setProgramManager(ProgramManager programManager) {
		this.programManager = programManager;
	}

	// retrieve list of providers who have edited specific note
	public void getEditors(CaseManagementNote note) {
		List<Provider> providers = this.caseManagementNoteDAO.getEditors(note);
		if (providers == null) providers = new ArrayList<Provider>();
		note.setEditors(providers);
	}

	// retrieves a list of providers that have been associated with each note
	// and stores this list in the coresponding note.
	public void getEditors(Collection<CaseManagementNote> notes) {
		List<Provider> providers;
		for (CaseManagementNote note : notes) {
			providers = caseManagementNoteDAO.getEditors(note);
			if (providers == null) providers = new ArrayList<Provider>();
			note.setEditors(providers);
		}
	}

	public List<Provider> getAllEditors(String demographicNo) {
		List<Provider> providers = this.caseManagementNoteDAO.getAllEditors(demographicNo);
		if (providers == null) providers = new ArrayList<Provider>();
		return providers;
	}

	public UserProperty getUserProperty(String provider_no, String name) {
		return this.userPropertyDAO.getProp(provider_no, name);
	}

	public void saveUserProperty(UserProperty prop) {
		this.userPropertyDAO.saveProp(prop);
	}

	public void saveEctWin(EncounterWindow ectWin) {
		if(ectWin.getId() == null)
			encounterWindowDao.persist(ectWin);
		else
			encounterWindowDao.merge(ectWin);
	}

	public EncounterWindow getEctWin(String provider) {
		return this.encounterWindowDao.find(provider);
	}

	public void saveNoteExt(CaseManagementNoteExt cExt) {
		caseManagementNoteExtDAO.save(cExt);
	}

	public void updateNoteExt(CaseManagementNoteExt cExt) {
		caseManagementNoteExtDAO.update(cExt);
	}

	public void saveNoteLink(CaseManagementNoteLink cLink) {
		caseManagementNoteLinkDAO.save(cLink);
	}

	public void updateNoteLink(CaseManagementNoteLink cLink) {
		caseManagementNoteLinkDAO.update(cLink);
	}

	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note, String cproviderNo, String userName, String lastStr, String roleName) {
		String noteStr = note.getNote();
		String noteHistory = note.getHistory();


		noteStr = noteStr.replaceAll("\r\n", "\n");
		noteStr = noteStr.replaceAll("\r", "\n");

		if (noteHistory == null) noteHistory = noteStr;
		else noteHistory = noteStr + "\n" + "   ----------------History Record----------------   \n" + noteHistory + "\n";

		// note.setNote(noteStr);
		note.setHistory(noteHistory);

		caseManagementNoteDAO.saveNote(note);

		// if note is signed we hash it and save hash
		if (note.isSigned()) {
			HashAudit hashAudit = new HashAudit();
			hashAudit.setType(HashAudit.NOTE);
			hashAudit.setId2(note.getId().toString());
			hashAudit.makeHash(note.getNote().getBytes());
			hashAuditDao.persist(hashAudit);
		}

		OscarProperties properties = OscarProperties.getInstance();
		if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
			return eChartDao.saveEchart(note, cpp, userName, lastStr);
		}

		return "";

	}

	/*
	 * fetch notes for demographicif date is set, fetch notes after specified date
	 */
	public List getNotes(String demographic_no, UserProperty prop) {
		if (prop == null) return getNotes(demographic_no);

		String staleDate = prop.getValue();
		return caseManagementNoteDAO.getNotesByDemographic(demographic_no, staleDate);
	}

	/*
	 * fetch notes linked to particular issue codeused for retrieving notes attached to cpp
	 */
	public List<CaseManagementNote> getCPP(String demoNo, long issueId, UserProperty prop) {
		String staleDate = null;
		if (prop != null) staleDate = prop.getValue();

		return caseManagementNoteDAO.getCPPNotes(demoNo, issueId, staleDate);

	}

	/**
	 * @deprecated
	 * Use authenticated method getNotes( LoggedInInfo loggedInInfo, String demographic_no, String[] issues, UserProperty prop)
	 */
	@Deprecated
	public List<CaseManagementNote> getNotes(String demographic_no, String[] issues, UserProperty prop) {
		if (prop == null) return getNotes(demographic_no, issues);

		String staleDate = prop.getValue();
		return caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues, staleDate);
	}
	
	/*
	 * fetch notes for demographic linked with specified issues if date is set, fetch notes after specified date
	 */
	public List<CaseManagementNote> getNotes( LoggedInInfo loggedInInfo, String demographic_no, String[] issues, UserProperty prop) {
		if (prop == null) {
			return getNotes( loggedInInfo, demographic_no, issues);
		}

		String staleDate = prop.getValue();
		return caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues, staleDate);
	}

	public List<CaseManagementNote> getNotes(String demographic_no) {
		return caseManagementNoteDAO.getNotesByDemographic(demographic_no);
	}

	public List<CaseManagementNote> getNotes(String demographic_no, Integer maxNotes) {
		return caseManagementNoteDAO.getNotesByDemographic(demographic_no, maxNotes);
	}

	/**
	 * @deprecated
	 * Use the authenticated method: getNotes(LoggedInInfo loggedInInfo, String demographic_no, String[] issues)
	 */
	@Deprecated
	public List<CaseManagementNote> getNotes(String demographic_no, String[] issues) {
		List<CaseManagementNote> notes = caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues);
		return notes;
	}
	
	public List<CaseManagementNote> getNotes( LoggedInInfo loggedInInfo, String demographic_no, String[] issues ) {

		if( ! securityInfoManager.isAllowedAccessToPatientRecord( loggedInInfo, Integer.parseInt( demographic_no ) ) ) {
			throw new RuntimeException("Access Denied");
		}
		
		//TODO: not sure how the CAISI programs should be wired through the security info manager.
		
		List<CaseManagementNote> notes = caseManagementNoteDAO.getNotesByDemographic( demographic_no, issues );
		return notes;
	}

    public List<CaseManagementNote> getNotes(String demographic_no, String[] issues, Integer maxNotes) {
    	List<CaseManagementNote> notes = caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues, maxNotes);
		return notes;
	}

    public List<CaseManagementNote> getNotesWithLimit(String demographic_no, Integer offset, Integer numToReturn) {
		return caseManagementNoteDAO.getNotesByDemographicLimit(demographic_no, offset, numToReturn);
	}

	public List<CaseManagementNote> getNotesInDateRange(String demographic_no, Date startDate, Date endDate) {
		return caseManagementNoteDAO.getNotesByDemographicDateRange(demographic_no, startDate, endDate);
	}

	/* Return only those notes with archived set to zero */
	public List<CaseManagementNote> getActiveNotes(String demographic_no, String[] issues) {
		List<CaseManagementNote> notes = caseManagementNoteDAO.getActiveNotesByDemographic(demographic_no, issues);
		return notes;
	}

	public List<CaseManagementIssue> getIssues(int demographic_no) {
		return caseManagementIssueDAO.getIssuesByDemographicOrderActive(demographic_no, null);
	}

	public Issue getIssueIByCmnIssueId(int cmnIssueId) {
		return caseManagementIssueDAO.getIssueByCmnId(cmnIssueId);
	}

	public List<CaseManagementIssue> getIssuesByNote(int noteId) {
		return caseManagementIssueDAO.getIssuesByNote(noteId, null);
	}

	public List<CaseManagementIssue> getIssues(int demographic_no, Boolean resolved) {
		return caseManagementIssueDAO.getIssuesByDemographicOrderActive(demographic_no, resolved);
	}

	public List<CaseManagementIssue> getIssues(String demographic_no, List accessRight) {
		return filterIssueList(getIssues(Integer.parseInt(demographic_no)), accessRight);
	}

	/* return true if have the right to access issues */
	public boolean inAccessRight(String right, String issueAccessType, List accessRight) {
		boolean rt = false;
		if (accessRight == null) return rt;
		Iterator itr = accessRight.iterator();
		while (itr.hasNext()) {
			AccessType par = (AccessType) itr.next();
			if (right.equalsIgnoreCase(par.getName()) && issueAccessType.equalsIgnoreCase(par.getType())) return true;
		}
		return rt;
	}

	/* filter the issues by caisi role */
	private List<CaseManagementIssue> filterIssueList(List<CaseManagementIssue> allIssue, List accessRight) {
		List<String> role = secRoleDao.findAllNames();
		List<CaseManagementIssue> filteredIssue = new ArrayList<CaseManagementIssue>();

		for (int i = 0; i < role.size(); i++) {
			Iterator<CaseManagementIssue> itr = allIssue.iterator();
			String rl = role.get(i);
			String right = rl.trim() + "issues";
			boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
			if (inaccessRight) {

				String iRole = rl;
				while (itr.hasNext()) {
					CaseManagementIssue iss = itr.next();

					if (iss.getIssue().getRole().trim().equalsIgnoreCase(iRole.trim())) {
						filteredIssue.add(iss);

					}
				}
			}
		}
		return filteredIssue;
	}

	public Issue getIssue(String issue_id) {
		return this.issueDAO.getIssue(Long.valueOf(issue_id));
	}

	public Issue getIssueByCode(String issueCode) {
		return this.issueDAO.findIssueByCode(issueCode);
	}

	public CaseManagementNote getNote(String note_id) {
		return this.caseManagementNoteDAO.getNote(Long.valueOf(note_id));
	}

	public List<CaseManagementNote> getNotesByUUID(String uuid) {
		return this.caseManagementNoteDAO.getNotesByUUID(uuid);
	}

	public CaseManagementNote getMostRecentNote(String uuid) {
		return this.caseManagementNoteDAO.getMostRecentNote(uuid);
	}

	public CaseManagementNoteExt getNoteExt(Long id) {
		return this.caseManagementNoteExtDAO.getNoteExt(id);
	}

	public List<CaseManagementNoteExt> getExtByNote(Long noteId) {
		return this.caseManagementNoteExtDAO.getExtByNote(noteId);
	}

	public List getExtByKeyVal(String keyVal) {
		return this.caseManagementNoteExtDAO.getExtByKeyVal(keyVal);
	}

	public List getExtByValue(String keyVal, String value) {
		return this.caseManagementNoteExtDAO.getExtByValue(keyVal, value);
	}

	public List getExtBeforeDate(String keyVal, Date dateValue) {
		return this.caseManagementNoteExtDAO.getExtBeforeDate(keyVal, dateValue);
	}

	public List getExtAfterDate(String keyVal, Date dateValue) {
		return this.caseManagementNoteExtDAO.getExtAfterDate(keyVal, dateValue);
	}

	public CaseManagementNoteLink getNoteLink(Long id) {
		return this.caseManagementNoteLinkDAO.getNoteLink(id);
	}

	public List getLinkByNote(Long noteId) {
		return this.caseManagementNoteLinkDAO.getLinkByNote(noteId);
	}

	public CaseManagementNoteLink getLatestLinkByNote(Long noteId) {
		return this.caseManagementNoteLinkDAO.getLastLinkByNote(noteId);
	}

	public List getLinkByTableId(Integer tableName, Long tableId) {
		return this.caseManagementNoteLinkDAO.getLinkByTableId(tableName, tableId);
	}

	public List<CaseManagementNoteLink> getLinkByTableId(Integer tableName, Long tableId, String otherId) {
		return this.caseManagementNoteLinkDAO.getLinkByTableId(tableName, tableId, otherId);
	}

	public List<CaseManagementNoteLink> getLinkByTableIdDesc(Integer tableName, Long tableId) {
		return this.caseManagementNoteLinkDAO.getLinkByTableIdDesc(tableName, tableId);
	}

	public List<CaseManagementNoteLink> getLinkByTableIdDesc(Integer tableName, Long tableId, String otherId) {
		return this.caseManagementNoteLinkDAO.getLinkByTableIdDesc(tableName, tableId, otherId);
	}

	public CaseManagementNoteLink getLatestLinkByTableId(Integer tableName, Long tableId, String otherId) {
		return this.caseManagementNoteLinkDAO.getLastLinkByTableId(tableName, tableId, otherId);
	}

	public CaseManagementNoteLink getLatestLinkByTableId(Integer tableName, Long tableId) {
		return this.caseManagementNoteLinkDAO.getLastLinkByTableId(tableName, tableId);
	}

	public Integer getTableNameByDisplay(String disp) {
		if (!filled(disp)) return null;
		Integer tName = CaseManagementNoteLink.CASEMGMTNOTE;

		if (disp.equals(CaseManagementNoteLink.DISP_ALLERGY)) tName = CaseManagementNoteLink.ALLERGIES;
		else if (disp.equals(CaseManagementNoteLink.DISP_DOCUMENT)) tName = CaseManagementNoteLink.DOCUMENT;
		else if (disp.equals(CaseManagementNoteLink.DISP_LABTEST)) tName = CaseManagementNoteLink.LABTEST;
		else if (disp.equals(CaseManagementNoteLink.DISP_PRESCRIP)) tName = CaseManagementNoteLink.DRUGS;
		else if (disp.equals(CaseManagementNoteLink.DISP_DEMO)) tName = CaseManagementNoteLink.DEMOGRAPHIC;
		else if (disp.equals(CaseManagementNoteLink.DISP_PREV)) tName = CaseManagementNoteLink.PREVENTIONS;

		return tName;
	}

	public CaseManagementCPP getCPP(String demographic_no) {
		return this.caseManagementCPPDAO.getCPP(demographic_no);
	}

	public List<Allergy> getAllergies(String demographic_no) {
		AllergyDao allergyDao=(AllergyDao) SpringUtils.getBean("allergyDao");
		return allergyDao.findAllergies(Integer.parseInt(demographic_no));
	}

	public List<Drug> getPrescriptions(String demographic_no, boolean all) {
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");

		if (all) {
			return (drugDao.findByDemographicIdOrderByPosition(new Integer(demographic_no), null));
		}
		return (drugDao.getUniquePrescriptions(demographic_no));
	}

	public List<Drug> getCurrentPrescriptions(int demographic_no) {
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		
		return (drugDao.findByDemographicIdOrderByPosition(new Integer(demographic_no), false));
	}

	/**
	 * This method gets all prescriptions including from integrated facilities. This method will also check to ensure the integrator is enabled for this facility before attemping to add remote drugs. If it's not enabled it will return only local drugs.
	 */
	public List<Drug> getPrescriptions(LoggedInInfo loggedInInfo, int demographicId, boolean all) {
		List<Drug> results = null;

		results = getPrescriptions(String.valueOf(demographicId), all);

		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			addIntegratorDrugs(loggedInInfo, results, all, demographicId);
		}

		return (results);
	}
	
	/**
	 * Only be used to get methadone or suboxone for custom rx modules 
	 * @param demographciId
	 * @param rxName
	 * @return
	 */
	public List<Drug> getCustomPrescriptions(int demographciId, String rxName) {
		if (rxName == null || (!rxName.toLowerCase().contains("methadone") && !rxName.toLowerCase().contains("suboxone") 
				&& !rxName.toLowerCase().contains("buprenorphine"))) {
			return null;
		}
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		return drugDao.findCustomByDemographicIdOrderByPosition(demographciId, rxName);
	}
	
	public Drug getLastRxForCustomRx(int demoNo, String rxName) {
		if (rxName == null) {
			return null;
		}
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		return drugDao.getLastRxForCustomRx(demoNo, rxName);
	}
	
	public Date getLastEndDateForCustomRx(int demoNo, String rxName) {
		if (rxName == null) {
			return null;
		}
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		Drug drug = drugDao.getLastRxForCustomRx(demoNo, rxName);
		return (drug != null)?drug.getEndDate():null;
	}

	private void addIntegratorDrugs(LoggedInInfo loggedInInfo,List<Drug> prescriptions, boolean viewAll, int demographicId) {

		if (prescriptions == null) {
			logger.warn("prescriptions passed in is null, it should never be null, empty list should be used if no entries for drugs.");
			return;
		}

		try {
			List<CachedDemographicDrug> remoteDrugs  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(demographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, demographicId);
			}




			for (CachedDemographicDrug cachedDrug : remoteDrugs) {
				if (viewAll) {
					prescriptions.add(getPrescriptDrug(loggedInInfo, cachedDrug));
				} else {
					// if it's not view all, we need to only add the drug if it's not already there, or if it's a newer prescription
					Drug pd = containsPrescriptDrug(prescriptions, cachedDrug.getRegionalIdentifier());
					if (pd == null) {
						prescriptions.add(getPrescriptDrug(loggedInInfo, cachedDrug));
					} else {
						if (pd.getRxDate().before(DateUtils.toDate(cachedDrug.getRxDate())) || (pd.getRxDate().equals(cachedDrug.getRxDate()) && pd.getCreateDate().before(DateUtils.toDate(cachedDrug.getCreateDate())))) {
							prescriptions.remove(pd);
							prescriptions.add(getPrescriptDrug(loggedInInfo, cachedDrug));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	private Drug getPrescriptDrug(LoggedInInfo loggedInInfo,CachedDemographicDrug cachedDrug) throws MalformedURLException {
		Drug pd = new Drug();

		pd.setBrandName(cachedDrug.getBrandName());
		pd.setCustomName(cachedDrug.getCustomName());
		pd.setRxDate(DateUtils.toDate(cachedDrug.getRxDate()));
		pd.setArchived(cachedDrug.isArchived());
		pd.setSpecial(cachedDrug.getSpecial());
		pd.setEndDate(DateUtils.toDate(cachedDrug.getEndDate()));
		pd.setRegionalIdentifier(cachedDrug.getRegionalIdentifier());
		pd.setCreateDate(DateUtils.toDate(cachedDrug.getCreateDate()));

		pd.setId(cachedDrug.getFacilityIdIntegerCompositePk().getCaisiItemId());

		int remoteFacilityId = cachedDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId();
		pd.setRemoteFacilityId(remoteFacilityId);

		CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteFacilityId);
		pd.setRemoteFacilityName(cachedFacility.getName());

		return (pd);
	}

	private static Drug containsPrescriptDrug(List<Drug> prescriptions, String regionalIdentifier) {
		for (Drug prescriptDrug : prescriptions) {
			if (regionalIdentifier.equals(prescriptDrug.getRegionalIdentifier())) return (prescriptDrug);
		}

		return (null);
	}

	public List<LabelValueBean> getMsgBeans(Integer demographicNo) {
		MsgDemoMapDao mdmd = SpringUtils.getBean(MsgDemoMapDao.class);
		List<MsgDemoMap> mdms = mdmd.findByDemographicNo(demographicNo);
		
		MessageTblDao mtd = SpringUtils.getBean(MessageTblDao.class);
		List<MessageTbl> items = mtd.findByMaps(mdms);
		
		Iterator<MessageTbl> iter = items.iterator();
		ArrayList<LabelValueBean> al = new ArrayList<LabelValueBean>();
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		while (iter.hasNext()) {
			MessageTbl mtbl = iter.next();
			al.add(new LabelValueBean(new Integer(i).toString(), mtbl.getSubject() + "-" + sdf.format(mtbl.getDate())));
			i++;
		}
		return al;
	}

	public void deleteIssueById(CaseManagementIssue issue) {
		caseManagementIssueDAO.deleteIssueById(issue);
	}

	public void saveAndUpdateCaseIssues(List issuelist) {
		/*
		 * We're having a problem where duplicate CaseManagementIssue objects being created (as in points to same issue).
		 */
		caseManagementIssueDAO.saveAndUpdateCaseIssues(issuelist);
	}

	public void saveCaseIssue(CaseManagementIssue issue) {
		caseManagementIssueDAO.saveIssue(issue);
	}

	public Issue getIssueInfo(Long l) {
		return issueDAO.getIssue(l);
	}

	public List getAllIssueInfo() {
		return issueDAO.getIssues();
	}

	public void saveCPP(CaseManagementCPP cpp, String providerNo) {
		cpp.setProviderNo(providerNo); // added because nothing else was setting providerNo; not sure this is the right place to do this -- rwd
		caseManagementCPPDAO.saveCPP(cpp);

		OscarProperties properties = OscarProperties.getInstance();
		if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
			eChartDao.saveCPPIntoEchart(cpp, providerNo);
		}
	}

	public List<Issue> getIssueInfoByCode(String providerNo, String[] codes) {
		return issueDAO.findIssueByCode(codes);
	}

	public List<Issue> getIssueInfoByCode(String providerNo, String code) {
		String[] codes = { code };
		return issueDAO.findIssueByCode(codes);
	}

	public Issue getIssueInfoByCode(String code) {
		return issueDAO.findIssueByCode(code);
	}

	public List<Issue> getIssueInfoBySearch(String providerNo, String search, List accessRight) {
		List<Issue> issList = issueDAO.findIssueBySearch(search);
		// filter the issue list by role
		List<String> role = secRoleDao.findAllNames();
		List<Issue> filteredIssue = new ArrayList<Issue>();

		for (int i = 0; i < role.size(); i++) {
			Iterator<Issue> itr = issList.iterator();
			String rl = role.get(i);
			String right = rl.trim() + "issues";
			boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
			if (inaccessRight) {

				String iRole = rl;
				while (itr.hasNext()) {
					Issue iss = itr.next();

					if (iss.getRole().trim().equalsIgnoreCase(iRole.trim())) {
						filteredIssue.add(iss);

					}
				}
			}
		}
		return filteredIssue;

	}

	public void addNewIssueToConcern(String demoNo, String issueName) {
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demoNo);
		}
		String ongoing = (cpp.getOngoingConcerns() == null) ? "" : cpp.getOngoingConcerns();
		ongoing = ongoing + issueName + "\n";
		cpp.setOngoingConcerns(ongoing);
		caseManagementCPPDAO.saveCPP(cpp);

		OscarProperties properties = OscarProperties.getInstance();
		if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
			eChartDao.updateEchartOngoing(cpp);
		}

	}

	/**
	 * substitute function for updateCurrentIssueToCPP We don't want to clobber existing text in ongoing concerns all we want to do is remove the issue description
	 **/
	public void removeIssueFromCPP(String demoNo, CaseManagementIssue issue) {
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null) {
			logger.error("Cannot remove issue: No CPP found for demographic: " + demoNo);
			return;
		}

		String ongoing = cpp.getOngoingConcerns();
		String newOngoing;
		String description;

		description = issue.getIssue().getDescription();
		Pattern pattern = Pattern.compile("^" + description + "$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(ongoing);

		if (matcher.find()) {
			newOngoing = matcher.replaceFirst("");

			cpp.setOngoingConcerns(newOngoing);
			caseManagementCPPDAO.saveCPP(cpp);

			OscarProperties properties = OscarProperties.getInstance();
			if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
				eChartDao.updateEchartOngoing(cpp);
			}
		}
	}

	/**
	 * Substitute for updateCurrentIssueToCPP we replace old issue with new without clobbering existing text
	 **/
	public void changeIssueInCPP(String demoNo, String origIssueDesc, String newIssueDesc) {
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null) {
			logger.error("Cannot change issue: No CPP found for demographic: " + demoNo);
			return;
		}
		String ongoing = cpp.getOngoingConcerns();
		String newOngoing;

		Pattern pattern = Pattern.compile("^" + origIssueDesc + "$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(ongoing);

		if (matcher.find()) {
			newOngoing = matcher.replaceFirst(newIssueDesc);
			cpp.setOngoingConcerns(newOngoing);
			caseManagementCPPDAO.saveCPP(cpp);

			OscarProperties properties = OscarProperties.getInstance();
			if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
				eChartDao.updateEchartOngoing(cpp);
			}
		}

	}

	public void updateCurrentIssueToCPP(String demoNo, List issueList) {
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demoNo);
		}
		String ongoing = "";
		Iterator itr = issueList.iterator();
		while (itr.hasNext()) {
			CaseManagementIssue iss = (CaseManagementIssue) itr.next();
			ongoing = ongoing + iss.getIssue().getDescription() + "\n";
		}

		cpp.setOngoingConcerns(ongoing);
		caseManagementCPPDAO.saveCPP(cpp);

		OscarProperties properties = OscarProperties.getInstance();
		if (!Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
			eChartDao.updateEchartOngoing(cpp);
		}
	}

	/* get the filtered Notes by caisi role */
	public List<CaseManagementIssue> getFilteredNotes(String providerNo, String demographic_no) {
		List<CaseManagementNote> allNotes = caseManagementNoteDAO.getNotesByDemographic(demographic_no);
		List<String> role = secRoleDao.findAllNames();
		List<CaseManagementIssue> filteredNotes = new ArrayList<CaseManagementIssue>();
		Iterator<CaseManagementNote> itr = allNotes.iterator();
		boolean added = false;
		while (itr.hasNext()) {
			CaseManagementNote note = itr.next();
			added = false;
			Set<CaseManagementIssue> se = note.getIssues();
			if (se == null || se.size() == 0) {
				Iterator<CaseManagementIssue> isit = se.iterator();
				while (isit.hasNext()) {
					CaseManagementIssue iss = isit.next();
					for (int i = 0; i < role.size(); i++) {
						String rl = role.get(i);
						if (iss.getIssue().getRole().trim().equalsIgnoreCase(rl.trim())) {
							filteredNotes.add(iss);
							added = true;
							break;
						}

					}
					if (added) break;
				}
			}
		}
		return filteredNotes;
	}

	public boolean haveIssue(Long issid, Long noteId, String demoNo) {
		return caseManagementNoteDAO.haveIssue(issid, demoNo);
	}

	/**
	 * Analyzes the issues attached to each note belonging to the supplied Demographic, searching for the supplied issue ID. This method searches local issues only.
	 *
	 * @param issid the desired issue ID to find
	 * @param demoNo the desired demographic ID to find issues for
	 * @return true if some note for this demographic is attached to this issue, false otherwise
	 */
	public boolean haveIssue(Long issid, String demoNo) {
		List allNotes = caseManagementNoteDAO.getNotesByDemographic(demoNo);
		Iterator itr = allNotes.iterator();
		while (itr.hasNext()) {
			CaseManagementNote note = (CaseManagementNote) itr.next();
			Set issues = note.getIssues();
			Iterator its = issues.iterator();
			while (its.hasNext()) {
				CaseManagementIssue iss = (CaseManagementIssue) its.next();
				if (iss.getId().intValue() == issid.intValue()) return true;
			}
		}
		return false;
	}

	public boolean greaterEqualLevel(int level, String providerNo) {
		if (level < 1 || level > 4) return false;
		List pcrList = secRoleDao.findAllNames();
		if (pcrList.size() == 0) return false;
		Iterator itr = pcrList.iterator();
		while (itr.hasNext()) {
			String pcr = (String) itr.next();
			String role = pcr;
			int secuL = 0, rtSecul = 0;
			if (role.equalsIgnoreCase("doctor")) secuL = 4;
			if (role.equalsIgnoreCase("nurse")) secuL = 3;
			if (role.equalsIgnoreCase("counsellor")) secuL = 2;
			if (role.equalsIgnoreCase("CSW")) secuL = 1;
			/* get provider's highest level */
			if (secuL > rtSecul) rtSecul = secuL;
			if (rtSecul >= level) return true;
		}
		return false;
	}

	// TODO terrible performance here. TERRIBLE. at LEAST cache this - rwd
	public List<AccessType> getAccessRight(String providerNo, String demoNo, String programId) {
		List<Integer> progList = new ArrayList<Integer>();

		if (programId == null) {
			for (Object o : demographicDao.getProgramIdByDemoNo(Integer.parseInt(demoNo))) {
				progList.add((Integer) o);
			}
		} else {
			progList.add(Integer.valueOf(programId));
		}

		if (progList.isEmpty()) {
			return null;
		}

		List<AccessType> rt = new ArrayList<AccessType>();
		Iterator<Integer> itr = progList.iterator();

		while (itr.hasNext()) {
			Integer pId = itr.next();

			List<ProgramProvider> ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, pId.longValue());
			List<ProgramAccess> paList = programAccessDAO.getAccessListByProgramId(pId.longValue());

			for (int i = 0; i < ppList.size(); i++) {
				ProgramProvider pp =  ppList.get(i);
				// add default role access
				List<DefaultRoleAccess> arList = roleProgramAccessDAO.getDefaultAccessRightByRole(pp.getRoleId());
				for (int j = 0; j < arList.size(); j++) {
					DefaultRoleAccess ar = arList.get(j);
					addrt(rt, ar.getAccess_type());
				}
				for (int k = 0; k < paList.size(); k++) {
					ProgramAccess pa = paList.get(k);
					if (pa.isAllRoles()) {
						addrt(rt, pa.getAccessType());
					} else if (roleInAccess(pp.getRoleId(), pa)) {
						addrt(rt, pa.getAccessType());
					}
				}
			}
		}

		return rt;
	}

	public boolean roleInAccess(Long roleId, ProgramAccess pa) {
		boolean rt = false;
		Set roleset = pa.getRoles();
		Iterator itr = roleset.iterator();
		while (itr.hasNext()) {
			Secrole rl = (Secrole) itr.next();
			if (roleId.compareTo(rl.getId()) == 0) return true;
		}
		return rt;
	}

	public void addrt(List<AccessType> rt, AccessType at) {
		if (at == null) return;

		boolean hasIt = false;
		for (int i = 0; i < rt.size(); i++) {
			AccessType ac = rt.get(i);
			if (ac.getId().compareTo(at.getId()) == 0) hasIt = true;
		}
		if (!hasIt) rt.add(at);
	}

	public boolean hasAccessRight(String accessName, String accessType, String providerNo, String demoNo, String pId) {
		if (accessName == null || accessType == null || !filled(pId)) return false;
		if (new Long(pId).intValue() == 0) pId = null;
		List arList = getAccessRight(providerNo, demoNo, pId);
		for (int i = 0; i < arList.size(); i++) {
			AccessType at = (AccessType) arList.get(i);
			if (accessName.equalsIgnoreCase(at.getName()) && accessType.equalsIgnoreCase(at.getType())) return true;
		}
		return false;
	}

	public String getRoleName(String providerNo, String program_id) {
		String rt = "";
		List ppList = null;
		if (program_id == null || "".equalsIgnoreCase(program_id) || "null".equalsIgnoreCase(program_id)) ppList = programProviderDao.getProgramProviderByProviderNo(providerNo);
		else {
			Long pid = new Long(program_id);
			ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, pid);
		}
		if (ppList != null && ppList.size() > 0) rt = ((ProgramProvider) ppList.get(0)).getRole().getRoleName();
		return rt;
	}

	public String getDemoName(String demoNo) {

		Demographic dg = demographicDao.getClientByDemographicNo(new Integer(demoNo));
		if (dg == null) return "";
		else return dg.getFirstName() + " " + dg.getLastName();
	}

	public String getDemoGender(String demoNo) {
		String gender = "";

		Demographic demo = demographicDao.getClientByDemographicNo(new Integer(demoNo));
		if (demo != null) {
			gender = demo.getSex();
		}

		return gender;
	}

	public String getDemoAge(String demoNo) {
		String age = "";

		Demographic demo = demographicDao.getClientByDemographicNo(new Integer(demoNo));
		if (demo != null) {
			age = demo.getAge();
		}

		return age;
	}

	public String getDemoDOB(String demoNo) {
		Demographic dg = demographicDao.getClientByDemographicNo(new Integer(demoNo));
		if (dg == null) return "";
		else return dg.getFormattedDob();
	}

	public String getCaisiRoleById(String id) {
		// return providerCaisiRoleDAO.getCaisiRoleById(id);
		return roleManager.getRole(id).getName();
	}

	public List<CaseManagementNote> search(CaseManagementSearchBean searchBean) {
		return this.caseManagementNoteDAO.search(searchBean);
	}

	public List<CaseManagementNote> filterNotesByAccess(List<CaseManagementNote> notes, String providerNo) {
		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		for (Iterator<CaseManagementNote> iter = notes.iterator(); iter.hasNext();) {
			CaseManagementNote note = iter.next();
			if (hasAccessRight(removeFirstSpace(getCaisiRoleById(note.getReporter_caisi_role())) + "notes", "access", providerNo, note.getDemographic_no(), note.getProgram_no())) {
				filteredNotes.add(note);
			}
		}
		return filteredNotes;
	}

	public void tmpSave(String providerNo, String demographicNo, String programId, String noteId, String note) {
		CaseManagementTmpSave tmp = new CaseManagementTmpSave();
		tmp.setProviderNo(providerNo);
		tmp.setDemographicNo(new Integer(demographicNo).intValue());
		tmp.setProgramId(new Integer(programId).intValue());
		if (noteId == null || "".equals(noteId)) {
			noteId = "0";
		}
		tmp.setNoteId(Integer.parseInt(noteId));
		tmp.setNote(note);
		tmp.setUpdateDate(new Date());
		caseManagementTmpSaveDao.persist(tmp);
	}

	public void deleteTmpSave(String providerNo, String demographicNo, String programId) {
		Integer intDemoNo = ConversionUtils.fromIntString(demographicNo);
		Integer intProgramId = ConversionUtils.fromIntString(programId);
		caseManagementTmpSaveDao.remove(providerNo, intDemoNo, intProgramId);
	}

	public CaseManagementTmpSave restoreTmpSave(String providerNo, String demographicNo, String programId) {
		CaseManagementTmpSave obj = caseManagementTmpSaveDao.find(providerNo, new Integer(demographicNo), new Integer(programId));
		return obj;
	}

	// we want to load a temp saved note only if it's more recent than date
	public CaseManagementTmpSave restoreTmpSave(String providerNo, String demographicNo, String programId, Date date) {
		CaseManagementTmpSave obj = caseManagementTmpSaveDao.find(providerNo, new Integer(demographicNo), new Integer(programId), date);
		return obj;
	}

	public List getHistory(String note_id) {
		CaseManagementNote note = caseManagementNoteDAO.getNote(Long.valueOf(note_id));
		return this.caseManagementNoteDAO.getHistory(note);
	}

	/*
	 * Get all notes which have been linked to issues
	 *
	 * @param issueIds csv of issue ids
	 *
	 * @param demoNo demographic to search for
	 */
	public List<CaseManagementNote> getIssueHistory(String issueIds, String demoNo) {
		issueIds = StringUtils.trimToNull(issueIds);
		if (issueIds == null) return (new ArrayList<CaseManagementNote>());
		return this.caseManagementNoteDAO.getIssueHistory(issueIds, demoNo);
	}


	public List<CaseManagementNote> filterNotes(LoggedInInfo loggedInInfo, String providerNo, Collection<CaseManagementNote> notes, String programId) {

		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		if (notes.isEmpty()) {
			return filteredNotes;
		}

		// Get Role - if no ProgramProvider record found, show no issues.
		@SuppressWarnings("unchecked")
		List ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
		if (ppList == null || ppList.isEmpty()) {
			return new ArrayList<CaseManagementNote>();
		}

		ProgramProvider pp = (ProgramProvider) ppList.get(0);
		Secrole role = pp.getRole();

		Map programAccessMap = ProgramAccessCache.getAccessMap(new Long(programId));
		// Load up access list from program
		//@SuppressWarnings("unchecked")
		//List programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programId));
		//@SuppressWarnings("unchecked")
		//Map programAccessMap = convertProgramAccessListToMap(programAccessList);

		// iterate through the issue list
		for (CaseManagementNote cmNote : notes) {
			String noteRole = cmNote.getReporter_caisi_role();
			String noteRoleName = RoleCache.getRole(Long.valueOf(noteRole)).getName().toLowerCase();
			ProgramAccess pa = null;
			boolean add = false;

			// write
			pa = null;
			// read
			pa = (ProgramAccess) programAccessMap.get("read " + noteRoleName + " notes");
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					// filteredIssues.add(cmIssue);
					add = true;
				}
			} else {
				logger.debug(noteRoleName + " is null");
				if (Long.parseLong(noteRole) == role.getId().longValue()) {
					// default
					logger.debug("noteRole " + noteRole + " = Provider Role from secRole " + role.getId());
					add = true;
				}
			}

			// apply defaults
			if (!add) {
				if (Long.parseLong(noteRole) == role.getId().longValue()) {
					logger.debug("noteRole " + noteRole + " = Provider Role from secRole " + role.getId());
					add = true;
				}
			}

			// global default role access
			String accessName = "read " + noteRoleName + " notes";
			if (roleProgramAccessDAO.hasAccess(accessName, role.getId())) {
				add = true;
			}

			// did it pass the test?
			if (add) {
				filteredNotes.add(cmNote);
			}
		}

		// filter notes based on facility
		if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
			filteredNotes = notesFacilityFiltering(loggedInInfo, filteredNotes);
		}

		return filteredNotes;
	}

	public List<EChartNoteEntry> filterNotes1(String providerNo, Collection<EChartNoteEntry> notes, String programId) {

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		if (notes.isEmpty()) {
			return filteredNotes;
		}

		// Get Role - if no ProgramProvider record found, show no issues.
		@SuppressWarnings("unchecked")
		List ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
		if (ppList == null || ppList.isEmpty()) {
			for(EChartNoteEntry note:notes) {
				if(!note.getType().equals("local_note") && !note.getType().equals("remote_note"))
					filteredNotes.add(note);
			}
			return filteredNotes;
		}

		ProgramProvider pp = (ProgramProvider) ppList.get(0);
		Secrole role = pp.getRole();

		Map programAccessMap = ProgramAccessCache.getAccessMap(new Long(programId));

		// iterate through the issue list
		for (EChartNoteEntry cmNote : notes) {
			if(!cmNote.getType().equals("local_note") && !cmNote.getType().equals("remote_note")) {
				filteredNotes.add(cmNote);
				continue;
			}
			String noteRole = null;
			String noteRoleName = "";

			if(cmNote.getType().equals("local_note")) {
				noteRole = cmNote.getRole();
				noteRoleName = RoleCache.getRole(Long.valueOf(noteRole)).getName().toLowerCase();
			}
			if(cmNote.getType().equals("remote_note")) {
				noteRoleName = cmNote.getRole();
			}
			ProgramAccess pa = null;
			boolean add = false;

			// write
			pa = null;
			// read
			pa = (ProgramAccess) programAccessMap.get("read " + noteRoleName + " notes");
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					// filteredIssues.add(cmIssue);
					add = true;
				}
			} else {
				logger.debug(noteRoleName + " is null");
				try {
					if (Long.valueOf(noteRole).longValue() == role.getId().longValue()) {
						// default
						logger.debug("noteRole " + noteRole + " = Provider Role from secRole " + role.getId());
						add = true;
					}
				} catch (NumberFormatException ex) {
					logger.error("noteRole cannot be parsed: noteRole = " + noteRole);
				}
			}

			// apply defaults
			if (!add) {
				try {
					if (Long.valueOf(noteRole).longValue() == role.getId().longValue()) {
						logger.debug("noteRole " + noteRole + " = Provider Role from secRole " + role.getId());
						add = true;
					}
				} catch (NumberFormatException ex) {
					logger.error("noteRole cannot be parsed: noteRole = " + noteRole);
				}
			}

			// global default role access
			String accessName = "read " + noteRoleName + " notes";
			if (RoleCache.hasAccess(accessName, role.getId())) {
				add = true;
			}

			// did it pass the test?
			if (add) {
				filteredNotes.add(cmNote);
			}
		}

		// filter notes based on facility
		//if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
		//	filteredNotes = notesFacilityFiltering(filteredNotes);
		//}

		return filteredNotes;
	}
	
	//filters notes linked to private documents
	public List<EChartNoteEntry> filterNotes2(LoggedInInfo loggedInInfo, Collection<EChartNoteEntry> notes) {

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();
		
		if (notes.isEmpty()) {
			return filteredNotes;
		}
		
		for(EChartNoteEntry cmNote:notes) {
			if(!cmNote.getType().equals("local_note") && !cmNote.getType().equals("remote_note")) {
				filteredNotes.add(cmNote);
				continue;
			}
			
			List<CaseManagementNoteLink> links = caseManagementNoteLinkDAO.getLinkByNote(((Long)cmNote.getId()));
			if(links.isEmpty()) {
				filteredNotes.add(cmNote);
				continue;
			}
			
			List<ProgramProvider> ppList = programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
			
			boolean addIt = true;
			for(CaseManagementNoteLink link:links) {
				if(CaseManagementNoteLink.DOCUMENT == link.getTableName().intValue()) {
					Document d = documentDao.find(link.getTableId().intValue());
					if(d != null) {
						if(d.isRestrictToProgram() != null && d.isRestrictToProgram() && d.getProgramId().intValue()>0) {
							addIt=false;
							for(ProgramProvider pp:ppList) {
								if(pp.getProgramId().intValue() == d.getProgramId().intValue()) {
									addIt=true;
									break;
								}
							}
						}
					}
				}
			}
			
			if(addIt) {
				filteredNotes.add(cmNote);
			}
		}
		
		return filteredNotes;
	}
	

	public boolean hasRole(String providerNo, CachedDemographicNote cachedDemographicNote, String programId) {

		// Get Role - if no ProgramProvider record found, show no issues.
		@SuppressWarnings("unchecked")
		List ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
		if (ppList == null || ppList.isEmpty()) {
			return (false);
		}

		ProgramProvider pp = (ProgramProvider) ppList.get(0);
		Secrole role = pp.getRole();

		// Load up access list from program
		@SuppressWarnings("unchecked")
		List programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programId));
		@SuppressWarnings("unchecked")
		Map programAccessMap = convertProgramAccessListToMap(programAccessList);

		// iterate through the issue list
		String noteRoleName = cachedDemographicNote.getRole();
		if (noteRoleName != null) noteRoleName = noteRoleName.toLowerCase();
		ProgramAccess pa = null;
		boolean add = false;

		// write
		pa = null;
		// read
		pa = (ProgramAccess) programAccessMap.get("read " + noteRoleName + " notes");
		if (pa != null) {
			if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
				// filteredIssues.add(cmIssue);
				return (true);
			}
		} else {
			if (noteRoleName.equals(role.getRoleName().toLowerCase())) {
				// default
				return (true);
			}
		}

		// apply defaults
		if (!add) {
			if (noteRoleName.equals(role.getRoleName().toLowerCase())) {
				return (true);
			}
		}

		// global default role access
		String accessName = "read " + noteRoleName + " notes";
		if (roleProgramAccessDAO.hasAccess(accessName, role.getId())) {
			return (true);
		}

		return (false);
	}

	public boolean isRoleIncludedInAccess(ProgramAccess pa, Secrole role) {
		boolean result = false;

		for (Iterator iter = pa.getRoles().iterator(); iter.hasNext();) {
			Secrole accessRole = (Secrole) iter.next();
			if (role.getId().longValue() == accessRole.getId().longValue()) {
				return true;
			}
		}
		return result;
	}

	public Map<String,ProgramAccess> convertProgramAccessListToMap(List<ProgramAccess> paList) {
		Map<String,ProgramAccess> map = new HashMap<String,ProgramAccess>();
		if (paList == null) {
			return map;
		}
		for (Iterator<ProgramAccess> iter = paList.iterator(); iter.hasNext();) {
			ProgramAccess pa =  iter.next();
			map.put(pa.getAccessType().getName().toLowerCase(), pa);
		}
		return map;
	}

	
	public Integer searchIssuesCount(String providerNo, String programId, String search) {
		// Get Role - if no ProgramProvider record found, show no issues.
		List<ProgramProvider> ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, (programId!=null)?new Long(programId):null);
		if (ppList == null || ppList.isEmpty()) {
			return 0;
		}
		ProgramProvider pp = ppList.get(0);
		Secrole role = pp.getRole();

		// get program accesses... program allows either all roles or not all roles (does this mean no roles?)
		List<ProgramAccess> paList = programAccessDAO.getAccessListByProgramId(new Long(programId));
		Map<String,ProgramAccess> paMap = convertProgramAccessListToMap(paList);

		// get all roles
		List<Secrole> allRoles = this.roleManager.getRoles();

		List<Secrole> allowableSearchRoles = new ArrayList<Secrole>();
		for (Iterator<Secrole> iter = allRoles.iterator(); iter.hasNext();) {
			Secrole r = iter.next();
			String key = "write " + r.getName().toLowerCase() + " issues";
			ProgramAccess pa = paMap.get(key);
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					allowableSearchRoles.add(r);
				}
			}
			if (pa == null && r.getId().intValue() == role.getId().intValue()) {
				allowableSearchRoles.add(r);
			}

			// global default role access
			if (roleProgramAccessDAO.hasAccess(key, role.getId())) {
				allowableSearchRoles.add(r);
			}
		}

		return  issueDAO.searchCount(search, allowableSearchRoles);
	}
	
	public List<Issue> searchIssues(String providerNo, String programId, String search) {
		return searchIssues(providerNo,programId,search,0,AbstractDao.MAX_LIST_RETURN_SIZE);
	}
	
	/**
	 * @param providerNo
	 * @param programId
	 * @param search
	 */
	public List<Issue> searchIssues(String providerNo, String programId, String search, int startIndex, int numToReturn) {
		
		if(numToReturn > AbstractDao.MAX_LIST_RETURN_SIZE) {
			throw new IllegalArgumentException("Can only return a maximum of " + AbstractDao.MAX_LIST_RETURN_SIZE + " results");
		}
		
		// Get Role - if no ProgramProvider record found, show no issues.
		List<ProgramProvider> ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
		if (ppList == null || ppList.isEmpty()) {
			return new ArrayList<Issue>();
		}
		ProgramProvider pp = ppList.get(0);
		Secrole role = pp.getRole();

		// get program accesses... program allows either all roles or not all roles (does this mean no roles?)
		List<ProgramAccess> paList = programAccessDAO.getAccessListByProgramId(new Long(programId));
		Map<String,ProgramAccess> paMap = convertProgramAccessListToMap(paList);

		// get all roles
		List<Secrole> allRoles = this.roleManager.getRoles();

		List<Secrole> allowableSearchRoles = new ArrayList<Secrole>();
		for (Iterator<Secrole> iter = allRoles.iterator(); iter.hasNext();) {
			Secrole r = iter.next();
			String key = "write " + r.getName().toLowerCase() + " issues";
			ProgramAccess pa = paMap.get(key);
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					allowableSearchRoles.add(r);
				}
			}
			if (pa == null && r.getId().intValue() == role.getId().intValue()) {
				allowableSearchRoles.add(r);
			}

			// global default role access
			if (roleProgramAccessDAO.hasAccess(key, role.getId())) {
				allowableSearchRoles.add(r);
			}
		}

		List<Issue> issList = issueDAO.search(search, allowableSearchRoles, startIndex, numToReturn);

		return issList;
	}

	public List searchIssuesNoRolesConcerned(String providerNo, String programId, String search) {

		List issList = issueDAO.searchNoRolesConcerned(search);

		return issList;
	}

	/**
	 * Filters a list of CaseManagementIssue objects based on role.
	 */
	public List<CaseManagementIssue> filterIssues(LoggedInInfo loggedInInfo, String providerNo, List<CaseManagementIssue> issues, String programId) {

		List<CaseManagementIssue> filteredIssues = new ArrayList<CaseManagementIssue>();

		if (issues.isEmpty()) {
			return issues;
		}

		// Get Role - if no ProgramProvider record found, show no issues.
		List<ProgramProvider> ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
		if (ppList == null || ppList.isEmpty()) {
			return new ArrayList<CaseManagementIssue>();
		}

		ProgramProvider pp = ppList.get(0);
		Secrole role = pp.getRole();

		// Load up access list from program
		List<ProgramAccess> programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programId));
		Map<String,ProgramAccess> programAccessMap = convertProgramAccessListToMap(programAccessList);

		// iterate through the issue list
		for (Iterator<CaseManagementIssue> iter = issues.iterator(); iter.hasNext();) {
			CaseManagementIssue cmIssue = iter.next();
			String issueRole = cmIssue.getIssue().getRole().toLowerCase();
			ProgramAccess pa = null;
			boolean add = false;

			// write
			pa = programAccessMap.get("write " + issueRole + " issues");
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					add = true;
				}
			} else {
				if (issueRole.equalsIgnoreCase(role.getRoleName())) {
					// default
					add = true;
				}
			}

			// global default role access
			String accessName = "write " + issueRole + " issues";
			if (roleProgramAccessDAO.hasAccess(accessName, role.getId())) {
				add = true;
			}

			pa = null;
			// read
			pa = programAccessMap.get("read " + issueRole + " issues");
			if (pa != null) {
				if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
					// filteredIssues.add(cmIssue);
					add = true;
				}
			} else {
				if (issueRole.equalsIgnoreCase(role.getRoleName())) {
					// default
					add = true;
				}
			}
			// global default role access
			accessName = "read " + issueRole + " issues";
			if (roleProgramAccessDAO.hasAccess(accessName, role.getId())) {
				add = true;
			}

			// apply defaults
			if (!add) {
				if (issueRole.equalsIgnoreCase(role.getRoleName())) {
					add = true;
				}
			}

			// did it pass the test?
			if (add) {
				filteredIssues.add(cmIssue);
			}
		}

		// filter issues based on facility
		if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
			filteredIssues = issuesFacilityFiltering(loggedInInfo, filteredIssues);
		}

		return filteredIssues;
	}

	private List<CaseManagementIssue> issuesFacilityFiltering(LoggedInInfo loggedInInfo, List<CaseManagementIssue> issues) {
		ArrayList<CaseManagementIssue> results = new ArrayList<CaseManagementIssue>();

		for (CaseManagementIssue caseManagementIssue : issues) {
			Integer programId = caseManagementIssue.getProgram_id();
			if (programManager.hasAccessBasedOnCurrentFacility(loggedInInfo, programId)) results.add(caseManagementIssue);
		}

		return results;
	}

	private List<CaseManagementNote> notesFacilityFiltering(LoggedInInfo loggedInInfo, List<CaseManagementNote> notes) {

		ArrayList<CaseManagementNote> results = new ArrayList<CaseManagementNote>();

		for (CaseManagementNote caseManagementNote : notes) {
			String programId = caseManagementNote.getProgram_no();

			if (programId == null || "".equals(programId)) {
				results.add(caseManagementNote);
			} else {
				if (programManager.hasAccessBasedOnCurrentFacility(loggedInInfo, Integer.parseInt(programId))) results.add(caseManagementNote);
			}
		}

		return results;
	}

	public void updateNote(CaseManagementNote note) {
		this.caseManagementNoteDAO.updateNote(note);
	}

	public void saveNoteSimple(CaseManagementNote note) {
		this.caseManagementNoteDAO.saveNote(note);
		
		
	}
	
	public Long saveNoteSimpleReturnID(CaseManagementNote note) {
		return (Long)this.caseManagementNoteDAO.saveAndReturn(note);
	}

	public boolean isClientInProgramDomain(String providerNo, String demographicNo) {

		List providerPrograms = programProviderDao.getProgramProviderByProviderNo(providerNo);

		List allAdmissions = this.admissionManager.getAdmissions(Integer.valueOf(demographicNo));

		for (int x = 0; x < providerPrograms.size(); x++) {
			ProgramProvider pp = (ProgramProvider) providerPrograms.get(x);
			long programId = pp.getProgramId().longValue();

			for (int y = 0; y < allAdmissions.size(); y++) {
				Admission admission = (Admission) allAdmissions.get(y);
				long admissionProgramId = admission.getProgramId().longValue();

				if (programId == admissionProgramId) {
					return true;
				}
			}
		}

		return false;
	}
	
	public List<ProgramProvider> getProgramProviders(String providerNo) {
		List<ProgramProvider> providerPrograms = programProviderDao.getProgramProviderByProviderNo(providerNo);
		return providerPrograms;
	}
	
	public List<Admission> getAdmission(Integer demographicNo) {
		List<Admission> allAdmissions = this.admissionManager.getAdmissions(Integer.valueOf(demographicNo));
		return allAdmissions;
	}
	
	public boolean isClientInProgramDomain(List<ProgramProvider> providerPrograms, List<Admission> allAdmissions) {

		for (int x = 0; x < providerPrograms.size(); x++) {
			ProgramProvider pp = providerPrograms.get(x);
			long programId = pp.getProgramId().longValue();

			for (int y = 0; y < allAdmissions.size(); y++) {
				Admission admission = allAdmissions.get(y);
				long admissionProgramId = admission.getProgramId().longValue();

				if (programId == admissionProgramId) {
					return true;
				}
			}
		}

		return false;
	}
	

	public boolean isClientReferredInProgramDomain(String providerNo, String demographicNo) {
	 	boolean referred = false;
       		List providerPrograms = programProviderDao.getProgramProviderByProviderNo(providerNo);
		for (int x = 0; x < providerPrograms.size(); x++) {
			ProgramProvider pp = (ProgramProvider) providerPrograms.get(x);
			long programId = pp.getProgramId().longValue();
			ProgramQueue queue = programQueueDao.getActiveProgramQueue(programId, Long.valueOf(demographicNo));
			if(queue!=null)
				referred = true;
		}
		return referred;
	}


	public boolean unlockNote(int noteId, String password) {
		CaseManagementNote note = this.caseManagementNoteDAO.getNote(new Long(noteId));
		if (note != null) {
			if (note.isLocked() && note.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	public void updateIssue(String demographicNo, Long originalIssueId, Long newIssueId) {
		List<CaseManagementIssue> issues = this.caseManagementIssueDAO.getIssuesByDemographic(demographicNo);
		for (CaseManagementIssue issue : issues) {
			boolean save = false;
			if (issue.getIssue_id() == originalIssueId.longValue()) {
				issue.setIssue_id(newIssueId.longValue());
				issue.setIssue(null);
				save = true;
			}
			if (save) {
				this.caseManagementIssueDAO.saveIssue(issue);
			}
		}

		/*
		 * String[] issueIdList = new String[1]; issueIdList[0] = String.valueOf(newIssueId); List<CaseManagementNote> notes = this.caseManagementNoteDAO.getNotesByDemographic(demographicNo); for(CaseManagementNote note:notes) { Set<CaseManagementIssue>
		 * issues = note.getIssues(); for(CaseManagementIssue issue:issues) { if(issue.getIssue().getId().equals(originalIssueId)) { //update this CaseManagementIssue issue.setIssue(null); issue.setIssue_id(newIssueId.longValue()); } }
		 * this.caseManagementNoteDAO.saveNote(note); }
		 */
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setHashAuditDao(HashAuditDao dao) {
		this.hashAuditDao = dao;
	}

	public void setCaseManagementNoteDAO(CaseManagementNoteDAO dao) {
		this.caseManagementNoteDAO = dao;
	}

	public void setCaseManagementNoteExtDAO(CaseManagementNoteExtDAO dao) {
		this.caseManagementNoteExtDAO = dao;
	}

	public void setCaseManagementNoteLinkDAO(CaseManagementNoteLinkDAO dao) {
		this.caseManagementNoteLinkDAO = dao;
	}

	public void setCaseManagementIssueDAO(CaseManagementIssueDAO dao) {
		this.caseManagementIssueDAO = dao;
	}

	public void setProgramAccessDAO(ProgramAccessDAO programAccessDAO) {
		this.programAccessDAO = programAccessDAO;
	}

	public void setIssueDAO(IssueDAO dao) {
		this.issueDAO = dao;
	}

	public void setCaseManagementCPPDAO(CaseManagementCPPDAO dao) {
		this.caseManagementCPPDAO = dao;
	}

	public void setProgramProviderDao(ProgramProviderDAO programProviderDao) {
		this.programProviderDao = programProviderDao;
	}

	public void setProgramQueueDao(ProgramQueueDao programQueueDao) {
		this.programQueueDao = programQueueDao; 
	}

	public void setRolesManager(RolesManager mgr) {
		this.roleManager = mgr;
	}

	public void setProviderExtDao(ProviderExtDao providerExtDao) {
		this.providerExtDao = providerExtDao;
	}

	public void setRoleProgramAccessDAO(RoleProgramAccessDAO roleProgramAccessDAO) {
		this.roleProgramAccessDAO = roleProgramAccessDAO;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	public void setCaseManagementTmpSaveDao(CaseManagementTmpSaveDao dao) {
		this.caseManagementTmpSaveDao = dao;
	}
	
	public void setProgramManager2(ProgramManager2 programManager2) {
		this.programManager2 = programManager2;
	}

	protected String removeFirstSpace(String withSpaces) {
		int spaceIndex = withSpaces.indexOf(' '); // use lastIndexOf to remove last space
		if (spaceIndex < 0) { // no spaces!
			return withSpaces;
		}
		return withSpaces.substring(0, spaceIndex) + withSpaces.substring(spaceIndex + 1, withSpaces.length());
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setUserPropertyDAO(UserPropertyDAO dao) {
		this.userPropertyDAO = dao;
	}

	public void setDxresearchDAO(DxresearchDAO dao) {
		this.dxresearchDAO = dao;
	}
	
	public void setSecRoleDao(SecRoleDao secRoleDao) {
		this.secRoleDao = secRoleDao;
	}
	

	public void saveToDx(LoggedInInfo loggedInInfo, String demographicNo, String code, String codingSystem, boolean association) {
		if (codingSystem == null) {
			codingSystem = "icd10";
		}

		Dxresearch dx = new Dxresearch();
		dx.setDxresearchCode(code);
		dx.setCodingSystem(codingSystem);
		dx.setDemographicNo(Integer.parseInt(demographicNo));
		dx.setStartDate(new Date());
		dx.setUpdateDate(new Date());
		dx.setStatus('A');
		dx.setAssociation(association?(byte)1:(byte)0);
		dx.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		if (!dxresearchDAO.entryExists(dx.getDemographicNo(), codingSystem, code)) {
			this.dxresearchDAO.save(dx);
		}
	}

	public void saveToDx(LoggedInInfo loggedInInfo, String demographicNo, String code) {
		saveToDx(loggedInInfo, demographicNo, code, null, false);
	}

	public List<Dxresearch> getDxByDemographicNo(String demographicNo) {
		return this.dxresearchDAO.getByDemographicNo(Integer.parseInt(demographicNo));
	}

	/**
	 * This method takes in a string (template) eg "Signed on ${DATE} by {$USERSIGNATURE}" it then searches the string for values surrounded by ${ }. Once a value is found it looks in the map to see if there is a value for that key. If it doesn't find a
	 * value in the map, it looks in the in recource bundle (this allows templates to be i18n compliant). If nothing is found in the resource bundle the value is added as a blank in the returned formatted string. This is the default signing line.
	 * ECHART_SIGN_LINE=[${oscarEncounter.class.EctSaveEncounterAction.msgSigned} ${DATE} ${oscarEncounter.class.EctSaveEncounterAction.msgSigBy} ${USERSIGNATURE}]\n
	 *
	 * @param template string with template values used to create the String that is returned
	 * @param rc The current locale's resource bundle
	 * @param map Values that can be subtituted in.
	 * @return Formatted String
	 */
	public String getTemplateSignature(String template, ResourceBundle rc, Map<String, String> map) {
		StringBuilder ret = new StringBuilder();
		int tagstart = -2;
		int tagend;
		int currentPosition = 0;
		while ((tagstart = template.indexOf("${", tagstart + 2)) >= 0) {
			tagend = template.indexOf("}", tagstart);
			String substituteName = template.substring(tagstart + 2, tagend);
			String substituteValue = map.get(substituteName);
			if (substituteValue == null) {
				try {
					substituteValue = rc.getString(substituteName);
				} catch (Exception e) {
					substituteValue = "";
				}
			}

			ret.append(template.substring(currentPosition, tagstart));
			ret.append(substituteValue);
			currentPosition = tagend + 1;
		}

		ret.append(template.substring(currentPosition));
		return ret.toString();
	}

	public String getSignature(String cproviderNo, String userName, String roleName, Locale locale, int type) {

		SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy H:mm", locale);
		Date now = new Date();
		// add the time, signiture and role at the end of note
		String rolename = "";
		rolename = roleName;
		if (rolename == null) rolename = "";
		// if have signiture setting, use signiture as username
		String tempS = null;
		// if (providerSignitureDao.isOnSig(cproviderNo))
		ProviderExt pe = providerExtDao.find(cproviderNo);
		if(pe != null)
			tempS = pe.getSignature();
		if (tempS != null && !"".equals(tempS.trim())) userName = tempS;

		ResourceBundle resourceBundle = ResourceBundle.getBundle("oscarResources", locale);
		String signature;
		if (userName != null && !"".equals(userName.trim())) {
			try {
				HashMap map = new HashMap();
				map.put("DATE", dt.format(now));
				map.put("USERSIGNATURE", userName);
				map.put("ROLENAME", rolename);

				if (type == this.SIGNATURE_SIGNED) {
					// TODO: In the future pull this from a USER/PROGRAM preference.
					String signLine = OscarProperties.getInstance().getProperty("ECHART_SIGN_LINE");
					signature = getTemplateSignature(signLine, resourceBundle, map);
				} else if (type == this.SIGNATURE_VERIFY) {
					String signLine = OscarProperties.getInstance().getProperty("ECHART_VERSIGN_LINE");
					signature = getTemplateSignature(signLine, resourceBundle, map);
				} else {
					throw new Exception("No Signature type defined");
				}
			} catch (Exception eSignature) {
				signature = "[Unknown Signature Type Requested]";
				logger.error("Signature error while signing note ", eSignature);
			}
		} else {
			signature = "\n[" + dt.format(now) + "]\n";
		}

		return signature;
	}

	private boolean filled(String s) {
		return (s != null && s.trim().length() > 0);
	}

	public void seteChartDao(EChartDao eChartDao) {
		this.eChartDao = eChartDao;
	}

	public void setEncounterWindowDao(EncounterWindowDao encounterWindowDao) {
		this.encounterWindowDao = encounterWindowDao;
	}
	
	
	
	/**
	 * gets all the notes
	 * if we have a key, and the note is locked, consider it
	 * caisi - filter notes
	 * grab the last one, where i am provider, and it's not signed
	 *
	 * @param request
	 * @param demono
	 * @param providerNo
	 */
	public CaseManagementNote getLastSaved(String programId, String demono, String providerNo,Map unlockedNotesMap) {
		//CaseManagementNote note = null;
		List<EChartNoteEntry> entries = new ArrayList<EChartNoteEntry>();

		//Gets some of the note data, no relationships, not the note/history..just enough
		List<Map<String,Object>> notes = caseManagementNoteDAO.getUnsignedRawNoteInfoMapByDemographic(demono);
		Map<String,Object> filteredNotes = new LinkedHashMap<String,Object>();

		//This gets rid of old revisions (better than left join on a computed subset of itself
		for(Map<String,Object> note:notes) {
			if(filteredNotes.get(note.get("uuid"))!=null)
				continue;
			filteredNotes.put((String)note.get("uuid"),true);
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(note.get("id"));
			e.setDate((Date)note.get("observation_date"));
			e.setProviderNo((String)note.get("providerNo"));
			e.setProgramId(ConversionUtils.fromIntString(note.get("program_no")));
			e.setRole((String)note.get("reporter_caisi_role"));
			e.setType("local_note");
			entries.add(e);

		}

		// UserProperty prop = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
		//notes = caseManagementMgr.getNotes(demono);
		//notes = manageLockedNotes(notes, false, this.getUnlockedNotesMap(request));

		
		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		entries = filterNotes1(providerNo, entries, programId);

		Collections.sort(entries,EChartNoteEntry.getDateComparatorDesc());

		
		for(EChartNoteEntry entry:entries) {
			CaseManagementNote n = getNote(String.valueOf(entry.getId()));
			if(n.isLocked() && unlockedNotesMap.get(entry.getId()) != null ) {
				n.setLocked(false);
			}
			if(n.getProviderNo().equals(providerNo)) {
				return n;
			}
		}

		return null;
	}
	
	
	public CaseManagementNote makeNewNote(String providerNo,String demographicNo,String  encType,String appointmentNo,Locale locale){
		CaseManagementNote note = new CaseManagementNote();
		note.setProviderNo(providerNo);
		note.setDemographic_no(demographicNo);
		
		if (!OscarProperties.getInstance().isPropertyActive("encounter.empty_new_note")) {
			OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
			String encounterText = "";
			try{
				Appointment appointment = appointmentDao.find(Integer.parseInt(appointmentNo));
				encounterText = "[" + oscar.util.UtilDateUtilities.DateToString(appointment.getAppointmentDate(), "dd-MMM-yyyy", locale) + " .: " + appointment.getReason() + "] \n";
				note.setAppointmentNo(Integer.parseInt(appointmentNo));
			}catch(Exception e){
				logger.error("error parsing appointmentNo",e);
			}
		
			note.setNote(encounterText);
			
		} else {
			note.setNote("");
		}
		
		if (encType == null || encType.equals("")) {
			note.setEncounter_type("");
		} else {
			note.setEncounter_type(encType);
		}
		
		return note;
	
	}
	
	
	
//Move this out of here.
private String updateApptStatus(String status, String type) {
	oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
	as.setApptStatus(status);

	if (type.equalsIgnoreCase("sign")) status = as.signStatus();
	if (type.equalsIgnoreCase("verify")) status = as.verifyStatus();

	return status;
}

//add new note link if note is document or rx note
	public void addNewNoteLink(Long noteId) {
		CaseManagementNote cmn = getNote(noteId.toString());
		List<CaseManagementNote> cmnList = getNotesByUUID(cmn.getUuid());
		Long firstNoteId;
		Long lastNoteId;
		List<Long> noteIdList = new ArrayList<Long>();
		for (CaseManagementNote note : cmnList) {
			noteIdList.add(note.getId());
		}
		if (noteIdList.size() > 0) {
			Collections.sort(noteIdList);
			firstNoteId = noteIdList.get(0);
			lastNoteId = noteIdList.get(noteIdList.size() - 1);
			if (firstNoteId != lastNoteId) {
				CaseManagementNote firstNote = getNote(firstNoteId.toString());
				if (firstNote.isDocumentNote()) {
					Long tableId = EDocUtil.getTableIdFromNoteId(firstNote.getId());
					CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
					cmnl.setNoteId(lastNoteId);
					cmnl.setTableName(CaseManagementNoteLink.DOCUMENT);
					cmnl.setTableId(tableId);
					saveNoteLink(cmnl);
				} else if (firstNote.isRxAnnotation()) {

					CaseManagementNoteLink latestLink = getLatestLinkByNote(firstNote.getId());

					CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
					cmnl.setNoteId(lastNoteId);
					cmnl.setTableName(CaseManagementNoteLink.DRUGS);
					cmnl.setTableId(latestLink.getTableId());
					saveNoteLink(cmnl);
					// EDocUtil.addCaseMgmtNoteLink(cmnl);
				}
			}
		}
	}
	
	
	public CaseManagementNote saveCaseManagementNote(LoggedInInfo loggedInInfo, CaseManagementNote note,List<CaseManagementIssue> issuelist,CaseManagementCPP cpp,String ongoing,boolean verify,Locale locale,Date now,CaseManagementNote annotationNote,String userName,String user,String remoteAddr,String lastSavedNoteString) throws Exception {
		ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
		AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");	
		

		Long old_note_id = note.getId(); // saved for use with annotation

		boolean inCaisi = OscarProperties.getInstance().isCaisiLoaded();

		String role = null;
		String team = null;

		boolean newNote = false;
		if (note.getCreate_date() == null) {
			note.setCreate_date(now);
			newNote = true;
		}

		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProviderNo(), note.getProgram_no())).getRole().getId());
		} catch (Exception e) {
			role = "0";
		}
		/*
		 * if(session.getAttribute("archiveView")!="true") note.setReporter_caisi_role(role); else note.setReporter_caisi_role("1");
		 */
		note.setReporter_caisi_role(role);

		try {
			Admission admission = admissionManager.getAdmission(note.getProgram_no(), Integer.valueOf(note.getDemographic_no()));
			if (admission != null) {
				team = String.valueOf(admission.getTeamId());
			} else {
				team = "0";
			}
		} catch (Exception e) {
			logger.error("Error", e);
			team = "0";
		}
		note.setReporter_program_team(team);

		/* save all issue changes for demographic */
		saveAndUpdateCaseIssues(issuelist);
		if (inCaisi) cpp.setOngoingConcerns(ongoing);  // << Check on this one???


		OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
		
		
		String roleName = getRoleName(note.getProviderNo(), note.getProgram_no());
		
		Appointment appointment = null;
		
		try{
			appointment = appointmentDao.find(note.getAppointmentNo());
		}catch(Exception e){
			logger.debug("Appointment number error",e);
		}
		logger.debug("note.getAppointmentNo() "+note.getAppointmentNo()+" --- "+appointment);
		
		
		if (verify) {
			String message = getSignature(note.getProviderNo(), userName, roleName, locale, SIGNATURE_VERIFY);

			String n = note.getNote() + "\n" + message;
			note.setNote(n);

			// only update appt if there is one
			if (appointment != null) {
				appointment.setStatus(updateApptStatus(appointment.getStatus(), "verify"));
			}

		} else if (note.isSigned()) {
			String message = getSignature(note.getProviderNo(), userName, roleName, locale, SIGNATURE_SIGNED);

			String n = note.getNote() + "\n" + message;
			note.setNote(n);

			// only update appt if there is one
			if (appointment != null ) {
				appointment.setStatus(updateApptStatus(appointment.getStatus(), "sign"));
			}
		}
		
		
		if (appointment != null) {
			appointmentArchiveDao.archiveAppointment(appointment);
			appointment.setLastUpdateUser(note.getProviderNo());
			appointmentDao.merge(appointment);
		}
		
		// PLACEHOLDER FOR DX CHECK
		/*
		 * If an issue is checked, new , and certain - we want to check dx associations. if found in dx associations. we want to make an entry into dx.
		 */
		if (note.isSigned()) {
			for (CaseManagementIssue cmIssue : note.getIssues()) {
				if (cmIssue.isCertain()) {
					DxAssociation assoc = dxDao.findAssociation(cmIssue.getIssue().getType(), cmIssue.getIssue().getCode());
					if (assoc != null) {
						// we found a match. Let's add them to registry
						this.saveToDx(loggedInInfo, note.getDemographic_no(), assoc.getDxCode(), assoc.getDxCodeType(), true);

					}
				}
			}
		}

		/*
		 * if provider is a doctor or nurse,get all major and resolved medical issue for demograhhic and append them to CPP medical history
		 */
		if (inCaisi) {
			/* get access right */
			List<AccessType> accessRight = getAccessRight(note.getProviderNo(), note.getDemographic_no(), note.getProgram_no());
			setCPPMedicalHistory(cpp, note.getProviderNo(), accessRight);
			cpp.setUpdate_date(now);
			saveCPP(cpp, note.getProviderNo());
		}
		
		int revision;

		if (note.getRevision() != null) {
			revision = Integer.parseInt(note.getRevision());
			++revision;
		} else revision = 1;

		note.setRevision(String.valueOf(revision));
		
		/* save note including add signature */
		String savedStr = saveNote(cpp, note, note.getProviderNo(), userName, lastSavedNoteString, roleName);
		addNewNoteLink(note.getId());

		try {
			this.deleteTmpSave(note.getProviderNo(), note.getDemographic_no(), note.getProgram_no());
		} catch (Exception e) {
			logger.warn("warn", e);
		}

		
		
		if (annotationNote != null) {
			// new annotation created and got it in session attribute

			saveNoteSimple(annotationNote);
			CaseManagementNoteLink cml = new CaseManagementNoteLink(CaseManagementNoteLink.CASEMGMTNOTE, note.getId(), annotationNote.getId());
			saveNoteLink(cml);
			LogAction.addLog(annotationNote.getDemographic_no(), LogConst.ANNOTATE, LogConst.CON_CME_NOTE, String.valueOf(annotationNote.getId()), remoteAddr, annotationNote.getDemographic_no(), annotationNote.getNote());
			
		}
		
		
		if (old_note_id != null) {
			// Not a new note, look for old annotation

			CaseManagementNoteLink cml_anno = null;
			CaseManagementNoteLink cml_dump = null;
			List<CaseManagementNoteLink> cmll = getLinkByTableIdDesc(CaseManagementNoteLink.CASEMGMTNOTE, old_note_id);
			for (CaseManagementNoteLink link : cmll) {
				CaseManagementNote cmmn = getNote(link.getNoteId().toString());
				if (cmmn == null) continue;

				if (cmmn.getNote().startsWith("imported.cms4.2011.06")) {
					if (cml_dump == null) cml_dump = link;
				} else {
					if (cml_anno == null) cml_anno = link;
				}
				if (cml_anno != null && cml_dump != null) break;
			}

			if (cml_anno != null) {// old annotation exists - create new link
				CaseManagementNoteLink cml_n = new CaseManagementNoteLink(CaseManagementNoteLink.CASEMGMTNOTE, note.getId(), cml_anno.getNoteId());
				saveNoteLink(cml_n);
			}
			if (cml_dump != null) {// old dump exists - create new link
				CaseManagementNoteLink cml_n = new CaseManagementNoteLink(CaseManagementNoteLink.CASEMGMTNOTE, note.getId(), cml_dump.getNoteId());
				saveNoteLink(cml_n);
			}
		}

		String logAction;
		if (newNote) {
			logAction = LogConst.ADD;
		} else {
			logAction = LogConst.UPDATE;
		}
		LogAction.addLog(user, logAction, LogConst.CON_CME_NOTE, "" + Long.valueOf(note.getId()).intValue(), remoteAddr, note.getDemographic_no(), note.getAuditString());
		
		return note;
	}
	
	public void setCPPMedicalHistory(CaseManagementCPP cpp, String providerNo,List accessRight)	{

		if (greaterEqualLevel(3, providerNo))	{
			String mHis = cpp.getMedicalHistory();
			if(mHis!=null) {
				mHis = mHis.replaceAll("\r\n", "\n");
				mHis = mHis.replaceAll("\r", "\n");
			}
			List<CaseManagementIssue> allIssues = getIssues(Integer.parseInt(cpp.getDemographic_no()));

			Iterator<CaseManagementIssue> itr = allIssues.iterator();
			while (itr.hasNext()) {
				CaseManagementIssue cis = itr.next();
				String issustring = cis.getIssue().getDescription();
				if (cis.isMajor() && cis.isResolved()) {
					if (mHis!=null && mHis.indexOf(issustring) < 0)
						mHis = mHis + issustring + ";\n";
				} else {

					if (mHis!=null && mHis.indexOf(issustring) >= 0)
						mHis = mHis.replaceAll(issustring + ";\n", "");
				}
			}
			if(mHis!=null) {
				mHis = mHis.replaceAll("\r\n", "\n");
				mHis = mHis.replaceAll("\r", "\n");
			}
			cpp.setMedicalHistory(mHis);
		}
	}

}
