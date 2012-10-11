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

import org.oscarehr.common.model.WaitingList;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingListDao extends AbstractDao<WaitingList> {

	public WaitingListDao() {
		super(WaitingList.class);
	}

	/**
	 * Finds all waiting lists for the specified demographic 
	 * 
	 * @param demographicNo
	 * 		Demographic to find the lists for
	 * @return
	 * 		Returns a list of all matching {@link org.oscarehr.common.model.WaitingListName}, {@link org.oscarehr.common.model.WaitingList} pairs.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findByDemographic(Integer demographicNo) {
		Query query = entityManager.createQuery("FROM WaitingListName wn, WaitingList w WHERE wn.id = w.listId AND w.demographicNo = :demoNo AND w.isHistory != 'Y'");
		query.setParameter("demoNo", demographicNo);
		return query.getResultList();
	}
}
