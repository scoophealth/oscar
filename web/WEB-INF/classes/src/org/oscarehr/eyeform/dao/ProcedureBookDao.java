package org.oscarehr.eyeform.dao;

import org.oscarehr.eyeform.model.ProcedureBook;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProcedureBookDao extends HibernateDaoSupport {
	
	public void save(ProcedureBook obj) {		
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	/*
	public List<ProcedureBook> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ?",demographicNo);
	}
	*/
	
	public ProcedureBook find(int id) {
		return (ProcedureBook)getHibernateTemplate().get(ProcedureBook.class, id);
	}
}
