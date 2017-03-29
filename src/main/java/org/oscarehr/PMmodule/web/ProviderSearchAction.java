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

package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProviderSearchAction extends DispatchAction {
	
	private ProviderManager providerManager;
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("q");
		if(name == null) {
			name = "";
		}
		request.setAttribute("providers",providerManager.search(name));
		return mapping.findForward("results");
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String programId = request.getParameter("programNo");
		if(programId == null || "".equals(programId) ) {
			programId = null;
		}
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		List<Provider> providers = new ArrayList<Provider>();
		
		if(programId != null) {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(Integer.parseInt(programId));
			ProgramProviderDAO programProviderDao = SpringUtils.getBean(ProgramProviderDAO.class);
			providers = programProviderDao.getProvidersByPrograms(ids);
		} else {
			ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
			providers = providerManager.getProviders(loggedInInfo, true);
		}
		
		JSONArray arr = new JSONArray();
		for(Provider p:providers) {
			JSONObject o = new JSONObject();
			o.put("id", p.getProviderNo());
			o.put("name", p.getFormattedName());
			arr.add(o);
		}
		
		response.getWriter().print(arr);
		
		return null;
	}
	
}
