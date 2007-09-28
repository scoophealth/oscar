package org.oscarehr.common.dao;

import org.hibernate.SessionFactory;

public abstract class BaseAuditStrategy implements AuditStrategy {
	
	private SessionFactory sessionFactory;
	
	public BaseAuditStrategy() {
		registerEvents();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public abstract void registerEvents();
	
	public void saveAudits() {
	    // write audits
		// clear pending audits
	}

}