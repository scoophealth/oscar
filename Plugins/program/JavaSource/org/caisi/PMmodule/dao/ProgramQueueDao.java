

package org.caisi.PMmodule.dao;


import java.util.List;

import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.service.ProgramManager;

//###############################################################################

public interface ProgramQueueDao
{
	public ProgramQueue getProgramQueue(Long queueId);

	public List getAllProgramQueues();

	public List getBedProgramQueues(ProgramManager programMgr, String programId);

	public List getServiceProgramQueues(ProgramManager programMgr, String programId);
	
	public List getProgramQueuesByClient(String demographicNo);
	
	public List getProgramQueuesByProgramId(String programId);
	
	public List getClientIdsByProgramIds(List programIds);
	
	public int getNumOfProgramQueuesByProgramId(String programId);
	
	public boolean isClientAlreadyReferredToProgram(String demographicNo, String programId);

	public boolean isClientReferredByProvider(String demographicNo, String providerNo);
	
	public boolean isProgramIdInProgramQueue(String programId);
	
	public void saveProgramQueue(ProgramQueue programQueue);

	public void updateProgramQueue(ProgramQueue programQueue);

	public void removeProgramQueue(Long queueId);

	public ProgramQueue getQueue(String programId, String clientId);
	
	public List getActiveProgramQueuesByProgramId(String programId);
	
	public ProgramQueue getActiveProgramQueue(Long programId, String demographicNo);
	
}

