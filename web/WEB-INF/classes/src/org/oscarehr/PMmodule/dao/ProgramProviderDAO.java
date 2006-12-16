package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.ProgramProvider;

public interface ProgramProviderDAO {

	public List getProgramProviders(Long programId);

	public List getProgramProvidersByProvider(Long providerNo);

	public ProgramProvider getProgramProvider(Long id);

	public ProgramProvider getProgramProvider(Long providerNo, Long programId);

	public void saveProgramProvider(ProgramProvider pp);

	public void deleteProgramProvider(Long id);

	public List getProgramProvidersInTeam(Integer programId, Integer teamId);

	public List getProgramDomain(Long providerNo);

}
