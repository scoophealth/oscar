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

import javax.persistence.Query;

import org.oscarehr.common.model.ScheduleTemplateCode;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleTemplateCodeDao extends AbstractDao<ScheduleTemplateCode> {
	
	public ScheduleTemplateCodeDao() {
		super(ScheduleTemplateCode.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ScheduleTemplateCode> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}
		
	public ScheduleTemplateCode getByCode(char code) {
		Query query = entityManager.createQuery("select s from ScheduleTemplateCode s where s.code=?");
		query.setParameter(1, code);
		
		@SuppressWarnings("unchecked")
		List<ScheduleTemplateCode> results = query.getResultList();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
	
	//"select code, duration from scheduletemplatecode where bookinglimit > 0 and duration != ''"
	public List<ScheduleTemplateCode> findTemplateCodes() {
		Query query = entityManager.createQuery("select s from ScheduleTemplateCode s where s.bookinglimit > 0 and s.duration <>''");
		
		@SuppressWarnings("unchecked")
		List<ScheduleTemplateCode> results = query.getResultList();
		
		return results;
	}
	
	public ScheduleTemplateCode findByCode(String code) {
		Query query = entityManager.createQuery("select s from ScheduleTemplateCode s where s.code like ?");
		query.setParameter(1, code);
		
		@SuppressWarnings("unchecked")
		List<ScheduleTemplateCode> results = query.getResultList();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

}
