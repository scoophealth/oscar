/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.GenericIntakeInstanceDAO;
import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of GenericIntakeInstanceDAO interface
 */
public class GenericIntakeInstanceDAOHibernate extends HibernateDaoSupport implements GenericIntakeInstanceDAO {
	
	private static final Log LOG = LogFactory.getLog(GenericIntakeInstanceDAOHibernate.class);
	
	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeInstanceDAO#getInstance(IntakeNode, java.lang.Integer)
	 */
	public IntakeInstance getInstance(IntakeNode node, Integer clientId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}
		
		List result = getHibernateTemplate().find("from IntakeInstance i where i.node = ? and i.clientId = ? order by i.createdOn desc", new Object[] { node, clientId });
		LOG.info("getEarliestInstance: " + result.size());
		
		return (result != null && result.size() > 0) ? (IntakeInstance) result.get(0) : null;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeInstanceDAO#saveIntakeInstance(org.oscarehr.PMmodule.model.IntakeInstance)
	 */
	public Integer saveIntakeInstance(IntakeInstance instance) {
		if (instance.getId() != null) {
			throw new IllegalArgumentException("Cannot update an existing instance (id: " + instance.getId() + ")");
		}
		
		Integer instanceId = (Integer) getHibernateTemplate().save(instance);
		LOG.info("saveIntakeInstance: " + instanceId);
		
		return instanceId;
	}

}