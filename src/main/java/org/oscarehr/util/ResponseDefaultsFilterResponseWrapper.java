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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class ResponseDefaultsFilterResponseWrapper extends HttpServletResponseWrapper
{
	private static Logger logger = MiscUtils.getLogger();
	private boolean forceStrongETag;
	private boolean warnCharsetCacheChange;

	public ResponseDefaultsFilterResponseWrapper(HttpServletResponse response, boolean forceStrongETag, boolean warnCharsetCacheChange)
	{
		super(response);
		this.forceStrongETag = forceStrongETag;
		this.warnCharsetCacheChange = warnCharsetCacheChange;
	}

	private static void warnWithStackTrace(String message)
	{
		logger.warn(message, new Exception(message));
	}

	@Override
	public void setCharacterEncoding(String encoding)
	{
		super.setCharacterEncoding(encoding);

		if (warnCharsetCacheChange) warnWithStackTrace("Some one is switching the encoding on me! : " + encoding);
	}

	@Override
	public void setContentType(String contentType)
	{
		super.setContentType(contentType);

		if (warnCharsetCacheChange && contentType.contains("charset")) warnWithStackTrace("Some one is switching the encoding on me! : " + contentType);
	}

	@Override
	public void setHeader(String key, String value)
	{
		// this is currently here because tomcat doesn't have a way to set strong etags, but it's strong to us, file size + date mofidied is good enough for me
		// The actual strings here are picked out of the tomcat 7 source code, I didn't do equals ignore just for efficiency, yes I know it's more fragile but
		// hopefully in the near future tomcat just lets us configure it so this should be a temporary fix anyways.
		if (forceStrongETag && "ETag".equals(key) && value != null && value.startsWith("W/"))
		{
			value = value.substring(2);
		}

		super.setHeader(key, value);

		// detect changes to my defaults
		if (warnCharsetCacheChange)
		{
			if ("Content-Type".equals(key) && value.contains("charset") && !value.contains("charset=UTF-8")) warnWithStackTrace("Some one is switching the encoding : " + value);
			else if ("Cache-Control".equals(key)) warnWithStackTrace("Some one is setting the cache control. " + value);
		}
	}
}
