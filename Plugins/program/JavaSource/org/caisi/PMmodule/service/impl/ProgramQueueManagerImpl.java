package org.caisi.PMmodule.service.impl;

import java.util.List;

import org.caisi.PMmodule.dao.ClientReferralDAO;
import org.caisi.PMmodule.dao.ProgramQueueDao;
import org.caisi.PMmodule.model.ClientReferral;
import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProgramQueueManager;

public class ProgramQueueManagerImpl implements ProgramQueueManager
{
//	private static Log log = LogFactory.getLog(ProgramQueueManagerImpl.class);
	private ProgramQueueDao dao;
	private ClientReferralDAO referralDAO;
	
	
	public void setProgramQueueDao(ProgramQueueDao dao)
	{
		this.dao = dao;
	}
	
	public void setClientReferralDAO(ClientReferralDAO dao)
	{
		this.referralDAO = dao;
	}
	
	public ProgramQueue getProgramQueue(String queueId)
	{
		ProgramQueue pq = dao.getProgramQueue(Long.valueOf(queueId));
		if(pq == null)
		{
//			log.warn("queueId '" + queueId + "' not found in database.");			
		}
		return pq;
	}

	public List getAllProgramQueues()
	{
		return dao.getAllProgramQueues();
	}

	public List getBedProgramQueues(ProgramManager programMgr, String programId)
	{
		return dao.getBedProgramQueues(programMgr, programId);
	}

	public List getServiceProgramQueues(ProgramManager programMgr, String programId)
	{
		return dao.getServiceProgramQueues(programMgr, programId);
	}
	
	public List getProgramQueuesByClient(String demographicNo)
	{
		return dao.getProgramQueuesByClient(demographicNo);
	}
	
	public List getProgramQueuesByProgramId(String programId)
	{
		return dao.getProgramQueuesByProgramId(programId);
	}

	public List getClientIdsByProgramIds(List programIds)
	{
		return dao.getClientIdsByProgramIds(programIds); 	
	}
	
	public int getNumOfProgramQueuesByProgramId(String programId)
	{
		return dao.getNumOfProgramQueuesByProgramId(programId);
	}

	public boolean isClientAlreadyReferredToProgram(String demographicNo, String programId)
	{
		return dao.isClientAlreadyReferredToProgram(demographicNo, programId);
	}

	public boolean isClientReferredByProvider(String demographicNo, String providerNo)
	{
		return dao.isClientReferredByProvider(demographicNo, providerNo);
	}
	
	public boolean isProgramIdInProgramQueue(String programId)
	{
		return dao.isProgramIdInProgramQueue(programId);
	}
	
	public void saveProgramQueue(ProgramQueue programQueue)
	{
		dao.saveProgramQueue(programQueue);
	}
	
	public void updateProgramQueue(ProgramQueue programQueue)
	{
		dao.updateProgramQueue(programQueue);
	}

	public void removeProgramQueue(String queueId)
	{
		dao.removeProgramQueue(Long.valueOf(queueId));
	}

	public List getActiveProgramQueuesByProgramId(String programId) {
		return dao.getActiveProgramQueuesByProgramId(programId);
	}
	
	public ProgramQueue getActiveProgramQueue(String programId, String demographicNo) {
		return dao.getActiveProgramQueue(Long.valueOf(programId),demographicNo);
	}
	
	public void rejectQueue(String programId, String clientId) {
		ProgramQueue queue = getActiveProgramQueue(programId,clientId);
		if(queue==null) {
			return;
		}
		ClientReferral referral = this.referralDAO.getClientReferral(queue.getReferralId());
		if(referral != null) {
			referral.setStatus("rejected");
			this.referralDAO.saveClientReferral(referral);
		}
		queue.setStatus("rejected");
		this.saveProgramQueue(queue);
	}
}
