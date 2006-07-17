package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.PMmodule.model.AccessType;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramAccess;
import org.caisi.PMmodule.model.ProgramBedLog;
import org.caisi.PMmodule.model.ProgramFunctionalUser;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.ProgramTeam;


public interface ProgramManager 
{
	public Program getProgram(String programId);
	
	public String getProgramName(String programId);

	public Program getProgramFromName(String name);
	
	public List getAllPrograms();
	
	public List getPrograms(String queryStr);

	public List getProgramsByAgencyId(String agencyId);
	
	public List getBedPrograms();

	public List getBedProgramIds(List programIds);
	
	public List getBedProgramsWithinProgramDomain(List bedProgramIds);
	
	public List getServicePrograms();
	
	public List getServiceProgramIds(List programIds);
	
	public List getServiceProgramsWithinProgramDomain(List serviceProgramIds);

	public boolean isBedProgram(String programId);
	
	public boolean isServiceProgram(String programId);
	
	public int getMaxAllowedNum(String programId);
	
	public int getNumOfMembers(String programId);
	
	public int getNumOfMembersFromAdmission(String programId);
	
	public boolean ensureNumOfMembersCorrect(String programId);
	
	public boolean isMaxAllowedMet(String programId);
	
	public boolean incrementNumOfMembers(Program program);
	
	public boolean decrementNumOfMembers(Program program);
	
	public boolean decrementNumOfMembers(String programId);
	
	public void saveProgram(Program program);
	
	public void updateProgram(Program program);

	public void removeProgram(String programId);
	
	/* added by Marc for testing */
	public Agency getAgencyByProgram(String programId);
	
	/* staff */
	public List getProgramProviders(String programId);
	
	public List getProgramProvidersByProvider(String providerNo);
	
	public ProgramProvider getProgramProvider(String  id);
	
	public ProgramProvider getProgramProvider(String  providerNo, String programId);
	
	public void saveProgramProvider(ProgramProvider pp);
	
	public void deleteProgramProvider(String id);
	
	/* functional users */

	public List getFunctionalUserTypes();
/*
	public FunctionalUserType getFunctionalUserType(String id);
	public void saveFunctionalUserType(FunctionalUserType fut);
	public void deleteFunctionalUserType(String id);
*/
	public List getFunctionalUsers(String programId);
	public ProgramFunctionalUser getFunctionalUser(String id);
	public void saveFunctionalUser(ProgramFunctionalUser pfu);
	public void deleteFunctionalUser(String id);
	public Long getFunctionalUserByUserType(Long programId, Long userTypeId);

	/* teams */
	
	public List getProgramTeams(String programId);
	public ProgramTeam getProgramTeam(String id);
	public void saveProgramTeam(ProgramTeam team);
	public void deleteProgramTeam(String id);
	public boolean teamNameExists(Long programId, String teamName);
	public List getAllProvidersInTeam(Long programId, Long teamId);
	public List getAllClientsInTeam(Long programId, Long teamId);
		
	/* access */
	public List getProgramAccesses(String programId);
	public ProgramAccess getProgramAccess(String id);
	public ProgramAccess getProgramAccess(String programId, String accessTypeId);
	public void saveProgramAccess(ProgramAccess pa);
	public void deleteProgramAccess(String id);
	public List getAccessTypes();
	public AccessType getAccessType(Long id);

	public List search(Program criteria);
	
	public Program getHoldingTankProgram();
	
	/* bed log */
	public void saveBedLog(ProgramBedLog pbl);
	public ProgramBedLog getBedLog(long id);
	public ProgramBedLog getBedLogByProgramId(long programId);
	public List getBedLogStatuses(String programId);
	public List getBedLogCheckTimes(String programId);

}
