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
	
	public List getAllActivePrograms();
	
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
	
	public void deleteProgramProviderByProgramId(Long programId);
	
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
	
	public Program[] getCommunityPrograms();
	public List getProgramBeans(String providerNo);
}
