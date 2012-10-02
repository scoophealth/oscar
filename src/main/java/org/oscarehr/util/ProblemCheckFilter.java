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


package org.oscarehr.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;

public final class ProblemCheckFilter implements Filter
{
	private static final Logger logger = MiscUtils.getLogger();

	private static final long MAX_THRESHOLD=1024000*10; // we'll start at 10 megs of ram for now
	
	public static class SessionChecker implements HttpSession
	{
		private HttpSession session;
		
		public SessionChecker(HttpSession session)
		{
			this.session=session;
		}

		public Object getAttribute(String arg0)
		{
			return session.getAttribute(arg0);
		}

		public Enumeration getAttributeNames()
		{
			return session.getAttributeNames();
		}

		public long getCreationTime()
		{
			return session.getCreationTime();
		}

		public String getId()
		{
			return session.getId();
		}

		public long getLastAccessedTime()
		{
			return session.getLastAccessedTime();
		}

		public int getMaxInactiveInterval()
		{
			return session.getMaxInactiveInterval();
		}

		public ServletContext getServletContext()
		{
			return session.getServletContext();
		}

		public HttpSessionContext getSessionContext()
		{
			return session.getSessionContext();
		}

		public Object getValue(String arg0)
		{
			return session.getValue(arg0);
		}

		public String[] getValueNames()
		{
			return session.getValueNames();
		}

		public void invalidate()
		{
			session.invalidate();
		}

		public boolean isNew()
		{
			return session.isNew();
		}

		public void putValue(String arg0, Object arg1)
		{
			if (!(arg1 instanceof Serializable)) logger.warn("Some one putting non serialisable item into session. key="+arg0, new Exception("Non serialisable item in session"));
			
			session.putValue(arg0, arg1);
		}

		public void removeAttribute(String arg0)
		{
			session.removeAttribute(arg0);
		}

		public void removeValue(String arg0)
		{
			session.removeValue(arg0);
		}

		public void setAttribute(String arg0, Object arg1)
		{
			if (!(arg1 instanceof Serializable)) logger.warn("Some one putting non serialisable item into session. key="+arg0);

			session.setAttribute(arg0, arg1);
		}

		public void setMaxInactiveInterval(int arg0)
		{
			session.setMaxInactiveInterval(arg0);
		}
	}
	
	public static class ServletRequestProxy extends HttpServletRequestWrapper
	{
		public ServletRequestProxy(HttpServletRequest request)
		{
			super(request);
		}
		
		public HttpSession getSession()
		{
			HttpSession parentSession = super.getSession();
			if (parentSession == null)
				return null;
			return new SessionChecker(super.getSession());
		}
		
		public HttpSession getSession(boolean create)
		{
			HttpSession parentSession = super.getSession(create);
			if (parentSession == null)
				return null;
			return new SessionChecker(super.getSession(create));
		}
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		logger.info("Initialising "+ProblemCheckFilter.class.getSimpleName());
	}

	@Override
	public void destroy()
	{
		logger.info("shutdown "+ProblemCheckFilter.class.getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest originalRequest, ServletResponse originalResponse, FilterChain chain) throws IOException, ServletException
	{
		long usedBefore=Runtime.getRuntime().maxMemory()-Runtime.getRuntime().freeMemory();
		HttpServletRequest request=(HttpServletRequest)originalRequest;
		try
		{
			chain.doFilter(new ServletRequestProxy(request), originalResponse);
		}
		finally
		{
			long usedAfter=Runtime.getRuntime().maxMemory()-Runtime.getRuntime().freeMemory();
			
			long usedForRequest=usedAfter-usedBefore;
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Memory usage | "+usedForRequest+" | "+request.getRequestURI()+" | "+request.getQueryString());
			}
			
			if (usedForRequest>MAX_THRESHOLD)
			{
				logger.warn("Possible excessive memory usage | "+usedForRequest+" | "+request.getRequestURI()+" | "+request.getQueryString());
			}
		}
	}
}
