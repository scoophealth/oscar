package org.oscarehr.common.dao;

import org.oscarehr.common.model.StudyData;
import org.springframework.stereotype.Repository;

@Repository
public class StudyDataDao extends AbstractDao<StudyData>{

	public StudyDataDao() {
		super(StudyData.class);
	}

}
