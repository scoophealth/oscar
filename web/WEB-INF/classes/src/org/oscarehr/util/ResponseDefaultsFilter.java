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
		
		chain.doFilter(request, new ResponseDefaultsChangeDetectingWrapper(response));
	}

	private static void setCaching(HttpServletRequest request, HttpServletResponse response)
	{
		// so the caching scheme will be as follows :
		// *.jsp should never be cached, presumably it's jsp because it's dynamic (and other jsp like pages)
		// everything else is allowed to be cached, but we won't explicity set the expires, we'll just let the browser sort it out, I don't want to mess with date formatting right now
		
		String requestUri=request.getRequestURI();
		if (requestUri.endsWith(".jsp") || requestUri.endsWith(".json") || requestUri.endsWith(".jsf") || requestUri.endsWith(".do")) response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	}

	private static void setEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{
		// use the requested encoding, but if none is specified then we use UTF-8
		if (request.getCharacterEncoding() == null)	request.setCharacterEncoding(ENCODING);
		response.setCharacterEncoding(ENCODING);
	}
}