package org.caisi.dao;


import java.util.List;

import org.caisi.model.ProviderDefaultProgram;

public interface ProviderDefaultProgramDao
{
	public List getProgramByProviderNo(String providerNo);
	public void setDefaultProgram(String providerNo,int programId);
	public List getProviderSig(String providerNo);
	public void saveProviderDefaultProgram(ProviderDefaultProgram pdp);
	public void toggleSig(String providerNo);
}
