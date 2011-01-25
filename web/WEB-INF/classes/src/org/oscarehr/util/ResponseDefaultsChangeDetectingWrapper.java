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
		getResponse().setCharacterEncoding(encoding);
		warnWithStackTrace("Some one is switching the encoding on me! : " + encoding);		
	}

	@Override
	public void setContentType(String contentType)
	{
		getResponse().setContentType(contentType);
		if (contentType.contains("charset")) warnWithStackTrace("Some one is switching the encoding on me! : " + contentType);

                else super.setContentType(contentType);
	}
	
	@Override
	public void setHeader(String key, String value)
	{
		if ("Content-Type".equals(key) && value.contains("charset")) warnWithStackTrace("Some one is switching the encoding : " + value);
		else if ("Cache-Control".equals(key)) warnWithStackTrace("Some one is setting the cache control. "+value);

                else super.setHeader(key, value);
	}
}
