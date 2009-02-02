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
package org.oscarehr.common.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.oscarehr.common.model.ProviderInboxItem;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import oscar.oscarDB.DBHandler;

/**
 *
 * @author jay gallagher
 */
public class ProviderInboxRoutingDao extends HibernateDaoSupport {
    
    public List getProvidersWithRoutingForDocument(String docType,String docId){
        int dId = Integer.parseInt(docId);
        List providers = this.getHibernateTemplate().find("from ProviderInboxItem where labType = ? and labNo = ?",new Object[] {docType,dId});
        return providers;
    }
    
    public boolean hasProviderBeenLinkedWithDocument(String docType,String docId,String providerNo){
        int dId = Integer.parseInt(docId);
        int count = (Integer)  this.getHibernateTemplate().find("from ProviderInboxItem where labType = ? and labNo = ?",new Object[] {docType,dId,providerNo}).get(0);       
        System.out.println("Number of provider links for prov "+providerNo+" id "+docId+" count "+count);
        
        if (count > 0){
            return true;
        }
        return false;
    }
    
    public void addToProviderInbox(String providerNo,String labNo,String labType){
        System.out.println("Add to PRoviderInbox");
        
        ArrayList<String> listofAdditionalProviders = new ArrayList();
        boolean fileForMainProvider = false;
        //TODO:Replace 
        try{
           DBHandler dbh = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs= dbh.GetSQL("select * from incomingLabRules where archive = 0 and provider_no = '"+providerNo+"'");   
           while(rs.next()){
              String status = rs.getString("status");
              String frwdProvider = rs.getString("frwdProvider_no");
              listofAdditionalProviders.add(frwdProvider);
              if (status != null && status.equals("F")){
                  fileForMainProvider = true;
              }
           }
           
           ProviderInboxItem p = new ProviderInboxItem();
           p.setProviderNo(providerNo);
           p.setLabNo(labNo);
           p.setLabType(labType);
           if (fileForMainProvider){
               p.setStatus(ProviderInboxItem.FILE);
           }else{
               p.setStatus(ProviderInboxItem.NEW);
           }
           if (!hasProviderBeenLinkedWithDocument(labType,labNo,providerNo)){
              this.getHibernateTemplate().save(p);
           }
           for (String s:listofAdditionalProviders){
               if (!hasProviderBeenLinkedWithDocument(labType,labNo,s)){
                   p = new ProviderInboxItem();
                   p.setProviderNo(s);
                   p.setLabNo(labNo);
                   p.setLabType(labType);
                   if (fileForMainProvider){
                      p.setStatus(ProviderInboxItem.FILE);
                   }else{
                      p.setStatus(ProviderInboxItem.NEW);
                   }
                   this.getHibernateTemplate().save(p);
               }
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }
        
       
       
    }
          
    
    

}
