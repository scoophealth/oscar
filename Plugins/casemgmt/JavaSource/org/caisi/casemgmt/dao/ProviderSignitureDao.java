package org.caisi.casemgmt.dao;

public interface ProviderSignitureDao
{
	public boolean isOnSig(String providerNo);
	public String getProviderSig(String providerNo);
}
