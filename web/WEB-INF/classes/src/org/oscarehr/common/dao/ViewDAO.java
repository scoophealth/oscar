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
 * ViewDAO.java
 *
 * Created on March 9, 2009, 9:28 AM
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.model.View;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author rjonasz
 */
public class ViewDAO extends HibernateDaoSupport {
    
    /**
     * Creates a new instance of ViewDAO
     */
    public ViewDAO() {
    }
    
    public Map<String, View> getView(String view, String role) {
        List<View> list = this.getHibernateTemplate().find("from View v where v.view_name = ? and v.role = ?", new Object[] {view,role});
        Map<String,View>map = new HashMap<String,View>();
        
        for( View v : list ) {
            map.put(v.getName(),v);
        }
        
        return map;
    }
    
    public void saveView(View v) {
        this.getHibernateTemplate().saveOrUpdate(v);
    }
    
    public void delete(View v) {
        this.getHibernateTemplate().delete(v);
    }
    
}
