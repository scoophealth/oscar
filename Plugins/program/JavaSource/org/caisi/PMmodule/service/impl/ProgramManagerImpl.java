package org.caisi.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.caisi.PMmodule.dao.AdmissionDao;
import org.caisi.PMmodule.dao.ProgramAccessDAO;
import org.caisi.PMmodule.dao.ProgramBedLogDAO;
import org.caisi.PMmodule.dao.ProgramDao;
import org.caisi.PMmodule.dao.ProgramFunctionalUserDAO;
import org.caisi.PMmodule.dao.ProgramProviderDAO;
import org.caisi.PMmodule.dao.ProgramTeamDAO;
import org.caisi.PMmodule.model.AccessType;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.model.FunctionalUserType;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramAccess;
import org.caisi.PMmodule.model.ProgramBedLog;
import org.caisi.PMmodule.model.ProgramFunctionalUser;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.ProgramTeam;
import org.caisi.PMmodule.service.ProgramManager;

public class ProgramManagerImpl implements ProgramManager
{
//	private static Log log = LogFactory.getLog(ProgramQueueManagerImpl.class);
	private ProgramDao dao;
	private ProgramProviderDAO programProviderDAO;
	private ProgramFunctionalUserDAO programFunctionalUserDAO;
	private ProgramTeamDAO programTeamDAO;
	private ProgramAccessDAO programAccessDAO;
	private AdmissionDao admissionDAO;
	private ProgramBedLogDAO programBedLogDAO;
	
	public void setProgramDao(ProgramDao dao)
	{
		this.dao = dao;
	}
	
