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

import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentArchiveDao extends AbstractDao<AppointmentArchive> {

	public AppointmentArchiveDao() {
		super(AppointmentArchive.class);
	}

	public AppointmentArchive archiveAppointment(Appointment appointment) {
		AppointmentArchive aa = new AppointmentArchive();
		BeanUtils.copyProperties(appointment, aa, new String[]{"id"});
		aa.setAppointmentNo(appointment.getId());
		persist(aa);
		return aa;
	}
	
	/**
	 * Results are ordered by updateDate.
	 * Note that the result size is limited so if you call this with a month timeframe, you may only receive 
	 * the first few days. The results are ordered by updateDateTime so you can call this again with the start
	 * time set to the largest updateDateTime you were returned last time. Note that you must call it with the largest
	 * and not the largest+1 because there can be duplicate entries at the same time. This means you need to de-duplicate
	 * the results yourself if that matters to you.
	 */
	public List<AppointmentArchive> findAllByUpdateDateRange(Date startDateInclusive, Date endDateExclusive) {
		
		String sql = "SELECT a FROM AppointmentArchive a WHERE a.updateDateTime >=?1 and a.updateDateTime<?2 order by a.updateDateTime";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, startDateInclusive);
		query.setParameter(2, endDateExclusive);
		setDefaultLimit(query);
		
		@SuppressWarnings("unchecked")
        List<AppointmentArchive> results = query.getResultList();
		return results;
	}

}
