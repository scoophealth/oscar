package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.ProgramQueue;


public interface ProgramQueueManager 
{
	public ProgramQueue getProgramQueue(String queueId);
		
	public List getProgramQueuesByProgramId(String programId);
	
	public List getActiveProgramQueuesByProgramId(String programId);

	public ProgramQueue getActiveProgramQueue(String programId, String demographicNo);
		
	public void saveProgramQueue(ProgramQueue programQueue);
	
	public void rejectQueue(String programId, String clientId,String notes);
}
