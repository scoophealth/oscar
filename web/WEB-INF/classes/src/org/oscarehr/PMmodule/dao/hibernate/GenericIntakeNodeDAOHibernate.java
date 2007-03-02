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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of GenericIntakeNodeDAO interface
 */
public class GenericIntakeNodeDAOHibernate extends HibernateDaoSupport implements GenericIntakeNodeDAO {
	
	private static final Log LOG = LogFactory.getLog(GenericIntakeNodeDAOHibernate.class);

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO#getIntakeNode(java.lang.Integer)
	 */
	public IntakeNode getIntakeNode(Integer intakeNodeId) {
		if (intakeNodeId == null || intakeNodeId < 1) {
			throw new IllegalArgumentException("intakeNodeId must be non-null and greater than 0");
		}
		
		IntakeNode intakeNode = (IntakeNode) getHibernateTemplate().load(IntakeNode.class, intakeNodeId);
		getChildren(intakeNode);
		
		LOG.info("getIntakeNode : " + intakeNodeId);

		return intakeNode;
	}
	
	private void getChildren(IntakeNode intakeNode) {
		HashSet<Integer> nodeIds = new HashSet<Integer>();
		nodeIds.add(intakeNode.getId());
		
		getChildren(nodeIds, intakeNode.getChildren());
	}

	private void getChildren(Set<Integer> nodeIds, List<IntakeNode> children) {
		for (IntakeNode child : children) {
			Integer childId = child.getId();
			
			if (nodeIds.contains(childId)) {
            	throw new IllegalStateException("intake node with id : " + childId + " is an ancestor of itself");
            } else {
            	nodeIds.add(childId);
            }
			
			// load children
			getChildren(nodeIds, child.getChildren());
        }
	}

}