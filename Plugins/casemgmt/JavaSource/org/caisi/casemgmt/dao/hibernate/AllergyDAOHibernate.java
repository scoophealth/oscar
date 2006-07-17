package org.caisi.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.casemgmt.dao.AllergyDAO;
import org.caisi.casemgmt.model.Allergy;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AllergyDAOHibernate extends HibernateDaoSupport implements
		AllergyDAO {

	public Allergy getAllergy(Long allergyid) {
		return (Allergy)this.getHibernateTemplate().get(Allergy.class,allergyid);
	}

	public List getAllergies(String demographic_no) {
		return this.getHibernateTemplate().find("from Allergy a where a.demographic_no = ?",new Object[] {demographic_no});
	}

}
