package org.oscarehr.dx.dao;

import java.util.List;

import org.hibernate.Query;
import org.oscarehr.dx.model.DxResearch;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DxResearchDAO extends HibernateDaoSupport {

	public void save(DxResearch obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<DxResearch> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from DxResearch dx where dx.demographicNo = ?",demographicNo);
	}
	
	public boolean entryExists(int demographicNo, String codeType, String code) {
		List<DxResearch> results = getHibernateTemplate().find("FROM DxResearch dx WHERE dx.demographicNo=? and dx.codingSystem=? and dx.code=?",
				new Object[] {demographicNo,codeType,code});
		return !results.isEmpty();
	}
	
	public void removeAllAssociationEntries() {
		Query q = getSession().createQuery("DELETE from DxResearch dx WHERE dx.association=true");
		q.executeUpdate();
	}
}
