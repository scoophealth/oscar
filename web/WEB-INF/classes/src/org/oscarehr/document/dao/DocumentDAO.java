/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.document.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.oscarehr.document.model.CtlDocument;
import org.oscarehr.document.model.Document;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author jaygallagher
 */
public class DocumentDAO extends HibernateDaoSupport {

    public Document getDocument(String documentNo) {
        Document document = (Document) getHibernateTemplate().get(Document.class, Long.parseLong(documentNo));
        return document;
    }
    
    public void save(Document document){
        getHibernateTemplate().saveOrUpdate(document);
    }
    
    
    public CtlDocument getCtrlDocument(Integer docId) {
        List cList = null;
        Session session = null;
        try{
            session = getSession();
	    Criteria c = session.createCriteria(CtlDocument.class);
	    c=c.add(Expression.eq("id.documentNo", docId));
	    cList=c.list();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (session != null){
               releaseSession(session);
            }
        }
                
        if (cList != null && cList.size()>0){
            return (CtlDocument)cList.get(0);
        }else{
            return null;
        }
    }
    
    
    public void saveCtlDocument(CtlDocument ctlDocument){
        //System.out.println("in saveCtlDocument"+ctlDocument);
        getHibernateTemplate().update(ctlDocument);
        //System.out.println("after update ctl document");
        
    }
    
}
