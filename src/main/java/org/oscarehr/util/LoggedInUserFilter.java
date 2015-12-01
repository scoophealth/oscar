/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;

public class LoggedInUserFilter implements javax.servlet.Filter {
	private static final Logger logger = MiscUtils.getLogger();

	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Starting Filter : " + getClass().getSimpleName());
	}

	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException {
		logger.debug("Entering LoggedInUserFilter.doFilter()");

		// set new / current data
		HttpServletRequest request = (HttpServletRequest) tmpRequest;
		LoggedInInfo x = generateLoggedInInfoFromSession(request);
		LoggedInInfo.setLoggedInInfoIntoSession(request.getSession(), x);

		logger.debug("LoggedInUserFilter chainning");
		chain.doFilter(tmpRequest, tmpResponse);
	}

	public void destroy() {
		// can't think of anything to do right now.
	}

	public static LoggedInInfo generateLoggedInInfoFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();

		LoggedInInfo loggedInInfo = new LoggedInInfo();
		loggedInInfo.setSession(session);
		loggedInInfo.setCurrentFacility((Facility) session.getAttribute(SessionConstants.CURRENT_FACILITY));
		loggedInInfo.setLoggedInProvider((Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER));
		loggedInInfo.setLoggedInSecurity((Security) session.getAttribute(SessionConstants.LOGGED_IN_SECURITY));
		loggedInInfo.setInitiatingCode(request.getRequestURI());
		loggedInInfo.setLocale(request.getLocale());
		loggedInInfo.setIp(request.getRemoteAddr());

		return (loggedInInfo);
	}
}
