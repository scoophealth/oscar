package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.PMmodule.model.ProgramQueue;


public interface ProgramQueueManager 
{
	public ProgramQueue getProgramQueue(String queueId);

	public List getAllProgramQueues();
	
	public List getBedProgramQueues(ProgramManager programMgr, String programId);
	
	public List getServiceProgramQueues(ProgramManager programMgr, String programId);
	
	public List getProgramQueuesByClient(String demographicNo);
	
	public List getProgramQueuesByProgramId(String programId);
	
	public List getActiveProgramQueuesByProgramId(String programId);

	public ProgramQueue getActiveProgramQueue(String programId, String demographicNo);
	
	public List getClientIdsByProgramIds(List programIds);
	
	public int getNumOfProgramQueuesByProgramId(String programId);

	public boolean isClientAlreadyReferredToProgram(String demographicNo, String ProgramId);

	public boolean isClientReferredByProvider(String demographicNo, String providerNo);

	public boolean isProgramIdInProgramQueue(String programId);
	
	public void saveProgramQueue(ProgramQueue programQueue);

	public void updateProgramQueue(ProgramQueue programQueue);

	public void removeProgramQueue(String queueId);
	
	public void rejectQueue(String programId, String clientId);
}
