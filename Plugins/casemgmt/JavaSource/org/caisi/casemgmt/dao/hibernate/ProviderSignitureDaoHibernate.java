package org.caisi.casemgmt.dao.hibernate;

import java.util.List;


import org.caisi.casemgmt.dao.ProviderSignitureDao;
import org.caisi.casemgmt.model.Providerext;
import org.caisi.casemgmt.model.ProviderDefaultProgram;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderSignitureDaoHibernate extends HibernateDaoSupport implements ProviderSignitureDao
{
		
	public boolean isOnSig(String providerNo)
	{
		String q = "FROM ProviderDefaultProgram pdp WHERE pdp.providerNo=?";
		List rs = (List) getHibernateTemplate().find(q,providerNo);
		boolean rt=false;
		if (!rs.isEmpty()) {
			ProviderDefaultProgram pdp=(ProviderDefaultProgram)rs.get(0);
			rt=pdp.isSignnote();
		}
		return rt;
	}
	
	public String getProviderSig(String providerNo)
	{
		String q = "FROM Providerext pet WHERE pet.providerNo=?";
		List rs = (List) getHibernateTemplate().find(q,providerNo);
		if (rs.isEmpty()) {
			return "";
		}
		Providerext pe=(Providerext) rs.get(0);
		String rt=pe.getSignature();
		if (rt==null) return "";
		return rt;
	}
	


}
