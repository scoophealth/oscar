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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * This file has been renamed to "Old" because this file should no longer be enhanced. A common version of this class
 * is made available from the Utils package. There maybe some methods left here which don't entirely make sense
 * or don't make sense in the context of a general purpose project agnostic utility class. This class still exists as "Old" so
 * we can slowly refactor the non sensical code to use the new common utilities. Any remaining methods which do make sense
 * should then me moved to a generic Oscar Utility class or similar. If the method makes sense in a project
 * agnostic fashion, then it should be moved to the util project itself. 
 */
public final class WebUtilsOld {

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
		return(WebUtils.popMessages(session, WebUtils.ERROR_MESSAGE_SESSION_KEY));
	}

	public static void addErrorMessage(HttpSession session, String message) {
		WebUtils.addMessage(session, WebUtils.ERROR_MESSAGE_SESSION_KEY, message);
	}

	/**
	 * @return an arrayList of error message or null if no messages, the messages are then removed from the session upon return from this call.
	 */
	public static ArrayList<String> popInfoMessages(HttpSession session) {
		return(WebUtils.popMessages(session, WebUtils.INFO_MESSAGE_SESSION_KEY));
	}

	public static void addInfoMessage(HttpSession session, String message) {
		WebUtils.addMessage(session, WebUtils.INFO_MESSAGE_SESSION_KEY, message);
	}
	
	public static void addResourceBundleInfoMessage(HttpServletRequest request, String key) {
		String msg=LocaleUtils.getMessage(request, key);
		addInfoMessage(request.getSession(), msg);
	}

}
