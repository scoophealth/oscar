package org.oscarehr.common.dao;

import org.oscarehr.common.model.RehabStudy2004;
import org.springframework.stereotype.Repository;

@Repository
public class RehabStudy2004Dao extends AbstractDao<RehabStudy2004>{

	public RehabStudy2004Dao() {
		super(RehabStudy2004.class);
	}
}
