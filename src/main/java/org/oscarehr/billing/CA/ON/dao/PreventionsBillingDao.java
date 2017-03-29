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
package org.oscarehr.billing.CA.ON.dao;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.model.PreventionsBilling;
import org.springframework.stereotype.Repository;

@Repository
public class PreventionsBillingDao extends AbstractDao<PreventionsBilling>{

	public PreventionsBillingDao() {
		super(PreventionsBilling.class);
	}
	
	public PreventionsBilling getPreventionBillingByType(String preventType) {
		Query q = entityManager.createQuery("select pb from PreventionsBilling pb where pb.preventionType = ?1");
		q.setParameter(1, preventType);
		return getSingleResultOrNull(q);
	}
}
