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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.web.forms.IntegratorPushItem;
import org.oscarehr.PMmodule.web.forms.IntegratorPushResponse;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.IntegratorProgress;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.IntegratorPushManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

public class IntegratorPushAction extends DispatchAction {

	private UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	private IntegratorPushManager integratorPushManager = SpringUtils.getBean(IntegratorPushManager.class);

	public ActionForward getPushData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = null;
		
		com.quatro.service.security.SecurityManager securityMgr = new com.quatro.service.security.SecurityManager();
		if (securityMgr.hasReadAccess("_admin", request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			List<IntegratorProgress> ipList = integratorPushManager.findAll();
			List<IntegratorPushItem> results = new ArrayList<IntegratorPushItem>();
			for(IntegratorProgress ip:ipList) {
	        	IntegratorPushItem item =  new IntegratorPushItem();
	        	BeanUtils.copyProperties(ip, item);
	        	item.setTotalDemographics(integratorPushManager.getTotalItemsForProgress(ip.getId()));
	        	item.setTotalOutstanding(integratorPushManager.getTotalOutstandingItemsForProgress(ip.getId()));
	        	results.add(item);
	        }
			
			IntegratorPushResponse ipResponse = new IntegratorPushResponse();
			ipResponse.setItems(results);
			ipResponse.setPaused(integratorPushManager.isPauseFlagSet());
			//jsonArray = JSONArray.fromObject( results );
			json = JSONObject.fromObject(ipResponse);
	        
		}

		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Couldn't return result", e);
		}
		
		return null;
	}

	/**
	 * We basically just set a system property in the DB so that the next time the job runs, it either, does or not based on the property
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward disableNextAndFuturePushes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();

		com.quatro.service.security.SecurityManager securityMgr = new com.quatro.service.security.SecurityManager();
		if (securityMgr.hasReadAccess("_admin", request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {

			String strType = request.getParameter("type");
			Boolean type = Boolean.valueOf(strType);

			UserProperty prop = userPropertyDao.getProp(IntegratorPushManager.DISABLE_INTEGRATOR_PUSH_PROP);
			if (prop == null) {
				prop = new UserProperty();
				prop.setName(IntegratorPushManager.DISABLE_INTEGRATOR_PUSH_PROP);
				prop.setProviderNo(null);
				prop.setValue(type.toString());
				userPropertyDao.persist(prop);
			} else {
				prop.setValue(type.toString());
				userPropertyDao.merge(prop);
			}
			json.put("success", true);
			json.put("DISABLE_INTEGRATOR_PUSH_PROP", prop.getValue());

		} else {
			json.put("success", false);
			json.put("reason", "Access Denied");
		}

		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Couldn't return result", e);
		}

		return null;
	}
	
	public ActionForward togglePause(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();

		String in = request.getParameter("pause");
		boolean doPause = false;
		
		if(in != null) {
			doPause = Boolean.valueOf(in);
			
			com.quatro.service.security.SecurityManager securityMgr = new com.quatro.service.security.SecurityManager();
			if (securityMgr.hasReadAccess("_admin", request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
	
				UserProperty prop = userPropertyDao.getProp(IntegratorPushManager.INTEGRATOR_PAUSE_FULL_PUSH);
				if (prop == null) {
					prop = new UserProperty();
					prop.setName(IntegratorPushManager.INTEGRATOR_PAUSE_FULL_PUSH);
					prop.setProviderNo(null);
					prop.setValue(new  Boolean(doPause).toString());
					userPropertyDao.persist(prop);
				} else {
					prop.setValue(new  Boolean(doPause).toString());
					userPropertyDao.merge(prop);
				}
				json.put("success", true);
				json.put("INTEGRATOR_PAUSE_FULL_PUSH", prop.getValue());
	
			} else {
				json.put("success", false);
				json.put("reason", "Access Denied");
			}
		}
		
		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Couldn't return result", e);
		}

		return null;
	}

}
