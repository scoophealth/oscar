/*
 *
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.List;

import org.oscarehr.common.model.UserProperty;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author rjonasz
 */
public class UserPropertyDAO extends HibernateDaoSupport {
    
    /** Creates a new instance of UserPropertyDAO */
    public UserPropertyDAO() {
    }
    
    
    public void delete(UserProperty prop) {
        this.getHibernateTemplate().delete(prop);
    }
    
    public void saveProp(UserProperty prop) {
        this.getHibernateTemplate().saveOrUpdate(prop);
    }
    
    public UserProperty getProp(String prov, String name) {
        List list = this.getHibernateTemplate().find("from UserProperty p where p.providerNo = ? and p.name = ?", new Object[] {prov,name});
        if( list != null && list.size() > 0 ) {
            UserProperty prop = (UserProperty)list.get(0);
            return prop;
        }
        else
            return null;            
    }
    
    public UserProperty getProp(String name) {
        List list = this.getHibernateTemplate().find("from UserProperty p where  p.name = ?", name);
        if( list != null && list.size() > 0 ) {
            UserProperty prop = (UserProperty)list.get(0);
            return prop;
        }
        else
            return null;            
    }
    
}
