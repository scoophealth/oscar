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

package org.oscarehr.PMmodule.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.PreIntakeForm;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

public class IntakeAction extends BaseAction {

    private static Logger log = MiscUtils.getLogger();
    private ClientManager clientManager;
    private IntakeAManager intakeAManager;
    private IntakeCManager intakeCManager;


    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("demographic", null);

        return mapping.findForward("pre-intake");
    }

    public ActionForward do_intake(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");
        request.getSession().setAttribute("demographic", null);

        Demographic[] results = new Demographic[0];

        Demographic d = new Demographic();
        d.setFirstName(formBean.getFirstName());
        d.setLastName(formBean.getLastName());
        d.setYearOfBirth(formBean.getYearOfBirth());
        d.setMonthOfBirth(String.valueOf(formBean.getMonthOfBirth()));
        d.setDateOfBirth(String.valueOf(formBean.getDayOfBirth()));
        d.setHin(formBean.getHealthCardNumber());
        d.setVer(formBean.getHealthCardVersion());

        ClientSearchFormBean searchBean = new ClientSearchFormBean();
        searchBean.setFirstName(formBean.getFirstName());
        searchBean.setLastName(formBean.getLastName());
        searchBean.setSearchOutsideDomain(true);
        searchBean.setSearchUsingSoundex(true);

        List resultList = clientManager.search(searchBean);
        results = (Demographic[]) resultList.toArray(new Demographic[resultList.size()]);
        log.debug("local search found " + results.length + " match(es)");

        if (results != null && results.length > 0) {
            request.setAttribute("clients", results);
            return mapping.findForward("pre-intake");
        }

        return new_client(mapping, form, request, response);
    }

    /*
      * There can be a new client in 1 scenerio
      * 1) new client button was clicked.
      */
    public ActionForward new_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");

        /* no matches */
        if (formBean.getDemographicId().equals("")) {
            return mapping.findForward(getIntakeForward());
        }

        Demographic demographic = null;
        request.getSession().setAttribute("demographic", demographic);

        return mapping.findForward(getIntakeForward());
    }

    /*
      * This is just updating the intake form on a client since
      * he/she are already in the local database.
      */
    public ActionForward update_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");

        String demographicNo = formBean.getDemographicId();

        log.debug("update intake for client " + demographicNo);

        request.setAttribute("demographicNo", demographicNo);

        return mapping.findForward(getIntakeForward());
    }


    private String getIntakeForward() {
        String value = "intakea";

        if (intakeAManager.isNewClientForm()) {
            value = "intakea";
        }
        if (intakeCManager.isNewClientForm()) {
            value = "intakec";
        }
        return value;
    }

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

    public void setIntakeAManager(IntakeAManager mgr) {
    	this.intakeAManager = mgr;
    }

    public void setIntakeCManager(IntakeCManager mgr) {
    	this.intakeCManager = mgr;
    }
}
