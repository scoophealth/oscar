package org.oscarehr.common.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecObjPrivilege;
import org.springframework.stereotype.Repository;

@Repository
public class SecObjPrivilegeDao extends AbstractDao<SecObjPrivilege> {

	public SecObjPrivilegeDao() {
		super(SecObjPrivilege.class);
	}

	public List<SecObjPrivilege> findByObjectNames(Collection<String> objectNames) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.objectName IN (:obj) order by s.priority desc";

		Query query = entityManager.createQuery(sql);
		query.setParameter("obj",  objectNames);

		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}

	public List<SecObjPrivilege> findByRoleUserGroup(String roleUserGroup) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.roleUserGroup like ? order by s.id.roleUserGroup, s.id.objectName";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, roleUserGroup);
		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}

	public List<SecObjPrivilege> findByObjectName(String objectName) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.objectName like ? order by s.id.objectName, s.id.roleUserGroup";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, objectName);
		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}
}
