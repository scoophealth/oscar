package org.oscarehr.common.dao;

import org.oscarehr.common.model.Groups;
import org.springframework.stereotype.Repository;

@Repository
public class GroupsDao extends AbstractDao<Groups>{

	public GroupsDao() {
		super(Groups.class);
	}
}
