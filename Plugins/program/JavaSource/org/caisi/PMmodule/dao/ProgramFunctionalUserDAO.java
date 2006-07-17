package org.caisi.PMmodule.dao;

import java.util.List;

import org.caisi.PMmodule.model.FunctionalUserType;
import org.caisi.PMmodule.model.ProgramFunctionalUser;

public interface ProgramFunctionalUserDAO {

	public List getFunctionalUserTypes();
	public FunctionalUserType getFunctionalUserType(Long id);
	public void saveFunctionalUserType(FunctionalUserType fut);
	public void deleteFunctionalUserType(Long id);
	
	public List getFunctionalUsers(Long programId);
	public ProgramFunctionalUser getFunctionalUser(Long id);
	public void saveFunctionalUser(ProgramFunctionalUser pfu);
	public void deleteFunctionalUser(Long id);
	
	public Long getFunctionalUserByUserType(Long programId, Long userTypeId);
}
