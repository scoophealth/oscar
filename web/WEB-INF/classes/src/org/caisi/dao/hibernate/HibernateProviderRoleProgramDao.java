package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.ProviderRoleProgramDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateProviderRoleProgramDao extends HibernateDaoSupport implements ProviderRoleProgramDao
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
	.getLogger(HibernateProviderRoleProgramDao.class);

	public List getPrograms(String providerNo)
	{
		String q="select p from Program p, ProviderRoleProgram prp where prp.providerNo=? and prp.programId=p.id";
		List rs = (List) getHibernateTemplate().find(q,providerNo);
		return rs;
	}

}
