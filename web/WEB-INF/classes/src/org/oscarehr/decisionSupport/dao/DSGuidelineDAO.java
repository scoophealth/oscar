/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author apavel
 */
public class DSGuidelineDAO extends HibernateDaoSupport {
    private static Log _log = LogFactory.getLog(DSGuidelineDAO.class);

    public DSGuidelineDAO() {
    }

    public DSGuideline getDSGuideline(String dsGuidelineId) {
        String sql ="from DSGuideline c where c.id = ?";
            
        List<DSGuideline> list = getHibernateTemplate().find(sql, Integer.parseInt(dsGuidelineId));
            
        if (list == null || list.size() == 0){
            return null;
        }
            
        return list.get(0);
    }

    public DSGuideline getDSGuidelineByUUID(String uuid) {
        String sql = "from DSGuideline c where c.uuid = ? and c.status = 'A' order by c.dateStart desc";

        List<DSGuideline> list = getHibernateTemplate().find(sql, uuid);

        if (list == null || list.size() == 0){
            return null;
        }

        return list.get(0);
    }

    public DSGuideline save(DSGuideline dsGuideline) {
        this.getHibernateTemplate().save(dsGuideline);
        this.getHibernateTemplate().refresh(dsGuideline);
        return dsGuideline;
	}

    public void update(DSGuideline dsGuideline) {
        this.getHibernateTemplate().update(dsGuideline);
    }

    public void save(DSGuidelineProviderMapping dsGuidelineProviderMapping) {
        this.getHibernateTemplate().save(dsGuidelineProviderMapping);
	}

    public void delete(DSGuidelineProviderMapping dsGuidelineProviderMapping) {
        this.getHibernateTemplate().delete(dsGuidelineProviderMapping);
    }

    public List<DSGuidelineProviderMapping> getMappingsByProvider(String providerNo) {;

        String sql = "from DSGuidelineProviderMapping c where c.providerNo = ?";

        List<DSGuidelineProviderMapping> list = getHibernateTemplate().find(sql, providerNo);

        if (list == null || list.size() == 0){
            return new ArrayList();
        }

        return list;
    }

    public boolean mappingExists(DSGuidelineProviderMapping dsGuidelineProviderMapping) {
        String sql ="from DSGuidelineProviderMapping m where m.guideilneId = ? and m.providerNo = ?";
        String[] params = new String[2];
        params[0] = dsGuidelineProviderMapping.getGuidelineUUID();
        params[1] = dsGuidelineProviderMapping.getProviderNo();
        List<DSGuideline> list = getHibernateTemplate().find(sql, params);

        if (list == null || list.size() == 0){
            return false;
        }

        return true;
    }
 
 /*   public DSGuideline getDSGuideline(String dsGuidelineId) {
        DSGuideline dsGuidelineFake = new DSGuidelineDrools();
        dsGuidelineFake.setId(1);
        dsGuidelineFake.setProviderNo("999998");
        //dsGuidelineFake.setTitle("Plavix Drug DS");
        try {
            File file = new File("/home/apavel/testXml.xml");
            String xmlString = FileUtils.readFileToString(file);
            dsGuidelineFake.setXml(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsGuidelineFake;
    }
*/
    public List<DSGuideline> getDSGuidelinesByProvider(String providerNo) {
        String sql ="select c from DSGuideline c, DSGuidelineProviderMapping m where c.uuid = m.guidelineUUID and m.providerNo = ? and c.status = 'A'";
        List<DSGuideline> list = getHibernateTemplate().find(sql, providerNo);
        return list;
    }

}
