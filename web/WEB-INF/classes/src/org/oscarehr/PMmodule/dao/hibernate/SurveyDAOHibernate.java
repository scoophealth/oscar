package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.dao.SurveyDAO;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SurveyDAOHibernate extends HibernateDaoSupport implements
		SurveyDAO {

	public OscarForm getForm(Long formId) {
		return (OscarForm)this.getHibernateTemplate().get(OscarForm.class,formId);
	}

	public void saveFormInstance(OscarFormInstance instance) {
		this.getHibernateTemplate().save(instance);
	}

	public OscarFormInstance getLatestForm(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ? and f.clientId = ? order by f.dateCreated DESC",
				new Object[] {formId,clientId});
		if(result.size()>0) {
			return (OscarFormInstance)result.get(0);
		}
		return null;
	}

	public List getForms(Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.clientId = ? order by f.dateCreated DESC",clientId);
		return result;
	}

	public List getForms(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ?, and f.clientId = ? order by f.dateCreated DESC",
				new Object[] {formId,clientId});
		return result;
	}
	
	public void saveFormData(OscarFormData data) {
		this.getHibernateTemplate().save(data);
	}
	
	public List getAllForms() {
		return this.getHibernateTemplate().find("from OscarForm f where f.status = " + OscarForm.STATUS_ACTIVE);
	}

	public List getCurrentForms(String formId, List clients) {
		List results = new ArrayList();
		
		for(Iterator iter=clients.iterator();iter.hasNext();) {
			Demographic client = (Demographic)iter.next();
			OscarFormInstance ofi = getLatestForm(new Long(formId), new Long(client.getDemographicNo().longValue()));
			results.add(ofi);
		}
		
		return results;
	}
}
