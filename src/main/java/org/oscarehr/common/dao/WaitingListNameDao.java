package org.oscarehr.common.dao;

import org.oscarehr.common.model.WaitingListName;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingListNameDao extends AbstractDao<WaitingListName>{

	public WaitingListNameDao() {
		super(WaitingListName.class);
	}
}
