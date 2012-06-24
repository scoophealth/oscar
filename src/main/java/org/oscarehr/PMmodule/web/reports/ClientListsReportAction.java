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
package org.oscarehr.PMmodule.web.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.Provider;

public class ClientListsReportAction extends DispatchAction {

    private DemographicDao demographicDao;

    public void setDemographicDao(DemographicDao demographicDao) {
        this.demographicDao = demographicDao;
    }

    private ProviderManager providerManager;

    public void setProviderManager(ProviderManager providerManager) {

        this.providerManager = providerManager;
    }

    private ProgramManager programManager;

    public void setProgramManager(ProgramManager programManager) {

        this.programManager = programManager;
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // need to get the reporting options here, i.e.
        // - provider list
        // - program list
        // - icd-10 isue list?

        List<Provider> providers = providerManager.getProviders();
        request.setAttribute("providers", providers);

        List<Program> programs = programManager.getAllPrograms();
        request.setAttribute("programs", programs);

        return mapping.findForward("form");
    }

    public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

// feature temporaryily disabled for security reasons    	
    	
//        DynaActionForm reportForm = (DynaActionForm)form;
//        ClientListsReportFormBean formBean = (ClientListsReportFormBean)reportForm.get("form");
//
//        Map<String, ClientListsReportResults> reportResults = demographicDao.findByReportCriteria(formBean);
//        request.setAttribute("reportResults", reportResults);

    	
        return mapping.findForward("report");
    }
}
