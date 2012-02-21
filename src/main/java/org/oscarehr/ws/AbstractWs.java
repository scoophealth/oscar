package org.oscarehr.ws;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;

import com.quatro.model.security.Security;

public class AbstractWs
{
	@Resource
    protected WebServiceContext context;
	
	protected HttpServletRequest getHttpServletRequest()
	{
		MessageContext messageContext=context.getMessageContext();
		HttpServletRequest request=(HttpServletRequest)messageContext.get(MessageContext.SERVLET_REQUEST);
		return(request);
	}
	
	protected Security getLoggedInSecurity()
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		return(loggedInInfo.loggedInSecurity);
	}
	
	protected Provider getLoggedInProvider()
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		return(loggedInInfo.loggedInProvider);
	}
}
