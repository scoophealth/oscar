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

import javax.persistence.Query;

import org.oscarehr.PMmodule.model.VacancyTemplate;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyTemplateDao extends AbstractDao<VacancyTemplate> {

	public VacancyTemplateDao() {
		super(VacancyTemplate.class);
	}

	public void saveVacancyTemplate(VacancyTemplate obj) {
		persist(obj);
	}
	
	public void mergeVacancyTemplate(VacancyTemplate obj) {
		merge(obj);
	}
	
	public VacancyTemplate getVacancyTemplate(Integer templateId) {
		return find(templateId);
	}
	
	public List<VacancyTemplate> getVacancyTemplateByWlProgramId(Integer wlProgramId) {
		Query query = entityManager.createQuery("select x from VacancyTemplate x where x.wlProgramId=?");
		query.setParameter(1, wlProgramId);
		
		@SuppressWarnings("unchecked")
		List<VacancyTemplate> results = query.getResultList();
		
		
		return results;	
	}
	
	 public List<VacancyTemplate> getActiveVacancyTemplatesByWlProgramId(Integer wlProgramId) {
		Query query = entityManager.createQuery("select x from VacancyTemplate x where x.wlProgramId=? and x.active=?");
		query.setParameter(1, wlProgramId);
		query.setParameter(2, true);
		
		@SuppressWarnings("unchecked")
		List<VacancyTemplate> results = query.getResultList();
		
		return results;	
	}
}
