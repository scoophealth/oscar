package org.oscarehr.PMmodule.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.ProgramQueueDao;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.service.ProgramQueueManager;

public class ProgramQueueManagerImpl implements ProgramQueueManager
{
	private static Log log = LogFactory.getLog(ProgramQueueManagerImpl.class);
	private ProgramQueueDao dao;
	private ClientReferralDAO referralDAO;
	
	
	public void setProgramQueueDao(ProgramQueueDao dao)	{
		this.dao = dao;
	}
	
	public void setClientReferralDAO(ClientReferralDAO dao)	{
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
	
	public List getProgramQueuesByProgramId(String programId) {
		return dao.getProgramQueuesByProgramId(programId);
	}

	public void saveProgramQueue(ProgramQueue programQueue)	{
		dao.saveProgramQueue(programQueue);
	}
	
	public List getActiveProgramQueuesByProgramId(String programId) {
		return dao.getActiveProgramQueuesByProgramId(programId);
	}
	
	public ProgramQueue getActiveProgramQueue(String programId, String demographicNo) {
		return dao.getActiveProgramQueue(Long.valueOf(programId),Integer.valueOf(demographicNo));
	}
	
	public void rejectQueue(String programId, String clientId,String notes) {
		ProgramQueue queue = getActiveProgramQueue(programId,clientId);
		if(queue==null) {
			return;
		}
		ClientReferral referral = this.referralDAO.getClientReferral(queue.getReferralId());
		if(referral != null) {
			referral.setStatus("rejected");
			referral.setCompletionDate(new Date());
			referral.setCompletionNotes(notes);
			this.referralDAO.saveClientReferral(referral);
		}
		queue.setStatus("rejected");
		this.saveProgramQueue(queue);
	}
}
