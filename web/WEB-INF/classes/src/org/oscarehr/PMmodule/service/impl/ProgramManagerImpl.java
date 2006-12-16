package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.XFireRuntimeException;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramFunctionalUserDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProgramTeamDAO;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.FunctionalUserType;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramManager;

public class ProgramManagerImpl implements ProgramManager {
	
	private static Log log = LogFactory.getLog(ProgramManagerImpl.class);

	private ProgramDao dao;

	private ProgramProviderDAO programProviderDAO;

	private ProgramFunctionalUserDAO programFunctionalUserDAO;

	private ProgramTeamDAO programTeamDAO;

	private ProgramAccessDAO programAccessDAO;

	private AdmissionDao admissionDAO;

	private IntegratorManager integratorManager;

	private DefaultRoleAccessDAO defaultRoleAccessDAO;

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

	public Program getProgram(Long agencyId, Integer programId) {
		if (integratorManager != null && integratorManager.isEnabled() && integratorManager.isRegistered()) {
			try {
				return integratorManager.getProgram(agencyId, new Long(programId.longValue()));
			} catch (IntegratorNotEnabledException e) {
				log.error(e);
			} catch (XFireRuntimeException e) {
				log.error(e);
			}
		}
		return getProgram(programId);
	}

	public String getProgramName(String programId) {
		return dao.getProgramName(Integer.valueOf(programId));
	}

	public List getAllPrograms() {
		return dao.getAllPrograms();
	}

	public List getProgramsByAgencyId(String agencyId) {
		return dao.getProgramsByAgencyId(agencyId);
	}

	public Program[] getBedPrograms() {
		return dao.getBedPrograms();
	}

	public List getServicePrograms() {
		return dao.getServicePrograms();
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
		return new Agency(new Long(0), "Seaton House", true, false);
	}

	public List getProgramProviders(String programId) {
		return programProviderDAO.getProgramProviders(Long.valueOf(programId));
	}

	public List getProgramProvidersByProvider(String providerNo) {
		return programProviderDAO.getProgramProvidersByProvider(Long.valueOf(providerNo));
	}

	public ProgramProvider getProgramProvider(String id) {
		return programProviderDAO.getProgramProvider(Long.valueOf(id));
	}

	public ProgramProvider getProgramProvider(String providerNo, String programId) {
		return programProviderDAO.getProgramProvider(Long.valueOf(providerNo), Long.valueOf(programId));
	}

	public void saveProgramProvider(ProgramProvider pp) {
		programProviderDAO.saveProgramProvider(pp);
	}

	public void deleteProgramProvider(String id) {
		programProviderDAO.deleteProgramProvider(Long.valueOf(id));
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

	public List getProgramDomain(String providerNo) {
		List<Program> programDomain = new ArrayList<Program>();
		
		for (Iterator i = programProviderDAO.getProgramDomain(new Long(providerNo)).iterator(); i.hasNext();) {
			ProgramProvider programProvider = (ProgramProvider) i.next();
			programDomain.add(getProgram(programProvider.getProgramId().toString()));
		}
		
		return programDomain;
	}

	public List getCommunityPrograms() {
		Program p = new Program();
		p.setType("community");
		return dao.search(p);
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
}
