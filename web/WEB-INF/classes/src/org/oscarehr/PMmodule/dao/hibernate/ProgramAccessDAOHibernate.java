package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramAccessDAOHibernate extends HibernateDaoSupport implements
		ProgramAccessDAO {
	
	private static Log log = LogFactory.getLog(ProgramAccessDAOHibernate.class);

	
	public List getProgramAccesses(Long programId) {
		
		if(programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}
		
		List results =  this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ?",programId);
		
		if(log.isDebugEnabled()) {
			log.debug("getProgramAccesses: programId=" + programId + ",# of results=" + results.size());
		}
		
		return results;
	}

	public ProgramAccess getProgramAccess(Long id) {
		
		if(id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}
		
		ProgramAccess result =  (ProgramAccess)this.getHibernateTemplate().get(ProgramAccess.class,id);
		
		if(log.isDebugEnabled()) {
			log.debug("getProgramAccess: id=" + id + ",found=" + (result!=null));
		}
		return result;
	}
	
	public ProgramAccess getProgramAccess(Long programId, Long accessTypeId) {
		if(programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}		
		if(accessTypeId == null || accessTypeId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}
		
		ProgramAccess result = null;
		List results =  this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ? and pa.AccessTypeId = ?", new Object[] {programId,accessTypeId});
		if(results.size()>0) {
			result =  (ProgramAccess)results.get(0);
		}
		
		if(log.isDebugEnabled()) {
			log.debug("getProgramAccess: programId=" + programId + ",accessTypeId=" + accessTypeId + ",found=" + (result!=null));
		}
		
		return result;
	}

	public void saveProgramAccess(ProgramAccess pa) {
		if(pa == null) {
			throw new IllegalArgumentException();
		}	
		
		this.getHibernateTemplate().saveOrUpdate(pa);
		
		if(log.isDebugEnabled()) {
			log.debug("saveProgramAccess:" + pa.getId());
		}
	}

	public void deleteProgramAccess(Long id) {
		if(id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}	
		
		this.getHibernateTemplate().delete(getProgramAccess(id));
		
		if(log.isDebugEnabled()) {
			log.debug("deleteProgramAccess:" + id);
		}

	}
	
	public List getAccessTypes() {
		List results = 	this.getHibernateTemplate().find("from AccessType at");
		
		if(log.isDebugEnabled()) {
			log.debug("getAccessTypes: # of results=" + results.size());
		}
		return results;
	}
	
	public AccessType getAccessType(Long id) {
		if(id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}	
		
		AccessType result = (AccessType)this.getHibernateTemplate().get(AccessType.class,id);
		
		if(log.isDebugEnabled()) {
			log.debug("getAccessType: id=" + id + ",found=" + (result!=null));
		}
		
		return result;
	}
}
