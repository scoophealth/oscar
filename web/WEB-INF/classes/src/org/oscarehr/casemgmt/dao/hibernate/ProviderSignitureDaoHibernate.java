package org.oscarehr.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.model.ProviderDefaultProgram;
import org.oscarehr.casemgmt.dao.ProviderSignitureDao;
import org.oscarehr.casemgmt.model.Providerext;
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
