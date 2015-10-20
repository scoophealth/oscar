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
package org.oscarehr.ws.rest.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.LoggedInInfo;

import oscar.log.LogAction;

public class AuthenticationInInterceptor extends AbstractPhaseInterceptor<Message> {

	public AuthenticationInInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	private LoggedInInfo getLoggedInInfo(Message message) {
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
               return LoggedInInfo.getLoggedInInfoFromSession(request);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		// allows WADL requests for unauthenticated users
		String messageQueryString = String.valueOf(message.get(Message.QUERY_STRING));
		boolean isServiceRequest = "_wadl".equalsIgnoreCase(messageQueryString);
		if (isServiceRequest) {
			return;
		}

		LoggedInInfo info = getLoggedInInfo(message);
		boolean isAuthenticated = info != null && (info.getLoggedInProvider() != null || info.getLoggedInSecurity() != null);
		if (isAuthenticated) {
			return;
		}

		logAccessError(message);

		ResponseBuilder builder = Response.status(Status.UNAUTHORIZED);
		builder.type(MediaType.TEXT_XML);
		builder.entity("<error>Not authorized</error>");
		message.getExchange().put(Response.class, builder.build());
	}

	private void logAccessError(Message message) {
		OscarLog oscarLog = new OscarLog();
		oscarLog.setAction("REST WS: NOT AUTHORIZED");
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		if (request != null) {
			oscarLog.setIp(request.getRemoteAddr());
			oscarLog.setContent(request.getRequestURL().toString());
			oscarLog.setData(request.getParameterMap().toString());
		}

		LogAction.addLogSynchronous(oscarLog);
	}
}
