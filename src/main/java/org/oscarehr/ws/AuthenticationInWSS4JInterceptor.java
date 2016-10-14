/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.ws;

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
			super.handleMessage(message);

			// if it gets here that means it succeeded
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromRequest(request);


			OscarLog oscarLog=new OscarLog();
			oscarLog.setProviderNo(loggedInInfo.getLoggedInProviderNo());
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

	@Override
	public void handle(Callback[] callbacks)
	{
		// do nothing
	}
}
