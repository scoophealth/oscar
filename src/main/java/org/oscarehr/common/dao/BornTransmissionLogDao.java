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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Query;

import org.oscarehr.common.model.BornTransmissionLog;
import org.springframework.stereotype.Repository;

import oscar.util.UtilDateUtilities;

@Repository
public class BornTransmissionLogDao extends AbstractDao<BornTransmissionLog>{

	public BornTransmissionLogDao() {
		super(BornTransmissionLog.class);
	}
	
	public Long getSeqNoToday(String filenameStart, Integer id) {
		String today = UtilDateUtilities.getToday("yyyy-MM-dd");
		Date todayDate = UtilDateUtilities.StringToDate(today, "yyyy-MM-dd");
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(todayDate);
		cal.add(Calendar.DATE, 1);
		String tomorrow = UtilDateUtilities.DateToString(cal.getTime(), "yyyy-MM-dd");
		
		String sql = "select count(*) from BornTransmissionLog b" +
					 " where b.filename like '" + filenameStart + "%' and b.id < " + id +
					 " and b.submitDateTime >= '" + today + "' and b.submitDateTime < '" + tomorrow + "'";
		Query query = entityManager.createQuery(sql);

		return (Long) query.getSingleResult()+1;
	}
}
