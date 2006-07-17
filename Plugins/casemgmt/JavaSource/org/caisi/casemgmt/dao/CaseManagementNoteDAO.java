package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.casemgmt.model.CaseManagementNote;

public interface CaseManagementNoteDAO extends DAO {
	public CaseManagementNote getNote(Long id);
	public List getNotesByDemographic(String demographic_no);
	public List getNotesByDemographic(String demographic_no,String[] issues);
	public void saveNote(CaseManagementNote note);
	
}
