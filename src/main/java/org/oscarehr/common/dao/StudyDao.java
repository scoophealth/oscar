package org.oscarehr.common.dao;

import org.oscarehr.common.model.Study;
import org.springframework.stereotype.Repository;

@Repository
public class StudyDao extends AbstractDao<Study>{

	public StudyDao() {
		super(Study.class);
	}
}
