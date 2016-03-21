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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ConsultationServices;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rjonasz
 */
@Repository
public class ConsultationServiceDao extends AbstractDao<ConsultationServices> {
	public final String REFERRING_DOCTOR = "Referring Doctor";
    public final String ACTIVE = "1";
    public final String INACTIVE = "02";
    public final boolean ACTIVE_ONLY = true;
    public final boolean WITH_INACTIVE = false;
	
    public ConsultationServiceDao() {
        super(ConsultationServices.class);
    }

    public List<ConsultationServices> findAll() {
    	String sql = "select x from ConsultationServices x order by x.serviceDesc";
    	Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<ConsultationServices> results = query.getResultList();
        return results;
    }

    public List<ConsultationServices> findActive() {
    	String sql = "select x from ConsultationServices x where x.active=? order by x.serviceDesc";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,"1");

        @SuppressWarnings("unchecked")
        List<ConsultationServices> results = query.getResultList();
        return results;
    }
    
    public List<ConsultationServices> findActiveNames() {
    	String sql = "select x.serviceId,x.serviceDesc from ConsultationServices x where x.active=? order by x.serviceDesc";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,"1");

        @SuppressWarnings("unchecked")
        List<ConsultationServices> results = query.getResultList();
        return results;
    }
    
    
    public ConsultationServices findByDescription(String description) {
    	String sql = "select x from ConsultationServices x where x.active=? and x.serviceDesc = ?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,"1");
    	query.setParameter(2, description);
    	
        return this.getSingleResultOrNull(query);
    }

    public ConsultationServices findReferringDoctorService(boolean activeOnly) {
    	String sql = "select x from ConsultationServices x where x.serviceDesc=?";
    	if (activeOnly) sql += " and x.active=?";
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, REFERRING_DOCTOR);
    	if (activeOnly) query.setParameter(2, ACTIVE);

        @SuppressWarnings("unchecked")
        List<ConsultationServices> results = query.getResultList();
        
        //filter out the first active one if multiple services are found
        if (results!=null && results.size()>1) {
        	for (ConsultationServices cs : results) {
        		if (cs.getActive().equals(ACTIVE)) {
        			results = new ArrayList<ConsultationServices>();
        			results.add(cs);
        			break;
        		}
        	}
        }
        return (results==null || results.isEmpty()) ? null : results.get(0);
    }
}
