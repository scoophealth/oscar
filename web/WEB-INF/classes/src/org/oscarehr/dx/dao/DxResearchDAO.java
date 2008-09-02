package org.oscarehr.dx.dao;

import java.util.List;

import org.oscarehr.dx.model.DxResearch;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DxResearchDAO extends HibernateDaoSupport {

	public void save(DxResearch obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<DxResearch> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from DxResearch dx where dx.demographicNo = ?",demographicNo);
	}
}
