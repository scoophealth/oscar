package org.caisi.dao;

import org.hibernate.SessionFactory;

public class AbstractDao {

	protected SessionFactory sessionFactory = null;

	public SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}
}
