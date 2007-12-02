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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.model.CaisiEditor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaisiEditorDAO extends HibernateDaoSupport {
    private Log log = LogFactory.getLog(CaisiEditorDAO.class);

    public List getCaisiEditors() {
        return getHibernateTemplate().find("from CaisiEditor");
    }

    public CaisiEditor getCaisiEditor(Long id) {
        return (CaisiEditor)getHibernateTemplate().get(CaisiEditor.class, id);
    }

    public void saveCaisiEditor(CaisiEditor CaisiEditor) {
        getHibernateTemplate().saveOrUpdate(CaisiEditor);

        if (log.isDebugEnabled()) {
            log.debug("CaisiEditorId set to:" + CaisiEditor.getId());
        }
    }

    public void removeCaisiEditor(Long id) {
        Object CaisiEditor = getHibernateTemplate().load(CaisiEditor.class, id);
        getHibernateTemplate().delete(CaisiEditor);
    }
}
