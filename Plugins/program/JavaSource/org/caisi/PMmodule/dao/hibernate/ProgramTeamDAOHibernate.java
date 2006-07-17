package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.ProgramTeamDAO;
import org.caisi.PMmodule.model.ProgramTeam;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramTeamDAOHibernate extends HibernateDaoSupport implements
		ProgramTeamDAO {

	public List getProgramTeams(Long programId) {
		return this.getHibernateTemplate().find("from ProgramTeam tp where tp.ProgramId = ?",programId);
	}

	public ProgramTeam getProgramTeam(Long id) {
		return (ProgramTeam)this.getHibernateTemplate().get(ProgramTeam.class,id);
	}

	public void saveProgramTeam(ProgramTeam team) {
		this.getHibernateTemplate().saveOrUpdate(team);
	}

	public void deleteProgramTeam(Long id) {
		this.getHibernateTemplate().delete(getProgramTeam(id));
	}

	public boolean teamNameExists(Long programId, String teamName) {
		Query q = getSession().createQuery("select pt.id from ProgramTeam pt where pt.ProgramId = ? and pt.Name = ?");
		q.setLong(0,programId.longValue());
		q.setString(1,teamName);
		List results = q.list();
		if(results.size() > 0) {
			return true;
		}
		return false;
	}
}
