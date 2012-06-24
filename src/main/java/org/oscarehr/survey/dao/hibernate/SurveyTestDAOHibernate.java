/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.survey.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.oscarehr.survey.dao.SurveyTestDAO;
import org.oscarehr.survey.model.SurveyTestData;
import org.oscarehr.survey.model.SurveyTestInstance;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SurveyTestDAOHibernate extends HibernateDaoSupport implements
		SurveyTestDAO {

	public SurveyTestInstance getSurveyInstance(Long id) {
		return this.getHibernateTemplate().get(SurveyTestInstance.class,id);
	}

	public SurveyTestInstance getSurveyInstance(Long surveyId, Long clientId) {
		List results = this.getHibernateTemplate().find("from SurveyTestInstance s where s.surveyId = ? and s.clientId = ? order by s.dateCreated DESC", 
				new Object[] {surveyId,clientId});
		if(results.size()>0) {
			return (SurveyTestInstance)results.get(0);
		}
		return null;
	}

	public void saveSurveyInstance(SurveyTestInstance instance) {
		this.getHibernateTemplate().save(instance);
	}

	public void saveSurveyData(SurveyTestData data) {
		this.getHibernateTemplate().save(data);
	}
	
	public void clearTestData(Long surveyId) {
		List results = this.getHibernateTemplate().find("from SurveyTestInstance s where s.surveyId = ?",surveyId);
		for(Iterator iter=results.iterator();iter.hasNext();) {
			SurveyTestInstance instance = (SurveyTestInstance)iter.next();
			this.getHibernateTemplate().delete(instance);
		}
		
		
	}
}
