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


package org.oscarehr.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.document.model.CtlDocument;
import org.oscarehr.document.model.Document;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 *
 * @author jaygallagher
 */
public class DocumentDAO extends HibernateDaoSupport {

	private static Logger log = MiscUtils.getLogger();

    public Document getDocument(String documentNo) {
        Document document = getHibernateTemplate().get(Document.class, Long.parseLong(documentNo));
        return document;
    }

    public void save(Document document){
    	document.setUpdatedatetime(new Date());
        getHibernateTemplate().saveOrUpdate(document);
    }

    public Demographic getDemoFromDocNo(String docNo){//return null if no demographic linked to this document
        Demographic d=null;
        Integer i=Integer.parseInt(docNo);
        String q="select d from Demographic d, CtlDocument c where c.id.module='demographic'"
                         + " and c.moduleId!='-1' and c.moduleId=d.DemographicNo and c.id.documentNo=? ";
        List rs=getHibernateTemplate().find(q,i);
        if(rs.size()>0){
            d=(Demographic)rs.get(0);
        }
        return d;
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
            MiscUtils.getLogger().error("Error", e);
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

        getHibernateTemplate().saveOrUpdate(ctlDocument);

    }

    public int getNumberOfDocumentsAttachedToAProviderDemographics(String providerNo,Date startDate,Date endDate){
       	String sql = "select count(*) from ctl_document c, demographic d,document doc where c.module_id = d.demographic_no and c.document_no = doc.document_no   and d.provider_no = ? and doc.observationdate >= ? and doc.observationdate <= ? ";
       	int ret = 0;
		Connection c = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, providerNo);
			ps.setTimestamp(2, new Timestamp(startDate.getTime()));
			ps.setTimestamp(3, new Timestamp(endDate.getTime()));

			ResultSet rs = ps.executeQuery();
			if(rs.next()){
			   ret=rs.getInt(1);
			}
		}catch(Exception e){
			log.error("Error counting documents for provider :"+providerNo,e);
		}
		return ret;


    }

    public void subtractPages(String documentNo, Integer i) {
    	Document doc = getDocument(documentNo);
    	doc.setNumberOfPages(doc.getNumberOfPages()-i);
    	save(doc);
    }

}
