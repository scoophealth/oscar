package org.oscarehr.casemgmt.web;

public class ProviderAccessRight
{
	private String accessName;
	private boolean isAccess;
	
	public String getAccessName()
	{
		return accessName;
	}
	public void setAccessName(String accessName)
	{
		this.accessName = accessName;
	}
	public boolean isAccess()
	{
		return isAccess;
	}
	public void setAccess(boolean isAccess)
	{
		this.isAccess = isAccess;
	}

}
