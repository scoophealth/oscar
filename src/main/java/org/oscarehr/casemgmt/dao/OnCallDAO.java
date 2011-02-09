package org.oscarehr.casemgmt.dao;

import org.oscarehr.casemgmt.model.OnCallQuestionnaire;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OnCallDAO extends HibernateDaoSupport {

	public void save(OnCallQuestionnaire ocq) {
		this.getHibernateTemplate().save(ocq);
	}
}
