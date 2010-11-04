package org.oscarehr.eyeform.dao;

import java.util.List;

import org.oscarehr.eyeform.model.FollowUp;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class FollowUpDao extends HibernateDaoSupport {

	public void save(FollowUp obj) {		
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	/*
	public List<ProcedureBook> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ?",demographicNo);
	}
	*/
	
	public FollowUp find(int id) {
		return (FollowUp)getHibernateTemplate().get(FollowUp.class, id);
	}
	
	public List<FollowUp> getByAppointmentNo(int appointmentNo) {
		return this.getHibernateTemplate().find("FROM FollowUp fu WHERE fu.appointmentNo = ?",appointmentNo);
	}
}
