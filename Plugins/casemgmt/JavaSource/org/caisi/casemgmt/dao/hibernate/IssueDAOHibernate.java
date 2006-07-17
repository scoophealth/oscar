package org.caisi.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.casemgmt.dao.IssueDAO;
import org.caisi.casemgmt.model.Issue;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IssueDAOHibernate extends HibernateDaoSupport implements IssueDAO {

	public Issue getIssue(Long id) {
		return (Issue)this.getHibernateTemplate().get(Issue.class,id);
	}

	public List getIssues() {
		return this.getHibernateTemplate().find("from Issue");
	}

	public Issue findIssueByCode(String code) {
		List results = this.getHibernateTemplate().find("from Issue i where i.code = ?",new Object[] {code});
		if(results != null) {
			return (Issue)results.get(0);
		}
		return null;
	}

	public void saveIssue(Issue issue) {
		this.getHibernateTemplate().saveOrUpdate(issue);
	}

	public List findIssueBySearch(String search)
	{
		search="%"+search+"%";
		search=search.toLowerCase();
		String sql="from Issue i where lower(i.code) like ? or lower(i.description) like ?";
		return this.getHibernateTemplate().find(sql,new Object[]{search,search});
	}
}
