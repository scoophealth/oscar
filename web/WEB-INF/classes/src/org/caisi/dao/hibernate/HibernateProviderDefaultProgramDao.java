package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.ProviderDefaultProgramDao;
import org.caisi.model.ProviderDefaultProgram;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateProviderDefaultProgramDao extends HibernateDaoSupport implements ProviderDefaultProgramDao
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
	.getLogger(HibernateProviderDefaultProgramDao.class);
	
	public List getProgramByProviderNo(String providerNo)
	{
		String q = "FROM ProviderDefaultProgram pdp WHERE pdp.providerNo=?";
		List rs = (List) getHibernateTemplate().find(q,providerNo);
		return rs;
	}

	public void setDefaultProgram(String providerNo, int programId)
	{
		List rs=getProgramByProviderNo(providerNo);
		ProviderDefaultProgram pdp;
		if (rs.size()==0) {
			pdp=new ProviderDefaultProgram();
			pdp.setProviderNo(providerNo);
			pdp.setSignnote(false);
		}else{
			pdp=(ProviderDefaultProgram) rs.get(0);
		}
		pdp.setProgramId(new Integer(programId));
		getHibernateTemplate().saveOrUpdate(pdp);
	}

	public List getProviderSig(String providerNo)
	{
		List rs = (List) getProgramByProviderNo(providerNo);
		return rs;
	}

	public void saveProviderDefaultProgram(ProviderDefaultProgram pdp)
	{
		getHibernateTemplate().saveOrUpdate(pdp);
		
	}

	public void toggleSig(String providerNo)
	{
		List list=getProgramByProviderNo(providerNo);
		ProviderDefaultProgram pdp=null;
		if (list.isEmpty()){
			pdp=new ProviderDefaultProgram();
			pdp.setProgramId(new Integer(0));
			pdp.setProviderNo(providerNo);
			pdp.setSignnote(false);
		}else{
			pdp=(ProviderDefaultProgram) list.get(0);
			pdp.setSignnote(!pdp.isSignnote());
		}
		saveProviderDefaultProgram(pdp);
	}

}
