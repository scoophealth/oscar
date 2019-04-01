/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.integration.cdx.dao.CdxConfigDao;
import org.oscarehr.integration.cdx.model.CdxConfig;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CDXAdminAction extends DispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String enabled = request.getParameter("cdx_polling_enabled");
        String interval = request.getParameter("cdx_polling_interval");
        String defaultProvider = request.getParameter("defaultProvider");
        String cdxOid = request.getParameter("cdxOid");
        MiscUtils.getLogger().info("action cdx_polling_enabled: " + enabled);
        MiscUtils.getLogger().info("action cdx_polling_interval: " + interval);
        MiscUtils.getLogger().info("action defaultProvider: " + defaultProvider);
        MiscUtils.getLogger().info("action cdxOid: " + cdxOid);

        try {

            CdxConfigDao cdxConfigDao = SpringUtils.getBean(CdxConfigDao.class);
            ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);
            //UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

            CdxConfig cdxConfig = cdxConfigDao.getCdxConfig(1);
            if (cdxConfig == null) {
                cdxConfig = new CdxConfig();
            }
            cdxConfig.setDefaultProvider(defaultProvider);
            cdxConfigDao.merge(cdxConfig);

            Clinic clinic = clinicDAO.getClinic();
            if (clinic == null) {  // clinic table empty, shouldn't happen
                clinic = new Clinic();
            }
            clinic.setCdxOid(cdxOid);
            clinicDAO.merge(clinic);

            int pollInterval = 30;
            if (toInteger(interval) != null) {
                pollInterval = toInteger(interval);
            }

            CDXConfiguration cdxConfiguration = new CDXConfiguration();
            cdxConfiguration.savePolling(LoggedInInfo.getLoggedInInfoFromSession(request), Boolean.valueOf(enabled), pollInterval);

            request.setAttribute("success", true);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Changing CDX Configuration failed", e);
            request.setAttribute("success", false);
        }

        return mapping.findForward("success");

    }

    private Integer toInteger(String num) {
        Integer result = null;
        try {
            result = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            // do nothing
        }
        return result;
    }
}


