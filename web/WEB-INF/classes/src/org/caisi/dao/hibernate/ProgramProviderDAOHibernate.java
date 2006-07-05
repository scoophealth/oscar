package org.caisi.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.ProgramProviderDAO;
import org.caisi.model.ProgramProvider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramProviderDAOHibernate extends HibernateDaoSupport implements
		ProgramProviderDAO {

	private Log log = LogFactory.getLog(ProgramProviderDAOHibernate.class);

	 
	public List getProgramProviders(Long programId) {
		return this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProgramId = ?",programId);
	}
	
	public List getProgramProvidersByProvider(Long providerNo) {
		return this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?",providerNo);
	}

	public ProgramProvider getProgramProvider(Long id) {
		return (ProgramProvider)this.getHibernateTemplate().get(ProgramProvider.class,id);
	}
	
	public ProgramProvider getProgramProvider(Long providerNo,Long programId) {
		List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId = ?", new Object[] {providerNo,programId});
		if(results.size()>0) {
			return (ProgramProvider)results.get(0);
		}
		return null;
	}
	

	public void saveProgramProvider(ProgramProvider pp) {
		this.getHibernateTemplate().saveOrUpdate(pp);
	}

	public void deleteProgramProvider(Long id) {
		this.getHibernateTemplate().delete(getProgramProvider(id));
	}

	public List getProgramProvidersInTeam(Long programId, Long teamId) {
		return this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProgramId = ? and pp.TeamId = ?",
				new Object[] {programId,teamId});
	}
	
	public List getProgramDomain(Long providerId) {
		return this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?",providerId);
	}
}
