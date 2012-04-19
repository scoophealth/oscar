/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.OtherId;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jason Gallagher
 */
@Repository
public class OtherIdDAO extends AbstractDao<OtherId> {

	/** Creates a new instance of UserPropertyDAO */
	public OtherIdDAO() {
		super(OtherId.class);
	}

	public OtherId getOtherId(Integer tableName, Integer tableId, String otherKey){
		return getOtherId(tableName,String.valueOf(tableId),otherKey);
	}

	public OtherId getOtherId(Integer tableName, String tableId, String otherKey){
		Query query = entityManager.createQuery("select o from OtherId o where o.tableName=? and o.tableId=? and o.otherKey=? and o.deleted=? order by o.id desc");
		query.setParameter(1, tableName);
		query.setParameter(2, tableId);
		query.setParameter(3, otherKey);
		query.setParameter(4, false);

		@SuppressWarnings("unchecked")
        List<OtherId> otherIdList = query.getResultList();

		return otherIdList.size()>0 ? otherIdList.get(0) : null;
	}

	public OtherId searchTable(Integer tableName, String otherKey, String otherValue){
		Query query = entityManager.createQuery("select o from OtherId o where o.tableName=? and o.otherKey=? and o.otherId=? and o.deleted=? order by o.id desc");
		query.setParameter(1, tableName);
		query.setParameter(2, otherKey);
		query.setParameter(3, otherValue);
		query.setParameter(4, false);

		@SuppressWarnings("unchecked")
        List<OtherId> otherIdList = query.getResultList();

		return otherIdList.size()>0 ? otherIdList.get(0) : null;
	}

	public void save(OtherId otherId) {
		if(otherId.getId() != null && otherId.getId().intValue() > 0 ) {
			merge(otherId);
		} else {
			persist(otherId);
		}
	}
}
