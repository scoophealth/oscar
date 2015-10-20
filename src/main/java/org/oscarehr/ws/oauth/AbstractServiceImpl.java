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

/*
 * Written by Brandon Aubie <brandon@aubie.ca>
 */

package org.oscarehr.ws.oauth;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.rs.security.oauth.data.OAuthContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;

/**
 * Base class for OAuth web services
 */
public abstract class AbstractServiceImpl {

	protected HttpServletRequest getHttpServletRequest()
	{
		Message message = PhaseInterceptorChain.getCurrentMessage();
	    HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
	    return(request);
	}

	protected OAuthContext getOAuthContext() {
		Message m = PhaseInterceptorChain.getCurrentMessage();
		OAuthContext oac = m.getContent(OAuthContext.class);
    	return oac;
	}

	protected Provider getCurrentProvider() {
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		return (loggedInInfo.getLoggedInProvider());
	}

	protected LoggedInInfo getLoggedInInfo() {

        /* The OAuthInterceptor already put its loggedInInfo into the request. */
        HttpServletRequest request = getHttpServletRequest();
	    LoggedInInfo info = LoggedInInfo.getLoggedInInfoFromRequest(request);
		if (info == null) {
            throw new IllegalStateException("Authentication info is not available.");

		}
		return info;
	}

}
