package org.oscarehr.common.dao;

import org.oscarehr.common.model.SurveyData;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyDataDao extends AbstractDao<SurveyData>{

	public SurveyDataDao() {
		super(SurveyData.class);
	}
}
