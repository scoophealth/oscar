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
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public final class ResponseDefaultsFilter implements Filter
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final String ENCODING="UTF-8";

    private static String[] noCacheEndings = { ".jsp", ".json", ".jsf" ,".do",".js"};
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		logger.info("Initialising "+ResponseDefaultsFilter.class.getSimpleName());
	}

	@Override
	public void destroy()
	{
		logger.info("shutdown "+ResponseDefaultsFilter.class.getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest originalRequest, ServletResponse originalResponse, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request=(HttpServletRequest)originalRequest;
		HttpServletResponse response=(HttpServletResponse)originalResponse;
		
		setEncoding(request, response);
		setCaching(request, response);
		
		response = new ResponseDefaultsFilterResponseWrapper(response, true, true);
		chain.doFilter(request, response);
		
	}

	private static void setCaching(HttpServletRequest request, HttpServletResponse response)
	{
		
		
		String requestUri=request.getRequestURI();
		for (String noCacheEnding : noCacheEndings) {
			if (requestUri.endsWith(noCacheEnding)) {
				response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				return;
			}
		}
	}

	private static void setEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{
		// use the requested encoding, but if none is specified then we use UTF-8
		if (request.getCharacterEncoding() == null)	request.setCharacterEncoding(ENCODING);
		response.setCharacterEncoding(ENCODING);
	}
}
