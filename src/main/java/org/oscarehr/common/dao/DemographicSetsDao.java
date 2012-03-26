package org.oscarehr.common.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicSets;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicSetsDao extends AbstractDao<DemographicSets>{

	public DemographicSetsDao() {
		super(DemographicSets.class);
	}

	public List<DemographicSets> findBySetName(String setName) {
		String sql = "select x from DemographicSets x where x.archive != ? and x.name=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "1");
		query.setParameter(2, setName);
		@SuppressWarnings("unchecked")
		List<DemographicSets> results = query.getResultList();
		return results;
	}

	public List<DemographicSets> findBySetNames(Collection<String> setNameList) {
		String sql = "select x from DemographicSets x where x.archive != :archive and x.name in (:nameList)";
		Query query = entityManager.createQuery(sql);
		query.setParameter("archive", "1");
		query.setParameter("nameList", setNameList);
		@SuppressWarnings("unchecked")
		List<DemographicSets> results = query.getResultList();
		return results;
	}

	public List<DemographicSets> findBySetNameAndEligibility(String setName, String eligibility) {
		String sql = "select x from DemographicSets x where x.name = ? and x.eligibility=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, setName);
		query.setParameter(2, eligibility);
		@SuppressWarnings("unchecked")
		List<DemographicSets> results = query.getResultList();
		return results;
	}

	public List<String> findSetNamesByDemographicNo(Integer demographicNo) {
		String sql = "select distinct(x.name) from DemographicSets x where x.archive = ? and x.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "1");
		query.setParameter(2, demographicNo);
		@SuppressWarnings("unchecked")
		List<String> results = query.getResultList();
		return results;
	}

	public List<String> findSetNames() {
		String sql = "select distinct(x.name) from DemographicSets x";
		Query query = entityManager.createQuery(sql);
		@SuppressWarnings("unchecked")
		List<String> results = query.getResultList();
		return results;
	}

	public List<DemographicSets> findBySetNameAndDemographicNo(String setName, int demographicNo) {
		String sql = "select x from DemographicSets x where x.name = ? and x.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, setName);
		query.setParameter(2, demographicNo);
		@SuppressWarnings("unchecked")
		List<DemographicSets> results = query.getResultList();
		return results;
	}
}
