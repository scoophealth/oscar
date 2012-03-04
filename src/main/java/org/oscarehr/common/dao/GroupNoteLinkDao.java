package org.oscarehr.common.dao;

import org.oscarehr.common.model.GroupNoteLink;
import org.springframework.stereotype.Repository;

@Repository
public class GroupNoteLinkDao extends AbstractDao<GroupNoteLink>{

	public GroupNoteLinkDao() {
		super(GroupNoteLink.class);
	}
}
