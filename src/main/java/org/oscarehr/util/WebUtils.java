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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public final class WebUtils {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String ERROR_MESSAGE_SESSION_KEY = WebUtils.class.getName() + ".ERROR_MESSAGE_SESSION_KEY";
	private static final String INFO_MESSAGE_SESSION_KEY = WebUtils.class.getName() + ".INFO_MESSAGE_SESSION_KEY";

	public static void dumpParameters(HttpServletRequest request) {
		logger.error("--- Dump Request Parameters for "+request.getRequestURI()+" Start ---");

		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			logger.error(key + '=' + request.getParameter(key));
		}

		logger.error("--- Dump Request Parameters End ---");
	}

	/**
	 * Be warned, this is a debugging method, it should not be called in production.
	 * Calling this method will flush the input stream as servlet streams are not markable.
	 * @param request
	 * @throws IOException
	 */
	public static void dumpRequest(HttpServletRequest request) throws IOException {
		logger.error("--- Dump Request Start ---");

		InputStream is = request.getInputStream();
			
		StringBuilder sb=new StringBuilder();
		int x=0;
		while ((x=is.read())!=-1) sb.append((char)x);
		logger.error(sb.toString());
		
		logger.error("--- Dump Request End ---");
	}
		
	/**
	 * This method is intended to be used to see if a check box was checked on a form submit
	 */
	public static boolean isChecked(HttpServletRequest request, String parameter) {
		String temp = request.getParameter(parameter);
		return (temp != null && (temp.equalsIgnoreCase("on") || temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("checked")));
	}

	public static String popErrorAndInfoMessagesAsHtml(HttpSession session)
	{
		return(popErrorMessagesAsHtml(session)+popInfoMessagesAsHtml(session));
	}
	
	public static String popErrorMessagesAsHtml(HttpSession session) {
		ArrayList<String> al = popErrorMessages(session);

		StringBuilder sb = new StringBuilder();

		if (al != null && al.size() > 0) {
			sb.append("<ul style=\"color:red\">");
			
			for (String s : al)
			{
				sb.append("<li>");
				sb.append(s);				
				sb.append("</il>");
			}
			
			sb.append("</ul>");
		}

		return(sb.toString());
	}

	public static String popInfoMessagesAsHtml(HttpSession session) {
		ArrayList<String> al = popInfoMessages(session);

		StringBuilder sb = new StringBuilder();

		if (al != null && al.size() > 0) {
			sb.append("<ul style=\"color:#009900\">");
			
			for (String s : al)
			{
				sb.append("<li>");
				sb.append(s);				
				sb.append("</il>");
			}
			
			sb.append("</ul>");
		}

		return(sb.toString());
	}

	public static String popErrorMessagesAsAlert(HttpSession session) {
		ArrayList<String> al = popErrorMessages(session);

		StringBuilder sb = new StringBuilder();

		if (al != null && al.size() > 0) {
			sb.append("<script type=\"text/javascript\">");
			sb.append("alert('");
			
			for (String s : al)	sb.append(StringEscapeUtils.escapeJavaScript(s));				
			
			sb.append("');");
			sb.append("</script>");
		}

		return(sb.toString());
	}
		
	/**
	 * @return an arrayList of error message or null if no messages, the messages are then removed from the session upon return from this call.
	 */
	public static ArrayList<String> popErrorMessages(HttpSession session) {
		return(popMessages(session, ERROR_MESSAGE_SESSION_KEY));
	}

	public static void addErrorMessage(HttpSession session, String message) {
		addMessage(session, ERROR_MESSAGE_SESSION_KEY, message);
	}

	public static void addResourceBundleErrorMessage(HttpServletRequest request, String key) {
		String msg=LocaleUtils.getMessage(request, key);
		addErrorMessage(request.getSession(), msg);
	}

	/**
	 * @return an arrayList of error message or null if no messages, the messages are then removed from the session upon return from this call.
	 */
	public static ArrayList<String> popInfoMessages(HttpSession session) {
		return(popMessages(session, INFO_MESSAGE_SESSION_KEY));
	}

	public static void addInfoMessage(HttpSession session, String message) {
		addMessage(session, INFO_MESSAGE_SESSION_KEY, message);
	}
	
	public static void addResourceBundleInfoMessage(HttpServletRequest request, String key) {
		String msg=LocaleUtils.getMessage(request, key);
		addInfoMessage(request.getSession(), msg);
	}
	/**
	 * @return an arrayList of error message or null if no messages, the messages are then removed from the session upon return from this call.
	 */
	private static ArrayList<String> popMessages(HttpSession session, String type) {
		synchronized (session) {
			@SuppressWarnings("unchecked")
			ArrayList<String> errors = (ArrayList<String>) (session.getAttribute(type));
			session.removeAttribute(type);
			return (errors);
		}
	}

	private static void addMessage(HttpSession session, String type, String message) {
		synchronized (session) {
			@SuppressWarnings("unchecked")
			ArrayList<String> errors = (ArrayList<String>) (session.getAttribute(type));

			if (errors == null) {
				errors = new ArrayList<String>();
				session.setAttribute(type, errors);
			}

			errors.add(message);
		}
	}
	
	/**
	 * This method will return a string like "?foo=bar&asdf=zxcv"
	 * based on the contents of the map. Note that the first item is a ?
	 * if an empty map is passed in it will return ""
	 * 
	 * Note that the results are not html escaped.
	 * 
	 * Null or blank values will not be added as query parameters.
	 */
	public static String buildQueryString(Map<String, Object> map)
	{
		StringBuilder sb=new StringBuilder();
		
		for (Map.Entry<String,Object> entry : map.entrySet())
		{
			if (entry.getValue()==null) continue;
			
			if (sb.length()==0) sb.append('?');
			else sb.append('&');
			
			sb.append(entry.getKey());
			sb.append('=');
			sb.append(entry.getValue());
		}
		
		return(sb.toString());
	}
	
	public static String getCheckedString(boolean b)
	{
		if (b) return("checked=\"checked\"");
		else return("");
	}

	public static String getSelectedString(boolean b)
	{
		if (b) return("selected=\"selected\"");
		else return("");
	}

	/**
	 * @return the html string to disable a button or component if !enabled
	 */
	public static String getDisabledString(boolean enabled)
	{
		if (!enabled) return("disabled=\"disabled\"");
		else return("");
	}
}
