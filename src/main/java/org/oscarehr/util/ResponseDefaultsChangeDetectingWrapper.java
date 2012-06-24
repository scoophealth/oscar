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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class ResponseDefaultsChangeDetectingWrapper extends HttpServletResponseWrapper
{
	private static final Logger logger = MiscUtils.getLogger();

	public ResponseDefaultsChangeDetectingWrapper(HttpServletResponse response)
	{
		super(response);
	}

	private static void warnWithStackTrace(String message)
	{
		logger.warn(message, new Exception(message));
	}
	
	@Override
	public void setCharacterEncoding(String encoding)
	{
		super.setCharacterEncoding(encoding);
		
		warnWithStackTrace("Some one is switching the encoding on me! : " + encoding);		
	}

	@Override
	public void setContentType(String contentType)
	{
		super.setContentType(contentType);
		
		if (contentType.contains("charset")) warnWithStackTrace("Some one is switching the encoding on me! : " + contentType);		
	}
	
	@Override
	public void setHeader(String key, String value)
	{
		super.setHeader(key, value);

		if ("Content-Type".equals(key) && value.contains("charset")) warnWithStackTrace("Some one is switching the encoding : " + value);		
		else if ("Cache-Control".equals(key)) warnWithStackTrace("Some one is setting the cache control. "+value);
	}
}
