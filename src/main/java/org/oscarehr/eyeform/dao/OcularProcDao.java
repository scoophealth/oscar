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
	
	public List<OcularProc> getByDateRange(int demographicNo,Date startDate, Date endDate) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ? and op.date >= ? and op.date <=?",new Object[] {demographicNo,startDate,endDate});
	}
	
	public List<OcularProc> getHistory(int demographicNo,Date endDate,String status) {
		if(status != null)
			return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ? and op.date <=? and op.status=? order by op.id desc",new Object[] {demographicNo,endDate,status});
		else
			return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ? and op.date <=? order by op.id desc",new Object[] {demographicNo,endDate});
	}
	
	public List<OcularProc> getByAppointmentNo(int appointmentNo) {
		return getHibernateTemplate().find("from OcularProc op where op.appointmentNo = ?",appointmentNo);
	}
	
	public List<OcularProc> getAllPreviousAndCurrent(int demographicNo, int appointmentNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ? and op.appointmentNo <= ?",new Object[] {demographicNo,appointmentNo});
	}
	
	public OcularProc find(int id) {
		return (OcularProc)getHibernateTemplate().get(OcularProc.class, id);
	}
}
