package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.GroupNoteLink;
import org.springframework.stereotype.Repository;

@Repository
public class GroupNoteDao extends AbstractDao<GroupNoteLink> {

	public GroupNoteDao() {
		super(GroupNoteLink.class);
	}

	public List<GroupNoteLink> findLinksByNoteId(Integer noteId) {

		String sqlCommand = "select * from GroupNoteLink where noteId=?1";

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