package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Diseases;
import org.springframework.stereotype.Repository;

@Repository
public class DiseasesDao extends AbstractDao<Diseases>{

	public DiseasesDao() {
		super(Diseases.class);
	}

	public List<Diseases> findByDemographicNo(int demographicNo) {
		String sql = "select x from Diseases x where x.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		@SuppressWarnings("unchecked")
		List<Diseases> results = query.getResultList();
		return results;
	}

	public List<Diseases> findByIcd9(String icd9) {
		String sql = "select x from Diseases x where x.icd9Entry=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, icd9);
		@SuppressWarnings("unchecked")
		List<Diseases> results = query.getResultList();
		return results;
	}
}
