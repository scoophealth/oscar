package org.oscarehr.casemgmt.dao;

import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;

public interface CaseManagementNoteDAO extends DAO {
	public CaseManagementNote getNote(Long id);
	public List getNotesByDemographic(String demographic_no);
	public List getNotesByDemographic(String demographic_no,String[] issues);
	public void saveNote(CaseManagementNote note);
	public List search(CaseManagementSearchBean searchBean);
	public List getAllNoteIds();
}
