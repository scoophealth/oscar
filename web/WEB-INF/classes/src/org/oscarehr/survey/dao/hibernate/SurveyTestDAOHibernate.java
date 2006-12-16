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
		return (SurveyTestInstance)this.getHibernateTemplate().get(SurveyTestInstance.class,id);
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