	public void setProgramProviderDAO(ProgramProviderDAO dao)
	{
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
	
	public void setProgramBedLogDAO(ProgramBedLogDAO dao) {
		this.programBedLogDAO = dao;
	}
	
	public Program getProgram(String programId)
	{
		Program program = dao.getProgram(Integer.valueOf(programId));
		if(program == null)
		{
//			log.warn("queueId '" + queueId + "' not found in database.");			
		}
		return program;
	}
	
	public String getProgramName(String programId)
	{
		return dao.getProgramName(Integer.valueOf(programId));
	}

	public Program getProgramFromName(String name)
	{
		return dao.getProgramFromName(name);
	}
	
	public List getAllPrograms()
	{
		return dao.getAllPrograms();
	}
	
	public List getPrograms(String queryStr)
	{
		return dao.getPrograms(queryStr);
	}
	
	public List getProgramsByAgencyId(String agencyId)
	{
		return dao.getProgramsByAgencyId(agencyId);
	}
	
	public List getBedPrograms()
	{
		return dao.getBedPrograms();
	}
	
//###############################################################################

	public List getBedProgramIds(List programIds)
	{
		if(programIds == null  ||  programIds.size() <= 0)
		{
			return null;
		}
		
		List bedProgramIds = new ArrayList();
		
		for(int i=0; i < programIds.size(); i++)
		{
			if( isBedProgram( ((Long)(programIds.get(i))).toString()) )
			{
				bedProgramIds.add((Long)(programIds.get(i)));
			}
		}
		
		if(bedProgramIds != null  ||  bedProgramIds.size() > 0)
		{
			return bedProgramIds;
		}
		return null;
	}
	
//###########################################################################
	
	public List getBedProgramsWithinProgramDomain(List bedProgramIds)
	{
		return dao.getBedProgramsWithinProgramDomain(bedProgramIds);
	}
	
	public List getServicePrograms()
	{
		return dao.getServicePrograms();
	}
//###########################################################################

	public List getServiceProgramIds(List programIds)
	{
		if(programIds == null  ||  programIds.size() <= 0)
		{
			return null;
		}
		
		List serviceProgramIds = new ArrayList();
		
		for(int i=0; i < programIds.size(); i++)
		{
			if( isServiceProgram( ((Long)(programIds.get(i))).toString()) )
			{
				serviceProgramIds.add((Long)(programIds.get(i)));
			}
		}
		if(serviceProgramIds != null  ||  serviceProgramIds.size() > 0)
		{
			return serviceProgramIds;
		}
		return null;
	}

//###############################################################################

	public List getServiceProgramsWithinProgramDomain(List serviceProgramIds)
	{
		return dao.getServiceProgramsWithinProgramDomain(serviceProgramIds);
	}
	

	public boolean isBedProgram(String programId)
	{
		return dao.isBedProgram(Integer.valueOf(programId));
	}
	
	public boolean isServiceProgram(String programId)
	{
		return dao.isServiceProgram(Integer.valueOf(programId));
	}
	
	public int getMaxAllowedNum(String programId)
	{
		return dao.getMaxAllowedNum(Integer.valueOf(programId));
	}
	
	public int getNumOfMembers(String programId)
	{
		return dao.getNumOfMembers(Integer.valueOf(programId));
	}

	public int getNumOfMembersFromAdmission(String programId)
	{
		return dao.getNumOfMembersFromAdmission(programId);
	}
	
	public boolean ensureNumOfMembersCorrect(String programId)
	{
		return dao.ensureNumOfMembersCorrect(programId);
	}
		
	public boolean isMaxAllowedMet(String programId)
	{
		return dao.isMaxAllowedMet(Integer.valueOf(programId));
	}
	
	public boolean incrementNumOfMembers(Program program)
	{
		return dao.incrementNumOfMembers(program);
	}
	
	public boolean decrementNumOfMembers(Program program)
	{
		return dao.decrementNumOfMembers(program);
	}
	
	public boolean decrementNumOfMembers(String programId)
	{
		return dao.decrementNumOfMembers(Integer.valueOf(programId));
	}
/*	
	public String getProgramUpdateSqlStr(Program program)
	{
		return dao.getProgramUpdateSqlStr(program);
	}
*/	
	public void saveProgram(Program program)
	{
		if(program.getHoldingTank()) {
			dao.resetHoldingTank();
		}
		dao.saveProgram(program);
	}

	
	public void updateProgram(Program program)
	{
		dao.updateProgram(program);
	}
	
	public void removeProgram(String programId)
	{
		dao.removeProgram(Integer.valueOf(programId));
	}
	
	//TODO: Implement this method for real
	public Agency getAgencyByProgram(String programId) {
		return new Agency(new Long(0),"Seaton House",true,false);
	}
	

	public List getProgramProviders(String programId) {
		return programProviderDAO.getProgramProviders(Long.valueOf(programId));
	}
	
	public List getProgramProvidersByProvider(String providerNo) {
		return programProviderDAO.getProgramProvidersByProvider(Long.valueOf(providerNo));
	}
	
	
	public ProgramProvider getProgramProvider(String  id) {
		return programProviderDAO.getProgramProvider(Long.valueOf(id));
	}
	
	public ProgramProvider getProgramProvider(String  providerNo, String programId) {
		return programProviderDAO.getProgramProvider(Long.valueOf(providerNo),Long.valueOf(programId));
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
		return programFunctionalUserDAO.getFunctionalUserByUserType(programId,userTypeId);
	}

	public List getProgramTeams(String programId) {
		return programTeamDAO.getProgramTeams(Long.valueOf(programId));
	}
	public ProgramTeam getProgramTeam(String id) {
		return programTeamDAO.getProgramTeam(Long.valueOf(id));
	}
	public void saveProgramTeam(ProgramTeam team) {
		programTeamDAO.saveProgramTeam(team);
	}
	public void deleteProgramTeam(String id) {
		programTeamDAO.deleteProgramTeam(Long.valueOf(id));
	}
	public boolean teamNameExists(Long programId, String teamName) {
		return programTeamDAO.teamNameExists(programId,teamName);
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

	public List getAllProvidersInTeam(Long programId, Long teamId) {
		return this.programProviderDAO.getProgramProvidersInTeam(programId, teamId);
	}
	public List getAllClientsInTeam(Long programId, Long teamId) {
		return this.admissionDAO.getAdmissionsInTeam(programId,teamId);
	}
	public List search(Program criteria) {
		return this.dao.search(criteria);
	}
	public Program getHoldingTankProgram() {
		return this.dao.getHoldingTankProgram();
	}
	public ProgramAccess getProgramAccess(String programId, String accessTypeId) {
		return this.programAccessDAO.getProgramAccess(Long.valueOf(programId),Long.valueOf(accessTypeId));
	}
	
	public void saveBedLog(ProgramBedLog pbl) {
		programBedLogDAO.saveBedLog(pbl);
	}
	
	public ProgramBedLog getBedLog(long id) {
		ProgramBedLog pbl =  programBedLogDAO.getBedLog(new Long(id));
		if(pbl != null) {
			return pbl;
		}
		return new ProgramBedLog();
	}
	public ProgramBedLog getBedLogByProgramId(long programId) {
		ProgramBedLog pbl = programBedLogDAO.getBedLogByProgramId(new Long(programId));
		if(pbl != null) {
			return pbl;
		}
		return new ProgramBedLog();
	}

	public List getBedLogStatuses(String programId) {
		return programBedLogDAO.getBedLogStatuses(Long.valueOf(programId));
	}
	public List getBedLogCheckTimes(String programId) {
		return programBedLogDAO.getBedLogCheckTimes(Long.valueOf(programId));
	}

}
