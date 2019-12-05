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

import ca.uvic.leadlab.obibconnector.facades.Config;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.support.IStatus;
import ca.uvic.leadlab.obibconnector.facades.support.ISupport;
import ca.uvic.leadlab.obibconnector.impl.support.Support;
import ca.uvic.leadlab.obibconnector.utils.OBIBConnectorHelper;
import ihe.iti.svs._2008.CD;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.OscarJobDao;
import org.oscarehr.common.dao.OscarJobTypeDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarJobUtils;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.integration.cdx.dao.NotificationDao;
import org.oscarehr.integration.cdx.model.Notification;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.util.Date;
import java.util.List;

public class CDXConfiguration implements Config {

    public CDXConfiguration() {
    }

    public void savePolling(LoggedInInfo loggedInInfo, boolean enabled, int frequencyInMinutes) {

        NotificationDao notificationDao= SpringUtils.getBean(NotificationDao.class);
        List<Notification> nList=notificationDao.findByCategory("import");
        NotificationController notificationController=new NotificationController();

        if(enabled==false) {
            if(nList!=null && nList.size()!=0 ) {
            } else {
                notificationController.insertNotifications("Warning", "Polling Disabled: System won't be able to search and import documents automatically. ", "import");
            }
        } else {
            if(nList!=null && nList.size()!=0 ) {
                notificationController.deleteNotifications("import");
             }

        }


        OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
        OscarJobTypeDao oscarJobTypeDao = SpringUtils.getBean(OscarJobTypeDao.class);

        OscarJobType oscarJobType;
        OscarJob oscarJob;





        List<OscarJobType> jobTypes = oscarJobTypeDao.findByClassName("org.oscarehr.integration.cdx.CDXDownloadJob");
        if (jobTypes.isEmpty()) {
            oscarJobType = new OscarJobType();
            oscarJobType.setClassName("org.oscarehr.integration.cdx.CDXDownloadJob");
            oscarJobType.setDescription("CDX Downloader");
            oscarJobType.setEnabled(true);
            oscarJobType.setName("CDXDownloadJobType");
            oscarJobType.setUpdated(new Date());
            oscarJobTypeDao.persist(oscarJobType);
        } else {
            oscarJobType = jobTypes.get(0);
        }

        List<OscarJob> jobs = oscarJobDao.findByType(oscarJobType);
        if (jobs.isEmpty()) {
            oscarJob = new OscarJob();
            oscarJob.setCronExpression("0 0/" + frequencyInMinutes + " * * * *");
            oscarJob.setDescription("CDX Downloader");
            oscarJob.setEnabled(enabled);
            oscarJob.setName("CDXDownloadJob");
            oscarJob.setOscarJobTypeId(oscarJobType.getId());
            oscarJob.setOscarJobType(oscarJobType);
            oscarJob.setProviderNo(loggedInInfo.getLoggedInProviderNo());
            oscarJob.setUpdated(new Date());
            oscarJobDao.persist(oscarJob);
        } else {
            oscarJob = jobs.get(0);
            oscarJob.setCronExpression("0 0/" + frequencyInMinutes + " * * * *");
            oscarJob.setEnabled(enabled);
            oscarJobDao.merge(oscarJob);
        }

        try {
            OscarJobUtils.resetJobExecutionFramework();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    private OscarJob getOscarJobForPolling() {
        OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
        OscarJobTypeDao oscarJobTypeDao = SpringUtils.getBean(OscarJobTypeDao.class);

        List<OscarJobType> jobTypes = oscarJobTypeDao.findByClassName("org.oscarehr.integration.cdx.CDXDownloadJob");

        for (OscarJobType jobType : jobTypes) {
            List<OscarJob> jobs = oscarJobDao.findByType(jobType);
            for (OscarJob job : jobs) {
                return job;
            }
        }

        return null;
    }

    public String getPollingInterval() {

        OscarJob job = getOscarJobForPolling();

        if (job != null && !StringUtils.isEmpty(job.getCronExpression())) {
            String[] expr = job.getCronExpression().split(" ");
            String val = expr[1];
            String v = val.split("/")[1];
            return v;
        }

        return "N/A";
    }

    public boolean isPollingEnabled() {

        OscarJob job = getOscarJobForPolling();

        if (job != null && job.isEnabled() && job.getOscarJobType().isEnabled() && !StringUtils.isEmpty(job.getCronExpression())) {
            return true;
        }

        return false;
    }

    public static boolean obibIsConnected() {
        CDXConfiguration cdxConfiguration = new CDXConfiguration();
        ISupport support = new Support(cdxConfiguration);
        IStatus status = null;
        try {
            status = support.checkConnectivity();

            return true;
        } catch (OBIBException ex) {
            MiscUtils.getLogger().error(ex.getMessage());

            return false;
        }
    }

    @Override
    public String getUrl() {
        String url = "http://127.0.0.1:8081";
        UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
        UserProperty up = userPropertyDao.getProp("cdx_url");
        if (up != null) url = up.getValue();
        return url;
    }

    public String getClinicId() {
        String cdxOid = OBIBConnectorHelper.getProperty("cdx.clinic.id");
        if (cdxOid.equals("")) {
            MiscUtils.getLogger().error("Error: CDX_Clinic_ID not set in properties file");
        }
        return cdxOid;
    }

}
