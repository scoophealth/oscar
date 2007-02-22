package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.model.IntakeInstance;

public interface GenericIntakeManager {
	
	public IntakeInstance createQuickIntake(String providerNo);

	public IntakeInstance createIndepthIntake(String providerNo);

	public IntakeInstance createProgramIntake(Integer programId, String providerNo);
	
	public IntakeInstance copyQuickIntake(Integer clientId, String staffId);

	public IntakeInstance copyIndepthIntake(Integer clientId, String staffId);

	public IntakeInstance copyProgramIntake(Integer clientId, Integer programId, String staffId);
	
	public Integer saveInstance(IntakeInstance instance);

}
