package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.ProgramFunctionalUserDAO;
import org.caisi.PMmodule.model.FunctionalUserType;
import org.caisi.PMmodule.model.ProgramFunctionalUser;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramFunctionalUserDAOHibernate extends HibernateDaoSupport
		implements ProgramFunctionalUserDAO {

	public List getFunctionalUserTypes() {
		return this.getHibernateTemplate().find("from FunctionalUserType");
	}

	public FunctionalUserType getFunctionalUserType(Long id) {
		return (FunctionalUserType)this.getHibernateTemplate().get(FunctionalUserType.class,id);
	}

	public void saveFunctionalUserType(FunctionalUserType fut) {
		this.getHibernateTemplate().saveOrUpdate(fut);
	}

	public void deleteFunctionalUserType(Long id) {
		this.getHibernateTemplate().delete(getFunctionalUserType(id));
	}

	public List getFunctionalUsers(Long programId) {
		return this.getHibernateTemplate().find("from ProgramFunctionalUser pfu where pfu.ProgramId = ?",programId);
	}

	public ProgramFunctionalUser getFunctionalUser(Long id) {
		return (ProgramFunctionalUser)this.getHibernateTemplate().get(ProgramFunctionalUser.class,id);
	}

	public void saveFunctionalUser(ProgramFunctionalUser pfu) {
		this.getHibernateTemplate().saveOrUpdate(pfu);
	}

	public void deleteFunctionalUser(Long id) {
		this.getHibernateTemplate().delete(getFunctionalUser(id));
	}

	public Long getFunctionalUserByUserType(Long programId, Long userTypeId) {
		Query q = getSession().createQuery("select pfu.ProgramId from ProgramFunctionalUser pfu where pfu.ProgramId = ? and pfu.UserTypeId = ?");
		q.setLong(0,programId.longValue());
		q.setLong(1,userTypeId.longValue());
		List results = q.list();
		if(results.size() > 0) {
			return (Long)results.get(0);
		}
		return null;
	}
}
