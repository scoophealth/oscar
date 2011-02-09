package org.oscarehr.eyeform.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.FollowUp;
import org.springframework.stereotype.Repository;

@Repository
public class FollowUpDao extends AbstractDao<FollowUp> {

	public FollowUpDao() {
		super(FollowUp.class);
	}
	
	public void save(FollowUp obj) {		
		if(obj.getId()!=null && obj.getId().intValue()>0) {
			entityManager.merge(obj);
		} else {
			entityManager.persist(obj);
		}
	}
	/*
	public List<ProcedureBook> getByDemographicNo(int demographicNo) {
		return getHibernateTemplate().find("from OcularProc op where op.demographicNo = ?",demographicNo);
	}
	*/
	
	public FollowUp find(int id) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.id=?1");
	    query.setParameter(1, id);
	    return(getSingleResultOrNull(query));
		//return (FollowUp)getHibernateTemplate().get(FollowUp.class, id);
	}
	
	public List<FollowUp> getByAppointmentNo(int appointmentNo) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1");
		query.setParameter(1, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<FollowUp> results=query.getResultList();
	    return(results);
	}
}
