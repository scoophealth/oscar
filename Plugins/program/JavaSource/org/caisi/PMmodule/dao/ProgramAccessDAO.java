package org.caisi.PMmodule.dao;

import java.util.List;

import org.caisi.PMmodule.model.AccessType;
import org.caisi.PMmodule.model.ProgramAccess;

public interface ProgramAccessDAO {
	public List getProgramAccesses(Long programId);
	public ProgramAccess getProgramAccess(Long id);
	public ProgramAccess getProgramAccess(Long programId, Long accessTypeId);
	public void saveProgramAccess(ProgramAccess pa);
	public void deleteProgramAccess(Long id);
	
	public List getAccessTypes();
	public AccessType getAccessType(Long id);
}
