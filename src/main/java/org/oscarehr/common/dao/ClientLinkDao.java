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

import javax.persistence.Query;

import org.oscarehr.common.model.ClientLink;
import org.springframework.stereotype.Repository;

@Repository
public class ClientLinkDao extends AbstractDao<ClientLink> {
	
	public ClientLinkDao() {
		super(ClientLink.class);
	}

	/**
	 * @param facilityId can not be null
	 * @param clientId can not be null
	 * @param currentlyLinked can be null to return both active and inactive records
	 * @param type can be null to return all link types
	 */
	public List<ClientLink> findByFacilityIdClientIdType(Integer facilityId, Integer clientId, Boolean currentlyLinked, ClientLink.Type type) {
		// build sql string
		StringBuilder sqlCommand=new StringBuilder();
		sqlCommand.append("select x from ClientLink x where x.facilityId=?1 and x.clientId=?2");
		if (type!=null) sqlCommand.append(" and x.linkType=?3");
		if (currentlyLinked!=null)
		{
			if (currentlyLinked) sqlCommand.append(" and x.unlinkProviderNo is null");
			else sqlCommand.append(" and x.unlinkProviderNo is not null");
		}
		
		// set parameters
		Query query = entityManager.createQuery(sqlCommand.toString());
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		if (type!=null) query.setParameter(3, type);
		
		// run query
		@SuppressWarnings("unchecked")
		List<ClientLink> results = query.getResultList();

		return(results);
	}
}
