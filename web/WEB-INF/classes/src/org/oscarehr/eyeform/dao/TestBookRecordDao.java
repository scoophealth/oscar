package org.oscarehr.eyeform.dao;

import org.oscarehr.eyeform.model.TestBookRecord;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TestBookRecordDao extends HibernateDaoSupport {
	
	public void save(TestBookRecord obj) {		
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	/*
	public List<ProcedureBook> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ?",demographicNo);
	}
	*/
	
	public TestBookRecord find(int id) {
		return (TestBookRecord)getHibernateTemplate().get(TestBookRecord.class, id);
	}
}
