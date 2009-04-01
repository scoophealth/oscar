/*
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebUtils {
	private static final String ERROR_MESSAGE_SESSION_KEY = WebUtils.class.getName() + ".ERROR_MESSAGE_SESSION_KEY";

	/**
	 * This method is intended to be used to see if a check box was checked on a form submit
	 */
	public static boolean isChecked(HttpServletRequest request, String parameter) {
		String temp = request.getParameter(parameter);
		return (temp != null && (temp.equalsIgnoreCase("on") || temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("checked")));
	}

	/**
	 * @return an arrayList of error message or null if no messages, the messages are then removed from the session upon return from this call.
	 */
	public static ArrayList<String> popErrorMessages(HttpSession session) {
		synchronized (session) {
			@SuppressWarnings("unchecked")
			ArrayList<String> errors = (ArrayList<String>) (session.getAttribute(ERROR_MESSAGE_SESSION_KEY));
			session.removeAttribute(ERROR_MESSAGE_SESSION_KEY);
			return (errors);
		}
	}

	public static void addErrorMessage(HttpSession session, String errorMessage) {
		synchronized (session) {
			@SuppressWarnings("unchecked")
			ArrayList<String> errors = (ArrayList<String>) (session.getAttribute(ERROR_MESSAGE_SESSION_KEY));

			if (errors == null) {
				errors = new ArrayList<String>();
				session.setAttribute(ERROR_MESSAGE_SESSION_KEY, errors);
			}

			errors.add(errorMessage);
		}
	}
}