package org.oscarehr.common.dao;

import org.oscarehr.common.model.StudyLogin;
import org.springframework.stereotype.Repository;

@Repository
public class StudyLoginDao extends AbstractDao<StudyLogin>{

	public StudyLoginDao() {
		super(StudyLogin.class);
	}
}
