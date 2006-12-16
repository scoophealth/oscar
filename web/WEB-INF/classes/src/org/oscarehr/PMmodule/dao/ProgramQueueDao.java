

package org.oscarehr.PMmodule.dao;


import java.util.List;

import org.oscarehr.PMmodule.model.ProgramQueue;


public interface ProgramQueueDao {
	
	public ProgramQueue getProgramQueue(Long queueId);

	public List getProgramQueuesByProgramId(String programId);
			
	public void saveProgramQueue(ProgramQueue programQueue);

	public ProgramQueue getQueue(String programId, String clientId);
	
	public List getActiveProgramQueuesByProgramId(String programId);
	
	public ProgramQueue getActiveProgramQueue(Long programId, Integer demographicNo);
}

