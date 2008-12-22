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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.casemgmt.model.ClientImage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientImageDAO extends HibernateDaoSupport {
    public ClientImage getClientImage(String id, String image_type) {
        List results = getHibernateTemplate().find("from ClientImage c where c.demographic_no = ? and c.image_type = ? order by c.update_date desc", new Object[] {Long.valueOf(id), image_type});

        if (results.size() > 0) {
            return (ClientImage)results.get(0);
        }
        return null;
    }

    public void saveClientImage(ClientImage clientImage) {
        ClientImage existing = getClientImage(clientImage.getDemographic_no());
        if (existing != null) {
            existing.setImage_data(clientImage.getImage_data());
            existing.setImage_type(clientImage.getImage_type());
            existing.setUpdate_date(new Date());
        }
        getHibernateTemplate().saveOrUpdate(clientImage);
    }

    public ClientImage getClientImage(Integer clientId) {
        List results = getHibernateTemplate().find("from ClientImage i where i.demographic_no=?", clientId);
        if (results.size() > 0) {
            return (ClientImage)results.get(0);
        }
        return null;
    }
}
