package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.oscarehr.PMmodule.dao.ProgramFunctionalUserDAO;
import org.oscarehr.PMmodule.model.FunctionalUserType;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramFunctionalUserDAOHibernate extends HibernateDaoSupport
		implements ProgramFunctionalUserDAO {

	private static Log log = LogFactory.getLog(ProgramFunctionalUserDAOHibernate.class);

	
	public List getFunctionalUserTypes() {
		List results =  this.getHibernateTemplate().find("from FunctionalUserType");
		
		if(log.isDebugEnabled()) {
			log.debug("getFunctionalUserTypes: # of results=" + results.size());
		}
		return results;
	}

	public FunctionalUserType getFunctionalUserType(Long id) {
		if(id == null || id.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		FunctionalUserType result =  (FunctionalUserType)this.getHibernateTemplate().get(FunctionalUserType.class,id);

		if(log.isDebugEnabled()) {
			log.debug("getFunctionalUserType: id=" + id + ",found=" + (result!=null));
		}
		
		return result;
	}

	public void saveFunctionalUserType(FunctionalUserType fut) {
		if(fut == null) {
			throw new IllegalArgumentException();
		}
		
		this.getHibernateTemplate().saveOrUpdate(fut);
		
		if(log.isDebugEnabled()) {
			log.debug("saveFunctionalUserType:" + fut.getId());
		}
	}

	public void deleteFunctionalUserType(Long id) {
		if(id == null || id.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		this.getHibernateTemplate().delete(getFunctionalUserType(id));
		
		if(log.isDebugEnabled()) {
			log.debug("deleteFunctionalUserType:" + id);
		}
	}

	public List getFunctionalUsers(Long programId) {
		if(programId == null || programId.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		List results =  this.getHibernateTemplate().find("from ProgramFunctionalUser pfu where pfu.ProgramId = ?",programId);
		
		if(log.isDebugEnabled()) {
			log.debug("getFunctionalUsers: programId=" + programId + ",# of results=" + results.size());
		}
		return results;
	}

	public ProgramFunctionalUser getFunctionalUser(Long id) {
		if(id == null || id.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		ProgramFunctionalUser result = (ProgramFunctionalUser)this.getHibernateTemplate().get(ProgramFunctionalUser.class,id);
		
		if(log.isDebugEnabled()) {
			log.debug("getFunctionalUser: id=" + id + ",found=" + (result!=null));
		}
		
		return result;
	}

	public void saveFunctionalUser(ProgramFunctionalUser pfu) {
		if(pfu == null) {
			throw new IllegalArgumentException();
		}
		
		this.getHibernateTemplate().saveOrUpdate(pfu);
		
		if(log.isDebugEnabled()) {
			log.debug("saveFunctionalUser:" + pfu.getId());
		}
	}

	public void deleteFunctionalUser(Long id) {
		if(id == null || id.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		this.getHibernateTemplate().delete(getFunctionalUser(id));
		
		if(log.isDebugEnabled()) {
			log.debug("deleteFunctionalUser:" + id);
		}
	}

	public Long getFunctionalUserByUserType(Long programId, Long userTypeId) {
		if(programId == null || programId.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		if(userTypeId == null || userTypeId.intValue() <=0) {
			throw new IllegalArgumentException();
		}
		
		Long result = null;
		
		Query q = getSession().createQuery("select pfu.ProgramId from ProgramFunctionalUser pfu where pfu.ProgramId = ? and pfu.UserTypeId = ?");
		q.setLong(0,programId.longValue());
		q.setLong(1,userTypeId.longValue());
		List results = q.list();
		if(results.size() > 0) {
			result =  (Long)results.get(0);
		}
		
		if(log.isDebugEnabled()) {
			log.debug("getFunctionalUserByUserType: programId=" + programId + ",userTypeId=" + userTypeId + ",result=" + result);
		}
		
		return result;
	}
}
