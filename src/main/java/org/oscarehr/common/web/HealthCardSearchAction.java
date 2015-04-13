/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class HealthCardSearchAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String hin = request.getParameter("hin");
		String ver = request.getParameter("ver");
		String issueDate = request.getParameter("issueDate");
		String hinExp = request.getParameter("hinExp");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		List<Demographic> matches = demographicDao.getDemographicsByHealthNum(hin);
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		
		if (matches != null) {
			if (matches.size() != 1 ) {
				hashMap.put("match", false);
			} else {
				hashMap.put("match", true);
				Demographic d = matches.get(0);
				hashMap.put("demoNo", d.getDemographicNo());
				hashMap.put("lastName", d.getLastName());
				hashMap.put("firstName", d.getFirstName());
				hashMap.put("hin", d.getHin());
				hashMap.put("hinVer", d.getVer());
				hashMap.put("phone", d.getPhone());
				
				String address = "";
				if (d.getAddress() != null && d.getAddress().trim().length() > 0)
					address += d.getAddress().trim() + "\n";
				if (d.getCity() != null && d.getCity().trim().length() > 0)
					address += d.getCity().trim();
				if (d.getProvince() != null && d.getProvince().trim().length() > 0)
					address += (d.getCity() != null && d.getCity().trim().length() > 0 ? ", " : "") + d.getProvince().trim();
				
				hashMap.put("address", address);
			}
		}
		
		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());
		
		
		return null;
		
	}
}
