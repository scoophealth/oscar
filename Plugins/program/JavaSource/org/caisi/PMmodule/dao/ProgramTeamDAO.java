package org.caisi.PMmodule.dao;

import java.util.List;

import org.caisi.PMmodule.model.ProgramTeam;

public interface ProgramTeamDAO {
	public List getProgramTeams(Long programId);
	public ProgramTeam getProgramTeam(Long id);
	public void saveProgramTeam(ProgramTeam team);
	public void deleteProgramTeam(Long id);

	public boolean teamNameExists(Long programId, String teamName);

}
