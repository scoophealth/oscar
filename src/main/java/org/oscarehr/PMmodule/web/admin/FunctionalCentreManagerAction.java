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

package org.oscarehr.PMmodule.web.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.util.WebUtils;

public class FunctionalCentreManagerAction extends DispatchAction{

	private FunctionalCentreDao functionalCentreDao;
	
	public void setFunctionalCentreDao(FunctionalCentreDao functionalCentreDao) {
		this.functionalCentreDao = functionalCentreDao;
	}
	
	private static final String FORWARD_EDIT = "edit";
	private static final String FORWARD_LIST = "list";
	private static final String BEAN_FACILITIES = "functionalCentres";
	
	public List<FunctionalCentre> getAllFunctionalCentres() {
		List<FunctionalCentre> results = functionalCentreDao.findAll();
		
		return (results);
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<FunctionalCentre>  functionalCentres = functionalCentreDao.findAll();

		request.setAttribute(BEAN_FACILITIES, functionalCentres);
		
		return mapping.findForward(FORWARD_LIST);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		FunctionalCentre  functionalCentre = functionalCentreDao.find(id);

		FunctionalCentreManagerForm managerForm = (FunctionalCentreManagerForm) form;
		managerForm.setFunctionalCentre(functionalCentre);

		request.setAttribute("id", functionalCentre.getAccountId());
				
		return mapping.findForward(FORWARD_EDIT);
	}
	
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		FunctionalCentre functionalCentre = new FunctionalCentre("", "");
		((FunctionalCentreManagerForm) form).setFunctionalCentre(functionalCentre);
		return mapping.findForward(FORWARD_EDIT);
	}	
		
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		FunctionalCentreManagerForm mform = (FunctionalCentreManagerForm) form;
		FunctionalCentre functionalCentre = mform.getFunctionalCentre();
		
		if (isCancelled(request)) {
			request.getSession().removeAttribute("functionalCentreManagerForm");
			return list(mapping, form, request, response);
		}

		try {
			functionalCentre.setEnableCbiForm(WebUtils.isChecked(request, "functionalCentre.enableCbiForm"));
			
			if(functionalCentre.getAccountId() == null || functionalCentre.getAccountId().equals("") ) 
				functionalCentreDao.persist(functionalCentre);
			else
				functionalCentreDao.merge(functionalCentre);			
					
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("functionalCentre.saved", functionalCentre.getDescription()));
			saveMessages(request, messages);

			request.setAttribute("id", functionalCentre.getAccountId());

			return list(mapping, form, request, response);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("duplicateKey", "The name " + functionalCentre.getDescription()));
			saveMessages(request, messages);

			return mapping.findForward(FORWARD_EDIT);
		}
	}
		
}
