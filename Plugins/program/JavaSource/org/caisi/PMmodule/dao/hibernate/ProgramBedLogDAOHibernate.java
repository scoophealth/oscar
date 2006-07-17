package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.ProgramBedLogDAO;
import org.caisi.PMmodule.model.ProgramBedLog;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramBedLogDAOHibernate extends HibernateDaoSupport implements
		ProgramBedLogDAO {

	public void saveBedLog(ProgramBedLog pbl) {
		this.getHibernateTemplate().saveOrUpdate(pbl);
	}

	public ProgramBedLog getBedLog(Long id) {
		return (ProgramBedLog)this.getHibernateTemplate().get(ProgramBedLog.class,id);
	}

	public ProgramBedLog getBedLogByProgramId(Long programId) {
		List result = this.getHibernateTemplate().find("from ProgramBedLog pbl where pbl.ProgramId = ?",programId);
		if(result.size() > 0) {
			return (ProgramBedLog)result.get(0);
		}
		return null;
	}

	public List getBedLogStatuses(Long programId) {
		ProgramBedLog pbl = this.getBedLogByProgramId(programId);
		if(pbl != null) {
			return pbl.getStatuses();
		}
		return null;
	}
	
	public List getBedLogCheckTimes(Long programId) {
		ProgramBedLog pbl = this.getBedLogByProgramId(programId);
		if(pbl != null) {
			return pbl.getCheckTimes();
		}
		return null;	}

}
