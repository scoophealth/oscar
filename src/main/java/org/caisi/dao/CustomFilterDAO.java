/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.dao;

import java.util.List;

import org.caisi.model.CustomFilter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class CustomFilterDAO extends HibernateDaoSupport {

    public CustomFilter getCustomFilter(String name) {
        List results = getHibernateTemplate().find("from CustomFilter c where c.name = ?", new Object[] {name});
        if (results.size() > 0) {
            return (CustomFilter)results.get(0);
        }
        else {
            return null;
        }
    }

    public CustomFilter getCustomFilter(String name, String providerNo) {
        List results = getHibernateTemplate().find("from CustomFilter c where c.name = ? and c.providerNo = ?", new Object[] {name, providerNo});
        if (results.size() > 0) {
            return (CustomFilter)results.get(0);
        }
        else {
            return null;
        }

    }

    public CustomFilter getCustomFilterById(Integer id) {
        Long idLong = id.longValue();
        List results = getHibernateTemplate().find("from CustomFilter c where c.id=?", new Object[] {idLong});
        if (results.size() > 0) {
            return (CustomFilter)results.get(0);
        }
        else return null;
    }

    public void saveCustomFilter(CustomFilter filter) {
        getHibernateTemplate().saveOrUpdate(filter);
    }

    public List getCustomFilters() {
        return getHibernateTemplate().find("from CustomFilter");
    }

    public List getCustomFilters(String provider_no) {
        return getHibernateTemplate().find("from CustomFilter cf where cf.providerNo = ?", new Object[] {provider_no});
    }

    public List getCustomFilterWithShortCut(String providerNo){
        return getHibernateTemplate().find("from CustomFilter c where c.providerNo = ? and c.shortcut = true",new Object[]{providerNo});
    }
    
    public void deleteCustomFilter(String name) {
        CustomFilter filter = getCustomFilter(name);
        getHibernateTemplate().delete(filter);
    }

    public void deleteCustomFilterById(Integer id) {
        CustomFilter filter = getCustomFilterById(id);
        getHibernateTemplate().delete(filter);
    }
}
