package org.caisi.dao;

import org.caisi.model.RedirectLink;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RedirectLinkDao {

	private SessionFactory sessionFactory = null;

	public SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
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
