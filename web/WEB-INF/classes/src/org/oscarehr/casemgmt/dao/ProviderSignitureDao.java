package org.oscarehr.casemgmt.dao;

public interface ProviderSignitureDao
{
	public boolean isOnSig(String providerNo);
	public String getProviderSig(String providerNo);
}
