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

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.codehaus.xfire.XFireRuntimeException;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramClientStatusDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramFunctionalUserDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProgramSignatureDao;
import org.oscarehr.PMmodule.dao.ProgramTeamDAO;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.FunctionalUserType;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramSignature;
import org.oscarehr.PMmodule.model.ProgramTeam;

public class ProgramManager {
	
	private static Log log = LogFactory.getLog(ProgramManager.class);

	private ProgramDao dao;
	private ProgramProviderDAO programProviderDAO;
	private ProgramFunctionalUserDAO programFunctionalUserDAO;
	private ProgramTeamDAO programTeamDAO;
	private ProgramAccessDAO programAccessDAO;
	private AdmissionDao admissionDAO;
	private IntegratorManager integratorManager;
	private DefaultRoleAccessDAO defaultRoleAccessDAO;
	private ProgramClientStatusDAO clientStatusDAO;
	private ProgramSignatureDao programSignatureDao;
	
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

	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}

	public void setProgramDao(ProgramDao dao) {
		this.dao = dao;
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

	public void setAdmissionDAO(AdmissionDao dao) {
		this.admissionDAO = dao;
	}

	public void setDefaultRoleAccessDAO(DefaultRoleAccessDAO dao) {
		this.defaultRoleAccessDAO = dao;
	}

	public void setProgramClientStatusDAO(ProgramClientStatusDAO dao) {
		this.clientStatusDAO = dao;
	}
	
	public Program getProgram(String programId) {
		return dao.getProgram(Integer.valueOf(programId));
	}

	public Program getProgram(Integer programId) {
		return dao.getProgram(programId);
	}

	public Program getProgram(Long programId) {
		return dao.getProgram(new Integer(programId.intValue()));
	}

	public Program getProgram(Long agencyId, String programId) {
		return getProgram(agencyId, new Integer(programId));
	}

	/**
	 * @return a program from the integrator or null upon error.
	 */
	public Program getProgram(Long agencyId, Integer programId) {
//		if (integratorManager != null) {
//			return integratorManager.getProgram(agencyId, new Long(programId.longValue()));
//		}
		
		return null;
	}

	public String getProgramName(String programId) {
		return dao.getProgramName(Integer.valueOf(programId));
	}

	public List<Program> getAllPrograms() {
		return dao.getAllPrograms();
	}

	public List<Program> getAllActivePrograms() {
		return dao.getAllActivePrograms();
	}
	
	public List<Program> getProgramsByAgencyId(String agencyId) {
		return dao.getProgramsByAgencyId(agencyId);
	}

	public Program[] getBedPrograms() {
		return dao.getBedPrograms();
	}

	public List getServicePrograms() {
		return dao.getServicePrograms();
	}

	public List getExternalPrograms() {
		return dao.getExternalPrograms();
	}
	
	public boolean isBedProgram(String programId) {
		return dao.isBedProgram(Integer.valueOf(programId));
	}

	public boolean isServiceProgram(String programId) {
		return dao.isServiceProgram(Integer.valueOf(programId));
	}

	public boolean isCommunityProgram(String programId) {
		return dao.isCommunityProgram(Integer.valueOf(programId));
	}

	public void saveProgram(Program program) {
		if (program.getHoldingTank()) {
			dao.resetHoldingTank();
		}
		dao.saveProgram(program);
	}

	public void removeProgram(String programId) {
		dao.removeProgram(Integer.valueOf(programId));
	}

	// TODO: Implement this method for real
	public Agency getAgencyByProgram(String programId) {
		return new Agency(new Long(0), 1, "HS", "HS", "", true, false);
	}

	public List getProgramProviders(String programId) {
		return programProviderDAO.getProgramProviders(Long.valueOf(programId));
	}

	public List getProgramProvidersByProvider(String providerNo) {
		return programProviderDAO.getProgramProvidersByProvider(providerNo);
	}

	public ProgramProvider getProgramProvider(String id) {
		return programProviderDAO.getProgramProvider(Long.valueOf(id));
	}

	public ProgramProvider getProgramProvider(String providerNo, String programId) {
		return programProviderDAO.getProgramProvider(providerNo, Long.valueOf(programId));
	}

	public void saveProgramProvider(ProgramProvider pp) {
		programProviderDAO.saveProgramProvider(pp);
	}

	public void deleteProgramProvider(String id) {
		programProviderDAO.deleteProgramProvider(Long.valueOf(id));
	}

	public void deleteProgramProviderByProgramId(Long programId){
		programProviderDAO.deleteProgramProviderByProgramId(programId);
	}
	
	public List getFunctionalUserTypes() {
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

	public List getFunctionalUsers(String programId) {
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

	public List getProgramTeams(String programId) {
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

	public List getProgramAccesses(String programId) {
		return programAccessDAO.getProgramAccesses(Long.valueOf(programId));
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

	public List getAccessTypes() {
		return programAccessDAO.getAccessTypes();
	}

	public AccessType getAccessType(Long id) {
		return programAccessDAO.getAccessType(id);
	}

	public List getAllProvidersInTeam(Integer programId, Integer teamId) {
		return this.programProviderDAO.getProgramProvidersInTeam(programId, teamId);
	}

	public List getAllClientsInTeam(Integer programId, Integer teamId) {
		return this.admissionDAO.getAdmissionsInTeam(programId, teamId);
	}

	public List search(Program criteria) {
		return this.dao.search(criteria);
	}

	public Program getHoldingTankProgram() {
		return this.dao.getHoldingTankProgram();
	}

	public ProgramAccess getProgramAccess(String programId, String accessTypeId) {
		return this.programAccessDAO.getProgramAccess(Long.valueOf(programId), Long.valueOf(accessTypeId));
	}

	// nys
	public Long getAgencyIdByProgramId(String programId) {
		Program p = dao.getProgram(Integer.valueOf(programId));
		Long pp = p.getAgencyId();
		return pp;
	}

	public List<Program> getProgramDomain(String providerNo) {
		List<Program> programDomain = new ArrayList<Program>();
		
		for (Iterator<?> i = programProviderDAO.getProgramDomain(providerNo).iterator(); i.hasNext();) {
			ProgramProvider programProvider = (ProgramProvider) i.next();
			programDomain.add(getProgram(programProvider.getProgramId()));
		}
		
		return programDomain;
	}

	public Program[] getCommunityPrograms() {
		return dao.getCommunityPrograms();
	}
	
	public List getProgramBeans(String providerNo) {		
		if (providerNo==null||"".equalsIgnoreCase(providerNo.trim())) return new ArrayList();
		ArrayList pList = new ArrayList();
		Program[] program = dao.getCommunityPrograms();
		for(int i=0; i<program.length; i++){
			pList.add(new LabelValueBean(program[i].getName(),program[i].getId().toString()));
		}
		return pList;
		/*
		Iterator iter = programProviderDAOT.getProgramProvidersByProvider(new Long(providerNo)).iterator();
		ArrayList pList = new ArrayList();
		while (iter.hasNext())
		{
			ProgramProvider p = (ProgramProvider) iter.next();
			if (p!=null && p.getProgramId() != null && p.getProgramId().longValue()>0){
				//logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString());
				Program program = programDao.getProgram(new Integer(p.getProgramId().intValue()));
				pList.add(new LabelValueBean(program.getName(),program.getId().toString()));
			}
		}
		return pList;
		*/
	}
	
	public List getDefaultRoleAccesses() {
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
		return clientStatusDAO.clientStatusNameExists(programId,statusName);
	}
	
	public List getAllClientsInStatus(Integer programId, Integer statusId) {
		return clientStatusDAO.getAllClientsInStatus(programId,statusId);
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
}
