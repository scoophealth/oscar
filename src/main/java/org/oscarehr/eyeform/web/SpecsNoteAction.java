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

import java.io.IOException;
import java.io.PrintWriter;
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
import org.oscarehr.eyeform.dao.EyeformSpecsHistoryDao;
import org.oscarehr.eyeform.model.EyeformSpecsHistory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SpecsNoteAction extends DispatchAction {
	EyeformSpecsHistoryDao dao = (EyeformSpecsHistoryDao)SpringUtils.getBean("eyeformSpecsHistoryDao");
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String demographicNo = request.getParameter("demographicNo");
    	

    	List<EyeformSpecsHistory> specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
    	request.setAttribute("specs", specs);

        return mapping.findForward("list");
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());

    	if(request.getParameter("specs.id") != null) {
    		int shId = Integer.parseInt(request.getParameter("specs.id"));
    		EyeformSpecsHistory specs = dao.find(shId);
    		DynaValidatorForm f = (DynaValidatorForm)form;
    		f.set("specs", specs);

    		if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
    			try {
    				HashMap<String, EyeformSpecsHistory> hashMap = new HashMap<String, EyeformSpecsHistory>();

    				hashMap.put("specs", specs);

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
    	String type=request.getParameter("type");
    	String date=request.getParameter("dateStr");
    	String odSph=request.getParameter("odSph");
    	String odCyl=request.getParameter("odCyl");
    	String odAxis=request.getParameter("odAxis");
    	String odAdd=request.getParameter("odAdd");
    	String odPrism=request.getParameter("odPrism");
    	String osSph=request.getParameter("osSph");
    	String osCyl=request.getParameter("osCyl");
    	String osAxis=request.getParameter("osAxis");
    	String osAdd=request.getParameter("osAdd");
    	String osPrism=request.getParameter("osPrism");
    	
    	int demographicNo = Integer.parseInt(request
				.getParameter("specs.demographicNo"));
		int appointmentNo = Integer.parseInt(request
				.getParameter("specs.appointmentNo"));
		
    	EyeformSpecsHistory specs = (EyeformSpecsHistory)f.get("specs");
    	if(specs.getId()!=null && specs.getId()==0) {
    		specs.setId(null);
    	}
    	
    	specs.setProvider(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
    	
    	if(request.getParameter("specs.id") != null && request.getParameter("specs.id").length()>0) {
    		specs.setId(Integer.parseInt(request.getParameter("specs.id")));
    	}
        
    	if(specs.getId() != null && specs.getId() == 0) {
    		specs.setId(null);
    	}
    	specs.setUpdateTime(new Date());
    	
    	specs.setOdAdd(odAdd);
    	specs.setOdAxis(odAxis);
    	specs.setOdCyl(odCyl);
    	specs.setOdPrism(odPrism);
    	specs.setOdSph(odSph);
    	
    	specs.setOsAdd(osAdd);
    	specs.setOsAxis(osAxis);
    	specs.setOsCyl(osCyl);
    	specs.setOsPrism(osPrism);
    	specs.setOsSph(osSph);
    	
    	specs.setType(type);
    	specs.setDateStr(date);
    	specs.setId(dao.getById(demographicNo, appointmentNo, type));
    	if (specs.getId() == null||specs.getId().equals(0)) {
			specs.setId(null);
			dao.persist(specs);
		} else {

			dao.merge(specs);

		}


		if (request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true")) {
			HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
			hashMap.put("saved", specs.getId());

			JSONObject json = JSONObject.fromObject(hashMap);
			response.getOutputStream().write(json.toString().getBytes());

			return null;
		}

    	request.setAttribute("parentAjaxId", "specshistory");
    	return mapping.findForward("success");
    }


    public ActionForward copySpecs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String demographicNo = request.getParameter("demographicNo");
    	
    	List<EyeformSpecsHistory> specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
    	if(specs.size()>0) {
    		EyeformSpecsHistory latestSpecs = specs.get(0);
    		PrintWriter out = response.getWriter();
    		out.println("setfieldvalue(\"od_manifest_refraction_sph\",\""+latestSpecs.getOdSph()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_sph\",\""+latestSpecs.getOsSph()+"\");");
    		out.println("setfieldvalue(\"od_manifest_refraction_cyl\",\""+latestSpecs.getOdCyl()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_cyl\",\""+latestSpecs.getOsCyl()+"\");");
    		out.println("setfieldvalue(\"od_manifest_refraction_axis\",\""+latestSpecs.getOdAxis()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_axis\",\""+latestSpecs.getOsAxis()+"\");");
    	} else {
    		PrintWriter out = response.getWriter();
    		out.println("alert('No Specs Found.');");
    	}
    	return null;
    }
}
