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

package org.oscarehr.casemgmt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.util.LabelValueBean;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.casemgmt.dao.AllergyDAO;
import org.oscarehr.casemgmt.dao.ApptDAO;
import org.oscarehr.casemgmt.dao.CaseManagementCPPDAO;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementTmpSaveDAO;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.dao.EchartDAO;
import org.oscarehr.casemgmt.dao.EncounterFormDAO;
import org.oscarehr.casemgmt.dao.EncounterWindowDAO;
import org.oscarehr.casemgmt.dao.HashAuditDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.dao.MessagetblDAO;
import org.oscarehr.casemgmt.dao.PrescriptionDAO;
import org.oscarehr.casemgmt.dao.ProviderSignitureDao;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.EncounterWindow;
import org.oscarehr.casemgmt.model.HashAuditImpl;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.model.Messagetbl;
import org.oscarehr.casemgmt.model.base.BaseHashAudit;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.dx.dao.DxResearchDAO;
import org.oscarehr.dx.model.DxResearch;

import oscar.OscarProperties;

public class CaseManagementManager {

    protected String issueAccessType = "access";
    protected CaseManagementNoteDAO caseManagementNoteDAO;
    protected CaseManagementIssueDAO caseManagementIssueDAO;
    protected IssueDAO issueDAO;
    protected CaseManagementCPPDAO caseManagementCPPDAO;
    protected AllergyDAO allergyDAO;
    protected PrescriptionDAO prescriptionDAO;
    protected EncounterFormDAO encounterFormDAO;
    protected MessagetblDAO messagetblDAO;
    protected EchartDAO echartDAO;
    protected ApptDAO apptDAO;
    protected ProviderDao providerDAO;
    protected ClientDao demographicDao;
    protected ProviderSignitureDao providerSignitureDao;
    protected RoleProgramAccessDAO roleProgramAccessDAO;
    protected ClientImageDAO clientImageDAO;
    protected RoleManager roleManager;
    protected CaseManagementTmpSaveDAO caseManagementTmpSaveDAO;
    protected AdmissionManager admissionManager;
    protected HashAuditDAO hashAuditDAO;
    protected EncounterWindowDAO ectWindowDAO;
    protected UserPropertyDAO userPropertyDAO;
    protected DxResearchDAO dxResearchDAO;
   
    private boolean enabled;
    
    /*
     *check to see if issue has been saved for this demo before
     *if it has return issue; else return null
     */
    public CaseManagementIssue getIssueById(String demo, String issue_id) {
        return this.caseManagementIssueDAO.getIssuebyId(demo,issue_id);
    }
    

    private ProgramManager programManager = null;
    public void setProgramManager(ProgramManager programManager) {
        this.programManager = programManager;
    }


    //retrieve list of providers who have edited specific note
    public void getEditors(CaseManagementNote note) {
        List<Provider>providers;
        providers = this.caseManagementNoteDAO.getEditors(note);
        if( providers == null ) providers = new ArrayList<Provider>();
        note.setEditors(providers);
    }
    
    //retrieves a list of providers that have been associated with each note
    //and stores this list in the coresponding note.
    public void getEditors(List<CaseManagementNote>notes) {
        Iterator<CaseManagementNote>iterator = notes.listIterator();
        List<Provider>providers;
        while(iterator.hasNext()) {
            CaseManagementNote note = iterator.next();
            providers = this.caseManagementNoteDAO.getEditors(note);
            if( providers == null ) providers = new ArrayList<Provider>();
            note.setEditors(providers);
        }
    }
    
    
    public void updateAppointment(String apptId, String status, String type) {

        apptDAO.updateAppointmentStatus(apptId, status, type);
    }
    
    public UserProperty getUserProperty(String provider_no, String name) {
        return this.userPropertyDAO.getProp(provider_no, name);
    }
    
    public void saveUserProperty(UserProperty prop) {
        this.userPropertyDAO.saveProp(prop);
    }
    
    public void saveEctWin(EncounterWindow ectWin) {
        ectWindowDAO.saveWindowDimensions(ectWin);
    }
    
    public EncounterWindow getEctWin(String provider) {
        return this.ectWindowDAO.getWindow(provider);
    }

