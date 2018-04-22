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

import org.oscarehr.common.model.ScheduleDate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class ScheduleDateDao extends AbstractDao<ScheduleDate>{

	public ScheduleDateDao() {
		super(ScheduleDate.class);
	}

	public ScheduleDate findByProviderNoAndDate(String providerNo, Date date) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.date=? and s.status=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, 'A');

		return(getSingleResultOrNull(query));
	}

	public List<ScheduleDate> findByProviderPriorityAndDateRange(String providerNo, char priority, Date date, Date date2) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.priority=? and s.date>=? and s.date <=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, priority);
		query.setParameter(3, date);
		query.setParameter(4, date2);

		
        List<ScheduleDate> results = query.getResultList();
		return results;
	}

	public List<ScheduleDate> findByProviderAndDateRange(String providerNo, Date date, Date date2) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.date>=? and s.date <=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, date2);

		
        List<ScheduleDate> results = query.getResultList();
		return results;
	}

	public List<ScheduleDate> search_scheduledate_c(String providerNo) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.priority='c' and s.status = 'A' and s.providerNo=?");
		query.setParameter(1, providerNo);
		
		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}
	
	public List<ScheduleDate> search_numgrpscheduledate(String myGroupNo, Date sDate) {
		Query query = entityManager.createQuery("select s from MyGroup m, ScheduleDate s where m.id.myGroupNo = ? and s.date=? and m.id.providerNo = s.providerNo and s.available = '1' and s.status='A'");
		query.setParameter(1, myGroupNo);
		query.setParameter(2, sDate);
		
		
		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}
	
	public List<Object[]> search_appttimecode(Date sDate, String providerNo) {
		Query query = entityManager.createQuery("FROM ScheduleTemplate st, ScheduleDate sd WHERE st.id.name=sd.hour and sd.date=? and sd.providerNo=? and sd.status='A' and (st.id.providerNo = sd.providerNo or st.id.providerNo='Public')");
		query.setParameter(1, sDate);
		query.setParameter(2, providerNo);
		
		
		@SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
		return results;
	}
	
	public List<ScheduleDate> search_scheduledate_teamp(Date date, Date date2, String status, List<String> providerNos) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.date>=:sdate and s.date <=:edate and s.status=:status and s.providerNo in (:providers) order by s.date");
		query.setParameter("sdate", date);
		query.setParameter("edate", date2);
		query.setParameter("status", status);
		query.setParameter("providers", providerNos);

		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}
	
	public List<ScheduleDate> search_scheduledate_datep(Date date, Date date2, String status) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.date>=:sdate and s.date <=:edate and s.status=:status  order by s.date");
		query.setParameter("sdate", date);
		query.setParameter("edate", date2);
		query.setParameter("status", status);
	
		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}
	
	public List<ScheduleDate> findActiveByProviderAndHour(String providerNo, String hour) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.status = ? and s.hour = ? order by s.date ASC");
		query.setParameter(1, providerNo);
		query.setParameter(2, 'A');
		query.setParameter(3, hour);
	
		return query.getResultList();
	}
	

	public List<ScheduleDate> findByProviderStartDateAndPriority(String providerNo, Date apptDate, String priority) {
		Query query = createQuery("sd", "sd.date = :apptDate AND sd.providerNo = :providerNo AND sd.priority = :priority");
		query.setParameter("providerNo", providerNo);
		query.setParameter("apptDate", apptDate);
		query.setParameter("priority", priority);
		return query.getResultList();
    }
}
