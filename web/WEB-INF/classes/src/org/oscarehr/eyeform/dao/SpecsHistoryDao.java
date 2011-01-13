package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.eyeform.model.SpecsHistory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SpecsHistoryDao extends HibernateDaoSupport {

	public void save(SpecsHistory obj) {
		obj.setUpdateTime(new Date());
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<SpecsHistory> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from SpecsHistory sh where sh.demographicNo = ? order by sh.date desc",demographicNo);
	}
	
	public List<SpecsHistory> getByAppointmentNo(int appointmentNo) {
		return getHibernateTemplate().find("from SpecsHistory sh where sh.appointmentNo = ? order by sh.date desc",appointmentNo);
	}
	
	public List<SpecsHistory> getByDateRange(int demographicNo,Date startDate, Date endDate) {
		return getHibernateTemplate().find("from SpecsHistory sh where sh.demographicNo = ? and sh.date >= ? and sh.date <=?",new Object[] {demographicNo,startDate,endDate});
	}
	
	public SpecsHistory find(int id) {
		return (SpecsHistory)getHibernateTemplate().get(SpecsHistory.class, id);
	}
}