    public String saveNote(CaseManagementCPP cpp, CaseManagementNote note, String cproviderNo, String userName, String lastStr, String roleName) {
    	SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	Date now = new Date();
        String noteStr = note.getNote();
        String noteHistory = note.getHistory();     
        
        // process noteStr, remove existing signed on string
        // noteStr = removeSignature(noteStr);
        if (note.isSigned())
        {
                
        	// add the time, signiture and role at the end of note
        	String rolename="";
        	rolename= roleName;
        	if (rolename == null)
        		rolename = "";
        	// if have signiture setting, use signiture as username
        	String tempS = null;
        	if (providerSignitureDao.isOnSig(cproviderNo))
        		tempS = providerSignitureDao.getProviderSig(cproviderNo);
        	if (tempS != null && !"".equals(tempS.trim()))
        		userName = tempS;

        	if (userName != null && !"".equals(userName.trim()))
        	{
        		noteStr = noteStr + "\n[[Signed on " + dt.format(now) + " "
        				+ "by " + userName + ", " + rolename + "]]\n";
        	} else
        		noteStr = noteStr + "\n[[" + dt.format(now) + "]]\n";

        } else
        {

        	// add time at the end of note
        	noteStr = noteStr + "\n[[" + dt.format(now) + "]]\n";
        }

        /* formate the "/n" in noteStr */
        noteStr = noteStr.replaceAll("\r\n", "\n");
        noteStr = noteStr.replaceAll("\r", "\n");

        if (noteHistory == null) noteHistory = noteStr;
        else noteHistory = noteStr + "\n" + "   ----------------History Record----------------   \n" + noteHistory + "\n";

        //note.setNote(noteStr);
        note.setHistory(noteHistory);
        
        caseManagementNoteDAO.saveNote(note);
        
        //if note is signed we hash it and save hash
        if( note.isSigned() ) {
            HashAuditImpl hashAudit = new HashAuditImpl();
            hashAudit.setType(BaseHashAudit.NOTE);
            hashAudit.setId(note.getId());
            hashAudit.makeHash(note.getNote().getBytes());
            hashAuditDAO.saveHash(hashAudit);
        }
        
        return echartDAO.saveEchart(note, cpp, userName, lastStr);                 

    }
    
    /*
     *fetch notes for demographic
     *if date is set, fetch notes after specified date
     */
    public List getNotes(String demographic_no, UserProperty prop) {
        if( prop == null )
            return getNotes(demographic_no);
        
        String staleDate = prop.getValue();
        return caseManagementNoteDAO.getNotesByDemographic(demographic_no, staleDate);
    }
    
