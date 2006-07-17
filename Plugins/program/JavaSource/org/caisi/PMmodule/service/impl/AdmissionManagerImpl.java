package org.caisi.PMmodule.service.impl;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.AdmissionDao;
import org.caisi.PMmodule.dao.ClientReferralDAO;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.dao.ProgramQueueDao;
import org.caisi.PMmodule.exception.ProgramFullException;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.ClientReferral;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.ProgramManager;

public class AdmissionManagerImpl implements AdmissionManager
{
	private static Log log = LogFactory.getLog(AdmissionManagerImpl.class);

	private AdmissionDao dao;
	private ProgramQueueDao programQueueDao;
	private ClientReferralDAO clientReferralDAO;
	
	
	public void setAdmissionDao(AdmissionDao dao)
	{
		this.dao = dao;
	}
	
	public void setProgramQueueDao(ProgramQueueDao dao) {
		this.programQueueDao = dao;
	}
	
	public void setClientReferralDAO(ClientReferralDAO dao) {
		this.clientReferralDAO = dao;
	}
	
	public Admission getAdmission(String programId, String demographicNo)
	{
		return dao.getAdmission(programId, demographicNo);
	}
	public Admission getCurrentAdmission(String programId, String demographicNo)
	{
		return dao.getCurrentAdmission(programId, demographicNo);
	}
	public List getAdmissions()
	{
		return dao.getAdmissions();
	}

	public List getAdmissions(String demographicNo)
	{
		return dao.getAdmissions(demographicNo);
	}
	public List getBedProgramAdmissionHistory(ProgramManager programMgr, String demographicNo)
	{
		return dao.getBedProgramAdmissionHistory(programMgr, demographicNo);
	}

	public List getServiceProgramAdmissionHistory(ProgramManager programMgr, String demographicNo)
	{
		return dao.getServiceProgramAdmissionHistory(programMgr, demographicNo);
		
	}
	public List getAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay)
	{
		return dao.getAdmissions(demographicNo, rowCountPass, totalRowDisplay);
	}

	public List getCurrentAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay)
	{
		return dao.getCurrentAdmissions(demographicNo, rowCountPass, totalRowDisplay);
	}

	public List getCurrentAdmissions(String demographicNo)
	{
		return dao.getCurrentAdmissions(demographicNo);
	}

	public Admission getCurrentBedProgramAdmission(ProgramManager programMgr, String demographicNo)
	{
		return dao.getCurrentBedProgramAdmission(programMgr, demographicNo);
	}

	public List getCurrAdmissionRecordsOfABedProgram(ProgramManager programMgr, String bedProgramId)
	{
		return dao.getCurrAdmissionRecordsOfABedProgram(programMgr, bedProgramId);
		
	}
	public List getCurrAdmissionRecordsOfAServiceProgram(ProgramManager programMgr, String serviceProgramId)
	{
		return dao.getCurrAdmissionRecordsOfAServiceProgram(programMgr, serviceProgramId);
	}

	public List getCurrentServiceProgramAdmission(ProgramManager programMgr, String demographicNo)
	{
		return dao.getCurrentServiceProgramAdmission(programMgr, demographicNo);
	}

	public List getClientIdsFromProgramIds(String providerNo, List selectedProgramIds)
	{
		return dao.getClientIdsFromProgramIds(providerNo, selectedProgramIds);
	}

	public List getClientIdsFromProgramIdsNotEqual(Database_Service databaseService, DataSource dataSource, String providerNo, List selectedProgramIds)
	{
		return dao.getClientIdsFromProgramIdsNotEqual(databaseService, dataSource, providerNo, selectedProgramIds);
	}
	
	public boolean isProgramIdInAdmission(String programId)
	{
		return dao.isProgramIdInAdmission(programId);
	}
	
	public boolean addAdmission(Admission admission, ProgramManager programMgr, Program program)
	{
		return dao.addAdmission(admission, programMgr, program);
	}

	public boolean dischargeAdmission(ProgramManager programMgr, String programId, Admission admission, String amId)
	{
		return dao.dischargeAdmission(programMgr, programId, admission, amId);
	}


	public boolean updateAdmission(Admission admission)
	{
		return dao.updateAdmission(admission);
	}

	public boolean removeAdmission(String amId)
	{
		return dao.removeAdmission(amId);
	}

	public List getCurrentAdmissionsByProgramId(String programId) {
		return dao.getCurrentAdmissionsByProgramId(Long.valueOf(programId));
	}
	
	public List getAllAdmissionsByProgramId(String programId) {
		return dao.getAllAdmissionsByProgramId(Long.valueOf(programId));
	}
	
	public Admission getAdmission(Long id) {
		return dao.getAdmission(id);
	}
	
	public void saveAdmission(Admission admission) {
		dao.saveAdmission(admission);
	}
	
	public void processAdmission(String demographicNo, String providerNo, Program program, ProgramManager programManager, String dischargeNotes, String admissionNotes) throws ProgramFullException {
		
		//see if there's room first
		if(program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}
		
		if(program.getType().equalsIgnoreCase("bed")) {
			//do a discharge first
			Admission fullAdmission = this.getCurrentBedProgramAdmission(programManager,demographicNo);
			if(fullAdmission == null) {
				log.warn("couldn't discharge..no existing bed program");
			} else {
				fullAdmission.setDischargeDate(new Date());
				fullAdmission.setDischargeNotes(dischargeNotes);
				fullAdmission.setAdmissionStatus("discharged");
				saveAdmission(fullAdmission);
			}						
		}
		
		Admission newAdmission = new Admission();
		newAdmission.setAdmissionDate(new Date());
		newAdmission.setAdmissionNotes(admissionNotes);
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(Long.valueOf(demographicNo));
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		
		saveAdmission(newAdmission);
		
		ProgramQueue pq = programQueueDao.getQueue(String.valueOf(program.getId()),demographicNo);
		if(pq != null) {
			pq.setStatus("admitted");
			programQueueDao.saveProgramQueue(pq);
			
			//is there a referral
			if(pq.getReferralId() != null && pq.getReferralId().longValue() > 0) {
				ClientReferral referral = clientReferralDAO.getClientReferral(pq.getReferralId());
				referral.setStatus("complete");
				clientReferralDAO.saveClientReferral(referral);
			}
		}
	}
	
	public void processInitialAdmission(String demographicNo, String providerNo, Program program, String admissionNotes) throws ProgramFullException {
		//see if there's room first
		if(program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}
		
		Admission newAdmission = new Admission();
		newAdmission.setAdmissionDate(new Date());
		newAdmission.setAdmissionNotes(admissionNotes);
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(Long.valueOf(demographicNo));
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		
		saveAdmission(newAdmission);
	}
}
