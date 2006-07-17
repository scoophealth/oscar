package org.caisi.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.casemgmt.dao.ProviderCaisiRoleDAO;
import org.caisi.model.Role;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderCaisiRoleDAOHibernate extends HibernateDaoSupport implements ProviderCaisiRoleDAO
{

	public Role getProviderCaisiRole(String providerNo)
	{
		String sql="from CaisiRole p where p.provider_no=?";
		/*one provider only has one caisi role*/
		 List rs=getHibernateTemplate().find(sql, providerNo);
		 if (rs.size()!=0) return (Role)rs.get(0);
		 else return null;
	}
	public List getAllProviderCaisiRole(String providerNo)
	{
		String sql="select cr.name from Role cr,CaisiRole p where p.provider_no=? and cr.id=p.role_id";
		
		return getHibernateTemplate().find(sql, providerNo);
	}
	public List getAllCaisiRole()
	{
		String sql="select name from Role cr";

		return getHibernateTemplate().find(sql);
	}
	

}
