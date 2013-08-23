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

import org.oscarehr.common.model.RSchedule;
import org.springframework.stereotype.Repository;

@Repository(value="rScheduleDao")
public class RScheduleDao extends AbstractDao<RSchedule>{

	public RScheduleDao() {
		super(RSchedule.class);
	}

	public List<RSchedule> findByProviderAvailableAndDate(String providerNo, String available, Date sdate) {
		Query query = entityManager.createQuery("select s from RSchedule s where s.providerNo=? and s.available=? and s.sDate=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, available);
		query.setParameter(3, sdate);

		@SuppressWarnings("unchecked")
        List<RSchedule> results = query.getResultList();
		return results;
	}

	public List<RSchedule> findByProviderNoAndDates(String providerNo, Date apptDate) {
		Query query = entityManager.createQuery("select s from RSchedule s where s.providerNo=? and s.sDate <=? and s.eDate >=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, apptDate);
		query.setParameter(3, apptDate);

		@SuppressWarnings("unchecked")
        	List<RSchedule> results = query.getResultList();
		return results;
    	}
}
