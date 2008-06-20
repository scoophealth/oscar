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
 * Jason Gallagher
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */

package org.oscarehr.consultationRequest.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.consultationRequest.model.ConsultationRequest;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Jason Gallagher
 */
public class ConsultationRequestDAO extends HibernateDaoSupport {
    
    /** Creates a new instance of UserPropertyDAO */
    public ConsultationRequestDAO() {
    }
    
   // public void saveProp(ConsultationRequest prop) {
    //    this.getHibernateTemplate().saveOrUpdate(prop);
   // }
    
    public List<ConsultationRequest> getReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff) {
        //select count(*) as cou from consultationRequests where  referalDate > '2002-05-11'  and status != 4 and sendto = '-1' \G

        List<ConsultationRequest> list = this.getHibernateTemplate().find("from ConsultationRequest p where p.referalDate < ? and p.status != 4 ", new Object[] {referralDateCutoff});
        return list;         
    }
    
    public List<ConsultationRequest> getReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff,String sendto) {
        //select count(*) as cou from consultationRequests where  referalDate > '2002-05-11'  and status != 4 and sendto = '-1' \G

        List<ConsultationRequest> list = this.getHibernateTemplate().find(" from ConsultationRequest p where p.referalDate < ? and p.status != 4 and sendto = ?", new Object[] {referralDateCutoff,sendto});
        return list;         
    }
    
    

    
}
