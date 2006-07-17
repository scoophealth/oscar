package org.caisi.casemgmt.common;

import javax.servlet.jsp.tagext.TagSupport;

import org.caisi.casemgmt.service.CaseManagementManager;

public class SecurLevel extends BasicTag
{
	private static final long serialVersionUID = -4523815337736640975L;

	private String greaterEqual;
	private String providerNo;
	
	public void setGreaterEqual(String greaterEqual)
	{
		this.greaterEqual = greaterEqual;
	}
	public int doStartTag()
	{
		int level;
		try{
			level=new Integer(greaterEqual).intValue();
		}catch (Exception e){
			return SKIP_BODY;
		}
		boolean isGreater=getCaseManagementManager().greaterEqualLevel(level,providerNo);
		if (isGreater) return EVAL_BODY_INCLUDE;
		return SKIP_BODY;
		
	}
	public void setProviderNo(String providerNo)
	{
		this.providerNo = providerNo;
	}

}
