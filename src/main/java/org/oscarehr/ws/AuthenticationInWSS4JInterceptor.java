package org.oscarehr.ws;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;

/**
 * As of WSS 1.6 we no longer need InInterceptors for authentication, that's now moved to the Validator classes.
 * We still want this interceptor here though as it's the only way I currently know of to make excludes for a global
 * security filter.
 */
public class AuthenticationInWSS4JInterceptor extends WSS4JInInterceptor implements CallbackHandler
{
	private static final Logger logger = MiscUtils.getLogger();

	private ArrayList<String> excludes = null;

	public AuthenticationInWSS4JInterceptor()
	{
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		properties.put(WSHandlerConstants.PW_CALLBACK_REF, this);

		setProperties(properties);
	}

	@Override
	public void handleMessage(SoapMessage message)
	{
		HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
		if (request==null) return; // it's an outgoing request
		String ip = request.getRemoteAddr();

		try
		{
			// if excluded from authentication
			String basePath = (String)message.get(SoapMessage.BASE_PATH);
			if (isExcluded(basePath)) return;

			super.handleMessage(message);

			// if it gets here that means it succeeded
			LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
			OscarLog oscarLog=new OscarLog();
			oscarLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
			oscarLog.setAction("WS_LOGIN_SUCCESS");
			oscarLog.setIp(ip);
			LogAction.addLogSynchronous(oscarLog);
		}
		catch (SoapFault e)
		{
			logger.debug("exception thrown", e);
			
			// this means wrong user/password
			OscarLog oscarLog=new OscarLog();
			oscarLog.setAction("WS_LOGIN_FAILURE");
			oscarLog.setIp(ip);
			LogAction.addLogSynchronous(oscarLog);

			throw(e);
		}
	}

	public void setExcludes(ArrayList<String> excludes)
	{
		this.excludes = excludes;
	}

	private boolean isExcluded(String s)
	{
		// this means it's a response to my request - still registers as in bound even though it's just an in bound response.  
		if (s == null) return(true);

		for (String x : excludes)
		{
			if (s.endsWith(x)) return(true);
		}

		return(false);
	}

	@Override
	public void handle(Callback[] callbacks)
	{
		// do nothing
	}
}
