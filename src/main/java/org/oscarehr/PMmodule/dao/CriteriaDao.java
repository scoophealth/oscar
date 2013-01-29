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

import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class CriteriaDao extends AbstractDao<Criteria> {

	public CriteriaDao() {
		super(Criteria.class);
	}

	public List<Criteria> getCriteriaByTemplateId(Integer templateId) {
		Query q = entityManager.createQuery("select c from Criteria c where c.templateId=?");
		q.setParameter(1, templateId);
		
		@SuppressWarnings("unchecked")
		List<Criteria> results = q.getResultList();
		
		return results;	
	}
	
	public Criteria getCriteriaByTemplateIdVacancyIdTypeId(Integer templateId, Integer vacancyId, Integer typeId) {
		if(templateId != null && vacancyId != null) {			
			Query q = entityManager.createQuery("select c from Criteria c where c.templateId=? and c.criteriaTypeId=? and c.vacancyId=?");
			q.setParameter(1, templateId);
			q.setParameter(2, typeId);
			q.setParameter(3, vacancyId);
			return this.getSingleResultOrNull(q);
		} else if(templateId == null && vacancyId != null) 	{	
			Query q = entityManager.createQuery("select c from Criteria c where c.templateId IS NULL and c.criteriaTypeId=? and c.vacancyId=?");
			q.setParameter(1, typeId);
			q.setParameter(2, vacancyId);
			return this.getSingleResultOrNull(q);
		} else if(templateId != null && vacancyId == null) 	{	
			Query q = entityManager.createQuery("select c from Criteria c where c.templateId=? and c.criteriaTypeId=? and c.vacancyId is null");
			q.setParameter(1, templateId);
			q.setParameter(2, typeId);
			return this.getSingleResultOrNull(q);
		} else {
			return null;
		}
		
		
	}
	
	public List<Criteria> getCriteriasByVacancyId(Integer vacancyId) {
		Query q = entityManager.createQuery("select c from Criteria c where c.vacancyId=?");
		q.setParameter(1, vacancyId);
		
		@SuppressWarnings("unchecked")
		List<Criteria> results = q.getResultList();
		
		return results;	
	}
	
	public List<Criteria> getRefinedCriteriasByVacancyId(Integer vacancyId) {
		Query q = entityManager.createQuery("select c from Criteria c where c.canBeAdhoc!=? and c.vacancyId=?");
		q.setParameter(1, 0);//canBeAdhoc=0 means don't appear in vacancy.
		q.setParameter(2, vacancyId);
		
		@SuppressWarnings("unchecked")
		List<Criteria> results = q.getResultList();
		
		return results;	
	}
	
	public List<Criteria> getRefinedCriteriasByTemplateId(Integer templateId) {
		Query q = entityManager.createQuery("select c from Criteria c where c.canBeAdhoc!=? and c.templateId=?");
		q.setParameter(1, 0); //canBeAdhoc=0 means don't appear in vacancy.
		q.setParameter(2, templateId);
		
		@SuppressWarnings("unchecked")
		List<Criteria> results = q.getResultList();
		
		return results;	
	}
	
	
}
