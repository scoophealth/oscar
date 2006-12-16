package org.oscarehr.casemgmt.dao.hibernate;

import java.util.List;

import org.oscarehr.casemgmt.dao.MessagetblDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MessagetblDAOHibernate extends HibernateDaoSupport implements MessagetblDAO{

	public List getMsgByDemoNo(Integer demographicNo)
	{
		String sql="select mtbl from Messagetbl mtbl, Msgdemomap mdmap where mtbl.messageid=mdmap.messageID and mdmap.demographicNo=?";
		return getHibernateTemplate().find(sql,demographicNo);
	}

}
