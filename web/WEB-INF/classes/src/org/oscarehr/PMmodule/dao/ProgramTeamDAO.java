package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.ProgramTeam;

public interface ProgramTeamDAO {
	
	public boolean teamExists(Integer teamId);

	public boolean teamNameExists(Integer programId, String teamName);
	
	public ProgramTeam getProgramTeam(Integer id);

	public List getProgramTeams(Integer programId);

	public void saveProgramTeam(ProgramTeam team);

	public void deleteProgramTeam(Integer id);

}
