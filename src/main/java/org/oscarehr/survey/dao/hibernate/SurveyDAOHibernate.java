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

import java.util.List;

import org.oscarehr.survey.dao.SurveyDAO;
import org.oscarehr.survey.model.Survey;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SurveyDAOHibernate extends HibernateDaoSupport implements SurveyDAO {

	public void saveSurvey(Survey survey) {
		this.getHibernateTemplate().saveOrUpdate(survey);
	}

	public Survey getSurvey(Long id) {
		return this.getHibernateTemplate().get(Survey.class,id);
	}

	public List getSurveys() {
		return this.getHibernateTemplate().find("from Survey");
	}

	public void deleteSurvey(Long id) {
		this.getHibernateTemplate().delete(getSurvey(id));
	}

	public Survey getSurveyByName(String name) {
		List result =  this.getHibernateTemplate().find("from Survey s where s.description = ?",name);
		if(result.size()>0) {
			return (Survey)result.get(0);
		}
		return null;
	}
}
