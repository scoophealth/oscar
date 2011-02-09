package org.oscarehr.eyeform.dao;

import java.util.List;

import org.oscarehr.eyeform.model.TestBookRecord;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TestBookRecordDao extends HibernateDaoSupport {
	
	public void save(TestBookRecord obj) {		
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public List<TestBookRecord> getByAppointmentNo(int appointmentNo) {
		return getHibernateTemplate().find("from TestBookRecord t where t.appointmentNo = ?",appointmentNo);
	}
	
	
	public TestBookRecord find(int id) {
		return (TestBookRecord)getHibernateTemplate().get(TestBookRecord.class, id);
	}
}
