package org.oscarehr.eyeform.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.springframework.stereotype.Repository;

@Repository
public class TestBookRecordDao extends AbstractDao<TestBookRecord> {
	
	public TestBookRecordDao() {
		super(TestBookRecord.class);
	}
	
	public void save(TestBookRecord obj) {		
		if(obj.getId()!=null && obj.getId().intValue()>0) {
			entityManager.merge(obj);
		} else {
			entityManager.persist(obj);
		}
	}
	
	
	public TestBookRecord find(int id) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.id=?1");
	    query.setParameter(1, id);
	    return(getSingleResultOrNull(query));
		//return (FollowUp)getHibernateTemplate().get(FollowUp.class, id);
	}
	
	public List<TestBookRecord> getByAppointmentNo(int appointmentNo) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1");
		query.setParameter(1, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<TestBookRecord> results=query.getResultList();
	    return(results);
	}

	public void deleteById(int id) {
		entityManager.remove(find(id));
	}
}
