package org.caisi.dao;

import java.util.List;

import org.caisi.model.IssueGroup;
import org.hibernate.Query;
import org.hibernate.Session;

public class IssueGroupDao extends AbstractDao {

	public IssueGroup find(int id) {

		Session session = sessionFactory.openSession();
		try {
			return((IssueGroup)session.get(IssueGroup.class, id));
		}
		finally {
			session.close();
		}
	}

    public List<IssueGroup> findAll() {

		Session session = sessionFactory.openSession();
		try {
			Query query=session.createQuery("from IssueGroup");
			
			@SuppressWarnings("unchecked")
			List<IssueGroup> results=query.list();
			
			return(results);
		}
		finally {
			session.close();
		}
	}

}
