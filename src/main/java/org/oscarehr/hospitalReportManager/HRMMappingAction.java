/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.hospitalReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMSubClassDao;
import org.oscarehr.hospitalReportManager.model.HRMSubClass;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class HRMMappingAction extends DispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {

		HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
		HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
		
		try {
			if (request.getParameter("deleteMappingId") != null && request.getParameter("deleteMappingId").trim().length() > 0) {
				hrmSubClassDao.remove(Integer.parseInt(request.getParameter("deleteMappingId")));
				return mapping.findForward("success");
			}
			String id = request.getParameter("id");
			
			String className = request.getParameter("class"); 
			String subClass = request.getParameter("subclass");
			String mnemonic = request.getParameter("mnemonic");
			String description = request.getParameter("description");
			String sendingFacilityIdSelect = request.getParameter("sendingFacilityIdSelect");
			String sendingFacilityId = request.getParameter("sendingFacilityId");
			String categoryId = request.getParameter("category");

			if(sendingFacilityIdSelect.equals("0")) {
				MiscUtils.getLogger().warn("Not a valid sending facility id chosen by user");
				request.setAttribute("success", false);
			}else if(!sendingFacilityIdSelect.equals("-1")) {
				sendingFacilityId = sendingFacilityIdSelect;
			}

			HRMSubClass hrmSubClass =null;
			if(id == null) {
				 hrmSubClass = new HRMSubClass();
			} else {
				hrmSubClass = hrmSubClassDao.find(Integer.parseInt(id));
			}
			
			hrmSubClass.setClassName(className);
			hrmSubClass.setSubClassName(subClass);
			hrmSubClass.setSendingFacilityId(sendingFacilityId);
			hrmSubClass.setSubClassMnemonic(mnemonic);
			hrmSubClass.setSubClassDescription(description);
			hrmSubClass.setHrmCategory(hrmCategoryDao.findById(Integer.parseInt(categoryId)).get(0));
			
			hrmSubClassDao.merge(hrmSubClass);
			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't set up sub class mapping", e);
			request.setAttribute("success", false);
		}

		return mapping.findForward("success");
	}

}
