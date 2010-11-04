package org.oscarehr.eyeform.dao;

import java.util.List;

import org.oscarehr.eyeform.model.ProcedureBook;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProcedureBookDao extends HibernateDaoSupport {
	
	public void save(ProcedureBook obj) {		
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<ProcedureBook> getByAppointmentNo(int appointmentNo) {
		return getHibernateTemplate().find("from ProcedureBook p where p.appointmentNo = ?",appointmentNo);
	}
	
	
	public ProcedureBook find(int id) {
		return (ProcedureBook)getHibernateTemplate().get(ProcedureBook.class, id);
	}
}
