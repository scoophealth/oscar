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
import java.util.ListIterator;

import javax.persistence.Query;

import org.oscarehr.common.model.JointAdmission;
import org.springframework.stereotype.Repository;

@Repository
public class JointAdmissionDao extends AbstractDao<JointAdmission> {

	public JointAdmissionDao() {
		super(JointAdmission.class);
	}
	
    public List<JointAdmission> getSpouseAndDependents(Integer clientId){
		Query query = entityManager.createQuery("SELECT x FROM JointAdmission x WHERE x.archived=0 and x.headClientId=?");
		query.setParameter(1,clientId);
		@SuppressWarnings("unchecked")
        List<JointAdmission> results = query.getResultList();
		return results;
    }
    
    public JointAdmission getJointAdmission(Integer clientId){
    	Query query = entityManager.createQuery("SELECT x FROM JointAdmission x WHERE x.archived=0 and x.clientId=?");
		query.setParameter(1,clientId);
		@SuppressWarnings("unchecked")
        List<JointAdmission> results = query.getResultList();
		
        ListIterator<JointAdmission> li = results.listIterator();
        
        if(li.hasNext()){
            return li.next();
        }else{
            return null;
        }
    }
    
    public void removeJointAdmission(Integer clientId,String providerNo){
        JointAdmission jadm = getJointAdmission(clientId);
        if(jadm != null) {
        	jadm.setArchivingProviderNo(providerNo);
        	removeJointAdmission(jadm);
        }
    }
    
    
    public void removeJointAdmission(JointAdmission admission){
    	JointAdmission tmp = find(admission.getId());
    	if(tmp != null) {
    		tmp.setArchived(true);
    		merge(tmp);
    	}
    }
    
}
