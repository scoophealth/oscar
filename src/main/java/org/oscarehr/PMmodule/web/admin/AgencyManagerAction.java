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

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.service.AgencyManager;

import oscar.log.LogAction;

public class AgencyManagerAction extends DispatchAction {

    private static final String FORWARD_EDIT = "edit";
    private static final String FORWARD_VIEW = "view";

    private static final String BEAN_AGENCY = "agency";

    private AgencyManager agencyManager;
  
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(BEAN_AGENCY, agencyManager.getLocalAgency());

        return mapping.findForward(FORWARD_VIEW);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        Agency localAgency = agencyManager.getLocalAgency();

        agencyForm.set(BEAN_AGENCY, localAgency);

        request.setAttribute("id", localAgency.getId());

        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        Agency agency = (Agency) agencyForm.get(BEAN_AGENCY);

        if (isCancelled(request)) {
            request.getSession().removeAttribute("agencyManagerForm");
            request.setAttribute("id", agency.getId());

            return view(mapping, form, request, response);
        }

        agencyManager.saveAgency(agency);

        ActionMessages messages = new ActionMessages();
        saveMessages(request, messages);

        request.setAttribute("id", agency.getId());

        LogAction.log("write", "agency", agency.getId().toString(), request);

        return mapping.findForward(FORWARD_EDIT);
    }

    public void setAgencyManager(AgencyManager mgr) {
    	this.agencyManager = mgr;
    }

}
