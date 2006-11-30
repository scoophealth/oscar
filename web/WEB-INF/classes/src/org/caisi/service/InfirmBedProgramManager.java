package org.caisi.service;

import java.util.Date;
import java.util.List;

import org.caisi.model.Program;

public interface InfirmBedProgramManager {
	public List getPrgramNameID();
	public List getPrgramName();
	public List getProgramBeans();
	public List getProgramBeans(String providerNo);
	public List getDemographicByBedProgramIdBeans(int programId,Date dt);
	public int getDefaultProgramId();
	public int getDefaultProgramId(String providerNo);
	public void setDefaultProgramId(String providerNo, int programId);
	public Boolean getProviderSig(String demoNo);
	public void toggleSig(String demoNo);
	public String[] getProgramInformation(int programId);
}
