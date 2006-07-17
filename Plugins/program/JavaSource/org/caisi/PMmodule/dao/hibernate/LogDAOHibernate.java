package org.caisi.PMmodule.dao.hibernate;

import org.caisi.PMmodule.dao.LogDAO;
import org.caisi.PMmodule.model.Log;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class LogDAOHibernate extends HibernateDaoSupport implements LogDAO {

	public void saveLog(Log log) {
		this.getHibernateTemplate().save(log);
	}

}
