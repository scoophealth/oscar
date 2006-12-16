package org.oscarehr.PMmodule.dao.hibernate;

import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.LogDAO;
import org.oscarehr.PMmodule.model.Log;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class LogDAOHibernate extends HibernateDaoSupport implements LogDAO {

	private static org.apache.commons.logging.Log log = LogFactory.getLog(LogDAOHibernate.class);

	public void saveLog(Log logObj) {
		this.getHibernateTemplate().save(logObj);
	}

}
