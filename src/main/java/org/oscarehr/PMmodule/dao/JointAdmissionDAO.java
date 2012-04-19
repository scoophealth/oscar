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


package org.oscarehr.PMmodule.dao;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.JointAdmission;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author jay
 */
public class JointAdmissionDAO extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();
    
    /** Creates a new instance of JointAdmissionDAO */
    public JointAdmissionDAO() {
    }

     public void saveJointAdmission(JointAdmission admission) {
        if (admission == null) {
            throw new IllegalArgumentException();
        }

        getHibernateTemplate().saveOrUpdate(admission);
        getHibernateTemplate().flush();

        if (log.isDebugEnabled()) {
            log.debug("saveJointAdmission: id= " + admission.getId());
        }
    }
     
    public List<JointAdmission> getSpouseAndDependents(Long clientId){
       
        String queryStr = "FROM JointAdmission a where Archived = 0 and HeadClientId ="+clientId;
        List rs = getHibernateTemplate().find(queryStr);

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions # of admissions: " + rs.size());
        }

        return rs;
    }
    
    public JointAdmission getJointAdmission(Long clientId){
       
        String queryStr = "FROM JointAdmission a where Archived = 0 and ClientId ="+clientId;
        List<JointAdmission> rs = getHibernateTemplate().find(queryStr);

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions # of admissions: " + rs.size());
        }
        ListIterator<JointAdmission> li = rs.listIterator();
        
        if(li.hasNext()){
            return li.next();
        }else{
            return null;
        }
    }
    
    public void removeJointAdmission(Long clientId,String providerNo){
        JointAdmission jadm = getJointAdmission(clientId);
        jadm.setArchivingProviderNo(providerNo);
        removeJointAdmission(jadm);
    }
    
    
    public void removeJointAdmission(JointAdmission admission){
       if (admission == null) {
            throw new IllegalArgumentException();
       }
       admission.setArchived(true);
       getHibernateTemplate().update(admission);
       getHibernateTemplate().flush();

       
    }
       
     
    
}
