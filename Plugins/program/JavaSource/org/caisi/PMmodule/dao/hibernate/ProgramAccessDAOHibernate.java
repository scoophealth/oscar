package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.ProgramAccessDAO;
import org.caisi.PMmodule.model.AccessType;
import org.caisi.PMmodule.model.ProgramAccess;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramAccessDAOHibernate extends HibernateDaoSupport implements
		ProgramAccessDAO {

	public List getProgramAccesses(Long programId) {
		return this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ?",programId);
	}

	public ProgramAccess getProgramAccess(Long id) {
		return (ProgramAccess)this.getHibernateTemplate().get(ProgramAccess.class,id);
	}
	
	public ProgramAccess getProgramAccess(Long programId, Long accessTypeId) {
		List results =  this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ? and pa.AccessTypeId = ?", new Object[] {programId,accessTypeId});
		if(results.size()>0) {
			return (ProgramAccess)results.get(0);
		}
		return null;
	}

	public void saveProgramAccess(ProgramAccess pa) {
		this.getHibernateTemplate().saveOrUpdate(pa);
	}

	public void deleteProgramAccess(Long id) {
		this.getHibernateTemplate().delete(getProgramAccess(id));

	}
	public List getAccessTypes() {
		return this.getHibernateTemplate().find("from AccessType at");
	}
	public AccessType getAccessType(Long id) {
		return (AccessType)this.getHibernateTemplate().get(AccessType.class,id);
	}
}
