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
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.util.ConversionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public class CDXAdminAction extends DispatchAction {

    public ActionForward saveConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String enabled = request.getParameter("cdx_polling_enabled");
        String interval = request.getParameter("cdx_polling_interval");
        String cdxUrl = request.getParameter("cdx_url");
        MiscUtils.getLogger().info("action cdx_polling_enabled: " + enabled);
        MiscUtils.getLogger().info("action cdx_polling_interval: " + interval);
        MiscUtils.getLogger().info("action cdx_url: " + cdxUrl);

        try {
            ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);
            UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

            UserProperty prop;
            if ((prop = userPropertyDao.getProp("cdx_url")) == null ) {
                prop = new UserProperty();
            }
            prop.setName("cdx_url");
            prop.setValue(cdxUrl);
            userPropertyDao.saveProp(prop);

            Clinic clinic = clinicDAO.getClinic();
            if (clinic == null) {  // clinic table empty, shouldn't happen
                clinic = new Clinic();
            }
            clinicDAO.merge(clinic);

            int pollInterval = 30;
            if (toInteger(interval) != null) {
                pollInterval = toInteger(interval);
            }

            CDXConfiguration cdxConfiguration = new CDXConfiguration();
            cdxConfiguration.savePolling(LoggedInInfo.getLoggedInInfoFromSession(request), Boolean.parseBoolean(enabled), pollInterval);

            request.setAttribute("success", true);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Changing CDX Configuration failed", e);
            request.setAttribute("success", false);
        }

        return mapping.findForward("success");

    }

    public ActionForward searchDocs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Get request parameters
        String startDateStr = request.getParameter("cdx_search_date_start");
        String endDateStr = request.getParameter("cdx_search_date_end");
        MiscUtils.getLogger().info("action cdx_search_date_start: " + startDateStr);
        MiscUtils.getLogger().info("action cdx_search_date_end: " + endDateStr);

        // Adjust Dates
        Date startDate = ConversionUtils.fromDateString(startDateStr);
        Date endDate = ConversionUtils.fromDateString(endDateStr);
        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) { // Swap dates to avoid query issues
                long starLong = startDate.getTime();
                startDate = new Date(endDate.getTime());
                endDate = new Date(starLong);
            }
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            endDate = endCal.getTime(); // add time to endDate

            // Return Date attributes
            request.setAttribute("cdx_search_date_start", startDate); // yyyy-MM-dd 00:00:00
            request.setAttribute("cdx_search_date_end", endDate); // yyyy-MM-dd 23:59:59
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


