package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.Config;
import org.oscarehr.integration.cdx.dao.CdxConfigDao;
import org.oscarehr.integration.cdx.model.CdxConfig;
import org.oscarehr.util.SpringUtils;

public class CDXConfig implements Config {

 //   CdxConfigDao configDao = SpringUtils.getBean(CdxConfigDao.class);

 //   CdxConfig conf = configDao.getCdxConfig(0);

    @Override
    public String getUrl() {
        return "http://192.168.100.101:8081";
    }

    @Override
    public String getClinicId() {
        return "cdxpostprod-otca";
    }
}
