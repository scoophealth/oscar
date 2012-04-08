package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecPrivilege;
import org.springframework.stereotype.Repository;

@Repository
public class SecPrivilegeDao extends AbstractDao<SecPrivilege>{

	public SecPrivilegeDao() {
		super(SecPrivilege.class);
	}

	public List<SecPrivilege> findAll() {
		String sql = "SELECT s FROM SecPrivilege s order by s.id";

		Query query = entityManager.createQuery(sql);

		@SuppressWarnings("unchecked")
		List<SecPrivilege> result =  query.getResultList();

		return result;
	}
}
