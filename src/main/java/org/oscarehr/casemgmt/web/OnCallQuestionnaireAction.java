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

package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.casemgmt.model.OnCallQuestionnaire;
import org.oscarehr.casemgmt.service.OnCallManager;
import org.oscarehr.casemgmt.web.formbeans.OnCallQuestionnaireFormBean;

public class OnCallQuestionnaireAction extends DispatchAction {
	private OnCallManager mgr;

	public void setOnCallManager(OnCallManager mgr) {
		this.mgr = mgr;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return form(mapping,form,request,response);
	}

	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		return mapping.findForward("form");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String providerNo = request.getParameter("providerNo");
		String type = request.getParameter("type");

		OnCallQuestionnaireFormBean f = (OnCallQuestionnaireFormBean)form;

		OnCallQuestionnaire bean = new OnCallQuestionnaire();
		bean.setProviderNo(providerNo);
		bean.setType(type);
		bean.setCourse_action(f.getCourse_action());
		bean.setDatetime(f.getDateObject());
		bean.setNurse(f.getNurse());
		bean.setPhysician_consult(f.getPhysician_consult());
		bean.setType_health(f.getType_health());

		mgr.save(bean);

		return mapping.findForward("windowClose");
	}


}
