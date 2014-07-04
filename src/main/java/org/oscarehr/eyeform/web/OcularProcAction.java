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


package org.oscarehr.eyeform.web;

import java.util.Date;
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
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.EyeformOcularProcedureDao;
import org.oscarehr.eyeform.model.EyeformOcularProcedure;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OcularProcAction extends DispatchAction {

	EyeformOcularProcedureDao dao = SpringUtils.getBean(EyeformOcularProcedureDao.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String demographicNo = request.getParameter("demographicNo");

    	List<EyeformOcularProcedure> procs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
    	request.setAttribute("procs", procs);

        return mapping.findForward("list");
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());

    	if(request.getParameter("proc.id") != null) {
    		int procId = Integer.parseInt(request.getParameter("proc.id"));
    		EyeformOcularProcedure procedure = dao.find(procId);
    		DynaValidatorForm f = (DynaValidatorForm)form;
    		f.set("proc", procedure);


    		if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
    			try {
    				HashMap<String, EyeformOcularProcedure> hashMap = new HashMap<String, EyeformOcularProcedure>();

    				hashMap.put("proc", procedure);

    				JsonConfig config = new JsonConfig();
    		    	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

    				JSONObject json = JSONObject.fromObject(hashMap, config);
    				response.getOutputStream().write(json.toString().getBytes());
    			} catch (Exception e) {
    				MiscUtils.getLogger().error("Can't write json encoded message", e);
    			}

    			return null;
    		}

    	}



        return mapping.findForward("form");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	EyeformOcularProcedure procedure = (EyeformOcularProcedure)f.get("proc");

    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

    	EyeformOcularProcedureDao dao = (EyeformOcularProcedureDao)SpringUtils.getBean("ocularProcDao");
		procedure.setProvider(loggedInInfo.getLoggedInProviderNo());

    	if(request.getParameter("proc.id") != null && request.getParameter("proc.id").length()>0) {
    		procedure.setId(Integer.parseInt(request.getParameter("proc.id")));
    	}

    	if(procedure.getId() != null && procedure.getId() == 0) {
    		procedure.setId(null);
    	}
    	procedure.setUpdateTime(new Date());

    	if(procedure.getId() == null) {
    		dao.persist(procedure);
    	} else {
    		dao.merge(procedure);
    	}

    	if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
			HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
			hashMap.put("saved", procedure.getId());

			JSONObject json = JSONObject.fromObject(hashMap);
			response.getOutputStream().write(json.toString().getBytes());

			return null;
		}

    	request.setAttribute("parentAjaxId", "ocularprocedure");
    	return mapping.findForward("success");
    }

}
