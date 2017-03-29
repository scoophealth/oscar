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

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.ScheduleTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class ScheduleTemplateDao extends AbstractDao<ScheduleTemplate> {
	
	public ScheduleTemplateDao() {
		super(ScheduleTemplate.class);
	}
	
	public List<ScheduleTemplate> findBySummary(String summary) {
		Query query = entityManager.createQuery("SELECT e FROM ScheduleTemplate e WHERE e.summary=? ");
		query.setParameter(1, summary);

        List<ScheduleTemplate> results = query.getResultList();
		return results;
	}

	public List<Object[]> findSchedules(Date date_from, Date date_to, String provider_no) {
	    String sql = "FROM ScheduleTemplate st, ScheduleDate sd " +
        		"WHERE st.id.name = sd.hour " +
        		"AND sd.date >= :date_from " + 
        		"AND sd.date <= :date_to " +
        		"AND sd.providerNo = :provider_no " +
        		"AND sd.status = 'A' " +
        		"AND (" +
        		"	st.id.providerNo = sd.providerNo " +
        		"	OR st.id.providerNo = 'Public' " +
        		") ORDER BY sd.date";
		Query query = entityManager.createQuery(sql);
		query.setParameter("date_from", date_from);
		query.setParameter("date_to", date_to);
		query.setParameter("provider_no", provider_no);
		return query.getResultList();
    }

	public List<Object[]> findSchedules(Date dateFrom, List<String> providerIds) {
		String sql = "FROM ScheduleTemplate st, ScheduleDate sd " +
				"WHERE st.id.name = sd.hour " +
				"AND sd.date >= :dateFrom " +
				"AND sd.providerNo in ( :providerIds ) " +
				"AND sd.status = 'A' " +
				"AND (" +
				"	st.providerNo = sd.providerNo " +
				"	OR st.providerNo = 'Public' " +
				") ORDER BY sd.date";
		Query query = entityManager.createQuery(sql);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("providerIds", providerIds);
		return query.getResultList();
    }

	public List<ScheduleTemplate> findByProviderNoAndName(String providerNo, String name) {
		Query query = entityManager.createQuery("SELECT e FROM ScheduleTemplate e WHERE e.id.providerNo=? and e.id.name=? ");
		query.setParameter(1, providerNo);
		query.setParameter(2, name);

        List<ScheduleTemplate> results = query.getResultList();
		return results;
	}
	
	public List<ScheduleTemplate> findByProviderNo(String providerNo) {
		Query query = entityManager.createQuery("SELECT e FROM ScheduleTemplate e WHERE e.id.providerNo=? order by e.id.name");
		query.setParameter(1, providerNo);
		
        List<ScheduleTemplate> results = query.getResultList();
		return results;
	}
	
	@NativeSql({"scheduletemplate", "scheduledate"})
	public List<Object> findTimeCodeByProviderNo(String providerNo, Date date) {
		String sql = "select timecode from scheduletemplate, (select hour from (select provider_no, hour, status from scheduledate where sdate = :date) as df where status = 'A' and provider_no= :providerNo) as hf where scheduletemplate.name=hf.hour and (scheduletemplate.provider_no= :providerNo or scheduletemplate.provider_no='Public')";
		Query query = entityManager.createNativeQuery(sql, modelClass);
		query.setParameter("date", date);
		query.setParameter("providerNo", providerNo);
		return query.getResultList();
	}
	
	//TODO:modelClass causing error on master record
	@NativeSql({"scheduletemplate", "scheduledate"})
	public List<Object> findTimeCodeByProviderNo2(String providerNo, Date date) {
		String sql = "select timecode from scheduletemplate, (select hour from (select provider_no, hour, status from scheduledate where sdate = :date) as df where status = 'A' and provider_no= :providerNo) as hf where scheduletemplate.name=hf.hour and (scheduletemplate.provider_no= :providerNo or scheduletemplate.provider_no='Public')";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("date", date);
		query.setParameter("providerNo", providerNo);
		List<Object> result = query.getResultList();
		if(result.size()>0)
			return query.getResultList();
		else return null;
	}
	
}
