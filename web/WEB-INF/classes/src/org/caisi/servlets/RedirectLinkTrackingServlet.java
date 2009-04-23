/*
* Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
* CAISI, 
* Toronto, Ontario, Canada 
*/

package org.caisi.servlets;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.RedirectLinkDao;
import org.oscarehr.common.dao.RedirectLinkTrackingDao;
import org.oscarehr.common.model.RedirectLink;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;

/**
 */
public class RedirectLinkTrackingServlet extends javax.servlet.http.HttpServlet {
    private static final Logger logger = LogManager.getLogger(RedirectLinkTrackingServlet.class);

    private static final Timer redirectLinkTrackingTimer = new Timer(RedirectLinkTrackingServlet.class.getName(), true);

    private static final long REDIRECT_CLEANING_PERIOD = DateUtils.MILLIS_PER_HOUR * 6;
    private static long dataRetentionTimeMillis = -1;
    private static RedirectCleaningTimerTask redirectCleaningTimerTask = null;
    private static RedirectLinkTrackingDao redirectLinkTrackingDao = null;
    private static RedirectLinkDao redirectLinkDao = null;

    public static class RedirectCleaningTimerTask extends TimerTask {
        public void run() {
            logger.debug("RedirectCleaningTimerTask timerTask started.");

    		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();

    		try {
                if (dataRetentionTimeMillis == -1) return;

                // delete old redirect entries
                redirectLinkTrackingDao.deleteOldEntries(new Date(System.currentTimeMillis() - dataRetentionTimeMillis));
            }
            catch (Exception e) {
                logger.error("Unexpected error flushing html open queue.", e);
            }
            finally {
    			LoggedInInfo.loggedInInfo.remove();
                DbConnectionFilter.releaseThreadLocalDbConnection();
            }

            logger.debug("RedirectCleaningTimerTask timerTask completed.");
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        // yes I know I'm setting static variables in an instance method but it's okay.
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        redirectLinkTrackingDao = (RedirectLinkTrackingDao)webApplicationContext.getBean("redirectLinkTrackingDao");
        redirectLinkDao = (RedirectLinkDao)webApplicationContext.getBean("redirectLinkDao");

        logger.info("REDIRECT_CLEANING_PERIOD=" + REDIRECT_CLEANING_PERIOD);

        String temp = StringUtils.trimToNull(OscarProperties.getInstance().getProperty("REDIRECT_TRACKING_DATA_RETENTION_MILLIS"));
        if (temp != null) {
            dataRetentionTimeMillis = Long.parseLong(temp);
        }

        redirectCleaningTimerTask = new RedirectCleaningTimerTask();
        redirectLinkTrackingTimer.scheduleAtFixedRate(redirectCleaningTimerTask, REDIRECT_CLEANING_PERIOD, REDIRECT_CLEANING_PERIOD);
    }

    public void destroy() {
        redirectCleaningTimerTask.cancel();
        redirectLinkTrackingTimer.cancel();

        super.destroy();
    }

    /**
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        try {
            // lookup redirectLink
            int redirectLinkId = -1;
            try {
                redirectLinkId = Integer.parseInt(request.getParameter("redirectLinkId"));
            }
            catch (NullPointerException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            RedirectLink redirectLink = redirectLinkDao.find(redirectLinkId);
            if (redirectLink == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // get provider
            String providerId = (String)request.getSession().getAttribute("user");

            // track redirect
            redirectLinkTrackingDao.addInstanceOfRedirect(new Date(), providerId, redirectLinkId);

            // send redirect
            response.sendRedirect(redirectLink.getUrl());
        }
        catch (Exception e) {
            logger.error("Error processing request. " + request.getRequestURL() + ", " + e.getMessage());
            logger.debug(e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }
    }

}
