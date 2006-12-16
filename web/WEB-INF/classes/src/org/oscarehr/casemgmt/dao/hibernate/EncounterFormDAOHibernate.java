package org.oscarehr.casemgmt.dao.hibernate;

import java.util.List;

import org.oscarehr.casemgmt.dao.EncounterFormDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EncounterFormDAOHibernate extends HibernateDaoSupport implements EncounterFormDAO
{

	public List getAllForms()
	{
		String sql="from Encounterform e order by e.formName";
		return getHibernateTemplate().find(sql);
	}

}
