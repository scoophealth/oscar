package org.oscarehr.survey.dao.oscar.hibernate;

import java.util.List;

import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OscarFormDAOHibernate extends HibernateDaoSupport implements
		OscarFormDAO {

	public void saveOscarForm(OscarForm form) {
		this.getHibernateTemplate().saveOrUpdate(form);
	}

	public void updateStatus(Long formId, Short status) {
		OscarForm form = getOscarForm(formId);
		if(form != null) {
			form.setStatus(status.shortValue());
		}
		saveOscarForm(form);
	}

	public OscarForm getOscarForm(Long formId) {
		return (OscarForm)this.getHibernateTemplate().get(OscarForm.class,formId);
	}

	public void saveOscarFormInstance(OscarFormInstance instance) {
		this.getHibernateTemplate().save(instance);
	}

	public void saveOscarFormData(OscarFormData data) {
		this.getHibernateTemplate().save(data);
	}

	public OscarFormInstance getOscarFormInstance(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ?, and f.clientId = ? order by dateCreated DESC",
				new Object[] {formId,clientId});
		if(result.size()>0) {
			return (OscarFormInstance)result.get(0);
		}
		return null;
	}

	public List getOscarForms(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ?, and f.clientId = ? order by dateCreated DESC",
				new Object[] {formId,clientId});
		return result;
	}

	public List getOscarFormsByClientId(Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.clientId = ?",clientId);
		return result;
	}

}
