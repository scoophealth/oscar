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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.GroupNoteLink;
import org.springframework.stereotype.Repository;

@Repository
public class GroupNoteDao extends AbstractDao<GroupNoteLink> {

	public GroupNoteDao() {
		super(GroupNoteLink.class);
	}

	public List<GroupNoteLink> findLinksByDemographic(Integer demographicNo) {
		String sqlCommand = "select * from GroupNoteLink where demographicNo=?1 and active=true";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<GroupNoteLink> results=query.getResultList();
	
		return (results);
	}
	
	public List<GroupNoteLink> findLinksByDemographicSince(Integer demographicNo, Date lastDateUpdated) {
		String sqlCommand = "select * from GroupNoteLink where demographicNo=?1 and active=true and created > ?2";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, demographicNo);
		query.setParameter(2,lastDateUpdated);
		
		@SuppressWarnings("unchecked")
		List<GroupNoteLink> results=query.getResultList();
	
		return (results);
	}
	
	public List<GroupNoteLink> findLinksByNoteId(Integer noteId) {

		String sqlCommand = "select * from GroupNoteLink where noteId=?1 and active=true";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, noteId);
		
		@SuppressWarnings("unchecked")
		List<GroupNoteLink> results=query.getResultList();
	
		return (results);
	}
	
	public int getNumberOfLinksByNoteId(Integer noteId) {
		return this.findLinksByNoteId(noteId).size();
	}
}
