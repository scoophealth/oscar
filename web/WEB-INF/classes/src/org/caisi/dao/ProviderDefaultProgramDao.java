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
package org.caisi.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.caisi.model.ProviderDefaultProgram;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderDefaultProgramDao extends HibernateDaoSupport {
    private static final Logger logger = Logger.getLogger(ProviderDefaultProgramDao.class);

    public List getProgramByProviderNo(String providerNo) {
        String q = "FROM ProviderDefaultProgram pdp WHERE pdp.providerNo=?";
        List rs = (List)getHibernateTemplate().find(q, providerNo);
        return rs;
    }

    public void setDefaultProgram(String providerNo, int programId) {
        List rs = getProgramByProviderNo(providerNo);
        ProviderDefaultProgram pdp;
        if (rs.size() == 0) {
            pdp = new ProviderDefaultProgram();
            pdp.setProviderNo(providerNo);
            pdp.setSignnote(false);
        }
        else {
            pdp = (ProviderDefaultProgram)rs.get(0);
        }
        pdp.setProgramId(new Integer(programId));
        getHibernateTemplate().saveOrUpdate(pdp);
    }

    public List getProviderSig(String providerNo) {
        List rs = (List)getProgramByProviderNo(providerNo);
        return rs;
    }

    public void saveProviderDefaultProgram(ProviderDefaultProgram pdp) {
        getHibernateTemplate().saveOrUpdate(pdp);

    }

    public void toggleSig(String providerNo) {
        List list = getProgramByProviderNo(providerNo);
        ProviderDefaultProgram pdp = null;
        if (list.isEmpty()) {
            pdp = new ProviderDefaultProgram();
            pdp.setProgramId(new Integer(0));
            pdp.setProviderNo(providerNo);
            pdp.setSignnote(false);
        }
        else {
            pdp = (ProviderDefaultProgram)list.get(0);
            pdp.setSignnote(!pdp.isSignnote());
        }
        saveProviderDefaultProgram(pdp);
    }

}
