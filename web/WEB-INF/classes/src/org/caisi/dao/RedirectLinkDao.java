package org.caisi.dao;

import org.caisi.model.RedirectLink;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class RedirectLinkDao {

	private SessionFactory sessionFactory = null;

	public SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

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
}
