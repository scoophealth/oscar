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

import org.oscarehr.common.model.SurveyTestInstance;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyTestInstanceDao extends AbstractDao<SurveyTestInstance>{

	public SurveyTestInstanceDao() {
		super(SurveyTestInstance.class);
	}
	
	public SurveyTestInstance getSurveyInstance(Integer surveyId, Integer clientId) {
		Query query = entityManager.createQuery("select s from SurveyTestInstance s where s.surveyId = ?1 and s.clientId = ?2 order by s.dateCreated DESC");
		query.setParameter(1,surveyId);
		query.setParameter(2, clientId);
		
		@SuppressWarnings("unchecked")
        List<SurveyTestInstance> results = query.getResultList();
		
		if(results.size()>0) {
			return results.get(0);
		}
		return null;
	}
	
	public void clearTestData(Integer surveyId) {
		Query query = entityManager.createQuery("select s from SurveyTestInstance s where s.surveyId = ?1");
		query.setParameter(1,surveyId);
		
		@SuppressWarnings("unchecked")
        List<SurveyTestInstance> results = query.getResultList();
		

		for(SurveyTestInstance instance:results) {
			remove(instance.getId());
		}
		
	}
}
