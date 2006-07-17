package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.PMmodule.model.Agency;

public interface IntegratorManager {
	public boolean isEnabled();
	public boolean isRegistered();
	public void refresh();
	public String register(Agency agencyInfo, String key);
	public List getAgencies();
	public Agency getAgency(String id);
}