    /*
     *fetch notes linked to particular issue code
     *used for retrieving notes attached to cpp
     */
    public List<CaseManagementNote>getCPP(String demoNo, long issueId, UserProperty prop) {
        String staleDate = null;
        if( prop != null )
            staleDate = prop.getValue();
        
        return caseManagementNoteDAO.getCPPNotes(demoNo, issueId, staleDate);
            
    }
    
    
    /*
     *fetch notes for demographic linked with specified issues
     *if date is set, fetch notes after specified date
     */
    public List<CaseManagementNote> getNotes(String demographic_no, String[] issues, UserProperty prop) {
        if( prop == null )
            return getNotes(demographic_no, issues);
                
        String staleDate = prop.getValue();
        return caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues, staleDate);
    }

    public List<CaseManagementNote> getNotes(String demographic_no) {
        return caseManagementNoteDAO.getNotesByDemographic(demographic_no);
    }

    public List<CaseManagementNote> getNotes(String demographic_no, String[] issues) {
        //List notesNoLocked = new ArrayList();
        List notes = caseManagementNoteDAO.getNotesByDemographic(demographic_no, issues);
        /*
        for(Iterator iter=notes.iterator();iter.hasNext();) {
        	CaseManagementNote note = (CaseManagementNote)iter.next();
        	if(!note.isLocked()) {
        		notesNoLocked.add(note);
        	}
        }
        return notesNoLocked;
        */
        return notes;
    }

    public List<CaseManagementIssue> getIssues(String providerNo, String demographic_no) {
        return caseManagementIssueDAO.getIssuesByDemographicOrderActive(demographic_no);

    }

    public List<CaseManagementIssue> getActiveIssues(String providerNo, String demographic_no) {
        return caseManagementIssueDAO.getActiveIssuesByDemographic(demographic_no);
    }

    public List<CaseManagementIssue> getIssues(String providerNo, String demographic_no, List accessRight) {
        return filterIssueList(getIssues(providerNo, demographic_no), providerNo, accessRight);
    }

    public List<CaseManagementIssue> getActiveIssues(String providerNo, String demographic_no, List accessRight) {
        return filterIssueList(getActiveIssues(providerNo, demographic_no), providerNo, accessRight);
    }

    /* return true if have the right to access issues */
    public boolean inAccessRight(String right, String issueAccessType, List accessRight) {
        boolean rt = false;
        if (accessRight == null) return rt;
        Iterator itr = accessRight.iterator();
        while (itr.hasNext()) {
            AccessType par = (AccessType)itr.next();
            if (right.equalsIgnoreCase(par.getName()) && issueAccessType.equalsIgnoreCase(par.getType())) return true;
        }
        return rt;
    }

    /* filter the issues by caisi role */
    public List filterIssueList(List allIssue, String providerNo, List accessRight) {
        List role = roleProgramAccessDAO.getAllRoleName();
        List filteredIssue = new ArrayList();

        for (int i = 0; i < role.size(); i++) {
            Iterator itr = allIssue.iterator();
            String rl = (String)role.get(i);
            String right = rl.trim() + "issues";
            boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
            if (inaccessRight) {

                String iRole = rl;
                while (itr.hasNext()) {
                    CaseManagementIssue iss = (CaseManagementIssue)itr.next();

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

    public CaseManagementNote getNote(String note_id) {
        return this.caseManagementNoteDAO.getNote(Long.valueOf(note_id));

    }
    
    public CaseManagementNote getMostRecentNote(String uuid) {
        return this.caseManagementNoteDAO.getMostRecentNote(uuid);
    }

    public CaseManagementCPP getCPP(String demographic_no) {
        return this.caseManagementCPPDAO.getCPP(demographic_no);
    }
    
    public List getAllergies(String demographic_no) {
        return this.allergyDAO.getAllergies(demographic_no);
    }

    public List getPrescriptions(String demographic_no, boolean all) {
        if (all) {
            return this.prescriptionDAO.getPrescriptions(demographic_no);
        }
        return this.prescriptionDAO.getUniquePrescriptions(demographic_no);
    }

    public List getEncounterFormBeans() {
        return encounterFormDAO.getAllForms();
    }

    public List getMsgBeans(Integer demographicNo) {
        Iterator iter = messagetblDAO.getMsgByDemoNo(demographicNo).iterator();
        ArrayList al = new ArrayList();
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");
        while (iter.hasNext()) {
            Messagetbl mtbl = (Messagetbl)iter.next();
            al.add(new LabelValueBean(new Integer(i).toString(), mtbl.getThesubject() + "-" + sdf.format(mtbl.getThedate())));
            i++;
        }
        return al;
    }

    public void deleteIssueById(CaseManagementIssue issue) {
        caseManagementIssueDAO.deleteIssueById(issue);
    }

    public void saveAndUpdateCaseIssues(List issuelist) {
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
        cpp.setProviderNo(providerNo);    // added because nothing else was setting providerNo; not sure this is the right place to do this -- rwd        
        caseManagementCPPDAO.saveCPP(cpp);
        echartDAO.saveCPPIntoEchart(cpp, providerNo);
    }

    public List<Issue> getIssueInfoByCode(String providerNo, String[] codes) {
        return issueDAO.findIssueByCode(codes);
    }
    
    public List<Issue> getIssueInfoByCode(String providerNo, String code) {
        String[] codes = {code};
        return issueDAO.findIssueByCode(codes);
    }    
    
    public Issue getIssueInfoByCode(String code) {
        return issueDAO.findIssueByCode(code);
    }
    
    public List getIssueInfoBySearch(String providerNo, String search, List accessRight) {
        List issList = issueDAO.findIssueBySearch(search);
        // filter the issue list by role
        List role = roleProgramAccessDAO.getAllRoleName();
        List filteredIssue = new ArrayList();

        for (int i = 0; i < role.size(); i++) {
            Iterator itr = issList.iterator();
            String rl = (String)role.get(i);
            String right = rl.trim() + "issues";
            boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
            if (inaccessRight) {

                String iRole = rl;
                while (itr.hasNext()) {
                    Issue iss = (Issue)itr.next();

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
        String ongoing = (cpp.getOngoingConcerns() == null)?"":cpp.getOngoingConcerns();
        ongoing = ongoing + issueName + "\n";
        cpp.setOngoingConcerns(ongoing);
        cpp.setUpdate_date(new Date());
        caseManagementCPPDAO.saveCPP(cpp);
        echartDAO.updateEchartOngoing(cpp);

    }
    
    /**
     *substitue function for updateCurrentIssueToCPP
     *We don't want to clobber existing text in ongoing concerns
     *all we want to do is remove the issue description
     **/
    public void removeIssueFromCPP(String demoNo, CaseManagementIssue issue) {
        CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
        
        String ongoing = cpp.getOngoingConcerns();
        String newOngoing;
        String description;       
        int idx;                       
        
        description = issue.getIssue().getDescription();
        Pattern pattern = Pattern.compile("^" + description + "$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(ongoing);
        
        if( matcher.find() ) {
            newOngoing = matcher.replaceFirst("");
            
            cpp.setOngoingConcerns(newOngoing);
            cpp.setUpdate_date(new Date());
            caseManagementCPPDAO.saveCPP(cpp);
            echartDAO.updateEchartOngoing(cpp);
        }
    }

    /**
     *Substitute for updateCurrentIssueToCPP
     *we replace old issue with new without clobbering existing text
     **/
    public void changeIssueInCPP(String demoNo, CaseManagementIssue origIssue, CaseManagementIssue newIssue) {
        CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
        
        String ongoing = cpp.getOngoingConcerns();
        String newOngoing;
        String description;       
        int idx;               
                
        description = origIssue.getIssue().getDescription();
        
        Pattern pattern = Pattern.compile("^" + description + "$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(ongoing);
        
        if( matcher.find()) {            
            description = newIssue.getIssue().getDescription();        
            newOngoing = matcher.replaceFirst(description);
            cpp.setOngoingConcerns(newOngoing);
            cpp.setUpdate_date(new Date());
            caseManagementCPPDAO.saveCPP(cpp);
            echartDAO.updateEchartOngoing(cpp);
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
            CaseManagementIssue iss = (CaseManagementIssue)itr.next();
            ongoing = ongoing + iss.getIssue().getDescription() + "\n";
        }
        
        cpp.setOngoingConcerns(ongoing);
        cpp.setUpdate_date(new Date());
        caseManagementCPPDAO.saveCPP(cpp);
        echartDAO.updateEchartOngoing(cpp);
    }

    /* get the filtered Notes by caisi role */
    public List getFilteredNotes(String providerNo, String demographic_no) {
        List allNotes = caseManagementNoteDAO.getNotesByDemographic(demographic_no);
        List role = roleProgramAccessDAO.getAllRoleName();
        List filteredNotes = new ArrayList();
        Iterator itr = allNotes.iterator();
        boolean added = false;
        while (itr.hasNext()) {
            CaseManagementNote note = (CaseManagementNote)itr.next();
            added = false;
            Set se = note.getIssues();
            if (se == null || se.size() == 0) {
                Iterator isit = se.iterator();
                while (isit.hasNext()) {
                    CaseManagementIssue iss = (CaseManagementIssue)isit.next();
                    for (int i = 0; i < role.size(); i++) {
                        String rl = (String)role.get(i);
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
    	/*
        List allNotes = caseManagementNoteDAO.getNotesByDemographic(demoNo);
        Iterator itr = allNotes.iterator();
        while (itr.hasNext()) {
            CaseManagementNote note = (CaseManagementNote)itr.next();
            if( note.getId() == noteId ) {
                continue;
            }
            Set issues = note.getIssues();
            Iterator its = issues.iterator();
            while (its.hasNext()) {
                CaseManagementIssue iss = (CaseManagementIssue)its.next();
                if (iss.getId().intValue() == issid.intValue()) return true;
            }
        }
        return false;
        */
    }
    
    
    public boolean haveIssue(Long issid, String demoNo) {
        List allNotes = caseManagementNoteDAO.getNotesByDemographic(demoNo);
        Iterator itr = allNotes.iterator();
        while (itr.hasNext()) {
            CaseManagementNote note = (CaseManagementNote)itr.next();
            Set issues = note.getIssues();
            Iterator its = issues.iterator();
            while (its.hasNext()) {
                CaseManagementIssue iss = (CaseManagementIssue)its.next();
                if (iss.getId().intValue() == issid.intValue()) return true;
            }
        }
        return false;
    }

    public boolean greaterEqualLevel(int level, String providerNo) {
        if (level < 1 || level > 4) return false;
        List pcrList = roleProgramAccessDAO.getAllRoleName();
        if (pcrList.size() == 0) return false;
        Iterator itr = pcrList.iterator();
        while (itr.hasNext()) {
            String pcr = (String)itr.next();
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

    // TODO terrible performance here.  TERRIBLE. at LEAST cache this - rwd
    public List getAccessRight(String providerNo, String demoNo, String programId) {
        List<Integer> progList = new ArrayList<Integer>();

        if (programId == null) {
            for (Object o : demographicDao.getProgramIdByDemoNo(demoNo)) {
                progList.add((Integer)o);
            }
        }
        else {
            progList.add(Integer.valueOf(programId));
        }

        if (progList.isEmpty()) {
            return null;
        }

        List rt = new ArrayList();
        Iterator<Integer> itr = progList.iterator();

        while (itr.hasNext()) {
            Integer pId = itr.next();

            List ppList = roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, pId.longValue());
            List paList = roleProgramAccessDAO.getAccessListByProgramID(pId.longValue());

            for (int i = 0; i < ppList.size(); i++) {
                ProgramProvider pp = (ProgramProvider)ppList.get(i);
                // add default role access
                List arList = roleProgramAccessDAO.getDefaultAccessRightByRole(pp.getRoleId());
                for (int j = 0; j < arList.size(); j++) {
                    DefaultRoleAccess ar = (DefaultRoleAccess)arList.get(j);
                    addrt(rt, ar.getAccess_type());
                }
                for (int k = 0; k < paList.size(); k++) {
                    ProgramAccess pa = (ProgramAccess)paList.get(k);
                    if (pa.isAllRoles()) {
                        addrt(rt, pa.getAccessType());
                    }
                    else if (roleInAccess(pp.getRoleId(), pa)) {
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
            Role rl = (Role)itr.next();
            if (roleId.compareTo(rl.getId()) == 0) return true;
        }
        return rt;
    }

    public void addrt(List rt, AccessType at) {
        if (at == null) return;

        boolean hasIt = false;
        for (int i = 0; i < rt.size(); i++) {
            AccessType ac = (AccessType)rt.get(i);
            if (ac.getId().compareTo(at.getId()) == 0) hasIt = true;
        }
        if (!hasIt) rt.add(at);
    }

    public boolean hasAccessRight(String accessName, String accessType, String providerNo, String demoNo, String pId) {
        if (accessName == null || accessType == null || pId == null || pId.equals("")) return false;
        if (new Long(pId).intValue() == 0) pId = null;
        List arList = getAccessRight(providerNo, demoNo, pId);
        for (int i = 0; i < arList.size(); i++) {
            AccessType at = (AccessType)arList.get(i);
            if (accessName.equalsIgnoreCase(at.getName()) && accessType.equalsIgnoreCase(at.getType())) return true;
        }
        return false;
    }

    public String getRoleName(String providerNo, String program_id) {
        String rt = "";
        List ppList = null;
        if (program_id == null || "".equalsIgnoreCase(program_id) || "null".equalsIgnoreCase(program_id)) ppList = roleProgramAccessDAO.getProgramProviderByProviderNo(providerNo);
        else {
            Long pid = new Long(program_id);
            ppList = roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, pid);
        }
        if (ppList != null && ppList.size() > 0) rt = ((ProgramProvider)ppList.get(0)).getRole().getName();
        return rt;
    }

    public String getProviderName(String providerNo) {
        Provider pv = providerDAO.getProvider(providerNo);
        if (pv != null) return pv.getFirstName() + " " + pv.getLastName();
        return null;
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
        else return dg.getYearOfBirth() + "-" + dg.getMonthOfBirth() + "-" + dg.getDateOfBirth();
    }

    public String getCaisiRoleById(String id) {
        //return providerCaisiRoleDAO.getCaisiRoleById(id);
        return roleManager.getRole(id).getName();
    }

    public List search(CaseManagementSearchBean searchBean) {
        return this.caseManagementNoteDAO.search(searchBean);
    }

    public List filterNotesByAccess(List notes, String providerNo) {
        List filteredNotes = new ArrayList();
        for (Iterator iter = notes.iterator(); iter.hasNext();) {
            CaseManagementNote note = (CaseManagementNote)iter.next();
            if (hasAccessRight(removeFirstSpace(getCaisiRoleById(note.getReporter_caisi_role())) + "notes", "access", providerNo, note.getDemographic_no(), note.getProgram_no())) {
                filteredNotes.add(note);
            }
        }
        return filteredNotes;
    }
    
    public void tmpSave(String providerNo, String demographicNo, String programId, String noteId, String note) {
            CaseManagementTmpSave tmp = new CaseManagementTmpSave();
            tmp.setProviderNo(providerNo);
            tmp.setDemographicNo(new Long(demographicNo).longValue());
            tmp.setProgramId(new Long(programId).longValue());
            if(noteId==null || "".equals(noteId)) {
            	noteId="0";
            }
            tmp.setNote_id(Long.parseLong(noteId));
            tmp.setNote(note);
            tmp.setUpdate_date(new Date());
            caseManagementTmpSaveDAO.save(tmp);
    }
   

    public void deleteTmpSave(String providerNo, String demographicNo, String programId) {
        caseManagementTmpSaveDAO.delete(providerNo, new Long(demographicNo), new Long(programId));
    }

    public CaseManagementTmpSave restoreTmpSave(String providerNo, String demographicNo, String programId) {
        CaseManagementTmpSave obj = caseManagementTmpSaveDAO.load(providerNo, new Long(demographicNo), new Long(programId));
        return obj;
    }
    
    //we want to load a temp saved note only if it's more recent than date
    public CaseManagementTmpSave restoreTmpSave(String providerNo, String demographicNo, String programId, Date date) {
        CaseManagementTmpSave obj = caseManagementTmpSaveDAO.load(providerNo, new Long(demographicNo), new Long(programId), date);
        return obj;
    }
    
    public List getHistory(String note_id) {
            CaseManagementNote note = caseManagementNoteDAO.getNote(Long.valueOf(note_id));
            return this.caseManagementNoteDAO.getHistory(note);
    }
    
    /*
     *Get all notes which have been linked to issues
     *@param issueIds csv of issue ids
     *@param demoNo demographic to search for
     */
    public List getIssueHistory(String issueIds, String demoNo) {
        return this.caseManagementNoteDAO.getIssueHistory(issueIds, demoNo);
    }

    /**
     * 
     * 
     * @param issues Unfiltered Set of issues
     * @param providerNo provider reading issues
     * @param programId program provider is logged into
     * 	
     */
    public List filterNotes(List notes, String providerNo, String programId, Integer currentFacilityId) {
        List filteredNotes = new ArrayList();

        if (notes.isEmpty()) {
            return notes;
        }

        //Get Role - if no ProgramProvider record found, show no issues.
        List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, new Long(programId));
        if (ppList == null || ppList.isEmpty()) {
            return new ArrayList();
        }

        ProgramProvider pp = (ProgramProvider)ppList.get(0);
        Role role = pp.getRole();

        //Load up access list from program
        List programAccessList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
        Map programAccessMap = convertProgramAccessListToMap(programAccessList);

        //iterate through the issue list
        for (Iterator iter = notes.iterator(); iter.hasNext();) {
            CaseManagementNote cmNote = (CaseManagementNote)iter.next();
            String noteRole = cmNote.getReporter_caisi_role();
            String noteRoleName = roleManager.getRole(noteRole).getName();
            ProgramAccess pa = null;
            boolean add = false;

            //write
            /*
            pa = (ProgramAccess)programAccessMap.get("write " + noteRoleName + " notes");
            if(pa != null) {
            	if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
            		cmIssue.setWriteAccess(true);
            		add = true;
            	}
            } else {
            	if(issueRole.equals(role.getName())) {
            		//default
            		cmIssue.setWriteAccess(true);
            		add=true;
            	}
            }
            */
            pa = null;
            //read
            pa = (ProgramAccess)programAccessMap.get("read " + noteRoleName + " notes");
            if (pa != null) {
                if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
                    //filteredIssues.add(cmIssue);
                    add = true;
                }
            }
            else {
                if (new Long(noteRole).intValue() == role.getId().intValue()) {
                    //default
                    add = true;
                }
            }

            //apply defaults
            if (!add) {
                if (new Long(noteRole).intValue() == role.getId().intValue()) {
                    //cmNote.setWriteAccess(true);
                    add = true;
                }
            }

            //did it pass the test?
            if (add) {
                filteredNotes.add(cmNote);
            }
        }
        
        // filter notes based on facility
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            filteredNotes = notesFacilityFiltering(currentFacilityId, filteredNotes);
        }

        return filteredNotes;
    }

    public boolean isRoleIncludedInAccess(ProgramAccess pa, Role role) {
        boolean result = false;

        for (Iterator iter = pa.getRoles().iterator(); iter.hasNext();) {
            Role accessRole = (Role)iter.next();
            if (role.getId() == accessRole.getId()) {
                return true;
            }
        }
        return result;
    }

    //private Map convertProgramAccessListToMap(List paList) {
    public Map convertProgramAccessListToMap(List paList) {
        Map map = new HashMap();

        for (Iterator iter = paList.iterator(); iter.hasNext();) {
            ProgramAccess pa = (ProgramAccess)iter.next();
            map.put(pa.getAccessType().getName(), pa);
        }
        return map;
    }

    public List searchIssues(String providerNo, String programId, String search) {
        //get Role
        //Get Role - if no ProgramProvider record found, show no issues.
        List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, new Long(programId));
        if (ppList == null || ppList.isEmpty()) {
            return new ArrayList();
        }
        ProgramProvider pp = (ProgramProvider)ppList.get(0);
        Role role = pp.getRole();

        //get program accesses
        List paList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
        Map paMap = convertProgramAccessListToMap(paList);

        //get all roles
        List allRoles = this.roleManager.getRoles();

        List allowableSearchRoles = new ArrayList();
        for (Iterator iter = allRoles.iterator(); iter.hasNext();) {
            Role r = (Role)iter.next();
            String key = "write " + r.getName() + " issues";
            ProgramAccess pa = (ProgramAccess)paMap.get(key);
            if (pa != null) {
                if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
                    allowableSearchRoles.add(r);
                }
            }
            if (pa == null && r.getId().intValue() == role.getId().intValue()) {
                allowableSearchRoles.add(r);
            }
        }

        List issList = issueDAO.search(search, allowableSearchRoles);

        return issList;
    }

    public List searchIssuesNoRolesConcerned(String providerNo, String programId, String search) {

        List issList = issueDAO.searchNoRolesConcerned(search);

        return issList;
    }

    public List<CaseManagementIssue> filterIssues(List<CaseManagementIssue> issues, String providerNo, String programId, Integer currentFacilityId) {
        List<CaseManagementIssue> filteredIssues = new ArrayList<CaseManagementIssue>();

        if (issues.isEmpty()) {
            return issues;
        }

        //Get Role - if no ProgramProvider record found, show no issues.
        List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, new Long(programId));
        if (ppList == null || ppList.isEmpty()) {
            return new ArrayList();
        }

        ProgramProvider pp = (ProgramProvider)ppList.get(0);
        Role role = pp.getRole();

        //Load up access list from program
        List programAccessList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
        Map programAccessMap = convertProgramAccessListToMap(programAccessList);

        //iterate through the issue list
        for (Iterator iter = issues.iterator(); iter.hasNext();) {
            CaseManagementIssue cmIssue = (CaseManagementIssue)iter.next();
            String issueRole = cmIssue.getIssue().getRole();
            ProgramAccess pa = null;
            boolean add = false;

            //write
            pa = (ProgramAccess)programAccessMap.get("write " + issueRole + " issues");
            if (pa != null) {
                if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
                    cmIssue.setWriteAccess(true);
                    add = true;
                }
            }
            else {
                if (issueRole.equals(role.getName())) {
                    //default
                    cmIssue.setWriteAccess(true);
                    add = true;
                }
            }
            pa = null;
            //read
            pa = (ProgramAccess)programAccessMap.get("read " + issueRole + " issues");
            if (pa != null) {
                if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
                    //filteredIssues.add(cmIssue);
                    add = true;
                }
            }
            else {
                if (issueRole.equals(role.getName())) {
                    //default
                    add = true;
                }
            }

            //apply defaults
            if (!add) {
                if (issueRole.equals(role.getName())) {
                    cmIssue.setWriteAccess(true);
                    add = true;
                }
            }

            //did it pass the test?
            if (add) {
                filteredIssues.add(cmIssue);
            }
        }
        
        // filter issues based on facility
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            filteredIssues = issuesFacilityFiltering(currentFacilityId, filteredIssues);
        }

        return filteredIssues;
    }

    private List<CaseManagementIssue> issuesFacilityFiltering(Integer currentFacilityId, List<CaseManagementIssue> issues) {
        ArrayList<CaseManagementIssue> results = new ArrayList<CaseManagementIssue>();

        for (CaseManagementIssue caseManagementIssue : issues) {
            Integer programId = caseManagementIssue.getProgram_id();
            if (programManager.hasAccessBasedOnFacility(currentFacilityId, programId)) 
            	results.add(caseManagementIssue);
        }

        return results;
    }

    private List<CaseManagementNote> notesFacilityFiltering(Integer currentFacilityId, List<CaseManagementNote> notes) {

        ArrayList<CaseManagementNote> results = new ArrayList<CaseManagementNote>();

        for (CaseManagementNote caseManagementNote : notes) {
            String programId = caseManagementNote.getProgram_no();
            
            if (programId==null || "".equals(programId)) {
            	results.add(caseManagementNote);
            } else {
            	if (programManager.hasAccessBasedOnFacility(currentFacilityId, Integer.parseInt(programId)))
            		results.add(caseManagementNote);
            }
        }

        return results;
    }
    
    public void saveNoteSimple(CaseManagementNote note) {
        this.caseManagementNoteDAO.saveNote(note);
    }

    public boolean isClientInProgramDomain(String providerNo, String demographicNo) {

        List providerPrograms = roleProgramAccessDAO.getProgramProviderByProviderNo(providerNo);

        List allAdmissions = this.admissionManager.getAdmissions(Integer.valueOf(demographicNo));

        for (int x = 0; x < providerPrograms.size(); x++) {
            ProgramProvider pp = (ProgramProvider)providerPrograms.get(x);
            long programId = pp.getProgramId().longValue();

            for (int y = 0; y < allAdmissions.size(); y++) {
                Admission admission = (Admission)allAdmissions.get(y);
                long admissionProgramId = admission.getProgramId().longValue();

                if (programId == admissionProgramId) {
                    return true;
                }
            }
        }

        return false;
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
        String[] issueIdList = new String[1];
        issueIdList[0] = String.valueOf(newIssueId);
        List<CaseManagementNote> notes = this.caseManagementNoteDAO.getNotesByDemographic(demographicNo);
        for(CaseManagementNote note:notes) {
        	Set<CaseManagementIssue> issues = note.getIssues();
        	for(CaseManagementIssue issue:issues) {
        		if(issue.getIssue().getId().equals(originalIssueId)) {
        			//update this CaseManagementIssue
        			issue.setIssue(null);
        			issue.setIssue_id(newIssueId.longValue());
        		}
        	}
        	this.caseManagementNoteDAO.saveNote(note);
        }
        */
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEchartDAO(EchartDAO echartDAO) {
        this.echartDAO = echartDAO;
    }

    public void setApptDAO(ApptDAO apptDAO) {
        this.apptDAO = apptDAO;
    }

    public void setEncounterFormDAO(EncounterFormDAO dao) {
        this.encounterFormDAO = dao;
    }

    public void setMessagetblDAO(MessagetblDAO dao) {
        this.messagetblDAO = dao;
    }

    public void setCaseManagementNoteDAO(CaseManagementNoteDAO dao) {
        this.caseManagementNoteDAO = dao;
    }

    public void setCaseManagementIssueDAO(CaseManagementIssueDAO dao) {
        this.caseManagementIssueDAO = dao;
    }

    public void setIssueDAO(IssueDAO dao) {
        this.issueDAO = dao;
    }

    public void setCaseManagementCPPDAO(CaseManagementCPPDAO dao) {
        this.caseManagementCPPDAO = dao;
    }
    
    public void setAllergyDAO(AllergyDAO dao) {
        this.allergyDAO = dao;
    }

    public void setPrescriptionDAO(PrescriptionDAO dao) {
        this.prescriptionDAO = dao;
    }

    public void setClientImageDAO(ClientImageDAO dao) {
        this.clientImageDAO = dao;
    }

    public void setRoleManager(RoleManager mgr) {
        this.roleManager = mgr;
    }

    public void setProviderSignitureDao(ProviderSignitureDao providerSignitureDao) {
        this.providerSignitureDao = providerSignitureDao;
    }

    public void setRoleProgramAccessDAO(RoleProgramAccessDAO roleProgramAccessDAO) {
        this.roleProgramAccessDAO = roleProgramAccessDAO;
    }

    public void setDemographicDao(ClientDao demographicDao) {
        this.demographicDao = demographicDao;
    }

    public void setProviderDAO(ProviderDao providerDAO) {
        this.providerDAO = providerDAO;
    }

    public void setCaseManagementTmpSaveDAO(CaseManagementTmpSaveDAO dao) {
        this.caseManagementTmpSaveDAO = dao;
    }

    protected String removeFirstSpace(String withSpaces) {
        int spaceIndex = withSpaces.indexOf(' '); //use lastIndexOf to remove last space
        if (spaceIndex < 0) { //no spaces!
            return withSpaces;
        }
        return withSpaces.substring(0, spaceIndex) + withSpaces.substring(spaceIndex + 1, withSpaces.length());
    }

    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }
    
    public void setHashAuditDAO(HashAuditDAO dao) {
        this.hashAuditDAO = dao;
    }
    
    public void setEctWindowDAO(EncounterWindowDAO dao) {
        this.ectWindowDAO = dao;
    }
    
    public void setUserPropertyDAO( UserPropertyDAO dao) {
        this.userPropertyDAO = dao;
    }
    
    public void setDxResearchDAO(DxResearchDAO dao) {
    	this.dxResearchDAO = dao;
    }
    
    public void saveToDx(String demographicNo, String code, String codingSystem) {
    	if(codingSystem == null) {
    		codingSystem = "icd10";
    	}
    	
    	DxResearch dx = new DxResearch();
    	dx.setCode(code);
    	dx.setCodingSystem(codingSystem);
    	dx.setDemographicNo(Integer.parseInt(demographicNo));
    	dx.setStartDate(new Date());
    	dx.setUpdateDate(new Date());
    	dx.setStatus("A");
    	    	
    	this.dxResearchDAO.save(dx);
    }
    
    public void saveToDx(String demographicNo, String code) {
    	saveToDx(demographicNo,code,null);
    }
    
    public List<DxResearch> getDxByDemographicNo(String demographicNo) {
    	return this.dxResearchDAO.getByDemographicNo(Integer.parseInt(demographicNo));
    }
}
