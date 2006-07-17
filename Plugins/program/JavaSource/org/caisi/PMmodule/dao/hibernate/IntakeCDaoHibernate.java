package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.IntakeCDao;
import org.caisi.PMmodule.model.Formintakec;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IntakeCDaoHibernate extends HibernateDaoSupport implements
		IntakeCDao {

	public Formintakec getCurrentForm(String demographicNo) {
		List results = this.getHibernateTemplate().find("from Formintakec f where f.DemographicNo = ? order by FormEdited desc",demographicNo);
		if(results.size()>0) {
			return (Formintakec)results.get(0);
		}
		return null;
	}

	public Formintakec getCurrentForm(String firstName, String lastName) {
		List results = this.getHibernateTemplate().find("from Formintakec f where f.FirstName = ? and f.LastName = ? order by FormEdited desc",new Object[] {firstName,lastName});
		if(results.size()>0) {
			return (Formintakec)results.get(0);
		}
		return null;
	}

	public Formintakec getForm(Long id) {
		return (Formintakec)this.getHibernateTemplate().get(Formintakec.class,id);
	}

	public void saveForm(Formintakec form) {
		this.getHibernateTemplate().save(form);
	}

}
