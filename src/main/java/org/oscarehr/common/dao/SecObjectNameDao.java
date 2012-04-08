package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecObjectName;
import org.springframework.stereotype.Repository;

@Repository(value="secObjectNameDaoJpa")
public class SecObjectNameDao extends AbstractDao<SecObjectName>{

	public SecObjectNameDao() {
		super(SecObjectName.class);
	}

	public List<SecObjectName> findAll() {
		String sql = "SELECT s FROM SecObjectName s order by s.id";

		Query query = entityManager.createQuery(sql);

		@SuppressWarnings("unchecked")
		List<SecObjectName> result =  query.getResultList();

		return result;
	}

	public List<String> findDistinctObjectNames() {
		String sql = "SELECT distinct(s.id) FROM SecObjectName s order by s.id";

		Query query = entityManager.createQuery(sql);

		@SuppressWarnings("unchecked")
		List<String> result =  query.getResultList();

		return result;
	}
}
