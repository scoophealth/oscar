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

package org.oscarehr.PMmodule.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramClientStatusDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramFunctionalUserDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProgramSignatureDao;
import org.oscarehr.PMmodule.dao.ProgramTeamDAO;
import org.oscarehr.PMmodule.dao.VacancyTemplateDao;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.FunctionalUserType;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramSignature;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.VacancyTemplate;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.EncounterTypeDao;
import org.oscarehr.common.dao.ProgramEncounterTypeDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.EncounterType;
import org.oscarehr.common.model.ProgramEncounterType;
import org.oscarehr.common.model.ProgramEncounterTypePK;
import org.oscarehr.util.LoggedInInfo;

import oscar.OscarProperties;
public class ProgramManager {

    private ProgramDao programDao;
    private ProgramProviderDAO programProviderDAO;
    private ProgramFunctionalUserDAO programFunctionalUserDAO;
    private ProgramTeamDAO programTeamDAO;
    private ProgramAccessDAO programAccessDAO;
    private AdmissionDao admissionDao;
    private DefaultRoleAccessDAO defaultRoleAccessDAO;
    private ProgramClientStatusDAO clientStatusDAO;
    private ProgramSignatureDao programSignatureDao;
    private VacancyTemplateDao vacancyTemplateDao;
    private ProgramEncounterTypeDao programEncounterTypeDao; 
    private EncounterTypeDao encounterTypeDao;
    
    private boolean enabled;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ProgramSignatureDao getProgramSignatureDao() {
        return programSignatureDao;
    }

    public void setProgramSignatureDao(ProgramSignatureDao programSignatureDao) {
        this.programSignatureDao = programSignatureDao;
    }

    public void setProgramDao(ProgramDao dao) {
        this.programDao = dao;
    }

    public void setProgramProviderDAO(ProgramProviderDAO dao) {
        this.programProviderDAO = dao;
    }

    public void setProgramFunctionalUserDAO(ProgramFunctionalUserDAO dao) {
        this.programFunctionalUserDAO = dao;
    }

    public void setProgramTeamDAO(ProgramTeamDAO dao) {
        this.programTeamDAO = dao;
    }

    public void setProgramAccessDAO(ProgramAccessDAO dao) {
        this.programAccessDAO = dao;
    }

    public void setAdmissionDao(AdmissionDao dao) {
        this.admissionDao = dao;
    }

    public void setDefaultRoleAccessDAO(DefaultRoleAccessDAO dao) {
        this.defaultRoleAccessDAO = dao;
    }

    public void setProgramClientStatusDAO(ProgramClientStatusDAO dao) {
        this.clientStatusDAO = dao;
    }
    
    

    public void setProgramEncounterTypeDao(ProgramEncounterTypeDao programEncounterTypeDao) {
		this.programEncounterTypeDao = programEncounterTypeDao;
	}

	public void setEncounterTypeDao(EncounterTypeDao encounterTypeDao) {
		this.encounterTypeDao = encounterTypeDao;
	}

	public Program getProgram(String programId) {
        return programDao.getProgram(Integer.valueOf(programId));
    }

    public Program getProgram(Integer programId) {
        return programDao.getProgram(programId);
    }

    public Program getProgram(Long programId) {
        return programDao.getProgram(new Integer(programId.intValue()));
    }
    
    public List<Program> getActiveProgramByFacility(String providerNo, Integer facilityId) {
        List<Program> programs = new ArrayList<Program>();
        for (ProgramProvider programProvider : programProviderDAO.getProgramDomainByFacility(providerNo, facilityId)) {
            Program program = this.getProgram(programProvider.getProgramId());
            if (program.isActive()) {
                programs.add(program);
            }
        }
        return programs;
    }

    public String getProgramName(String programId) {
        return programDao.getProgramName(Integer.valueOf(programId));
    }

    public Integer getProgramIdByProgramName(String programName) {
    	return programDao.getProgramIdByProgramName(programName);
    }

    public List<Program> getAllPrograms() {
        return programDao.getAllPrograms();
    }

    public List<Program> getAllPrograms(String programStatus, String type, int facilityId) {
        return programDao.getAllPrograms(programStatus, type, facilityId);
    }

