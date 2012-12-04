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

import javax.persistence.Query;

import org.oscarehr.common.model.SurveyData;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyDataDao extends AbstractDao<SurveyData>{

	public SurveyDataDao() {
		super(SurveyData.class);
	}

	public int getMaxProcessed(String surveyId) {
	    Query q = entityManager.createQuery("SELECT MAX(s.processed) FROM SurveyData s WHERE s.surveyId = :sid");
	    q.setParameter("sid", surveyId);
	    Object result = q.getSingleResult();
	    if (result == null) {
	    	return 0;
	    }
	    return (Integer)result;
    }

	public int getProcessCount(String surveyId) {
		String sql = "SELECT COUNT(s.id) FROM SurveyData s " +
				"WHERE s.surveyId = :sid " +
				"AND s.processed IS NULL " +
				"AND s.status = 'A'";
		Query query = entityManager.createQuery(sql);
		query.setParameter("sid", surveyId);
		Object result = query.getSingleResult();
	    if (result == null) {
	    	return 0;
	    }
	    return ((Long)result).intValue();	    
    }
	
	
}
