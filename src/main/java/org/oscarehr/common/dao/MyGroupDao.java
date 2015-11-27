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

package org.oscarehr.common.dao;



import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.MyGroup;
import org.oscarehr.common.model.MyGroupPrimaryKey;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Toby
 */
@Repository
public class MyGroupDao extends AbstractDao<MyGroup> {

	public MyGroupDao() {
		super(MyGroup.class);
	}

	@SuppressWarnings("unchecked")
	public List<MyGroup> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}
	
     public List<String> getGroupDoctors (String groupNo){

        Query query = entityManager.createQuery("SELECT g.id.providerNo FROM MyGroup g WHERE g.id.myGroupNo=?");
        query.setParameter(1, groupNo);

        @SuppressWarnings("unchecked")
        List<String> dList = query.getResultList();

        if (dList != null && dList.size() > 0) {
            return dList;
        } else {
            return null;
        }
     }

     public List<String> getGroups(){
    	 Query query = entityManager.createQuery("SELECT distinct g.id.myGroupNo FROM MyGroup g order by g.id.myGroupNo");

         @SuppressWarnings("unchecked")
         List<String> dList = query.getResultList();

         return dList;
     }
     
     public List<MyGroup> getGroupByGroupNo(String groupNo) {
         Query query = entityManager.createQuery("SELECT g FROM MyGroup g where g.id.myGroupNo = ?");
         query.setParameter(1, groupNo);
         
         @SuppressWarnings("unchecked")
         List<MyGroup> dList = query.getResultList();

         return dList;
     }

     public void deleteGroupMember(String myGroupNo, String providerNo){
    	 MyGroupPrimaryKey key = new MyGroupPrimaryKey();
    	 key.setMyGroupNo(myGroupNo);
    	 key.setProviderNo(providerNo);
    	 remove(key);
     }
     
     public List<MyGroup> getProviderGroups(String providerNo) {
         Query query = entityManager.createQuery("SELECT g FROM MyGroup g WHERE g.id.providerNo = ?");
         query.setParameter(1, providerNo);
         
         @SuppressWarnings("unchecked")
         List<MyGroup> dList = query.getResultList();

         return dList;
     }
     
     public String getDefaultBillingForm(String myGroupNo) {
         Query query = entityManager.createQuery("SELECT distinct g.defaultBillingForm FROM MyGroup g WHERE g.id.myGroupNo = ?");
         query.setParameter(1, myGroupNo);
         
         @SuppressWarnings("unchecked")
         List<String> dList = query.getResultList();         

         if (dList.size() > 1)
             MiscUtils.getLogger().warn("More than one Default biling form for this group. Should only be one");
         String billingForm = "";         
         if (dList != null && !dList.isEmpty())
             billingForm = dList.get(0);
         return billingForm;
     }
     
     public List<Provider> search_groupprovider (String groupNo){

         Query query = entityManager.createQuery("SELECT p  FROM MyGroup g, Provider p WHERE g.id.myGroupNo=? and p.ProviderNo = g.id.providerNo order by p.LastName");
         query.setParameter(1, groupNo);

         @SuppressWarnings("unchecked")
         List<Provider> dList = query.getResultList();

         return dList;
      }
     
     public List<MyGroup> search_mygroup(String groupNo) {
         Query query = entityManager.createQuery("SELECT g FROM MyGroup g WHERE g.id.myGroupNo like ? group by g.id.myGroupNo order by g.id.myGroupNo");
         query.setParameter(1, groupNo);
         
         @SuppressWarnings("unchecked")
         List<MyGroup> dList = query.getResultList();

         return dList;
     }
     
 
     public List<MyGroup> searchmygroupno() {
         Query query = entityManager.createQuery("SELECT g FROM MyGroup g group by g.id.myGroupNo order by g.id.myGroupNo");
         
         @SuppressWarnings("unchecked")
         List<MyGroup> dList = query.getResultList();

         return dList;
     }
     
     public List<MyGroup> search_providersgroup(String lastName, String firstName) {
         Query query = entityManager.createQuery("SELECT g FROM MyGroup g where g.lastName like ? and g.firstName like ? order by g.lastName, g.firstName, g.id.myGroupNo");
         query.setParameter(1, lastName);
         query.setParameter(2, firstName);
         
         @SuppressWarnings("unchecked")
         List<MyGroup> dList = query.getResultList();

         return dList;
     }
     
}
