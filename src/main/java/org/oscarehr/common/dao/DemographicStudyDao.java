package org.oscarehr.common.dao;


import javax.persistence.Query;

import org.oscarehr.common.model.DemographicStudy;
import org.oscarehr.common.model.DemographicStudyPK;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicStudyDao extends AbstractDao<DemographicStudy>{

	public DemographicStudyDao() {
		super(DemographicStudy.class);
	}

	public int removeByDemographicNo(Integer demographicNo) {
		Query query = entityManager.createQuery("delete x from DemographicStudy x where x.demographicNo=?");
		query.setParameter(1, demographicNo);
		return query.executeUpdate();
	}

	public DemographicStudy findByDemographicNoAndStudyNo(int demographicNo, int studyNo) {
		DemographicStudyPK pk = new DemographicStudyPK();
		pk.setDemographicNo(demographicNo);
		pk.setStudyNo(studyNo);

		return find(pk);
	}

}
