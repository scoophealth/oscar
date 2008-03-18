package org.caisi.dao;

import java.util.List;

import org.caisi.model.RedirectLink;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RedirectLinkDao extends AbstractDao{

	public RedirectLink find(int id) {

		Session session = sessionFactory.openSession();
		try {
			return((RedirectLink)session.get(RedirectLink.class, id));
		}
		finally {
			session.close();
		}
	}

	public void save(RedirectLink redirectLink) {

		Session session = sessionFactory.openSession();
		try {
			Transaction tx=session.beginTransaction();
			session.save(redirectLink);
			tx.commit();
		}
		finally {
			session.close();
		}
	}


    public List<RedirectLink> findAll() {

		Session session = sessionFactory.openSession();
		try {
			Query query=session.createQuery("from RedirectLink");
			
			@SuppressWarnings("unchecked")
			List<RedirectLink> results=query.list();
			
			return(results);
		}
		finally {
			session.close();
		}
	}

}
