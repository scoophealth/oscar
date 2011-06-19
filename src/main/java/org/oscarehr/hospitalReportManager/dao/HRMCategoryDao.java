package org.oscarehr.hospitalReportManager.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.springframework.stereotype.Repository;

@Repository
public class HRMCategoryDao extends AbstractDao<HRMCategory> {
	
	public HRMCategoryDao() {
	    super(HRMCategory.class);
    }

	public List<HRMCategory> findById(int id) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.id=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<HRMCategory> documents = query.getResultList();
		return documents;
	}
	
}
