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
		return (Survey)this.getHibernateTemplate().get(Survey.class,id);
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
