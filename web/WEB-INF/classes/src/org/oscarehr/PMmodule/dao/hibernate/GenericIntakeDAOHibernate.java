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
import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of GenericIntakeDAO interface
 */
public class GenericIntakeDAOHibernate extends HibernateDaoSupport implements GenericIntakeDAO {
	
	private static final Log LOG = LogFactory.getLog(GenericIntakeDAOHibernate.class);
	
	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getIntake(IntakeNode, java.lang.Integer)
	 */
	public Intake getIntake(IntakeNode node, Integer clientId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}
		
		List result = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? order by i.createdOn desc", new Object[] { node, clientId });
		LOG.info("getIntake: " + result.size());
		
		return (result != null && result.size() > 0) ? (Intake) result.get(0) : null;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#saveIntake(org.oscarehr.PMmodule.model.Intake)
	 */
	public Integer saveIntake(Intake intake) {
		if (intake.getId() != null) {
			throw new IllegalArgumentException("Cannot update an existing intake (id: " + intake.getId() + ")");
		}
		
		Integer intakeId = (Integer) getHibernateTemplate().save(intake);
		LOG.info("saveIntake: " + intakeId);
		
		return intakeId;
	}

}