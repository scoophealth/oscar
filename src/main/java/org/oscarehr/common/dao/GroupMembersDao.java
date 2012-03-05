package org.oscarehr.common.dao;

import org.oscarehr.common.model.GroupMembers;
import org.springframework.stereotype.Repository;

@Repository
public class GroupMembersDao extends AbstractDao<GroupMembers>{

	public GroupMembersDao() {
		super(GroupMembers.class);
	}
}
