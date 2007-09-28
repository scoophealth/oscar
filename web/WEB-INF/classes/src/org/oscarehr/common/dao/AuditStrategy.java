package org.oscarehr.common.dao;

import java.io.Serializable;

public interface AuditStrategy {

	public void auditCreate(Object entity, Serializable id, Object[] state, String[] propertyNames);

	public void auditUpdate(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames);

	public void auditDelete(Object entity, Serializable id, Object[] state, String[] propertyNames);
	
	public void saveAudits();

}
