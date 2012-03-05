package org.oscarehr.common.dao;

import org.oscarehr.common.model.WaitingList;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingListDao extends AbstractDao<WaitingList>{

	public WaitingListDao() {
		super(WaitingList.class);
	}
}
