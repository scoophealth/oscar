/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.eform.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.util.SpringUtils;

public class RTLSettingsAction extends DispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean status = "on".equals(request.getParameter("indivica_rtl_enabled"));
        EFormDao efd = (EFormDao) SpringUtils.getBean("EFormDao");
        efd.setIndivicaRTLEnabled(status);
        request.setAttribute("status", status ? "Enabled" : "Disabled");
        return mapping.findForward("success");
    }
}
