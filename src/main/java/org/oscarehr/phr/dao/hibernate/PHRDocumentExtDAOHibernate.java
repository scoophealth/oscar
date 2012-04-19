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


package org.oscarehr.phr.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.phr.dao.PHRDocumentExtDAO;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRDocumentExt;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author jay
 */
public class PHRDocumentExtDAOHibernate extends HibernateDaoSupport
        implements PHRDocumentExtDAO {
    
    private static Logger log = MiscUtils.getLogger();
    
    /**
     * Creates a new instance of PHRDocumentDAOHibernate
     */
    public PHRDocumentExtDAOHibernate() {
    }
    
    public void save(PHRDocumentExt doc) {
        this.getHibernateTemplate().save(doc);
    }
    
    public int findPHRDocumentIdByKeyValue(String key,String value){
        String sql ="from PHRDocumentExt d where d.key = ? and value= ?";
        String[] f = new String[2];
        f[0] = new String(key);
        f[1] = new String(value);
        
        List<PHRDocument> list = getHibernateTemplate().find(sql,f);
        
        if (list != null && list.size() > 0){
            return list.get(0).getId();
        }
        return -1;
    }
    
    public List getDocumentsExt(int id){
        String sql ="from PHRDocumentExt d where d.phrDocumentId = ? ";
        List<PHRDocumentExt> list = getHibernateTemplate().find(sql,""+id);
        return list;
    }
    
    
    
    
    
    
}
