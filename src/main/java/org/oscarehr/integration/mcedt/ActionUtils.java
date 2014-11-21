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
package org.oscarehr.integration.mcedt;

import static org.oscarehr.integration.mcedt.McedtConstants.REQUEST_ATTR_KEY_RESOURCE_ID;
import static org.oscarehr.integration.mcedt.McedtConstants.SESSION_KEY_MCEDT_UPDATES;
import static org.oscarehr.integration.mcedt.McedtConstants.SESSION_KEY_MCEDT_UPLOADS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import oscar.util.ConversionUtils;
import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.TypeListResult;
import ca.ontario.health.edt.UpdateRequest;
import ca.ontario.health.edt.UploadData;

/**
 * Defines utility methods for action classes.
 *
 */
class ActionUtils {

	static ActionMessages addMessage(String messageId, String... messageParams) {
		ActionMessage message = null;
		if (messageParams != null) {
			message = new ActionMessage(messageId, messageParams);
		} else {
			message = new ActionMessage(messageId);
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		return messages;
	}

	static Detail getDetails(HttpServletRequest request) {
		Detail result = (Detail) request.getSession().getAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST);
		return result;
	}

	static void setDetails(HttpServletRequest request, Detail detail) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST, detail);
	}

	static void removeDetails(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST);
	}

	static void clearUpdateList(HttpServletRequest request) {
		clear(request.getSession(), SESSION_KEY_MCEDT_UPDATES);
	}

	static List<UpdateRequest> getUpdateList(HttpServletRequest request) {
		return getUpdateList(request.getSession());
	}

	static List<UpdateRequest> getUpdateList(HttpSession session) {
		return getSessionList(session, SESSION_KEY_MCEDT_UPDATES);
	}

	static List<UploadData> getUploadList(HttpServletRequest request) {
		return getUploadList(request.getSession());
	}

	static void clearUploadList(HttpServletRequest request) {
		clear(request.getSession(), SESSION_KEY_MCEDT_UPLOADS);
	}

	static List<UploadData> getUploadList(HttpSession session) {
		return getSessionList(session, SESSION_KEY_MCEDT_UPLOADS);
	}

	private static void clear(HttpSession session, String sessionKey) {
		session.removeAttribute(sessionKey);
	}

	private static <T> List<T> getSessionList(HttpSession session, String sessionKey) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) session.getAttribute(sessionKey);
		if (result == null) {
			result = new ArrayList<T>();
			session.setAttribute(sessionKey, result);
		}
		return result;
	}

	static List<BigInteger> getResourceIds(HttpServletRequest request) {
		String[] resourceIds = request.getParameterValues(REQUEST_ATTR_KEY_RESOURCE_ID);

		List<BigInteger> ids = new ArrayList<BigInteger>();
		if (resourceIds == null) {
			return ids;
		}

		for (String i : resourceIds) {
			ids.add(BigInteger.valueOf(ConversionUtils.fromIntString(i)));
		}
		return ids;
	}

	static TypeListResult getTypeList(HttpServletRequest request) {
		return (TypeListResult) request.getSession().getAttribute(McedtConstants.SESSION_KEY_TYPE_LIST);
	}

	static void setTypeList(HttpServletRequest request, TypeListResult result) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_TYPE_LIST, result);
	}

	static void removeTypeList(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_TYPE_LIST);
	}
}
