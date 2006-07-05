package org.caisi.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.SystemMessageDAO;
import org.caisi.model.SystemMessage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SystemMessageDAOHibernate extends HibernateDaoSupport implements
		SystemMessageDAO {

	private static Log log = LogFactory.getLog(SystemMessageDAOHibernate.class);
	
	public SystemMessage getMessage(Long id) {
		return (SystemMessage)this.getHibernateTemplate().get(SystemMessage.class,id);
	}
	
	public List getMessages() {
		return this.getHibernateTemplate().find("from SystemMessage sm order by sm.expiry_date desc");
	}
	
	public void saveMessage(SystemMessage mesg) {
		this.getHibernateTemplate().saveOrUpdate(mesg);
	}

}
