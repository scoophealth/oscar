/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.common.model.AbstractModel;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDao<T extends AbstractModel<?>> {
	protected Class<T> modelClass;

	@PersistenceContext
	protected EntityManager entityManager = null;

	protected AbstractDao(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * aka update
	 */
	public void merge(T o) {
		entityManager.merge(o);
	}

	/**
	 * aka create
	 */
	public void persist(T o) {
		entityManager.persist(o);
	}

	/**
	 * You can only remove attached instances.
	 */
	public void remove(T o) {
		entityManager.remove(o);
	}

	/**
	 * You can only refresh attached instances.
	 */
	public void refresh(T o) {
		entityManager.refresh(o);
	}

	public T find(Object id) {
		return (entityManager.find(modelClass, id));
	}

	public void remove(Object id) {
		T abstractModel = find(id);
		if (abstractModel != null) remove(abstractModel);
	}

	protected T getSingleResultOrNull(Query query) {
		query.setMaxResults(1);
		
		@SuppressWarnings("unchecked")
		List<T> results = query.getResultList();
		if (results.size() == 1) return (results.get(0));
		else if (results.size() == 0) return (null);
		else throw (new NonUniqueResultException("SingleResult requested but result was not unique : " + results.size()));
	}

	public int getCountAll() {
		// new JPA way of doing it, but our hibernate is too old or doesn't support primitives yet?
		// String sqlCommand="select count(*) from "+modelClass.getSimpleName();
		// Query query = entityManager.createNativeQuery(sqlCommand, Integer.class);
		// return((Integer)query.getSingleResult());

		String tableName = modelClass.getSimpleName();
		javax.persistence.Table t= modelClass.getAnnotation(javax.persistence.Table.class);
		if(t != null && t.name() != null && t.name().length()>0) {
			tableName = t.name();
		}

		// older hibernate work around
		String sqlCommand = "select count(*) from " + tableName;
		Query query = entityManager.createNativeQuery(sqlCommand);
		return (((Number) query.getSingleResult()).intValue());
	}

	/**
	 * Creates new string builder containing the base query with the specified select and alias strings
	 *
	 * @param select
	 * Select clause to be appended to the query. May be null
	 * @param alias
	 * Alias to be used for referencing the base entity class
	 * @return
	 * Returns the string buffer containing the base query
	 */
	protected StringBuilder getBaseQueryBuf(String select, String alias) {
		StringBuilder buf = new StringBuilder();
		if (select != null) {
			buf.append(select);
			buf.append(" ");
		}
		buf.append("FROM ");
		buf.append(getModelClassName());
		if (alias != null) buf.append(" AS ").append(alias).append(" ");
		return buf;
	}

	public Class<T> getModelClass() {
		return modelClass;
	}

	protected Query createQuery(String alias, String whereClause) {
		return createQuery(null, alias, whereClause);
	}

	/**
	 * Creates a query with the specified entity alias and where clause
	 *
	 * <p/>
	 *
	 * For example, invoking
	 *
	 * <pre>
	 * createQuery("select entity.id" "entity", "entity.propertyName like :propertyValue");
	 * </pre>
	 *
	 * would create query:
	 *
	 * <pre>
	 * SELECT entity.id FROM ModelClass AS entity WHERE entity.propertyName like :propertyValue
	 * </pre>
	 *
	 * @param select
	 * Select clause to be included in the query
	 * @param alias
	 * Alias to be included in the query
	 * @param whereClause
	 * Where clause to be included in the query
	 * @return
	 * Returns the query
	 */
	protected Query createQuery(String select, String alias, String whereClause) {
		StringBuilder buf = createQueryString(select, alias, whereClause);
		return entityManager.createQuery(buf.toString());
	}

	/**
	 * Creates query string for the specified alias and where clause
	 *
	 * @param select
	 * Select clause
	 * @param alias
	 * Alias to be included in the query
	 * @param whereClause
	 * Where clause to be included in the query
	 * @return
	 * Returns the query string
	 *
	 * @see #createQuery(String, String)
	 */
	protected StringBuilder createQueryString(String select, String alias, String whereClause) {
		StringBuilder buf = getBaseQueryBuf(select, alias);
		if (whereClause != null && !whereClause.isEmpty()) {
			buf.append("WHERE ");
			buf.append(whereClause);
		}
		return buf;
	}

	/**
	 * Gets name of the model class.
	 *
	 * @return
	 * Returns the class name without package prefix
	 */
	protected String getModelClassName() {
		return getModelClass().getSimpleName();
	}
}
