

package org.caisi.PMmodule.dao;


import java.util.List;

import org.caisi.PMmodule.model.Program;

//###############################################################################

public interface ProgramDao
{
	public Program getProgram(Integer programId);
	
	public String getProgramName(Integer programId);

	public Program getProgramFromName(String name);
	
	public List getAllPrograms();
	
	public List getPrograms(String queryStr);

	public List getProgramsByAgencyId(String agencyId);
	
	public List getBedPrograms();

	public List getBedProgramsWithinProgramDomain(List bedProgramIds);
	
	public List getServiceProgramsWithinProgramDomain(List serviceProgramIds);

	public List getServicePrograms();
	
	public boolean isBedProgram(Integer programId);
	
	public boolean isServiceProgram(Integer programId);
	
	public int getMaxAllowedNum(Integer programId);
	
	public int getNumOfMembers(Integer programId);
	
	public int getNumOfMembersFromAdmission(String programId);
	
	public boolean ensureNumOfMembersCorrect(String programId);
	
	public boolean isMaxAllowedMet(Integer programId);
	
	public boolean incrementNumOfMembers(Program program);
	
	public boolean decrementNumOfMembers(Program program);
	
	public boolean decrementNumOfMembers(Integer programId);
	
//	public String getProgramUpdateSqlStr(Program program);
	
	public void saveProgram(Program program);

	public void updateProgram(Program program);
	
	public void removeProgram(Integer programId);

	public List search(Program criteria);
	
	public void resetHoldingTank();
	
	public Program getHoldingTankProgram();
}

