package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;

public interface ProgramManager 
{
	public Program getProgram(String programId);
	
	public Program getProgram(Integer programId);
	
	public Program getProgram(Long programId);
	
	public Program getProgram(Long agencyId, String programId);
	
	public Program getProgram(Long agencyId, Integer programId);
	
	public String getProgramName(String programId);
	
	public List getAllPrograms();
	
	public List getProgramsByAgencyId(String agencyId);
	
	public Program[] getBedPrograms();
		
	public List getServicePrograms();
		
	public boolean isBedProgram(String programId);
	
	public boolean isServiceProgram(String programId);
	
	public boolean isCommunityProgram(String programId);	
	
	public void saveProgram(Program program);
	
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
	public boolean teamNameExists(Integer programId, String teamName);
	public List getAllProvidersInTeam(Integer programId, Integer teamId);
	public List getAllClientsInTeam(Integer programId, Integer teamId);
		
	/* access */
	public List getProgramAccesses(String programId);
	public ProgramAccess getProgramAccess(String id);
	public ProgramAccess getProgramAccess(String programId, String accessTypeId);
	public void saveProgramAccess(ProgramAccess pa);
	public void deleteProgramAccess(String id);
	public List getAccessTypes();
	public AccessType getAccessType(Long id);
	public List getDefaultRoleAccesses();
	public DefaultRoleAccess getDefaultRoleAccess(String id);
	public void saveDefaultRoleAccess(DefaultRoleAccess dra);
	public void deleteDefaultRoleAccess(String id);
	public DefaultRoleAccess findDefaultRoleAccess(long roleId, long accessTypeId);
	
	public List search(Program criteria);
	
	public Program getHoldingTankProgram();
	
	public List getProgramDomain(String providerNo);
	
	public List getCommunityPrograms();
}
