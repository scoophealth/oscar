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
			
			String className = request.getParameter("class"); 
			String subClass = request.getParameter("subclass");
			String mnemonic = request.getParameter("mnemonic");
			String description = request.getParameter("description");
			String sendingFacilityId = request.getParameter("sendingFacilityId");
			String categoryId = request.getParameter("category");


			HRMSubClass hrmSubClass = new HRMSubClass();
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
