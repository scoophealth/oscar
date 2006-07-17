package org.caisi.casemgmt.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.caisi.casemgmt.dao.CaseManagementIssueDAO;
import org.caisi.casemgmt.model.CaseManagementIssue;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementIssueDAOHibernate extends HibernateDaoSupport
		implements CaseManagementIssueDAO
{

	public List getIssuesByDemographic(String demographic_no)
	{
		return this.getHibernateTemplate().find(
				"from CaseManagementIssue cmi where cmi.demographic_no = ?",
				new Object[]
				{ demographic_no });
	}
	

	public List getIssuesByDemographicOrderActive(String demographic_no)
	{
		return this
				.getHibernateTemplate()
				.find(
						"from CaseManagementIssue cmi where cmi.demographic_no = ? order by cmi.resolved",
						new Object[]
						{ demographic_no });

	}

	public List getActiveIssuesByDemographic(String demographic_no)
	{
		return this
				.getHibernateTemplate()
				.find(
						"from CaseManagementIssue cmi where cmi.demographic_no = ? and cmi.resolved=false",
						new Object[]
						{ demographic_no });
	}

	public void deleteIssueById(CaseManagementIssue issue)
	{
		getHibernateTemplate().delete(issue);
		return;

	}

	public void saveAndUpdateCaseIssues(List issuelist)
	{
		Iterator itr = issuelist.iterator();
		while (itr.hasNext())
		{
			getHibernateTemplate().saveOrUpdate(
					(CaseManagementIssue) itr.next());
		}

	}

}
