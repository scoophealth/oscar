package org.oscarehr.casemgmt.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.caisi.model.Role;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
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
	
	public List search(String search, List roles) {
		if(roles.size() == 0) {
			return new ArrayList();
		}
		
		StringBuffer buf = new StringBuffer();
		for(int x=0;x<roles.size();x++) {
			if(x!=0) {
				buf.append(",");
			}
			buf.append("\'" + StringEscapeUtils.escapeSql(((Role)roles.get(x)).getName()) + "\'");
		}
		String roleList = buf.toString();
		
		search="%"+search+"%";
		search=search.toLowerCase();
		String sql="from Issue i where (lower(i.code) like ? or lower(i.description) like ?) and i.role in (" + roleList + ")";
		System.out.println(sql);
		return this.getHibernateTemplate().find(sql,new Object[]{search,search});

	}
}
