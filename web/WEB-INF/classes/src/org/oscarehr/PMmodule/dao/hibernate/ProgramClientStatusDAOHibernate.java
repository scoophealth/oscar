package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.oscarehr.PMmodule.dao.ProgramClientStatusDAO;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramClientStatusDAOHibernate extends HibernateDaoSupport
		implements ProgramClientStatusDAO {

	private Log log = LogFactory.getLog(ProgramClientStatusDAOHibernate.class);

	
	public List<ProgramClientStatus> getProgramClientStatuses(Integer programId) {
		return this.getHibernateTemplate().find("from ProgramClientStatus pcs where pcs.programId=?",programId);
	}

	public void saveProgramClientStatus(ProgramClientStatus status) {
		this.getHibernateTemplate().saveOrUpdate(status);
	}
	
	public ProgramClientStatus getProgramClientStatus(String id) {
		return (ProgramClientStatus)this.getHibernateTemplate().get(ProgramClientStatus.class,new Integer(id));
	}

	public void deleteProgramClientStatus(String id) {
		this.getHibernateTemplate().delete(getProgramClientStatus(id));
	}
	
	public boolean clientStatusNameExists(Integer programId, String statusName) {
		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (statusName == null || statusName.length() <= 0) {
			throw new IllegalArgumentException();
		}

		Query query = getSession().createQuery("select pt.id from ProgramClientStatus pt where pt.programId = ? and pt.name = ?");
		query.setLong(0, programId.longValue());
		query.setString(1, statusName);

		List teams = query.list();

		if (log.isDebugEnabled()) {
			log.debug("teamNameExists: programId = " + programId + ", statusName = " + statusName + ", result = " + !teams.isEmpty());
		}

		return !teams.isEmpty();
	}
	
	public List getAllClientsInStatus(Integer programId, Integer statusId) {
		if (programId == null || programId <= 0) {
			throw new IllegalArgumentException();
		}

		if (statusId == null || statusId <= 0) {
			throw new IllegalArgumentException();
		}

		List results = this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.TeamId = ? and a.AdmissionStatus='current'", new Object[] { programId, statusId });

		if (log.isDebugEnabled()) {
			log.debug("getAdmissionsInTeam: programId= " + programId + ",statusId=" + statusId + ",# results=" + results.size());
		}

		return results;
	}
}
