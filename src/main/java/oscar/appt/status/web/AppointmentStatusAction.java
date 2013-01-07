/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.appt.status.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.appt.status.service.AppointmentStatusMgr;
import oscar.appt.status.service.impl.AppointmentStatusMgrImpl;

public class AppointmentStatusAction extends DispatchAction {

    private static final Logger logger = MiscUtils.getLogger();

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("view");
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("reset");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        apptStatusMgr.reset();
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward changestatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("changestatus");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        int ID = Integer.parseInt(request.getParameter("statusID"));
        int iActive = Integer.parseInt(request.getParameter("iActive"));
        apptStatusMgr.changeStatus(ID, iActive);
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("modify");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        int ID = Integer.parseInt(request.getParameter("statusID"));
        AppointmentStatus appt = apptStatusMgr.getStatus(ID);
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        lazyForm.set("ID", ID);
        lazyForm.set("apptStatus", appt.getStatus());
        lazyForm.set("apptDesc", appt.getDescription());
        lazyForm.set("apptOldColor", appt.getColor());

        return mapping.findForward("edit");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("update");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        int ID = Integer.parseInt(lazyForm.get("ID").toString());
        String strDesc = lazyForm.get("apptDesc").toString();
        String strColor = lazyForm.get("apptColor").toString();
        if (null==strColor || strColor.equals(""))
            strColor = lazyForm.get("apptOldColor").toString();
        apptStatusMgr.modifyStatus(ID, strDesc, strColor);
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public WebApplicationContext getApptContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
    }

    public AppointmentStatusMgr getApptStatusMgr() {
        return new AppointmentStatusMgrImpl();
    }

    private void populateAllStatus(HttpServletRequest request) {
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        List allStatus = apptStatusMgr.getAllStatus();
        request.setAttribute("allStatus", allStatus);
        int iUseStatus = apptStatusMgr.checkStatusUsuage(allStatus);
        if (iUseStatus > 0) {
            request.setAttribute("useStatus", apptStatusMgr.getStatus(iUseStatus + 1).getStatus());
        }
    }
}
