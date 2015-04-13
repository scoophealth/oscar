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

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;

public class DemographicExtServiceAction extends DispatchAction {

	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	   

	public ActionForward saveNewValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String demographicNo = request.getParameter("demographicNo");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo = loggedInInfo.getLoggedInProviderNo();
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }

		
		DemographicExt existing = demographicExtDao.getDemographicExt(Integer.parseInt(demographicNo), key);
		Integer id = null;
		if(existing == null) {
			DemographicExt d = new DemographicExt();
			d.setDateCreated(new Date());
			d.setDemographicNo(Integer.parseInt(demographicNo));
			d.setKey(key);
			d.setValue(value);
			d.setProviderNo(providerNo);
			
			demographicExtDao.persist(d);
			id = d.getId();
		} else {
			existing.setValue(value);
			demographicExtDao.merge(existing);
			id = existing.getId();
		}
			
		
		LogAction.addLog(providerNo, "write", id.toString(), value);
		
		JSONObject json = JSONObject.fromObject(new LabelValueBean("id",String.valueOf(id)));
		response.getWriter().println(json);
		return null;
	}
}
