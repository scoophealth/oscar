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
 * Jason Gallagher
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.Hashtable;
import java.util.List;

import org.oscarehr.common.model.UserDSMessagePrefs;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Jason Gallagher
 */
public class UserDSMessagePrefsDAO extends HibernateDaoSupport {

    /** Creates a new instance of UserPropertyDAO */
    public UserDSMessagePrefsDAO() {
    }

    public void saveProp(UserDSMessagePrefs prop) {
        this.getHibernateTemplate().saveOrUpdate(prop);
    }

    public void updateProp(UserDSMessagePrefs prop) {
        this.getHibernateTemplate().update(prop);
    }

    public UserDSMessagePrefs getMessagePrefsOnType(String prov, String name) {
        List list = this.getHibernateTemplate().find("from UserDSMessagePrefs p where p.providerNo = ? and p.resourceType = ? and p.archived = true ", new Object[] {prov,name});
        if( list != null && list.size() > 0 ) {
            UserDSMessagePrefs prop = (UserDSMessagePrefs)list.get(0);
            return prop;
        }
        else
            return null;
    }



    public Hashtable getHashofMessages(String providerNo,String name){
        Hashtable retHash = new Hashtable();
        List<UserDSMessagePrefs> list = this.getHibernateTemplate().find("from UserDSMessagePrefs p where p.providerNo = ? and p.resourceType = ? and p.archived = true", new Object[] {providerNo,name});

        if( list != null && list.size() > 0 ) {
            for(UserDSMessagePrefs pref: list){

                retHash.put(pref.getResourceType()+pref.getResourceId(),pref.getResourceUpdatedDate().getTime());
            }
        }
        return retHash;
    }

    public UserDSMessagePrefs getDsMessage(String providerNo,String resourceType,String resourceId, boolean archived){
        UserDSMessagePrefs pref=new UserDSMessagePrefs();
        List<UserDSMessagePrefs> list = this.getHibernateTemplate().find("from UserDSMessagePrefs p where p.providerNo = ? and p.resourceType = ? and p.resourceId=?   and p.archived = ? order by p.id desc", new Object[] {providerNo,resourceType,resourceId, archived});

        if( list != null && list.size() > 0 ) {
            pref=list.get(0);// since it's desc order, most recent result = first one
        }
        return pref;
    }
}
