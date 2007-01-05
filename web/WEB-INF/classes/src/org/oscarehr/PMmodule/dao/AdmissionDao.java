package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;

public interface AdmissionDao {
	
	public Admission getAdmission(Long programId, Integer demographicNo);

	public Admission getCurrentAdmission(Long programId, Integer demographicNo);

	public List getAdmissions();

	public List getAdmissions(Integer demographicNo);

	public List getCurrentAdmissions(Integer demographicNo);

	public Admission getCurrentBedProgramAdmission(ProgramDao programDAO, Integer demographicNo);

	public List getCurrentServiceProgramAdmission(ProgramDao programDAO, Integer demographicNo);

	public Admission getCurrentCommunityProgramAdmission(ProgramDao programDAO, Integer demographicNo);

	public List getCurrentAdmissionsByProgramId(Long programId);

	public Admission getAdmission(Long id);

	public void saveAdmission(Admission admission);

	public List getAdmissionsInTeam(Integer programId, Integer teamId);

	public Admission getTemporaryAdmission(Integer demographicNo);

	public List search(AdmissionSearchBean searchBean);
	
	public List getClientIdByProgramDate(int programId, Date dt);
	
}