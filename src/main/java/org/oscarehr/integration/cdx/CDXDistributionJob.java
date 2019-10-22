package org.oscarehr.integration.cdx;

import org.apache.log4j.Logger;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class CDXDistributionJob implements OscarRunnable {

    private static Logger logger = MiscUtils.getLogger();

    private Provider provider;
    private Security security;

    private static Boolean running = false;

    @Override
    public void setLoggedInProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public void setLoggedInSecurity(Security security) {
        this.security = security;
    }

    @Override
    public void setConfig(String config) {

    }

    @Override
    public void run() {
        LoggedInInfo x = new LoggedInInfo();
        x.setLoggedInProvider(provider);
        x.setLoggedInSecurity(security);

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
}
