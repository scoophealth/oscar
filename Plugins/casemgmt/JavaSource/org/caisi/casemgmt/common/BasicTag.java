package org.caisi.casemgmt.common;

import javax.servlet.jsp.tagext.TagSupport;

import org.caisi.casemgmt.service.CaseManagementManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BasicTag extends TagSupport
{
	public ApplicationContext getAppContext()
    {
		ApplicationContext cont=WebApplicationContextUtils.getWebApplicationContext(
        		pageContext.getServletContext());
         return cont;
    }

	public CaseManagementManager getCaseManagementManager() {
		
		CaseManagementManager bpm = (CaseManagementManager) getAppContext()
				.getBean("CaseManagementManager");
		return bpm;
	}
}
