package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.SpecsHistory;
import org.springframework.stereotype.Repository;

@Repository
public class SpecsHistoryDao extends AbstractDao<SpecsHistory> {

	public SpecsHistoryDao() {
		super(SpecsHistory.class);
	}
	
	
	public List<SpecsHistory> getByDemographicNo(int demographicNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 order by x.date DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
	    
		@SuppressWarnings("unchecked")
	    List<SpecsHistory> results=query.getResultList();
	    return(results);	  
	}
	
	public List<SpecsHistory> getByDateRange(int demographicNo,Date startDate, Date endDate) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=? and x.date >= ? and x.date <=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
	    
		@SuppressWarnings("unchecked")
	    List<SpecsHistory> results=query.getResultList();
	    return(results);
	}
	
	public List<SpecsHistory> getHistory(int demographicNo,Date endDate,String status) {
		String sql=null;
		if(status != null) {
			sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.date <=? and x.status=? order by x.id desc";
		} else {
			sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.date <=? order by x.id desc";
		}			
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, endDate);
		if(status != null) {
			query.setParameter(3,status);
		}
		@SuppressWarnings("unchecked")
	    List<SpecsHistory> results=query.getResultList();
	    return(results);	
	}
	
	public List<SpecsHistory> getByAppointmentNo(int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1 order by x.date DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<SpecsHistory> results=query.getResultList();
	    return(results);		   
	}
	
	public List<SpecsHistory> getAllPreviousAndCurrent(int demographicNo, int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.appointmentNo<=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);	    
		query.setParameter(2, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<SpecsHistory> results=query.getResultList();
	    return(results);		
	}
	
	
}
