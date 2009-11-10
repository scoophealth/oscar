package org.oscarehr.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * Hibernate Audit Interceptor
 */
public class AuditInterceptor extends EmptyInterceptor {

	private List<AuditStrategy> strategies;
	
	public AuditInterceptor() {
		strategies = new ArrayList<AuditStrategy>();
	}
	
	/**
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		
	    return false;
	}
	
	/**
	 * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		
		return false;
	}

	/**
	 * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
	}
	
	/**
	 * @see org.hibernate.EmptyInterceptor#postFlush(java.util.Iterator)
	 */
	@Override
	public void postFlush(Iterator entities) {
	}

}