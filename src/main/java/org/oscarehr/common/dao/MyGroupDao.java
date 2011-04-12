/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.MyGroup;
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
    	 Query query = entityManager.createQuery("SELECT distinct g.id.myGroupNo FROM MyGroup g");
         
         @SuppressWarnings("unchecked")
         List<String> dList = query.getResultList();    
         
         return dList;
     }
}
