package org.oscarehr.eyeform.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.EyeForm;
import org.springframework.stereotype.Repository;

@Repository
public class EyeFormDao extends AbstractDao<EyeForm> {

	public EyeFormDao() {
		super(EyeForm.class);
	}
	
	public void save(EyeForm obj) {		
		if(obj.getId()!=null && obj.getId().intValue()>0) {
			entityManager.merge(obj);
		} else {
			entityManager.persist(obj);
		}
	}
	
	public EyeForm getByAppointmentNo(int appointmentNo) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1");
		query.setParameter(1, appointmentNo);
		
		EyeForm eyeform = null;
	
		try {
			eyeform = (EyeForm)query.getSingleResult();
		} catch(NoResultException e) {}
	
		return eyeform;
		
	}
}
