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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;


public abstract class AbstractWs
{
	protected static final int GZIP_THRESHOLD = 0;

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
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		return(loggedInInfo.getLoggedInSecurity());
	}
	
	protected Provider getLoggedInProvider()
	{
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		return(loggedInInfo.getLoggedInProvider());
	}
	
	protected LoggedInInfo getLoggedInInfo()
	{
		return(LoggedInInfo.getLoggedInInfoFromRequest(getHttpServletRequest()));
	}
}
