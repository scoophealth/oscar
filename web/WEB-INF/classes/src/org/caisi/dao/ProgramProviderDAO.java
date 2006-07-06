package org.caisi.dao;

import java.util.List;

import org.caisi.model.ProgramProvider;

public interface ProgramProviderDAO {
	
	public List getProgramProviders(Long programId);
	
	public List getProgramProvidersByProvider(Long providerNo);
	
	public ProgramProvider getProgramProvider(Long id);
	
	public ProgramProvider getProgramProvider(Long providerNo,Long programId);
	
	public void saveProgramProvider(ProgramProvider pp);
	
	public void deleteProgramProvider(Long id);
	
	public List getProgramProvidersInTeam(Long programId, Long teamId);
	
	public List getProgramDomain(Long providerNo);

}
