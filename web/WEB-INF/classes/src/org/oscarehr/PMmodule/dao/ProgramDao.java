package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Program;

public interface ProgramDao {
	
	public boolean isBedProgram(Integer programId);
	
	public boolean isServiceProgram(Integer programId);
	
	public boolean isCommunityProgram(Integer programId);
	
	public Program getProgram(Integer programId);

	public String getProgramName(Integer programId);

	public List getAllPrograms();

	public List getProgramsByAgencyId(String agencyId);

	public Program[] getBedPrograms();

	public List getServicePrograms();

	public Program[] getCommunityPrograms();
	
	public void saveProgram(Program program);

	public void removeProgram(Integer programId);

	public List search(Program criteria);

	public void resetHoldingTank();

	public Program getHoldingTankProgram();

	public boolean programExists(Integer programId);
	
}
