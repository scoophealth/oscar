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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cxf.interceptor.Fault;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * Our web services are inherently stateless so we want to prevent excessive session object build up. This is caused because
 * the oscar permissions system sets credentials into the session space upon authentication. 
 */
// @WebFilter(urlPatterns={"/ws/*"})
public class WebServiceSessionInvalidatingFilter implements javax.servlet.Filter
{
	private static final Logger logger = MiscUtils.getLogger();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// nothing
	}

	@Override
	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) tmpRequest;

		try
		{
			chain.doFilter(tmpRequest, tmpResponse);
		}
		catch(RuntimeException runtimeException)
		{
			//unwrap exceptions to leverage web.xml
			Throwable cause = runtimeException.getCause();
			
			if(cause instanceof Fault) {
				Throwable rootCause = cause.getCause();
				if(rootCause instanceof RuntimeException) {
					throw (RuntimeException) rootCause;
				}
			}
			
			logger.error(runtimeException.getMessage());
			throw runtimeException;//this exception wasn't a standard wrapped exception - so rethrow
		}
		finally
		{
			String requestURL = request.getRequestURL().toString();
			
			//don't apply to REST calls, we want those to be available to the web interface without losing session
			if(requestURL.indexOf("/ws/rs/")==-1 && requestURL.indexOf("/ws/oauth/")==-1 && requestURL.indexOf("/ws/services/")==-1) {
				HttpSession session = request.getSession(false);
				if (session != null) session.invalidate();
			}
			//I still need to figure out how to invalidate REST sessions that are stateless.
		}
	}

	@Override
	public void destroy()
	{
		// can't think of anything to do right now.
	}
}
