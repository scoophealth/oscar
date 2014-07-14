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
package org.oscarehr.common.web;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

/**
 * Creates a provider notification record. 
 */
public class ProviderNotificationAction extends Action {

	private static Logger logger = Logger.getLogger(ProviderNotificationAction.class);

	private UserDSMessagePrefsDao userDsMessagePrefsDao = SpringUtils.getBean(UserDSMessagePrefsDao.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		String resourceId = request.getParameter("id");
		String resourceType = request.getParameter("type");

		if (logger.isDebugEnabled()) {
			logger.debug("Processing notification");
		}

		List<UserDSMessagePrefs> prefs = userDsMessagePrefsDao.findMessages(providerNo, resourceType, resourceId, false);
		for(UserDSMessagePrefs p : prefs) {
			p.setArchived(true);
			userDsMessagePrefsDao.merge(p);
		}

		UserDSMessagePrefs pref = new UserDSMessagePrefs();
		pref.setProviderNo(providerNo);
		pref.setResourceId(resourceId);
		pref.setResourceType(resourceType);
		pref.setArchived(false);
		pref.setResourceUpdatedDate(new Date());

		userDsMessagePrefsDao.persist(pref);

		JSONObject json = new JSONObject();
		json.put("id", pref.getId());
		json.put("status", "success");

		Writer writer = null;
		try {
			writer = new OutputStreamWriter(response.getOutputStream());
			json.write(writer);
			writer.flush();
		} finally {
			writer.close();
		}

		return null;
	}


}
