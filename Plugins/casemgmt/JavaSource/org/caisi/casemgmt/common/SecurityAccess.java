package org.caisi.casemgmt.common;

public class SecurityAccess extends BasicTag
{

	private String accessName;
	private String accessType;
	private String providerNo;
	private String demoNo;
	private String programId;
	
	public void setAccessName(String accessName)
	{
		this.accessName = accessName;
	}
	public void setProviderNo(String providerNo)
	{
		this.providerNo = providerNo;
	}
	
	public void setDemoNo(String demoNo)
	{
		this.demoNo = demoNo;
	}
	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}
	
	public String getProgramId()
	{
		return programId;
	}
	public void setProgramId(String programId)
	{
		this.programId = programId;
	}
	public int doStartTag()
	{
		if ("".equalsIgnoreCase(programId)||"null".equalsIgnoreCase(programId)) programId="0";
		boolean hasAccess=getCaseManagementManager().hasAccessRight(accessName,accessType,providerNo,demoNo,programId);
		if (hasAccess) return EVAL_BODY_INCLUDE;
		else return SKIP_BODY;
	}
}
