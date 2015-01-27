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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.eyeform.dao.MacroDao;
import org.oscarehr.eyeform.model.Macro;

import org.oscarehr.util.SpringUtils;


public class MacroAction extends DispatchAction {

	public static List<LabelValueBean> sliCodeList = new ArrayList<LabelValueBean>();
	static {
		sliCodeList.add(new LabelValueBean("Not Applicable","NA"));
		sliCodeList.add(new LabelValueBean("HDS | Hospital Day Surgery","HDS"));
		sliCodeList.add(new LabelValueBean("HED | Hospital Emergency Department","HED"));
		sliCodeList.add(new LabelValueBean("HIP | Hospital In-Patient","HIP"));
		sliCodeList.add(new LabelValueBean("HOP | Hospital Out-Patient","HOP"));
		sliCodeList.add(new LabelValueBean("HRP | Hospital Referred Patient","HRP"));
		sliCodeList.add(new LabelValueBean("IHF | Independant Health Facility","IHF"));
		sliCodeList.add(new LabelValueBean("OFF | Office of community physician","OFF"));
		sliCodeList.add(new LabelValueBean("OTN | Ontario Telemedicine Network","OTN"));
	}

	MacroDao dao = (MacroDao)SpringUtils.getBean("MacroDAO");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	BillingServiceDao billingServiceDao = (BillingServiceDao)SpringUtils.getBean("billingServiceDao");

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<Macro> macros = dao.getAll();
		request.setAttribute("macros", macros);
        return mapping.findForward("list");
    }

	public ActionForward addMacro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

    	request.setAttribute("providers",providerDao.getActiveProviders());

    	if(request.getParameter("macro.id") != null) {
    		int macroId = Integer.parseInt(request.getParameter("macro.id"));
    		Macro macro = dao.find(macroId);
    		DynaValidatorForm f = (DynaValidatorForm)form;
    		f.set("macro", macro);
    	}

        return mapping.findForward("form");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	DynaValidatorForm f = (DynaValidatorForm)form;

    	Macro macro = (Macro)f.get("macro");

    	if(request.getParameter("macro.id") != null && request.getParameter("macro.id").length()>0) {
    		macro.setId(Integer.parseInt(request.getParameter("macro.id")));
    	}

    	StringBuilder errors = new StringBuilder();

    	//validate billing
    	String bcodes = macro.getBillingCodes();
		if (StringUtils.isNotBlank(bcodes)) {
			macro.setBillingCodes(bcodes.trim().replace("\r", ""));
			String[] bcs = macro.getBillingCodes().split("\n");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String serviceDate = sf.format(new Date());

			for (String code : bcs) {
				if (StringUtils.isBlank(code))
					continue;
				String[] atts = code.split("\\|");
				Object[] price = billingServiceDao.getUnitPrice(atts[0], serviceDate);
				if (price == null) {
					errors.append("<br/>Invalid billing code or format: " + code);
				}
				boolean requiresSli = billingServiceDao.codeRequiresSLI(atts[0]);
				if(requiresSli&& atts.length != 3) {
					errors.append("<br/>SLI code required for billing code: " + code);
					continue;
				}
				if(requiresSli) {
					String sli = atts[2];
					if(!isValidSli(sli)) {
						errors.append("<br/>invalid SLI code for billing code: " + code);
						continue;
					}
				}
			}
		}

		//validate tests
		String tests = macro.getTestRecords().replace("\r", "");
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(tests)) {
			for (String test : tests.split("\n")) {
				if (StringUtils.isBlank(test))
					continue;
				if (!test.matches(".*\\|(routine|ASAP|urgent)\\|.*")) {
					errors.append("<br/>Invalid test_urgency attribute in test bookings.");
				}
				if (!test.matches(".*\\|(OU|OD|OS)\\|.*")) {
					errors.append("<br/>Invalid test_eye attribute in test bookings.");
				}
				sb.append(test.trim()).append("\n");
			}
		}
		macro.setTestRecords(sb.toString());

		//addMessage(request, "Macro has been saved successfully.");
		if(errors.toString().length()>0) {
			request.setAttribute("errors", errors.toString());
			request.setAttribute("providers",providerDao.getActiveProviders());
	    	DynaValidatorForm f2 = (DynaValidatorForm)form;
	    	f2.set("macro", macro);
	        return mapping.findForward("form");
		}

    	if(macro.getId() != null && macro.getId() == 0) {
    		macro.setId(null);
    	}

    	if(macro.getId() == null) {
    		dao.persist(macro);
    	} else {
    		dao.merge(macro);
    	}

    	request.setAttribute("parentAjaxId", "macro");
    	return mapping.findForward("success");
    }

    private boolean isValidSli(String sli) {
    	if(sli.equals("NA") || sli.equals("HDS") || sli.equals("HED") || sli.equals("HIP") || sli.equals("HOP")
    			|| sli.equals("HRP") || sli.equals("IHF") || sli.equals("OFF") || sli.equals("OTN")) {
    		return true;
    	}
    	return false;
    }
    public ActionForward deleteMacro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String[] ids = request.getParameterValues("selected_id");
    	for(int x=0;x<ids.length;x++) {
    		dao.remove(Integer.parseInt(ids[x]));
    	}
        return list(mapping,form,request,response);
    }
}
