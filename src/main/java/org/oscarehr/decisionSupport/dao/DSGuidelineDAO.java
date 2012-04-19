/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.dao;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author apavel
 */
public class DSGuidelineDAO extends HibernateDaoSupport {

    public DSGuidelineDAO() {
    }

    public DSGuideline getDSGuideline(String dsGuidelineId) {
        String sql ="from DSGuideline c where c.id = ?";

        @SuppressWarnings("unchecked")
        List<DSGuideline> list = getHibernateTemplate().find(sql, Integer.parseInt(dsGuidelineId));

        if (list == null || list.size() == 0){
            return null;
        }

        return list.get(0);
    }

    public DSGuideline getDSGuidelineByUUID(String uuid) {
        String sql = "from DSGuideline c where c.uuid = ? and c.status = 'A' order by c.dateStart desc";

        @SuppressWarnings("unchecked")
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

    public List<DSGuidelineProviderMapping> getMappingsByProvider(String providerNo) {

        String sql = "from DSGuidelineProviderMapping c where c.providerNo = ?";

        @SuppressWarnings("unchecked")
        List<DSGuidelineProviderMapping> list = getHibernateTemplate().find(sql, providerNo);

        if (list == null || list.size() == 0){
            return new ArrayList<DSGuidelineProviderMapping>();
        }

        return list;
    }

    public boolean mappingExists(DSGuidelineProviderMapping dsGuidelineProviderMapping) {
        String sql ="from DSGuidelineProviderMapping m where m.guideilneId = ? and m.providerNo = ?";
        String[] params = new String[2];
        params[0] = dsGuidelineProviderMapping.getGuidelineUUID();
        params[1] = dsGuidelineProviderMapping.getProviderNo();
        @SuppressWarnings("unchecked")
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
            MiscUtils.getLogger().error("Error", e);
        }

        return dsGuidelineFake;
    }
*/
    public List<DSGuideline> getDSGuidelinesByProvider(String providerNo) {
        String sql ="select c from DSGuideline c, DSGuidelineProviderMapping m where c.uuid = m.guidelineUUID and m.providerNo = ? and c.status = 'A'";
        @SuppressWarnings("unchecked")
        List<DSGuideline> list = getHibernateTemplate().find(sql, providerNo);
        return list;
    }

}