    /**
      * facilityId can be null, it will return all community programs optionally filtering by facility id if filtering is enabled.
     */
    public List<Program> getCommunityPrograms(Integer facilityId) {
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            return programDao.getCommunityProgramsByFacilityId(facilityId);
        }
        else {
            return programDao.getPrograms();
        }
    }

 
    public List<Program> getPrograms(Integer facilityId) {
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            return programDao.getProgramsByFacilityId(facilityId);
        }
        else {
            return programDao.getAllPrograms();
        }
    }

    public List<Program> getPrograms() {
        return programDao.getPrograms();
    }

    public Program[] getBedPrograms() {
        return programDao.getProgramsByType(null, Program.BED_TYPE, null).toArray(new Program[0]);
    }

    public Program[] getBedPrograms(Integer facilityId) {
        return programDao.getProgramsByType(facilityId, Program.BED_TYPE, null).toArray(new Program[0]);
    }

    public List<Program> getServicePrograms() {
        return programDao.getProgramsByType(null, Program.SERVICE_TYPE, null);
    }

    public Program[] getExternalPrograms() {
        return programDao.getProgramsByType(null, Program.EXTERNAL_TYPE, true).toArray(new Program[0]);
    }

    public boolean isBedProgram(String programId) {
        return programDao.isBedProgram(Integer.valueOf(programId));
    }

    public boolean isServiceProgram(String programId) {
        return programDao.isServiceProgram(Integer.valueOf(programId));
    }

    public boolean isCommunityProgram(String programId) {
        return programDao.isCommunityProgram(Integer.valueOf(programId));
    }

    public void saveProgram(Program program) {
        if (program.getHoldingTank()) {
            programDao.resetHoldingTank();
        }
        programDao.saveProgram(program);
    }

    public void removeProgram(String programId) {
        programDao.removeProgram(Integer.valueOf(programId));
    }

    public List<ProgramProvider> getProgramProviders(String programId) {
        return programProviderDAO.getProgramProviders(Long.valueOf(programId));
    }

    public List<ProgramProvider> getProgramProvidersByProvider(String providerNo) {
        return programProviderDAO.getProgramProvidersByProvider(providerNo);
    }

    public ProgramProvider getProgramProvider(String id) {
        return programProviderDAO.getProgramProvider(Long.valueOf(id));
    }

    public ProgramProvider getProgramProvider(String providerNo, String programId) {
        return programProviderDAO.getProgramProvider(providerNo, Long.valueOf(programId));
    }

    public void saveProgramProvider(LoggedInInfo loggedInInfo, ProgramProvider pp) {
    	pp.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
    	pp.setLastUpdateDate(new Date());
        programProviderDAO.saveProgramProvider(pp);
    }

    public void deleteProgramProvider(String id) {
        programProviderDAO.deleteProgramProvider(Long.valueOf(id));
    }

    public void deleteProgramProviderByProgramId(Long programId) {
        programProviderDAO.deleteProgramProviderByProgramId(programId);
    }

    public List<FunctionalUserType> getFunctionalUserTypes() {
        return programFunctionalUserDAO.getFunctionalUserTypes();
    }

    public FunctionalUserType getFunctionalUserType(String id) {
        return programFunctionalUserDAO.getFunctionalUserType(Long.valueOf(id));
    }

    public void saveFunctionalUserType(FunctionalUserType fut) {
        programFunctionalUserDAO.saveFunctionalUserType(fut);
    }

    public void deleteFunctionalUserType(String id) {
        programFunctionalUserDAO.deleteFunctionalUserType(Long.valueOf(id));
    }

    public List<FunctionalUserType> getFunctionalUsers(String programId) {
        return programFunctionalUserDAO.getFunctionalUsers(Long.valueOf(programId));
    }

    public ProgramFunctionalUser getFunctionalUser(String id) {
        return programFunctionalUserDAO.getFunctionalUser(Long.valueOf(id));
    }

    public void saveFunctionalUser(ProgramFunctionalUser pfu) {
        programFunctionalUserDAO.saveFunctionalUser(pfu);
    }

    public void deleteFunctionalUser(String id) {
        programFunctionalUserDAO.deleteFunctionalUser(Long.valueOf(id));
    }

    public Long getFunctionalUserByUserType(Long programId, Long userTypeId) {
        return programFunctionalUserDAO.getFunctionalUserByUserType(programId, userTypeId);
    }

    public List<ProgramTeam> getProgramTeams(String programId) {
        return programTeamDAO.getProgramTeams(Integer.valueOf(programId));
    }

    public ProgramTeam getProgramTeam(String id) {
        return programTeamDAO.getProgramTeam(Integer.valueOf(id));
    }

    public void saveProgramTeam(ProgramTeam team) {
        programTeamDAO.saveProgramTeam(team);
    }

    public void deleteProgramTeam(String id) {
        programTeamDAO.deleteProgramTeam(Integer.valueOf(id));
    }

    public boolean teamNameExists(Integer programId, String teamName) {
        return programTeamDAO.teamNameExists(programId, teamName);
    }

    public List<ProgramAccess> getProgramAccesses(String programId) {
        return programAccessDAO.getAccessListByProgramId(Long.valueOf(programId));
    }

    public ProgramAccess getProgramAccess(String id) {
        return programAccessDAO.getProgramAccess(Long.valueOf(id));
    }

    public void saveProgramAccess(ProgramAccess pa) {
        programAccessDAO.saveProgramAccess(pa);
    }

    public void deleteProgramAccess(String id) {
        programAccessDAO.deleteProgramAccess(Long.valueOf(id));
    }

    public List<AccessType> getAccessTypes() {
        return programAccessDAO.getAccessTypes();
    }

    public AccessType getAccessType(Long id) {
        return programAccessDAO.getAccessType(id);
    }

    public List<ProgramProvider> getAllProvidersInTeam(Integer programId, Integer teamId) {
        return this.programProviderDAO.getProgramProvidersInTeam(programId, teamId);
    }

    public List<Admission> getAllClientsInTeam(Integer programId, Integer teamId) {
        return admissionDao.getAdmissionsInTeam(programId, teamId);
    }

    public List<Program> search(Program criteria) {
        return this.programDao.search(criteria);
    }

    public List<Program> searchByFacility(Program criteria, Integer facilityId){
        return this.programDao.searchByFacility(criteria, facilityId);
    }

    public Program getHoldingTankProgram() {
        return this.programDao.getHoldingTankProgram();
    }

    public ProgramAccess getProgramAccess(String programId, String accessTypeId) {
        return this.programAccessDAO.getProgramAccess(Long.valueOf(programId), Long.valueOf(accessTypeId));
    }

    public List<Program> getProgramDomain(String providerNo) {
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator<?> i = programProviderDAO.getProgramDomain(providerNo).iterator(); i.hasNext();) {
            ProgramProvider programProvider = (ProgramProvider) i.next();
            Program p = getProgram(programProvider.getProgramId());
            if(p != null)
            	programDomain.add(p);
        }

        return programDomain;
    }


    public List<Program> getActiveProgramDomain(String providerNo) {
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator<?> i = programProviderDAO.getActiveProgramDomain(providerNo).iterator(); i.hasNext();) {
            ProgramProvider programProvider = (ProgramProvider) i.next();
            Program p = getProgram(programProvider.getProgramId());
            if(p!=null)
            	programDomain.add(p);
        }

        return programDomain;
    }

    public List<Program> getProgramDomainInCurrentFacilityForCurrentProvider(LoggedInInfo loggedInInfo, boolean activeOnly) {
    	List<Program> programs = null;

    	if (activeOnly) programs=getActiveProgramDomain(loggedInInfo.getLoggedInProviderNo());
    	else programs=getProgramDomain(loggedInInfo.getLoggedInProviderNo());

    	List<Program> results = new ArrayList<Program>();
    	for(Program program : programs) {
    		if(program.getFacilityId()==loggedInInfo.getCurrentFacility().getId().intValue()) {
    			results.add(program);
    		}
    	}
    	return results;
    }

    public Program[] getCommunityPrograms() {
        return programDao.getProgramsByType(null, Program.COMMUNITY_TYPE, null).toArray(new Program[0]);
    }

    public List<LabelValueBean> getProgramBeans(String providerNo) {
        if (providerNo == null || "".equalsIgnoreCase(providerNo.trim())) return new ArrayList<LabelValueBean>();
        ArrayList<LabelValueBean> pList = new ArrayList<LabelValueBean>();
        List<Program> programs = programDao.getProgramsByType(null, Program.COMMUNITY_TYPE, null);
        for (Program program : programs) {
            pList.add(new LabelValueBean(program.getName(), program.getId().toString()));
        }
        return pList;
        /*
         * Iterator iter = programProviderDAOT.getProgramProvidersByProvider(new Long(providerNo)).iterator(); ArrayList pList = new ArrayList(); while (iter.hasNext()) { ProgramProvider p = (ProgramProvider) iter.next(); if (p!=null && p.getProgramId() !=
         * null && p.getProgramId().longValue()>0){ //logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString()); Program program = programDao.getProgram(new Integer(p.getProgramId().intValue()));
         * pList.add(new LabelValueBean(program.getName(),program.getId().toString())); } } return pList;
         */
    }

    public List<DefaultRoleAccess> getDefaultRoleAccesses() {
        return defaultRoleAccessDAO.getDefaultRoleAccesses();
    }

    public DefaultRoleAccess getDefaultRoleAccess(String id) {
        return defaultRoleAccessDAO.getDefaultRoleAccess(Long.valueOf(id));
    }

    public void saveDefaultRoleAccess(DefaultRoleAccess dra) {
        defaultRoleAccessDAO.saveDefaultRoleAccess(dra);
    }

    public void deleteDefaultRoleAccess(String id) {
        defaultRoleAccessDAO.deleteDefaultRoleAccess(Long.valueOf(id));
    }

    public DefaultRoleAccess findDefaultRoleAccess(long roleId, long accessTypeId) {
        return defaultRoleAccessDAO.find(new Long(roleId), new Long(accessTypeId));
    }

    public List<ProgramClientStatus> getProgramClientStatuses(Integer programId) {
        return clientStatusDAO.getProgramClientStatuses(programId);
    }

    public void saveProgramClientStatus(ProgramClientStatus status) {
        clientStatusDAO.saveProgramClientStatus(status);
    }

    public void deleteProgramClientStatus(String id) {
        clientStatusDAO.deleteProgramClientStatus(id);
    }

    public boolean clientStatusNameExists(Integer programId, String statusName) {
        return clientStatusDAO.clientStatusNameExists(programId, statusName);
    }

    public List<Admission> getAllClientsInStatus(Integer programId, Integer statusId) {
        return clientStatusDAO.getAllClientsInStatus(programId, statusId);
    }

    public ProgramClientStatus getProgramClientStatus(String statusId) {
        return clientStatusDAO.getProgramClientStatus(statusId);
    }

    public ProgramSignature getProgramFirstSignature(Integer programId) {
        return programSignatureDao.getProgramFirstSignature(programId);
    }

    public List<ProgramSignature> getProgramSignatures(Integer programId) {
        return programSignatureDao.getProgramSignatures(programId);
    }

    public void saveProgramSignature(ProgramSignature programSignature) {
        programSignatureDao.saveProgramSignature(programSignature);
    }
    
    public VacancyTemplate getVacancyTemplate(Integer templateId) {
    	return vacancyTemplateDao.getVacancyTemplate(templateId);
    }

	public void setVacancyTemplateDao(VacancyTemplateDao vacancyTemplateDao) {
    	this.vacancyTemplateDao = vacancyTemplateDao;
    }

	public boolean hasAccessBasedOnCurrentFacility(LoggedInInfo loggedInInfo, Integer programId) {
    	// if no program restrictions are defined.
        if (programId == null) return(true);

        // check the providers facilities against the programs facilities
        Program program = getProgram(programId);
        if(program!=null) {
        	return(program.getFacilityId() == loggedInInfo.getCurrentFacility().getId().intValue());
        } else {
        	return false;
        }
    }

    public List<Program> getAllProgramsByRole(String providerNo,int roleId) {
    	List<Program> results = new ArrayList<Program>();
    	List<ProgramProvider> ppList = programProviderDAO.getProgramProvidersByProvider(providerNo);
    	for(ProgramProvider pp:ppList) {
    		if(pp.getRoleId().intValue() == roleId) {
    			Program p = programDao.getProgram(pp.getProgramId().intValue());
    			results.add(p);
    		}
    	}
    	return results;
    }
    

    public List<ProgramEncounterType> getCustomEncounterTypes(LoggedInInfo loggedInInfo, Integer programId) {
    	List<ProgramEncounterType> types =  programEncounterTypeDao.findByProgramId(programId);
    	for(ProgramEncounterType type:types) {
    		type.setEncounterType(encounterTypeDao.find(type.getId().getEncounterTypeId()));
    	}
    	return types;
    }
    
    public void deleteCustomEncounterType(ProgramEncounterTypePK id) {
    	programEncounterTypeDao.remove(id);
    }
    
    public void saveCustomEncounterType(ProgramEncounterType encounterType) {
    	programEncounterTypeDao.persist(encounterType);
    }
    
    public ProgramEncounterType findCustomEncounterType(ProgramEncounterTypePK id) {
    	return programEncounterTypeDao.find(id);
    }
    
    public List<EncounterType> getNonGlobalEncounterTypes(LoggedInInfo loggedInInfo) {
    	List<EncounterType> types = encounterTypeDao.findNonGlobal();
    	return types;
    }
    
}
