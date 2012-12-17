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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ReportAgeSex;
import org.springframework.stereotype.Repository;

@Repository
public class ReportAgeSexDao extends AbstractDao<ReportAgeSex>{

	public ReportAgeSexDao() {
		super(ReportAgeSex.class);
	}

    public List<ReportAgeSex> findBeforeReportDate(Date reportDate) {
    	String sql = "select x from ReportAgeSex x where x.reportDate=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,reportDate);

        @SuppressWarnings("unchecked")
        List<ReportAgeSex> results = query.getResultList();
        return results;
    }
    
    public void deleteAllByDate(Date reportDate) {
    	String sql = "delete from ReportAgeSex x where x.reportDate <= ?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,reportDate);
    	query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
    public Long count_reportagesex_roster(String roster, String sex, String providerNo, int age, Date dateStarted, Date dateEnded) {
    	String sql = "select count(x) from ReportAgeSex x where (x.status<>'OP' and x.status<>'IN' and x.status<>'DE') and x.roster=? and x.sex like ? and x.providerNo = ? and x.age >= ? and x.dateJoined >= ? and x.dateJoined <= ?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,roster);
    	query.setParameter(2,sex);
    	query.setParameter(3,providerNo);
    	query.setParameter(4,age);
    	query.setParameter(5,dateStarted);
    	query.setParameter(6,dateEnded);
       
        Long results = (Long)query.getSingleResult();
        return results;
    }
    
    public Long count_reportagesex_noroster(String roster, String sex, String providerNo,int minAge, int maxAge, Date dateStarted, Date dateEnded) {
    	String sql = "select count(x)  from ReportAgeSex x  where (x.status<>'OP' and x.status<>'IN' and x.status<>'DE') and x.roster<>? and x.sex like ? and x.providerNo=? and x.age >= ? and x.age <=? and x.dateJoined >=? and x.dateJoined <=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,roster);
    	query.setParameter(2,sex);
    	query.setParameter(3,providerNo);
    	query.setParameter(4,minAge);
    	query.setParameter(5,maxAge);
    	query.setParameter(6,dateStarted);
    	query.setParameter(7,dateEnded);
       
        Long results = (Long)query.getSingleResult();
        return results;
    }
    
    public Long count_reportagesex(String roster, String sex, String providerNo, int minAge, int maxAge, Date startDate, Date endDate) {
    	String sql = "select count(x) from ReportAgeSex x where (x.status<>'OP' and x.status<>'IN' and x.status<>'DE') and x.roster like ? and x.sex like ? and x.providerNo=? and x.age >= ? and x.age <=? and x.dateJoined >=? and x.dateJoined <=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,roster);
    	query.setParameter(2,sex);
    	query.setParameter(3,providerNo);
    	query.setParameter(4,minAge);
    	query.setParameter(5,maxAge);
    	query.setParameter(6,startDate);
    	query.setParameter(7,endDate);
       
        Long results = (Long)query.getSingleResult();
        return results;
    }
}
