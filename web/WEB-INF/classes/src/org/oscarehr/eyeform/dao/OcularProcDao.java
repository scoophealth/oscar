package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.eyeform.model.OcularProc;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OcularProcDao extends HibernateDaoSupport {

	public void save(OcularProc obj) {
		obj.setUpdateTime(new Date());
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<OcularProc> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ?",demographicNo);
	}
	
	public List<OcularProc> getByAppointmentNo(int appointmentNo) {
		return getHibernateTemplate().find("from OcularProc op where op.appointmentNo = ?",appointmentNo);
	}
	
	public OcularProc find(int id) {
		return (OcularProc)getHibernateTemplate().get(OcularProc.class, id);
	}
}
