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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public class CDXDownloadJob implements OscarRunnable {

    private static Logger logger = MiscUtils.getLogger();

    UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

    private Provider provider;
    private Security security;

    private static Boolean running = false;

    public void setLoggedInProvider(Provider provider) {
        this.provider = provider;
    }

    public void setLoggedInSecurity(Security security) {
        this.security = security;
    }

    private String getUserPropertyValueOrNull(String propName) {
        UserProperty up = userPropertyDao.getProp(propName);
        if (up != null && !StringUtils.isEmpty(up.getValue())) {
            return up.getValue();
        }
        return null;
    }

    @Override
    public void run() {

        LoggedInInfo x = new LoggedInInfo();
        x.setLoggedInProvider(provider);
        x.setLoggedInSecurity(security);

        checkObibConnection();

        importNewDocs();

        distributionStatus();
    }

    private void importNewDocs() {
        try {
            if (!running) {
                logger.info("Starting CDX Job");
                running = true;
                CDXImport cdxImport = new CDXImport();
                cdxImport.importNewDocs();
                running = false;
                logger.info("===== CDX JOB DONE RUNNING....");
            }
        } catch (Exception e) {
            logger.error("Error", e);
        } finally {
            DbConnectionFilter.releaseAllThreadDbResources();
            running = false;
        }
    }


    private void checkObibConnection() {
        try {
            if (!running) {
                logger.info("Checking OBIB Connection");
                running = true;
                CDXConfiguration.obibIsConnected();
                running = false;
                logger.info("===== Checked OBIB Connection....");
            }
        } catch (Exception e) {
            logger.error("Error", e);
        } finally {
            DbConnectionFilter.releaseAllThreadDbResources();
            running = false;
        }
    }


    private void distributionStatus() {
        try {
            if (!running) {
                logger.info("Starting CDX Distribution Job");
                running = true;
                CDXDistribution cdxDistribution = new CDXDistribution();
                cdxDistribution.updateDistributeStatus();
                running = false;
                logger.info("===== CDX DISTRIBUTION JOB DONE RUNNING....");
            }
        } catch (Exception e) {


            logger.error("Error", e);
        } finally {
            DbConnectionFilter.releaseAllThreadDbResources();
            running = false;
        }
    }

    public void setConfig(String config) {
    }

}