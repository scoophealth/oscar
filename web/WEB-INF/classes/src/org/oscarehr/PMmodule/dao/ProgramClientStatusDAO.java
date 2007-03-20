package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.ProgramClientStatus;

public interface ProgramClientStatusDAO {

	public List<ProgramClientStatus> getProgramClientStatuses(Integer programId);	
	public void saveProgramClientStatus(ProgramClientStatus status);
	public void deleteProgramClientStatus(String id);
	public boolean clientStatusNameExists(Integer programId, String statusName);
	public List getAllClientsInStatus(Integer programId, Integer statusId);
	public ProgramClientStatus getProgramClientStatus(String statusId);	
	
}
